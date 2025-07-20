package taskmanager.android_mizu_shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;
import taskmanager.android_mizu_shop.adapter.PromotionAdapter;
import taskmanager.android_mizu_shop.api.PromotionRepository;
import taskmanager.android_mizu_shop.model.Promotion;
import taskmanager.android_mizu_shop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;
import taskmanager.android_mizu_shop.api.ApiService;
import androidx.fragment.app.FragmentManager;
import taskmanager.android_mizu_shop.activity.PromotionFormBottomSheet;
import android.widget.ImageButton;

public class PromotionListActivity extends AppCompatActivity implements PromotionAdapter.OnPromotionClickListener {
    private RecyclerView rvPromotionList;
    private PromotionAdapter adapter;
    private List<Promotion> promotionList = new ArrayList<>();
    private PromotionRepository promotionRepository;
    private SearchView searchView;
    private TabLayout tabLayoutFilter;
    private FloatingActionButton fabAddPromotion;
    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_list);

        btnBack = findViewById(R.id.btnBackPromotion);
        btnBack.setOnClickListener(v -> finish());
        rvPromotionList = findViewById(R.id.rvPromotionList);
        searchView = findViewById(R.id.searchViewPromotion);
        tabLayoutFilter = findViewById(R.id.tabLayoutPromotionFilter);
        fabAddPromotion = findViewById(R.id.fabAddPromotion);

        adapter = new PromotionAdapter(promotionList, this);
        rvPromotionList.setLayoutManager(new LinearLayoutManager(this));
        rvPromotionList.setAdapter(adapter);

        promotionRepository = ApiService.getPromotionRepository();
        loadPromotions();

        fabAddPromotion.setOnClickListener(v -> openPromotionForm(null));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPromotions(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterPromotions(newText);
                return true;
            }
        });

        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Tất cả"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Đang hoạt động"));
        tabLayoutFilter.addTab(tabLayoutFilter.newTab().setText("Ngừng hoạt động"));
        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterByStatus(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadPromotions() {
        String token = getToken(); // TODO: Lấy token thực tế từ SharedPreferences
        promotionRepository.getAllPromotions("Bearer " + token).enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    promotionList.clear();
                    promotionList.addAll(response.body());
                    adapter.setPromotionList(new ArrayList<>(promotionList));
                } else {
                    Toast.makeText(PromotionListActivity.this, "Không lấy được danh sách khuyến mãi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Toast.makeText(PromotionListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getToken() {
        // TODO: Lấy token thực tế từ SharedPreferences
        return "";
    }

    private void filterPromotions(String query) {
        List<Promotion> filtered = new ArrayList<>();
        for (Promotion p : promotionList) {
            if (p.getCode() != null && p.getCode().toLowerCase().contains(query.toLowerCase())
                /* || (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase())) */) {
                filtered.add(p);
            }
        }
        adapter.setPromotionList(filtered);
    }

    private void filterByStatus(int tabPosition) {
        List<Promotion> filtered = new ArrayList<>();
        for (Promotion p : promotionList) {
            Boolean isActive = p.getIsActive() != null && p.getIsActive();
            if (tabPosition == 0 || (tabPosition == 1 && isActive) || (tabPosition == 2 && !isActive)) {
                filtered.add(p);
            }
        }
        adapter.setPromotionList(filtered);
    }

    private void openPromotionForm(@Nullable Promotion promotion) {
        PromotionFormBottomSheet.OnPromotionSavedListener listener = new PromotionFormBottomSheet.OnPromotionSavedListener() {
            @Override
            public void onPromotionSaved(Promotion savedPromotion) {
                loadPromotions();
            }
        };
        PromotionFormBottomSheet bottomSheet = new PromotionFormBottomSheet(promotion, listener);
        FragmentManager fm = getSupportFragmentManager();
        bottomSheet.show(fm, "PromotionFormBottomSheet");
    }

    // Xóa khuyến mãi
    private void deletePromotion(Promotion promotion) {
        String token = getToken();
        promotionRepository.deletePromotion("Bearer " + token, promotion.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PromotionListActivity.this, "Đã xóa khuyến mãi", Toast.LENGTH_SHORT).show();
                    loadPromotions();
                } else {
                    Toast.makeText(PromotionListActivity.this, "Không thể xóa khuyến mãi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PromotionListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPromotionClick(Promotion promotion) {
        openPromotionForm(promotion);
    }
} 