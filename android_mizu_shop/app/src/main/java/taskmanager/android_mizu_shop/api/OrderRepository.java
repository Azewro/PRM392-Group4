package taskmanager.android_mizu_shop.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import taskmanager.android_mizu_shop.model.Order;

public interface OrderRepository {

    // Fetch orders for a specific user (Order History)
    @GET("api/orders/user/{userId}")
    Call<List<Order>> getOrdersByUser(@Path("userId") int userId);

    // Place an order (sending Order data to the backend)
    @POST("api/orders")
    Call<Order> placeOrder(@Body Order order);
}
