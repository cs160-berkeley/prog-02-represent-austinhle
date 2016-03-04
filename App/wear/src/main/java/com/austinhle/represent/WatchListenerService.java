package com.austinhle.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by austinhle on 3/1/16.
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String ZIPCODE_PATH = "/zipcode";
    public static final String ZIPCODE = "com.austinhle.represent.ZIPCODE";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        if( messageEvent.getPath().equalsIgnoreCase(ZIPCODE_PATH) ) {
            String zipcode = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ZIPCODE, zipcode);
            Log.d("T", "about to start watch MainActivity using provided zipcode");
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }
    }
}
