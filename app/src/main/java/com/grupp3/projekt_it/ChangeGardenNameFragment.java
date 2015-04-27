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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Daniel on 2015-04-21.
 */
public class ChangeGardenNameFragment extends DialogFragment {
    String TAG = "com.grupp3.projekt_it";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Context context = getActivity().getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText editText = new EditText(getActivity().getApplicationContext());
        //get fileName
        Bundle bundle = getArguments();
        String gardenName1 = "";
        if (bundle != null) {
            gardenName1 = bundle.getString("gardenName");
        }
        final String gardenName = gardenName1;
        //set fileName to textfield
        editText.setText(gardenName);
        builder.setView(editText);
        builder.setMessage(R.string.garden_list_menu_box_message2);
        //set listeners
        builder.setPositiveButton(R.string.garden_list_menu_box_message_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String newGardenName = editText.getText().toString();
                if (gardenName.equals(newGardenName)) {
                    return;
                }
                GardenUtil gardenUtil = new GardenUtil();
                Garden garden = gardenUtil.loadGarden(gardenName, context);
                garden.setName(newGardenName);
                //convert back
                gardenUtil.saveGarden(garden, context);
                gardenUtil.deleteGarden(gardenName, context);
                // call method to rebuild list view, it has to be if to avoid exceptions, will always go through though
                Activity activity = getActivity();
                if (activity instanceof MyGardenListActivity) {
                    ((MyGardenListActivity) activity).buildListView();
                }
            }
        });
        builder.setNegativeButton(R.string.garden_list_menu_box_message_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                return;
            }
        });
        return builder.create();
    }
}




