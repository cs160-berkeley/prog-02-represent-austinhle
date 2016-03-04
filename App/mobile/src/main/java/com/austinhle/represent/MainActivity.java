package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by austinhle on 3/1/16.
 */
public class MainActivity extends Activity {
    protected static HashMap<Integer, String> map;
    public final static String ZIPCODE = "com.austinhle.represent.ZIPCODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Part C, programmatically complete and update the map.
        map = new HashMap<Integer, String>();
        map.put(R.id.cInfo1, getString(R.string.name1));
        map.put(R.id.cInfo2, getString(R.string.name2));
        map.put(R.id.cInfo3, getString(R.string.name3));

        Button zipButton = (Button) findViewById(R.id.zip_btn);
        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.zip_code);
                String zipcode = input.getText().toString();

                if (!zipcode.equals("95914")) {
                    map.put(R.id.cInfo3, "Barbara Lee");
                }

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra(ZIPCODE, zipcode);
                Log.d("T", "Sending ZIPCODE\n");
                startService(sendIntent);

                sendIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                sendIntent.putExtra(ZIPCODE, zipcode);
                startActivity(sendIntent);
            }
        });

        Button locButton = (Button) findViewById(R.id.loc_btn);
        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put(R.id.cInfo3, "Barbara Lee");

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra(ZIPCODE, "94720");
                Log.d("T", "Sending ZIPCODE\n");
                startService(sendIntent);

                sendIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                sendIntent.putExtra(ZIPCODE, "94720");
                startActivity(sendIntent);
            }
        });
    }
}
