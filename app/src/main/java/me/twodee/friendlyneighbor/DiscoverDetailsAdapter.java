package me.twodee.friendlyneighbor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiscoverDetailsAdapter extends RecyclerView.Adapter <DiscoverDetailsAdapter.DiscoverDetailsViewHolder> implements Filterable {

    private Context mCtx;
    private List<DiscoverDetails> discoverDetailsList;
    private List<DiscoverDetails> discoverDetailsListFull;

    private OnDiscoverDetailsClickListener onDiscoverDetailsClickListener;

    public DiscoverDetailsAdapter(Context mCtx, List<DiscoverDetails> discoverDetailsList, OnDiscoverDetailsClickListener onDiscoverDetailsClickListener) {
        this.mCtx = mCtx;
        this.onDiscoverDetailsClickListener = onDiscoverDetailsClickListener;
        this.discoverDetailsList = discoverDetailsList;
        discoverDetailsListFull = new ArrayList<>(discoverDetailsList);

    }

    @NonNull
    @Override
    public DiscoverDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.discover_layout, parent, false);
        DiscoverDetailsViewHolder discoverDetailsViewHolder = new DiscoverDetailsViewHolder(view, onDiscoverDetailsClickListener);

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

    @Override
    public Filter getFilter() {
        return discoverFilter;
    }

    private Filter discoverFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DiscoverDetails> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(discoverDetailsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(DiscoverDetails item : discoverDetailsListFull) {
                    if (item.getDiscoverTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            discoverDetailsList.clear();
            discoverDetailsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    public class DiscoverDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTV, typeTV, personTV, distanceTV, timeTV;
        OnDiscoverDetailsClickListener onDiscoverDetailsClickListener;


        public DiscoverDetailsViewHolder(@NonNull View itemView, OnDiscoverDetailsClickListener onDiscoverDetailsClickListener) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.discover_title);
            typeTV = (TextView) itemView.findViewById(R.id.discover_type);
            personTV = (TextView) itemView.findViewById(R.id.discover_person);
            distanceTV = (TextView) itemView.findViewById(R.id.discover_price);
            timeTV = (TextView) itemView.findViewById(R.id.discover_time);
            this.onDiscoverDetailsClickListener = onDiscoverDetailsClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onDiscoverDetailsClickListener.onDiscoverDetailsClick(getAdapterPosition());

        }
    }

    public interface OnDiscoverDetailsClickListener {
        void onDiscoverDetailsClick (int position);
    }

}
