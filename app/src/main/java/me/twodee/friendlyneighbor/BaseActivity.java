package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        TextView textView = findViewById(R.id.textView3);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.pin2, "Discover"))
                .addItem(new BottomNavigationItem(R.drawable.pin3, "Karma"))
                .addItem(new BottomNavigationItem(R.drawable.pin4, "History"))

                .setFirstSelectedPosition(0)
                .initialise();


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
                        ft.commit();
                        break;

                    case 2:
                        textView.setText("Third");
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
}