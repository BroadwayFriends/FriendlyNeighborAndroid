package me.twodee.friendlyneighbor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText address1, searchRadius, city, state, country, pincode, contactNumber;
    Button submitButton,autoFillAddress;

    AwesomeValidation awesomeValidation;
    private LatLng finalPosition ;
    SharedPreferences preferences;
    int LAUNCH_LOCATION_ACTIVITY = 42;
    Geocoder geocoder;
    List<Address> addresses;
    String locatedRadius;

    private final String TAG = "regForm" ;
    String addr1, addr2, cty, st, cntry, cno;
    int pc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_registration);

        address1 = findViewById(R.id.addressLine1);
        searchRadius = findViewById(R.id.searchRadius);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        pincode = findViewById(R.id.pincode);
        contactNumber = findViewById(R.id.contact);
        submitButton = findViewById(R.id.submitButton);
        autoFillAddress = findViewById(R.id.autoFillAddress);
        address1.setFocusable(false);
        address1.setCursorVisible(false);
        city.setFocusable(false);
        city.setCursorVisible(false);
        state.setFocusable(false);
        state.setCursorVisible(false);
        country.setFocusable(false);
        country.setCursorVisible(false);
        pincode.setFocusable(false);
        pincode.setCursorVisible(false);
        searchRadius.setFocusable(false);
        searchRadius.setCursorVisible(false);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        //Validations
//        awesomeValidation.addValidation(this, R.id.addressLine1, RegexTemplate.NOT_EMPTY, R.string.invalid_address_1);
//        awesomeValidation.addValidation(this, R.id.addressLine2, RegexTemplate.NOT_EMPTY, R.string.invalid_address_2);
        awesomeValidation.addValidation(this, R.id.city, RegexTemplate.NOT_EMPTY, R.string.invalid_city);
        awesomeValidation.addValidation(this, R.id.state, RegexTemplate.NOT_EMPTY, R.string.invalid_state);
        awesomeValidation.addValidation(this, R.id.country, RegexTemplate.NOT_EMPTY, R.string.invalid_country);
        awesomeValidation.addValidation(this, R.id.pincode, "[1-9][0-9]{5}$", R.string.invalid_pincode);
        awesomeValidation.addValidation(this, R.id.contact, "(0/91)?[7-9][0-9]{9}", R.string.invalid_contact_number);


        autoFillAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, locationPickerActivity.class);
                startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {

                    addr1 = address1.getText().toString();
                    addr2 = searchRadius.getText().toString();
                    cty = city.getText().toString();
                    st = state.getText().toString();
                    cntry = country.getText().toString();
                    pc = Integer.parseInt(pincode.getText().toString());
                    cno = contactNumber.getText().toString();

                    sendRegistrationData();

//                    Toast.makeText(RegistrationActivity.this, "Registration successful !!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void sendRegistrationData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject defaultLocation = new JSONObject();
        JSONObject address = new JSONObject();

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String userId = preferences.getString("_id", null);
        try {
            object.put("id", userId);
            defaultLocation.put("latitude",finalPosition.latitude);
            defaultLocation.put("longitude",finalPosition.longitude);
            object.put("id", userId);
            address.put("addr", addr1);
            address.put("city", cty);
            address.put("state", st);
            address.put("country", cntry);
            address.put("pincode", pc);
            object.put("address",address);
            object.put("contactNumber", cno);
            object.put("defaultLocation",defaultLocation);
            object.put("defaultSearchRadius",Integer.parseInt(locatedRadius));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w("Regi Data", object.toString());

        final String id = preferences.getString("_id", null);
        String url = getResources().getString(R.string.base_url) + "/api/users/register";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                response -> {
                    Log.w("ServerResponse", response.toString());

                    if(response.has("uid")) {
                        startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
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
                headers.put("_id", id);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode ==  LAUNCH_LOCATION_ACTIVITY){
            try {
                if (resultCode == Activity.RESULT_OK) {

                    assert data != null;
                    geocoder = new Geocoder(RegistrationActivity.this,Locale.getDefault());
                    locatedRadius = data.getExtras().getString("radius");
                    searchRadius.setText(locatedRadius);
                    finalPosition = data.getExtras().getParcelable("finalPosition");
                    addresses = geocoder.getFromLocation(finalPosition.latitude, finalPosition.longitude, 1);
                    String locatedAddressLine1 = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    String locatedCity = addresses.get(0).getLocality();
                    String locatedState = addresses.get(0).getAdminArea();
                    String locatedCountry = addresses.get(0).getCountryName();
                    String locatedPostalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName();
                    Log.v(TAG,"Addr,"+locatedAddressLine1+"City,"+locatedState+"state,"+locatedState+"country,"+locatedCountry+"postalCode,"+locatedPostalCode);
                    address1.setText(locatedAddressLine1);
                    city.setText(locatedCity);
                    state.setText(locatedState);
                    country.setText(locatedCountry);
                    pincode.setText(locatedPostalCode);

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(RegistrationActivity.this, R.string.locationNotPickedError, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Log.v(TAG,e.getStackTrace().toString());
//                Toast.makeText(this, "Something went wrong: "+ e.getStackTrace(), Toast.LENGTH_LONG).show();

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
