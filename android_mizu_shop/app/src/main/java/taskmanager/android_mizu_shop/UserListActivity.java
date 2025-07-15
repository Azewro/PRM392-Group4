package taskmanager.android_mizu_shop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private EditText etSearchUser;
    private Button btnAllUsers, btnAdmins, btnCustomers, btnBlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(new ArrayList<>(), (user, position) -> deleteUser(user, position), this);
        recyclerView.setAdapter(userAdapter);
        etSearchUser = findViewById(R.id.etSearchUser);
        btnAllUsers = findViewById(R.id.btnAllUsers);
        btnAdmins = findViewById(R.id.btnAdmins);
        btnCustomers = findViewById(R.id.btnCustomers);
        btnBlocked = findViewById(R.id.btnBlocked);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        btnAllUsers.setOnClickListener(v -> { setTabSelected(btnAllUsers); filterUsers(); });
        btnAdmins.setOnClickListener(v -> { setTabSelected(btnAdmins); filterUsers(); });
        btnCustomers.setOnClickListener(v -> { setTabSelected(btnCustomers); filterUsers(); });
        btnBlocked.setOnClickListener(v -> { setTabSelected(btnBlocked); filterUsers(); });
        setTabSelected(btnAllUsers);
        loadUsers();
    }

    private void setTabSelected(Button selected) {
        btnAllUsers.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnAdmins.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnCustomers.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnBlocked.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnAllUsers.setTextColor(0xFF888888);
        btnAdmins.setTextColor(0xFF888888);
        btnCustomers.setTextColor(0xFF888888);
        btnBlocked.setTextColor(0xFF888888);
        selected.setBackgroundResource(R.drawable.bg_tab_selected);
        selected.setTextColor(0xFF1976D2);
    }

    private void filterUsers() {
        String keyword = etSearchUser.getText().toString().toLowerCase().trim();
        Button selectedTab = null;
        if (btnAllUsers.getCurrentTextColor() == 0xFF1976D2) selectedTab = btnAllUsers;
        else if (btnAdmins.getCurrentTextColor() == 0xFF1976D2) selectedTab = btnAdmins;
        else if (btnCustomers.getCurrentTextColor() == 0xFF1976D2) selectedTab = btnCustomers;
        else if (btnBlocked.getCurrentTextColor() == 0xFF1976D2) selectedTab = btnBlocked;
        List<User> filtered = new ArrayList<>();
        for (User u : userAdapter.getFullUserList()) {
            boolean match = (u.getUsername() != null && u.getUsername().toLowerCase().contains(keyword))
                    || (u.getEmail() != null && u.getEmail().toLowerCase().contains(keyword));
            if (!match) continue;
            if (selectedTab == btnAllUsers) filtered.add(u);
            else if (selectedTab == btnAdmins && "admin".equalsIgnoreCase(u.getRole())) filtered.add(u);
            else if (selectedTab == btnCustomers && "customer".equalsIgnoreCase(u.getRole())) filtered.add(u);
            else if (selectedTab == btnBlocked && !u.isActive()) filtered.add(u);
        }
        userAdapter.filterList(filtered);
    }

    private void loadUsers() {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // Đổi nếu backend khác
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserRepository userRepo = retrofit.create(UserRepository.class);
        userRepo.getAllUsers("Bearer " + token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userAdapter = new UserAdapter(response.body(), (user, position) -> deleteUser(user, position), UserListActivity.this);
                    recyclerView.setAdapter(userAdapter);
                    filterUsers();
                } else {
                    Toast.makeText(UserListActivity.this, "Lỗi tải danh sách user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(UserListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser(User user, int position) {
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserRepository userRepo = retrofit.create(UserRepository.class);
        userRepo.deleteUser("Bearer " + token, user.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    userAdapter.removeUser(position);
                    Toast.makeText(UserListActivity.this, "Đã xoá user", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserListActivity.this, "Lỗi xoá user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 