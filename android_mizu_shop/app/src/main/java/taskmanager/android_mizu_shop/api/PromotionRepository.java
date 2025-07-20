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
import taskmanager.android_mizu_shop.model.Promotion;
import taskmanager.android_mizu_shop.model.CreatePromotionRequest;

public interface PromotionRepository {
    @GET("/api/promotions")
    Call<List<Promotion>> getAllPromotions(@Header("Authorization") String token);

    @POST("/api/promotions")
    Call<Promotion> createPromotion(@Header("Authorization") String token, @Body CreatePromotionRequest request);

    @PUT("/api/promotions/{id}")
    Call<Promotion> updatePromotion(@Header("Authorization") String token, @Path("id") int id, @Body CreatePromotionRequest request);

    @DELETE("/api/promotions/{id}")
    Call<Void> deletePromotion(@Header("Authorization") String token, @Path("id") int id);
} 