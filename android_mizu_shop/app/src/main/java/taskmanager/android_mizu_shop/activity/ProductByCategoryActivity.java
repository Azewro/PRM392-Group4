package taskmanager.android_mizu_shop.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import taskmanager.android_mizu_shop.api.CategoryRepository;
import taskmanager.android_mizu_shop.model.Product;

public class ProductByCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductCateAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private CategoryRepository categoryRepository;
    private String token;
    private int categoryId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("auth_token", "");

        categoryId = getIntent().getIntExtra("categoryId", -1);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (categoryId != -1) {
            loadProductsByCategory(categoryId);
        }
    }

    private void loadProductsByCategory(int id) {
        categoryRepository = ApiClient.getClient().create(CategoryRepository.class);
        Call<List<Product>> call = categoryRepository.getProductsByCategory(token, id);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    productAdapter = new ProductCateAdapter( productList,ProductByCategoryActivity.this);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
