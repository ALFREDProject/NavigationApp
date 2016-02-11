package eu.alfred.navigationapp;

import android.os.Bundle;

import java.util.Map;

import eu.alfred.navigationapp.actions.CallTaxiAction;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;


public class MainActivity extends AppActivity {

    private static final String CALL_TAXI_ACTION = "CallTaxiAction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change your view contents. Note, the the button has to be included last.
        setContentView(eu.alfred.navigationapp.R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());
    }

    @Override
    public void performAction(String command, Map<String, String> map) {

        //Add custom events here
        switch (command) {
            case (CALL_TAXI_ACTION):
                CallTaxiAction cta = new CallTaxiAction(this, cade);
                cta.performAction(command, map);
                break;
            default:
                break;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}