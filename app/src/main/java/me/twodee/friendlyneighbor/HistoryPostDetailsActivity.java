package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HistoryPostDetailsActivity<RecyclerViewAdapter> extends AppCompatActivity {

    RecyclerView itemsContainerRV;
    RecyclerViewAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> data = new ArrayList();
        for (int i = 1; i <= 20; i++) {
            data.add("Item " + i);
        }

        itemsContainerRV = findViewById(R.id.itemsContainerRV);
        itemAdapter = new HistoryPostDetailsAdapter(this, data);
        itemAdapter.notifyDataSetChanged();
        itemsContainerRV.setAdapter(itemAdapter);

        SwipeHelper swipeHelper = new SwipeHelper(this) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final String item = itemAdapter.getData().get(pos);
                                itemAdapter.removeItem(pos);

                                Snackbar snackbar = Snackbar.make(itemsContainerRV, "Item was removed from the list.", Snackbar.LENGTH_LONG);
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
                        "Like",
                        0,
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(getApplicationContext(), "You clicked like on item position " + pos, Toast.LENGTH_LONG).show();
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Share",
                        0,
                        Color.parseColor("#C7C7CB"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(getApplicationContext(), "You clicked share on item position " + pos, Toast.LENGTH_LONG).show();
                            }
                        }
                ));
            }
        };
        swipeHelper.attachToRecyclerView(itemsContainerRV);
    }
}