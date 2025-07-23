package taskmanager.android_mizu_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.api.ApiService;
import taskmanager.android_mizu_shop.model.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItemList, OnQuantityChangeListener listener) {
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.textProductName.setText(item.getName());
        holder.textProductPrice.setText("Giá: " + item.getPrice());
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));

        // Load image using Glide
        Glide.with(holder.imageProduct.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageProduct);

        holder.buttonIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            listener.onIncrease(item);

            // Cập nhật lại API khi tăng số lượng
            ApiService.getCartRepository().updateCartItem(item.getId(), item).enqueue(new Callback<CartItem>() {
                @Override
                public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(v.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CartItem> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                listener.onDecrease(item);

                // Cập nhật lại API khi giảm số lượng
                ApiService.getCartRepository().updateCartItem(item.getId(), item).enqueue(new Callback<CartItem>() {
                    @Override
                    public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CartItem> call, Throwable t) {
                        Toast.makeText(v.getContext(), "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName, textProductPrice, textQuantity;
        Button buttonIncrease, buttonDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
        }
    }
}
