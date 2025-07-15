package taskmanager.android_mizu_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AdminManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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