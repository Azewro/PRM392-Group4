package taskmanager.android_mizu_shop.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.ProductAdapter;
import taskmanager.android_mizu_shop.adapter.ProductCateAdapter;
import taskmanager.android_mizu_shop.api.ApiClient;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.CategoryRepository;
import taskmanager.android_mizu_shop.model.Product;

public class ProductByCategoryActivity extends AppCompatActivity {
    RecyclerView rvProductByCategory;
    TextView tvCategoryTitle;
    ProductCateAdapter adapter;
    List<Product> productList = new ArrayList<>();
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        rvProductByCategory = findViewById(R.id.rvProductByCategory);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        adapter = new ProductCateAdapter(this, productList);
        rvProductByCategory.setLayoutManager(new LinearLayoutManager(this));
        rvProductByCategory.setAdapter(adapter);

        categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryName = getIntent().getStringExtra("categoryName");
//        categoryId = getIntent().getIntExtra("categoryId", -1);
        Log.d("CATEGORY_ID", "ID: " + categoryId);

//        Log.d("CATEGORY_ID", "ID: " + categoryId);
        tvCategoryTitle.setText("Danh sách sản phẩm: " + categoryName);

        fetchProductsByCategory(categoryId);
    }

    private void fetchProductsByCategory(int categoryId) {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) token = ""; //if null


        ApiService.getProductRepository().getProductsByCategory("Bearer " + token, categoryId)
                    .enqueue(new Callback<List<Product>>() {
                        @Override
                        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                productList.clear();
                                productList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ProductByCategoryActivity.this, "2 Không có sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            Toast.makeText(ProductByCategoryActivity.this, "Lỗi gọi API", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

}
