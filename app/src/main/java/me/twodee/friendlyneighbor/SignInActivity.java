package me.twodee.friendlyneighbor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;



public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private CountryCodePicker mCountryCode;
    private EditText mPhoneNumber;

    private Button mGenerateOTP;
    private ProgressBar mLoginProgress;

    String completePhoneNumber;

    String code;

    String authCred;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static final String TAG = SignInActivity.class.getSimpleName();


    // RequestQueue For Handle Network Request
//    RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_sign_in);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mCountryCode = (CountryCodePicker) findViewById(R.id.country_code_text);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number_text);
        mGenerateOTP = (Button) findViewById(R.id.generate_btn);
        mLoginProgress = (ProgressBar) findViewById(R.id.login_progress_bar);

        mGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = mCountryCode.getSelectedCountryCodeWithPlus();
                String phoneNumber = mPhoneNumber.getText().toString();
                completePhoneNumber = countryCode + "" + phoneNumber;

                if(phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                    mPhoneNumber.setError("Invalid number");
                } else {
                    mLoginProgress.setVisibility(View.VISIBLE);
                    mGenerateOTP.setEnabled(false);
                    sendUserToOtpVerification();
//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            completePhoneNumber,
//                            60,
//                            TimeUnit.SECONDS,
//                            SignInActivity.this,
//                            mCallbacks
//                    );
                }

            }
        });

//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                //Getting the code sent by SMS
//                code = phoneAuthCredential.getSmsCode();
//                }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Log.w("PHONE VERIF", "Phone number verification failed on " + completePhoneNumber);
//                mLoginFeedbackText.setText("Verification failed. Please try again.");
//                mLoginFeedbackText.setVisibility(View.VISIBLE);
//                mLoginProgress.setVisibility(View.INVISIBLE);
//                mGenerateOTP.setEnabled(true);
//                Log.w("ERROR MSG", e.getMessage());
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//
//                authCred = s;
//
//                new android.os.Handler().postDelayed(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                sendUserToOtpVerification();
//                            }
//                        }
//                , 10000);
//            }
//        };


//        preferences = getApplicationContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
//        editor = preferences.edit();


    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mCurrentUser != null) {
            sendUserToHome();
        }
    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            //sendUserToHome();
//                            sendUserToOtpVerification();
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                mLoginFeedbackText.setVisibility(View.VISIBLE);
//                                mLoginFeedbackText.setText("There was an error verifying OTP");
//                            }
//                        }
//                        mLoginProgress.setVisibility(View.INVISIBLE);
//                        mGenerateOTP.setEnabled(true);
//                    }
//                });
//    }

    public void sendUserToHome() {

        Log.w("INTENT STATUS", "Sending user to dashboard");

        Intent homeIntent = new Intent(SignInActivity.this, DashboardActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    public void sendUserToOtpVerification() {

        mLoginProgress.setVisibility(View.INVISIBLE);
        mGenerateOTP.setEnabled(true);
        Intent otpIntent = new Intent(SignInActivity.this, OtpActivity.class);
//        otpIntent.putExtra("AuthCredentials", authCred);
//        otpIntent.putExtra("Code", code);
        otpIntent.putExtra("PhoneNumber", completePhoneNumber);
        Log.w("PHONE: ", completePhoneNumber);
        startActivity(otpIntent);
        finish();
    }

}