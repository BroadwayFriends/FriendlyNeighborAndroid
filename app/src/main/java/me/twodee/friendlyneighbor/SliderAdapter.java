package me.twodee.friendlyneighbor;

import android.content.Context;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    Context context;
    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

    SliderAdapter(Context context, List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.context = context;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        SliderItem currentSliderItem = sliderItems.get(position);
        String imageUrl = currentSliderItem.getImageUrl();
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imageView);

        Log.w("Image string", sliderItems.get(position).getImageUrl());
        if(position == sliderItems.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
           sliderItems.addAll(sliderItems);
           notifyDataSetChanged();
        }
    };
}
