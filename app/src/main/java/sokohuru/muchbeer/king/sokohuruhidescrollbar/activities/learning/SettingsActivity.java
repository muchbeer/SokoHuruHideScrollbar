package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.sync.UkawaSyncAdapter;

import static android.R.attr.value;

/**
 * Created by muchbeer on 11/17/2016.
 */

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener{

    // since we use the preference change initially to populate the summary
    // field, we'll ignore that change at start of the activity
    boolean mBindingPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));

    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        mBindingPreference = true;
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));

        mBindingPreference = false;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringValue = newValue.toString();

        // are we starting the preference activity?
        if (!mBindingPreference) {
            if (preference.getKey().equals(getString(R.string.pref_location_key))) {
                /*FetchNewsTask weatherTask = new FetchNewsTask(this);
                String location = newValue.toString();
                weatherTask.execute(location);*/
                UkawaSyncAdapter.syncImmediately(this);

            } else {
                // notify code that weather may be impacted
                getContentResolver().notifyChange(UkawaContract.UkawaEntry.CONTENT_URI, null);
            }
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
            }

        }

        return true;

        }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}