package com.austinhle.represent;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by austinhle on 3/11/16.
 */
public class Person {
    // Final static variables
    public static final String DEMOCRAT = "Democrat";
    public static final String REPUBLICAN = "Republican";
    public static final String INDEPENDENT = "Independent";
    public static final String SENATOR = "Senator";
    public static final String REPRESENTATIVE = "Representative";

    // Instance variables
    public String bioguideId;
    public String name;
    public String party;
    public String title;
    public String lastTweet;
    public String email;
    public String website;
    public String twitterId;
    public String endOfTermDate;
    public Bitmap bmp;
    public ArrayList<String> committees;
    public ArrayList<Bill> bills;

    public ImageView imageView;
    public TextView tweetView;

    public Person() {
        committees = new ArrayList<String>();
        bills = new ArrayList<Bill>();
    }
}
