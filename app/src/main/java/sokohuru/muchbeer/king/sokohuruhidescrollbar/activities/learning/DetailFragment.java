package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;

import static android.util.Log.e;

/**
 * Created by muchbeer on 11/17/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String UKAWA_UI_PANE_KEY = "flip_id";
    public static final String DATE_KEY = "ukawa_date";
    private static final String LOCATION_KEY = "location";

     private static final int DETAIL_LOADER = 0;

    private ShareActionProvider mShareActionProvider;
    private String mLocation;
    private String mForecast;

 private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final String UKAWA_SHARE_HASHTAG = " #ukawa";
    private String ukawaShare;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    private static final String[] UKAWA_COLUMNS = {
            UkawaContract.UkawaEntry.TABLE_NAME + "." + UkawaContract.UkawaEntry._ID,
            UkawaContract.UkawaEntry.COLUMN_DATETEXT,
            UkawaContract.UkawaEntry.COLUMN_UKAWA_ID_UI,
            UkawaContract.UkawaEntry.COLUMN_DESC,
            UkawaContract.UkawaEntry.COLUMN_TITLE,
            UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER,
            UkawaContract.UkawaEntry.COLUMN_IMAGE,
            UkawaContract.UkawaEntry.COLUMN_COMMENTS,
            UkawaContract.UkawaEntry.COLUMN_UKAWA_ID,
            UkawaContract.UkawaEntry.COLUMN_LIKE_VIEW
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

     /*   // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
           ukawaShare = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(ukawaShare);
        }
*/
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(LOCATION_KEY);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (null !=mLocation) {
            outState.putString(LOCATION_KEY, mLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocation != null &&
                !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                ukawaShare + UKAWA_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null || !intent.hasExtra(DATE_KEY)) {
            Toast.makeText(getActivity(), "It has not reach ", Toast.LENGTH_LONG).show();
            return null;
        }
        String forecastDate = intent.getStringExtra(DATE_KEY);

        // Sort order:  Ascending, by date.
      //  String sortOrder = UkawaContract.UkawaEntry.COLUMN_UKAWA_ID + " ASC";

        mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = UkawaContract.UkawaEntry.buildUkawaLocationWithUiPanel(
                mLocation, forecastDate);
        Log.v(LOG_TAG, weatherForLocationUri.toString());

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                UKAWA_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        String dateString = Utility.formatDate(
                data.getString(data.getColumnIndex(UkawaContract.UkawaEntry.COLUMN_DATETEXT)));
        ((TextView) getView().findViewById(R.id.detail_date_textview))
                .setText(dateString);

        String weatherDescription =
                data.getString(data.getColumnIndex(UkawaContract.UkawaEntry.COLUMN_DESC));
        ((TextView) getView().findViewById(R.id.detail_desc_textview))
                .setText(weatherDescription);

      String ukawaAuthor =
              data.getString(data.getColumnIndex(UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER));

        ((TextView) getView().findViewById(R.id.detail_author_textview))
                .setText(ukawaAuthor);

        String ukawaComments =
                data.getString(data.getColumnIndex(UkawaContract.UkawaEntry.COLUMN_COMMENTS));
        ((TextView) getView().findViewById(R.id.detail_comments_textview))
                .setText(ukawaComments);

        String ukawaLikes =
                data.getString(data.getColumnIndex(UkawaContract.UkawaEntry.COLUMN_LIKE_VIEW));
        ((TextView) getView().findViewById(R.id.detail_likes_textview))
                .setText(ukawaLikes);

        // We still need this for the share intent
        mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, ukawaAuthor, ukawaComments);

        Log.v(LOG_TAG, "Forecast String: " + mForecast);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
