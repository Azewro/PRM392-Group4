package taskmanager.android_mizu_shop.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.ReviewAdapter;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.model.Product;
import taskmanager.android_mizu_shop.model.Review;
import taskmanager.android_mizu_shop.model.ReviewRequest;
import android.content.SharedPreferences;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView tvProductName, tvPrice, tvStockStatus, tvQuantity, tvSelectedQuantity, tvTotal, tvTotal2;
    TextView btnMinus;
    ImageButton btnPlus;
    Button btnBuyNow;

    private RecyclerView rvFeedbackList;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();
    private EditText edtFeedbackContent;
    private RatingBar ratingBarFeedback;
    private Button btnSendFeedback;

    int quantity = 1;
    double unitPrice = 0; // Giá từng sản phẩm (theo đơn vị VNĐ)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_adokok);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(v -> finish());

        Product product = (Product) getIntent().getSerializableExtra("product");

        // Ánh xạ view
        imgProduct = findViewById(R.id.imgProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvStockStatus = findViewById(R.id.tvStock);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvSelectedQuantity = findViewById(R.id.tvSelectedQuantity);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotal2 = findViewById(R.id.tvTotal2);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        rvFeedbackList = findViewById(R.id.rvFeedbackList);
        reviewAdapter = new ReviewAdapter(reviewList);
        rvFeedbackList.setAdapter(reviewAdapter);
        rvFeedbackList.setLayoutManager(new LinearLayoutManager(this));

        edtFeedbackContent = findViewById(R.id.edtFeedbackContent);
        ratingBarFeedback = findViewById(R.id.ratingBarFeedback);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);


        if (product != null) {
            tvProductName.setText(product.getName());
            tvPrice.setText(product.getPrice() + " USD");
            tvStockStatus.setText("Tình trạng: " + (product.getIsActive() ? "Còn hàng" : "Hết hàng"));
            tvQuantity.setText("Số lượng còn: " + product.getStock());


            unitPrice = (product.getPrice().doubleValue() );


            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                try {
                    byte[] imageBytes = android.util.Base64.decode(product.getImageUrl(), android.util.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    if (bitmap != null) {
                        imgProduct.setImageBitmap(bitmap);
                    } else {
                        imgProduct.setImageResource(R.drawable.produc);
                    }
                } catch (Exception e) {
                    imgProduct.setImageResource(R.drawable.produc);
                }
            } else {
                imgProduct.setImageResource(R.drawable.produc);
            }

        }


        tvSelectedQuantity.setText(String.valueOf(quantity));
        updateTotal();


        if (product != null) {
            loadFeedbackList(product.getId());
        }


        btnSendFeedback.setOnClickListener(v -> {
            String content = edtFeedbackContent.getText().toString().trim();
            int rating = (int) ratingBarFeedback.getRating();
            if (content.isEmpty() || rating == 0) {
                Toast.makeText(this, "Vui lòng nhập nội dung và chọn số sao!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            int userId = prefs.getInt("user_id", -1);
            if (userId == -1) {
                Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            }
            int productId = product != null ? product.getId() : 0;
            ReviewRequest request = new ReviewRequest(userId, productId, rating, content);
            getApiService().addReview(request).enqueue(new Callback<Review>() {
                @Override
                public void onResponse(Call<Review> call, Response<Review> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        reviewList.add(0, response.body());
                        reviewAdapter.notifyItemInserted(0);
                        rvFeedbackList.scrollToPosition(0);
                        edtFeedbackContent.setText("");
                        ratingBarFeedback.setRating(0);
                        Toast.makeText(ProductDetailActivity.this, "Gửi phản hồi thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Lỗi gửi phản hồi! Mã lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<Review> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Gửi phản hồi thất bại! " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // Cộng
        btnPlus.setOnClickListener(v -> {
            if (product != null && quantity < product.getStock()) {
                quantity++;
                tvSelectedQuantity.setText(String.valueOf(quantity));
                updateTotal();
            }
        });

        //  trừ
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvSelectedQuantity.setText(String.valueOf(quantity));
                updateTotal();
            }
        });

    }


    private void updateTotal() {
        double total = quantity * unitPrice;
        String formatted = String.format("  Tổng: %.2fđ", total);
        tvTotal.setText(formatted);
        tvTotal2.setText(formatted);
    }

    private void loadFeedbackList(int productId) {
        getApiService().getReviewsByProduct(productId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                // Có thể hiển thị Toast hoặc log lỗi
            }
        });
    }

    private ApiService getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/api/") // Sửa lại baseUrl nếu cần
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {

            Toast.makeText(this, "Chia sẻ sản phẩm", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_favorite) {

            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
