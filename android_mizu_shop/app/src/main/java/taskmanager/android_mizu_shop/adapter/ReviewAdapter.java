package taskmanager.android_mizu_shop.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taskmanager.android_mizu_shop.R;
import taskmanager.android_mizu_shop.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.tvUsername.setText(review.getUsername());
        holder.ratingBar.setRating(review.getRating());
        holder.tvComment.setText(review.getComment());
        holder.tvCreatedAt.setText(review.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvComment, tvCreatedAt;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}