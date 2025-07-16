package taskmanager.android_mizu_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import androidx.fragment.app.FragmentActivity;
import java.util.ArrayList;
import android.widget.ImageView;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.User;
import taskmanager.android_mizu_shop.activity.UserDetailBottomSheet;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private List<User> fullUserList;
    private OnUserDeleteListener deleteListener;
    private FragmentActivity activity;

    public interface OnUserDeleteListener {
        void onDelete(User user, int position);
    }

    public UserAdapter(List<User> userList, OnUserDeleteListener deleteListener, FragmentActivity activity) {
        this.userList = userList;
        this.fullUserList = new ArrayList<>(userList);
        this.deleteListener = deleteListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());
        holder.tvRole.setText(user.getRole() != null ? (user.getRole().equalsIgnoreCase("admin") ? "Admin" : "Customer") : "");
        // Trạng thái
        if (user.isActive()) {
            holder.tvStatus.setText("Active");
            holder.viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_green);
        } else {
            holder.tvStatus.setText("Blocked");
            holder.viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_red);
        }
        // Avatar: nếu có avatar thì load, nếu không thì để mặc định
        // TODO: Nếu muốn load ảnh từ URL, dùng Glide/Picasso
        holder.itemView.setOnClickListener(v -> {
            UserDetailBottomSheet sheet = new UserDetailBottomSheet(user, updatedUser -> {
                userList.set(position, updatedUser);
                notifyItemChanged(position);
            });
            sheet.show(activity.getSupportFragmentManager(), "UserDetailBottomSheet");
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void removeUser(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    public void filterList(List<User> filtered) {
        userList.clear();
        userList.addAll(filtered);
        notifyDataSetChanged();
    }
    public List<User> getFullUserList() {
        return fullUserList;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail, tvRole, tvStatus;
        View viewStatusDot;
        ImageView imgAvatar;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            viewStatusDot = itemView.findViewById(R.id.viewStatusDot);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
} 