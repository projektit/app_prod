package com.grupp3.projekt_it;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/*************************************************************************************************
 * Author: Marcus Elwin
 * Projekt IT, 2015-04-27
 * Version: 1.0
 * This Activity is to be used together with MyGardenActivity
 * The user have n number of flowers, which they can click on by onItemClick method.
 * When a flower has been selected, a new Activity , MyFlowerActivity is opened
 * In this Activity the flower data in is displayed with the help of textView class on the
 * user screen after the columns, name, swe_name, type, zone max/min, soil, watering, sun,
 * misc, latinname
 *
 * This is a similar Activity but connected to the web server instead of the local
 * database, to display more info about the flowers from a search query
 *************************************************************************************************/


public class MyFlowerWebActivity extends ActionBarActivity {
    String jsonPlant;
    String TAG = "com.grupp3.projekt_it";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flower_web);
        //create new Intent to handle putExtra input
        Intent intent = getIntent();
        //receives jsonPlant object
        //that we send from MyGardenActivity
        jsonPlant = intent.getStringExtra("jsonPlant");
        //call show flower method
        showFlowerInfo();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_flower, menu);
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


        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //method to display flower info
    public void showFlowerInfo(){

        //get info from jsonPLant
        Gson gson = new Gson();
        //convert PLANT again
        //by making instant of PLANT_DB and covert Gson object to
        //handle this type
        Plant plant = gson.fromJson(jsonPlant, Plant.class);
        //create textView for print outs

        //Image URL
        //convert to bitArray
        /*byte[] flowerImage = new getFlowerImage.execute("http://www.alltomtradgard.se/ImageGallery/Thumbnails/63/135763/107909_191262.jpg");*/
        //new getFlowerImage().execute("http://www.alltomtradgard.se/ImageGallery/Thumbnails/63/135763/107909_191262.jpg");
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);

        //new DownloadImage(imageView1).execute(plant.getImg_url());
        /* Picasso is a 3rd party library that takes care of image loading in android applications
                 * With the library re-use of adapters is automatically detected and previous downloads are
                  * canceled. The with method is used for the global default instance with defaut values:
                   *LRU memory cache of 15% the available application RAM
                   * Disk cache of 2% storage space up to 50MB but no less than 5MB. (Note: this is only available on API 14+ or if you are using a standalone library that provides a disk cache on all API levels like OkHttp)
                   *  Three download threads for disk and network access.
                   load is where you specify the URL/file that the picture should be read from
                   into method specifies which imageView that should be used*/
        Picasso.with(this).load(plant.getImg_url()).into(imageView1);
        //Printouts for name column
        TextView textView =(TextView) findViewById(R.id.textView1);
        textView.setText("Namn : " +plant.getName());

        new DownloadImage(imageView1).execute(plant.getImg_url());

        // Print name of the flower
        TextView textView12 =(TextView) findViewById(R.id.flower_name);
        textView12.setText(plant.getSwe_name());

        //Printouts for latin name column
        TextView textView3 =(TextView) findViewById(R.id.latin_name);
        textView3.setText(Html.fromHtml(getString(R.string.plant_latin_name)) + plant.getLatin_name());

        //Printouts for type column
        TextView textView4 =(TextView) findViewById(R.id.category);
        textView4.setText(Html.fromHtml(getString(R.string.plant_category)) + plant.getType());

        //Printouts for soil column
        TextView textView5 =(TextView) findViewById(R.id.soil);
        textView5.setText(Html.fromHtml(getString(R.string.plant_soil)) + plant.getSoil());

        //Printouts for zone_min column
        TextView textView6 =(TextView) findViewById(R.id.min_zone);
        textView6.setText(Html.fromHtml(getString(R.string.plant_min)) + plant.getZone_min());

        //Printouts for zone max column
        TextView textView7 =(TextView) findViewById(R.id.max_zone);
        textView7.setText(Html.fromHtml(getString(R.string.plant_max)) + plant.getZone_max());

        //Printouts for water column
        TextView textView8 =(TextView) findViewById(R.id.watering);
        textView8.setText(Html.fromHtml(getString(R.string.plant_water)) + plant.getWater());

        //Printouts for sun column
        TextView textView10 =(TextView) findViewById(R.id.sun);
        textView10.setText(Html.fromHtml(getString(R.string.plant_sun)) + plant.getSun());

        //Printouts for misc column
        TextView textView9 =(TextView) findViewById(R.id.misc);
        textView9.setText(Html.fromHtml(plant.getMisc()));
    }
}
