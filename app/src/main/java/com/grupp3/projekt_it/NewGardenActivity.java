package com.grupp3.projekt_it;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_garden);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setBackgroundDrawable(null);
        spinner1 = (Spinner) findViewById((R.id.spinner1));
        //spinner2 = (Spinner) findViewById((R.id.spinner2));
        buildSpinnerView1();
        //buildSpinnerView2();
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
        //String zone = spinner2.getSelectedItem().toString();
        String zone = "1";
        String[] result = {name, location +", SE", zone};
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
    private void buildSpinnerView1() {
        String [] items = {"Alingsås","Arboga","Arvika","Askersund","Avaskär","Avesta","Boden","Bollnäs","Borgholm","Borlänge",
                "Borås","Broo","Brätte","Båstad","Djursholm","Eksjö","Elleholm","Enköping","Eskilstuna","Eslöv","Fagersta","Falkenberg",
                "Falköping","Falsterbo","Falun","Filipstad","Flen","Gränna","Gävle","Göteborg","Hagfors","Halmstad","Haparanda","Hedemora",
                "Helsingborg","Hjo","Hudiksvall","Huskvarna","Härnösand","Hässleholm","Höganäs","Järle","Jönköping","Kalmar","Karlshamn",
                "Karlskoga","Karlskrona","Karlstad","Katrineholm","Kiruna","Kongahälla","Kramfors","Kristianopel","Kristianstad",
                "Kristinehamn","Kumla","Kungsbacka","Kungälv","Köping","Laholm","Landskrona","Lidingö","Lidköping","Lindesberg","Linköping"
                ,"Ljungby","Lomma","Ludvika","Luntertun","Luleå","Lund","Lycksele","Lyckå","Lysekil","Lödöse","Malmö","Mariefred","Mariestad"
                ,"Marstrand","Mjölby","Motala","Mölndal","Mönsterås","Nacka","Nora","Norrköping","Norrtälje","Nybro","Nyköping","Nya Lidköping"
                ,"Nynäshamn","Nässjö","Oskarshamn","Oxelösund","Piteå","Ronneby","Sala","Sandviken","Sigtuna","Simrishamn","Skanör","Skara",
                "Skellefteå","Skänninge","Skövde","Sollefteå","Solna","Stockholm","Strängnäs","Strömstad","Sundbyberg","Sundsvall","Säffle",
                "Säter","Sävsjö","Söderhamn","Söderköping","Södertälje","Sölvesborg","Tidaholm","Tommarp","Torget","Torshälla","Tranås",
                "Trelleborg","Trollhättan","Trosa","Uddevalla","Ulricehamn","Umeå","Uppsala","Vadstena","Varberg","Vaxholm","Vetlanda",
                "Vimmerby","Visby","Vä","Vänersborg","Värnamo","Västervik","Västerås","Växjö","Ystad","Åhus","Åmål","Älvsborg","Ängelholm",
                "Örebro","Öregrund","Örnsköldsvik","Östersund","Östhammar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        spinner1.setAdapter(adapter);
    }
    private void buildSpinnerView2() {
        String [] items = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        spinner2.setAdapter(adapter);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }
}
