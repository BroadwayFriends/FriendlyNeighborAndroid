package me.twodee.friendlyneighbor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.data.UploadInfo;
import net.gotev.uploadservice.network.ServerResponse;
import net.gotev.uploadservice.observer.request.RequestObserverDelegate;
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    int LAUNCH_LOCATION_ACTIVITY = 877;
    private Button editProfileButton;
    private EditText editTextEmail ,editTextPhone,editTextUsername,editTextRadius, editTextLocation;
    private ImageView editPictureButton;
    private SharedPreferences preferences;
    private String TAG = "editProfilePage";
    String changedUri = "" ;
    private Geocoder geocoder ;
    private LatLng finalPosition ;
    private List<Address> addresses;
    private String changedEmail, changedPhone, changedLocation,changedUsername,changedRadius;
    private static final String baseUrl = "https://ptsv2.com/t/m65jb-1589964055/post";
    private Uri mCropImageUri;



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
        editPictureButton = (ImageView) findViewById(R.id.editPictureButton);
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
                updateData();
            }
        });

        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileEdit();
                onSelectImageClick(findViewById(R.id.cropImageView));
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




    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
//                .setCropShape(CropImageView.CropShape.OVAL)
                .setGuidelines(CropImageView.Guidelines.OFF)


                .start(this);

    }


    @SuppressLint("NewApi")
    public void onSelectImageClick(View view) {
        if (CropImage.isExplicitCameraPermissionRequired(this)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
        } else {
            CropImage.startPickImageActivity(this);
        }
    }



    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
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


        }


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                changedUri = result.getUri().toString().replace("file://","");
                Log.v(TAG,  result.getUri().toString().replace("file://","")) ;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Log.e(TAG, result.getError().toString()) ;
            }
        }



        super.onActivityResult(requestCode, resultCode, data);
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
                            editTextUsername.setText(response.getString("name"));
                            editTextEmail.setText(response.getString("email"));
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

        JSONObject object = new JSONObject();

        String userId = preferences.getString("_id", null);
        changedUsername = editTextUsername.getText().toString();
        changedEmail = editTextEmail.getText().toString();
        changedPhone = editTextPhone.getText().toString();
        changedRadius = editTextRadius.getText().toString();
        changedLocation = editTextLocation.getText().toString();
        try {

//            object.put("id", userId);
            object.put("name", changedUsername);
            object.put("email", changedEmail);
            object.put("changedPhone", changedPhone);
            object.put("defaultLocation", String.format("{lat:%s,lng:%s}", finalPosition.latitude, finalPosition.longitude));
            object.put("changedRadius", changedRadius);


        } catch (JSONException e) {
            e.printStackTrace();
        }




        MultipartUploadRequest reqObj = new MultipartUploadRequest(this, baseUrl)
                .setMethod("POST")
                .addHeader("_id", "5ebc27d7e6fe7a77013ecd2a")
                .addParameter("data", object.toString());
        try {
            reqObj.addFileToUpload(changedUri, "updatedPhoto");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//                .addFileToUpload(changedUri, "updatedPhoto")

                reqObj.subscribe( this, this, new RequestObserverDelegate() {

                    @Override
                    public void onSuccess(@NotNull Context context, @NotNull UploadInfo uploadInfo, @NotNull ServerResponse serverResponse) {
                        Log.i(TAG, "Success:"+serverResponse.getBodyString());
                        Toast.makeText(EditProfileActivity.this,"Successfully Updated !!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(@NotNull Context context, @NotNull UploadInfo uploadInfo) {

                    }

                    @Override
                    public void onError(@NotNull Context context, @NotNull UploadInfo uploadInfo, @NotNull Throwable throwable) {
                        Log.e(TAG, "Error, upload error:");
                    }

                    @Override
                    public void onCompletedWhileNotObserving() {

                    }

                    @Override
                    public void onCompleted(@NotNull Context context, @NotNull UploadInfo uploadInfo) {

                    }
                } );

        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(intent);

    }



}
