package taskmanager.android_mizu_shop.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;

import taskmanager.android_mizu_shop.R;

public class PersonalInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            prefs.edit().remove("token").apply();
            Intent intent = new Intent(PersonalInfoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
} 