package me.twodee.friendlyneighbor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

//import android.support.v7.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    String idToken;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    // RequestQueue For Handle Network Request
//    RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_sign_in);

        preferences = getApplicationContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
        editor = preferences.edit();


        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void postData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("idToken",idToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w("Sign In Data", object.toString());

        // Enter the correct url for your api service site
        String url = getResources().getString(R.string.base_url) + "/api/users/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("ServerResponse", response.toString());

                        try {


                            editor.putString("uid", response.getJSONObject("user").getString("_id"));

//                            editor.putString("uid", response.getJSONObject("user").getString("uid"));

                            editor.putString("_id", response.getJSONObject("user").getString("_id"));
                            editor.putString("email", response.getJSONObject("user").getString("email"));
                            editor.putString("name", response.getJSONObject("user").getString("name"));
//                            editor.apply();
                            boolean committed = editor.commit();
                            boolean userStatus = response.getBoolean("newUser");

                            if (userStatus == true) {
                                startActivity(new Intent(SignInActivity.this, RegistrationActivity.class));
                            } else {
                                startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                            }

                            String idReceived = preferences.getString("_id", null);
                            String uidReceived = preferences.getString("uid", null);
                            String emailReceived = preferences.getString("email", null);
                            String nameReceived = preferences.getString("name", null);
                            Log.w("SP Status", String.valueOf(committed));
                            Log.w("Shared Preferences Data", idReceived);
//                            Log.w("Shared Preferences Data", uidReceived);
                            Log.w("Shared Preferences Data", emailReceived);
                            Log.w("Shared Preferences Data", nameReceived);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerError", error);;
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            idToken = account.getIdToken();
            Log.w("ID_TOKEN", idToken);

            Log.w("Sign In Data", idToken.toString());

            postData();

            // Signed in successfully, show authenticated UI.
//            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {

            idToken = account.getIdToken();
            Log.w("ID_TOKEN", idToken);

            postData();

//            Toast.makeText(SignInActivity.this, "Already Signed In !!!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
//            finish();
        }

        super.onStart();
    }
}