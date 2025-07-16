package taskmanager.android_mizu_shop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.api.ApiClient;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.model.LoginRequest;
import taskmanager.android_mizu_shop.model.LoginResponse;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvError;
    private TextView tvBackToRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvError = findViewById(R.id.tvError);
        TextView tvBackHome = findViewById(R.id.tvBackHome);
        tvBackToRegister = findViewById(R.id.tvBackToRegister);

        etEmail.setHint("Username");
        etEmail.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvBackToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login() {
        String username = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        tvError.setVisibility(View.GONE);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            tvError.setText("Username và mật khẩu không được để trống");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(request);
        btnLogin.setEnabled(false);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    // Lưu token và role
                    saveTokenAndRole(response.body().getToken(), response.body().getUser() != null ? response.body().getUser().getRole() : null);
                    // Chuyển màn hình (ví dụ MainActivity)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tvError.setText("Đăng nhập thất bại. Kiểm tra lại thông tin.");
                    tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                tvError.setText("Lỗi kết nối server: " + t.getMessage());
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveTokenAndRole(String token, String role) {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        if (role != null) editor.putString("role", role);
        editor.apply();
    }
} 