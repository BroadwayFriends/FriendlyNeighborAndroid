//package me.twodee.friendlyneighbor;
//
//public class HistoryDetailsAdapter {
//}
package me.twodee.friendlyneighbor;

import android.app.Dialog;
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

public class HistoryDetailsAdapter extends RecyclerView.Adapter <HistoryDetailsAdapter.ChoicePageDetailsViewHolder> implements Filterable {

    private Context mCtx;
    private List<HistoryDetails> choicePageDetailsList;
    private List<HistoryDetails> choicePageDetailsListFull;

    private OnChoicePageDetailsClickListener onChoicePageDetailsClickListener;

    public HistoryDetailsAdapter(Context mCtx, List<HistoryDetails> choicePageDetailsList, OnChoicePageDetailsClickListener onChoicePageDetailsClickListener) {
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

        HistoryDetails choicePageDetails = choicePageDetailsList.get(position);
        holder.titleTV.setText(choicePageDetails.getChoicePageTitle());
        holder.typeTV.setText(choicePageDetails.getChoicePageType());

        String status = choicePageDetails.getChoicePageCompleted() ? "Completed" : "Pending";
        holder.statusTV.setText(status);

        if (choicePageDetails.getChoicePageCompleted()) {
            holder.statusNameTV.setText("with " + choicePageDetails.getChoicePageAcceptedUser());
        }
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
            List<HistoryDetails> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(choicePageDetailsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(HistoryDetails item : choicePageDetailsListFull) {
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

        TextView titleTV, typeTV, statusTV, statusNameTV, timeTV;
        OnChoicePageDetailsClickListener onChoicePageDetailsClickListener;


        public ChoicePageDetailsViewHolder(@NonNull View itemView, OnChoicePageDetailsClickListener onChoicePageDetailsClickListener) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.choicePage_title);
            typeTV = (TextView) itemView.findViewById(R.id.choicePage_type);
            statusTV = (TextView) itemView.findViewById(R.id.choice_page_status);
            statusNameTV = (TextView) itemView.findViewById(R.id.choice_page_status_name);
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
