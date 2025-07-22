package taskmanager.android_mizu_shop.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import taskmanager.android_mizu_shop.model.CartItem;

public interface CartRepository {
    @GET("/api/cart")
    Call<List<CartItem>> getCartItems(@Query("userId") int userId);

    @POST("/api/cart")
    Call<CartItem> addToCart(@Body CartItem cartItem);

    @PUT("/api/cart/{id}")
    Call<CartItem> updateCartItem(@Path("id") int cartItemId, @Body CartItem cartItem);

    @DELETE("/api/cart/{id}")
    Call<Void> removeFromCart(@Path("id") int cartItemId);
}

