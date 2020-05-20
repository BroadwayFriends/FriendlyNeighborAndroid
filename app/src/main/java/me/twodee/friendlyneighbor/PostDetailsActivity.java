package me.twodee.friendlyneighbor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    TextView selectedTitle, selectedDescription, selectedPostedBy;

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
        String strValue = getIntent().getStringExtra("jsonString");
        JSONObject value = null;

        try {
            value = new JSONObject(strValue);

            Log.w("Selected JSON", value.toString());

            title = value.getString("title");
            description = value.getString("description");
            postedBy = value.getJSONObject("requestedBy").getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectedTitle = (TextView) findViewById(R.id.selected_title);
        selectedDescription = (TextView) findViewById(R.id.selected_description);
        selectedPostedBy = (TextView) findViewById(R.id.discover_posted_by);

        selectedTitle.setText(title);
        selectedDescription.setText(description);
        selectedPostedBy.setText(postedBy);





        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.test1));
        sliderItems.add(new SliderItem(R.drawable.test2));
        sliderItems.add(new SliderItem(R.drawable.test3));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

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

        CardView authorDetails = (CardView)findViewById(R.id.authorDetails);
        authorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PostDetailsActivity.this, R.style.BottomSheet);
                View bottomSheet = LayoutInflater.from(getApplicationContext()).inflate(R.layout.profile_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetContent));
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
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
