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
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
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
import taskmanager.android_mizu_shop.api.ProductRepository;
//import taskmanager.android_mizu_shop.model.DealItem;
import taskmanager.android_mizu_shop.model.DealItem;
import taskmanager.android_mizu_shop.model.MenuItem;
import taskmanager.android_mizu_shop.model.Product;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);

// Danh sách ảnh banner (bạn phải có các ảnh này trong res/drawable)
        List<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.ban1);
        bannerImages.add(R.drawable.banner1);
        bannerImages.add(R.drawable.banner1);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        // Menu fix cứng
        RecyclerView rvMenu = findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột

        List<MenuItem> menuList = new ArrayList<>();
        menuList.add(new MenuItem(R.drawable.iconskincare1, "Dưỡng Da"));
        menuList.add(new MenuItem(R.drawable.iconskincare2, "Son"));
        menuList.add(new MenuItem(R.drawable.iconskincare2, "Làm Đẹp"));
        menuList.add(new MenuItem(R.drawable.viewmore, "Xem thêm"));

        MenuAdapter menuAdapter = new MenuAdapter(menuList);
        rvMenu.setAdapter(menuAdapter);

        RecyclerView rvDeals = findViewById(R.id.rvDeals);
        rvDeals.setLayoutManager(new LinearLayoutManager(this));

        ProductRepository productRepo = ApiService.getProductRepository();
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) token = ""; // hoặc Bearer token nếu cần

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