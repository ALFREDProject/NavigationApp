package eu.alfred.navigationapp.actions;

import android.util.Log;

import java.util.Map;

import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;
import eu.alfred.navigationapp.MainActivity;

/**
 * Created by Gary on 16.02.2016.
 */
public class ShowWayHomeAction implements ICadeCommand {

    MainActivity main;
    Cade cade;

    public ShowWayHomeAction(MainActivity main, Cade cade) {
        this.main = main;
        this.cade = cade;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i("Navigation-Log", s);

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
