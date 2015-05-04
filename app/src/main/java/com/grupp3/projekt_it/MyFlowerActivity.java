        package com.grupp3.projekt_it;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.os.AsyncTask;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.google.gson.Gson;

        import org.apache.http.util.ByteArrayBuffer;

        import java.io.BufferedInputStream;
        import java.io.InputStream;
        import java.net.URL;
        import java.net.URLConnection;

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
         *************************************************************************************************/


        public class MyFlowerActivity extends ActionBarActivity {
            String jsonPlant;
            String TAG = "com.grupp3.projekt_it";

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_my_flower);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //StrictMode.enableDefaults();
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
                MenuItem item = menu.findItem(R.id.action_settings);
                item.setVisible(false);
                return true;
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
                //convert PLANT_DB again
                //by making instant of PLANT_DB and covert Gson object to
                //handle this type
                Plant_DB plant_db = gson.fromJson(jsonPlant, Plant_DB.class);
                //create textView for print outs
                //Image URL
                //convert to bitArray
                /*byte[] flowerImage = new getFlowerImage.execute("http://www.alltomtradgard.se/ImageGallery/Thumbnails/63/135763/107909_191262.jpg");*/
                //new getFlowerImage().execute("http://www.alltomtradgard.se/ImageGallery/Thumbnails/63/135763/107909_191262.jpg");
                new getFlowerImage().execute(plant_db.get_img_url());
                //Log.i(TAG, "byte array: " + flowerImage[0]);
                //Printouts for name column
                TextView textView =(TextView) findViewById(R.id.textView1);
                textView.setText("Namn : " +plant_db.get_name());

                //Printouts for swe_name column
                TextView textView2 =(TextView) findViewById(R.id.textView2);
                textView2.setText("Svenskt Namn: " +plant_db.get_swe_name());

                //Printouts for latin name column
                TextView textView3 =(TextView) findViewById(R.id.textView3);
                textView3.setText("Latinskt Namn: " +plant_db.get_latin_name());

                //Printouts for type column
                TextView textView4 =(TextView) findViewById(R.id.textView4);
                textView4.setText("Kategori: " +plant_db.get_type());

                //Printouts for soil column
                TextView textView5 =(TextView) findViewById(R.id.textView5);
                textView5.setText("Jord: " +plant_db.get_soil());

                //Printouts for zone_min column
                TextView textView6 =(TextView) findViewById(R.id.textView6);
                textView6.setText("Minsta Zonen: " +plant_db.get_zone_min());

                //Printouts for zone max column
                TextView textView7 =(TextView) findViewById(R.id.textView7);
                textView7.setText("Största Zonen: " +plant_db.get_zone_max());

                //Printouts for water column
                TextView textView8 =(TextView) findViewById(R.id.textView8);
                textView8.setText("Bevattning: " +plant_db.get_water());

                //Printouts for misc column
                TextView textView9 =(TextView) findViewById(R.id.textView9);
                textView9.setText("Allmänt : " +plant_db.get_water());

                //Printouts for sun column
                TextView textView10 =(TextView) findViewById(R.id.textView10);
                textView10.setText("Sol : " +plant_db.get_sun());

                TextView textView0 =(TextView) findViewById(R.id.textView0);
                textView0.setText("Id : " +plant_db.get_id());

                TextView textView12 =(TextView) findViewById(R.id.textView12);
                textView12.setText(plant_db.get_name());


            }

            //Create AsyncTask by doing a private nested class to handle
            //That extends AsyncTask
            //network operations on other thread then main
            //Using 2 methods from AsyncTask doInBackground & onPostExecute
            //doInBackground is on another thread & onPostExecute on the main thread
            private class getFlowerImage extends AsyncTask<String, Void, byte[]> {

                @Override
                //method to to in background on other thread
                protected byte[] doInBackground(String... urls) {
                    // do above Server call here
                    try{
                        //convert URL to URL object
                        URL imageUrl = new URL(urls[0]);
                        //open connection
                        Log.i(TAG, "Get URL" + urls[0]);
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

                        //return the image as a byteArray
                        return baf.toByteArray();

                    }catch(Exception e){
                        Log.d("ImageManager", "Error: " + e.toString());
                    }
                    return null;
                }

                @Override
                //to do on main thread again
                protected void onPostExecute(byte[] result) {
                    //process message
                    //convert back to Image
                    Bitmap image = ImageUtilities.getImage(result);
                    //print out on ImageView
                    ImageView view = (ImageView)findViewById(R.id.imageView1);
                    view.setImageBitmap(image);
                }
            }

        }

