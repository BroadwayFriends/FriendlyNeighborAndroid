package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class DiscoverActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DiscoverDetailsAdapter discoverDetailsAdapter;

    List<DiscoverDetails> discoverDetailsList;

//    GET REQUEST to https://4112a99e.ngrok.io/api/requests/<uid_comes_here>

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


        //Adding dummy static data
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Maggi",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Saabun",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Kitaab",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Hathoda",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Torch",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Banyaan",
//                        "Request",
//                        "Akhil",
//                        "10:00 PM",
//                        100));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Mouse",
//                        "Giveaway",
//                        "Agni",
//                        "09:00 PM",
//                        270));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Eldoper",
//                        "Giveaway",
//                        "Dediyaman",
//                        "07:00 AM",
//                        100.5f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Ande",
//                        "Request",
//                        "Ritwik",
//                        "5:30 PM",
//                        87.8f));
//
//        discoverDetailsList.add(
//                new DiscoverDetails(
//                        "Jumping wire",
//                        "Giveaway",
//                        "Priyam",
//                        "10:10 AM",
//                        700));

        loadDiscoverData();


        discoverDetailsAdapter = new DiscoverDetailsAdapter(this, discoverDetailsList);
        recyclerView.setAdapter(discoverDetailsAdapter);
    }

    private void loadDiscoverData() {

    }
}
