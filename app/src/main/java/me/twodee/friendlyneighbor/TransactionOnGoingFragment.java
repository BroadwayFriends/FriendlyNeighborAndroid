package me.twodee.friendlyneighbor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionOnGoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionOnGoingFragment extends Fragment implements OnGoingDetailsAdapter.OnOnGoingDetailsClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionOnGoingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionOnGoingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionOnGoingFragment newInstance(String param1, String param2) {
        TransactionOnGoingFragment fragment = new TransactionOnGoingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    RecyclerView recyclerView;
    OnGoingDetailsAdapter onGoingDetailsAdapter;

    List<OnGoingDetails> onGoingDetailsList;

//    SearchView searchView;

    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_on_going, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        onGoingDetailsList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.ongoing_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        preferences = this.getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

        //Adding dummy static data
        onGoingDetailsList.add(
                new OnGoingDetails(
                        "Akhil",
                        "+919999911111",
                        "Maggi",
                        "10:00 AM",
                        null));

        onGoingDetailsList.add(
                new OnGoingDetails(
                        "Agni",
                        "+919999911111",
                        "Charger",
                        "05:00 AM",
                        null));

        onGoingDetailsList.add(
                new OnGoingDetails(
                        "Priyam",
                        "+919999911111",
                        "Table",
                        "11:30 PM",
                        null));

        onGoingDetailsList.add(
                new OnGoingDetails(
                        "Ritwik",
                        "+919999911111",
                        "Mouse",
                        "07:00 AM",
                        null));

        onGoingDetailsList.add(
                new OnGoingDetails(
                        "2D",
                        "+919999911111",
                        "Eggs",
                        "01:00 PM",
                        null));

        onGoingDetailsList.add(
                new OnGoingDetails(
                        "Nihar",
                        "+919999911111",
                        "Cap",
                        "03:30 PM",
                        null));

        onGoingDetailsAdapter = new OnGoingDetailsAdapter(getActivity(), onGoingDetailsList, TransactionOnGoingFragment.this);
        recyclerView.setAdapter(onGoingDetailsAdapter);

    }

    @Override
    public void onOnGoingDetailsClick(int position) {
        onGoingDetailsList.get(position);
        OnGoingDetails onGoingDetails = onGoingDetailsList.get(position);

        Log.w("Clicker Checker", String.valueOf(position));
        Log.w("Clicker Checker", onGoingDetails.getOngoingPerson());
        Log.w("Clicker Checker", onGoingDetails.getOngoingItem());
        Log.w("Clicker Checker", onGoingDetails.getOngoingTime());
//        Log.w("Clicker Checker", onGoingDetails.getOngoingProfilePicture());
    }

    @Override
    public void onCallButtonClick(int position) {
        onGoingDetailsList.get(position);
        OnGoingDetails onGoingDetails = onGoingDetailsList.get(position);

        Toast.makeText(getActivity(),"Calling " + onGoingDetails.getOngoingPerson(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishButtonClick(int position) {
        onGoingDetailsList.get(position);
        OnGoingDetails onGoingDetails = onGoingDetailsList.get(position);
        onGoingDetailsList.remove(position);
        onGoingDetailsAdapter.notifyItemRemoved(position);
        onGoingDetailsAdapter.notifyDataSetChanged();

        Toast.makeText(getActivity(), "Finishing request: " + onGoingDetails.getOngoingPerson(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                onGoingDetailsList.clear();
//                loadDiscoverData();
            }
        }
    }
}