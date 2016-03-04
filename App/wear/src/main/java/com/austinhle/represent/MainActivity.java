package com.austinhle.represent;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity {
    public static final String RANDOM_ZIPCODE = "com.austinhle.represent.RANDOM_ZIPCODE";

    private MyGridPagerAdapter mgpa;
    private SensorManager mSensorManager;

    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last recorded acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));

            if (Math.abs(mAccelCurrent - mAccelLast) > 50f) {
                Log.d("A", "Shake detected!");
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.putExtra(RANDOM_ZIPCODE, "True");
                startService(sendIntent);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String zipcode = intent.getStringExtra(WatchListenerService.ZIPCODE);
        Log.d("T", "Started watch MainActivity using provided zipcode " + zipcode);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        mgpa = new MyGridPagerAdapter(this, getFragmentManager(), zipcode);
        pager.setAdapter(mgpa);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
