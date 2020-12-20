package me.twodee.friendlyneighbor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;

public class MainActivity extends AppCompatActivity {
    //Button next, signInPage, dashboard, register, discover, payment, profile, postDetails, historyButton, postReqButton;
//    LoadingButton loadingButton;

    private static final int SPLASH_SCREEN = 5000;

    Animation bottomAnim;
    ImageView image;
    TextView logo, slogan;

    private static final String TAG = MainActivity.class.getSimpleName();

    private MFPPush push; // Push client
    private MFPPushNotificationListener notificationListener; // Notification listener to handle a push sent to the phone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splash_bottom_animation);

        //Hooks
//        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        }, SPLASH_SCREEN);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        String channelId = getString(R.string.notification_channel_id);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                                                                  "Channel human readable title",
                                                                  NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
    }
//        discover = findViewById(R.id.discover);
//        discover.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, DiscoverActivity.class);
//                startActivity(intent);
//            }
//        });

//        next = findViewById(R.id.next);
//        next.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//
//                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
//
//                startActivity(intent);
//            }
//        });

//        signInPage = findViewById(R.id.sign_in_page);
//        signInPage.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//                startActivity(intent);
//            }
//        });

//        dashboard = findViewById(R.id.dashoard);
//        dashboard.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                startActivity(intent);
//            }
//        });

//        register = findViewById(R.id.register);
//        register.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
//                startActivity(intent);
//            }
//        });

//        payment = findViewById(R.id.payment);
//        payment.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
//                startActivity(intent);
//            }
//        });

//        profile = findViewById(R.id.profile);
//        profile.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
//                startActivity(intent);
//            }
//        });

//        postDetails = findViewById(R.id.postDetails);
//        postDetails.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, PostDetailsActivity.class);
//                startActivity(intent);
//            }
//        });

//        historyButton = findViewById(R.id.historyButton);
//        historyButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
//                startActivity(intent);
//            }
//        });

//        postReqButton = findViewById(R.id.post_req_button);
//        postReqButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, postRequirementActivity.class);
//                startActivity(intent);
//            }
//        });

//        loadingButton = (LoadingButton) findViewById(R.id.loadingButton);
//
//        final Boolean[] isLoading = {false};
//
//        loadingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(!isLoading[0]) {
//                    loadingButton.showLoading();
//                    isLoading[0] = true;
//                }
//                else {
//                    loadingButton.hideLoading();
//                    isLoading[0] = false;
//                    loadingButton.setButtonText("Completed");
//                    loadingButton.setEnabled(false);
//                }
//            }
//        });

    @Override
    protected void onResume() {
        super.onResume();
        if (push != null) {
            push.listen(notificationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (push != null) {
            push.hold();
        }
    }

}
