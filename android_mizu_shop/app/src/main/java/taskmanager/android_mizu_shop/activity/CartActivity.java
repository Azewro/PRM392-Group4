package taskmanager.android_mizu_shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import taskmanager.android_mizu_shop.api.CartRepository;
import taskmanager.android_mizu_shop.model.CartItem;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();
    private TextView textViewTotal;
    private Button buttonCheckout;
    private ImageButton buttonBack;

    private CartRepository cartRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotal = findViewById(R.id.textViewTotal);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonBack = findViewById(R.id.buttonBack);

//        // Sample hardcoded cart item
//        cartItemList = new ArrayList<>();
//
//        // Add a sample product
//        cartItemList.add(new CartItem(
//                1, 2, 1,
//                "Kem Dưỡng Ẩm Neutrogena Hydro Boost",
//                "Dưỡng ẩm, phục hồi da",
//                "https://cdn.tgdd.vn/Products/Images/5473/233951/serum-duong-am-neutrogena-hydro-boost-water-gel-50ml-8-1.jpg",
//                325000,
//                1,
//                "2025-05-22T12:40:00" // addedAt example
//        ));
//
//
//        // Add more products if you want!
//        cartItemList.add(new CartItem(
//                2,
//                2,
//                2,
//                "Sữa Rửa Mặt Hada Labo",
//                "Làm sạch sâu, dịu nhẹ",
//                "https://myphamthiennhien.vn/wp-content/uploads/2021/05/sua-rua-mat-hada-labo-advanced-nourish-hyaluronic-acid-face-wash-100g-2.jpg",
//                180000,
//                2,
//                "2025-07-22T10:00:00"
//
//        ));

        cartAdapter = new CartAdapter(cartItemList, new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onIncrease(CartItem item) {
                item.setQuantity(item.getQuantity() + 1);
                updateCartItemOnServer(item);
            }

            @Override
            public void onDecrease(CartItem item) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    updateCartItemOnServer(item);
                } else {
                    // Optional: confirm delete
                    removeCartItemOnServer(item);
                }
            }
        }, true);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Initialize repository
        cartRepository = ApiService.getCartRepository();

        buttonCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putParcelableArrayListExtra("cart", new ArrayList<>(cartItemList));  // Pass cart data
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> finish());

        loadCartItems();
    }

    private void loadCartItems() {
        int userId = getCurrentUserId();
        cartRepository.getCartItems(userId).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemList.clear();
                    cartItemList.addAll(response.body());
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                } else {
                    Toast.makeText(CartActivity.this, "Không lấy được giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartItemOnServer(CartItem item) {
        cartRepository.updateCartItem(item.getId(), item).enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful()) {
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                } else {
                    Toast.makeText(CartActivity.this, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeCartItemOnServer(CartItem item) {
        cartRepository.removeFromCart(item.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cartItemList.remove(item);
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                } else {
                    Toast.makeText(CartActivity.this, "Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi xoá sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        textViewTotal.setText("Tổng: " + String.format("%,.0f", total) + "đ");
    }

    // Replace this with your actual user-id fetching logic
    private int getCurrentUserId() {
        // Example: from SharedPreferences or LoginManager
        return 2; // hardcode for now, replace with actual user
    }
}
