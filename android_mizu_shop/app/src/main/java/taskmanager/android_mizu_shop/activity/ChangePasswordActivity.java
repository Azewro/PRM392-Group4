package taskmanager.android_mizu_shop.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.api.ApiClient;
import taskmanager.android_mizu_shop.api.UserRepository;
import taskmanager.android_mizu_shop.model.ChangePasswordRequest;


public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword, btnCancel;
    private Drawable normalBg, errorBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnCancel = findViewById(R.id.btnCancel);
        ImageView ivBack = findViewById(R.id.ivBack);

        // nut back
        ivBack.setOnClickListener(v -> finish());

        // Lấy background mặc định và background lỗi
        normalBg = ContextCompat.getDrawable(this, R.drawable.bg_edittext_rounded);
        errorBg = ContextCompat.getDrawable(this, R.drawable.bg_edittext_error);

        btnChangePassword.setOnClickListener(v -> {
            // Reset viền về mặc định
            edtOldPassword.setBackground(normalBg);
            edtNewPassword.setBackground(normalBg);
            edtConfirmPassword.setBackground(normalBg);

            String oldPass = edtOldPassword.getText().toString().trim();
            String newPass = edtNewPassword.getText().toString().trim();
            String confirmPass = edtConfirmPassword.getText().toString().trim();

            boolean hasError = false;

            // 1. Kiểm tra trống
            if (oldPass.isEmpty()) {
                edtOldPassword.setBackground(errorBg);
                hasError = true;
            }
            if (newPass.isEmpty()) {
                edtNewPassword.setBackground(errorBg);
                hasError = true;
            }
            if (confirmPass.isEmpty()) {
                edtConfirmPassword.setBackground(errorBg);
                hasError = true;
            }
            if (hasError) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Kiểm tra mật khẩu mới hợp lệ
            if (!isValidPassword(newPass)) {
                edtNewPassword.setBackground(errorBg);
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt", Toast.LENGTH_LONG).show();
                return;
            }

            // 3. So sánh mật khẩu mới và xác nhận
            if (!newPass.equals(confirmPass)) {
                edtNewPassword.setBackground(errorBg);
                edtConfirmPassword.setBackground(errorBg);
                Toast.makeText(this, "Mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Gọi API đổi mật khẩu
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            String token = prefs.getString("token", "");
            int userId = prefs.getInt("userId", -1);

            ChangePasswordRequest req = new ChangePasswordRequest(oldPass, newPass, confirmPass);
            UserRepository userRepo = ApiClient.getClient().create(UserRepository.class);
            userRepo.changePassword(token, userId, req).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (response.code() == 400 || response.code() == 401) {
                        // Mật khẩu cũ không đúng (giả sử backend trả về 400/401)
                        edtOldPassword.setBackground(errorBg);
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    // Hàm kiểm tra mật khẩu mạnh
    private boolean isValidPassword(String password) {
        // Ít nhất 8 ký tự, có chữ hoa, chữ thường, số, ký tự đặc biệt
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}
