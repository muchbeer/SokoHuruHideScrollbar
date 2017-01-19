package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning.DetailFragment.DATE_KEY;

public class DetailActivity extends AppCompatActivity {

    public static final String UKAWA_UI_PANE_DATE_KEY = "ukawa_date";
    private static final String LOCATION_KEY = "location";

    private boolean mTwoPane;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            String date = getIntent().getStringExtra(UKAWA_UI_PANE_DATE_KEY);

            Bundle arguments = new Bundle();
            arguments.putString(DetailActivity.UKAWA_UI_PANE_DATE_KEY, date);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ukawa_detail_container, fragment)
                    .commit();
        }

/*

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ukawa_detail_container, new DetailFragment())
                    .commit();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent startSettings = new Intent(this, SettingsActivity.class);
            startActivity(startSettings);
            return true;
        }

        // Respond to the action bar's Up/Home button
       /* if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;

        }
*/
        return super.onOptionsItemSelected(item);
    }

}
