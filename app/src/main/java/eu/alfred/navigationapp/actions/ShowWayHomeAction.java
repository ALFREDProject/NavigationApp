package eu.alfred.navigationapp.actions;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;
import eu.alfred.navigationapp.MainActivity;

/**
 * Created by Gary on 16.02.2016.
 */
public class ShowWayHomeAction implements ICadeCommand, RoutingListener {

    MainActivity main;
    Cade cade;
    GoogleMap googleMap;

    public ShowWayHomeAction(MainActivity main, Cade cade, GoogleMap googleMap) {
        this.main = main;
        this.cade = cade;
        this.googleMap = googleMap;
    }

    @Override
    public void performAction(String name, Map<String, String> map) {
        String calledAction = map.get("selected_town");
        Log.i("Navigation-Log", calledAction);

        List<Address> address = null;
        try {
            address = new Geocoder(main).getFromLocationName(calledAction, 5);
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            LatLng start = new LatLng(Double.valueOf(map.get("ownLat")), Double.valueOf(map.get("ownLng")));
            LatLng end = new LatLng(52, 13);

            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.WALKING)
                    .withListener(this)
                    .waypoints(start, end)
                    .key("AIzaSyAFyfSNIwGO0o_0eAytM92K_7n28BQkTAQ")
                    .build();
            routing.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performWhQuery(String name, Map<String, String> map) {

    }

    @Override
    public void performValidity(String name, Map<String, String> map) {

    }

    @Override
    public void performEntityRecognizer(String name, Map<String, String> map) {

    }
    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(main, e.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(10 + i * 3);
        polyOptions.addAll(route.get(i).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        cade.sendActionResult(true);
    }

    @Override
    public void onRoutingCancelled() {

    }
}
