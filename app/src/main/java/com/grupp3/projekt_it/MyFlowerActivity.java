    package com.grupp3.projekt_it;

    import android.content.Intent;
    import android.support.v7.app.ActionBarActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.TextView;

    import com.google.gson.Gson;

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
     *************************************************************************************************/


    public class MyFlowerActivity extends ActionBarActivity {
        String jsonPlant;
        String TAG = "com.grupp3.projekt_it";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_flower);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        }

    }

