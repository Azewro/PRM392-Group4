package taskmanager.android_mizu_shop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import taskmanager.android_mizu_shop.R;

public class AdminManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⚠️ Check quyền admin trước khi set layout
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String role = prefs.getString("role", null);
        if (role == null || !role.equalsIgnoreCase("admin")) {
            Toast.makeText(this, "Bạn phải là ADMIN", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return;
        }

        // ✅ Nếu là admin → load layout như bình thường
        setContentView(R.layout.activity_admin_management);

        ImageButton btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            finish();
        });

        Button btnAccount = findViewById(R.id.btn_manage_accounts);
        Button btnProduct = findViewById(R.id.btn_manage_products);
        Button btnPromotion = findViewById(R.id.btn_manage_promotions);

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManagementActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        btnProduct.setOnClickListener(v -> {
            // TODO: Chuyển sang màn quản lý sản phẩm
        });
        btnPromotion.setOnClickListener(v -> {
            // TODO: Chuyển sang màn quản lý khuyến mãi
        });
    }
}
