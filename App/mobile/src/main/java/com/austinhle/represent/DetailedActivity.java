package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by austinhle on 3/1/16.
 */
public class DetailedActivity extends Activity {
    public static final int MAX_COMMS = 4;
    public static final int MAX_BILLS = 6;

    public ArrayList<Integer> commIds;
    public ArrayList<Integer> billIds;
    public ArrayList<Integer> dateIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        // Initialize lists of IDs for later updating.
        commIds = new ArrayList<Integer>(Arrays.asList(
                R.id.c1, R.id.c2, R.id.c3, R.id.c4
        ));
        billIds = new ArrayList<Integer>(Arrays.asList(
                R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6
        ));
        dateIds = new ArrayList<Integer>(Arrays.asList(
                R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5, R.id.d6
        ));

        Intent intent = getIntent();
        String name = intent.getStringExtra(CongressionalActivity.NAME);

        Person p = CongressionalActivity.nameToPerson.get(name);
        String party = p.party;

        // Update background color.
        ScrollView sView = (ScrollView) findViewById(R.id.detailed_view);
        if (party.equalsIgnoreCase(Person.DEMOCRAT)) {
            sView.setBackgroundResource(R.color.democrat);
        } else if (party.equalsIgnoreCase(Person.REPUBLICAN)){
            sView.setBackgroundResource(R.color.republican);
        } else {
            sView.setBackgroundResource(R.color.independent);
        }

        // TODO: Get correct image from Twitter.
        ImageView view = (ImageView) findViewById(R.id.cImage);
        view.setImageBitmap(p.bmp);

        // Update name, position, party, and term end date.
        TextView info = (TextView) findViewById(R.id.cName);
        info.setText(p.name);
        info = (TextView) findViewById(R.id.cPosition);
        info.setText(p.title);
        info = (TextView) findViewById(R.id.cParty);
        info.setText(p.party);
        if (party.equalsIgnoreCase(Person.DEMOCRAT)) {
            info.setTextColor(getResources().getColor(R.color.democrat));
        } else if (party.equalsIgnoreCase(Person.REPUBLICAN)){
            info.setTextColor(getResources().getColor(R.color.republican));
        } else {
            info.setTextColor(getResources().getColor(R.color.independent));
        }

        info = (TextView) findViewById(R.id.cTerm);
        info.setText("Term ends on " + p.endOfTermDate);

        // Update committees list.
        TextView commView;
        for (int i = 0; i < p.committees.size() && i < MAX_COMMS; i++) {
            commView = (TextView) findViewById(commIds.get(i));
            commView.setText(p.committees.get(i));
        }

        // Update bills list.
        TextView billView;
        for (int i = 0; i < p.bills.size() && i < MAX_BILLS; i++) {
            billView = (TextView) findViewById(billIds.get(i));
            billView.setText(truncateBillString(p.bills.get(i).name));
        }

        // Update corresponding dates for bills.
        TextView dateView;
        for (int i = 0; i < p.bills.size() && i < MAX_BILLS; i++) {
            dateView = (TextView) findViewById(dateIds.get(i));
            dateView.setText(p.bills.get(i).date);
        }
    }

    public String truncateBillString(String s) {
        return s;
//        return (s.length() > 40) ? s.substring(0, 40) + "..." : s;
    }
}
