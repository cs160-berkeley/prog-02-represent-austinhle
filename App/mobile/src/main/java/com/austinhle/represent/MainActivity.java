package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by austinhle on 3/1/16.
 */
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "03qV7QuwiXl3C3GPwlzm79UdM";
    private static final String TWITTER_SECRET = "vix6zWIaJj38H2r0f6j9RWWhNrixJW2bdmzK7VPnftswAUQbrp";

    public static final String ZIPCODE = "com.austinhle.represent.ZIPCODE";
    public static final String LAT = "com.austinhle.represent.LAT";
    public static final String LONG = "com.austinhle.represent.LONG";

    protected GoogleApiClient mGoogleApiClient;

    protected String lastLat, lastLong;
    protected Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Twitter client and authentication
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        // Login as a guest to use Twitter API
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                Log.d("LoginGuest", "Successfully logged in as a guest on Twitter.");
            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        // Set up Google API client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Wearable.API)
                    .build();
        }

        Button zipButton = (Button) findViewById(R.id.zip_btn);
        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.zip_code);
                String zipcode = input.getText().toString();

                Intent sendIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                sendIntent.putExtra(ZIPCODE, zipcode);
                Log.d("T", "[To CongressionalActivity] Sending zipcode: " + zipcode + "\n.");
                startActivity(sendIntent);
            }
        });

        Button locButton = (Button) findViewById(R.id.loc_btn);
        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                sendIntent.putExtra(LAT, lastLat);
                sendIntent.putExtra(LONG, lastLong);
                Log.d("T", "[To CongressionalActivity] Sending latitude and longitude: " + lastLat + ", " + lastLong + "\n.");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("T", "Connected.");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("T", "Last location: " + lastLocation);
        if (lastLocation != null) {
            lastLat = String.valueOf(lastLocation.getLatitude());
            lastLong = String.valueOf(lastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int n) {}

    @Override
    public void onConnectionFailed(ConnectionResult cr) {}
}

