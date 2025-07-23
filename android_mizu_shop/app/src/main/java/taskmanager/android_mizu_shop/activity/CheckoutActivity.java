package taskmanager.android_mizu_shop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.CartAdapter;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.PromotionRepository;
import taskmanager.android_mizu_shop.model.CartItem;
import taskmanager.android_mizu_shop.model.Promotion;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCheckout;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();
    private TextView textViewTotal;
    private Button buttonCheckout, buttonApplyCoupon;
    private ImageButton buttonBack;
    private AutoCompleteTextView spinnerCoupons;
    private List<Promotion> promotionList = new ArrayList<>();
    private ArrayAdapter<String> couponAdapter;
    private PromotionRepository promotionRepository;
    private double discountPercent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Lấy dữ liệu giỏ hàng từ Intent
        ArrayList<CartItem> cartItems = getIntent().getParcelableArrayListExtra("cart");

        // Kiểm tra nếu cartItems có hợp lệ không
        if (cartItems != null && !cartItems.isEmpty()) {
            cartItemList = cartItems;  // Sử dụng dữ liệu giỏ hàng để hiển thị
        } else {
            // Nếu không có dữ liệu từ Intent, khởi tạo giỏ hàng mới (trường hợp chưa có dữ liệu)
            cartItemList = new ArrayList<>();
            Toast.makeText(this, "Giỏ hàng rỗng, vui lòng thêm sản phẩm.", Toast.LENGTH_SHORT).show();
        }

        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        textViewTotal = findViewById(R.id.textViewTotal);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        spinnerCoupons = findViewById(R.id.spinnerCoupons);
        buttonBack = findViewById(R.id.buttonBack);

        // Set up CartAdapter (with product images)
        cartAdapter = new CartAdapter(cartItemList, new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onIncrease(CartItem item) {
                item.setQuantity(item.getQuantity() + 1);
                cartAdapter.notifyDataSetChanged();
                updateTotal();
            }

            public void onDecrease(CartItem item) {
                if (item.getQuantity() > 1) {  // Đảm bảo quantity không giảm dưới 1
                    item.setQuantity(item.getQuantity() - 1);
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                } else if (item.getQuantity() == 1) {
                    // Show confirmation dialog when quantity is reduced to 0
                    new AlertDialog.Builder(CheckoutActivity.this)
                            .setTitle("Xóa sản phẩm")
                            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Remove item from cart when user confirms
                                    cartItemList.remove(item);
                                    cartAdapter.notifyDataSetChanged();
                                    updateTotal();
                                    Toast.makeText(CheckoutActivity.this, "Sản phẩm đã bị xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
            }
        });
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCheckout.setAdapter(cartAdapter);

        updateTotal();

        promotionRepository = ApiService.getPromotionRepository();
        loadPromotions();

        buttonCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        });

        buttonBack.setOnClickListener(v -> {
            // Xóa danh sách cartItemList khi quay lại
            if (cartItemList != null) {
                cartItemList.clear();  // Xóa tất cả các phần tử trong giỏ hàng
            }
            // Go back to the ProductDetailActivity when back button is pressed
            Intent intent = new Intent(CheckoutActivity.this, ProductDetailActivity.class);
            startActivity(intent);
            finish(); // Close CheckoutActivity
        });

        spinnerCoupons.setOnItemClickListener((parent, view, position, id) -> {
            String couponCode = parent.getItemAtPosition(position).toString();
            applyCoupon(couponCode);
        });
    }

    private void loadPromotions() {
        // Get token from SharedPreferences
        String token = "Bearer " + getTokenFromPrefs(); // Get token from SharedPreferences

        // Fetch promotions from the backend using PromotionRepository
        promotionRepository.getAllPromotions(token).enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    promotionList.clear();
                    promotionList.addAll(response.body());  // Add the promotions fetched from backend
                    Log.d("PromotionData", "Response body: " + response.body().toString());

                    List<String> codes = new ArrayList<>();
                    for (Promotion p : promotionList) {
                        Log.d("PromotionData", "Promotion Code: " + p.getCode() + " isActive: " + p.getIsActive());
                        // Ensure that only active promotions are shown
                        if (p.getIsActive() != null && p.getIsActive()) {
                            codes.add(p.getCode());
                        }
                    }

                    Log.d("PromotionData", "Active Promotion Codes: " + codes.toString());

                    // Set up the adapter for the Spinner to display the codes
                    couponAdapter = new ArrayAdapter<>(CheckoutActivity.this,
                            android.R.layout.simple_dropdown_item_1line, codes); // CHANGE layout for AutoCompleteTextView
                    spinnerCoupons.setAdapter(couponAdapter);

                    // ✅ Force dropdown to show every time it's clicked
                    spinnerCoupons.setOnClickListener(v -> {
                        spinnerCoupons.showDropDown();
                    });
                } else {
                    Toast.makeText(CheckoutActivity.this, "Không lấy được mã giảm giá!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getTokenFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        return prefs.getString("token", "");
    }

    private void applyCoupon(String code) {
        Promotion appliedPromo = null;
        for (Promotion promo : promotionList) {
            if (promo.getCode().equalsIgnoreCase(code)) {
                appliedPromo = promo;
                break;
            }
        }
        if (appliedPromo != null) {
            discountPercent = appliedPromo.getDiscountPercent();
            Toast.makeText(this, "Áp dụng mã thành công! Giảm " + discountPercent + "%.", Toast.LENGTH_SHORT).show();
        } else {
            discountPercent = 0;
            Toast.makeText(this, "Mã không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        double discount = total * (discountPercent / 100.0);
        double totalAfterDiscount = total - discount;
        textViewTotal.setText("Tổng: " + String.format("%,.0f", totalAfterDiscount) + "đ");
    }

    private void displayImage(String imageUrl, ImageView imageView) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
