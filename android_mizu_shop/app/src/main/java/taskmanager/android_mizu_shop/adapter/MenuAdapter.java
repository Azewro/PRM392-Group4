package taskmanager.android_mizu_shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.activity.ProductByCategoryActivity;
import taskmanager.android_mizu_shop.activity.ProductDetailActivity;
import taskmanager.android_mizu_shop.model.Category;
import taskmanager.android_mizu_shop.model.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private Context context;

    public MenuAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_adokok, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());


        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            try {
                byte[] imageBytes = android.util.Base64.decode(category.getImageUrl(), android.util.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                if (bitmap != null) {
                    holder.imgCategory.setImageBitmap(bitmap);
                } else {
                    holder.imgCategory.setImageResource(R.drawable.cate);
                }
            } catch (Exception e) {
                holder.imgCategory.setImageResource(R.drawable.cate);
            }
        } else {
            holder.imgCategory.setImageResource(R.drawable.cate);
        }
        Log.d("MenuAdapter", "imgCategory is null? " + (holder.imgCategory == null));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductByCategoryActivity.class);
            intent.putExtra("categoryId", category.getId());
            Log.d("CATEGORY_ID", "ID: " + category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCate);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
