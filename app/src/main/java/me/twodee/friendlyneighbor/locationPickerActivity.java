package me.twodee.friendlyneighbor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;
import java.util.Locale;

public class locationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  Circle distanceCircle;
    private static final String TAG = "MapsActivity";

    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    SeekBar seekbar;
    TextView textViewSeekBar;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private int circleRadius = 1000;
    private LatLng finalPosition ;

//
//    // Used for selecting the current place.
//    private static final int M_MAX_ENTRIES = 5;
//    private String[] mLikelyPlaceNames;
//    private String[] mLikelyPlaceAddresses;
//    private String[] mLikelyPlaceAttributions;
//    private LatLng[] mLikelyPlaceLatLngs;
//



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up the action toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the views
//        lstPlaces = (ListView) findViewById(R.id.listPlaces);

        // Initialize the Places client

        Places.initialize(getApplicationContext(),String.valueOf(R.string.google_maps_api_key));
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        seekbar =(SeekBar)findViewById(R.id.seekBar1);
        textViewSeekBar = findViewById(R.id.textViewSeekBar);
        Toast toast= Toast.makeText(locationPickerActivity.this,"You can change your search distance by seeking",Toast.LENGTH_SHORT) ;
        toast.setGravity(Gravity.BOTTOM|Gravity.LEFT, 0, 100);
        toast.show();







    }







    private void getLocationPermission()
    {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI()
    {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            updateLocationUI();

            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (mLocationPermissionGranted) {
                final Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        LatLng loc = mDefaultLocation;

                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();

                            loc = new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());


//                            mMap.addMarker(
//                                    new MarkerOptions().position(loc).title("Set marker here").draggable(true));

                            finalPosition = loc;
                             Circle distanceCircle =  mMap.addCircle(new CircleOptions()
                                    .center(loc)
                                    .radius(circleRadius)
                                    .fillColor(0x404CAF50)
                                    .strokeColor(R.color.mapCircleStroke)
                                    .strokeWidth(8));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, DEFAULT_ZOOM));

                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                        // draw circle
//                        CircleOptions circleOptions = new CircleOptions()
//                                .center(loc)
//                                .radius(1000); // In meters

// Get back the mutable Circle
//                        Circle circle = mMap.addCircle(circleOptions);
                        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                textViewSeekBar.setText(Integer.toString(progress) + " kms");
                                double percent = progress / (double) seekBar.getMax();
                                int offset = seekBar.getThumbOffset();
                                int seekWidth = seekBar.getWidth();
                                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                                int labelWidth = textViewSeekBar.getWidth();
                                textViewSeekBar.setX(offset + seekBar.getX() + val
                                        - Math.round(percent * offset)
                                        - Math.round(percent * labelWidth/2));
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                mMap.clear();


                            }
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                 circleRadius = 1000 * seekBar.getProgress();
                                LatLng center = mMap.getCameraPosition().target;
                                finalPosition = center;
                                Circle resizedCircle =  mMap.addCircle(new CircleOptions()
                                        .center(center)
                                        .radius(circleRadius)
                                        .fillColor(0x404CAF50)
                                        .strokeColor(R.color.mapCircleStroke)
                                        .strokeWidth(8));


                            }
                        });



                        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                            @Override
                            public void onCameraMoveStarted(int i) {
                                mMap.clear();
                            }
                        });
                        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                            @Override
                            public void onCameraIdle() {
                                LatLng center = mMap.getCameraPosition().target;
                                Log.d(TAG, "Pinned at" + center);
                                finalPosition = center;
                                    Circle newCircle =  mMap.addCircle(new CircleOptions()
                                            .center(center)
                                            .radius(circleRadius)
                                            .fillColor(0x404CAF50)
                                            .strokeColor(R.color.mapCircleStroke)
                                            .strokeWidth(8));





                            }
                        });

                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
                        {
                            @Override
                            public void onMarkerDragStart(Marker marker)
                            {
//                                Log.d(TAG, "Drag started: " + marker.getPosition().toString());
                                if (distanceCircle != null){
                                    distanceCircle.remove();
                                }



                            }

                            @Override
                            public void onMarkerDrag(Marker marker)
                            {
//                                Log.d(TAG, "Dragging: " + marker.getPosition().toString());
                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker)
                            {
//                                Log.d(TAG, "Drag ended: " + marker.getPosition().toString());

                                LatLng  movedPosition = marker.getPosition();
                                Log.d(TAG, "New position" +  marker.getPosition().toString());
                                moveDistanceCircle(movedPosition);


//                                Toast.makeText(locationPickerActivity.this, "Called", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
            else {
                getLocationPermission();
                onMapReady(mMap);
            }

            // Add a marker in Sydney and move the camera
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
            e.printStackTrace();
        }
    }

     void moveDistanceCircle(LatLng newLatlng) {
        if(distanceCircle != null){
            distanceCircle.setCenter(newLatlng);
            finalPosition =  newLatlng;
            distanceCircle.setVisible(true);
            Log.d(TAG, "Moved to : " + newLatlng);
        }
        }

    void ss()
    {
        if (mLocationPermissionGranted) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_map, menu);

        return super.onCreateOptionsMenu(menu);
    }

//    private void getDeviceLocation()
//    {
//
//    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w(TAG, strReturnedAddress.toString());
            } else {
                Log.w(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Cannot get Address!");
        }
        return strAdd;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.saveAsHome) {
            double lat = finalPosition.latitude;
            double lng = finalPosition.longitude;
            String fullAddress =  getCompleteAddressString(lat,lng);
            Log.v(TAG, "Distance"+ circleRadius +"Position"+finalPosition + "addr" + fullAddress );
            Intent returnIntent = new Intent();
            returnIntent.putExtra("radius",String.valueOf(circleRadius/1000));
            returnIntent.putExtra("fullAddress",fullAddress);
            returnIntent.putExtra("finalPosition",finalPosition);


//            returnIntent.putExtra("lat",String.valueOf(lat));
//            returnIntent.putExtra("lng",String.valueOf(lng));
            setResult(Activity.RESULT_OK,returnIntent);
            finish();


        }
        return super.onOptionsItemSelected(item);

    }




}