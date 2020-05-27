package me.twodee.friendlyneighbor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryPostDetailsAdapter extends RecyclerView.Adapter<HistoryPostDetailsAdapter.ViewHolder>  {

    Context context;
    List<HistoryRespondedUserDetails> data;

    public HistoryPostDetailsAdapter(Context context, List<HistoryRespondedUserDetails> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.history_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        HistoryRespondedUserDetails historyRespondedUserDetails = data.get(position);

        holder.itemNameTV.setText(historyRespondedUserDetails.getRUName());
        holder.itemNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Swipe left to accept or decline", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemContactNumberTV.setText(historyRespondedUserDetails.getRUContactNumber());
        Picasso.get().load(historyRespondedUserDetails.getRUProfilePicture()).fit().centerInside().into(holder.itemProfilePictureIV);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(HistoryRespondedUserDetails item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public List<HistoryRespondedUserDetails> getData() {
        return data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View container;
        TextView itemNameTV;
        TextView itemContactNumberTV;
        ImageView itemProfilePictureIV;

        public ViewHolder(View view) {
            super(view);
            container = view;
            itemNameTV = view.findViewById(R.id.history_post_name);
            itemContactNumberTV = view.findViewById(R.id.history_post_contact);
            itemProfilePictureIV = view.findViewById(R.id.history_post_profile_picture);

        }

    }
}