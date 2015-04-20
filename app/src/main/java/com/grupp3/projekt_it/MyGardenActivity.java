package com.grupp3.projekt_it;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MyGardenActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    TextView textView1;
    TextView textView2;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");

        String json = "";
        Context context = getApplication();
        FileInputStream fileInputStream;
        try{
            fileInputStream = context.openFileInput(fileName);
            byte[] input = new byte[fileInputStream.available()];
            while(fileInputStream.read(input) != -1){
                json += new String(input);
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert json to java object
        Gson gson = new Gson();
        Garden garden = gson.fromJson(json, Garden.class);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        if(garden.getForecast() != null){
            if(garden.getForecast().getName() != null)
                textView1.setText("Station name: " + garden.getForecast().getName());
            if(garden.getForecast().getWind() != null)
                textView2.setText("Wind speed: " + Double.toString(garden.getForecast().getWind().getSpeed()));
            if(garden.getForecast().getMain() != null) {
                textView3.setText("Current Temperature: " + Double.toString(garden.getForecast().getMain().getTemp()));
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_garden, menu);
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
}
