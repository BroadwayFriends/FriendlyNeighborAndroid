package me.twodee.friendlyneighbor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;



//    GoogleSignInClient mGoogleSignInClient;
    Button sign_out;
    TextView nameTV, emailTV;
    LinearLayout editProfileButton;

    MaterialCardView RequestPage, DiscoverPage, KarmaPage, RespondToPosts,PaymentPage;
    CardView myProfile;
    String personName, personEmail;
    ImageView displayImage;

    private static final String TAG = DashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_dashboard);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();



        sign_out = findViewById(R.id.sign_out_button);
        nameTV = findViewById(R.id.name);
//        emailTV = findViewById(R.id.email);
        editProfileButton = findViewById(R.id.editProfieButton);
        RequestPage = findViewById(R.id.RequestPage);
        PaymentPage = findViewById(R.id.PaymentPage);
        DiscoverPage = findViewById(R.id.DiscoverPage);
        KarmaPage = findViewById(R.id.KarmaPage);
        myProfile = findViewById(R.id.myProfile);
        RespondToPosts = findViewById(R.id.RespondToPosts);
        displayImage = findViewById(R.id.displayImage);
//        emailTV.setVisibility(View.GONE);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        fetchData();

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
            intent.putExtra("visitReason", "edit");
            startActivity(intent);

        });

        myProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
            intent.putExtra("visitReason", "view");
            startActivity(intent);

        });


        KarmaPage.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, KarmaPointsActivity.class);
            startActivity(intent);
        });

        RequestPage.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, postRequirementActivity.class);
            startActivity(intent);
        });

        PaymentPage.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BaseActivity.class);
            startActivity(intent);
        });

        DiscoverPage.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DiscoverActivity.class);
            startActivity(intent);
        });

        RespondToPosts.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.server_client_id))
//                .requestEmail()
//                .build();

        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DashboardActivity.this);
//        if (acct != null) {
//            personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//
//            nameTV.setText(String.format(personName));
//            emailTV.setText(String.format("Email: %s", personEmail));
////            idTV.setText("ID: "+personId);
////            Glide.with(this).load(personPhoto).into(photoIV);
//            Picasso.get().load(personPhoto).fit().centerInside().into(displayImage);
//
//        }


        sign_out.setOnClickListener(view -> signOut());
    }



    private void signOut() {
        mAuth.signOut();
        sendUserToLogin();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
        moveTaskToBack(true);
    }


    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        String userId = preferences.getString("_id", null);
        try {
            object.put("_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String baseUrl = getResources().getString(R.string.base_url) + "/api/users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, object,
                                                                    response -> {
                                                                        Log.w("ServerResponse", response.toString());

                                                                        try {
                                                                            JSONObject respObj = new JSONObject(
                                                                                    response.getString("user"));


//                        Toast.makeText(DashboardActivity.this, response.getString("name"), Toast.LENGTH_SHORT).show();
                                                                            nameTV.setText(respObj.getString("firstName"));
//                        emailTV.setText(respObj.getString("email"));
                                                                            String profilePictureUrl = respObj.getString(
                                                                                    "profilePicture");

                                                                                Picasso.get().load(
                                                                                        profilePictureUrl.isEmpty() ? null : profilePictureUrl).error(R.drawable.ppchange).fit().centerInside().into(
                                                                                        displayImage);



//                            editTextUsername.setText(response.getString("name"));
//                            editTextEmail.setText(response.getString("email"));
//                            editTextPhone.setText(response.getString("changedPhone"));
//                            editTextLocation.setText(response.getString("changedLocation"));
//                            editTextRadius.setText(response.getString("changedRadius"));

                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }


                                                                    }, error -> {
            Log.w("ServerError", error);

        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("_id", userId);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser == null) {
            sendUserToLogin();
        }
    }

    private void sendUserToLogin() {
        Intent loginIntent = new Intent(DashboardActivity.this, SignInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
