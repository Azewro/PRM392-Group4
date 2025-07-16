package taskmanager.android_mizu_shop.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import taskmanager.android_mizu_shop.model.UpdateUserProfileRequest;
import taskmanager.android_mizu_shop.User;

public interface UserRepository {
    @GET("/api/users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Header("Authorization") String token, @Path("id") int id);

    @PUT("/api/users/{id}/profile")
    Call<User> updateUser(@Header("Authorization") String token, @Path("id") int id, @Body UpdateUserProfileRequest request);
} 