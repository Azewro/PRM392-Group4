package taskmanager.android_mizu_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taskmanager.android_mizu_shop.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    List<Integer> bannerImages; // drawable id

    public BannerAdapter(List<Integer> bannerImages) {
        this.bannerImages = bannerImages;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_banner_adokok, parent, false);
        return new BannerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.imageView.setImageResource(bannerImages.get(position));
    }

    @Override
    public int getItemCount() {
        return bannerImages.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgBanner);
        }
    }
}
