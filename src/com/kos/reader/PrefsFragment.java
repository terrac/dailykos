package com.kos.reader;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PrefsFragment extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.layout.preferences);
    }
    
    
    
}