package me.twodee.friendlyneighbor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
    ImageView pin;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private int circleRadius = 1000;
    private LatLng finalPosition ;

    private static final int PERMISSION_ID = 69420;
    private  LatLng loc;
    

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
        pin =  findViewById(R.id.pin);
//        Toast toast= Toast.makeText(locationPickerActivity.this,"You can change your search distance by seeking",Toast.LENGTH_SHORT) ;
//        toast.setGravity(Gravity.BOTTOM|Gravity.LEFT, 0, 100);
//        toast.show();







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
            case PERMISSION_ID :
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();

                }}
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
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.map_style_json));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsActivityRaw", "Can't find style.", e);
            }

            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (mLocationPermissionGranted) {
                final Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task task)
                    {


                        if (task.isSuccessful() && task.getResult() != null)
//                        if (task.isSuccessful() )
                        {
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

                            getLastLocation();
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
                                if(Integer.toString(progress).equals( "1") || Integer.toString(progress).equals( "0")){
                                    textViewSeekBar.setText(Integer.toString(progress) + " km");
                                }
                                else{
                                textViewSeekBar.setText(Integer.toString(progress) + " kms");
                                }
                                double percent = progress / (double) seekBar.getMax();
                                int offset = seekBar.getThumbOffset();
                                int seekWidth = seekBar.getWidth();
                                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                                int labelWidth = textViewSeekBar.getWidth();
//                                textViewSeekBar.setX(offset + seekBar.getX() + val
//                                        - Math.round(percent * offset)
//                                        - Math.round(percent * labelWidth/2));
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
                                pin.requestLayout();
                                pin.getLayoutParams().height = 400;
                                pin.getLayoutParams().width = 400;
                                pin.setImageResource(R.drawable.dragpin);



                            }
                        });
                        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                            @Override
                            public void onCameraIdle() {
                                pin.requestLayout();
                                pin.getLayoutParams().height = 320;
                                pin.getLayoutParams().width = 320;
                                pin.setImageResource(R.drawable.droppin);
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


    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    loc = new LatLng(location.getLatitude(), location.getLongitude());


                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//
//        }
//    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }



}