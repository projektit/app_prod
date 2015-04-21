package com.grupp3.projekt_it;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;


public class MyGardenActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    TextView textView1;
    TextView textView2;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);
        //enable strict mode to be able to use HTTP request
        StrictMode.enableDefaults();
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


        //test download task form server
        /*DownloadTask dlTask = new DownloadTask();
        dlTask.execute();*/

        //call method for printing out context from MySQL database(on local)

        getFlowerData();
    }

    public void getFlowerData(){
        //string to save result in
        String result = "";
        InputStream isr = null;
        try{
            //create new Http client
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://130.229.154.152:8080/flowersDBProject/db_demo.php");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e){
            Log.e("Log_tag", "Error in http connection "+e.toString());

        }
        //convert response to string
        try{
            //create new Http
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            isr.close();
            result=sb.toString();
        }
        catch(Exception e){
            Log.e("Log_tag", "Error in converting result "+e.toString());

        }
        //parse json date
        try{
            String s = "";
            JSONArray jsonArray = new JSONArray(result);

            for(int i=0; i< jsonArray.length(); i++){
                JSONObject json = jsonArray.getJSONObject(i);
                s = s +
                        "latinname : "+json.getString("latinname")+" "+"\n"+
                        "name: "+json.getString("name")+"\n"+
                        "category : "+json.getString("category") +"\n"+
                        "soil : "+json.getString("soil") +"\n"+
                        "zon : "+json.getInt("zon")+"\n\n";


            }

            textView1.setText(s);
        }
        catch(Exception e){
            Log.e("Log_tag", "Error parsing data "+e.toString());

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
