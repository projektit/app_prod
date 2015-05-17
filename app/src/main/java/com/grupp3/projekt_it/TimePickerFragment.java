package com.grupp3.projekt_it;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

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