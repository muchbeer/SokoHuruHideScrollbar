package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 11/23/2016.
 */

public class Utility {
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

   }
