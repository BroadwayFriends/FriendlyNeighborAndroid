package me.twodee.friendlyneighbor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.*;
import com.kusu.loadingbutton.LoadingButton;

public class MainActivity extends AppCompatActivity
{
    //Button next, signInPage, dashboard, register, discover, payment, profile, postDetails, historyButton, postReqButton;
//    LoadingButton loadingButton;

    private static int SPLASH_SCREEN = 5000;

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
        image = findViewById(R.id.imageView);
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

        // Initialize the SDK
        BMSClient.getInstance().initialize(this, BMSClient.REGION_SYDNEY);
        //Initialize client Push SDK

        MFPPush push = MFPPush.getInstance();
        push.initialize(getApplicationContext(), getString(R.string.ibm_push_app_guid), getString(R.string.ibm_push_client_secret));


        push.registerDevice(new MFPPushResponseListener<String>() {

            @Override
            public void onSuccess(String response) {
                //handle successful device registration here
                Log.i(TAG, "Successful registration of device");
            }

            @Override
            public void onFailure(MFPPushException ex) {
                //handle failure in device registration here
                ex.printStackTrace();
            }
        });
        String userId = "abc123";
        push.registerDeviceWithUserId(userId, new MFPPushResponseListener<String>() {

            @Override
            public void onSuccess(String response) {
                //handle successful device registration here
                Log.i(TAG, "Successful registration of device with UID: " + userId);
            }

            @Override
            public void onFailure(MFPPushException ex) {
                //handle failure in device registration here
                ex.printStackTrace();
            }
        });

        notificationListener = new MFPPushNotificationListener() {
            @Override
            public void onReceive(final MFPSimplePushNotification message) {
                Log.i(TAG, "Received a Push Notification: " + message.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("Received a Push Notification")
                                .setMessage(message.getAlert())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                                .show();
                    }
                });
            }
        };
        push.listen(notificationListener);

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
//                Intent intent = new Intent(MainActivity.this, RespondToRequestActivity.class);
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

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(push != null) {
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
