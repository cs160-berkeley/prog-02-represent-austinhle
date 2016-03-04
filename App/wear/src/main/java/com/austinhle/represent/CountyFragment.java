package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by austinhle on 3/2/16.
 */
public class CountyFragment extends CardFragment {
    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View v = inflater.inflate(R.layout.county_activity, container, false);

        final TextView county = (TextView) v.findViewById(R.id.county);
        county.setText(bundle.getString("county"));

        final TextView cand1 = (TextView) v.findViewById(R.id.cand1);
        cand1.setText(bundle.getString("cand1"));

        final TextView cand2 = (TextView) v.findViewById(R.id.cand2);
        cand2.setText(bundle.getString("cand2"));

        return v;
    }
}
