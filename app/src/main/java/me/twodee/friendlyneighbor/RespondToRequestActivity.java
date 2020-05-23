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

public class RespondToRequestActivity extends AppCompatActivity implements RespondToRequestAdapter.OnChoicePageDetailsClickListener {

    RecyclerView recyclerView;
    RespondToRequestAdapter choicePageDetailsAdapter;

    List<RespondToRequestDetails> choicePageDetailsList;

    SearchView searchView;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_respond_to_request);

        choicePageDetailsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.choicePage_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = (SearchView) findViewById(R.id.choicePage_search_view);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);


//        Adding dummy static data
        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Maggi",
                        "Request",
                        "Akhil",
                        "10:00 PM",
                        100,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Saabun",
                        "Giveaway",
                        "Agni",
                        "09:00 PM",
                        270,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Kitaab",
                        "Giveaway",
                        "Dediyaman",
                        "07:00 AM",
                        100.5f,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Hathoda",
                        "Request",
                        "Ritwik",
                        "5:30 PM",
                        87.8f,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Torch",
                        "Giveaway",
                        "Priyam",
                        "10:10 AM",
                        700,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Banyaan",
                        "Request",
                        "Akhil",
                        "10:00 PM",
                        100,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Mouse",
                        "Giveaway",
                        "Agni",
                        "09:00 PM",
                        270,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Eldoper",
                        "Giveaway",
                        "Dediyaman",
                        "07:00 AM",
                        100.5f,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Ande",
                        "Request",
                        "Ritwik",
                        "5:30 PM",
                        87.8f,""));

        choicePageDetailsList.add(
                new RespondToRequestDetails(
                        "Jumping wire",
                        "Giveaway",
                        "Priyam",
                        "10:10 AM",
                        700,""));

        choicePageDetailsAdapter = new RespondToRequestAdapter(this, choicePageDetailsList, this);
        recyclerView.setAdapter(choicePageDetailsAdapter);


//        loadChoicePageData();


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                choicePageDetailsAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

//    private void loadChoicePageData() {
//
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.choicePage_progress_bar);
//        final RelativeLayout choicePageLoadingLayout = (RelativeLayout) findViewById(R.id.choicePage_loading_layout);
//        choicePageLoadingLayout.setVisibility(View.VISIBLE);
//        ThreeBounce threeBounce = new ThreeBounce();
//        progressBar.setIndeterminateDrawable(threeBounce);
//        progressBar.setVisibility(View.VISIBLE);
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        final String id = preferences.getString("_id", null);
//        String userId = preferences.getString("uid", null);
//
//
//        String url = getResources().getString(R.string.base_url) + "/api/requests/" + userId;
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray choicePage) {
//                        Log.w("choicePage Response", choicePage.toString());
//
//                        progressBar.setVisibility(View.GONE);
//                        choicePageLoadingLayout.setVisibility(View.GONE);
//
//                        // Do something with choicePage
//                        // Process the JSON
//                        try{
//                            // Loop through the array elements
//                            JSONArray allItems = choicePage;
//                            for(int i=0; i<allItems.length(); i++){
//
//                                // Get current json object
//                                JSONObject item = choicePage.getJSONObject(i);
//                                String jsonObjectString = item.toString();
//
//                                JSONObject requestDets = item.getJSONObject("request");
//
//                                Log.w("REQDETS", requestDets.toString());
//
//                                // Get the current student (json object) data
//                                String title = requestDets.getString("title");
//                                String person = requestDets.getJSONObject("requestedBy").getString("name");
//                                String createdAt = requestDets.getString("createdAt");
//                                float cost = (float) requestDets.getInt("cost");
//                                String type = (cost != 0.0f) ? "Request" : "Giveaway";
//                                float distance = (float) item.getInt("distance");
//
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                SimpleDateFormat timeExtract = new SimpleDateFormat("dd/MM/yyyy" + ", " + "HH:mm a");
//                                Date date = dateFormat.parse(createdAt);
//                                String time = timeExtract.format(date.getTime());
//
//                                Log.w("ITEMS: ", title);
//                                Log.w("ITEMS: ", person);
//                                Log.w("ITEMS: ", createdAt);
//                                Log.w("ITEMS: ", String.valueOf(cost));
//                                Log.w("ITEMS: ", type);
//                                Log.w("ITEMS: ", time);
//                                Log.w("ITEMS: ", String.valueOf(distance));
//
//
//                                RespondToRequestDetails choicePageDetails = new RespondToRequestDetails(title, type, person, time, distance, jsonObjectString);
//                                choicePageDetailsList.add(choicePageDetails);
//                            }
//
//                            choicePageDetailsAdapter = new RespondToRequestAdapter(RespondToRequestActivity.this, choicePageDetailsList, RespondToRequestActivity.this);
//                            recyclerView.setAdapter(choicePageDetailsAdapter);
//
//                        }catch (JSONException | ParseException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener(){
//
//                    @Override
//                    public void onErrorResponse(VolleyError error){
//
////                        progressBar.setVisibility(View.GONE);
//                        choicePageLoadingLayout.setVisibility(View.GONE);
//
//                        Log.w("ServerError", error);
//                        Toast.makeText(RespondToRequestActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("_id", id);
//
//                return params;
//            }
//        };
//        requestQueue.add(jsonArrayRequest);
//        Log.w("_id", id);
//    }

    @Override
    public void onChoicePageDetailsClick(int position) {

        choicePageDetailsList.get(position);
        Intent intent = new Intent(this, PostDetailsActivity.class);
        RespondToRequestDetails selectedRespondToRequestDetails = choicePageDetailsList.get(position);
        String selectedJsonString = selectedRespondToRequestDetails.getChoicePageJsonResponse();
        intent.putExtra("jsonString", selectedJsonString);
        startActivity(intent);

        Log.w("Clicker Checker", String.valueOf(position));
    }
}
