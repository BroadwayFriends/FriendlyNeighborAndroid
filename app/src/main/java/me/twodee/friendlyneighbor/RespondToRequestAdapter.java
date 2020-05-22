//package me.twodee.friendlyneighbor;
//
//public class RespondToRequestAdapter {
//}
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

public class RespondToRequestAdapter extends RecyclerView.Adapter <RespondToRequestAdapter.ChoicePageDetailsViewHolder> implements Filterable {

    private Context mCtx;
    private List<RespondToRequestDetails> choicePageDetailsList;
    private List<RespondToRequestDetails> choicePageDetailsListFull;

    private OnChoicePageDetailsClickListener onChoicePageDetailsClickListener;

    public RespondToRequestAdapter(Context mCtx, List<RespondToRequestDetails> choicePageDetailsList, OnChoicePageDetailsClickListener onChoicePageDetailsClickListener) {
        this.mCtx = mCtx;
        this.onChoicePageDetailsClickListener = onChoicePageDetailsClickListener;
        this.choicePageDetailsList = choicePageDetailsList;
        choicePageDetailsListFull = new ArrayList<>(choicePageDetailsList);

    }

    @NonNull
    @Override
    public ChoicePageDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.response_layout, parent, false);
        ChoicePageDetailsViewHolder choicePageDetailsViewHolder = new ChoicePageDetailsViewHolder(view, onChoicePageDetailsClickListener);

        return  choicePageDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChoicePageDetailsViewHolder holder, int position) {

        RespondToRequestDetails choicePageDetails = choicePageDetailsList.get(position);
        holder.titleTV.setText(choicePageDetails.getChoicePageTitle());
        holder.typeTV.setText(choicePageDetails.getChoicePageType());
        holder.personTV.setText(choicePageDetails.getChoicePagePerson());
        holder.distanceTV.setText(String.valueOf(choicePageDetails.getChoicePagePrice()));
        holder.timeTV.setText(choicePageDetails.getChoicePageTime());

    }

    @Override
    public int getItemCount() {
        return choicePageDetailsList.size();
    }

    @Override
    public Filter getFilter() {
        return choicePageFilter;
    }

    private Filter choicePageFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RespondToRequestDetails> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(choicePageDetailsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(RespondToRequestDetails item : choicePageDetailsListFull) {
                    if (item.getChoicePageTitle().toLowerCase().contains(filterPattern)) {
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
            choicePageDetailsList.clear();
            choicePageDetailsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    public class ChoicePageDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTV, typeTV, personTV, distanceTV, timeTV;
        OnChoicePageDetailsClickListener onChoicePageDetailsClickListener;


        public ChoicePageDetailsViewHolder(@NonNull View itemView, OnChoicePageDetailsClickListener onChoicePageDetailsClickListener) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.choicePage_title);
            typeTV = (TextView) itemView.findViewById(R.id.choicePage_type);
            personTV = (TextView) itemView.findViewById(R.id.choicePage_person);
            distanceTV = (TextView) itemView.findViewById(R.id.choicePage_price);
            timeTV = (TextView) itemView.findViewById(R.id.choicePage_time);
            this.onChoicePageDetailsClickListener = onChoicePageDetailsClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onChoicePageDetailsClickListener.onChoicePageDetailsClick(getAdapterPosition());

        }
    }

    public interface OnChoicePageDetailsClickListener {
        void onChoicePageDetailsClick (int position);
    }

}
