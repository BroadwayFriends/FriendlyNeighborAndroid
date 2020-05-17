package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    TextInputLayout address1, address2, city, state, country, pincode, contactNumber;
    Button submitButton;

    AwesomeValidation awesomeValidation;

    SharedPreferences preferences;

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
        address2 = findViewById(R.id.addressLine2);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        pincode = findViewById(R.id.pincode);
        contactNumber = findViewById(R.id.contact);
        submitButton = findViewById(R.id.submitButton);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        //Validations
        awesomeValidation.addValidation(this, R.id.addressLine1, RegexTemplate.NOT_EMPTY, R.string.invalid_address_1);
        awesomeValidation.addValidation(this, R.id.addressLine2, RegexTemplate.NOT_EMPTY, R.string.invalid_address_2);
        awesomeValidation.addValidation(this, R.id.city, RegexTemplate.NOT_EMPTY, R.string.invalid_city);
        awesomeValidation.addValidation(this, R.id.state, RegexTemplate.NOT_EMPTY, R.string.invalid_state);
        awesomeValidation.addValidation(this, R.id.country, RegexTemplate.NOT_EMPTY, R.string.invalid_country);
        awesomeValidation.addValidation(this, R.id.pincode, "[1-9][0-9]{5}$", R.string.invalid_pincode);
        awesomeValidation.addValidation(this, R.id.contact, "(0/91)?[7-9][0-9]{9}", R.string.invalid_contact_number);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {

                    addr1 = address1.getEditText().getText().toString();
                    addr2 = address2.getEditText().getText().toString();
                    cty = city.getEditText().getText().toString();
                    st = state.getEditText().getText().toString();
                    cntry = country.getEditText().getText().toString();
                    pc = Integer.parseInt(pincode.getEditText().getText().toString());
                    cno = contactNumber.getEditText().getText().toString();

                    sendRegistrationData();

                    Toast.makeText(RegistrationActivity.this, "Registration successful !!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void sendRegistrationData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        String userId = preferences.getString("_id", null);
        try {
            //input your API parameters
//            object.put("id", userId);  // hardcoded for the time being
            object.put("id", userId);
            object.put("address1", addr1);
            object.put("address2", addr2);
            object.put("city", cty);
            object.put("state", st);
            object.put("country", cntry);
            object.put("pincode", pc);
            object.put("contactNumber", cno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w("Regi Data", object.toString());

        // Enter the correct url for your api service site
        String url = "https://7a4d6a44.ngrok.io/api/users/register";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("ServerResponse", response.toString());

                        if(response.has("uid")) {
                            startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
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
