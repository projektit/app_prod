package com.grupp3.projekt_it;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.gson.Gson;
import java.lang.*;
import java.util.ArrayList;


/**
 * Created by Daniel on 2015-04-23.
 * Class defining the pick garden dialog fragment and the logic behind its clickable list items, items correspond to gardens saved
 * on the device
 * onCreateDialog takes a bundle from previous activity which contains a json String representation of a flower as received from
 * the web server.
 * Clicking on an item results in the flower being saved into a SQLite database table corresponding to the corresponding garden.
 */
public class ChooseGardenFragment extends DialogFragment {
    String TAG = "com.grupp3.projekt_it";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        //load all gardens
        String [] files = context.fileList();
        ArrayList<String> desiredFiles = new ArrayList<>();

        //get all garden names
        for(int i = 0; i < files.length; i ++){
            if (files[i].endsWith(".grdn")){
                desiredFiles.add(files[i].substring(0, files[i].length()-5));
            }
        }
        //copy garden names to string array
        final String [] items = desiredFiles.toArray(new String[desiredFiles.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.garden_list_menu_box_message4)
                // build list with all gardenNames
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // open garden
                        String gardenName = items[which];
                        String json = "";
                        Bundle bundle = getArguments();
                        if (bundle != null) {
                            json = bundle.getString("jsonPlant");
                        }
                        GardenUtil gardenUtil = new GardenUtil();
                        Garden garden = gardenUtil.loadGarden(gardenName, context);
                        Gson gson = new Gson();
                        Plant plant = gson.fromJson(json, Plant.class);
                        Plant_DB plant_db = new Plant_DB(plant, "", "", "");
                        SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(context);
                        sqlPlantHelper.addPlant(plant_db, garden.getTableName());

                    }
                });
        return builder.create();
    }
}
