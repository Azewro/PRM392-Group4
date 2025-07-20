package taskmanager.android_mizu_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import taskmanager.android_mizu_shop.model.Promotion;
import taskmanager.android_mizu_shop.R;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    private List<Promotion> promotionList;
    private OnPromotionClickListener listener;

    public interface OnPromotionClickListener {
        void onPromotionClick(Promotion promotion);
    }

    public PromotionAdapter(List<Promotion> promotionList, OnPromotionClickListener listener) {
        this.promotionList = promotionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);
        holder.bind(promotion);
    }

    @Override
    public int getItemCount() {
        return promotionList != null ? promotionList.size() : 0;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
        notifyDataSetChanged();
    }

    class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDiscount, tvStatus;
        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvPromotionTitle);
            tvDescription = itemView.findViewById(R.id.tvPromotionDescription);
            tvDiscount = itemView.findViewById(R.id.tvPromotionDiscount);
            tvStatus = itemView.findViewById(R.id.tvPromotionStatus);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onPromotionClick(promotionList.get(getAdapterPosition()));
                }
            });
        }
        public void bind(Promotion promotion) {
            tvTitle.setText(promotion.getCode());
            tvDescription.setText("Bắt đầu: " + promotion.getStartDate());
            tvDiscount.setText(promotion.getDiscountPercent() + "%");
            tvStatus.setText(promotion.getIsActive() != null && promotion.getIsActive() ? "Đang hoạt động" : "Ngừng hoạt động");
        }
    }
} 