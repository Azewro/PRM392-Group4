package taskmanager.android_mizu_shop.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import taskmanager.android_mizu_shop.adapter.OrderHistoryAdapter;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.model.Order;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrderHistory);
        orderHistoryAdapter = new OrderHistoryAdapter(orderList);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(orderHistoryAdapter);

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1); // Retrieve user ID from SharedPreferences

        if (userId != -1) {
            // Fetch orders for the user using OrderRepository
            fetchOrders(userId);
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOrders(int userId) {
        ApiService.getOrderRepository().getOrdersByUser(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderHistoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

