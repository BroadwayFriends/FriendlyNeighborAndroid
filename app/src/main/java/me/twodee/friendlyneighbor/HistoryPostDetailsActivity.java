package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPostDetailsActivity extends AppCompatActivity {

    TextView selectedTitleTV, selectedTypeTV;

    RecyclerView itemsContainerRV;
    HistoryPostDetailsAdapter itemAdapter;

    SharedPreferences preferences;

    List<HistoryRespondedUserDetails> data;

    String requestId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_post_details);

        itemsContainerRV = (RecyclerView) findViewById(R.id.itemsContainerRV);
        itemsContainerRV.setHasFixedSize(true);
        itemsContainerRV.setLayoutManager(new LinearLayoutManager(this));


        data = new ArrayList();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        //JSON data from previous activity
        String strValue = getIntent().getStringExtra("jsonString");

        String title = getIntent().getStringExtra("title");
        String type = getIntent().getStringExtra("type");
        boolean accepted = getIntent().getBooleanExtra("completed", false);
        requestId = getIntent().getStringExtra("requestId");

        selectedTitleTV = (TextView) findViewById(R.id.history_post_item_title);
        selectedTypeTV = (TextView) findViewById(R.id.history_post_item_type);

        selectedTitleTV.setText(title);
        selectedTypeTV.setText(type);

        JSONArray userArray = null;

        String name = null;
        String profilePicture = null;
        String contactNumber = null;
        String id = null;

        final TextView noResponseTV = (TextView) findViewById(R.id.history_post_no_response);

        //Parsing JSON Data
        try {

            userArray = new JSONArray(strValue);

            Log.w("USER ARRAY", userArray.toString());

            if(userArray.length() == 0) {
                noResponseTV.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < userArray.length(); i++) {
                // Get current json object
                JSONObject item = userArray.getJSONObject(i);

                name = item.getString("name");
                contactNumber = item.getString("contactNumber");
                profilePicture = item.getString("profilePicture");
                id = item.getString("_id");

                HistoryRespondedUserDetails hruUserDetails = new HistoryRespondedUserDetails(name, contactNumber, profilePicture, id, accepted);
                data.add(hruUserDetails);

            }

            itemAdapter = new HistoryPostDetailsAdapter(HistoryPostDetailsActivity.this, data);
            itemsContainerRV.setAdapter(itemAdapter);
            itemsContainerRV.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

//        for (int i = 1; i <= 20; i++) {
//            data.add("Item " + i);
//        }


        Log.w("Data Items", data.toString());

//        itemsContainerRV = findViewById(R.id.itemsContainerRV);
//        itemAdapter = new HistoryPostDetailsAdapter(HistoryPostDetailsActivity.this, data);
//        itemAdapter.notifyDataSetChanged();
//        itemsContainerRV.setAdapter(itemAdapter);


        SwipeHelper swipeHelper = new SwipeHelper(this) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Decline",
                        0,
                        Color.parseColor("#f44336"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
//                                final String item = itemAdapter.getData().get(pos);
//                                itemAdapter.removeItem(pos);

                                declineRequest(data.get(pos).getRU_id());

                                Snackbar snackbar = Snackbar.make(itemsContainerRV, "Declined " + data.get(pos).getRU_id(), Snackbar.LENGTH_LONG);
                                snackbar.setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        itemAdapter.restoreItem(item, pos);
//                                        itemsContainerRV.scrollToPosition(pos);
                                    }
                                });

                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Accept",
                        0,
                        Color.parseColor("#4CAF50"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                                acceptRequest(data.get(pos).getRU_id());

                                Toast.makeText(getApplicationContext(), "Item accpeted " + data.get(pos).getRU_id(), Toast.LENGTH_LONG).show();

                            }
                        }
                ));
            }
        };
        swipeHelper.attachToRecyclerView(itemsContainerRV);
    }

    void declineRequest(String userReqId) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String id = preferences.getString("_id", null);

        String userId = userReqId;


//        String url = getResources().getString(R.string.base_url) + "/api/requests/" + requestId + "/respond/" + id;
        String url = getResources().getString(R.string.base_url) + "/api/requests/" + requestId + "/respond/" + userId;

        Log.w("REQUEST ID", requestId);
        Log.w("_ID", userId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w("Respond Response", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respond Error", "Error: " + error.getMessage());
                Log.e("Respond Error", "Site Info Error: " + error.getMessage());
                Toast.makeText(HistoryPostDetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("_id", id);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    void acceptRequest(String userReqId) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String id = preferences.getString("_id", null);

        String userId = userReqId;


//        String url = getResources().getString(R.string.base_url) + "/api/requests/" + requestId + "/respond/" + id;
        String url = getResources().getString(R.string.base_url) + "/api/requests/" + requestId + "/respond/" + userId;

        Log.w("REQUEST ID", requestId);
        Log.w("_ID", userId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH,url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w("Respond Response", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respond Error", "Error: " + error.getMessage());
                Log.e("Respond Error", "Site Info Error: " + error.getMessage());
                Toast.makeText(HistoryPostDetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("_id", id);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }



    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}