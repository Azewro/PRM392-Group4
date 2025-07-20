package taskmanager.android_mizu_shop.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.CategoryAdapter;
import taskmanager.android_mizu_shop.model.Category;
import androidx.fragment.app.FragmentManager;
import taskmanager.android_mizu_shop.activity.CategoryFormBottomSheet;
import android.content.SharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.CategoryRepository;
import android.content.Intent;
import taskmanager.android_mizu_shop.activity.ProductListActivity;
import android.widget.ImageButton;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CategoryListActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener, CategoryAdapter.OnCategoryActiveListener {
    private RecyclerView rvCategoryList;
    private Button btnAddCategory;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private CategoryRepository categoryRepository;
    private String token;
    private ImageButton btnBackCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        rvCategoryList = findViewById(R.id.rvCategoryList);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnBackCategory = findViewById(R.id.btnBackCategory);
        btnBackCategory.setOnClickListener(v -> finish());

        categoryList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        token = prefs.getString("token", "");
        categoryRepository = ApiService.getCategoryRepository();
        loadCategories();

        adapter = new CategoryAdapter(categoryList, this);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryList.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            showCategoryForm(null);
        });
    }

    private void loadCategories() {
        categoryRepository.getAllCategories("Bearer " + token).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    adapter.setCategoryList(categoryList);
                } else {
                    Toast.makeText(CategoryListActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoryListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        // Mở ProductListActivity, truyền id category
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category_id", category.getId());
        startActivity(intent);
    }

    @Override
    public void onCategoryEdit(Category category) {
        showCategoryForm(category);
    }

    @Override
    public void onCategoryDelete(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa danh mục '" + category.getName() + "'?\nNếu còn sản phẩm sẽ không xóa được!")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (category.getProductCount() > 0) {
                        Toast.makeText(this, "Không thể xóa, còn sản phẩm!", Toast.LENGTH_SHORT).show();
                    } else {
                        categoryRepository.deleteCategory("Bearer " + token, category.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // Không xóa khỏi list, chỉ cập nhật trạng thái
                                    category.setIsActive(false);
                                    adapter.setCategoryList(categoryList);
                                    Toast.makeText(CategoryListActivity.this, "Đã ẩn danh mục!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CategoryListActivity.this, "Lỗi xóa danh mục", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(CategoryListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onCategoryActive(Category category) {
        // Gọi API updateCategory để active lại
        Category updated = new Category(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.getProductCount(),
                true // isActive
        );
        Gson gson = new Gson();
        String json = gson.toJson(updated);
        RequestBody categoryBody = RequestBody.create(json, MediaType.parse("application/json"));
        categoryRepository.updateCategory("Bearer " + token, category.getId(), categoryBody, null).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int idx = categoryList.indexOf(category);
                    if (idx >= 0) categoryList.set(idx, response.body());
                    adapter.setCategoryList(categoryList);
                    Toast.makeText(CategoryListActivity.this, "Đã kích hoạt lại danh mục!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CategoryListActivity.this, "Lỗi kích hoạt lại danh mục", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(CategoryListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCategoryForm(Category category) {
        FragmentManager fm = getSupportFragmentManager();
        CategoryFormBottomSheet bottomSheet = new CategoryFormBottomSheet(category, new CategoryFormBottomSheet.OnCategorySavedListener() {
            @Override
            public void onCategorySaved(Category newCategory) {
                Gson gson = new Gson();
                String json = gson.toJson(newCategory);
                RequestBody categoryBody = RequestBody.create(json, MediaType.parse("application/json"));
                // KHÔNG gửi file imageUrl nếu đã là Base64 string
                if (category == null) {
                    // Thêm mới
                    categoryRepository.createCategory("Bearer " + token, categoryBody, null).enqueue(new Callback<Category>() {
                        @Override
                        public void onResponse(Call<Category> call, Response<Category> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                categoryList.add(response.body());
                                adapter.setCategoryList(categoryList);
                                Toast.makeText(CategoryListActivity.this, "Đã thêm danh mục!", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = "";
                                try { error = response.errorBody() != null ? response.errorBody().string() : ""; } catch (Exception e) {}
                                Toast.makeText(CategoryListActivity.this, "Lỗi thêm danh mục: " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Category> call, Throwable t) {
                            Toast.makeText(CategoryListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Sửa
                    categoryRepository.updateCategory("Bearer " + token, category.getId(), categoryBody, null).enqueue(new Callback<Category>() {
                        @Override
                        public void onResponse(Call<Category> call, Response<Category> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int idx = categoryList.indexOf(category);
                                if (idx >= 0) categoryList.set(idx, response.body());
                                adapter.setCategoryList(categoryList);
                                Toast.makeText(CategoryListActivity.this, "Đã cập nhật danh mục!", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = "";
                                try { error = response.errorBody() != null ? response.errorBody().string() : ""; } catch (Exception e) {}
                                Toast.makeText(CategoryListActivity.this, "Lỗi cập nhật danh mục: " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Category> call, Throwable t) {
                            Toast.makeText(CategoryListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        bottomSheet.show(fm, "CategoryFormBottomSheet");
    }
} 