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
/*
 *
 * @author Marcus Elwin
 * @author Daniel Freberg
 * @author Esra Kahraman
 * @author Oscar Melin
 * @author Mikael Mölder
 * @author Erik Nordell
 * @author Felicia Schnell
 *
*/
/**
 * Created by Daniel on 2015-04-21.
 * Class defining the pick garden dialog fragment and the logic behind its clickable list items
 * onCreateDialog takes a bundle from previous activity which contains the current gardens name
 * Clicking on item "Öppna trädgård" starts a new activity, "MyGardenActivity"
 * Clicking on item "Byt namn" results in another fragment, "ChangeGardenNameFragment"
 * Clicking on item "Ta bort" results in the deletion of the loaclly saved garden and its corresponding table in SQLite database
 * Clicking on item "Lägg till växt" starts  a new activity "PlantSearchActivity"
 */
public class GardenListFragment extends DialogFragment {
    String TAG = "com.grupp3.projekt_it";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        String[] options = {"Öppna trädgård", "Byt namn", "Ta bort", "Lägg till växt"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.garden_list_menu_box_message)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String gardenName = "";
                        Bundle bundle = getArguments();
                        if (bundle != null) {
                            gardenName = bundle.getString("gardenName");
                        }
                        if(which == 0){
                            Intent intent = new Intent(context, MyGardenActivity.class);
                            intent.putExtra("gardenName", gardenName);
                            startActivity(intent);
                        }
                        if(which == 1){
                            FragmentManager fragmentManager = getFragmentManager();
                            ChangeGardenNameFragment changName = new ChangeGardenNameFragment();

                            changName.setArguments(bundle);
                            changName.show(fragmentManager, "disIsTag2");
                        }
                        if(which == 2){
                            GardenUtil gardenUtil = new GardenUtil();
                            gardenUtil.deleteGarden(gardenName, context);
                            Activity activity = getActivity();
                            if (activity instanceof MyGardenListActivity) {
                                ((MyGardenListActivity) activity).buildListView();
                            }
                        }
                        if(which == 3){
                            Intent intent = new Intent(context, PlantSearchActivity.class);
                            startActivity(intent);
                        }
                    }


                });

        return builder.create();
    }
}
