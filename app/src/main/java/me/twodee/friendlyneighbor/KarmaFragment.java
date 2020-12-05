package me.twodee.friendlyneighbor;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

public class KarmaFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private Button addKarma;
    private TextView currentKarmaDisplay;
    private int topUp = 50;
    private int currentKarmaAmount = 200;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //return inflater.inflate(R.layout.fragment_karma, parent, false);
        View view = inflater.inflate(R.layout.fragment_karma, parent, false);

//        addKarma = view.findViewById(R.id.button);
        currentKarmaDisplay = view.findViewById(R.id.karma_points_value);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        ValueAnimator animator = ValueAnimator.ofInt(0, topUp);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentKarmaDisplay.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.setDuration(1500);
        animator.start();

//        addKarma.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ValueAnimator animator = ValueAnimator.ofInt(0, topUp);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        currentKarmaDisplay.setText(animation.getAnimatedValue().toString());
//                    }
//                });
//                animator.start();
//            }
//        });

    }

}