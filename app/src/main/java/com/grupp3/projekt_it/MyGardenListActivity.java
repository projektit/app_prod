package com.grupp3.projekt_it;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class MyGardenListActivity extends BaseActivity {
   String TAG = "com.grupp3.projekt_it";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_garden_list);
        /**
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_my_garden_list, frameLayout);

        /**
         * Setting title and itemChecked
         */
        mDrawerList.setItemChecked(position, true);
        //setTitle(listArray[position]);
        //((ImageView)findViewById(R.id.image_view)).setBackgroundResource(R.drawable.image1);
        buildListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_garden_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Change name setting in overflow menu is pressed
        if (id == R.id.action_settings) {
            Toast.makeText(MyGardenListActivity.this, "Tryck för att döpa om trädgård", Toast.LENGTH_LONG).show();
            changeNameView();
            return true;
        }
        //Delete garden in overflow menu is pressed
        if(id == R.id.remove_garden){
            Toast.makeText(MyGardenListActivity.this, "Tryck för att välja trädgård", Toast.LENGTH_LONG).show();
            deleteGardenView();
            return true;
        }
        //Add a new garden option in action menu is pressed
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, NewGardenActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == NewGardenActivity.RESULT_OK )
        {
            if(requestCode == 1) {
                String[] result = data.getStringArrayExtra("Result");
                Context context = getApplicationContext();
                Garden garden = new Garden(result[0], result[1]);
                String [] files = getApplicationContext().fileList();
                ArrayList<String> files2 = new ArrayList<String>(Arrays.asList(files));
                if(files2.contains(result[0] + ".grdn")){
                    Toast toast = Toast.makeText(context, "Trädgård finns redan", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }


                GardenUtil gardenUtil = new GardenUtil();
                int version = gardenUtil.getDBversion(context);
                SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(getApplicationContext());
                sqlPlantHelper.createNewTable(result[0]);

                gardenUtil.setDBversion(context, version + 1);

                //Plant_DB plant_db = new Plant_DB();
                //sqlPlantHelper.addPlant(plant_db, result[0]);
                //Plant_DB plant_db2 = sqlPlantHelper.getPlant(plant_db.get_id(), result[0]);
                //Plant_DB plant_db3 = sqlPlantHelper.getPlant(plant_db2.get_id(), result[0]);

                //Log.i(TAG, plant_db.get_swe_name() + " " +  plant_db2.get_swe_name() + " " + plant_db3.get_swe_name());

                gardenUtil.saveGarden(garden, context);
                buildListView();
                OnBootReceiver.setAlarms(getApplicationContext());
            }
        }
    }
    public void buildListView() {

        String [] files = getApplicationContext().fileList();
        ArrayList <String> desiredFiles = new ArrayList<>();
        for(int i = 0; i < files.length; i ++){
            if (files[i].endsWith(".grdn")){
                desiredFiles.add(files[i].substring(0, files[i].length()-5));
            }
        }
        String [] items = desiredFiles.toArray(new String[desiredFiles.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);

        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);

        //Listen for normal click on items in list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MyGardenActivity.class);
                intent.putExtra("gardenName", gardenName);
                startActivity(intent);
            }
        });

        //Listener for long click on items in list
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();

                FragmentManager fragmentManager = getFragmentManager();
                GardenListFragment quickOptions = new GardenListFragment();

                Bundle bundle = new Bundle();
                bundle.putString("gardenName", gardenName);

                quickOptions.setArguments(bundle);
                quickOptions.show(fragmentManager, "disIsTag");
                return true;
            }
        });
    }

    // When the delete garden settings option is chosen this view is shown and clicking on items
    // in the list will remove them
    public void deleteGardenView() {
        String[] files = getApplicationContext().fileList();
        ArrayList<String> desiredFiles = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".grdn")) {
                desiredFiles.add(files[i].substring(0, files[i].length() - 5));
            }
        }
        String[] items = desiredFiles.toArray(new String[desiredFiles.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);

        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);

        //Listen for normal click on items in list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();

                getApplicationContext().deleteFile(gardenName + ".grdn");

                buildListView();
            }
        });
    }
    // When the change name settings option is chosen this view is shown and clicking on an item in
    // the list will allow the user to change the name of the garden
    public void changeNameView() {
        String[] files = getApplicationContext().fileList();
        ArrayList<String> desiredFiles = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".grdn")) {
                desiredFiles.add(files[i].substring(0, files[i].length() - 5));
            }
        }
        String[] items = desiredFiles.toArray(new String[desiredFiles.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);

        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);

        //Listen for normal click on items in list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();
                FragmentManager fragmentManager = getFragmentManager();
                ChangeGardenNameFragment changName = new ChangeGardenNameFragment();

                Bundle bundle = new Bundle();
                bundle.putString("gardenName", gardenName);
                changName.setArguments(bundle);
                changName.show(fragmentManager, "disIsTag2");

                buildListView();
            }
        });
    }

    @Override
    protected void openActivity(int position) {

        /**
         * All activities with navigation drawer must contain this override function in order
         * to not be able to launch another instance of itself from navigation bar.
         * Remove correct startActivity(..) call to do so.
         */

//		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:
                break;
            case 2:
                startActivity(new Intent(this, PlantSearchActivity.class));
                break;
            case 3:
                break;
            case 4:
                startActivity(new Intent(this, Login.class));
                break;
            case 5:
                startActivity(new Intent(this, Preferences.class));
            default:
                break;
        }
    }
}
