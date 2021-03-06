package com.grupp3.projekt_it;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private TextView tv;
    String TAG = "com.grupp3.projekt_it";
    LinearLayout linearLayoutLeft;
    LinearLayout linearLayoutRight;

    ArrayList<MainItemObject> mainItemObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /**
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_main2, frameLayout);
        //set alarms
        OnBootReceiver.setForecastAlarms(getApplicationContext());
        int month = Calendar.getInstance().get(Calendar.MONTH);
        if(month == 11){
            month = 0;
        }else{
            month +=1;
        }
        OnBootReceiver.setMonthlyAlarms(getApplicationContext(), month);
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
        ArrayList <String> divList = new ArrayList<>();
        ArrayList <String> urlList = new ArrayList<>();
        // Read data from html file and display it depending on month
        try{
            InputStream stream = this.getAssets().open(tipsArray[currentMonth]);
            int streamSize = stream.available();
            byte[] buffer = new byte[streamSize];
            stream.read(buffer);
            stream.close();
            String html = new String(buffer);
            /*
            String div1 = substringBetween(html, "<div id=\"div1\">", "</div>");
            String div2 = substringBetween(html, "<div id=\"div2\">", "</div>");
            String div3 = substringBetween(html, "<div id=\"div3\">", "</div>");
            String div4 = substringBetween(html, "<div id=\"div4\">", "</div>");
            String div5 = substringBetween(html, "<div id=\"div5\">", "</div>");
            String div6 = substringBetween(html, "<div id=\"div6\">", "</div>");
            String div7 = substringBetween(html, "<div id=\"div7\">", "</div>");
            String div8 = substringBetween(html, "<div id=\"div8\">", "</div>");
            String div9 = substringBetween(html, "<div id=\"div9\">", "</div>");
            String div10 = substringBetween(html, "<div id=\"div10\">", "</div>");
            String div11 = substringBetween(html, "<div id=\"div11\">", "</div>");
            String div12 = substringBetween(html, "<div id=\"div12\">", "</div>");
            String div13 = substringBetween(html, "<div id=\"div13\">", "</div>");
            String div14 = substringBetween(html, "<div id=\"div14\">", "</div>");

            String [] divArray = {div1, div2, div3, div4, div5, div6, div7, div8, div9, div10, div11, div12, div13, div14};
            divList = new ArrayList(Arrays.asList(divArray));
            */
            int currentDiv = 1;
            while(html.contains("<div id=\"div" + currentDiv + "\">")){
                String thisDiv = substringBetween(html, "<div id=\"div" + currentDiv + "\">", "</div>");
                divList.add(thisDiv);
                currentDiv++;
            }

            InputStream stream2 = this.getAssets().open("may_tips_url.html");
            int streamSize2 = stream2.available();
            byte[] buffer2 = new byte[streamSize2];
            stream2.read(buffer2);
            stream2.close();
            String url = new String(buffer2);

            int currentUrl = 1;
            while(url.contains("<div id=\"url" + currentUrl + "\">")){
                urlList.add((substringBetween(url, "<div id=\"url" + currentUrl + "\">", "</div>")));
                Log.i(TAG, (substringBetween(url, "<div id=\"url" + currentUrl + "\">", "</div>")));
                currentUrl++;
            }
            /*
            String [] urls = {
                    "",
                    "http://pixabay.com/static/uploads/photo/2014/08/05/17/23/the-carrot-410670_640.jpg",
                    "http://pixabay.com/static/uploads/photo/2013/08/28/18/07/clematis-176818_640.jpg",
                    "",
                    "",
                    "http://pixabay.com/static/uploads/photo/2013/12/06/13/00/water-lily-224195_640.jpg",
                    "http://pixabay.com/static/uploads/photo/2013/09/06/16/00/potatoes-179471_640.jpg",
                    "",
                    "http://pixabay.com/static/uploads/photo/2013/11/28/12/14/grass-220465_640.jpg",
                    "",
                    "http://pixabay.com/static/uploads/photo/2013/10/06/10/45/hare-191365_640.jpg",
                    "http://pixabay.com/static/uploads/photo/2010/12/13/09/58/appetite-2038_640.jpg",
                    "",
                    "http://pixabay.com/static/uploads/photo/2014/04/01/14/19/rose-302531_640.jpg"
            };
            urlList = new ArrayList(Arrays.asList(urls));
            */
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Get correct drawable for the month
        TypedArray images = getResources().obtainTypedArray(R.array.monthArray);
        int resourceId = images.getResourceId(currentMonth, -1);
        // Define the ImageView
        ImageView im = (ImageView) findViewById(R.id.imageView1);
        // Set correct drawable as background in the ImageView
        im.setImageResource(resourceId);

        // Array to store background colors to be shown in the activity
        String [] colors = {"#7c4c8a", "#790b14", "#608f31", "#f6c61a", "#71945c", "#b52c3e", "#89144b", "#a2882d", "#918f80", "#354a12", "#d3d069", "#7f4a18"};
        // Define the layout and set the color to the correct one
        linearLayoutLeft = (LinearLayout) findViewById(R.id.leftLinear);
        linearLayoutRight = (LinearLayout) findViewById(R.id.rightLinear);
        mainItemObjects = new ArrayList<MainItemObject>();

        for(int i = 0; i < divList.size(); i++){
            MainItemObject mainItemObject = new MainItemObject(divList.get(i), Html.fromHtml(urlList.get(i)).toString());
            mainItemObjects.add(mainItemObject);
        }
        buildList();
    }
    private void buildList(){
        ArrayList<MainItemObject> toLeftList = new ArrayList<>();
        ArrayList<MainItemObject> toRightList = new ArrayList<>();
        boolean left = true;
        for(MainItemObject mainItemObject : mainItemObjects){
            if(left){
                toLeftList.add(mainItemObject);
                left = false;
            }else{
                toRightList.add(mainItemObject);
                left = true;
            }
        }
        ArrayAdapter <MainItemObject> adapterLeft = new MainListAdapter(toLeftList);
        ArrayAdapter <MainItemObject> adapterRight = new MainListAdapter(toRightList);

        final int adapterCountLeft = adapterLeft.getCount();
        linearLayoutLeft.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 19, 5, 19);

        for (int i = 0; i < adapterCountLeft; i++) {
            View item = adapterLeft.getView(i, null, null);
            linearLayoutLeft.addView(item, layoutParams);
        }

        final int adapterCountRight = adapterRight.getCount();
        linearLayoutRight.removeAllViews();
        for (int i = 0; i < adapterCountRight; i++) {
            View item = adapterRight.getView(i, null, null);
            linearLayoutRight.addView(item, layoutParams);
        }
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
                startActivity(new Intent(this, NotificationManager.class));
                break;
            case 3:
                startActivity(new Intent(this, PlantSearchActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, HelpActivity.class));
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
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }
    private class MainListAdapter extends ArrayAdapter<MainItemObject> {
        ArrayList<MainItemObject> someMainItemObjects;
        public MainListAdapter(ArrayList<MainItemObject> someMainItemObjects){
            super(MainActivity.this, R.layout.main_item, someMainItemObjects);
            this.someMainItemObjects = someMainItemObjects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View mainItemView = convertView;
            //check if given view is null, if so inflate a new one
            if(mainItemView == null){
                mainItemView = getLayoutInflater().inflate(R.layout.main_item, parent, false);
            }

            Context context = getApplicationContext();
            MainItemObject mainItemObject = someMainItemObjects.get(position);

            ImageView imageView1 = (ImageView) mainItemView.findViewById(R.id.imageView1);
            String url = mainItemObject.getImageUrl();
            if(!"null".equals(url)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    try {
                        Picasso.with(context).load(url).into(imageView1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "Connected but failed anyway");
                    }
                }
            }


            Spanned text = Html.fromHtml(mainItemObject.getHtmlText());
            final String htmlText = mainItemObject.getHtmlText();
            
            //htmlText.
            final TextView textView1 = (TextView) mainItemView.findViewById(R.id.textView1);
            final String preview = substringBetween(htmlText, "<p>", "</p>");
            final TextView textView2 = (TextView) mainItemView.findViewById(R.id.textView2);
            textView1.setText(text);
            textView2.setText("Läs mer");
            textView1.setText(Html.fromHtml(preview));

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textView2.getText().equals("Läs mer")){
                        textView1.setText(Html.fromHtml(htmlText));
                        textView2.setText("Visa mindre");

                    }else if(textView2.getText().equals("Visa mindre")){
                        textView1.setText(Html.fromHtml(preview));
                        textView2.setText("Läs mer");
                    }
                }
            });
            mainItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textView2.getText().equals("Läs mer")){
                        textView1.setText(Html.fromHtml(htmlText));
                        textView2.setText("Visa mindre");

                    }else if(textView2.getText().equals("Visa mindre")){
                        textView1.setText(Html.fromHtml(preview));
                        textView2.setText("Läs mer");
                    }
                }
            });
            return mainItemView;
        }
    }
}
