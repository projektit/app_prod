package com.grupp3.projekt_it;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

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
        setContentView(R.layout.activity_my_flower);
        //create new Intent to handle putExtra input
        Intent intent = getIntent();
        //recives jsonPlant object
        //that we send from MyGardenActivity
        jsonPlant = intent.getStringExtra("jsonPlant");
        //call show flower method
        showFlowerInfo();
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
        new DownloadImage(imageView1).execute(plant.getImg_url());
        //Printouts for name column
        TextView textView =(TextView) findViewById(R.id.textView1);
        textView.setText("Namn : " +plant.getName());

        //Printouts for swe_name column
        TextView textView2 =(TextView) findViewById(R.id.textView2);
        textView2.setText("Svenskt Namn: " +plant.getSwe_name());

        //Printouts for latin name column
        TextView textView3 =(TextView) findViewById(R.id.textView3);
        textView3.setText("Latinskt Namn: " +plant.getLatin_name());

        //Printouts for type column
        TextView textView4 =(TextView) findViewById(R.id.textView4);
        textView4.setText("Kategori: " +plant.getType());

        //Printouts for soil column
        TextView textView5 =(TextView) findViewById(R.id.textView5);
        textView5.setText("Jord: " +plant.getSoil());

        //Printouts for zone_min column
        TextView textView6 =(TextView) findViewById(R.id.textView6);
        textView6.setText("Minsta Zonen: " +plant.getZone_min());

        //Printouts for zone max column
        TextView textView7 =(TextView) findViewById(R.id.textView7);
        textView7.setText("Största Zonen: " +plant.getZone_max());

        //Printouts for water column
        TextView textView8 =(TextView) findViewById(R.id.textView8);
        textView8.setText("Bevattning: " +plant.getWater());

        //Printouts for misc column
        TextView textView9 =(TextView) findViewById(R.id.textView9);
        textView9.setText("Allmänt : " +plant.getMisc());

        //Printouts for sun column
        TextView textView10 =(TextView) findViewById(R.id.textView10);
        textView10.setText("Sol : " +plant.getSun());

        //Printouts for _id column
        TextView textView0 =(TextView) findViewById(R.id.textView0);
        textView0.setText("Id : " +plant.getId());

        TextView textView12 =(TextView) findViewById(R.id.textView12);
        textView12.setText(plant.getName());

    }
}
