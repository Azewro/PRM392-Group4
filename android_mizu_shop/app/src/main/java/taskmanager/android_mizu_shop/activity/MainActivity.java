package taskmanager.android_mizu_shop.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.BannerAdapter;
import taskmanager.android_mizu_shop.adapter.DealAdapter;
import taskmanager.android_mizu_shop.adapter.MenuAdapter;
import taskmanager.android_mizu_shop.api.ApiClient;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.CategoryRepository;
import taskmanager.android_mizu_shop.api.ProductRepository;
//import taskmanager.android_mizu_shop.model.DealItem;
import taskmanager.android_mizu_shop.model.Category;
import taskmanager.android_mizu_shop.model.DealItem;
import taskmanager.android_mizu_shop.model.MenuItem;
import taskmanager.android_mizu_shop.model.Product;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdmin, fabCart;

    private ViewPager2 viewPager;
    private BannerAdapter bannerAdapter;
    private List<Integer> bannerImages = Arrays.asList(
            R.drawable.ban2,
            R.drawable.ban1,
            R.drawable.ban3
    );
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        LinearLayout searchBar = findViewById(R.id.searchBar);
        EditText etSearch = findViewById(R.id.etSearch);
        ImageButton btnSearch = findViewById(R.id.btnSearch);

        View.OnClickListener searchClickListener = v -> {
            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
            startActivity(intent);
        };
        searchBar.setOnClickListener(searchClickListener);
        etSearch.setOnClickListener(searchClickListener);
        btnSearch.setOnClickListener(searchClickListener);

        FloatingActionButton fab = findViewById(R.id.fabPersonalInfo);
        fab.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            Intent intent;
            if (token == null || token.isEmpty()) {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
            }
            startActivity(intent);
        });

        fabAdmin = findViewById(R.id.fabAdmin);
        updateFabAdminVisibility();

        fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewPager2 bannerViewPager = findViewById(R.id.viewPagerBanner);


        viewPager = findViewById(R.id.viewPagerBanner);
        bannerAdapter = new BannerAdapter(bannerImages);
        viewPager.setAdapter(bannerAdapter);

        // Auto-scroll logic
        sliderRunnable = () -> {
            int nextPos = viewPager.getCurrentItem() + 1;
            if (nextPos >= bannerAdapter.getItemCount()) nextPos = 0;
            viewPager.setCurrentItem(nextPos, true);
        };

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000); // 3 giây tự động trượt
            }
        });




//        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
//        bannerViewPager.setAdapter(bannerAdapter);


        RecyclerView rvMenu = findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new GridLayoutManager(this, 4));

        CategoryRepository categoryRepo = ApiService.getCategoryRepository();
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) token = ""; //if null

        categoryRepo.getAllCategories("Bearer " + token).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MenuAdapter adapter = new MenuAdapter(MainActivity.this, response.body());
                    rvMenu.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Không tải được danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        RecyclerView rvDeals = findViewById(R.id.rvDeals);
        rvDeals.setLayoutManager(new LinearLayoutManager(this));

        ProductRepository productRepo = ApiService.getProductRepository();


        productRepo.getAllProducts(token).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> dealList = response.body();
                    DealAdapter dealAdapter = new DealAdapter(MainActivity.this, dealList);
//                    recyclerView.setAdapter(dealAdapter);

                    rvDeals.setAdapter(dealAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFabAdminVisibility();
    }

    private void updateFabAdminVisibility() {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String role = prefs.getString("role", null);

        if (role != null && role.trim().equalsIgnoreCase("admin")) {
            fabAdmin.setVisibility(View.VISIBLE);
            fabAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AdminManagementActivity.class);
                startActivity(intent);
            });
        } else {
            fabAdmin.setVisibility(View.GONE);
            fabAdmin.setOnClickListener(null); // Không nên để mở login khi user không phải admin
        }
    }

}