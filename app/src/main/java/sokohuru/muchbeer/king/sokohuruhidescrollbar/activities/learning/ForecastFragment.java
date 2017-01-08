package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;

import static android.R.attr.format;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.id.container;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.string.pref_units_specific;

/**
 * Created by muchbeer on 1/1/2017.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mForecastAdapter;
    private TextView display_result_textview;

    private static final int FORECAST_LOADER = 0;
    private String mLocation;


    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS_NEWS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            UkawaContract.UkawaEntry.TABLE_NAME + "." + UkawaContract.UkawaEntry._ID,
            UkawaContract.UkawaEntry.COLUMN_DATETEXT,
            UkawaContract.UkawaEntry.COLUMN_DESC,
            UkawaContract.UkawaEntry.COLUMN_TITLE,
            UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER,
            UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    public static final int COL_UKAWA_ID = 0;
    public static final int COL_UKAWA_DATE = 1;
    public static final int COL_UKAWA_DESC = 2;
    public static final int COL_UKAWA_TITLE = 3;
    public static final int COL_UKAWA_NEWS_REPORTER = 4;
    public static final int COL_UKAWA_LOCATION_SETTING = 5;

    public ForecastFragment() {
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshNewz();

                        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshNewz() {


      //  FetchNewsTask weatherTask = new FetchNewsTask();

       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));*/
/*
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = sharedPrefs.getString(getString(R.string.pref_units_key), getString(pref_units_specific));*/

        String location = Utility.getPreferredLocation(getActivity());

        new FetchNewsTask(getActivity()).execute(location);
     //   weatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshNewz();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Mon 6/23?- Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new SimpleCursorAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_forecast_json, // The name of the layout ID.
                        null,
                        new String[] {
                                UkawaContract.UkawaEntry.COLUMN_DATETEXT,
                                UkawaContract.UkawaEntry.COLUMN_DESC,
                                UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER,
                                UkawaContract.UkawaEntry.COLUMN_TITLE


                        },
                        new int[]{
                                R.id.list_item_date_textview,
                                R.id.list_item_forecast_textview,
                                R.id.list_item_habari_textview,
                                R.id.list_item_ukawa_textview
                        },
                        0 // The ID of the textview to populate.
                      );

        mForecastAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
             //  boolean isMetric = Utility.isMetric(getActivity());
                switch (columnIndex) {

                    case COL_UKAWA_DATE: {
                        // we have to do some formatting and possibly a conversion
                        String dateString = cursor.getString(columnIndex);
                        TextView dateView = (TextView) view;
                        dateView.setText(Utility.formatDate(dateString));
                        return true;
                    }

                }
                return false;
            }
        });
        View rootView = inflater.inflate(R.layout.fragment_main_json, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        display_result_textview = (TextView) rootView.findViewById(R.id.view_data);
        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               /* String ukawaData = mForecastAdapter.getItem(position);
                Intent sendData = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, ukawaData);
                startActivity(sendData);*/
            }
        });
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, get the String representation for today,
        // and filter the query to return weather only for dates after or including today.
        // Only return data after today.
        String startDate = UkawaContract.getDbDateString(new Date());

        // Sort order:  Ascending, by date.
        String sortOrder = UkawaContract.UkawaEntry.COLUMN_DATETEXT + " ASC";

        mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = UkawaContract.UkawaEntry.buildUkawaLocationWithStartDate(
                mLocation, startDate);

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS_NEWS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }
}

