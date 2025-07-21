package taskmanager.android_mizu_shop.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.User;
import taskmanager.android_mizu_shop.api.ApiClient;
import taskmanager.android_mizu_shop.api.UserRepository;
import taskmanager.android_mizu_shop.model.UpdateUserProfileRequest;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView ivBack, ivAvatar;
    private TextView tvUserName, tvEmail;
    private EditText edtPhone, edtAddress;
    private Button btnSave, btnCancel;

    private int userId;
    private String token;
    private String username, email, avatar, role;
    private boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Ánh xạ view
        ivBack = findViewById(R.id.ivBack);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Lấy userId và token từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        userId = prefs.getInt("userId", -1);

        // Lấy thông tin user từ API để hiển thị lên form
        loadUserInfo();

        // Nút back
        ivBack.setOnClickListener(v -> finish());

        // Nút hủy: Quay về trang thông tin tài khoản
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, PersonalInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Nút lưu thay đổi
        btnSave.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            // Kiểm tra trống
            if (phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra số điện thoại hợp lệ (bắt đầu bằng 0, 10 số)
            if (!phone.matches("0\\d{9}")) {
                Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo request update
            UpdateUserProfileRequest req = new UpdateUserProfileRequest(
                    username, phone, address, avatar, role, isActive
            );

            UserRepository userRepo = ApiClient.getClient().create(UserRepository.class);
            Call<User> call = userRepo.updateUser(token, userId, req);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        // Quay về trang thông tin tài khoản
                        Intent intent = new Intent(EditProfileActivity.this, PersonalInfoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadUserInfo() {
        UserRepository userRepo = ApiClient.getClient().create(UserRepository.class);
        Call<User> call = userRepo.getUserById(token, userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    username = user.getUsername();
                    email = user.getEmail();
                    avatar = user.getAvatar();
                    role = user.getRole();
                    isActive = true; // hoặc user.isActive() nếu có

                    tvUserName.setText(username);
                    tvEmail.setText("Email: " + email);
                    edtPhone.setText(user.getPhone());
                    edtAddress.setText(user.getAddress());
                    // Load avatar nếu có
                    // Glide.with(EditProfileActivity.this).load(avatar).into(ivAvatar);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}