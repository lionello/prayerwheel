package com.lunesu.prayerwheel;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class CustomizeActivity extends PreferenceActivity {

    public static final String PREFS_NAME = "PrayerWheelPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getPreferenceManager().setSharedPreferencesName(PREFS_NAME);

        addPreferencesFromResource(R.xml.preferences);
        
        Preference.OnPreferenceClickListener onhelp = new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				PrayerWheelActivity.onMenuHelpClicked(CustomizeActivity.this);
				return false;
			}
        };
        
        findPreference("help").setOnPreferenceClickListener(onhelp);
    }
}
