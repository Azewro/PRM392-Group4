package taskmanager.android_mizu_shop.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;

import taskmanager.android_mizu_shop.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fabPersonalInfo);
        fab.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            Intent intent;
            if (token == null || token.isEmpty()) {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
            }
            startActivity(intent);
        });

        // --- FAB ADMIN ---
        FloatingActionButton fabAdmin = findViewById(R.id.fabAdmin);
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String role = prefs.getString("role", null);
        if ("admin".equals(role)) {
            fabAdmin.setVisibility(View.VISIBLE);
        } else {
            fabAdmin.setVisibility(View.GONE);
        }
        fabAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminManagementActivity.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}