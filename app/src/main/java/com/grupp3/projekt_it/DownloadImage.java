package com.grupp3.projekt_it;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Daniel on 2015-05-04.
 */
public class DownloadImage extends AsyncTask <String, Void, byte[]>{
    String TAG = "com.grupp3.projekt_it";
    ImageView imageView;
    public DownloadImage(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected byte[] doInBackground(String... urls) {
        // do above Server call here
        try{
            //convert URL to URL object
            URL imageUrl = new URL(urls[0]);
            //open connection
            //Log.i(TAG, "Get URL" + urls[0]);
            URLConnection ucon = imageUrl.openConnection();
            //get input stream from URL
            //Log.i(TAG, "Open connection" + ucon.getContent());
            InputStream is = ucon.getInputStream();
            //Log.i(TAG, "is" + is.toString());
            //create buffer input stream
            BufferedInputStream bis = new BufferedInputStream(is);
            //Log.i(TAG, "bis" + bis.toString());
            //create buffer array
            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            //Log.i(TAG, "baf" + baf.toString());
            //initiate current
            int current = 0;
            //set current to current input stream
            while((current = bis.read()) != -1){
                //build the array with those values
                //type cast to byte
                baf.append((byte)current);
            }

            String result = baf.toByteArray().toString();
            if(result.equals("None")){
                Log.i(TAG, "equals None");
            }
            //return the image as a byteArray
            return baf.toByteArray();

        }catch(Exception e){
            Log.d(TAG, "Download image error: " + e.toString());
        }
        return null;
    }

    @Override
    //to do on main thread again
    protected void onPostExecute(byte[] result) {
        if(result == null){
            Log.i(TAG,"ImageDownloader; image is null" );
            imageView.setImageResource(R.drawable.garden_lits_item_picture);
            return;
        }
        //process message
        //convert back to Image
        Bitmap image = ImageUtilities.getImage(result);
        //print out on ImageView
        imageView.setImageBitmap(image);
    }
}
