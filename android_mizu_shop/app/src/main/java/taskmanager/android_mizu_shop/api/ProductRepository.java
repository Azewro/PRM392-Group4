package taskmanager.android_mizu_shop.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import taskmanager.android_mizu_shop.model.Product;

public interface ProductRepository {
    @GET("/api/products")
    Call<List<Product>> getAllProducts(@Header("Authorization") String token);

    @GET("/api/products/category/{categoryId}")
    Call<List<Product>> getProductsByCategory(@Header("Authorization") String token, @Path("categoryId") int categoryId);

    @POST("/api/products")
    Call<Product> createProduct(@Header("Authorization") String token, @Body Product product);

    @PUT("/api/products/{id}")
    Call<Product> updateProduct(@Header("Authorization") String token, @Path("id") int id, @Body Product product);

    @DELETE("/api/products/{id}")
    Call<Void> deleteProduct(@Header("Authorization") String token, @Path("id") int id);
} 