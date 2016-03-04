package com.austinhle.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by austinhle on 3/1/16.
 */
public class DetailedActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        Intent intent = getIntent();
        String name = intent.getStringExtra(CongressionalActivity.NAME);

        // TODO: Part C, use name to retrieve corresponding data from data structures.
        if (name.equals("Barbara Boxer")) {
            // Update background color.
            ScrollView sView = (ScrollView) findViewById(R.id.detailed_view);
            sView.setBackgroundResource(R.color.democrat);

            // Update image.
            ImageView view = (ImageView) findViewById(R.id.cImage1);
            view.setImageResource(R.drawable.boxer);

            // Update name, position, party, and term end date.
            TextView info = (TextView) findViewById(R.id.cName1);
            info.setText(name);
            info = (TextView) findViewById(R.id.cPosition1);
            info.setText(R.string.sen);
            info = (TextView) findViewById(R.id.cParty1);
            info.setText(R.string.democrat);
            info.setTextColor(getResources().getColor(R.color.democrat));
            info = (TextView) findViewById(R.id.term);
            info.setText(R.string.term);

            // Update committees list.
            TextView comms = (TextView) findViewById(R.id.c1);
            comms.setText("Select Committee on Ethics");
            comms = (TextView) findViewById(R.id.c2);
            comms.setText("Committee on Environment and Public Works");
            comms = (TextView) findViewById(R.id.c3);
            comms.setText("Committee on Foreign Relations");

            // Update bills list.
            TextView bills = (TextView) findViewById(R.id.b1);
            bills.setText("Female Veteran Prevention Suicide Act");
            bills = (TextView) findViewById(R.id.b2);
            bills.setText("Tule Lake National Historic Site Establish...");
            bills = (TextView) findViewById(R.id.b3);
            bills.setText("End of Suffering Act");
            bills = (TextView) findViewById(R.id.b4);
            bills.setText("SAFE DRONE Act");
            bills = (TextView) findViewById(R.id.b5);
            bills.setText("West Coast Ocean Protection Act");
            bills = (TextView) findViewById(R.id.b6);
            bills.setText("Pell Grant Restoration Act");

            // Update corresponding dates for bills.
            TextView dates = (TextView) findViewById(R.id.d1);
            dates.setText("02/03/16");
            dates = (TextView) findViewById(R.id.d2);
            dates.setText("12/17/15");
            dates = (TextView) findViewById(R.id.d3);
            dates.setText("10/22/15");
            dates = (TextView) findViewById(R.id.d4);
            dates.setText("10/07/15");
            dates = (TextView) findViewById(R.id.d5);
            dates.setText("10/07/15");
            dates = (TextView) findViewById(R.id.d6);
            dates.setText("09/16/15");

        } else if (name.equals("Dianne Feinstein")) {
            // Update background color.
            ScrollView sView = (ScrollView) findViewById(R.id.detailed_view);
            sView.setBackgroundResource(R.color.democrat);

            // Update image.
            ImageView view = (ImageView) findViewById(R.id.cImage1);
            view.setImageResource(R.drawable.feinstein);

            // Update name, position, party, and term end date.
            TextView info = (TextView) findViewById(R.id.cName1);
            info.setText(name);
            info = (TextView) findViewById(R.id.cPosition1);
            info.setText(R.string.sen);
            info = (TextView) findViewById(R.id.cParty1);
            info.setText(R.string.democrat);
            info.setTextColor(getResources().getColor(R.color.democrat));
            info = (TextView) findViewById(R.id.term);
            info.setText(R.string.term);

            // Update committees list.
            TextView comms = (TextView) findViewById(R.id.c1);
            comms.setText("Select Committee on Intelligence");
            comms = (TextView) findViewById(R.id.c2);
            comms.setText("Committee on Appropriations");
            comms = (TextView) findViewById(R.id.c3);
            comms.setText("Committee on the Judiciary");

            // Update bills list.
            TextView bills = (TextView) findViewById(R.id.b1);
            bills.setText("California Desert Conservation Act");
            bills = (TextView) findViewById(R.id.b2);
            bills.setText("Interstate Threats Clarification Act of 2016");
            bills = (TextView) findViewById(R.id.b3);
            bills.setText("California Long-Term Provisions for Water...");
            bills = (TextView) findViewById(R.id.b4);
            bills.setText("Requiring Reporting of Online Terrorist...");
            bills = (TextView) findViewById(R.id.b5);
            bills.setText("Visa Waiver Program Security Enhancement...");
            bills = (TextView) findViewById(R.id.b6);
            bills.setText("Scholarships for Opportunity and Results...");

            // Update corresponding dates for bills.
            TextView dates = (TextView) findViewById(R.id.d1);
            dates.setText("02/23/16");
            dates = (TextView) findViewById(R.id.d2);
            dates.setText("02/11/16");
            dates = (TextView) findViewById(R.id.d3);
            dates.setText("02/10/16");
            dates = (TextView) findViewById(R.id.d4);
            dates.setText("12/08/15");
            dates = (TextView) findViewById(R.id.d5);
            dates.setText("12/01/15");
            dates = (TextView) findViewById(R.id.d6);
            dates.setText("10/08/15");
        } else if (name.equals("Doug Lamalfa")) {
            // Update background color.
            ScrollView sView = (ScrollView) findViewById(R.id.detailed_view);
            sView.setBackgroundResource(R.color.republican);

            // Update image.
            ImageView view = (ImageView) findViewById(R.id.cImage1);
            view.setImageResource(R.drawable.lamalfa);

            // Update name, position, party, and term end date.
            TextView info = (TextView) findViewById(R.id.cName1);
            info.setText(name);
            info = (TextView) findViewById(R.id.cPosition1);
            info.setText(R.string.rep);
            info = (TextView) findViewById(R.id.cParty1);
            info.setText(R.string.republican);
            info.setTextColor(getResources().getColor(R.color.republican));
            info = (TextView) findViewById(R.id.term);
            info.setText(R.string.term);

            // Update committees list.
            TextView comms = (TextView) findViewById(R.id.c1);
            comms.setText("Committee on Agriculture");
            comms = (TextView) findViewById(R.id.c2);
            comms.setText("Committee on Natural Resources");
            comms = (TextView) findViewById(R.id.c3);
            comms.setText("");

            // Update bills list.
            TextView bills = (TextView) findViewById(R.id.b1);
            bills.setText("Tule Lake National Historic Site Establish...");
            bills = (TextView) findViewById(R.id.b2);
            bills.setText("Safe Agriculture Production Act of 2015");
            bills = (TextView) findViewById(R.id.b3);
            bills.setText("Federal Wildland Firefighter Recognition Act");
            bills = (TextView) findViewById(R.id.b4);
            bills.setText("Public Power Risk Management Act of 2015");
            bills = (TextView) findViewById(R.id.b5);
            bills.setText("Expressing support for designation of...");
            bills = (TextView) findViewById(R.id.b6);
            bills.setText("Santa Ynez Band of Chumash Mission...");

            // Update corresponding dates for bills.
            TextView dates = (TextView) findViewById(R.id.d1);
            dates.setText("01/13/16");
            dates = (TextView) findViewById(R.id.d2);
            dates.setText("10/08/15");
            dates = (TextView) findViewById(R.id.d3);
            dates.setText("07/29/15");
            dates = (TextView) findViewById(R.id.d4);
            dates.setText("04/28/15");
            dates = (TextView) findViewById(R.id.d5);
            dates.setText("03/03/15");
            dates = (TextView) findViewById(R.id.d6);
            dates.setText("02/27/15");
        } else if (name.equals("Barbara Lee")) {
            // Update background color.
            ScrollView sView = (ScrollView) findViewById(R.id.detailed_view);
            sView.setBackgroundResource(R.color.democrat);

            // Update image.
            ImageView view = (ImageView) findViewById(R.id.cImage1);
            view.setImageResource(R.drawable.lee);

            // Update name, position, party, and term end date.
            TextView info = (TextView) findViewById(R.id.cName1);
            info.setText(name);
            info = (TextView) findViewById(R.id.cPosition1);
            info.setText(R.string.rep);
            info = (TextView) findViewById(R.id.cParty1);
            info.setText(R.string.democrat);
            info.setTextColor(getResources().getColor(R.color.democrat));
            info = (TextView) findViewById(R.id.term);
            info.setText(R.string.term);

            // Update committees list.
            TextView comms = (TextView) findViewById(R.id.c1);
            comms.setText("Committee on Appropriations");
            comms = (TextView) findViewById(R.id.c2);
            comms.setText("Committee on the Budget");
            comms = (TextView) findViewById(R.id.c3);
            comms.setText("");

            // Update bills list.
            TextView bills = (TextView) findViewById(R.id.b1);
            bills.setText("Improving Access to Mental Health Act");
            bills = (TextView) findViewById(R.id.b2);
            bills.setText("Recognizing the 70th anniversary of...");
            bills = (TextView) findViewById(R.id.b3);
            bills.setText("Equal Access to Abortion Coverage in Health...");
            bills = (TextView) findViewById(R.id.b4);
            bills.setText("Pathways out of Poverty Act");
            bills = (TextView) findViewById(R.id.b5);
            bills.setText("Calling for Sickle Cell Trait Research");
            bills = (TextView) findViewById(R.id.b6);
            bills.setText("Recognizing the significance of National...");

            // Update corresponding dates for bills.
            TextView dates = (TextView) findViewById(R.id.d1);
            dates.setText("10/08/15");
            dates = (TextView) findViewById(R.id.d2);
            dates.setText("09/16/15");
            dates = (TextView) findViewById(R.id.d3);
            dates.setText("07/08/15");
            dates = (TextView) findViewById(R.id.d4);
            dates.setText("06/10/15");
            dates = (TextView) findViewById(R.id.d5);
            dates.setText("06/03/15");
            dates = (TextView) findViewById(R.id.d6);
            dates.setText("05/21/15");
        } else {

        }
    }
}
