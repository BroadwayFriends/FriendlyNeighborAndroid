package me.twodee.friendlyneighbor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OnGoingDetailsAdapter extends RecyclerView.Adapter <OnGoingDetailsAdapter.OnGoingDetailsViewHolder> {

    private Context mCtx;
    private List<OnGoingDetails> onGoingDetailsList;
    private List<OnGoingDetails> onGoingDetailsListFull;

    private OnOnGoingDetailsClickListener onOnGoingDetailsClickListener;

    public OnGoingDetailsAdapter(Context mCtx, List<OnGoingDetails> onGoingDetailsList, OnOnGoingDetailsClickListener onOnGoingDetailsClickListener) {
        this.mCtx = mCtx;
        this.onOnGoingDetailsClickListener = onOnGoingDetailsClickListener;
        this.onGoingDetailsList = onGoingDetailsList;
        onGoingDetailsListFull = new ArrayList<>(onGoingDetailsList);

    }

    @NonNull
    @Override
    public OnGoingDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.on_going_layout, parent, false);
        OnGoingDetailsViewHolder onGoingDetailsViewHolder = new OnGoingDetailsViewHolder(view, onOnGoingDetailsClickListener);

        return  onGoingDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingDetailsViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        OnGoingDetails onGoingDetails = onGoingDetailsList.get(position);
        holder.personTV.setText(onGoingDetails.getOngoingPerson());
        holder.itemTV.setText(onGoingDetails.getOngoingItem());
        holder.timeTV.setText(onGoingDetails.getOngoingTime());

//        Picasso.get().load(onGoingDetails.getOngoingProfilePicture()).fit().centerInside().into(holder.personProfilePictureIV);

    }

    @Override
    public int getItemCount() {
        return onGoingDetailsList.size();
    }
//
//    @Override
//    public Filter getFilter() {
//        return discoverFilter;
//    }
//
//    private Filter discoverFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<DiscoverDetails> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(discoverDetailsListFull);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for(DiscoverDetails item : discoverDetailsListFull) {
//                    if (item.getDiscoverTitle().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            discoverDetailsList.clear();
//            discoverDetailsList.addAll((List) results.values);
//            notifyDataSetChanged();
//
//        }
//    };
//
//    public void clear() {
//
//        discoverDetailsList.clear();
//        notifyDataSetChanged();
//    }



    public class OnGoingDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView personTV, itemTV, timeTV;
        ImageView personProfilePictureIV;
        Button callButton, finishButton;

        OnOnGoingDetailsClickListener onOnGoingDetailsClickListener;


        public OnGoingDetailsViewHolder(@NonNull View itemView, OnOnGoingDetailsClickListener onOnGoingDetailsClickListener) {
            super(itemView);

            personTV = (TextView) itemView.findViewById(R.id.ongoing_offering_name);
            itemTV = (TextView) itemView.findViewById(R.id.ongoing_item);
            timeTV = (TextView) itemView.findViewById(R.id.ongoing_time);
            personProfilePictureIV = (ImageView) itemView.findViewById(R.id.ongoing_offering_profile_picture);

            callButton = (Button) itemView.findViewById(R.id.ongoing_call_button);
            finishButton = (Button) itemView.findViewById(R.id.ongoing_finish_button);

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOnGoingDetailsClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onOnGoingDetailsClickListener.onCallButtonClick(position);
                        }
                    }
                }
            });

            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOnGoingDetailsClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onOnGoingDetailsClickListener.onFinishButtonClick(position);
                        }
                    }
                }
            });


            this.onOnGoingDetailsClickListener = onOnGoingDetailsClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onOnGoingDetailsClickListener.onOnGoingDetailsClick(getAdapterPosition());

        }
    }

    public interface OnOnGoingDetailsClickListener {
        void onOnGoingDetailsClick (int position);
        void onCallButtonClick (int position);
        void onFinishButtonClick (int position);
    }
}
