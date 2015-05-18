package com.grupp3.projekt_it;

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
public class PlantSearchFragment extends DialogFragment {
    String TAG = "com.grupp3.projekt_it";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        String[] options = {"Lägg till i trädgård", "Visa"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.garden_list_menu_box_message)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String jsonPlant = "";
                        Bundle bundle = getArguments();
                        if (bundle != null) {
                            jsonPlant = bundle.getString("jsonPlant");
                        }
                        if(which == 0){
                            FragmentManager fragmentManager = getFragmentManager();
                            ChooseGardenFragment chooseGardenFragment = new ChooseGardenFragment();
                            chooseGardenFragment.setArguments(bundle);
                            chooseGardenFragment.show(fragmentManager, "disIsTag4");
                        }
                        if(which == 1){
                            Intent intent = new Intent(context, MyFlowerWebActivity.class);
                            intent.putExtra("jsonPlant", jsonPlant);
                            getActivity().startActivity(intent);
                        }
                    }
                });

        return builder.create();
    }
}
