package me.twodee.friendlyneighbor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class ProfileFragment extends Fragment {

    int LAUNCH_LOCATION_ACTIVITY = 877;
    private Button updateProfileButton;
    private TextView textViewName,textViewTitle;
    private EditText editTextEmail, editTextPhone, editTextUsername, editTextRadius, editTextLocation;
    private ImageView editPictureButton,displayImage;
    private final String TAG = "editProfilePage";
    String changedUri = "",userId;
    private Boolean CHANGED_PICTURE_FLAG = Boolean.FALSE;
    private Geocoder geocoder;
    private LinearLayout editProfilePicture,goBack;
    private Boolean UPDATE_FLAG = Boolean.FALSE;
    private LatLng finalPosition;
    private List<Address> addresses;
    private String changedEmail, changedPhone, changedLocation, changedUsername, changedRadius;
    private  String baseUrl,visitReason ;
    private Uri mCropImageUri;

    String locatedAddressLine1, locatedCity, locatedState, locatedCountry, locatedPostalCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
       View view = inflater.inflate(R.layout.fragment_profile, parent, false);

//        if (getIntent().hasExtra("visitReason")) {
//            visitReason = getIntent().getStringExtra("visitReason");
//        } else {
//            visitReason = "view";
//        }

        displayImage = view.findViewById(R.id.displayImage);
        goBack = view.findViewById(R.id.goBackLayout);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        textViewName =  view.findViewById(R.id.textViewName);
        textViewTitle =  view.findViewById(R.id.textViewTitle);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        updateProfileButton = view.findViewById(R.id.updateProfileButton);
        editPictureButton = view.findViewById(R.id.editPictureButton);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        editTextRadius = view.findViewById(R.id.editTextRadius);
        editProfilePicture =  view.findViewById(R.id.editProfilePicture);
        return view ;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        baseUrl = getResources().getString(R.string.base_url) ;
        editTextLocation.setFocusable(false);
        editTextLocation.setCursorVisible(false);
        editTextEmail.setFocusable(false);
        editTextEmail.setCursorVisible(false);
        editTextUsername.setFocusable(false);
        editTextUsername.setCursorVisible(false);
        updateProfileButton.setClickable(false);

        visitReason = "view";
//        updateProfileButton.setVisibility(View.GONE);
//        textViewTitle.setText("My Profile");


        HashMap<String,String> userData = ((BaseActivity)getActivity()).passDataToFrags();
        userId = userData.get("_id");


        if(visitReason.equals("edit")){
            textViewTitle.setText("Edit Profile");
            updateProfileButton.setVisibility(View.VISIBLE);
        }
        if(visitReason.equals("view")){
            textViewTitle.setText("My Profile");
            updateProfileButton.setVisibility(View.GONE);
        }

        fetchData();

//        updateProfileButton.setClickable(false);
//        updateProfileButton.setAlpha(.4f);


        goBack.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), DashboardActivity.class);
            startActivity(i);

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
                textViewName.setText(s.toString());
                if(editTextUsername.hasFocus()) {
                    onProfileEdit();
                }

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
                if(editTextEmail.hasFocus()) {
                    onProfileEdit();
                }
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
                if(editTextPhone.hasFocus()) {
                    onProfileEdit();
                }


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
//                onProfileEdit();

            }
        });


        editTextLocation.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), locationPickerActivity.class);
            startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY);
            onProfileEdit();
        });

        updateProfileButton.setOnClickListener(v -> {
            if (UPDATE_FLAG) {
                if(finalPosition != null){
                    updateData();
                }
                else{
                    Toast.makeText(getActivity(),"Please provide your current location.",Toast.LENGTH_SHORT).show();}
            }
        });

        editProfilePicture.setOnClickListener(v -> {
            onProfileEdit();
            onSelectImageClick(view.findViewById(R.id.cropImageView));
            CHANGED_PICTURE_FLAG = Boolean.TRUE;
        });

    }

    private void onProfileEdit() {

        updateProfileButton.setClickable(true);
        updateProfileButton.setAlpha(1f);
        UPDATE_FLAG = Boolean.TRUE;
        textViewTitle.setText("Edit Profile");
        updateProfileButton.setVisibility(View.VISIBLE);

    }

    private void refreshActivity() {
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(new Intent(getActivity(), EditProfileActivity.class));
//        overridePendingTransition(0, 0);
        getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(),
        new ProfileFragment()).commit();

    }

    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
//                .setCropShape(CropImageView.CropShape.OVAL)
                .setGuidelines(CropImageView.Guidelines.OFF)


                .start(getActivity());

    }


    @SuppressLint("NewApi")
    public void onSelectImageClick(View view) {
        if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
        } else {
            CropImage.startPickImageActivity(getActivity());
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(getActivity());
            } else {
                Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LAUNCH_LOCATION_ACTIVITY) {
            try {
                if (resultCode == Activity.RESULT_OK) {

                    assert data != null;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
                    Toast.makeText(getActivity(), R.string.locationNotPickedError, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

//                Toast.makeText(this, "Something went wrong: ", Toast.LENGTH_LONG).show();
                Log.e(TAG, e.getMessage());

            }


        }


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
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
            if (resultCode == Activity.RESULT_OK) {
                changedUri = result.getUri().toString().replace("file://", "");
                Log.v(TAG, result.getUri().toString().replace("file://", ""));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e(TAG, result.getError().toString());
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }



    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject object = new JSONObject();

//        String userId =  "5ec7e4eddb059c13762d643f" ;
        try {
            object.put("_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String baseUrl = getResources().getString(R.string.base_url)+ "/api/users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, object,
                response -> {
                    Log.w("FETCH DATA", response.toString());

                    try {
                        JSONObject respObj = new JSONObject(response.getString("user"));
                        String profilePictureUrl =  respObj.getString("profilePicture");
                        Picasso.get().load(profilePictureUrl).fit().centerInside().into(displayImage);
                        editTextUsername.setText(respObj.getString("name"));
                        editTextEmail.setText(respObj.getString("email"));
                        editTextPhone.setText(respObj.getString("contactNumber"));
                        editTextRadius.setText(respObj.getString("defaultSearchRadius"));
                        boolean canChangeName  = response.getBoolean("canChangeName");

                        Log.w(TAG, "canChangeName" + canChangeName);

                        editTextUsername.setFocusable(canChangeName);
                        editTextUsername.setFocusableInTouchMode(canChangeName);
                        editTextUsername.setCursorVisible(canChangeName);

//                        editTextEmail.setFocusable(canChangeName);
//                        editTextEmail.setFocusableInTouchMode(canChangeName);
//                        editTextEmail.setCursorVisible(canChangeName);

                        String address = respObj.getString("address");
                        Log.v(TAG,address);
                        try {

                            JSONObject obj = new JSONObject(address);
                            String addr = obj.getString("addr");
                            editTextLocation.setText(addr);



                        } catch (Throwable t) {
                            Log.e(TAG, "Could not parse malformed JSON" + address);
                        }


                        textViewName.setText(respObj.getString("name"));
//                            editTextLocation.setText(response.getString("defaultLocation"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Log.w("ServerError", error);

        }){
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("_id", userId);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    private void updateData() {

        JSONObject object = new JSONObject();
        JSONObject defaultLocation = new JSONObject();
        JSONObject address = new JSONObject();

        changedUsername = editTextUsername.getText().toString();
        changedEmail = editTextEmail.getText().toString();
        changedPhone = editTextPhone.getText().toString();
        changedRadius = editTextRadius.getText().toString();
        changedLocation = editTextLocation.getText().toString();

        try {

            defaultLocation.put("latitude", finalPosition.latitude);
            defaultLocation.put("longitude", finalPosition.longitude);
            address.put("addr",locatedAddressLine1);
            address.put("state",locatedState);
            address.put("city",locatedCity);
            address.put("pincode",locatedPostalCode);
            address.put("country",locatedCountry);

            object.put("id", userId);
            object.put("name", changedUsername);
//            object.put("email", changedEmail);
            object.put("contactNumber", changedPhone);
            object.put("defaultLocation", defaultLocation);
            object.put("address",address);
            object.put("defaultSearchRadius", Integer.parseInt(changedRadius));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String baseUrl = getResources().getString(R.string.base_url)+ "/api/users/" + userId ;
//        String baseUrl = "https://httpbin.org/anything";


        if (CHANGED_PICTURE_FLAG) {
            MultipartUploadRequest reqObj = new MultipartUploadRequest(getContext(), baseUrl)
                    .setMethod("PUT")
                    .addHeader("_id", userId)
                    .addParameter("data", object.toString());
            try {

                reqObj.addFileToUpload(changedUri, "image");


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//                .addFileToUpload(changedUri, "updatedPhoto")

            reqObj.subscribe(getContext(), this, new RequestObserverDelegate() {

                @Override
                public void onSuccess(@NotNull Context context, @NotNull UploadInfo uploadInfo, @NotNull ServerResponse serverResponse) {
                    Log.i(TAG, "Success:" + serverResponse.getBodyString());
                    Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_SHORT).show();
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
        }
        else {

            try {
                object.put("image","");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v(TAG,"Works");
            RequestQueue newRequestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, baseUrl, object,
                    response -> {
                        Log.w(TAG, response.toString());
                        refreshActivity();

                    }, error -> {
                Log.w("ServerError", error);
            }){
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("_id", userId);
                    return headers;
                }
            };

            newRequestQueue.add(jsonObjectRequest);

        }

        refreshActivity();


    }
}