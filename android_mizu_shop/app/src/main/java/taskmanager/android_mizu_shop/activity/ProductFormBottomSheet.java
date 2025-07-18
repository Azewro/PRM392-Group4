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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Category;
import taskmanager.android_mizu_shop.model.Product;

public class ProductFormBottomSheet extends BottomSheetDialogFragment {
    private static final int PICK_IMAGE_REQUEST = 2001;
    private EditText etName, etDescription, etPrice, etStock;
    private ImageView ivImagePreview;
    private Button btnSelectImage, btnSave;
    private Spinner spinnerCategory;
    private byte[] imageBytes;
    private String imageBase64;
    private Product product;
    private List<Category> categoryList;
    private OnProductSavedListener listener;

    public interface OnProductSavedListener {
        void onProductSaved(Product product);
    }

    public ProductFormBottomSheet(@Nullable Product product, List<Category> categoryList, OnProductSavedListener listener) {
        this.product = product;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_product_form, null);
        dialog.setContentView(view);

        etName = view.findViewById(R.id.etProductName);
        etDescription = view.findViewById(R.id.etProductDescription);
        etPrice = view.findViewById(R.id.etProductPrice);
        etStock = view.findViewById(R.id.etProductStock);
        ivImagePreview = view.findViewById(R.id.ivProductImagePreview);
        btnSelectImage = view.findViewById(R.id.btnSelectProductImage);
        btnSave = view.findViewById(R.id.btnSaveProduct);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        // Setup spinner category
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Category c : categoryList) {
            adapter.add(c.getName());
        }
        spinnerCategory.setAdapter(adapter);

        if (product != null) {
            etName.setText(product.getName());
            etDescription.setText(product.getDescription());
            etPrice.setText(product.getPrice() != null ? product.getPrice().toString() : "");
            etStock.setText(product.getStock() != null ? String.valueOf(product.getStock()) : "");
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                // TODO: Nếu backend trả về base64/byte[], cần decode tại đây
                // Hiện tại chỉ demo placeholder
                ivImagePreview.setImageResource(R.drawable.ic_launcher_background);
            }
            // Set spinner category
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getName().equals(product.getCategoryName())) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveProduct());

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
                imageBase64 = encodeBitmapToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_WRAP);
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        int categoryIdx = spinnerCategory.getSelectedItemPosition();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || categoryIdx < 0) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        BigDecimal price = new BigDecimal(priceStr);
        int stock = TextUtils.isEmpty(stockStr) ? 0 : Integer.parseInt(stockStr);
        Category selectedCategory = categoryList.get(categoryIdx);
        int categoryId = selectedCategory.getId();
        String imageToSave = imageBase64;
        if (product != null && imageBase64 == null) {
            imageToSave = product.getImageUrl();
        }
        boolean isActive = product != null ? product.getIsActive() : true;
        if (listener != null) {
            Product newProduct = new Product(
                    product != null ? product.getId() : 0,
                    name,
                    description,
                    price,
                    imageToSave != null ? imageToSave : "",
                    stock,
                    null,
                    null,
                    selectedCategory.getName(),
                    categoryId,
                    isActive
            );
            listener.onProductSaved(newProduct);
        }
        dismiss();
    }
} 