package taskmanager.android_mizu_shop.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;
import android.util.Log;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Category;

public class CategoryFormBottomSheet extends BottomSheetDialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private EditText etName, etDescription;
    private ImageView ivImagePreview;
    private Button btnSelectImage, btnSave;
    private String imageBase64;
    private Category category;
    private OnCategorySavedListener listener;
    private boolean isImageChanged = false;
    private static String lastBase64Sent = null;

    public interface OnCategorySavedListener {
        void onCategorySaved(Category category);
    }

    public CategoryFormBottomSheet(@Nullable Category category, OnCategorySavedListener listener) {
        this.category = category;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_category_form, null);
        dialog.setContentView(view);

        etName = view.findViewById(R.id.etCategoryName);
        etDescription = view.findViewById(R.id.etCategoryDescription);
        ivImagePreview = view.findViewById(R.id.ivCategoryImagePreview);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSave = view.findViewById(R.id.btnSaveCategory);

        if (category != null) {
            etName.setText(category.getName());
            etDescription.setText(category.getDescription());
            if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
                try {
                    byte[] imageBytes = Base64.decode(category.getImageUrl(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    ivImagePreview.setImageBitmap(bitmap);
                    imageBase64 = category.getImageUrl();
                } catch (Exception e) {
                    ivImagePreview.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }

        btnSelectImage.setOnClickListener(v -> {
            openImagePicker();
            isImageChanged = true;
        });
        btnSave.setOnClickListener(v -> saveCategory());

        return dialog;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ivImagePreview.setImageBitmap(bitmap);
                // Log 10 bytes đầu tiên của ảnh gốc trước khi encode Base64
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] rawBytes = stream.toByteArray();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < Math.min(10, rawBytes.length); i++) {
                    sb.append(rawBytes[i]);
                    if (i < Math.min(10, rawBytes.length) - 1) sb.append(", ");
                }
                sb.append("]");
                Log.d("CategoryForm", "First 10 bytes of raw image before encode: " + sb.toString());
                imageBase64 = encodeBitmapToBase64(bitmap);
                isImageChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();
        // Chỉ encode một lần duy nhất, không decode lại!
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void saveCategory() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }
        String imageToSave = imageBase64;
        if (!isImageChanged && category != null) {
            imageToSave = category.getImageUrl();
        }
        // Không decode lại Base64 trước khi gửi lên backend!
        if (imageToSave != null && !imageToSave.isEmpty() && isImageChanged) {
            try {
                // Log 10 bytes đầu tiên của ảnh gốc trước khi encode (đã log ở onActivityResult)
                Log.d("CategoryForm", "Base64 head before send: " + imageToSave.substring(0, Math.min(100, imageToSave.length())));
                lastBase64Sent = imageToSave.substring(0, Math.min(100, imageToSave.length()));
            } catch (Exception e) {
                Log.e("CategoryForm", "Error logging Base64 before send", e);
            }
        }
        if (listener != null) {
            boolean isActive = category != null ? category.getIsActive() : true;
            Category newCategory = new Category(
                    category != null ? category.getId() : 0,
                    name,
                    description,
                    imageToSave,
                    category != null ? category.getProductCount() : 0,
                    isActive
            );
            listener.onCategorySaved(newCategory);
        }
        dismiss();
    }
} 