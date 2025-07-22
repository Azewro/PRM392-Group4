package taskmanager.android_mizu_shop.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
    private Spinner spinnerCoupons;
    private List<Promotion> promotionList = new ArrayList<>();
    private ArrayAdapter<String> couponAdapter;
    private PromotionRepository promotionRepository;
    private double discountPercent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ArrayList<CartItem> cartItems = getIntent().getParcelableArrayListExtra("cart");
        if (cartItems != null) {
            // Use the cart data to populate RecyclerView or display items
        }

        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        textViewTotal = findViewById(R.id.textViewTotal);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        spinnerCoupons = findViewById(R.id.spinnerCoupons);
        buttonApplyCoupon = findViewById(R.id.buttonApplyCoupon);

        // TODO: Replace with your actual cart list (from backend/cart screen)
        cartItemList = getDemoCartItems();

        // Set up CartAdapter (hide +/− buttons)
        cartAdapter = new CartAdapter(cartItemList, null, false);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCheckout.setAdapter(cartAdapter);

        updateTotal();

        promotionRepository = ApiService.getPromotionRepository();
        loadPromotions();

        buttonApplyCoupon.setOnClickListener(v -> {
            String code = spinnerCoupons.getSelectedItem().toString();
            applyCoupon(code);
        });

        buttonCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
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
                            android.R.layout.simple_spinner_item, codes);
                    couponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCoupons.setAdapter(couponAdapter);  // Set the adapter to the Spinner
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

    // For demo/testing only. Replace with real data from backend/cart
    private List<CartItem> getDemoCartItems() {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(1, 2, 1, "Kem Dưỡng Ẩm Neutrogena Hydro Boost", "Dưỡng ẩm, phục hồi da",
                "https://cdn.tgdd.vn/Products/Images/5473/233951/serum-duong-am-neutrogena-hydro-boost-water-gel-50ml-8-1.jpg", 325000, 1, ""));
        list.add(new CartItem(2, 2, 2, "Sữa Rửa Mặt Hada Labo", "Làm sạch sâu, dịu nhẹ",
                "https://myphamthiennhien.vn/wp-content/uploads/2021/05/sua-rua-mat-hada-labo-advanced-nourish-hyaluronic-acid-face-wash-100g-2.jpg", 180000, 2, ""));
        list.add(new CartItem(3, 2, 3, "Serum Vitamin C Melano", "Làm sáng da, mờ thâm",
                "https://cdn.tgdd.vn/Products/Images/5473/236016/serum-duong-da-melano-cc-vitamin-c-20ml-1.jpg", 350000, 1, ""));
        return list;
    }
}
