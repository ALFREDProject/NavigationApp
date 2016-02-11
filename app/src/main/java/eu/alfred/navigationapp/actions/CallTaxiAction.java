package eu.alfred.navigationapp.actions;

import android.util.Log;

import java.util.Map;

import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;
import eu.alfred.navigationapp.MainActivity;

/**
 * Created by Gary on 05.02.2016.
 */
public class CallTaxiAction implements ICadeCommand {

    MainActivity main;
    Cade cade;

    public CallTaxiAction(MainActivity main, Cade cade) {
        this.main = main;
        this.cade = cade;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i("Navigation-Log",map.get("taxi_to_call"));
    }
}
