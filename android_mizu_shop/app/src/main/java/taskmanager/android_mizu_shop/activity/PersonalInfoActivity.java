package taskmanager.android_mizu_shop.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.User;
import taskmanager.android_mizu_shop.api.ApiClient;
import taskmanager.android_mizu_shop.api.UserRepository;

public class PersonalInfoActivity extends AppCompatActivity {
    private ImageView ivAvatar, ivEdit, ivBack;
    private TextView tvUserName, tvEmail, tvPhone, tvAddress;
    private Button btnChangePassword, btnLogout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Ánh xạ view
        ivAvatar = findViewById(R.id.ivAvatar);
        ivEdit = findViewById(R.id.ivEdit);
        ivBack = findViewById(R.id.ivBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy thông tin user từ API
        loadUserInfoFromApi();

        ivBack.setOnClickListener(v -> finish());

        ivEdit.setOnClickListener(v -> {
            // Mở màn hình chỉnh sửa thông tin
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            //Intent sang ChangePasswordActivity
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        });


        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            prefs.edit().remove("token").remove("role").apply();

            Intent intent = new Intent(PersonalInfoActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }

    private void loadUserInfoFromApi() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        int userId = prefs.getInt("user_id", -1);

        UserRepository userRepo = ApiClient.getClient().create(UserRepository.class);
        Call<User> call = userRepo.getUserById(token, userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // Hiển thị lên view
                    tvUserName.setText(user.getUsername());
                    tvEmail.setText("Email: " + user.getEmail());
                    tvPhone.setText("Số điện thoại: " + user.getPhone());
                    tvAddress.setText("Địa chỉ: " + user.getAddress());
                    // Nếu có avatar là URL, dùng Glide/Picasso để load
                    // Glide.with(PersonalInfoActivity.this).load(user.getAvatar()).into(ivAvatar);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(PersonalInfoActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // load lại sdt và dia chi khi edit thanh cong
    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfoFromApi();
    }

} 