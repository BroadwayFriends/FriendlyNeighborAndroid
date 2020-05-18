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
        discoverDetailsList.add(
                new DiscoverDetails(
                        "Request",
                        "Akhil",
                        "10:00 PM",
                        100));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Giveaway",
                        "Agni",
                        "09:00 PM",
                        270));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Giveaway",
                        "Dediyaman",
                        "07:00 AM",
                        100.5f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Request",
                        "Ritwik",
                        "5:30 PM",
                        87.8f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Giveaway",
                        "Priyam",
                        "10:10 AM",
                        700));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Request",
                        "Akhil",
                        "10:00 PM",
                        100));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Giveaway",
                        "Agni",
                        "09:00 PM",
                        270));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Giveaway",
                        "Dediyaman",
                        "07:00 AM",
                        100.5f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Request",
                        "Ritwik",
                        "5:30 PM",
                        87.8f));

        discoverDetailsList.add(
                new DiscoverDetails(
                        "Giveaway",
                        "Priyam",
                        "10:10 AM",
                        700));


        discoverDetailsAdapter = new DiscoverDetailsAdapter(this, discoverDetailsList);
        recyclerView.setAdapter(discoverDetailsAdapter);
    }
}
