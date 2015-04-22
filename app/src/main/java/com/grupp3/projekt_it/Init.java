package com.grupp3.projekt_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Init extends ActionBarActivity {

    // Time the init activity runs on startup (ms)
    private final int DURATION = 3000;
    private Thread initThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        // Hide the actionbar
        getSupportActionBar().hide();
        // Run this activity for a time, then change to another
        initThread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try{
                        wait(DURATION);
                    }
                    catch (InterruptedException e) {
                    }
                    finally {
                            finish();
                            Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                }
            }
        };
        initThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_init, menu);
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
}
