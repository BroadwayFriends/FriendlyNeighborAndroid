package me.twodee.friendlyneighbor;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.twodee.friendlyneighbor.service.TransactionService;

public class KarmaFragment extends Fragment {

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    //    private Button addKarma;
    private TextView currentKarmaDisplay, currentRequestMadeValue, currentHelpCompletedValue;
    private int topUp = 50;
    private int currentKarmaAmount = 200;


    SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = this.getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //return inflater.inflate(R.layout.fragment_karma, parent, false);
//        View view = inflater.inflate(R.layout.fragment_karma, parent, false);

        return inflater.inflate(R.layout.fragment_karma, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        loadKarmaPoints();

        currentKarmaDisplay = view.findViewById(R.id.karma_points_value);
        currentRequestMadeValue = view.findViewById(R.id.request_made_value);
        currentHelpCompletedValue = view.findViewById(R.id.help_completed_value);

//        ValueAnimator animator = ValueAnimator.ofInt(0, topUp);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentKarmaDisplay.setText(animation.getAnimatedValue().toString());
//            }
//        });
//        animator.setDuration(1500);
//        animator.start();

    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // Setup any handles to view objects here
//        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
//
//        ValueAnimator animator = ValueAnimator.ofInt(0, topUp);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentKarmaDisplay.setText(animation.getAnimatedValue().toString());
//            }
//        });
//        animator.setDuration(1500);
//        animator.start();
//    }


    void loadKarmaPoints() {

        View v = getView();

//        recyclerView.setVisibility(View.GONE);
//        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.discover_progress_bar);
//        final FrameLayout discoverLoadingLayout = (FrameLayout) v.findViewById(R.id.progress_view);
//        discoverLoadingLayout.setVisibility(View.VISIBLE);

//        final TextView noUsersTV = (TextView) v.findViewById(R.id.ongoing_no_users);

//        ThreeBounce threeBounce = new ThreeBounce();
//        progressBar.setIndeterminateDrawable(threeBounce);
//        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        String id = preferences.getString("_id", null);
//        String userId = preferences.getString("uid", null);


        String url = getResources().getString(R.string.base_url) + "/api/users/karma/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("Karma Response", response.toString());

//                        progressBar.setVisibility(View.GONE);
//                        discoverLoadingLayout.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);

                        // Do something with response
                        // Process the JSON
                        try {
                            int karmaPoints = response.getInt("karmaPoints");
                            int requestsMade = response.getInt("requestsMade");
                            int helpsCompleted = response.getInt("completedRequests");


                            ValueAnimator animator = ValueAnimator.ofInt(0, karmaPoints);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    currentKarmaDisplay.setText(animation.getAnimatedValue().toString());
                                }
                            });
                            animator.setDuration(1500);
                            animator.start();

                            ValueAnimator animator2 = ValueAnimator.ofInt(0, requestsMade);
                            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    currentRequestMadeValue.setText(animation.getAnimatedValue().toString());
                                }
                            });
                            animator2.setDuration(1500);
                            animator2.start();

                            ValueAnimator animator3 = ValueAnimator.ofInt(0, helpsCompleted);
                            animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    currentHelpCompletedValue.setText(animation.getAnimatedValue().toString());
                                }
                            });
                            animator3.setDuration(1500);
                            animator3.start();


                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        progressBar.setVisibility(View.GONE);
//                        discoverLoadingLayout.setVisibility(View.GONE);

                        Log.w("RESPONSE ERROR", error);
                        Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_id", id);

                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        Log.w("_id", id);
    }

}