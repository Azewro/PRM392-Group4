package taskmanager.android_mizu_shop;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Header;
import java.util.List;
import taskmanager.android_mizu_shop.model.LoginRequest;
import taskmanager.android_mizu_shop.model.LoginResponse;

// Chỉ tạo mẫu, sẽ bổ sung chi tiết sau
public interface ApiService {
    // Đăng nhập
    @POST("api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

} 