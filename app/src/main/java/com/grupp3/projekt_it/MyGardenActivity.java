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

        //open file with filename
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

        //get  weather from Open Weather Map
        //First check network status:
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        if(networkInfo != null && networkInfo.isConnected()){

            Forecast forecast = null;
            String urlLocation = garden.location.toLowerCase();
            urlLocation = urlLocation.replaceAll("å", "a");
            urlLocation = urlLocation.replaceAll("ä", "a");
            urlLocation = urlLocation.replaceAll("ö", "o");
            try {
                new DownloadData(fileName, context, textView1, textView2, textView3)
                        .execute("http://api.openweathermap.org/data/2.5/weather?q=" + urlLocation);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Connected but failed anyway");
            }

        }else{
            Log.i(TAG, "No connection");
        }

        //View stuff

        //test open dB
        DBHelper myDbhelper = new DBHelper(this);

        myDbhelper.createDataBase();
        myDbhelper.openDatabase();


        textView1.setText(myDbhelper.toString());
        Log.i("marcus", "so far");

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
