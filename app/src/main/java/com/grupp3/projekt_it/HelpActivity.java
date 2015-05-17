package com.grupp3.projekt_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

/*
 * This activity gets data from a local file witch is then displayed in a simple textViev. It gives
 * users of the application answers to some basic functionality of the app and some of the features.
 */

public class HelpActivity extends BaseActivity {
    // Define the activity textView
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_activity_help, frameLayout);
        mDrawerList.setItemChecked(position, true);

        /*
         * Open an input stream and read the content of a file char by char and save it in a string
         */
        try{
            InputStream stream = this.getAssets().open("helpText.html");
            int streamSize = stream.available();
            byte[] buffer = new byte[streamSize];
            stream.read(buffer);
            stream.close();
            String html = new String(buffer);
            /**
             * Textview settings
             */
            // Define the specific textview
            tv = (TextView)findViewById(R.id.helpText);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_help, menu);
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
    /*
     * Method for hiding the overflow menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    protected void openActivity(int position) {

        /**
         * All activities with navigation drawer must contain this override function in order
         * to not be able to launch another instance of itself from navigation bar.
         * Remove correct startActivity(..) call to do so.
         */
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, MyGardenListActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, NotificationManager.class));
                break;
            case 3:
                startActivity(new Intent(this, PlantSearchActivity.class));
                break;
            case 4:
                break;
            case 5:
                startActivity(new Intent(this, Login.class));
                break;
            case 6:
                startActivity(new Intent(this, Preferences.class));
                break;
            default:
                break;
        }
}}