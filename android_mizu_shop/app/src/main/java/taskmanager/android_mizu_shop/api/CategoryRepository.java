package taskmanager.android_mizu_shop.api;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;
import taskmanager.android_mizu_shop.model.Category;
import taskmanager.android_mizu_shop.model.Product;

public interface CategoryRepository {
    @GET("/api/categories")
    Call<List<Category>> getAllCategories(@Header("Authorization") String token);

    @Multipart
    @POST("/api/categories")
    Call<Category> createCategory(
        @Header("Authorization") String token,
        @Part("category") RequestBody categoryJson,
        @Part MultipartBody.Part imageUrl
    );
    @GET("/api/categories/{id}/products")
    Call<List<Product>> getProductsByCategory(
            @Header("Authorization") String token,
            @Path("id") int categoryId
    );

    @Multipart
    @PUT("/api/categories/{id}")
    Call<Category> updateCategory(
        @Header("Authorization") String token,
        @Path("id") int id,
        @Part("category") RequestBody categoryJson,
        @Part MultipartBody.Part imageUrl
    );

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Header("Authorization") String token, @Path("id") int id);
} 