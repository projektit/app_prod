package com.grupp3.projekt_it;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyGardenActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    String gardenName;
    public Boolean onDel;
    ListView listView1;
    Context context;
    ArrayList<Plant_DB> allPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        gardenName = intent.getStringExtra("gardenName");
        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());
        onDel = false;
        context = getApplicationContext();
        listView1 = (ListView)findViewById(R.id.listView1);
        buildListView();
    }
    // method to populate listview from garden database
    public void buildListView() {
        getSupportActionBar().setTitle(gardenName);
        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());

        SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(getApplicationContext());

        allPlants = sqlPlantHelper.getAllPlants(garden.getTableName());
        Collections.sort(allPlants, new Comparator<Plant_DB>() {
            public int compare(Plant_DB p1, Plant_DB p2) {
                return p1.get_swe_name().compareToIgnoreCase(p2.get_swe_name());
            }
        });

        ArrayAdapter<Plant_DB> adapter = new PlantListAdapter();
        listView1.setAdapter(adapter);


        //Methods for starting MyFlowerActivity after click on list items
        //started from flowers in users MyGarden

        //Listen for normal click on items in list
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new PLANT_DB locally
                //get all plants by position, depends on which flower user push on
                Plant_DB plant_db = allPlants.get(position);
                //create new Gson object
                Gson gson = new Gson();
                //create jsonPlant string of DB
                String jsonPlant = gson.toJson(plant_db);
                //create new intent for starting MyFlowerActivity.class
                Intent intent = new Intent(getApplicationContext(), MyFlowerActivity.class);
                //send flower info to MyFlowerActivity
                Log.i(TAG, "nuuuu");
                intent.putExtra("jsonPlant", jsonPlant);
                //start Activity
                startActivity(intent);
            }
        });
        //Listener for long click on items in list

       //Listener for long click on items in list
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Plant_DB plant_db = allPlants.get(position);
                Gson gson = new Gson();
                String jsonPlant = gson.toJson(plant_db);

                FragmentManager fragmentManager = getFragmentManager();
                GardenFragment quickOptions = new GardenFragment();

                Bundle bundle = new Bundle();
                String [] info = {jsonPlant, gardenName};
                bundle.putStringArray("info", info);

                quickOptions.setArguments(bundle);
                quickOptions.show(fragmentManager, "disIsTag");
                return true;
            }

        });
    }
    // method to populate listview from garden database
    public void deletePlantView() {
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plant_DB plant_db = allPlants.get(position);
                Gson gson = new Gson();
                String jsonPlant = gson.toJson(plant_db);

                GardenUtil gardenUtil = new GardenUtil();
                Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());

                SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(getApplicationContext());
                Log.i(TAG, "frag " + Integer.toString(plant_db.get_id()));
                sqlPlantHelper.deletePlant_ID(plant_db.get_id(), garden.getTableName());
                onDel = false;
                buildListView();
            }
        });
        listView1.setOnItemLongClickListener(null);
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
        if (id == R.id.action_new) {
            /*
            Intent intent = new Intent(getApplicationContext(), PlantSearchActivity.class);
            startActivity(intent);
            */

            Intent intent = new Intent(getApplicationContext(), PlantSearchActivity.class);
            intent.putExtra("gardenName", gardenName);
            startActivityForResult(intent, 1);

        }
        if(id == R.id.remove_plant){
            onDel = true;
            Log.i(TAG, "HEJ0");
            Toast.makeText(MyGardenActivity.this, "Tryck för att välja växt", Toast.LENGTH_LONG).show();
            // Change view to be able to delete a garden
            deletePlantView();
            Log.i(TAG, "HEJ1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        buildListView();
    }

    // Override the back button to change the view back to the previous if it is set as one of the two
    // settings views, if not go back to previous activity
    @Override
    public void onBackPressed() {
        if(onDel == true){
            onDel = false;
            buildListView();
            return;
        }else {
            super.onBackPressed();
        }
    }
    private class PlantListAdapter extends ArrayAdapter <Plant_DB> {
        public PlantListAdapter() {
            super(context, R.layout.plant_list_item, allPlants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View gardenItemView = convertView;
            //check if given view is null, if so inflate a new one
            if (gardenItemView == null) {
                gardenItemView = getLayoutInflater().inflate(R.layout.plant_list_item, parent, false);
            }
            //find garden to work with
            Plant_DB plant = allPlants.get(position);

            //fill view
            ImageView imageView1 = (ImageView) gardenItemView.findViewById(R.id.imageView1);

            TextView textView1 = (TextView) gardenItemView.findViewById(R.id.textView1);
            textView1.setText(plant.get_swe_name());

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    new DownloadImage(imageView1).execute(plant.get_img_url());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Connected but failed anyway");
                }
            }
            return gardenItemView;
        }
    }
}
