package com.austinhle.represent;

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

public class MainActivity extends WearableActivity {
    public static final String RANDOM_ZIPCODE = "com.austinhle.represent.RANDOM_ZIPCODE";

    private MyGridPagerAdapter mgpa;
    private SensorManager mSensorManager;

    private float currentAccel;
    private float lastAccel;

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            Log.d("A", "Acceleration changed.");
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            lastAccel = currentAccel;
            currentAccel = (float) Math.sqrt((double) (x*x + y*y + z*z));

            // Difference of 50f is arbitrary.
            if (Math.abs(currentAccel - lastAccel) > 50f) {
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
        Log.d("MainActivity", "Started watch main activity.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String data = intent.getStringExtra(WatchListenerService.DATA);
        Log.d("MainActivity", "Data in main activity: " + data);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        mgpa = new MyGridPagerAdapter(this, getFragmentManager(), data);
        pager.setAdapter(mgpa);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        currentAccel = SensorManager.GRAVITY_EARTH;
        lastAccel = SensorManager.GRAVITY_EARTH;
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
