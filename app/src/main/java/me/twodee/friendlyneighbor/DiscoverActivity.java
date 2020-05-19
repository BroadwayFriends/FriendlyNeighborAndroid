package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverActivity extends AppCompatActivity {

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
        discoverDetailsList.add(
                new DiscoverDetails(
                        "Maggi",
                        "Request",
                        "Akhil",
                        "10:00 PM",
                        100));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Saabun",
                        "Giveaway",
                        "Agni",
                        "09:00 PM",
                        270));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Kitaab",
                        "Giveaway",
                        "Dediyaman",
                        "07:00 AM",
                        100.5f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Hathoda",
                        "Request",
                        "Ritwik",
                        "5:30 PM",
                        87.8f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Torch",
                        "Giveaway",
                        "Priyam",
                        "10:10 AM",
                        700));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Banyaan",
                        "Request",
                        "Akhil",
                        "10:00 PM",
                        100));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Mouse",
                        "Giveaway",
                        "Agni",
                        "09:00 PM",
                        270));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Eldoper",
                        "Giveaway",
                        "Dediyaman",
                        "07:00 AM",
                        100.5f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Ande",
                        "Request",
                        "Ritwik",
                        "5:30 PM",
                        87.8f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Jumping wire",
                        "Giveaway",
                        "Priyam",
                        "10:10 AM",
                        700));

//        loadDiscoverData();


        discoverDetailsAdapter = new DiscoverDetailsAdapter(this, discoverDetailsList);
        recyclerView.setAdapter(discoverDetailsAdapter);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String id = preferences.getString("_id", null);
        String userId = preferences.getString("uid", null);

        String url = "https://4112a99e.ngrok.io/api/requests/" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w("Discover Response", response.toString());

//                        // Do something with response
//                        //mTextView.setText(response.toString());
//
//                        // Process the JSON
//                        try{
//                            // Loop through the array elements
//                            for(int i=0;i<response.length();i++){
//                                // Get current json object
//                                JSONObject student = response.getJSONObject(i);
//
//                                // Get the current student (json object) data
//                                String firstName = student.getString("firstname");
//                                String lastName = student.getString("lastname");
//                                String age = student.getString("age");
//
//                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.w("ServerError", error);
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
}
