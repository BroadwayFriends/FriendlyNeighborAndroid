package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appyvet.materialrangebar.RangeBar;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class postRequirementActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextPhone,
            editTextDate, editTextDistance;

    private Button buttonSubmit,buttonImageUpload;
    private AwesomeValidation awesomeValidation;
    String title,description,distance,expirationDate,phoneNumber;
    private int mYear, mMonth, mDay;
//    private RangeBar rangebar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_requirement);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextDistance = (EditText) findViewById(R.id.editTextDistance);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
//        rangebar = findViewById(R.id.rangebar1);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonImageUpload = (Button) findViewById(R.id.btnUploadImage);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.editTextTitle, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.errorInTitle);
        awesomeValidation.addValidation(this, R.id.editTextDescription, "^(?!\\s*$).+", R.string.errorInDescription);
        awesomeValidation.addValidation(this, R.id.editTextPhone, "^[0-9]{10}$", R.string.errorInPhone);
        awesomeValidation.addValidation(this, R.id.editTextDistance, Range.closed(1, 20), R.string.errorInDistance);
//        awesomeValidation.addValidation(this, R.id.editTextDob, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.nameerror);

        buttonImageUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(postRequirementActivity.this, imageUploadActivity.class);
                startActivity(intent);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                submitForm();
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(postRequirementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });




//        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
//
//            }
//
//            @Override
//            public void onTouchEnded(RangeBar rangeBar) {
//
//            }
//
//            @Override
//            public void onTouchStarted(RangeBar rangeBar) {
//
//            }
//        });

    }



    private void submitForm() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successfull



        // Title,Description maybe(Why I need it, specifications), Tag, Distance, Paisa(not compulsory)


        if (awesomeValidation.validate()) {
             title = editTextTitle.getText().toString();
             description = editTextDescription.getText().toString();
             phoneNumber = editTextPhone.getText().toString();

             distance = editTextDistance.getText().toString();
             expirationDate = editTextDate.getText().toString();


//            Toast.makeditTextDate(this, "Validation Successfull", Toast.LENGTH_LONG).show();
            postData();

        }

    }



    public void postData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("title",title);
            object.put("description",description);
            object.put("phoneNumber",phoneNumber);
            object.put("expirationDate",expirationDate);
            object.put("distance",distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "https://httpbin.org/post";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("ServerResponse", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerError", error);;
            }
        });
        requestQueue.add(jsonObjectRequest);
    }



}
