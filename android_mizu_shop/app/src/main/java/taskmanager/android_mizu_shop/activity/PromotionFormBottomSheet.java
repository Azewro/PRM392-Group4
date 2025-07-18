package taskmanager.android_mizu_shop.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import android.app.DatePickerDialog;
import java.util.Calendar;

public class PromotionFormBottomSheet extends BottomSheetDialogFragment {
    private TextView tvId;
    private EditText etCode, etDiscount, etStartDate, etEndDate, etMinOrderValue;
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

        tvId = view.findViewById(R.id.tvPromotionId);
        etCode = view.findViewById(R.id.etPromotionCode);
        etDiscount = view.findViewById(R.id.etPromotionDiscount);
        etStartDate = view.findViewById(R.id.etPromotionStartDate);
        etEndDate = view.findViewById(R.id.etPromotionEndDate);
        etMinOrderValue = view.findViewById(R.id.etPromotionMinOrderValue);
        cbActive = view.findViewById(R.id.cbPromotionActive);
        btnSave = view.findViewById(R.id.btnSavePromotion);

        promotionRepository = ApiService.getPromotionRepository();
        token = getToken();

        if (promotion != null) {
            tvId.setText("ID: " + promotion.getId());
            etCode.setText(promotion.getCode());
            etDiscount.setText(String.valueOf(promotion.getDiscountPercent()));
            etStartDate.setText(promotion.getStartDate());
            etEndDate.setText(promotion.getEndDate());
            etMinOrderValue.setText(String.valueOf(promotion.getMinOrderValue()));
            cbActive.setChecked(promotion.getIsActive() != null && promotion.getIsActive());
        } else {
            tvId.setText("ID: (Tự động)");
        }

        btnSave.setOnClickListener(v -> savePromotion());
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));
        return dialog;
    }

    private void savePromotion() {
        String code = etCode.getText().toString().trim();
        String discountStr = etDiscount.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String minOrderValueStr = etMinOrderValue.getText().toString().trim();
        boolean active = cbActive.isChecked();

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(discountStr) || TextUtils.isEmpty(startDate)
                || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(minOrderValueStr)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double discountPercent = Double.parseDouble(discountStr);
        double minOrderValue = Double.parseDouble(minOrderValueStr);

        CreatePromotionRequest request = new CreatePromotionRequest(
                code, discountPercent, startDate, endDate, minOrderValue
        );

        if (promotion != null && promotion.getId() != null && promotion.getId() != 0) {
            // UPDATE
            promotionRepository.updatePromotion("Bearer " + token, promotion.getId(), request)
                    .enqueue(new Callback<Promotion>() {
                        @Override
                        public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (listener != null) listener.onPromotionSaved(response.body());
                                dismiss();
                            } else {
                                String errorMsg = "Không thể cập nhật khuyến mãi";
                                try {
                                    if (response.errorBody() != null) {
                                        errorMsg = response.errorBody().string();
                                    }
                                } catch (Exception ignored) {}
                                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
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

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(selectedDate);
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
} 