package me.twodee.friendlyneighbor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.maps.model.LatLng;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText address1, searchRadius, username, email, country, firstName, lastName, pincode;
    Button submitButton;

    AwesomeValidation awesomeValidation;
    private LatLng finalPosition;
    int LAUNCH_LOCATION_ACTIVITY = 42;
    Geocoder geocoder;
    List<Address> addresses;
    String locatedRadius;

    private final String TAG = "regForm";
    private Boolean CHANGED_PICTURE_FLAG = Boolean.FALSE;
    String addr1, addr2, em, uname, fname, lname;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_registration);
        address1 = findViewById(R.id.addressLine1);
        searchRadius = findViewById(R.id.searchRadius);
        lastName = findViewById(R.id.last_name);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pincode = findViewById(R.id.pincode);
        firstName = findViewById(R.id.name);
        firstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        submitButton = findViewById(R.id.submitButton);
//        autoFillAddress = findViewById(R.id.autoFillAddress);
//        country.setVisibility(View.GONE);
        pincode.setVisibility(View.GONE);
        address1.setFocusable(false);
        address1.setCursorVisible(false);
//        country.setFocusable(false);
//        country.setCursorVisible(false);
        pincode.setFocusable(false);
        pincode.setCursorVisible(false);
        searchRadius.setFocusable(false);
        searchRadius.setCursorVisible(false);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        //Validations
        awesomeValidation.addValidation(this, R.id.username, "^[a-zA-Z0-9._-]{3,}$", R.string.invalid_username);
        awesomeValidation.addValidation(this, R.id.email, "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.pincode, "[1-9][0-9]{5}$", R.string.invalid_pincode);
        awesomeValidation.addValidation(this, R.id.name, RegexTemplate.NOT_EMPTY, R.string.invalid_fname);
        awesomeValidation.addValidation(this, R.id.last_name, RegexTemplate.NOT_EMPTY, R.string.invalid_lname);

        address1.setOnClickListener(v -> autoFill());
        searchRadius.setOnClickListener(v -> autoFill());

        submitButton.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                addr1 = address1.getText().toString();
                addr2 = searchRadius.getText().toString();
                em = email.getText().toString();
                uname = username.getText().toString();
                fname = firstName.getText().toString();
                lname = lastName.getText().toString();
                sendRegistrationData();
            } else {
                Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        preferences = getApplicationContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
        editor = preferences.edit();
    }

    void sendRegistrationData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject defaultLocation = new JSONObject();
        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String _id = preferences.getString("_id", null);
        try {
            object.put("_id", _id);
            defaultLocation.put("latitude", finalPosition.latitude);
            defaultLocation.put("longitude", finalPosition.longitude);
            object.put("firstName", fname);
            object.put("lastName", lname);
            object.put("email", em);
            object.put("username", uname);
            object.put("address", addr1);
            object.put("defaultLocation", defaultLocation);
            object.put("defaultSearchRadius", Integer.parseInt(locatedRadius));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        final String id = preferences.getString("_id", null);
        String url = getResources().getString(R.string.base_url) + "/api/users/register";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                response -> {
                    Log.w("ServerResponse", response.toString());
                    try {

                        editor.putString("email",
                                response.getJSONObject(
                                        "user").getString(
                                        "email"));
                        editor.putString("firstName",
                                response.getJSONObject(
                                        "user").getString(
                                        "firstName"));
                        editor.putString("lastName",
                                response.getJSONObject(
                                        "user").getString(
                                        "lastName"));
                        editor.putString("contactNumber",
                                response.getJSONObject(
                                        "user").getString(
                                        "contactNumber"));

                        boolean committed = editor.commit();

                        String idReceived = preferences.getString(
                                "_id", null);
                        String emailReceived = preferences.getString(
                                "email", null);
                        String firstNameReceived = preferences.getString(
                                "firstName", null);
                        String lastNameReceived = preferences.getString(
                                "lastName", null);
                        String contactNumberReceived = preferences.getString(
                                "contactNumber", null);

                        Log.w("SP Status",
                                String.valueOf(committed));
                        Log.w("Shared Preferences Data",
                                idReceived);
//                            Log.w("Shared Preferences Data", uidReceived);
                        Log.w("Shared Preferences Data",
                                emailReceived);
                        Log.w("Shared Preferences Data",
                                firstNameReceived);
                        Log.w("Shared Preferences Data",
                                lastNameReceived);
                        Log.w("Shared Preferences Data",
                                contactNumberReceived);

                        if(response.getBoolean("success")) {
                            sendUserToHome();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.w("ServerError", error);
            ;
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("_id", _id);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    protected void autoFill() {
        Intent i = new Intent(RegistrationActivity.this, locationPickerActivity.class);
        startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LAUNCH_LOCATION_ACTIVITY) {
            try {
                if (resultCode == Activity.RESULT_OK) {

                    assert data != null;
                    geocoder = new Geocoder(RegistrationActivity.this, Locale.getDefault());
                    locatedRadius = data.getExtras().getString("radius");
                    searchRadius.setText(locatedRadius);
                    finalPosition = data.getExtras().getParcelable("finalPosition");
                    addresses = geocoder.getFromLocation(finalPosition.latitude, finalPosition.longitude, 1);
                    String locatedAddressLine1 = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    address1.setText(locatedAddressLine1);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(RegistrationActivity.this, R.string.locationNotPickedError, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.v(TAG, e.getStackTrace().toString());
//                Toast.makeText(this, "Something went wrong: "+ e.getStackTrace(), Toast.LENGTH_LONG).show();

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void sendUserToHome() {

        Log.w("INTENT STATUS", "Sending user to dashboard");

        Intent homeIntent = new Intent(RegistrationActivity.this, OnboardingActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
