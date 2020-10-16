package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Check if it is the first time

        if (restorePrefData()) {
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class );
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_onboarding);

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Request","Need a screwdriver and the shops are closed? Bought an extra pack of eggs and want to sell them? Make a new request or an offer and find people in your area who can match your needs!",R.drawable.ic_img2));
        mList.add(new ScreenItem("Discover","Check out what's going on in one stop. Delivered live right to your discover, you can check out what people are buying and selling in your area.",R.drawable.ic_img1));
        mList.add(new ScreenItem("Collect","Find out the best response to your offering or request and meet them up at your convenience. We make sure they are close enough to your place so you don't have to hire a delivery agency. Use your legs or your webs to swing by.",R.drawable.ic_img3));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // next button click Listener

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);


                }

                if (position == mList.size()-1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loaddLastScreen();


                }



            }
        });
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // Get Started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Open main activity

                Intent mainActivity = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(mainActivity);
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                savePrefsData();
                finish();
            }
        });

        // skip button click listener

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });



    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);



    }
}
