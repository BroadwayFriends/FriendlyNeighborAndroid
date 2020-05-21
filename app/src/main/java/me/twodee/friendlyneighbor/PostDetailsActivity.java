package me.twodee.friendlyneighbor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    TextView selectedTitle, selectedDescription, selectedPostedBy, selectedMinutesAway;
    ImageView profilePictureView;

    TextView bottomSheetName;
    ImageView bottonSheetProfilePicture;

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
        String profilePicture = null;
        double time = 0;

        List<SliderItem> sliderItems = new ArrayList<>();

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
            time = value.getInt("distance") / 0.25;         //Since, average bicycle speed in 15kmph with is equal to 0.25 kmpmin

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

        } catch (JSONException e) {
            Log.w("JSON_ERROR", e);
        }

        selectedTitle = (TextView) findViewById(R.id.selected_title);
        selectedDescription = (TextView) findViewById(R.id.selected_description);
        selectedPostedBy = (TextView) findViewById(R.id.discover_posted_by);
        profilePictureView = (ImageView) findViewById(R.id.postDetails_profile_picture);
        selectedMinutesAway = (TextView) findViewById(R.id.profile_minutes_away);

        selectedTitle.setText(title);
        selectedDescription.setText(description);
        selectedPostedBy.setText(postedBy);
        Picasso.get().load(profilePicture).fit().centerInside().into(profilePictureView);
        selectedMinutesAway.setText(String.valueOf((int)time) + " minutes away");


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


//        bottomSheetName.setText("HELLO");
//        Picasso.get().load(profilePicture).fit().centerInside().into(bottonSheetProfilePicture);


        CardView authorDetails = (CardView)findViewById(R.id.authorDetails);
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
}
