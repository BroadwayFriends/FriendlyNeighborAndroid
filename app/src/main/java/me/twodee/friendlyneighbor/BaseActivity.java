package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity  {
    private SharedPreferences preferences;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        TextView textView = findViewById(R.id.textView3);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.pin2, "Discover"))
                .addItem(new BottomNavigationItem(R.drawable.pin3, "Karma"))
                .addItem(new BottomNavigationItem(R.drawable.pin4, "Profile"))

                .setFirstSelectedPosition(0)
                .initialise();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        userId = preferences.getString("_id", null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();



        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        textView.setText("First");

                        break;

                    case 1:
                        textView.setText("Second");

                        ft.replace(R.id.mainFrame, new KarmaFragment());
                        ft.addToBackStack(null);
                        ft.commit();

                        break;

                    case 2:
                        textView.setText("Third");
                        ft.replace(R.id.mainFrame, new ProfileFragment());
                        ft.commit();
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }
    public HashMap<String, String> passDataToFrags(){
        HashMap<String, String> userData= new HashMap<>();
        userData.put("userId",userId);
        return userData;
    }

}