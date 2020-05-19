package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {


    Button editProfileButton;
    EditText editTextEmail ,editTextPhone,editTextLocation,editTextUsername;
    private String TAG = "editProfilePage";
    private String changedEmail, changedPhone, changedLocation,changedUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_edit_profile);

        editProfileButton = (Button) findViewById(R.id.editProfileButton);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
//        editProfileButton.setClickable(false);
//        editProfileButton.setAlpha(.4f);




        editTextUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                changedUsername = s.toString() ;
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
                changedEmail = s.toString() ;

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
                changedPhone = s.toString() ;
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onProfileEdit();

            }
        });

        editTextLocation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                onProfileEdit();


            }
        });

    }

    private void onProfileEdit(){

        editProfileButton.setClickable(false);
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
}
