package me.twodee.friendlyneighbor;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.material.snackbar.Snackbar;

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

public class HistoryActivity extends AppCompatActivity implements HistoryDetailsAdapter.OnChoicePageDetailsClickListener {

    RecyclerView recyclerView;
    HistoryDetailsAdapter choicePageDetailsAdapter;

    List<HistoryDetails> choicePageDetailsList;

    SearchView searchView;

    Dialog selectedDialog;

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
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Maggi",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100,""));

//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Saabun",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Kitaab",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Hathoda",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Torch",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Banyaan",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Mouse",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Eldoper",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Ande",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f,""));
//
//        choicePageDetailsList.add(
//                new HistoryDetails(
//                        "Jumping wire",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700,""));
//
        choicePageDetailsAdapter = new HistoryDetailsAdapter(this, choicePageDetailsList, this);
        recyclerView.setAdapter(choicePageDetailsAdapter);


        loadChoicePageData();


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

    private void loadChoicePageData() {

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.choicePage_progress_bar);
        final RelativeLayout choicePageLoadingLayout = (RelativeLayout) findViewById(R.id.choicePage_loading_layout);
        choicePageLoadingLayout.setVisibility(View.VISIBLE);

        final TextView noUsersTV = (TextView) findViewById(R.id.history_no_users);

        ThreeBounce threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String id = preferences.getString("_id", null);
        String userId = preferences.getString("uid", null);


//        String url = getResources().getString(R.string.base_url) + "/api/requests/" + userId;
        String url = getResources().getString(R.string.base_url) + "/api/requests/history/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray choicePage) {
                        Log.w("History Response", choicePage.toString());

                        progressBar.setVisibility(View.GONE);
                        choicePageLoadingLayout.setVisibility(View.GONE);

                        // Do something with choicePage
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            JSONArray allItems = choicePage;

                            if (allItems.length() == 0) {
                                noUsersTV.setVisibility(View.VISIBLE);
                            }

                            for(int i=0; i<allItems.length(); i++){

                                // Get current json object
                                JSONObject item = choicePage.getJSONObject(i);
                                String jsonObjectString = item.toString();

                                JSONObject requestDets = item.getJSONObject("request");
                                JSONArray usersDets = item.getJSONArray("users");

                                Log.w("REQDETS", requestDets.toString());

                                // Get the current student (json object) data
                                String title = requestDets.getString("title");
                                boolean completed = requestDets.getBoolean("completed");
                                String acceptedUser = requestDets.getString("acceptedUser");
                                String createdAt = requestDets.getString("createdAt");
//                                float cost = (float) requestDets.getInt("cost");
                                String type = requestDets.getString("requestType");


                                String requestId = requestDets.getString("_id");

                                String jsonUsersArr = usersDets.toString();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                SimpleDateFormat timeExtract = new SimpleDateFormat("dd/MM/yyyy" + ", " + "HH:mm a");
                                Date date = dateFormat.parse(createdAt);
                                String time = timeExtract.format(date.getTime());

                                Log.w("ITEMS: ", title);
                                Log.w("ITEMS: ", requestId);
//                                Log.w("ITEMS: ", createdAt);
//                                Log.w("ITEMS: ", String.valueOf(cost));
                                Log.w("ITEMS: ", type);
                                Log.w("ITEMS: ", time);
//                                Log.w("ITEMS: ", String.valueOf(distance));


                                HistoryDetails choicePageDetails = new HistoryDetails(title, type, time, jsonUsersArr, completed, acceptedUser, requestId);
                                choicePageDetailsList.add(choicePageDetails);
                            }

                            choicePageDetailsAdapter = new HistoryDetailsAdapter(HistoryActivity.this, choicePageDetailsList, HistoryActivity.this);
                            recyclerView.setAdapter(choicePageDetailsAdapter);

                        }catch (JSONException | ParseException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){

//                        progressBar.setVisibility(View.GONE);
                        choicePageLoadingLayout.setVisibility(View.GONE);

                        Log.w("ServerError", error);
                        Toast.makeText(HistoryActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
    public void onChoicePageDetailsClick(int position) {

        choicePageDetailsList.get(position);

        HistoryDetails selectedHistoryDetails = choicePageDetailsList.get(position);

        if(selectedHistoryDetails.getChoicePageCompleted()) {
            Snackbar snackbar = Snackbar.make(recyclerView, "Already Accepted", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Intent intent = new Intent(this, HistoryPostDetailsActivity.class);
            String selectedJsonString = selectedHistoryDetails.getJsonUsersArray();
            intent.putExtra("jsonString", selectedJsonString);
            intent.putExtra("title", selectedHistoryDetails.getChoicePageTitle());
            intent.putExtra("type", selectedHistoryDetails.getChoicePageType());
            intent.putExtra("completed", selectedHistoryDetails.getChoicePageCompleted());
            intent.putExtra("requestId", selectedHistoryDetails.getChoicePageRequestId());

            startActivityForResult(intent, 1);
        }

//        openDialog(position);

        Log.w("Clicker Checker", String.valueOf(position));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                choicePageDetailsList.clear();
                loadChoicePageData();
            }
        }
    }

    void openDialog(int position) {

        selectedDialog = new Dialog(this);
        selectedDialog.setContentView(R.layout.dialog_selected_history);
        selectedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView dialog_name = (TextView)selectedDialog.findViewById(R.id.dialog_history_name);
        TextView dialog_info = (TextView)selectedDialog.findViewById(R.id.dialog_history_info);
        Button dialogAcceptButton = (Button) selectedDialog.findViewById(R.id.dialog_history_accept_btn);
        Button dialogDeclineButton = (Button) selectedDialog.findViewById(R.id.dialog_history_decline_btn);

//        dialog_name.setText(choicePageDetailsList.get(position).getChoicePagePerson());
        dialog_info.setText(choicePageDetailsList.get(position).getChoicePageTitle());

        dialogAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HistoryActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
            }
        });

        dialogDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HistoryActivity.this, "Declined", Toast.LENGTH_SHORT).show();
            }
        });

        selectedDialog.show();
    }
}
