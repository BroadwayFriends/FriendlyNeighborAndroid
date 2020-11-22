package me.twodee.friendlyneighbor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.twodee.friendlyneighbor.service.VolleyUtils;

public class OtpActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
//    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String mAuthVerificationId;

    private PinView mOtpText;
    private Button mVerifyBtn;

    private TextView mOtpDesc;

    private ProgressBar mOtpProgress;

    private TextView mOtpFeedback;

    private LinearLayout mResendOtp;

    private String phone;

    private String code;

    private String uuid;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

//        mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");

//        uuid = UUID.toString();

        uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.w("UUID", uuid);



        mOtpFeedback = findViewById(R.id.otp_form_feedback);
        mOtpProgress = findViewById(R.id.otp_progress_bar);


        mOtpText = findViewById(R.id.otp_pin_view);

        mVerifyBtn = findViewById(R.id.verify_btn);

        mVerifyBtn.setEnabled(false);
        mOtpProgress.setVisibility(View.VISIBLE);

        phone = getIntent().getStringExtra("PhoneNumber");
        mOtpDesc = (TextView) findViewById(R.id.otp_desc);
        mOtpDesc.setText("We have sent you an OTP" + "\nto " + phone);
        sendVerificationCode(phone);

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = mOtpText.getText().toString();
                Log.w("OTP Entered", otp);

                if (otp.isEmpty()) {

                    mOtpFeedback.setVisibility(View.VISIBLE);
                    mOtpFeedback.setText("Please fill in the form and try again.");
                    mResendOtp.setVisibility(View.VISIBLE);

                } else {

                    mOtpProgress.setVisibility(View.VISIBLE);
                    mVerifyBtn.setEnabled(false);
                    verifyVerificationCode(otp);

//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
//                    signInWithPhoneAuthCredential(credential);

//                    if(verified)
//                    {
//                        postData();
//                    }
                }

            }
        });

        mResendOtp = (LinearLayout) findViewById(R.id.resend_otp);
        mResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(OtpActivity.this, SignInActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

        preferences = getApplicationContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
        editor = preferences.edit();

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                OtpActivity.this,
                mCallbacks);
        Log.w("STATUS OF SMS", "Sent to " + phone);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String otp = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (otp != null) {
                Log.w("CODE SENT", otp);

//                //Disabling the button
//                mVerifyBtn.setEnabled(false);
//                mOtpProgress.setVisibility(View.VISIBLE);

                mOtpText.setText(otp);
            }

            //verifying the code
//            verifyVerificationCode(otp);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("PHONE VERIF", "Phone number verification failed on " + phone);
            mOtpFeedback.setText(e.getMessage());
            mResendOtp.setVisibility(View.VISIBLE);
            mOtpFeedback.setVisibility(View.VISIBLE);
            mOtpProgress.setVisibility(View.INVISIBLE);
            mVerifyBtn.setEnabled(false);
            Log.w("ERROR MSG", e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mAuthVerificationId = s;

            //Enabling the button
            mVerifyBtn.setEnabled(true);
            mOtpProgress.setVisibility(View.INVISIBLE);
        }
    };

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);

        Log.w("AUTH ID", mAuthVerificationId);
        Log.w("ENTERED OTP", otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);

//        if(verified)
//        {
//            postData();
//        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Log.w("TASK DEBUG ERROR 1", String.valueOf(task.isSuccessful()));
//                            sendUserToHome();
                            postData();
                        } else {
//                            invalidVerificationCode();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
//                                invalidVerificationCode();
                                Log.w("TASK DEBUG ERROR 2", String.valueOf(task.isSuccessful()));
                                Log.w("TASK DEBUG ERROR 2 EXP", task.getException());
//                                Log.w("TASK ERROR", FirebaseAuthInvalidCredentialsException.class.toString());
                                Toast.makeText(OtpActivity.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                                mOtpProgress.setVisibility(View.INVISIBLE);
                                mOtpFeedback.setVisibility(View.VISIBLE);
                                mOtpFeedback.setText("There was an error verifying OTP");
                                mResendOtp.setVisibility(View.VISIBLE);
                            }
                        }
//                        mOtpProgress.setVisibility(View.INVISIBLE);
//                        mVerifyBtn.setEnabled(true);
                    }
                });
    }

//    public void invalidVerificationCode() {
//        mOtpFeedback.setVisibility(View.VISIBLE);
////        mOtpFeedback.setText("There was an error verifying OTP");
//        mOtpFeedback.setText("Error Message");
//        mResendOtp.setVisibility(View.VISIBLE);
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mCurrentUser != null) {
////            sendUserToHome();
//
//        }
//    }

    public void sendUserToHome() {

        Log.w("INTENT STATUS", "Sending user to dashboard");

        Intent homeIntent = new Intent(OtpActivity.this, DashboardActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    public void sendUserToRegistration() {

        Log.w("INTENT STATUS", "Sending user to registration");

        Intent homeIntent = new Intent(OtpActivity.this, RegistrationActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    public void postData() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            //input your API parameters
            object.put("_id", currentUserId);
            object.put("contactNumber", phone);
            object.put("uuid", uuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w("Sign In Data", object.toString());

        // Enter the correct url for your api service site
        String url = getResources().getString(R.string.base_url) + "/api/users/login";

        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("ServerResponse",
                        response.toString());

                editor.putString("_id", currentUserId);
                editor.commit();

                try {

                    boolean isAlreadySignedIn = response.getBoolean("isAlreadySignedIn");

                    boolean userStatus = response.getBoolean("newUser");

                    if (isAlreadySignedIn == true) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getApplicationContext(), "You are already signed-in in another device.", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        if (userStatus == true) {
                            sendUserToRegistration();
                        } else {
                            sendUserToHome();
                        }
                    }

                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                            task -> updateNotificationToken(task));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerError", error);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    private void updateNotificationToken(Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "getInstanceId failed", task.getException());
            return;
        }

        String token = task.getResult().getToken();
        String userId = preferences.getString("_id", null);
        Log.d(TAG, "Updating token for user: " + userId);

        Map<Object, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("token", token);
        VolleyUtils.post(getApplicationContext(), getString(R.string.base_url) + "/api/notifications/register",
                data,
                OtpActivity::listenToResponse,
                OtpActivity::listenToError
        );
    }

    private static void listenToError(VolleyError volleyError) {
        Log.i(TAG, volleyError.toString());

    }

    private static void listenToResponse(JSONObject jsonObject) {
        Log.d(TAG, "Successful POST!");
        Log.i(TAG, jsonObject.toString());
    }

}
