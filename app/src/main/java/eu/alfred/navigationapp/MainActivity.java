package eu.alfred.navigationapp;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.navigationapp.actions.CallTaxiAction;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity implements ICadeCommand, OnMapReadyCallback {

    private final CountDownLatch mapLatch = new CountDownLatch (1);

    final static String CALL_TAXI_ACTION = "CallTaxiAction";
    final static String SHOW_WAY_HOME_ACTION = "ShowWayHomeAction";

    GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void performAction(String calledAction, Map<String, String> map) {

        switch (calledAction) {
            case CALL_TAXI_ACTION:
                CallTaxiAction cta = new CallTaxiAction(this, cade);
                cta.performAction(calledAction, map);
                break;
            case SHOW_WAY_HOME_ACTION:
              //  ShowWayHomeAction sha = new ShowWayHomeAction(this, cade);
             //   sha.performAction(calledAction, map);
                break;
            default:
                break;
        }
    }
    @Override
    public void performWhQuery(String calledAction, Map<String, String> map) {
    }

    @Override
    public void performValidity(String calledAction, Map<String, String> map) {
    }

    @Override
    public void performEntityRecognizer(String calledAction, Map<String, String> map) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
    }
}
