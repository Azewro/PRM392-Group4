package taskmanager.android_mizu_shop.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
import taskmanager.android_mizu_shop.model.CartItem;
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

    // Thêm hàm static để lấy CartRepository
    static CartRepository getCartRepository() {
        return ApiClient.getClient().create(CartRepository.class);
    }


    // Thêm hàm static để lấy PromotionRepository
    static PromotionRepository getPromotionRepository() {
        return ApiClient.getClient().create(PromotionRepository.class);
    }

    static CategoryRepository getCategoryRepository() {
        return ApiClient.getClient().create(CategoryRepository.class);
    }

    static ProductRepository getProductRepository() {
        return ApiClient.getClient().create(ProductRepository.class);
    }
} 