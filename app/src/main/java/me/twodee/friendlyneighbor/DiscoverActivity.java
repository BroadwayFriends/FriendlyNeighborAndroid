package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverActivity extends AppCompatActivity implements DiscoverDetailsAdapter.OnDiscoverDetailsClickListener {

    RecyclerView recyclerView;
    DiscoverDetailsAdapter discoverDetailsAdapter;

    List<DiscoverDetails> discoverDetailsList;

    SearchView searchView;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_discover);

        discoverDetailsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.discover_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = (SearchView) findViewById(R.id.discover_search_view);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);


        //Adding dummy static data
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Maggi",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Saabun",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Kitaab",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Hathoda",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Torch",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Banyaan",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Mouse",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Eldoper",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Ande",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Jumping wire",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700));
//
//        discoverDetailsAdapter = new DiscoverDetailsAdapter(this, discoverDetailsList, this);
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

    private void loadDiscoverData() {

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.discover_progress_bar);
        final RelativeLayout discoverLoadingLayout = (RelativeLayout) findViewById(R.id.discover_loading_layout);
        discoverLoadingLayout.setVisibility(View.VISIBLE);
        ThreeBounce threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String id = preferences.getString("_id", null);
        String userId = preferences.getString("uid", null);


        String url = getResources().getString(R.string.agni_url) + "/api/requests/" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w("Discover Response", response.toString());

                        progressBar.setVisibility(View.GONE);
                        discoverLoadingLayout.setVisibility(View.GONE);

                        // Do something with response
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            JSONArray allItems = response;
                            for(int i=0; i<allItems.length(); i++){

                                // Get current json object
                                JSONObject item = response.getJSONObject(i);
                                String jsonObjectString = item.toString();

                                // Get the current student (json object) data
                                String title = item.getString("title");
                                String person = item.getJSONObject("requestedBy").getString("name");
                                String createdAt = item.getString("createdAt");
                                float cost = (float) item.getInt("cost");
                                String type = (cost != 0.0f) ? "Request" : "Giveaway";

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                SimpleDateFormat timeExtract = new SimpleDateFormat("dd/MM/yyyy" + ", " + "HH:mm a");
                                Date date = dateFormat.parse(createdAt);
                                String time = timeExtract.format(date.getTime());

                                Log.w("ITEMS: ", title);
                                Log.w("ITEMS: ", person);
                                Log.w("ITEMS: ", createdAt);
                                Log.w("ITEMS: ", String.valueOf(cost));
                                Log.w("ITEMS: ", type);
                                Log.w("ITEMS: ", time);


                                DiscoverDetails discoverDetails = new DiscoverDetails(title, type, person, time, cost, jsonObjectString);
                                discoverDetailsList.add(discoverDetails);
                            }

                            discoverDetailsAdapter = new DiscoverDetailsAdapter(DiscoverActivity.this, discoverDetailsList, DiscoverActivity.this);
                            recyclerView.setAdapter(discoverDetailsAdapter);

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

                        Log.w("ServerError", error);
                        Toast.makeText(DiscoverActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this, PostDetailsActivity.class);
        DiscoverDetails selectedDiscoverDetails = discoverDetailsList.get(position);
        String selectedJsonString = selectedDiscoverDetails.getDiscoverJsonResponse();
        intent.putExtra("jsonString", selectedJsonString);
        startActivity(intent);

        Log.w("Clicker Checker", String.valueOf(position));
    }
}
