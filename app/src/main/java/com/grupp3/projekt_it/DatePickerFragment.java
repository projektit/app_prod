package com.grupp3.projekt_it;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/*
 * This fragment displays a date picker so that the user of the application can pick a specific
 * time for the created notification to be triggered.
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

public class DatePickerFragment extends DialogFragment {

  DatePickerDialog.OnDateSetListener ondateSet;
   public DatePickerFragment(){}
    public void setCallBack (DatePickerDialog.OnDateSetListener ondate){

        ondateSet = ondate;
    }

    private int year,month,day;

    @Override
    public void setArguments (Bundle args){
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");

    }
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState){
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }
}