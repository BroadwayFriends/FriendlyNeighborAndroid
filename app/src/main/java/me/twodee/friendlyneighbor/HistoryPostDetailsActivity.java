package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HistoryPostDetailsActivity<RecyclerViewAdapter> extends AppCompatActivity {

    RecyclerView itemsContainerRV;
    HistoryPostDetailsAdapter itemAdapter;

    List<String> data = new ArrayList();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_post_details);

        itemsContainerRV = (RecyclerView) findViewById(R.id.itemsContainerRV);
        itemsContainerRV.setHasFixedSize(true);
        itemsContainerRV.setLayoutManager(new LinearLayoutManager(this));


        for (int i = 1; i <= 20; i++) {
            data.add("Item " + i);
        }

        Log.w("Data Items", data.toString());

        itemsContainerRV = findViewById(R.id.itemsContainerRV);
        itemAdapter = new HistoryPostDetailsAdapter(HistoryPostDetailsActivity.this, data);
        itemAdapter.notifyDataSetChanged();
        itemsContainerRV.setAdapter(itemAdapter);

        SwipeHelper swipeHelper = new SwipeHelper(this) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Decline",
                        0,
                        Color.parseColor("#e57373"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final String item = itemAdapter.getData().get(pos);
                                itemAdapter.removeItem(pos);

                                Snackbar snackbar = Snackbar.make(itemsContainerRV, "Declined", Snackbar.LENGTH_LONG);
                                snackbar.setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        itemAdapter.restoreItem(item, pos);
                                        itemsContainerRV.scrollToPosition(pos);
                                    }
                                });

                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Accept",
                        0,
                        Color.parseColor("#81C784"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(getApplicationContext(), "Item accpeted " + pos, Toast.LENGTH_LONG).show();
                            }
                        }
                ));

//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Share",
//                        0,
//                        Color.parseColor("#C7C7CB"),
//                        new SwipeHelper.UnderlayButtonClickListener() {
//                            @Override
//                            public void onClick(int pos) {
//                                Toast.makeText(getApplicationContext(), "You clicked share on item position " + pos, Toast.LENGTH_LONG).show();
//                            }
//                        }
//                ));
            }
        };
        swipeHelper.attachToRecyclerView(itemsContainerRV);
    }
}