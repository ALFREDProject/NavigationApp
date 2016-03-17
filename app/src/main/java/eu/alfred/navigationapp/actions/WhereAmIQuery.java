package eu.alfred.navigationapp.actions;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;
import eu.alfred.navigationapp.MainActivity;

/**
 * Created by Gary on 05.02.2016.
 */
public class WhereAmIQuery implements ICadeCommand {

    MainActivity main;
    Cade cade;
    GoogleMap googleMap;

    public WhereAmIQuery(MainActivity main, Cade cade, GoogleMap googleMap) {
        this.main = main;
        this.cade = cade;
        this.googleMap = googleMap;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        cade.sendActionResult(true);
    }

    @Override
    public void performWhQuery(String s, Map<String, String> map) {

        LatLng nearPosition = new LatLng(Double.valueOf(map.get("nearLat")),
                Double.valueOf(map.get("nearLng")));

        googleMap.addMarker(new MarkerOptions().position(nearPosition));

        List<Map<String, String>> result = new ArrayList<>();
        HashMap tempMap = new HashMap<>();
        tempMap.put("grammar_entry", map.get("name"));
        result.add(tempMap);

        cade.sendWHQueryResult(result);
    }

    @Override
    public void performValidity(String s, Map<String, String> map) {

    }

    @Override
    public void performEntityRecognizer(String s, Map<String, String> map) {

    }
}
