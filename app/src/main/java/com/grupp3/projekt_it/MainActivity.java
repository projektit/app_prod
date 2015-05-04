package com.grupp3.projekt_it;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private TextView tv;

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
        // Array for storing the names of the files containing the tips for different months
        String[] tipsArray = {"mon_tips_january.html","mon_tips_february.html","mon_tips_march.html",
                "mon_tips_april.html","mon_tips_may.html","mon_tips_june.html",
                "mon_tips_july.html","mon_tips_august.html","mon_tips_september.html",
                "mon_tips_october.html","mon_tips_november.html","mon_tips_december.html"};
        // Get the current month
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        // Read data from html file and display it depending on month
        try{
            InputStream stream = this.getAssets().open(tipsArray[currentMonth]);
            int streamSize = stream.available();
            byte[] buffer = new byte[streamSize];
            stream.read(buffer);
            stream.close();
            String html = new String(buffer);
            /**
             * Textview settings
             */
            // Define the specific textview
            tv = (TextView)findViewById(R.id.mon_tips_text);
            // Set the text in the textview
            tv.setText(Html.fromHtml(html));
            // Allow scrolling
            //tv.setMovementMethod(new ScrollingMovementMethod());
            // Allow links to be clicked
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Get correct drawable for the month
        TypedArray images = getResources().obtainTypedArray(R.array.monthArray);
        int resourceId = images.getResourceId(currentMonth, -1);
        // Define the ImageView
        ImageView im = (ImageView) findViewById(R.id.imageView);
        // Set correct drawable as background in the ImageView
        im.setImageResource(resourceId);

        // Array to store background colors to be shown in the activity
        String [] colors = {"#7c4c8a", "#790b14", "#608f31", "#f6c61a", "#71945c", "#b52c3e", "#89144b", "#a2882d", "#918f80", "#354a12", "#d3d069", "#7f4a18"};
        // Define the layout and set the color to the correct one
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.backgroundColor);
        rl.setBackgroundColor(Color.parseColor(colors[currentMonth]));

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
                startActivity(new Intent(this, HelpActivity.class));
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
