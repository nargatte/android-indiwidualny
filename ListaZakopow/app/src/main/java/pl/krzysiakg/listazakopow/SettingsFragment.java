package pl.krzysiakg.listazakopow;

import android.os.Bundle;

import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
