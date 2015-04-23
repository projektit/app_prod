package com.grupp3.projekt_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Mikael on 2015-04-22.
 */
public class Preferences extends PreferenceActivity {
    private Preference mPreference;
    private Toolbar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mActionBar.setTitle("Inställningar");
        // Get user number from previous activity
        /*Intent intent = getIntent();
        String user = intent.getExtras().getString("userNumber");
        // Set the preference to current user number
        mPreference = getPreferenceScreen().findPreference("subscription_number");
        mPreference.setSummary(user);*/



    //Auto-fill EditText
    String premNum = getPremNumber2();
        Log.i("After getPremNumber", "HEJ0");
        if(premNum != null) {
            Log.i("After getPremNumber", "HEJ1");
            Log.i("After getPremNumber", premNum);
            Log.i("After getPremNumber", "HEJ2");
        }
        Log.i("After getPremNumber", "HEJ3");
        //Log.i("After getPremNumber", premNum);
    premNum = "1234";
    if(premNum != null){
        mPreference = getPreferenceScreen().findPreference("subscription_number");
        mPreference.setSummary(premNum);

    }

    }
    // Add a custom "fake" actionbar to the activity which contains a title and a back button
    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.settings_activity, new LinearLayout(this), false);

        mActionBar = (Toolbar) contentView.findViewById(R.id.action_bar);
        mActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);

        getWindow().setContentView(contentView);
    }


    private String getPremNumber2() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        Log.i("After getPremNumber", "HEJ10");

        String key = getString(R.string.premKey);
        Log.i("After getPremNumber", key);
        String existingPrem = sharedPreferences.getString(key, null);

        if (existingPrem != null) {
            Log.i("After getPremNumber", existingPrem);
            return existingPrem;
        }
        Log.i("After getPremNumber", "HEJ12");
        return null;
    }

}
