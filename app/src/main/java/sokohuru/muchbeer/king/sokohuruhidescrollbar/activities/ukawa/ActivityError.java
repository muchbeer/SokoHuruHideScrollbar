package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 8/27/2015.
 */
public class ActivityError extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        FragmentRais fragment = new FragmentRais();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
     //   fragmentTransaction.replace(R.id.container3, fragment);
    }
}
