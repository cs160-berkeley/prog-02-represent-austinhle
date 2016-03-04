package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
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
public class MyFragment extends CardFragment {
    public final static String NAME = "com.austinhle.represent.NAME";

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View v = inflater.inflate(R.layout.fragment_activity, container, false);

        final TextView partyField = (TextView) v.findViewById(R.id.party);
        String party = bundle.getString("party");
        partyField.setText(party);
        if (party.equals("Republican")) {
            partyField.setTextColor(getResources().getColor(R.color.republican));
        } else {
            partyField.setTextColor(getResources().getColor(R.color.democrat));
        }

        final TextView nameField = (TextView) v.findViewById(R.id.name);
        nameField.setText(bundle.getString("name"));
        if (party.equals("Republican")) {
            nameField.setTextColor(getResources().getColor(R.color.republican));
        } else {
            nameField.setTextColor(getResources().getColor(R.color.democrat));
        }

        final TextView positionField = (TextView) v.findViewById(R.id.position);
        positionField.setText(bundle.getString("position"));

        final Button infoButton = (Button) v.findViewById(R.id.more_info);
        if (party.equals("Republican")) {
            infoButton.setBackgroundResource(R.color.republican);
        } else {
            infoButton.setBackgroundResource(R.color.democrat);
        }
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
                sendIntent.putExtra(NAME, nameField.getText());
                Log.d("T", "Sending name: " + nameField.getText() + "\n");
                getActivity().startService(sendIntent);
            }
        });

        return v;
    }
}
