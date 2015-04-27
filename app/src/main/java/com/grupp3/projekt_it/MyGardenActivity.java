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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class MyGardenActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    String gardenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);
        Intent intent = getIntent();
        gardenName = intent.getStringExtra("gardenName");
        getSupportActionBar().setTitle(gardenName);
        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());
        buildListView();


    }
    // method to populate listview from garden database
    public void buildListView() {
        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());

        SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(getApplicationContext());

        final ArrayList <Plant_DB> allPlants = sqlPlantHelper.getAllPlants(garden.getTableName());
        ArrayList <String> plantNames = new ArrayList<>();
        // extract swe name from arraylist
        for(Plant_DB plant : allPlants){
            plantNames.add(plant.get_swe_name());
        }

        String [] items = plantNames.toArray(new String[plantNames.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);

        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);


        //Methods for starting MyFlowerActivity after click on list items
        //started from flowers in users MyGarden

        //Listen for normal click on items in list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
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
                intent.putExtra("jsonPlant", jsonPlant);
                //start Activity
                startActivity(intent);
            }
        });


        //Listener for long click on items in list

       /* //Listener for long click on items in list
>>>>>>> 8e8dff3... Add MyFlowerActivity that start when flower in list is pushed
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        });*/


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
            Intent intent = new Intent(getApplicationContext(), PlantSearchActivity.class);
            intent.putExtra("gardenName", gardenName);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
