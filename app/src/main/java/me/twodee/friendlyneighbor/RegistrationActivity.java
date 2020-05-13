package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    TextInputLayout addr1;
    TextInputLayout addr2;
    TextInputLayout city;
    TextInputLayout state;
    TextInputLayout country;
    TextInputLayout pincode;
    TextInputLayout contactNumber;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_registration);

        addr1 = findViewById(R.id.addressLine1);
        addr2 = findViewById(R.id.addressLine2);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        pincode = findViewById(R.id.pincode);
        contactNumber = findViewById(R.id.contact);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
            }
        });
    }

    void checkDataEntered() {
        if (isEmpty(addr1)) {
//            Toast.makeText(RegistrationActivity.this, "Address Line 1 cannot be empty !", Toast.LENGTH_LONG).show();
            addr1.setError("Address Line 1 Required");
        }

        if (isEmpty(city)) {
//            Toast.makeText(RegistrationActivity.this, "City cannot be empty !", Toast.LENGTH_LONG).show();
            city.setError("City Required");
        }

        if (isEmpty(state)) {
//            Toast.makeText(RegistrationActivity.this, "State cannot be empty !", Toast.LENGTH_LONG).show();
            state.setError("State Required");
        }

        if (isEmpty(country)) {
//            Toast.makeText(RegistrationActivity.this, "Country cannot be empty !", Toast.LENGTH_LONG).show();
            country.setError("Country Required");
        }


        if (!Pattern.matches("[1-9][0-9]{5}$", pincode.getEditText().getText().toString())) {
//            Toast.makeText(RegistrationActivity.this, "Invalid Pin Code !", Toast.LENGTH_LONG).show();
            pincode.setError("Invalid Pin Code");
        }

//        if (pincode.toString().matches("^[0-9]{6}$")) {
//            Toast.makeText(RegistrationActivity.this, "Invalid Pin code !", Toast.LENGTH_LONG).show();
//        }

        if (!Pattern.matches("(0/91)?[7-9][0-9]{9}", contactNumber.getEditText().getText().toString())) {
//            Toast.makeText(RegistrationActivity.this, "Invalid Contact Number !", Toast.LENGTH_LONG).show();
            contactNumber.setError("Invalid Contact Number");
        }
    }

    boolean isEmpty(TextInputLayout text) {
        CharSequence str = text.getEditText().getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isPinCode(TextInputLayout text) {
        CharSequence pincode = text.getEditText().getText().toString();
        return (!TextUtils.isEmpty(pincode) && !Pattern.matches("[0-9]{6}$", pincode));
    }

    boolean isContact(TextInputLayout text) {
        CharSequence con = text.getEditText().getText().toString();
        return (!TextUtils.isEmpty(con) && Patterns.PHONE.matcher(con).matches());
    }
}
