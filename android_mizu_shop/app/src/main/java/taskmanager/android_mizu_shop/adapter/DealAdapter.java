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

import com.bumptech.glide.Glide;

import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.activity.ProductDetailActivity;
import taskmanager.android_mizu_shop.model.DealItem;
import taskmanager.android_mizu_shop.model.Product;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    private List<Product> productList;
    private Context context;

    public DealAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public DealAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void setProductList(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText((double) product.getPrice().doubleValue() + " USD");


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
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product); // cần Serializable hoặc Parcelable
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgFood);
        }
    }
}
