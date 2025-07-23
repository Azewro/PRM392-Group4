package taskmanager.android_mizu_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Order;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textOrderId.setText("Order ID: " + order.getId());
        holder.textOrderDate.setText("Date: " + order.getCreatedAt());
        holder.textOrderTotal.setText("Total: " + order.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textOrderDate, textOrderTotal;

        public OrderViewHolder(View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.textOrderId);
            textOrderDate = itemView.findViewById(R.id.textOrderDate);
            textOrderTotal = itemView.findViewById(R.id.textOrderTotal);
        }
    }
}
