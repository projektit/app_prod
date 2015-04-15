package com.grupp3.projekt_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class NewGardenActivity extends ActionBarActivity {
    EditText editText1;
    Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_garden);
        editText1 = (EditText) findViewById(R.id.editText1);
        spinner1 = (Spinner) findViewById((R.id.spinner1));
        buildSpinnerView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_garden, menu);
        return true;
    }
    public void save(View view){
        String name = editText1.getText().toString();
        String location = spinner1.getSelectedItem().toString();
        String[] result = {name, location +", SE"};
        Intent intent = getIntent(); //get intent that started this activity
        intent.putExtra("Result", result); // data to be sent back to calling activity
        setResult(RESULT_OK, intent); // set result
        finish();
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
    private void buildSpinnerView() {
        String [] items = {"Stockholm", "Nacka", "Kista", "Falun", "Mora", "Leksand", "Bjurs", "Särna", "Idre", "Ludvika"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        spinner1.setAdapter(adapter);
    }
}
