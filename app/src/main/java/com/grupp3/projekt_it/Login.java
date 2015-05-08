package com.grupp3.projekt_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Arrays;


public class Login extends ActionBarActivity {

    protected EditText mUserLogin;
    String acceptedUsers[] = {"1234","4321"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // hide the actionbar in this activity
        getSupportActionBar().hide();

        mUserLogin = (EditText)findViewById(R.id.user_login_info);
        // define the button
        ImageButton button = (ImageButton)findViewById(R.id.login_button);

        //Auto-fill EditText
        Context context = getBaseContext();
        String premNum = getDefaults("premKey", context);


        if(premNum != null){
            mUserLogin.setText(premNum);

        }

        // wait for the button to be clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text from textbox
                String user = mUserLogin.getText().toString().trim();
                // check if the entered information is a valid user, if not show error message
                if(Arrays.asList(acceptedUsers).contains(user)) {
                    // Save valid premnumber

                    Context context = getBaseContext();
                    setDefaults("premKey", user, context);


                    // Change activity and send the user number to be used in other activities
                    Intent i = new Intent(Login.this, MainActivity.class);
                    //i.putExtra("user", user);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Login.this,"Ogiltigt prenumerationsnummer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    // on back button click, exit the application, hence skip the login screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
