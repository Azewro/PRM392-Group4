package taskmanager.android_mizu_shop.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import taskmanager.android_mizu_shop.model.LoginRequest;
import taskmanager.android_mizu_shop.model.LoginResponse;
import taskmanager.android_mizu_shop.model.CreateUserRequest;
import okhttp3.ResponseBody;

// Chỉ tạo mẫu, sẽ bổ sung chi tiết sau
public interface ApiService {
    // Đăng nhập
    @POST("api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // Đăng ký (xác thực email)
    @POST("api/users/register")
    Call<ResponseBody> register(@Body CreateUserRequest request);

    // Thêm hàm static để lấy PromotionRepository
    static PromotionRepository getPromotionRepository() {
        return ApiClient.getClient().create(PromotionRepository.class);
    }
} 