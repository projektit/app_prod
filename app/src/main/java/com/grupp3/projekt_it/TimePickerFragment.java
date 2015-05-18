package com.grupp3.projekt_it;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

/*
 * This fragment displays a time picker s that the user of the application can pick a specific
 * date for the created notification to be triggered
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
public class TimePickerFragment extends DialogFragment {

    TimePickerDialog.OnTimeSetListener ontimeSet;
    public TimePickerFragment(){}
    public void setCallBack (TimePickerDialog.OnTimeSetListener ontime){

        ontimeSet = ontime;
    }

    private int hour,minute;

    @Override
    public void setArguments (Bundle args){
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");

    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState){
        return new TimePickerDialog(getActivity(),ontimeSet, hour, minute, false);

    }
}