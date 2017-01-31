package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.services.*;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.sync.UkawaSyncAdapter;

/**
 * Created by muchbeer on 1/1/2017.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  //  private SimpleCursorAdapter mForecastAdapter;
    //this help to get two split larger and small
  private UkawaAdapter mForecastAdapter;
    private TextView display_result_textview;

    private static final int FORECAST_LOADER = 0;
    private String mLocation;
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;


    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    //thhis is a join of the two tables
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
            UkawaContract.UkawaEntry.COLUMN_UKAWA_ID,
            UkawaContract.UkawaEntry.COLUMN_TITLE,
            UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER,
            UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING,
            UkawaContract.UkawaEntry.COLUMN_LIKE_VIEW,
            UkawaContract.UkawaEntry.COLUMN_UKAWA_ID_UI
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    public static final int COL_UKAWA_ID_AUTO = 0;
    public static final int COL_UKAWA_DATE = 1;

    public static final int COL_UKAWA_DESC = 2;
    public static final int COL_UKAWA_ID = 3;
    public static final int COL_UKAWA_TITLE = 4;
    public static final int COL_UKAWA_NEWS_REPORTER = 5;
    public static final int COL_UKAWA_LOCATION_SETTING = 6;
    public static final int COL_UKAWA_LIKE_VIEW = 7;
    public static final int COL_UKAWA_ID_UI = 8;
    private boolean mUseTodayLayout;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String date);
    }


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.

       // Set Collapsing Toolbar layout to the screen
        mForecastAdapter = new UkawaAdapter(getActivity(), null, 0);



        View rootView = inflater.inflate(R.layout.fragment_main_json, container, false);

        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar();

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               /* String ukawaData = mForecastAdapter.getItem(position);
                Intent sendData = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, ukawaData);
                startActivity(sendData);*/

                Cursor cursor = mForecastAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {

                    ((Callback)getActivity())
                            .onItemSelected(cursor.getString(COL_UKAWA_DATE));
                  /*  Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(DetailFragment.DATE_KEY, cursor.getString(COL_UKAWA_DATE));
                    startActivity(intent);*/
                }
                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        return rootView;

    }

    private void refreshNewz() {

/*
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = sharedPrefs.getString(getString(R.string.pref_units_key), getString(pref_units_specific));*/

       /* String location = Utility.getPreferredLocation(getActivity());

        new FetchNewsTask(getActivity()).execute(location);*/

        UkawaSyncAdapter.syncImmediately(getActivity());
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();
        //   refreshNewz();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        }
    }

    public void setUseTodayLayout(boolean useTodayLayout) {

        mUseTodayLayout = useTodayLayout;
        if(mForecastAdapter !=null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }

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
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.setSelection(mPosition);
          //  mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }
}

