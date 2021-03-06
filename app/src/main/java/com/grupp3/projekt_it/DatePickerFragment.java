package com.grupp3.projekt_it;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Felicia on 2015-05-05.
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