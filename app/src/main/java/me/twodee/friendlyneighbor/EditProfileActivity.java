package me.twodee.friendlyneighbor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    int LAUNCH_LOCATION_ACTIVITY = 877;
    Button editProfileButton;
    EditText editTextEmail ,editTextPhone,editTextUsername,editTextRadius, editTextLocation;
    SharedPreferences preferences;
    private String TAG = "editProfilePage";
    Geocoder geocoder ;
    private LatLng finalPosition ;
    List<Address> addresses;
    private String changedEmail, changedPhone, changedLocation,changedUsername,changedRadius;
    private static final String baseUrl = "";
    ArrayList<String> userData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_edit_profile);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editProfileButton = (Button) findViewById(R.id.editProfileButton);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextRadius = (EditText) findViewById(R.id.editTextRadius);
        editTextLocation.setFocusable(false);
        editTextLocation.setCursorVisible(false);

//        fetchData();

//        editProfileButton.setClickable(false);
//        editProfileButton.setAlpha(.4f);




        editTextUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                changedUsername = editTextUsername.getText().toString() ;
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onProfileEdit();

            }
        });


        editTextEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                changedEmail =editTextEmail.getText().toString() ;

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onProfileEdit();

            }
        });


        editTextPhone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                changedPhone = editTextPhone.getText().toString() ;
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onProfileEdit();

            }
        });

        editTextRadius.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                changedRadius = editTextRadius.getText().toString() ;
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onProfileEdit();

            }
        });


        editTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, locationPickerActivity.class);
                startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
                onProfileEdit();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });


    }

    private void onProfileEdit(){

        editProfileButton.setClickable(true);
        editProfileButton.setAlpha(1f);


//        editTextEmail.getText();
//        JSONObject userDetailsObj = new JSONObject();
//        try {
//            userDetailsObj.put("username", "3");
//            userDetailsObj.put("phoneNumber", "NAME OF STUDENT");
//            userDetailsObj.put("email", "3rd");
//            userDetailsObj.put("location", "Arts");
//
//
//
//        } catch (JSONException e) {
//            Log.v(TAG , String.format("%s", e.getMessage() )) ;
//        }
//


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode ==  LAUNCH_LOCATION_ACTIVITY){
            try {
                if (resultCode == Activity.RESULT_OK) {

                    assert data != null;
                    geocoder = new Geocoder(EditProfileActivity.this, Locale.getDefault());
                    String locatedRadius = data.getExtras().getString("radius");
                    editTextRadius.setText(locatedRadius);
                    finalPosition = data.getExtras().getParcelable("finalPosition");
                    addresses = geocoder.getFromLocation(finalPosition.latitude, finalPosition.longitude, 1);
                    String locatedAddressLine1 = addresses.get(0).getAddressLine(0);
                    Log.v(TAG,"Addr,"+locatedAddressLine1);
                    editTextLocation.setText(locatedAddressLine1);
                    editTextRadius.setText(locatedRadius);

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(EditProfileActivity.this, R.string.locationNotPickedError, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {

//                Toast.makeText(this, "Something went wrong: ", Toast.LENGTH_LONG).show();
                Log.e(TAG,e.getMessage());

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        String userId = preferences.getString("_id", null);
        try {
            object.put("id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://httpbin.org/post";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("ServerResponse", response.toString());

                        try {
                            editTextUsername.setText(response.getString("changedUsername"));
                            editTextEmail.setText(response.getString("changedEmail"));
                            editTextPhone.setText(response.getString("changedPhone"));
                            editTextLocation.setText(response.getString("changedLocation"));
                            editTextRadius.setText(response.getString("changedRadius"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerError", error);
                ;
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void updateData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        String userId = preferences.getString("_id", null);
        try {

            object.put("id", userId);
            object.put("changedUsername", changedUsername);
            object.put("changedEmail", changedEmail);
            object.put("changedPhone", changedPhone);
            object.put("changedLocation", changedLocation);
            object.put("changedRadius", changedRadius);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://httpbin.org/post";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("ServerResponse", response.toString());
                        if(response.has("update_successful")) {
                            Toast.makeText(EditProfileActivity.this,"Successfully Updated Profile",Toast.LENGTH_SHORT).show();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerError", error);
                ;
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
