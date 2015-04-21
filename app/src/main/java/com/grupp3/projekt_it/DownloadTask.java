package com.grupp3.projekt_it;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;

/**
 * Created by Marcus on 2015-04-21.
 */
public class DownloadTask extends AsyncTask<String, Void, String> {
    String TAG = "com.grupp3.projekt_it";
    String fileName;
    Context context;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    @Override
    protected String doInBackground(String... urls) {
        String result = "";


        InputStream isr = null;

        try{

            URL url = new URL
                    ("http://130.229.154.152:8080/flowersDBProject/get_all_flowers.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String temp = "";
            while((temp = reader.readLine()) != null){
                json.append(temp).append("\n");

            }

            result = json.toString();
            reader.close();

        }  catch(Exception e){

            Log.e("log_tag", "Error in http connection " + e.toString());


            textView1.setText("Couldnt connect to database");

        }

        //parse json data

        try {

            String s = "";

            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length();i++){

                JSONObject json = jArray.getJSONObject(i);

                s = s +"Test : "+json.getInt("pid")
                        +" "+json.getString("latinname")
                        +" "+json.getString("name")
                        +" "+json.getString("category")
                        +" "+json.getString("soil")
                        +" "+json.getInt("zon");  }

            textView1.setText(s);

        } catch (Exception e) {

            // TODO: handle exception

            Log.e("log_tag", "Error Parsing Data "+e.toString());

        }
        return result;
    }

    protected void onPostExecute(String result) {
        textView1.setText(result);
    }

}
