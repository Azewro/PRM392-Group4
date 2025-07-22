package taskmanager.android_mizu_shop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.ProductAdapter;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.ProductRepository;
import taskmanager.android_mizu_shop.model.Product;
import java.util.Collections;
import taskmanager.android_mizu_shop.model.Category;
import taskmanager.android_mizu_shop.activity.ProductFormBottomSheet;
import android.widget.ImageButton;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    private RecyclerView rvProductList;
    private Button btnAddProduct;
    private EditText etSearchProduct;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private List<Product> filteredList = new ArrayList<>();
    private ProductRepository productRepository;
    private String token;
    private int categoryId = -1;
    private List<Category> categoryList = new ArrayList<>();
    private ImageButton btnBackProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rvProductList = findViewById(R.id.rvProductList);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        etSearchProduct = findViewById(R.id.etSearchProduct);
        btnBackProduct = findViewById(R.id.btnBackProduct);
        btnBackProduct.setOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        token = prefs.getString("token", "");
        productRepository = ApiService.getProductRepository();

        if (getIntent() != null && getIntent().hasExtra("category_id")) {
            categoryId = getIntent().getIntExtra("category_id", -1);
        }

        adapter = new ProductAdapter(filteredList, this);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(adapter);

        btnAddProduct.setOnClickListener(v -> {
            loadCategoriesAndShowForm(null);
        });

        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadProducts();
    }

    private void loadProducts() {
        Call<List<Product>> call = (categoryId > 0)
                ? productRepository.getProductsByCategory("Bearer " + token, categoryId)
                : productRepository.getAllProducts("Bearer " + token);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    filterProducts(etSearchProduct.getText().toString());
                } else {
                    Toast.makeText(ProductListActivity.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            filteredList.clear();
            filteredList.addAll(productList);
        } else {
            filteredList.clear();
            for (Product p : productList) {
                if (p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(p);
                }
            }
        }
        adapter.setProductList(filteredList);
    }

    @Override
    public void onProductClick(Product product) {
        // TODO: Xem chi tiết sản phẩm hoặc mở form sửa
        Toast.makeText(this, "Click: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductEdit(Product product) {
        loadCategoriesAndShowForm(product);
    }

    @Override
    public void onProductDelete(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm '" + product.getName() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    productRepository.deleteProduct("Bearer " + token, product.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Không xóa khỏi list, chỉ cập nhật trạng thái
                                product.setIsActive(false);
                                filterProducts(etSearchProduct.getText().toString());
                                Toast.makeText(ProductListActivity.this, "Đã ẩn sản phẩm!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductListActivity.this, "Lỗi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ProductListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    public void onProductToggleActive(Product product) {
        boolean newActive = !product.getIsActive();
        if (!newActive) {
            // Đang active, xác nhận xóa
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa sản phẩm '" + product.getName() + "'?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        updateProductActiveState(product, false);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        } else {
            // Đang ẩn, xác nhận khôi phục
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận khôi phục")
                    .setMessage("Bạn có chắc muốn khôi phục sản phẩm '" + product.getName() + "'?")
                    .setPositiveButton("Khôi phục", (dialog, which) -> {
                        updateProductActiveState(product, true);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }

    private void updateProductActiveState(Product product, boolean newActive) {
        Integer categoryId = product.getCategoryId();
        if (categoryId == null && product.getCategoryName() != null) {
            for (Category c : categoryList) {
                if (product.getCategoryName().equals(c.getName())) {
                    categoryId = c.getId();
                    break;
                }
            }
        }
        if (categoryId == null) {
            Toast.makeText(this, "Không xác định được danh mục cho sản phẩm!", Toast.LENGTH_LONG).show();
            return;
        }
        Product updated = new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStock(),
                product.getVolume(),
                product.getAverageRating(),
                product.getCategoryName(),
                categoryId,
                newActive
        );
        productRepository.updateProduct("Bearer " + token, product.getId(), updated).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int idx = productList.indexOf(product);
                    if (idx >= 0) productList.set(idx, response.body());
                    filterProducts(etSearchProduct.getText().toString());
                    adapter.setProductList(filteredList);
                    Toast.makeText(ProductListActivity.this, newActive ? "Đã kích hoạt lại sản phẩm!" : "Đã ẩn sản phẩm!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductListActivity.this, "Lỗi cập nhật trạng thái sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategoriesAndShowForm(Product product) {
        // Gọi API lấy danh sách category để truyền vào form
        ApiService.getCategoryRepository().getAllCategories("Bearer " + token).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    showProductForm(product, categoryList);
                } else {
                    Toast.makeText(ProductListActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProductForm(Product product, List<Category> categories) {
        ProductFormBottomSheet bottomSheet = new ProductFormBottomSheet(product, categories, new ProductFormBottomSheet.OnProductSavedListener() {
            @Override
            public void onProductSaved(Product newProduct) {
                if (product == null) {
                    // Thêm mới
                    productRepository.createProduct("Bearer " + token, newProduct).enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                productList.add(response.body());
                                filterProducts(etSearchProduct.getText().toString());
                                Toast.makeText(ProductListActivity.this, "Đã thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductListActivity.this, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            Toast.makeText(ProductListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Sửa
                    productRepository.updateProduct("Bearer " + token, product.getId(), newProduct).enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int idx = productList.indexOf(product);
                                if (idx >= 0) productList.set(idx, response.body());
                                filterProducts(etSearchProduct.getText().toString());
                                Toast.makeText(ProductListActivity.this, "Đã cập nhật sản phẩm!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductListActivity.this, "Lỗi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            Toast.makeText(ProductListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        bottomSheet.show(getSupportFragmentManager(), "ProductFormBottomSheet");
    }
} 