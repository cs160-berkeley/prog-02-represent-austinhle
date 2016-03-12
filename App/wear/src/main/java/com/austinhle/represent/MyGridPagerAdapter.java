package com.austinhle.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by austinhle on 3/2/16.
 */
public class MyGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private String data;

    private static String county;
    private static String state;
    private static double obamaVote;
    private static double romneyVote;

    private List<SimplePerson> legislators = new ArrayList<SimplePerson>();

    public MyGridPagerAdapter(Context ctx, FragmentManager fm, String data) {
        super(fm);
        mContext = ctx;
        this.data = data;

        Log.d("MyGridPagerAdapter", "Data: " + data);

        if (data != null) {
            populateInstanceVariables();
        }
    }

    private void populateInstanceVariables() {
        StringTokenizer st = new StringTokenizer(data, "|");

        String mCounty = st.nextToken();
        String mState = st.nextToken();
        Double mObamaVote = Double.parseDouble(st.nextToken());
        Double mRomneyVote = Double.parseDouble(st.nextToken());

        if (mCounty != null) {
            county = mCounty;
        }
        if (mState != null) {
            state = mState;
        }
        if (mObamaVote != 0.0) {
            obamaVote = mObamaVote;
        }
        if (mRomneyVote != 0.0) {
            romneyVote = mRomneyVote;
        }

        while (st.hasMoreElements()) {
            SimplePerson sp = new SimplePerson();
            sp.name = st.nextToken();
            if (sp.name == null || sp.name.equalsIgnoreCase("null"))
                return;

            sp.party = st.nextToken();
            if (sp.party == null || sp.party.equalsIgnoreCase("null"))
                return;

            sp.title = st.nextToken();
            if (sp.title == null || sp.title.equalsIgnoreCase("null"))
                return;

            Log.d("MyGridPagerAdapter", "Got: " + sp.name);
            legislators.add(sp);
        }
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        Bundle bundle = new Bundle();

        if (col == 0 && row == 0) { // Home screen for watch.
            CardFragment fragment = new HomeFragment();
            fragment.setCardGravity(Gravity.BOTTOM);
            fragment.setExpansionEnabled(true);
            fragment.setExpansionDirection(CardFragment.EXPAND_DOWN);
            fragment.setExpansionFactor(3.0f);
            return fragment;
        }

        if (row == 1) { // Show county vote data from 2012 elections.
            bundle.putString("county", county + ", " + state);
            bundle.putString("obama", "B. Obama " + obamaVote + "%");
            bundle.putString("romney", "M. Romney " + romneyVote + "%");
        } else { // Row 0 but col != 0
            SimplePerson sp = legislators.get(col - 1);
            bundle.putString("name", sp.name);
            bundle.putString("party", sp.party);
            bundle.putString("title", sp.title);
        }

        CardFragment fragment;
        if (row == 0) {
            fragment = new MyFragment();
        } else {
            fragment = new CountyFragment();
        }
        fragment.setArguments(bundle);
        fragment.setCardGravity(Gravity.BOTTOM);
        fragment.setExpansionEnabled(true);
        fragment.setExpansionDirection(CardFragment.EXPAND_DOWN);
        fragment.setExpansionFactor(3.0f);
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage (int row, int column) {
        return BACKGROUND_NONE;
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        if (legislators.isEmpty()) {
            return 1;
        } else {
            return 2;
        }
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        if (legislators.isEmpty()) {
            return 1;
        } else {
            return legislators.size() + 1;
        }
    }
}
