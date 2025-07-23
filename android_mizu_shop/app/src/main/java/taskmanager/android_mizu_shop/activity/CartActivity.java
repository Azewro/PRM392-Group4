package taskmanager.android_mizu_shop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.adapter.CartAdapter;
import taskmanager.android_mizu_shop.model.CartItem;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();
    private TextView textViewTotal;
    private Button buttonCheckout;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotal = findViewById(R.id.textViewTotal);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonBack = findViewById(R.id.buttonBack);

        // Nhận giỏ hàng từ SharedPreferences
        cartItemList = getCartFromSharedPreferences();

        if (cartItemList != null && !cartItemList.isEmpty()) {
            // Nếu có giỏ hàng, hiển thị nó
            cartAdapter = new CartAdapter(cartItemList, new CartAdapter.OnQuantityChangeListener() {
                @Override
                public void onIncrease(CartItem item) {
                    item.setQuantity(item.getQuantity() + 1);
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                }

                @Override
                public void onDecrease(CartItem item) {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                        cartAdapter.notifyDataSetChanged();
                        updateTotal();
                    } else {
                        // Optional: confirm delete
                        cartItemList.remove(item);
                        cartAdapter.notifyDataSetChanged();
                        updateTotal();
                    }
                }
            });

            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCart.setAdapter(cartAdapter);

            updateTotal();
        } else {
            // Nếu không có giỏ hàng, thông báo cho người dùng
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
        }

        // Khi nhấn nút "Thanh toán"
        buttonCheckout.setOnClickListener(v -> {
            // Tiến hành thanh toán, hoặc chuyển qua màn hình CheckoutActivity
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putParcelableArrayListExtra("cart", new ArrayList<>(cartItemList));  // Truyền giỏ hàng
            startActivity(intent);
        });

        // Khi nhấn nút back
        buttonBack.setOnClickListener(v -> finish());
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        textViewTotal.setText("Tổng: " + String.format("%,.0f", total) + "đ");
    }

    // Lấy giỏ hàng từ SharedPreferences
    private List<CartItem> getCartFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        String json = prefs.getString("cartItems", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
