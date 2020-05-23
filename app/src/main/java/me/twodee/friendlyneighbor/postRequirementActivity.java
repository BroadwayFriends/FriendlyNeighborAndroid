package me.twodee.friendlyneighbor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Range;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class postRequirementActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextPhone,
            editTextDate, editTextDistance,editTextAddress,editTextPrice;

    private Button buttonSubmit,buttonImageUpload;
    private AwesomeValidation awesomeValidation;
    private Switch switchPrice;
    private Spinner spinnerChooseLocation;
    String title,description,radius,expirationDate,phoneNumber,imageEncoded,fullAddress,priceQuote;
    private int mYear, mMonth, mDay;
    //Images
    int PICK_IMAGE_MULTIPLE = 1;
    List<String> imagesEncodedList;
    private final String TAG = "request" ;
    private boolean IMAGE_FLAG = false;
    ArrayList<String> imageUriArray = new ArrayList<>();
    //Location
    int LAUNCH_LOCATION_ACTIVITY = 42;
    LatLng finalPosition;


    //    private RangeBar rangebar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_post_requirement);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextDistance = (EditText) findViewById(R.id.editTextDistance);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextPrice.setVisibility(View.GONE);
        switchPrice = findViewById(R.id.switchPrice);
        spinnerChooseLocation = (Spinner)findViewById(R.id.spinnerChooseLocation);
        editTextAddress.setVisibility(View.GONE);
        editTextDistance.setVisibility(View.GONE);


        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonImageUpload = (Button) findViewById(R.id.btnUploadImage);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.editTextTitle, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.errorInTitle);
        awesomeValidation.addValidation(this, R.id.editTextDescription, "^(?!\\s*$).+", R.string.errorInDescription);
        awesomeValidation.addValidation(this, R.id.editTextPhone, "^[0-9]{10}$", R.string.errorInPhone);
//        awesomeValidation.addValidation(this, R.id.editTextDistance, Range.closed(1, 20), R.string.errorInDistance);
//        awesomeValidation.addValidation(this, R.id.editTextDob, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.nameerror);


//        TODO: TAKE DATA FROM PREVIOUS ACTIVITY (uID,phoneNumber,Home location,distance)


        buttonImageUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Pictures"), PICK_IMAGE_MULTIPLE);


            }
        });

        switchPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchPrice.isChecked())
                {
                    editTextPrice.setVisibility(View.VISIBLE);
                    editTextPrice.setText("₹");
                }
                else
                {
                    editTextPrice.setVisibility(View.GONE);
                }
            }
        });

        editTextPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("₹")){
                    editTextPrice.setText("₹"+ editTextPrice.getText().toString().replace("₹",""));
                    editTextPrice.setSelection(1);
                }

//                .replace("₹", "") ;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                submitForm();
            }
        });

//        editTextAddress.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent i = new Intent(postRequirementActivity.this, locationPickerActivity.class);
//                startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
//            }
//        });




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

                                editTextDate.setText(year  + "-"  +"0"+(monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        String[] availableLocations = new String[] { "Home", "Choose a custom location" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(postRequirementActivity.this,
                android.R.layout.simple_spinner_item,availableLocations);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseLocation.setAdapter(adapter);
        spinnerChooseLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Choose a custom location"))
                {
                    Intent i = new Intent(postRequirementActivity.this, locationPickerActivity.class);
                    startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
                }
                if(selectedItem.equals("Home"))
                {
                   editTextAddress.setText("Default");
                   editTextDistance.setText("1");
                }

//                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO : take default location
            }
        });





    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_MULTIPLE) {
            try {
                // When an Image is picked
                if (resultCode == RESULT_OK
                        && null != data) {
                    // Get the Image from data

                    String[] filePathColumn = {MediaStore.Images.Media._ID};
                    imagesEncodedList = new ArrayList<String>();
                    if (data.getData() != null) {

                        Uri uri = data.getData();

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imageUriArray.add(uri.toString());
                        cursor.close();
                        IMAGE_FLAG = true;

                    } else {
                        if (data.getClipData() != null) {

                            ClipData mClipData = data.getClipData();
//                        ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                imageUriArray.add(uri.toString());
                                // Get the cursor
                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();

                            }
                            IMAGE_FLAG = true;
                            Log.v(TAG, "Selected Images" + imageUriArray.size() + "IMAGE_FLAG :" + IMAGE_FLAG);

                        for (int i = 0; i < imageUriArray.size(); i++)
                        {
                            Log.v(TAG,"Grabbed Image: "+ imageUriArray.get(i) +" with number " +i);
                        }

                        }
                    }
                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }
        if (requestCode ==  LAUNCH_LOCATION_ACTIVITY){
            try {
                if (resultCode == Activity.RESULT_OK) {

                    assert data != null;
                    radius = data.getStringExtra("radius");
                    fullAddress = data.getStringExtra("fullAddress");
                    finalPosition = data.getExtras().getParcelable("finalPosition");
//                    Toast.makeText(this, "Addr"+fullAddress, Toast.LENGTH_LONG).show();
                    editTextAddress.setVisibility(View.VISIBLE);
                    editTextAddress.setText(fullAddress);
                    editTextDistance.setVisibility(View.VISIBLE);
                    editTextDistance.setText(radius);



                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(postRequirementActivity.this, R.string.locationNotPickedError, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Log.v(TAG,e.getStackTrace().toString());
//                Toast.makeText(this, "Something went wrong: "+ e.getStackTrace(), Toast.LENGTH_LONG).show();

            }

        super.onActivityResult(requestCode, resultCode, data);
    }
    }



    private void submitForm() {
        // Title,Description maybe(Why I need it, specifications), Tag, Distance, Paisa(not compulsory)


        if (awesomeValidation.validate()) {
             title = editTextTitle.getText().toString();
             description = editTextDescription.getText().toString();
             phoneNumber = editTextPhone.getText().toString();
            priceQuote = editTextPrice.getText().toString();

             expirationDate = editTextDate.getText().toString()+"T23:59:00.000Z";
            Intent i = new Intent(postRequirementActivity.this, imageUploadActivity.class);
            i.putExtra("title",title);
            i.putExtra("description",description);
            i.putExtra("phoneNumber",phoneNumber);
            i.putExtra("expirationDate",expirationDate);
            i.putExtra("radius",radius);
            i.putExtra("lat",String.valueOf( finalPosition.latitude));
            i.putExtra("lng",String.valueOf( finalPosition.longitude));
            i.putExtra("priceQuote",priceQuote.substring(1));
             if (IMAGE_FLAG){
//                 String[] imageStringPaths = Arrays.copyOf(new ArrayList[]{imageUriArray}, imageUriArray.size(), String[].class);
                 i.putExtra("imageUriArray",imageUriArray);
                 Log.v(TAG,"Array "+imageUriArray);
             }
             else {
                 String[] imageStringPaths = {};
                 i.putExtra("imageStringPaths",imageStringPaths);
                             }

            startActivity(i);

//            Toast.makeditTextDate(this, "Validation Successfull", Toast.LENGTH_LONG).show();
//

        }

    }




}
