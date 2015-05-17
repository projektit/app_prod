package com.grupp3.projekt_it;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

public class GardenFragment extends DialogFragment {
    String TAG = "com.grupp3.projekt_it";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        String[] options = {"Visa växtinfo", "Ta bort"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.garden_list_menu_box_message)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String [] info = new String[2];
                        Bundle bundle = getArguments();
                        if (bundle != null) {
                            info = bundle.getStringArray("info");
                        }
                        String jsonPlant = info[0];
                        String gardenName = info[1];
                        if(which == 0){
                            Intent intent = new Intent(context, MyFlowerWebActivity.class);
                            intent.putExtra("jsonPlant", jsonPlant);
                            getActivity().startActivity(intent);
                        }
                        if(which == 1){
                            Gson gson = new Gson();
                            Plant_DB plant_db = gson.fromJson(jsonPlant, Plant_DB.class);
                            GardenUtil gardenUtil = new GardenUtil();
                            Garden garden = gardenUtil.loadGarden(gardenName, context);

                            SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(context);
                            Log.i(TAG, "frag " + Integer.toString(plant_db.get_id()));
                            sqlPlantHelper.deletePlant_ID(plant_db.get_id(), garden.getTableName());

                            Activity activity = getActivity();
                            if (activity instanceof MyGardenActivity) {;
                                ((MyGardenActivity) activity).buildListView();
                            }
                        }
                    }


                });

        return builder.create();
    }
}