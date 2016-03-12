package com.austinhle.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by austinhle on 3/1/16.
 */
public class CongressionalActivity extends Activity {
    public static final String SUNLIGHT_API_KEY = "fe2173613eaa4a56b4d08e8317de38ff";
    public static final String GOOGLE_API_KEY = "AIzaSyDTzOcsmRpkNxLzNLcolwMVQi3g9AOMfvA";

    public static final String STATE_TYPE = "administrative_area_level_1";
    public static final String COUNTY_TYPE = "administrative_area_level_2";

    public static final String GEOCODE_BASE = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyDTzOcsmRpkNxLzNLcolwMVQi3g9AOMfvA";
    public static final String REVERSE_GEOCODE_BASE = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyDTzOcsmRpkNxLzNLcolwMVQi3g9AOMfvA";
    public static final String SUNLIGHT_LEGISLATORS_ZIP_BASE = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=%s&apikey=fe2173613eaa4a56b4d08e8317de38ff";
    public static final String SUNLIGHT_LEGISLATORS_COORD_BASE = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=%s&longitude=%s&apikey=fe2173613eaa4a56b4d08e8317de38ff";
    public static final String SUNLIGHT_COMMS_BASE = "http://congress.api.sunlightfoundation.com/committees?member_ids=%s&apikey=fe2173613eaa4a56b4d08e8317de38ff";
    public static final String SUNLIGHT_BILLS_BASE = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=%s&apikey=fe2173613eaa4a56b4d08e8317de38ff";
    public static final String IMAGE_BASE = "https://theunitedstates.io/images/congress/225x275/%s.jpg";

    // String keys
    public static final String NAME = "com.austinhle.represent.NAME";
    public static final String ZIPCODE = "com.austinhle.represent.ZIPCODE";
    public static final String RANDOM_ZIPCODE = "com.austinhle.represent.RANDOM_ZIPCODE";
    public static final String COUNTY = "com.austinhle.represent.COUNTY";
    public static final String STATE = "com.austinhle.represent.STATE";
    public static final String ROMNEY = "com.austinhle.represent.ROMNEY";
    public static final String OBAMA = "com.austinhle.represent.OBAMA";
    public static final String LEGISLATORS = "com.austinhle.represent.LEGISLATORS";

    public static HashMap<String, Person> nameToPerson;
    public static HashMap<String, Person> idToPerson;
    public static HashMap<String, Boolean> addedToView;

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;

    TwitterApiClient twitterApiClient;
    StatusesService statusesService;

    Bundle watchBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congressional_view);

        twitterApiClient = TwitterCore.getInstance().getApiClient();
        statusesService = twitterApiClient.getStatusesService();

        watchBundle = new Bundle();

        idToPerson = new HashMap<String, Person>();
        nameToPerson = new HashMap<String, Person>();
        addedToView = new HashMap<String, Boolean>();

        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        Intent intent = getIntent();
        String zipcode = intent.getStringExtra(MainActivity.ZIPCODE);
        String latitude = intent.getStringExtra(MainActivity.LAT);
        String longitude = intent.getStringExtra(MainActivity.LONG);
        String randomZipcode = intent.getStringExtra(RANDOM_ZIPCODE);

        TextView locationText = (TextView) findViewById(R.id.location);

        if (latitude != null && longitude != null) { // Use current location.
            locationText.setText("Current location: (" + latitude + ", " + longitude + ")");

            // Use reverse geocoding API to get county for watch.
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("onCreate [coords]", "Calling Reverse Geocoding API to get county for coords.");
                new ReverseGeocodingAPITask().execute(String.format(REVERSE_GEOCODE_BASE, latitude, longitude));
            } else {
                Log.d("onCreate [coords]", "No network connection available.");
            }

            // Directly call Sunlight API to get data using lat, long.
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("onCreate [coords]", "Calling Sunlight API to get legislators for coords.");
                new SunlightAPITask().execute(String.format(SUNLIGHT_LEGISLATORS_COORD_BASE, latitude, longitude));
            } else {
                Log.d("onCreate [coords]", "No network connection available.");
            }

        } else if (zipcode != null) { // Use provided zip code.
            locationText.setText("Current location: " + zipcode);

            // Call Geocoding API to get lat, long.
            // Then use reverse geocoding API to get county for watch.
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("onCreate [zipcode]", "Calling Location API to get coords for zipcode.");
                new LocationAPITask().execute(String.format(GEOCODE_BASE, zipcode));
            } else {
                Log.d("onCreate [zipcode]", "No network connection available.");
            }

            // Directly call Sunlight API to get data using zip code. May get more than 3 people.
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("onCreate [zipcode]", "Calling Sunlight API to get legislators for zipcode.");
                new SunlightAPITask().execute(String.format(SUNLIGHT_LEGISLATORS_ZIP_BASE, zipcode));
            } else {
                Log.d("onCreate [zipcode]", "No network connection available.");
            }
        } else if (randomZipcode != null) { // Generate random location because watch was shaken.
            double[] coords = getValidRandomCoords();
            double randLat = coords[0];
            double randLong = coords[1];
            locationText.setText("Random location: (" + randLat + ", " + randLong + ")");

            // Use reverse geocoding API to get county for watch.
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("onCreate [random]", "Calling Reverse Geocoding API to get county for random coords.");
                new ReverseGeocodingAPITask().execute(String.format(REVERSE_GEOCODE_BASE, randLat, randLong));
            } else {
                Log.d("onCreate [random]", "No network connection available.");
            }

            // Directly call Sunlight API to get data using lat, long.
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("onCreate [random]", "Calling Sunlight API to get legislators for random coords.");
                new SunlightAPITask().execute(String.format(SUNLIGHT_LEGISLATORS_COORD_BASE, randLat, randLong));
            } else {
                Log.d("onCreate [random]", "No network connection available.");
            }
        }
    }

    public double[] getValidRandomCoords() {
        double[] coords = new double[2];

        try {
            InputStream stream = getAssets().open("random-places.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            final int NUM_LINES = 43583;
            while (true) {
                int randomIndex = (int) (Math.random() * NUM_LINES);
                int i = 0;

                String line = br.readLine();
                while (i < randomIndex && line != null) {
                    i++;
                    line = br.readLine();
                }
                StringTokenizer st = new StringTokenizer(line, ",");

                // Burn through irrelevant data from text file.
                st.nextToken();
                st.nextToken();
                st.nextToken();
                st.nextToken();
                st.nextToken();

                String lat, lng;
                if (st.hasMoreTokens()) {
                    lat = st.nextToken();
                    coords[0] = Double.parseDouble(lat);
                } else {
                    continue;
                }
                if (st.hasMoreTokens()) {
                    lng = st.nextToken();
                    coords[1] = Double.parseDouble(lng);
                } else {
                    continue;
                }

                br.close();
                return coords;
            }
        } catch (IOException e) {
            Log.d("getValidRandomCoords", "IOException: " + e);
        }

        return coords;
    }

    public void getAndBundleCountyData(String county, String state) throws org.json.JSONException {
        try {
            InputStream stream = getAssets().open("election-county-2012.json");

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");

            JSONObject myJObject = new JSONObject(jsonString);
            // TODO: Parse provided county data to get the data needed to be sent to watch. Store this data in watchBundle.
            String key = county + ", " + state;
            JSONObject results = myJObject.optJSONObject(key);
            double romneyVote = 0.0;
            double obamaVote = 0.0;
            if (results == null) {
                Log.d("getAndBundleCountyData", "No county found for key: " + key);
            } else {
                romneyVote = results.optDouble("romney");
                obamaVote = results.optDouble("obama");
            }

            Log.d("getAndBundleCountyData", String.format("Romney: %f, Obama: %f", romneyVote, obamaVote));

            watchBundle.putDouble(ROMNEY, romneyVote);
            watchBundle.putDouble(OBAMA, obamaVote);
        } catch (IOException e) {
            Log.d("getAndBundleCountyData", "IOException in opening assets: " + e);
        }

        // Send bundle and update watch.
        watchBundle.putString(COUNTY, county);
        watchBundle.putString(STATE, state);
        updateWatch();
    }

    public void updateWatch() {
        // Send information to watch to update its list of legislators.
        if (watchBundle.getString(COUNTY) != null && watchBundle.getString(LEGISLATORS) != null) {
            Log.d("updateWatch", "Sending watchBundle to watch with some data");
            Intent sendIntent = new Intent(this, PhoneToWatchService.class);
            sendIntent.putExtras(watchBundle);
            startService(sendIntent);
        }
    }

    public void updateSmartphoneDisplay() {
        Log.d("updateSmartphoneDisplay", "Updating smartphone display of legislators.");

        LinearLayout ll = (LinearLayout) findViewById(R.id.linear);

        for (String name : nameToPerson.keySet()) {
            Person p = nameToPerson.get(name);

            // Inflate a new legislator card view.
            View legislatorView = getLayoutInflater().inflate(R.layout.legislator, ll, false);

            ImageView imageView = (ImageView) legislatorView.findViewById(R.id.cImage);
            imageView.setImageBitmap(p.bmp);
            p.imageView = imageView;

            TextView nameView = (TextView) legislatorView.findViewById(R.id.cName);
            nameView.setText(p.name);

            TextView titleView = (TextView) legislatorView.findViewById(R.id.cTitle);
            titleView.setText(p.title);

            TextView emailView = (TextView) legislatorView.findViewById(R.id.cEmail);
            emailView.setText(p.email);

            TextView websiteView = (TextView) legislatorView.findViewById(R.id.cWebsite);
            websiteView.setText(p.website);

            TextView tweetView = (TextView) legislatorView.findViewById(R.id.cTweet);
            if (p.twitterId != null && !p.twitterId.equalsIgnoreCase("null")) {
                tweetView.setText("@" + p.twitterId + " " + p.lastTweet);
            } else {
                tweetView.setText(p.lastTweet);
            }
            p.tweetView = tweetView;

            CardView cardView = (CardView) legislatorView.findViewById(R.id.card);
            ImageView partyView = (ImageView) legislatorView.findViewById(R.id.cParty);

            if (p.party.equalsIgnoreCase(Person.DEMOCRAT)) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.democrat));
                partyView.setImageResource(R.drawable.dem2);
                emailView.setBackgroundResource(R.color.dem_button);
                websiteView.setBackgroundResource(R.color.dem_button);
            } else if (p.party.equalsIgnoreCase(Person.REPUBLICAN)){
                cardView.setCardBackgroundColor(getResources().getColor(R.color.republican));
                emailView.setBackgroundResource(R.color.rep_button);
                websiteView.setBackgroundResource(R.color.rep_button);
                partyView.setImageResource(R.drawable.rep2);
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.independent));
                partyView.setImageResource(R.drawable.ind);
                emailView.setBackgroundResource(R.color.ind_button);
                websiteView.setBackgroundResource(R.color.ind_button);
            }

            // Add the legislator card view to the overall congressional view.
            ll.addView(legislatorView);
            addedToView.put(p.name, true);
        }
    }

    public void viewCongressMember(View view) {
        ImageView iconClicked = (ImageView) view;
        LinearLayout ll = (LinearLayout) (iconClicked.getParent().getParent());
        TextView nameView = (TextView) ll.findViewById(R.id.cName);
        String name = nameView.getText().toString();

        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra(NAME, name);
        startActivity(intent);
    }

    // Used to compute the latitude and longitude coordinates for some zipcode.
    // Also used to then figure out the county for those coordinates.
    private class LocationAPITask extends CallAPITask {
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                // Parse result to get latitude and longitude in order to find county.

                String lat = "";
                String lon = "";
                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.optJSONArray("results");
                JSONObject res = jArray.optJSONObject(0);
                JSONObject loc = res.optJSONObject("geometry").optJSONObject("location");
                lat = loc.getString("lat");
                lon = loc.getString("lng");

                Log.d("LocationAPITask", String.format("Latitude: %s, Longitude: %s", lat, lon));

                // Call reverse geocoding API to get county.
                if (networkInfo != null && networkInfo.isConnected()) {
                    new ReverseGeocodingAPITask().execute(String.format(REVERSE_GEOCODE_BASE, lat, lon));
                } else {
                    Log.d("LocationAPITask", "No network connection available.");
                }
            } catch (org.json.JSONException e) {
                Log.d("LocationAPITask", "Error in JSON parsing: " + e);
            }
        }
    }

    // Used to figure out what county to display 2012 vote data for.
    private class ReverseGeocodingAPITask extends CallAPITask {
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                // Parse result string to get county and state.
                String county = "";
                String state = "";

                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.optJSONArray("results");
                JSONArray addressComponents = jArray.optJSONObject(0).optJSONArray("address_components");
                for (int i = 0; i < addressComponents.length(); i++) {
                    JSONObject jObj = addressComponents.optJSONObject(i);
                    JSONArray types = jObj.optJSONArray("types");
                    for (int j = 0; j < types.length(); j++) {
                        String s = types.getString(j);
                        if (s.equalsIgnoreCase(COUNTY_TYPE)) {
                            county = jObj.getString("short_name");
                        }
                        if (s.equalsIgnoreCase(STATE_TYPE)) {
                            state = jObj.getString("short_name");
                        }
                    }
                }

                Log.d("ReverseGeocodingAPITask", String.format("(County, State) found: (%s, %s)", county, state));

                getAndBundleCountyData(county, state);
            } catch (org.json.JSONException e) {
                Log.d("ReverseGeocodingAPITask", "Error in JSON parsing: " + e);
            }
        }
    }

    // Used to get information on the legislators for some given coordinates or zipcode.
    private class SunlightAPITask extends CallAPITask {
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jObject = new JSONObject(result);

                // Parse returned data to get information on 3+ legislators.
                int count = jObject.getInt("count");
                Log.d("SunlightAPITask", "Number of legislators found: " + count);

                List<String> legislatorsInfo = new ArrayList<>();

                JSONArray people = jObject.optJSONArray("results");
                for (int i = 0; i < people.length(); i++) {
                    JSONObject details = people.optJSONObject(i);
                    final Person p = new Person();
                    p.name = details.optString("first_name") + " " + details.optString("last_name");

                    p.party = details.optString("party");
                    if (p.party.equalsIgnoreCase("D")) {
                        p.party = "Democrat";
                    } else if (p.party.equalsIgnoreCase("R")){
                        p.party = "Republican";
                    } else {
                        p.party = "Independent";
                    }

                    p.title = details.optString("title");
                    if (p.title.equalsIgnoreCase("Sen")) {
                        p.title = "Senator";
                    } else {
                        p.title = "Representative";
                    }

                    p.email = details.optString("oc_email");
                    p.website = details.optString("website");
                    p.endOfTermDate = details.optString("term_end");
                    p.twitterId = details.optString("twitter_id");
                    p.bioguideId = details.optString("bioguide_id");

                    // Add to string storing all relevant info for watch.
                    legislatorsInfo.add(p.name);
                    legislatorsInfo.add(p.party);
                    legislatorsInfo.add(p.title);

                    // Update HashMaps.
                    nameToPerson.put(p.name, p);
                    idToPerson.put(p.bioguideId, p);

                    // Call Sunlight committees API to get committees.
                    if (networkInfo != null && networkInfo.isConnected()) {
                        SunlightCommsAPITask t = new SunlightCommsAPITask();
                        t.bioguideId = p.bioguideId;
                        t.execute(String.format(SUNLIGHT_COMMS_BASE, t.bioguideId));
                    } else {
                        Log.d("SunlightAPITask", "No network connection available.");
                    }

                    // Call Sunlight bills API to get bills.
                    if (networkInfo != null && networkInfo.isConnected()) {
                        SunlightBillsAPITask t = new SunlightBillsAPITask();
                        t.bioguideId = p.bioguideId;
                        t.execute(String.format(SUNLIGHT_BILLS_BASE, t.bioguideId));
                    } else {
                        Log.d("SunlightAPITask", "No network connection available.");
                    }

                    // Download and store profile picture.
                    if (networkInfo != null && networkInfo.isConnected()) {
                        DownloadImageTask t = new DownloadImageTask();
                        t.bioguideId = p.bioguideId;
                        t.execute(String.format(IMAGE_BASE, t.bioguideId));
                    } else {
                        Log.d("SunlightAPITask", "No network connection available.");
                    }

                    // Use Twitter API to get latest tweet.
                    if (networkInfo != null && networkInfo.isConnected()) {
                        statusesService.userTimeline(null, p.twitterId, null, 1L, null, null, null, null, null, new Callback<List<Tweet>>() {
                            @Override
                            public void success(Result<List<Tweet>> result) {
                                Tweet t = result.data.get(0);
                                p.lastTweet = t.text.replaceAll("&amp;", "&");
                                if (p.tweetView != null) {
                                    p.tweetView.setText("@" + p.twitterId + " " + p.lastTweet);
                                }
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Log.d("StatusesService", "Failure in retrieving tweets from user timeline: " + e);
                            }
                        });
                    } else {
                        Log.d("SunlightAPITask", "No network connection available.");
                    }
                }

                // Store relevant data into watchBundle.
                String watchString = TextUtils.join("|", legislatorsInfo.toArray());
                Log.d("SunlightAPITask", "String sent to watch: " + watchString);
                watchBundle.putString(LEGISLATORS, watchString);

                // Update smartphone display.
                updateSmartphoneDisplay();

                // Send data to watch for updates.
                updateWatch();

            } catch (org.json.JSONException e) {
                Log.d("SunlightAPITask", "Error in JSON parsing: " + e);
            }
        }
    }

    private class SunlightCommsAPITask extends CallAPITask {
        public String bioguideId;

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                // Parse result and get all committee names. Then put them into Person's list.
                Person p = idToPerson.get(bioguideId);

                JSONObject jObject = new JSONObject(result);

                JSONArray comms = jObject.optJSONArray("results");
                for (int i = 0; i < comms.length(); i++) {
                    JSONObject comm = comms.optJSONObject(i);
                    p.committees.add(comm.optString("name"));
                }
            } catch (org.json.JSONException e) {
                Log.d("SunlightCommsAPITask", "Error in JSON parsing: " + e);
            }
        }
    }

    private class SunlightBillsAPITask extends CallAPITask {
        public String bioguideId;

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                // Parse result and get all bill names. Then put them into Person's list.
                Person p = idToPerson.get(bioguideId);

                JSONObject jObject = new JSONObject(result);

                JSONArray bills = jObject.optJSONArray("results");
                for (int i = 0; i < bills.length(); i++) {
                    JSONObject bill = bills.optJSONObject(i);
                    String date = bill.optString("introduced_on");
                    String shortTitle = bill.optString("short_title");
                    String officialTitle = bill.optString("official_title");
                    if (shortTitle == null || shortTitle.equalsIgnoreCase("null")) {
                        p.bills.add(new Bill(date, officialTitle));
                    } else {
                        p.bills.add(new Bill(date, shortTitle));
                    }

                }
            } catch (org.json.JSONException e) {
                Log.d("SunlightBillsAPITask", "Error in JSON parsing: " + e);
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        String bioguideId;

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("DownloadImageTask", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            Person p = idToPerson.get(bioguideId);
            p.bmp = result;
            if (p.imageView != null) {
                p.imageView.setImageBitmap(p.bmp);
            }
        }
    }

    private class CallAPITask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return makeGETRequest(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        private String makeGETRequest(String url) throws IOException {
            InputStream is = null;

            try {
                URL u = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("makeGETRequest", "The response code for " + url + " is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                return streamToString(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        public String streamToString(InputStream stream) throws IOException, UnsupportedEncodingException {
            Scanner s = new Scanner(stream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
