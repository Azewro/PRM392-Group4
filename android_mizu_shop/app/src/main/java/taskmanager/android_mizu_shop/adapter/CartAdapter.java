package taskmanager.android_mizu_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private OnQuantityChangeListener listener;
    private boolean showButtons = true;

    public interface OnQuantityChangeListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItemList, OnQuantityChangeListener listener, boolean showButtons) {
        this.cartItemList = cartItemList;
        this.listener = listener;
        this.showButtons = showButtons;
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
        holder.textProductDescription.setText(item.getDescription());
        holder.textProductPrice.setText("Giá: " + String.format("%,.0f", item.getPrice()) + "đ");
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));

        // Load image (requires Glide library in your app)
        Glide.with(holder.imageProduct.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageProduct);

        holder.buttonIncrease.setOnClickListener(v -> {
            listener.onIncrease(item);
        });
        holder.buttonDecrease.setOnClickListener(v -> {
            listener.onDecrease(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName, textProductDescription, textProductPrice, textQuantity;
        Button buttonIncrease, buttonDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductDescription = itemView.findViewById(R.id.textProductDescription);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
        }
    }
}
