package com.grupp3.projekt_it;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.*;
import java.util.ArrayList;

/**
 * Created by Daniel on 2015-04-23.
 */

//Fragment which allows users to pick a garden in which the selected flower will be saved
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
