package taskmanager.android_mizu_shop;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetailBottomSheet extends BottomSheetDialogFragment {
    private User user;
    private OnUserUpdatedListener listener;

    public interface OnUserUpdatedListener {
        void onUserUpdated(User updatedUser);
    }

    public UserDetailBottomSheet(User user, OnUserUpdatedListener listener) {
        this.user = user;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_user_detail, container, false);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etAddress = view.findViewById(R.id.etAddress);
        Spinner spinnerRole = view.findViewById(R.id.spinnerRole);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        Button btnEdit = view.findViewById(R.id.btnEditUser);
        Button btnSave = view.findViewById(R.id.btnSaveUser);
        Button btnToggleActive = view.findViewById(R.id.btnToggleActive);

        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());
        tvStatus.setText(user.isActive() ? "Đang hoạt động" : "Đã khoá");

        // Setup spinner role
        String[] roles = {"admin", "customer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
        if (user.getRole() != null && user.getRole().equalsIgnoreCase("admin")) {
            spinnerRole.setSelection(0);
        } else {
            spinnerRole.setSelection(1);
        }

        setEditable(view, false);
        btnEdit.setOnClickListener(v -> setEditable(view, true));
        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Username và email không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            updateUser(username, email, phone, address, role, user.isActive());
        });
        btnToggleActive.setOnClickListener(v -> {
            boolean newActive = !user.isActive();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();
            updateUser(username, email, phone, address, role, newActive);
        });
        return view;
    }

    private void setEditable(View view, boolean editable) {
        view.findViewById(R.id.etUsername).setEnabled(editable);
        view.findViewById(R.id.etEmail).setEnabled(editable);
        view.findViewById(R.id.etPhone).setEnabled(editable);
        view.findViewById(R.id.etAddress).setEnabled(editable);
        view.findViewById(R.id.spinnerRole).setEnabled(editable);
        view.findViewById(R.id.btnSaveUser).setVisibility(editable ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.btnEditUser).setVisibility(editable ? View.GONE : View.VISIBLE);
        Button btnToggleActive = view.findViewById(R.id.btnToggleActive);
        btnToggleActive.setVisibility(editable ? View.VISIBLE : View.GONE);
        if (editable) {
            btnToggleActive.setText(user.isActive() ? "Deactive" : "Active");
        }
    }

    private void updateUser(String username, String email, String phone, String address, String role, boolean isActive) {
        SharedPreferences prefs = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserRepository userRepo = retrofit.create(UserRepository.class);
        UpdateUserProfileRequest req = new UpdateUserProfileRequest(username, phone, address, user.getAvatar(), role, isActive);
        userRepo.updateUser("Bearer " + token, user.getId(), req).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onUserUpdated(response.body());
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Lỗi cập nhật user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 