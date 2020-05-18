package me.twodee.friendlyneighbor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiscoverDetailsAdapter extends RecyclerView.Adapter <DiscoverDetailsAdapter.DiscoverDetailsViewHolder> {

    private Context mCtx;
    private List<DiscoverDetails> discoverDetailsList;

    public DiscoverDetailsAdapter(Context mCtx, List<DiscoverDetails> discoverDetailsList) {
        this.mCtx = mCtx;
        this.discoverDetailsList = discoverDetailsList;
    }

    @NonNull
    @Override
    public DiscoverDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.discover_layout, parent, false);
        DiscoverDetailsViewHolder discoverDetailsViewHolder = new DiscoverDetailsViewHolder(view);

        return  discoverDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverDetailsViewHolder holder, int position) {

        DiscoverDetails discoverDetails = discoverDetailsList.get(position);
        holder.titleTV.setText(discoverDetails.getDiscoverTitle());
        holder.typeTV.setText(discoverDetails.getDiscoverType());
        holder.personTV.setText(discoverDetails.getDiscoverPerson());
        holder.distanceTV.setText(String.valueOf(discoverDetails.getDiscoverPrice()));
        holder.timeTV.setText(discoverDetails.getDiscoverTime());

    }

    @Override
    public int getItemCount() {
        return discoverDetailsList.size();
    }

    class DiscoverDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, typeTV, personTV, distanceTV, timeTV;

        public DiscoverDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.discover_title);
            typeTV = (TextView) itemView.findViewById(R.id.discover_type);
            personTV = (TextView) itemView.findViewById(R.id.discover_person);
            distanceTV = (TextView) itemView.findViewById(R.id.discover_price);
            timeTV = (TextView) itemView.findViewById(R.id.discover_time);

        }
    }

}
