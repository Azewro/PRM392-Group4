package taskmanager.android_mizu_shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.model.CreateUserRequest;
import taskmanager.android_mizu_shop.model.User;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etPhone, etAddress, etAvatar;
    private Button btnRegister;
    private TextView tvError, tvBackToLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etAvatar = findViewById(R.id.etAvatar);
        btnRegister = findViewById(R.id.btnRegister);
        tvError = findViewById(R.id.tvErrorRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnRegister.setOnClickListener(v -> register());
        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void register() {
        tvError.setVisibility(View.GONE);
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String avatar = etAvatar.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError("Vui lòng nhập đầy đủ username, email, password");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Email không hợp lệ");
            return;
        }
        if (password.length() < 6) {
            showError("Mật khẩu phải từ 6 ký tự trở lên");
            return;
        }
        // Có thể bổ sung validate phone, avatar nếu muốn

        CreateUserRequest request = new CreateUserRequest(username, email, password, phone, address, avatar);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        btnRegister.setEnabled(false);
        apiService.register(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                btnRegister.setEnabled(true);
                if (response.isSuccessful()) {
                    showError("Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.");
                } else {
                    String error = "";
                    try {
                        error = response.errorBody() != null ? response.errorBody().string() : "";
                    } catch (Exception e) {}
                    showError("Đăng ký thất bại: " + error);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btnRegister.setEnabled(true);
                showError("Lỗi kết nối server: " + t.getMessage());
            }
        });
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }
} 