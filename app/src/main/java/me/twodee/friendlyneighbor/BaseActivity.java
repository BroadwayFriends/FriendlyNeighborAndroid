package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity  {

    private SharedPreferences preferences;
    private String userId;

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//
//        bottomNavigationBar
//                .addItem(new BottomNavigationItem(R.drawable.pin2, "Discover"))
//                .addItem(new BottomNavigationItem(R.drawable.pin3, "Karma"))
//                .addItem(new BottomNavigationItem(R.drawable.pin4, "Profile"))
//
//                .setFirstSelectedPosition(0)
//                .initialise();


        chipNavigationBar = findViewById(R.id.bottom_navigation_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_discover, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DiscoverFragment()).commit();
        bottomMenu();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        userId = preferences.getString("_id", null);





//        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
//            @Override
//            public void onTabSelected(int position) {
//                switch (position) {
//                    case 0:
//
//                        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
//                        ft0.replace(R.id.mainFrame, new DiscoverFragment());
//                        ft0.addToBackStack(null);
//                        ft0.commit();
//
//                        break;
//
//                    case 1:
//
//                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.mainFrame, new KarmaFragment());
//                        ft.addToBackStack(null);
//                        ft.commit();
//
//                        break;
//
//                    case 2:
//
//                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
//                        ft1.replace(R.id.mainFrame, new ProfileFragment());
//                        ft1.addToBackStack(null);
//                        ft1.commit();
//                        break;
//                }
//            }
//            @Override
//            public void onTabUnselected(int position) {
//            }
//            @Override
//            public void onTabReselected(int position) {
//            }
//        });
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_discover:
                        fragment = new DiscoverFragment();
                        break;

                    case R.id.bottom_nav_karma:
                        fragment = new KarmaFragment();
                        break;

                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    public HashMap<String, String> passDataToFrags(){
        HashMap<String, String> userData= new HashMap<>();
        userData.put("userId",userId);
        return userData;
    }

}