package com.grupp3.projekt_it;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by Mikael on 2015-04-22.
 */
public class Preferences extends PreferenceActivity {
    private Preference mPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        // Get user number from previous activity
        Intent intent = getIntent();
        String user = intent.getExtras().getString("userNumber");
        // Set the preference to current user number
        mPreference = getPreferenceScreen().findPreference("subscription_number");
        mPreference.setSummary(user);
    }

}
