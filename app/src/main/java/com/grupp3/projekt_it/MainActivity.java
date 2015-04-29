package com.grupp3.projekt_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;


public class MainActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /**
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        //set alarms
        OnBootReceiver.setForecastAlarms(getApplicationContext());
        OnBootReceiver.setMonthlyAlarms(getApplicationContext());
        OnBootReceiver.setZoneAlarms(getApplicationContext());
        /**
         * Setting title and itemChecked
         */
        mDrawerList.setItemChecked(position, true);

        /**
         * Textview settings
         */
        // Define the specific textview
        TextView tv=(TextView)findViewById(R.id.mon_tips_text);
        // Set the text in the textview
        tv.setText(Html.fromHtml(getString(R.string.mon_tips)));
        // Allow scrolling
        tv.setMovementMethod(new ScrollingMovementMethod());
        // Allow links to be clicked
        tv.setMovementMethod(LinkMovementMethod.getInstance());

    }
    @Override
     public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void openActivity(int position) {

        /**
         * All activities with navigation drawer must contain this override function in order
         * to not be able to launch another instance of itself from navigation bar.
         * Remove correct startActivity(..) call to do so.
         */

//		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                break;
            case 1:
                startActivity(new Intent(this, MyGardenListActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, PlantSearchActivity.class));
                break;
            case 3:
                break;
            case 4:
                startActivity(new Intent(this, Login.class));
                break;
            case 5:
                // Send the user number to the preference activity
                //Intent intent = getIntent();
                //String user = intent.getExtras().getString("user");
                Intent i = new Intent(MainActivity.this, Preferences.class);
                //i.putExtra("userNumber", user);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    // on back button click, exit the application, hence skip the login screen
    @Override
    public void onBackPressed() {
        //Back closes drawer if opened
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){

            mDrawerLayout.closeDrawer(mDrawerList);

        } else {

            if (mBackPressed + TIME_INTERVAL > java.lang.System.currentTimeMillis()) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {

                Toast.makeText(getBaseContext(), "Klicka igen för att avsluta", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = java.lang.System.currentTimeMillis();
        }
    }
}
