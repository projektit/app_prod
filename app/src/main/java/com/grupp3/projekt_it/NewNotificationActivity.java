package com.grupp3.projekt_it;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Calendar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;


public class NewNotificationActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    EditText editText;
    EditText editText2;
    ImageButton setTime;
    ImageButton setDate;
    Button save;
    TextView textDate;
    TextView textTime;
    Context context;
    int year = -1;
    int month = -1;
    int day = -1;
    int hour = -1;
    int minute = -1;
    String title = null;
    String text = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);
        editText = (EditText) findViewById(R.id.edit_notification_title);
        editText.setBackgroundResource(R.drawable.garden_name_textbox);
        String notificationTitle = editText.getText().toString();
        editText2 = (EditText) findViewById(R.id.edit_notification_text);
        context = getApplicationContext();
        editText2.setBackgroundResource(R.drawable.garden_name_textbox);
        String notificationText = editText2.getText().toString();
        textDate = (TextView) findViewById(R.id.text_date);
        textTime = (TextView) findViewById(R.id.text_time);
        setDate = (ImageButton) findViewById(R.id.datePicker);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        setTime = (ImageButton) findViewById(R.id.timePicker);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        save = (Button) findViewById(R.id.save_notification);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year != -1 && month != -1 && day != -1 && hour != -1 && minute != -1 ){
                    title = editText.getText().toString();
                    text = editText2.getText().toString();
                    if(title != null && !"".equals(title)){
                        if(text == null){
                            text = "";
                        }
                        GardenUtil gardenUtil = new GardenUtil();
                        int id = gardenUtil.getNotificationNumber(context);
                        gardenUtil.setNotificationNumber(context, id+1);
                        UserNotification userNotification = new UserNotification(id, year, month, day, hour, minute, title, text);
                        gardenUtil.saveUserNotification(userNotification, context);
                        OnBootReceiver.setAllUserAlarms(context);
                        Toast.makeText(NewNotificationActivity.this, "En ny händelse har skapats", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    Toast.makeText(NewNotificationActivity.this, "Din händelse saknar titel", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(NewNotificationActivity.this, "Ange tid för händelse", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showDatePicker(){
        DatePickerFragment date = new DatePickerFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
            year = yearOfCentury;
            month = monthOfYear;
            day = dayOfMonth;
            textDate.setText(new StringBuilder().append(padding_str(dayOfMonth))
                            .append("-").append(padding_str(monthOfYear+1)).append("-").append(padding_str(yearOfCentury)).append(" "));
        }
    };

    private void showTimePicker(){
        TimePickerFragment time = new TimePickerFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", calendar.get(Calendar.MINUTE));
        time.setArguments(args);
        time.setCallBack(ontime);
        time.show(getFragmentManager(), "Time Picker");
    }

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
            hour = hourOfDay;
            minute = minuteOfDay;
            textTime.setText(new StringBuilder().append(padding_str(hourOfDay)).append(":").append(padding_str(minute)));
        }
    };

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }

}



