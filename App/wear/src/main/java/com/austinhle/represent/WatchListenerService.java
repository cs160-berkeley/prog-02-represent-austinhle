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
    public static final String DATA = "com.austinhle.represent.DATA";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String data = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent i = getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(getApplicationContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(DATA, data);
//        intent.putExtra(DATA, data);
        Log.d("WatchListenerService", "Starting MainActivity on watch with data: " + data);
        startActivity(i);
    }
}
