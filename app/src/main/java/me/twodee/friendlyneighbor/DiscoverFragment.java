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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment implements DiscoverDetailsAdapter.OnDiscoverDetailsClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
    DiscoverDetailsAdapter discoverDetailsAdapter;

    List<DiscoverDetails> discoverDetailsList;

    SearchView searchView;

    SharedPreferences preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        preferences = this.getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        View v = getView();

        discoverDetailsList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.discover_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = (SearchView) v.findViewById(R.id.discover_search_view);

//        preferences = this.getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

        //Adding dummy static data
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Maggi",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Saabun",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Kitaab",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Hathoda",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Torch",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Banyaan",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Mouse",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Eldoper",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Ande",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f,
//                        null));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Jumping wire",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700,
//                        null));
//
//        discoverDetailsAdapter = new DiscoverDetailsAdapter(getActivity(), discoverDetailsList, this);
//        recyclerView.setAdapter(discoverDetailsAdapter);


        loadDiscoverData();


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                discoverDetailsAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    void loadDiscoverData() {

        View v = getView();

        recyclerView.setVisibility(View.GONE);
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.discover_progress_bar);
        final FrameLayout discoverLoadingLayout  = (FrameLayout) v.findViewById(R.id.progress_view);
        discoverLoadingLayout.setVisibility(View.VISIBLE);

        final TextView noUsersTV = (TextView) v.findViewById(R.id.discover_no_users);

        ThreeBounce threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        String id = preferences.getString("_id", null);
//        String userId = preferences.getString("uid", null);


        String url = getResources().getString(R.string.base_url) + "/api/requests/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w("Discover Response", response.toString());

                        progressBar.setVisibility(View.GONE);
                        discoverLoadingLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        // Do something with response
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            JSONArray allItems = response;

                            if (allItems.length() == 0) {
                                noUsersTV.setVisibility(View.VISIBLE);
                            }

                            for(int i=0; i<allItems.length(); i++){

                                // Get current json object
                                JSONObject item = response.getJSONObject(i);
                                String jsonObjectString = item.toString();

                                if (item.isNull("request")) {
                                    continue;
                                }

                                JSONObject requestDets = item.getJSONObject("request");

                                Log.v("Discover", requestDets.toString());
//                                Log.v("Discover", requestDets.getString("distance"));


                                // Get the current student (json object) data
                                String title = requestDets.getString("title");
                                String person = requestDets.getJSONObject("requestedBy").getString("firstName");
                                String createdAt = requestDets.getString("createdAt");
                                float cost = (float) requestDets.getInt("cost");
                                String type = requestDets.getString("requestType");

                                DecimalFormat df = new DecimalFormat("0.00");
                                float dist = (float) item.getDouble("distance");
                                float distance = Float.parseFloat(df.format(dist));

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                SimpleDateFormat timeExtract = new SimpleDateFormat("dd MMM" + ", " + "HH:mm a");

                                //  Changed Time to IST
                                Date date = dateFormat.parse(createdAt);
//                                timeExtract.setTimeZone(TimeZone.getTimeZone("IST"));
                                String time = timeExtract.format(date.getTime());

                                Log.w("ITEMS: ", title);
                                Log.w("ITEMS: ", person);
                                Log.w("ITEMS: ", createdAt);
                                Log.w("ITEMS: ", String.valueOf(cost));
                                Log.w("ITEMS: ", type);
                                Log.w("ITEMS: ", time);
                                Log.w("ITEMS: ", String.valueOf(distance));


                                DiscoverDetails discoverDetails = new DiscoverDetails(title, type, person, time, distance, jsonObjectString);
                                discoverDetailsList.add(discoverDetails);
                            }

                            discoverDetailsAdapter = new DiscoverDetailsAdapter(getActivity().getApplicationContext(), discoverDetailsList, DiscoverFragment.this::onDiscoverDetailsClick);
                            recyclerView.setAdapter(discoverDetailsAdapter);
//                            recyclerView.setVisibility(View.VISIBLE);

                        }catch (JSONException | ParseException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){

//                        progressBar.setVisibility(View.GONE);
                        discoverLoadingLayout.setVisibility(View.GONE);

                        Log.w("RESPONSE ERROR", error);
                        Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_LONG).show();
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
    public void onDiscoverDetailsClick(int position) {

        discoverDetailsList.get(position);
        Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
        DiscoverDetails selectedDiscoverDetails = discoverDetailsList.get(position);

        String selectedJsonString = selectedDiscoverDetails.getDiscoverJsonResponse();

//        Boolean responded = null;
//        responded = "false";

        intent.putExtra("jsonString", selectedJsonString);

        startActivityForResult(intent, 1);

        Log.w("Clicker Checker", String.valueOf(position));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                discoverDetailsList.clear();
                loadDiscoverData();
            }
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        discoverDetailsList.clear();
//        loadDiscoverData();
//    }
}