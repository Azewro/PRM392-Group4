package taskmanager.android_mizu_shop.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView tvProductName, tvPrice, tvStockStatus, tvQuantity, tvSelectedQuantity, tvTotal, tvTotal2;
    TextView btnMinus;
    ImageButton btnPlus;
    Button btnBuyNow;

    int quantity = 1;
    double unitPrice = 0; // Giá từng sản phẩm (theo đơn vị VNĐ)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_adokok);

        // Lấy dữ liệu từ Intent
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

        // Hiển thị thông tin sản phẩm
        if (product != null) {
            tvProductName.setText(product.getName());
            tvPrice.setText(product.getPrice() + " USD");
            tvStockStatus.setText("Tình trạng: " + (product.getIsActive() ? "Còn hàng" : "Hết hàng"));
            tvQuantity.setText("Số lượng còn: " + product.getStock());

            // Nếu product.getPrice() là USD (double), bạn cần convert sang VNĐ
            unitPrice = (product.getPrice().doubleValue() ); // ví dụ: 1 USD = 50.000đ

            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ban1)
                    .into(imgProduct);
        }

        // Hiển thị mặc định
        tvSelectedQuantity.setText(String.valueOf(quantity));
        updateTotal();

        // Nút cộng
        btnPlus.setOnClickListener(v -> {
            if (product != null && quantity < product.getStock()) {
                quantity++;
                tvSelectedQuantity.setText(String.valueOf(quantity));
                updateTotal();
            }
        });

        // Nút trừ
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
}
