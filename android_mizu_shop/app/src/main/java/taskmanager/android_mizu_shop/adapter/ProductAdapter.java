package taskmanager.android_mizu_shop.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onProductEdit(Product product);
        void onProductDelete(Product product);
        void onProductToggleActive(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCategoryName());
        holder.tvPrice.setText("Giá: " + product.getPrice() + "đ");
        holder.tvQuantity.setText("Số lượng: " + (product.getStock() != null ? product.getStock() : 0));
        holder.tvStock.setText((product.getStock() != null && product.getStock() > 0) ? "Còn hàng" : "Hết hàng");
        holder.tvStock.setTextColor(holder.itemView.getContext().getResources().getColor(
            (product.getStock() != null && product.getStock() > 0) ? R.color.teal_700 : android.R.color.holo_red_dark
        ));
        holder.tvActive.setText(product.getIsActive() ? "Đang hoạt động" : "Đã ẩn");
        holder.tvActive.setTextColor(product.getIsActive() ? 0xFF388E3C : 0xFFD32F2F);
        if (product.getIsActive()) {
            holder.btnDelete.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            holder.btnDelete.setImageResource(R.drawable.ic_active_category);
        }
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductToggleActive(product);
            }
        });
        // Hiển thị ảnh (nếu imageUrl là base64 hoặc byte[] thì decode, nếu là URL thì dùng Glide/Picasso)
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                byte[] imageBytes = android.util.Base64.decode(product.getImageUrl(), android.util.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                if (bitmap != null) {
                    holder.ivImage.setImageBitmap(bitmap);
                } else {
                    holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } catch (Exception e) {
                holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
        holder.btnEdit.setOnClickListener(v -> listener.onProductEdit(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice, tvStock, tvQuantity, tvActive;
        ImageView ivImage, btnEdit, btnDelete;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvProductCategory);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvStock = itemView.findViewById(R.id.tvProductStock);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvActive = itemView.findViewById(R.id.tvProductActive);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
} 