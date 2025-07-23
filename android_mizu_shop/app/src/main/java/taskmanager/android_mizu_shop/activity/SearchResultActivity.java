package taskmanager.android_mizu_shop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import taskmanager.android_mizu_shop.adapter.SearchAdapter;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.ProductRepository;
import taskmanager.android_mizu_shop.model.Product;
//import taskmanager.android_mizu_shop.retrofit.RetrofitClient;
public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView rvResults;
    private SearchAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private EditText edtSearch;
    private ImageView btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_adokok);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(v -> finish());


        rvResults = findViewById(R.id.rvSearchResults);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);

        rvResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(this, productList, product -> {
            Intent intent = new Intent(SearchResultActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            startActivity(intent);
        });

        rvResults.setAdapter(adapter);

        String keyword = getIntent().getStringExtra("keyword");
        if (keyword != null) {
            edtSearch.setText(keyword);
            searchProduct(keyword);
        }

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchProduct(query);
            } else {
                Toast.makeText(this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProduct(String keyword) {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }

        ApiService.getProductRepository()
                .searchProductsByName(token, keyword)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            productList.clear();
                            productList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            productList.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(SearchResultActivity.this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(SearchResultActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
