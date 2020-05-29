package me.twodee.friendlyneighbor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kusu.loadingbutton.LoadingButton;
import com.snov.timeagolibrary.PrettyTimeAgo;
import com.squareup.picasso.Picasso;

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

public class PostDetailsActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    String strValue = "";
    String requestId = null;

    TextView selectedTitle, selectedDescription, selectedPostedBy, selectedCost, selectedTimeAgo;
    ImageView profilePictureView;

    LinearLayout goBack;

    TextView bottomSheetName;
    ImageView bottonSheetProfilePicture;

    LoadingButton respondButton;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_post_details);

        String title = null;
        String description = null;
        String postedBy = null;
        JSONArray imageUrl = null;
        JSONArray respondedByArray = null;
        String profilePicture = null;
        double cost = 0;
        String creationtime = null;
        String timeAgo = null;

        List<SliderItem> sliderItems = new ArrayList<>();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        //JSON data from previous activity
        String strValue = getIntent().getStringExtra("jsonString");

        JSONObject value = null;
        JSONObject value2 = null;

        //Parsing JSON data
        try {
            value = new JSONObject(strValue);
            value2 = value.getJSONObject("request");

            Log.w("Selected JSON", value.toString());

            title = value2.getString("title");
            description = value2.getString("description");
            postedBy = value2.getJSONObject("requestedBy").getString("name");
            profilePicture = value2.getJSONObject("requestedBy").getString("profilePicture");
            imageUrl = value2.getJSONArray("images");

            cost = (double) value2.getInt("cost");


            creationtime = value2.getString("createdAt");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat timeExtract = new SimpleDateFormat("dd/MM/yyyy" + ", " + "HH:mm a");
            Date date = dateFormat.parse(creationtime);
            long tago = date.getTime();
            long now = System.currentTimeMillis();

            timeAgo = PrettyTimeAgo.getTimeAgo(tago);

            Log.w("TIME IN MILI", String.valueOf(tago));
            Log.w("TIME AGO", timeAgo);

            int sizeImageArray = imageUrl.length();

            if (sizeImageArray == 0) {
                sliderItems.add(new SliderItem("noimage"));
            } else {
                for (int i = 0; i < sizeImageArray; i++) {
                    JSONObject imageDetails = imageUrl.getJSONObject(i);
                    String iurl = imageDetails.getString("imageURL");
                    sliderItems.add(new SliderItem(iurl));
                }
            }

        } catch (JSONException | ParseException | NullPointerException e) {
            Log.w("JSON_ERROR", e);
        }

        goBack = (LinearLayout) findViewById(R.id.postDetails_goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        selectedTitle = (TextView) findViewById(R.id.selected_title);
        selectedDescription = (TextView) findViewById(R.id.selected_description);
        selectedPostedBy = (TextView) findViewById(R.id.discover_posted_by);
        profilePictureView = (ImageView) findViewById(R.id.postDetails_profile_picture);
        selectedCost = (TextView) findViewById(R.id.profile_cost);
        selectedTimeAgo = (TextView) findViewById(R.id.postDetails_time_ago);

        respondButton = (LoadingButton) findViewById(R.id.postDetails_respond_button);

        selectedTitle.setText(title);
        selectedDescription.setText(description);
        selectedPostedBy.setText(postedBy);
        Picasso.get().load(profilePicture).fit().centerInside().into(profilePictureView);

        String costDisplay = (cost != 0.0) ? "₹" + String.valueOf(cost) : "Free";

        selectedCost.setText(costDisplay);


        selectedTimeAgo.setText(timeAgo);


        viewPager2 = findViewById(R.id.viewPagerImageSlider);


        viewPager2.setAdapter(new SliderAdapter(this, sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        CardView authorDetails = (CardView) findViewById(R.id.authorDetails);
        final String finalProfilePicture = profilePicture;
        final String finalPostedBy = postedBy;
        authorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PostDetailsActivity.this, R.style.BottomSheet);

                Log.w("BottomSheet", finalPostedBy);
                Log.w("BottomSheet", finalProfilePicture);

                View bottomSheet = LayoutInflater.from(getApplicationContext()).inflate(R.layout.profile_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetContent));

                bottomSheetName = (TextView) bottomSheet.findViewById(R.id.profile_bottom_sheet_name);
                bottonSheetProfilePicture = (ImageView) bottomSheet.findViewById(R.id.profile_bottom_sheet_profile_picture);

                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
                bottomSheetName.setText(finalPostedBy);
                Picasso.get().load(finalProfilePicture).fit().centerInside().into(bottonSheetProfilePicture);
            }
        });

        final String id = preferences.getString("_id", null);

        try {
            respondedByArray = value2.getJSONArray("respondedBy");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w("Ids in Array", respondedByArray.toString());
        Log.w("Ids find", id);

        boolean found = false;
        for (int i = 0; i < respondedByArray.length(); i++) {
            try {
                if (respondedByArray.getString(i).equals(id))
                    found = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (found) {
            respondButton.setEnabled(false);
            respondButton.setButtonText("Already Responded");
        } else {
            respondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        respondData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

//

    void respondData() throws JSONException {

        respondButton.showLoading();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        //JSON data from previous activity
        String strValue = getIntent().getStringExtra("jsonString");

        JSONObject value = null;
        JSONObject value2 = null;

        //Parsing JSON Data
        value = new JSONObject(strValue);
        value2 = value.getJSONObject("request");

        requestId = value2.getString("_id");

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String id = preferences.getString("_id", null);
        String userId = preferences.getString("uid", null);


        String url = getResources().getString(R.string.base_url) + "/api/requests/" + requestId + "/respond/" + id;
//        String url = "https://988d618d.ngrok.io" + "/api/requests/" + requestId + "/respond/" + id;

        Log.w("REQUEST ID", requestId);
        Log.w("_ID", id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w("Respond Response", response.toString());

                try {
                    if(response.getBoolean("success")) {
                        respondButton.hideLoading();
                        respondButton.setButtonText("Done");
                        respondButton.setEnabled(false);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respond Error", "Error: " + error.getMessage());
                Log.e("Respond Error", "Site Info Error: " + error.getMessage());
                Toast.makeText(PostDetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                respondButton.hideLoading();

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

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

}
