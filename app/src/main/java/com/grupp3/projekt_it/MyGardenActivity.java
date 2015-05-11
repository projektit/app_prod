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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyGardenActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    String gardenName;
    String gardenLocation;
    ListView listView1;
    Context context;
    ArrayList<Plant_DB> allPlants;
    ArrayList<Integer> selectedPlants;
    Menu menu;
    Boolean onModify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        gardenName = intent.getStringExtra("gardenName");
        gardenLocation = intent.getStringExtra("gardenLocation");
        context = getApplicationContext();
        listView1 = (ListView)findViewById(R.id.listView2);
        selectedPlants = new ArrayList<>();
        onModify = false;
        addGardenInfo();
        buildListView();
    }
    // method to populate listview from garden database
    public void buildListView() {
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
        MenuItem newItem = menu.findItem(R.id.action_new);
        newItem.setVisible(false);
        MenuItem discardItem = menu.findItem(R.id.action_discard);
        discardItem.setVisible(true);

        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());

        SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(getApplicationContext());

        allPlants = sqlPlantHelper.getAllPlants(garden.getTableName());
        Collections.sort(allPlants, new Comparator<Plant_DB>() {
            public int compare(Plant_DB p1, Plant_DB p2) {
                return p1.get_swe_name().compareToIgnoreCase(p2.get_swe_name());
            }
        });

        ArrayAdapter<Plant_DB> adapter = new DeletePlantListAdapter();
        listView1.setAdapter(adapter);

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
                onModify = false;
                buildListView();
            }
        });
        listView1.setOnItemLongClickListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_garden, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(context);
        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, context);
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
            onModify = true;
            Toast.makeText(MyGardenActivity.this, "Tryck för att välja växt", Toast.LENGTH_LONG).show();
            // Change view to be able to delete a garden
            deletePlantView();
            return true;
        }
        if(id == R.id.action_discard){
            if(selectedPlants == null){
                return true;
            }
            for(int plantId : selectedPlants){
                sqlPlantHelper.deletePlant_ID(plantId, garden.getTableName());
            }
            selectedPlants.clear();
            MenuItem discardItem = menu.findItem(R.id.action_discard);
            discardItem.setVisible(false);
            MenuItem newItem = menu.findItem(R.id.action_new);
            newItem.setVisible(true);
            buildListView();
            onModify = false;
            return true;
        }

        if(id == R.id.add_notification){
            Intent intent = new Intent(this, NewNotificationActivity.class);
            startActivityForResult(intent, 1);
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
        if(onModify == true){
            onModify = false;
            MenuItem discardItem = menu.findItem(R.id.action_discard);
            discardItem.setVisible(false);
            MenuItem newItem = menu.findItem(R.id.action_new);
            newItem.setVisible(true);
            buildListView();
            return;
        }else {
            super.onBackPressed();
        }
    }
    public void addGardenInfo() {
        GardenUtil gardenUtil = new GardenUtil();
        Garden garden = gardenUtil.loadGarden(gardenName, getApplicationContext());
        //fill view
        int weatherCode = -1;
        if(garden.getForecast() != null){
            Weather[] weathers = garden.getForecast().getWeather();
            if (weathers[0].getIcon().equals("01d")) {
                weatherCode = R.drawable.d01d;
            } else if (weathers[0].getIcon().equals("02d")) {
                weatherCode = R.drawable.d02d;
            } else if (weathers[0].getIcon().equals("03d")) {
                weatherCode = R.drawable.d03d;
            } else if (weathers[0].getIcon().equals("04d")) {
                weatherCode = R.drawable.d04d;
            } else if (weathers[0].getIcon().equals("09d")) {
                weatherCode = R.drawable.d09d;
            } else if (weathers[0].getIcon().equals("10d")) {
                weatherCode = R.drawable.d10d;
            } else if (weathers[0].getIcon().equals("11d")) {
                weatherCode = R.drawable.d11d;
            } else if (weathers[0].getIcon().equals("13d")) {
                weatherCode = R.drawable.d13d;
            }else if (weathers[0].getIcon().equals("50d")) {
                weatherCode = R.drawable.d50d;
            }
            if (weathers[0].getIcon().equals("01n")) {
                weatherCode = R.drawable.n01n;
            } else if (weathers[0].getIcon().equals("02n")) {
                weatherCode = R.drawable.n02n;
            } else if (weathers[0].getIcon().equals("03n")) {
                weatherCode = R.drawable.n03n;
            } else if (weathers[0].getIcon().equals("04n")) {
                weatherCode = R.drawable.n04n;
            } else if (weathers[0].getIcon().equals("09n")) {
                weatherCode = R.drawable.n09n;
            } else if (weathers[0].getIcon().equals("10n")) {
                weatherCode = R.drawable.n10n;
            } else if (weathers[0].getIcon().equals("11n")) {
                weatherCode = R.drawable.n11n;
            } else if (weathers[0].getIcon().equals("13n")) {
                weatherCode = R.drawable.n13n;
            }else if (weathers[0].getIcon().equals("50d")) {
                weatherCode = R.drawable.n50n;
            }
        }
        TextView gardenNameTV = (TextView)findViewById(R.id.my_garden_name);
        gardenNameTV.setText(gardenName);
        String gardenCity = garden.getLocation().substring(0, garden.getLocation().length() - 4);
        TextView gardenLocationTV = (TextView) findViewById(R.id.my_garden_location);
        gardenLocationTV.setText(gardenCity);
        int temp = (int) Math.round(garden.getForecast().getMain().getTemp());
        TextView gardenTemp = (TextView) findViewById(R.id.my_garden_temp);
        gardenTemp.setText(Double.toString(temp).substring(0, Double.toString(temp).length()-2) + "\u00b0");
        ImageView weatherPicture = (ImageView) findViewById(R.id.my_garden_weather);
        weatherPicture.setImageResource(weatherCode);
    }
    private class PlantListAdapter extends ArrayAdapter <Plant_DB> {
        public PlantListAdapter() {
            super(context, R.layout.plant_list_item, allPlants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View plantItemView = convertView;
            //check if given view is null, if so inflate a new one
            if (plantItemView == null) {
                plantItemView = getLayoutInflater().inflate(R.layout.plant_list_item, parent, false);
            }
            //find garden to work with
            Plant_DB plant = allPlants.get(position);

            //fill view
            ImageView imageView1 = (ImageView) plantItemView.findViewById(R.id.imageView1);

            TextView textView1 = (TextView) plantItemView.findViewById(R.id.textView1);
            textView1.setText(plant.get_swe_name());
            TextView textView2 = (TextView) plantItemView.findViewById(R.id.plantResultLatinName);
            textView2.setText(plant.get_latin_name());

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    /*Picasso.with(context)
                            .load(plant.get_img_url())
                            .resize(50, 50)
                            .centerCrop()
                            .into(imageView1);*/
                    Picasso.with(context).load(plant.get_img_url()).into(imageView1);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Connected but failed anyway");
                }
            }
            return plantItemView;
        }
    }
    private class DeletePlantListAdapter extends ArrayAdapter <Plant_DB> {
        public DeletePlantListAdapter() {
            super(context, R.layout.plant_list_item_delete, allPlants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View plantItemView = convertView;
            //check if given view is null, if so inflate a new one
            if (plantItemView == null) {
                plantItemView = getLayoutInflater().inflate(R.layout.plant_list_item_delete, parent, false);
            }
            //find garden to work with
            Plant_DB plant = allPlants.get(position);

            //fill view
            ImageView imageView1 = (ImageView) plantItemView.findViewById(R.id.imageView1);

            TextView textView1 = (TextView) plantItemView.findViewById(R.id.textView1);
            textView1.setText(plant.get_swe_name());

            final int plant_id = plant.get_id();
            CheckBox checkBox1 = (CheckBox) plantItemView.findViewById(R.id.checkBox1);
            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        boolean inList = false;
                        for(int id : selectedPlants){
                            if(id == plant_id) {
                                inList = true;
                                break;
                            }
                        }
                        if(!inList){
                            selectedPlants.add(plant_id);
                        }
                    }else if(!isChecked){
                        boolean inList = false;
                        for(int id : selectedPlants){
                            if(id == plant_id) {
                                inList = true;
                                break;
                            }
                        }
                        if(inList){
                            selectedPlants.remove(new Integer(plant_id));
                        }
                    }
                }
            });

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    /*Picasso.with(context)
                            .load(plant.get_img_url())
                            .resize(50, 50)
                            .centerCrop()
                            .into(imageView1);*/
                    Picasso.with(context).load(plant.get_img_url()).into(imageView1);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Connected but failed anyway");
                }
            }
            return plantItemView;
        }
    }
}
