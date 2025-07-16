package taskmanager.android_mizu_shop.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import taskmanager.android_mizu_shop.model.Promotion;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.api.PromotionRepository;
import taskmanager.android_mizu_shop.model.CreatePromotionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionFormBottomSheet extends BottomSheetDialogFragment {
    private EditText etTitle, etDescription, etDiscount;
    private CheckBox cbActive;
    private Button btnSave;
    private Promotion promotion;
    private OnPromotionSavedListener listener;
    private PromotionRepository promotionRepository;
    private String token;

    public interface OnPromotionSavedListener {
        void onPromotionSaved(Promotion promotion);
    }

    public PromotionFormBottomSheet(@Nullable Promotion promotion, OnPromotionSavedListener listener) {
        this.promotion = promotion;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_promotion_form, null);
        dialog.setContentView(view);
        etTitle = view.findViewById(R.id.etPromotionTitle);
        etDescription = view.findViewById(R.id.etPromotionDescription);
        etDiscount = view.findViewById(R.id.etPromotionDiscount);
        cbActive = view.findViewById(R.id.cbPromotionActive);
        btnSave = view.findViewById(R.id.btnSavePromotion);

        promotionRepository = ApiService.getPromotionRepository();
        token = getToken();

        if (promotion != null) {
            etTitle.setText(promotion.getCode());
            etDescription.setText(promotion.getStartDate()); // Hoặc endDate/minOrderValue nếu muốn
            etDiscount.setText(String.valueOf(promotion.getDiscountPercent()));
            cbActive.setChecked(promotion.getIsActive() != null && promotion.getIsActive());
        }

        btnSave.setOnClickListener(v -> savePromotion());
        return dialog;
    }

    private void savePromotion() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String discountStr = etDiscount.getText().toString().trim();
        boolean active = cbActive.isChecked();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(discountStr)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        int discount = Integer.parseInt(discountStr);
        // Lấy các trường cần thiết
        String code = title;
        Double discountPercent = Double.valueOf(discountStr);
        String startDate = "2024-01-01"; // TODO: Cho nhập thực tế
        String endDate = "2024-12-31"; // TODO: Cho nhập thực tế
        Double minOrderValue = 0.0; // TODO: Cho nhập thực tế
        CreatePromotionRequest request = new CreatePromotionRequest(code, discountPercent, startDate, endDate, minOrderValue);
        if (promotion == null || promotion.getId() == null || promotion.getId() == 0) {
            // Tạo mới
            promotionRepository.createPromotion("Bearer " + token, request).enqueue(new Callback<Promotion>() {
                @Override
                public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (listener != null) listener.onPromotionSaved(response.body());
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Không thể tạo khuyến mãi", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Promotion> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Sửa
            promotionRepository.updatePromotion("Bearer " + token, promotion.getId(), request).enqueue(new Callback<Promotion>() {
                @Override
                public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (listener != null) listener.onPromotionSaved(response.body());
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Không thể cập nhật khuyến mãi", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Promotion> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getToken() {
        // TODO: Lấy token thực tế từ SharedPreferences
        return "";
    }
} 