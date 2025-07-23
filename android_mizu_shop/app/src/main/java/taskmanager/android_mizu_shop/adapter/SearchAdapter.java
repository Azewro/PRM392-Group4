package taskmanager.android_mizu_shop.adapter;

import android.content.Context;
import android.content.Intent;
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
import taskmanager.android_mizu_shop.activity.ProductDetailActivity;
import taskmanager.android_mizu_shop.model.Product;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnProductClickListener clickListener;
    private Context context;
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public SearchAdapter(List<Product> productList, OnProductClickListener clickListener) {
        this.productList = productList;
        this.clickListener = clickListener;
    }
    public SearchAdapter(Context context, List<Product> productList, OnProductClickListener clickListener) {
        this.context = context;
        this.productList = productList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_search, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%.0f đ", product.getPrice()));
        // TODO: Load ảnh bằng Glide hoặc Picasso nếu có image URL
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                byte[] imageBytes = android.util.Base64.decode(product.getImageUrl(), android.util.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                if (bitmap != null) {
                    holder.imgProduct.setImageBitmap(bitmap);
                } else {
                    holder.imgProduct.setImageResource(R.drawable.produc);
                }
            } catch (Exception e) {
                holder.imgProduct.setImageResource(R.drawable.produc);
            }
        } else {
            holder.imgProduct.setImageResource(R.drawable.produc);
        }
//        holder.itemView.setOnClickListener(v -> clickListener.onProductClick(product));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product); // cần Serializable hoặc Parcelable
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newList) {
        productList.clear();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
