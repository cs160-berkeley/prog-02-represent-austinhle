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

import java.util.List;

/**
 * Created by austinhle on 3/2/16.
 */
public class MyGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private List mRows;
    private String zipcode;

    public MyGridPagerAdapter(Context ctx, FragmentManager fm, String zipcode) {
        super(fm);
        mContext = ctx;
        this.zipcode = zipcode;
    }

    static final int[] BG_IMAGES = new int[] {R.drawable.boxer, R.drawable.feinstein, R.drawable.lamalfa};

    // A simple container for static data in each page
    private static class Page {
        // static resources
        int titleRes;
        int textRes;
        int iconRes;

        public Page () {

        }

        public Page(int title, int text, int icon) {
            titleRes = title;
            textRes = text;
            iconRes = icon;
        }
    }

    private final Page[][] PAGES = {
            {new Page(R.string.name1, R.string.sen, R.drawable.ic_info_outline_white_24dp),
             new Page(R.string.name2, R.string.sen, R.drawable.ic_info_outline_white_24dp),
             new Page(R.string.name3, R.string.rep, R.drawable.ic_info_outline_white_24dp)},
            {new Page()}
    };

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        Bundle bundle = new Bundle();

        Log.d("T", "Inside getFragment(). Zipcode is " + zipcode);

        if (row == 1) {
            if (zipcode != null && zipcode.equals("95914")) {
                bundle.putString("county", "Butte County, California");
                bundle.putString("cand1", "B. Obama 46.4%");
                bundle.putString("cand2", "M. Romney 18.4%");
            } else {
                bundle.putString("county", "Alameda County, California");
                bundle.putString("cand1", "B. Obama 78.7%");
                bundle.putString("cand2", "M. Romney 18.4%");
            }
        }

        switch (col) {
            case 0:
                bundle.putString("name", "Barbara Boxer");
                bundle.putString("party", "Democrat");
                bundle.putString("position", "Senator");
                break;
            case 1:
                bundle.putString("name", "Dianne Feinstein");
                bundle.putString("party", "Democrat");
                bundle.putString("position", "Senator");
                break;
            case 2:
                if (zipcode != null && zipcode.equals("95914")) {
                    bundle.putString("name", "Doug Lamalfa");
                    bundle.putString("party", "Republican");
                    bundle.putString("position", "Representative");
                    break;
                } else {
                    bundle.putString("name", "Barbara Lee");
                    bundle.putString("party", "Democrat");
                    bundle.putString("position", "Representative");
                    break;
                }

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
        return PAGES.length;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }
}
