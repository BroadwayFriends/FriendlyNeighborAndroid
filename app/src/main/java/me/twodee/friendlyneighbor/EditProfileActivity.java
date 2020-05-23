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
import android.widget.LinearLayout;
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
import com.google.common.io.LineReader;
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
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    int LAUNCH_LOCATION_ACTIVITY = 877;
    private Button updateProfileButton;
    private EditText editTextEmail, editTextPhone, editTextUsername, editTextRadius, editTextLocation;
    private ImageView editPictureButton;
    private SharedPreferences preferences;
    private String TAG = "editProfilePage";
    String changedUri = "";
    private Geocoder geocoder;
    private LinearLayout editProfilePicture;
    private Boolean UPDATE_FLAG = Boolean.FALSE;
    private LatLng finalPosition;
    private List<Address> addresses;
    private String changedEmail, changedPhone, changedLocation, changedUsername, changedRadius;
    private  String baseUrl ;
    private Uri mCropImageUri;

    String locatedAddressLine1, locatedCity, locatedState, locatedCountry, locatedPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_edit_profile);

        preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        baseUrl = getResources().getString(R.string.base_url) ;
        LinearLayout goBack = findViewById(R.id.goBackLayout);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        updateProfileButton = (Button) findViewById(R.id.updateProfileButton);
        editPictureButton = (ImageView) findViewById(R.id.editPictureButton);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextRadius = (EditText) findViewById(R.id.editTextRadius);
        editProfilePicture =  findViewById(R.id.editProfilePicture);
        editTextLocation.setFocusable(false);
        editTextLocation.setCursorVisible(false);
        updateProfileButton.setClickable(false);
        fetchData();

//        updateProfileButton.setClickable(false);
//        updateProfileButton.setAlpha(.4f);


        goBack.setOnClickListener(v -> {
            Intent i = new Intent(EditProfileActivity.this, DashboardActivity.class);
            startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
            onProfileEdit();
        });

        editTextUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                changedUsername = editTextUsername.getText().toString();
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
                changedEmail = editTextEmail.getText().toString();

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
                changedPhone = editTextPhone.getText().toString();
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
                changedRadius = editTextRadius.getText().toString();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onProfileEdit();

            }
        });


        editTextLocation.setOnClickListener(v -> {
            Intent i = new Intent(EditProfileActivity.this, locationPickerActivity.class);
            startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
            onProfileEdit();
        });

        updateProfileButton.setOnClickListener(v -> {
            if (UPDATE_FLAG) {
                updateData();
            }
        });

        editProfilePicture.setOnClickListener(v -> {
            onProfileEdit();
            onSelectImageClick(findViewById(R.id.cropImageView));
        });

    }

    private void onProfileEdit() {

        updateProfileButton.setClickable(true);
        updateProfileButton.setAlpha(1f);
        UPDATE_FLAG = Boolean.TRUE;

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


    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], int[] grantResults) {
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

        if (requestCode == LAUNCH_LOCATION_ACTIVITY) {
            try {
                if (resultCode == Activity.RESULT_OK) {

                    assert data != null;
                    geocoder = new Geocoder(EditProfileActivity.this, Locale.getDefault());
                    String locatedRadius = Objects.requireNonNull(data.getExtras()).getString("radius");
                    editTextRadius.setText(locatedRadius);
                    finalPosition = data.getExtras().getParcelable("finalPosition");
                    assert finalPosition != null;
                    addresses = geocoder.getFromLocation(finalPosition.latitude, finalPosition.longitude, 1);
                    locatedAddressLine1 = addresses.get(0).getAddressLine(0);
                    locatedCity = addresses.get(0).getLocality();
                    locatedState = addresses.get(0).getAdminArea();
                    locatedCountry = addresses.get(0).getCountryName();
                    locatedPostalCode = addresses.get(0).getPostalCode();
                    Log.v(TAG, "Addr," + locatedAddressLine1);
                    editTextLocation.setText(locatedAddressLine1);
                    editTextRadius.setText(locatedRadius);

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(EditProfileActivity.this, R.string.locationNotPickedError, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

//                Toast.makeText(this, "Something went wrong: ", Toast.LENGTH_LONG).show();
                Log.e(TAG, e.getMessage());

            }


        }


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                changedUri = result.getUri().toString().replace("file://", "");
                Log.v(TAG, result.getUri().toString().replace("file://", ""));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e(TAG, result.getError().toString());
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

//        String userId = preferences.getString("_id", null);
        String userId =  "5ec7e4eddb059c13762d643f" ;
        try {
            object.put("id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String baseUrl = getResources().getString(R.string.base_url)+ "api/users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, object,
                response -> {
                    Log.w("ServerResponse", response.toString());

                    try {
//                            Toast.makeText(DashboardActivity.this, response.getString("name"), Toast.LENGTH_SHORT).show();
//                            nameTV.setText(response.getString("name"));
//                            emailTV.setText(response.getString("email"));
                        editTextUsername.setText(response.getString("name"));
                        editTextEmail.setText(response.getString("email"));
                        editTextPhone.setText(response.getString("contactNumber"));
                        editTextRadius.setText(response.getString("defaultSearchRadius"));
                        editTextLocation.setText(response.getString("address"));
//                            editTextLocation.setText(response.getString("defaultLocation"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
                    Log.w("ServerError", error);

                });
        requestQueue.add(jsonObjectRequest);
    }


    private void updateData() {

        JSONObject object = new JSONObject();
        JSONObject defaultLocation = new JSONObject();
        JSONObject address = new JSONObject();

        String userId = preferences.getString("_id", null);
        changedUsername = editTextUsername.getText().toString();
        changedEmail = editTextEmail.getText().toString();
        changedPhone = editTextPhone.getText().toString();
        changedRadius = editTextRadius.getText().toString();
        changedLocation = editTextLocation.getText().toString();
        try {

//            defaultLocation.put("latitude", finalPosition.latitude);
//            defaultLocation.put("longitude", finalPosition.longitude);
//            address.put("addr",locatedAddressLine1);
//            address.put("state",locatedState);
//            address.put("city",locatedCity);
//            address.put("pincode",locatedPostalCode);
//            address.put("country",locatedCountry);

//            object.put("id", userId);
            object.put("name", changedUsername);
            object.put("email", changedEmail);
            object.put("contactNumber", changedPhone);
            object.put("defaultLocation", defaultLocation);
            object.put("address",address);
            object.put("defaultSearchRadius", Integer.parseInt(changedRadius));


        } catch (JSONException e) {
            e.printStackTrace();
        }
//        https://fn.twodee.me/api/users/5ec7e4eddb059c13762d643f
        String baseUrl = getResources().getString(R.string.base_url)+ "api/users/" + userId ;
        MultipartUploadRequest reqObj = new MultipartUploadRequest(this, baseUrl)
                .setMethod("PUT")
                .addHeader("_id", "5ec7e77bb6bad31464e5ae9b")
                .addParameter("data", object.toString());
        try {
           reqObj.addFileToUpload(changedUri, "image");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//                .addFileToUpload(changedUri, "updatedPhoto")

        reqObj.subscribe(this, this, new RequestObserverDelegate() {

            @Override
            public void onSuccess(@NotNull Context context, @NotNull UploadInfo uploadInfo, @NotNull ServerResponse serverResponse) {
                Log.i(TAG, "Success:" + serverResponse.getBodyString());
                Toast.makeText(EditProfileActivity.this, "Successfully Updated !!", Toast.LENGTH_SHORT).show();
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
        });

        Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
        startActivity(intent);

    }


}
