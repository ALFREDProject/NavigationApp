package eu.alfred.navigationapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.navigationapp.actions.ShowWayHomeAction;
import eu.alfred.navigationapp.actions.WhereAmIQuery;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity implements ICadeCommand, OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    final static String WHERE_AM_I_QUERY = "result_location";
    final static String SHOW_WAY_TO_TOWN_ACTION = "ShowWayToTownAction";

    GoogleMap gmap;

    private static final long INTERVAL = 1000 * 30;
    private static final long FASTEST_INTERVAL = 1000 * 30;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    Location mCurrentLocation;
    LatLng nearPlaceLatLng;
    String nearPlaceString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gmap.setMyLocationEnabled(true);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void performAction(final String calledAction, final Map<String, String> map) {


        if(mCurrentLocation!=null && gmap!=null) {
            switch (calledAction) {
                case SHOW_WAY_TO_TOWN_ACTION:
                    map.put("ownLat", String.valueOf(mCurrentLocation.getLatitude()));
                    map.put("ownLng", String.valueOf(mCurrentLocation.getLongitude()));
                    ShowWayHomeAction sha = new ShowWayHomeAction(this, cade, gmap);
                    sha.performAction(calledAction, map);
                    break;
                default:
                    break;
            }
        } else if (personalAssistant!=null) {
            int interval = 200; // 1 Second
            Handler handler = new Handler();
            Runnable runnable = new Runnable(){
                public void run() {
                    performAction(calledAction,map);
                }
            };
            handler.postDelayed(runnable, interval);
        } else {

        }
    }

    @Override
    public void performWhQuery(final String calledAction, final Map<String, String> map) {
        if(nearPlaceLatLng!=null && gmap!=null) {
            switch (calledAction) {
                case WHERE_AM_I_QUERY:
                    map.put("nearLat", String.valueOf(nearPlaceLatLng.latitude));
                    map.put("nearLng", String.valueOf(nearPlaceLatLng.longitude));
                    map.put("name", String.valueOf(nearPlaceString));
                    WhereAmIQuery waia = new WhereAmIQuery(this, cade, gmap);
                    waia.performWhQuery(calledAction, map);
                    break;
                default:
                    break;
            }
        } else {
            int interval = 200; // 1 Second
            Handler handler = new Handler();
            Runnable runnable = new Runnable(){
                public void run() {
                    performWhQuery(calledAction, map);
                }
            };
            handler.postDelayed(runnable, interval);
        }
    }

    @Override
    public void performValidity(String calledAction, Map<String, String> map) {
    }

    @Override
    public void performEntityRecognizer(String calledAction, Map<String, String> map) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

       // ArrayList<String> restrictToTaxis = new ArrayList<>();
       // restrictToTaxis.add(Integer.toString(Place.TYPE_TAXI_STAND));
       // PlaceFilter pf = new PlaceFilter(true, restrictToTaxis);

        PendingResult<PlaceLikelihoodBuffer> result =
                Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                Log.d("LOG_TAG", "Got results: " + likelyPlaces.getCount() + " place found.");
                if(likelyPlaces.getCount()!=0) {
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        Log.i("LOG_TAG", String.format("Place '%s' has likelihood: '%s'",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getPlace().getPlaceTypes().toString()));
                    }
                    nearPlaceLatLng = new LatLng(likelyPlaces.get(0).getPlace().getLatLng().latitude,
                            likelyPlaces.get(0).getPlace().getLatLng().longitude);
                    nearPlaceString = new String(likelyPlaces.get(0).getPlace().getName().toString());
                }
                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(),location.getLongitude()))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom// Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        super.onResume();
    }

}
