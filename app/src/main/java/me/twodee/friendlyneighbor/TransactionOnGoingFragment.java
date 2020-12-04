package me.twodee.friendlyneighbor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twodee.friendlyneighbor.service.TransactionService;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionOnGoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionOnGoingFragment extends Fragment implements OnGoingDetailsAdapter.OnOnGoingDetailsClickListener {

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
//        onGoingDetailsList.add(
//                new OnGoingDetails(
//                        "Akhil",
//                        "+919999911111",
//                        "Maggi",
//                        "10:00 AM",
//                        null));
//
//        onGoingDetailsList.add(
//                new OnGoingDetails(
//                        "Agni",
//                        "+919999911111",
//                        "Charger",
//                        "05:00 AM",
//                        null));
//
//        onGoingDetailsList.add(
//                new OnGoingDetails(
//                        "Priyam",
//                        "+919999911111",
//                        "Table",
//                        "11:30 PM",
//                        null));
//
//        onGoingDetailsList.add(
//                new OnGoingDetails(
//                        "Ritwik",
//                        "+919999911111",
//                        "Mouse",
//                        "07:00 AM",
//                        null));
//
//        onGoingDetailsList.add(
//                new OnGoingDetails(
//                        "2D",
//                        "+919999911111",
//                        "Eggs",
//                        "01:00 PM",
//                        null));
//
//        onGoingDetailsList.add(
//                new OnGoingDetails(
//                        "Nihar",
//                        "+919999911111",
//                        "Cap",
//                        "03:30 PM",
//                        null));
//
//        onGoingDetailsAdapter = new OnGoingDetailsAdapter(getActivity(), onGoingDetailsList, TransactionOnGoingFragment.this);
//        recyclerView.setAdapter(onGoingDetailsAdapter);

        loadDiscoverData();

//        int itemCount = recyclerView.getAdapter().getItemCount();
//        Log.w("ITEM COUNT", String.valueOf(itemCount));

//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                discoverDetailsAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });

    }

    void loadDiscoverData() {

        View v = getView();

        recyclerView.setVisibility(View.GONE);
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.discover_progress_bar);
        final FrameLayout discoverLoadingLayout = (FrameLayout) v.findViewById(R.id.progress_view);
        discoverLoadingLayout.setVisibility(View.VISIBLE);

        final TextView noUsersTV = (TextView) v.findViewById(R.id.ongoing_no_users);

        ThreeBounce threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        String id = preferences.getString("_id", null);
//        String userId = preferences.getString("uid", null);


        String url = getResources().getString(R.string.base_url) + "/api/requests/" + "ongoing/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w("Ongoing Response", response.toString());

                        progressBar.setVisibility(View.GONE);
                        discoverLoadingLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        // Do something with response
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            JSONArray allItems = response;

                            if (allItems.length() == 0) {
                                noUsersTV.setVisibility(View.VISIBLE);
                            }

                            for (int i = 0; i < allItems.length(); i++) {

                                // Get current json object
                                JSONObject item = response.getJSONObject(i);
                                String jsonObjectString = item.toString();

//                                if (item.isNull("request")) {
//                                    continue;
//                                }

//                                Log.v("Discover", requestDets.toString());
//                                Log.v("Discover", requestDets.getString("distance"));


                                // Get the current student (json object) data
                                JSONObject ongoingDets = item.getJSONObject("acceptedUser");
                                String person = ongoingDets.getString("firstName");
                                String profilePicture = ongoingDets.getString("profilePicture");

                                String createdAt = item.getString("createdAt");
                                String title = item.getString("title");
                                String contactNumber = item.getString("contactNumber");
                                String ongoing_id = item.getString("_id");


//                                DecimalFormat df = new DecimalFormat("0.00");
//                                float dist = (float) item.getDouble("distance");
//                                float distance = Float.parseFloat(df.format(dist));

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                SimpleDateFormat timeExtract = new SimpleDateFormat("dd MMM" + ", " + "HH:mm a");

                                //  Changed Time to IST
                                Date date = dateFormat.parse(createdAt);
//                                timeExtract.setTimeZone(TimeZone.getTimeZone("IST"));
                                String time = timeExtract.format(date.getTime());

                                Log.w("ITEMS: ", title);
                                Log.w("ITEMS: ", person);
                                Log.w("ITEMS: ", createdAt);
                                Log.w("ITEMS: ", time);
                                Log.w("ITEMS: ", ongoing_id);

                                OnGoingDetails onGoingDetails = new OnGoingDetails(person, contactNumber, title, time, profilePicture, ongoing_id);
                                onGoingDetailsList.add(onGoingDetails);
                            }

                            onGoingDetailsAdapter = new OnGoingDetailsAdapter(getActivity(), onGoingDetailsList, TransactionOnGoingFragment.this);
                            recyclerView.setAdapter(onGoingDetailsAdapter);

                            int itemCount = recyclerView.getAdapter().getItemCount();
                            Log.w("ITEM COUNT", String.valueOf(itemCount));

                            if (itemCount > 0)
                            {
                                Intent transactionIntent = new Intent(getActivity(), TransactionService.class);
                                //        transactionIntent.putExtra("name", name);

                                getActivity().startService(transactionIntent);
                            }

//                            recyclerView.setVisibility(View.VISIBLE);

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        progressBar.setVisibility(View.GONE);
                        discoverLoadingLayout.setVisibility(View.GONE);

                        Log.w("RESPONSE ERROR", error);
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_id", id);

                return params;
            }
        };
        requestQueue.add(jsonArrayRequest);
        Log.w("_id", id);
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

        Toast.makeText(getActivity(), "Calling " + onGoingDetails.getOngoingPerson(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishButtonClick(int position) {

        onGoingDetailsList.get(position);
        OnGoingDetails onGoingDetails = onGoingDetailsList.get(position);

        finishRequest(onGoingDetails.getOngoing_id(), position);

//        onGoingDetailsList.remove(position);
//        onGoingDetailsAdapter.notifyItemRemoved(position);
//        onGoingDetailsAdapter.notifyDataSetChanged();
//
//        Toast.makeText(getActivity(), "Finishing request: " + onGoingDetails.getOngoingPerson(), Toast.LENGTH_SHORT).show();

    }

    void finishRequest(String ongoing_id, int position) {

        View v = getView();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        String id = preferences.getString("_id", null);
        String url = getResources().getString(R.string.base_url) + "/api/requests/" + ongoing_id;

        final TextView noUsersTV = (TextView) v.findViewById(R.id.ongoing_no_users);

        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("ServerResponse",
                        response.toString());

//                editor.putString("_id", currentUserId);
//                editor.commit();

                try {

                    boolean success = response.getBoolean("success");

//                    boolean userStatus = response.getBoolean("newUser");

                    if (success == true) {
                        onGoingDetailsList.remove(position);
                        onGoingDetailsAdapter.notifyItemRemoved(position);
                        onGoingDetailsAdapter.notifyDataSetChanged();

                        int itemCount = recyclerView.getAdapter().getItemCount();
                        Log.w("ITEM COUNT", String.valueOf(itemCount));

                        if (itemCount <= 0)
                        {
                            Intent transactionIntent = new Intent(getActivity(), TransactionService.class);
                            getActivity().stopService(transactionIntent);
                            noUsersTV.setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(getActivity(), "Transaction finished ", Toast.LENGTH_SHORT).show();
                    } else {
//                        mAuth.signOut();
                        Toast.makeText(getActivity(), "There was problem, Try again later", Toast.LENGTH_SHORT).show();
                    }

//                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
//                            task -> updateNotificationToken(task));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerError", error);
                Toast.makeText(getActivity(), "There was problem, Try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_id", id);

                return params;
            }
        };
        ;

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                onGoingDetailsList.clear();
//                loadDiscoverData();
            }
        }
    }
}