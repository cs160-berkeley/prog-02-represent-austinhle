package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by austinhle on 3/1/16.
 */
public class CongressionalActivity extends Activity {
    public final static String NAME = "com.austinhle.represent.NAME";
    public static final String RANDOM_ZIPCODE = "com.austinhle.represent.RANDOM_ZIPCODE";
    private String zipcode;
    private String randomZipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congressional_view);

        Intent intent = getIntent();
        zipcode = intent.getStringExtra(MainActivity.ZIPCODE);
        randomZipcode = intent.getStringExtra(RANDOM_ZIPCODE);

        TextView zipcodeView = (TextView) findViewById(R.id.zipcode_results);

        if (zipcode != null) {
            zipcodeView.setText(zipcode);
        } else if (randomZipcode != null) {
            double randLat = (Math.random() * 180) - 90;
            double randLong = (Math.random() * 360) - 180;
            String coords = "(" + randLat + ", " + randLong + ")";
            zipcodeView.setText(coords);
        } else {
            // Shouldn't happen.
            zipcodeView.setText("94720"); // Yay Berkeley.
        }

        // TODO: Actually randomize zipcode and pull data for all 3 congressmen from API.
        if (randomZipcode != null || (zipcode != null && !zipcode.equals("95914"))) {
            ImageView pic = (ImageView) findViewById(R.id.cImage3);
            pic.setImageResource(R.drawable.lee);

            TextView name = (TextView) findViewById(R.id.cName3);
            name.setText("Barbara Lee");

            ImageView party = (ImageView) findViewById(R.id.cParty3);
            party.setImageResource(R.drawable.dem);

            TextView tweet = (TextView) findViewById(R.id.cTweet3);
            tweet.setText(R.string.tweet4);

            TextView website = (TextView) findViewById(R.id.cWebsite3);
            website.setText("lee.house.gov");
            website.setBackgroundResource(R.color.dem_button);

            TextView email = (TextView) findViewById(R.id.cEmail3);
            email.setText("rep@lee.house.gov");
            email.setBackgroundResource(R.color.dem_button);

            CardView card = (CardView) findViewById(R.id.c3);
            card.setCardBackgroundColor(getResources().getColor(R.color.democrat));
        }
    }

    public void viewCongressMember(View view) {
        Intent intent = new Intent(this, DetailedActivity.class);
        ImageView iconClicked = (ImageView) view;
        String congressmanName = MainActivity.map.get(iconClicked.getId());
        intent.putExtra(NAME, congressmanName);
        startActivity(intent);
    }
}
