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

    static final String CHEAP = "cheap";
    static final String NEAR = "near";

    public CallTaxiAction(MainActivity main, Cade cade) {
        this.main = main;
        this.cade = cade;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        String calledAction = map.get("taxi_to_call");
        Log.i("Navigation-Log",calledAction);

        switch (calledAction) {
            case CHEAP:
                break;
            case NEAR:
                break;
        }
    }

    @Override
    public void performWhQuery(String s, Map<String, String> map) {

    }

    @Override
    public void performValidity(String s, Map<String, String> map) {

    }

    @Override
    public void performEntityRecognizer(String s, Map<String, String> map) {

    }
}
