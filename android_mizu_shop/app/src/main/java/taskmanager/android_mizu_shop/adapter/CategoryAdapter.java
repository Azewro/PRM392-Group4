package taskmanager.android_mizu_shop.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
        void onCategoryEdit(Category category);
        void onCategoryDelete(Category category);
    }
    public interface OnCategoryActiveListener {
        void onCategoryActive(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvName.setText(category.getName());
        holder.tvDescription.setText(category.getDescription());
        holder.tvProductCount.setText("Sản phẩm: " + category.getProductCount());
        holder.tvActive.setText(category.getIsActive() ? "Đang hoạt động" : "Đã ẩn");
        holder.tvActive.setTextColor(category.getIsActive() ? 0xFF388E3C : 0xFFD32F2F); // xanh hoặc đỏ
        if (category.getIsActive()) {
            holder.btnDelete.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            holder.btnDelete.setImageResource(R.drawable.ic_active_category);
        }
        // Log dữ liệu imageUrl để kiểm tra
        if (category.getImageUrl() != null) {
            String base64 = category.getImageUrl();
            Log.d("CategoryAdapter", "Category: " + category.getName() + ", imageUrl length: " + base64.length());
            String head = base64.substring(0, Math.min(100, base64.length()));
            Log.d("CategoryAdapter", "Base64 head: " + head);
            try {
                Class<?> formClass = Class.forName("taskmanager.android_mizu_shop.activity.CategoryFormBottomSheet");
                java.lang.reflect.Field field = formClass.getDeclaredField("lastBase64Sent");
                field.setAccessible(true);
                String lastSent = (String) field.get(null);
                if (lastSent != null && !lastSent.equals(head)) {
                    Log.w("CategoryAdapter", "Base64 head mismatch! Android sent: " + lastSent + ", backend returned: " + head);
                }
            } catch (Exception e) {
                Log.e("CategoryAdapter", "Reflection error: " + e.getMessage());
            }
        } else {
            Log.d("CategoryAdapter", "Category: " + category.getName() + ", imageUrl is null");
        }
        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            try {
                byte[] imageBytes = Base64.decode(category.getImageUrl(), Base64.DEFAULT);
                Log.d("CategoryAdapter", "Decoded bytes length: " + imageBytes.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                if (bitmap != null) {
                    holder.ivImage.setImageBitmap(bitmap);
                } else {
                    Log.w("CategoryAdapter", "Bitmap decode failed for category: " + category.getName());
                    holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } catch (Exception e) {
                Log.e("CategoryAdapter", "Base64 decode error for category: " + category.getName(), e);
                holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        holder.btnEdit.setOnClickListener(v -> listener.onCategoryEdit(category));
        holder.btnDelete.setOnClickListener(v -> {
            if (category.getIsActive()) {
                listener.onCategoryDelete(category);
            } else {
                if (listener instanceof OnCategoryActiveListener) {
                    ((OnCategoryActiveListener) listener).onCategoryActive(category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvProductCount, tvActive;
        ImageView ivImage;
        ImageView btnEdit, btnDelete;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
            tvProductCount = itemView.findViewById(R.id.tvCategoryProductCount);
            tvActive = itemView.findViewById(R.id.tvCategoryActive);
            ivImage = itemView.findViewById(R.id.ivCategoryImage);
            btnEdit = itemView.findViewById(R.id.btnEditCategory);
            btnDelete = itemView.findViewById(R.id.btnDeleteCategory);
        }
    }
} 