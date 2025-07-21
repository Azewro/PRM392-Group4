package taskmanager.android_mizu_shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.activity.ProductDetailActivity;
import taskmanager.android_mizu_shop.model.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<MenuItem> menuList;

    public MenuAdapter(List<MenuItem> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_adokok, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);
        holder.tvName.setText(item.name);
        holder.imgIcon.setImageResource(item.iconResId);

        holder.itemView.setOnClickListener(v -> {
            // Tạo intent mở ProductDetailActivity
            Context context = v.getContext();
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("img", item.iconResId);
            intent.putExtra("name", item.name);
            intent.putExtra("price", "25.000đ"); // Fix cứng nếu chưa có giá
            intent.putExtra("desc", "Mô tả sản phẩm mẫu..."); // Fix cứng nếu chưa có desc
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvName;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgMenu);
            tvName = itemView.findViewById(R.id.tvMenuName);
        }
    }
}

