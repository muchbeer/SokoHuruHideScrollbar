package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning.MainLearningActivity;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning.Utility;

import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.services.UkawaServices.LOCATION_QUERY_EXTRA;

/**
 * Created by muchbeer on 1/26/2017.
 */

public class UkawaSyncAdapter  extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = UkawaSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;


    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[] {
            UkawaContract.UkawaEntry.COLUMN_UKAWA_ID,
            UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER,
            UkawaContract.UkawaEntry.COLUMN_TITLE,
            UkawaContract.UkawaEntry.COLUMN_DESC
    };

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_AUTHOR = 1;
    private static final int INDEX_TITLE = 2;
    private static final int INDEX_SHORT_DESC = 3;

    public UkawaSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    private long addLocation(String locationSetting, String habari, String moto, String current) {

        // First, check if the location with this city name exists in the db
        Cursor cursor = getContext().getContentResolver().query(
                UkawaContract.LocationEntry.CONTENT_URI,
                new String[]{UkawaContract.LocationEntry._ID},
                UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (cursor.moveToFirst()) {
            int locationIdIndex = cursor.getColumnIndex(UkawaContract.LocationEntry._ID);
            return cursor.getLong(locationIdIndex);
        } else {
            ContentValues locationValues = new ContentValues();
            locationValues.put(UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(UkawaContract.LocationEntry.COLUMN_CITY_NAME, habari);
            locationValues.put(UkawaContract.LocationEntry.COLUMN_MBUNGE, moto);
            locationValues.put(UkawaContract.LocationEntry.COLUMN_DIWANI, current);

            Uri locationInsertUri = getContext().getContentResolver()
                    .insert(UkawaContract.LocationEntry.CONTENT_URI, locationValues);

            return ContentUris.parseId(locationInsertUri);
        }
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public Void getWeatherDataFromJson(String forecastJsonStr, String locationSetting)
            throws JSONException {

        // Location information
        final String UKAWA_HABARI = "habari";
        final String UKAWA_MOTO = "moto";
        final String UKAWA_CURRENT = "current";


        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_MAIN_CITY = "city";
        final String UKAWA_MAIN_NEW = "main";
        final String UKAWA_BLOGS = "blogs";
        final String UKAWA_UBUNGE = "mbunge";
        final String UKAWA_TTTLE = "ukawa_title";
        final String UKAWA_AUTHOR = "ukawa_author";
        final String UKAWA_COMMENTS = "ukawa_comments";
        final String UKAWA_DATETIME = "ukawa_date";
        final String BLOGS_NAME = "blogs_name";
        final String UBUNGE_MAJIMBO = "majimbo";
        final String UKAWA_DESC = "ukawa_desc";
        final String UKAWA_IMAGE = "ukawa_image";
        final String UKAWA_ID  = "ukawa_id";
        final String UKAWA_LIKE = "ukawa_likes";
        final String UKAWA_ID_UI_PANEL = "flip_id";
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);


        JSONObject ukawahabari = forecastJson.getJSONObject(OWM_MAIN_CITY);
        String habari = ukawahabari.getString(UKAWA_HABARI);

        String moto = ukawahabari.getString(UKAWA_MOTO);

        String current = ukawahabari.getString(UKAWA_CURRENT);

        // Insert the location into the database.
        long locationID = addLocation(locationSetting, habari, moto, current);
// Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());

        for(int i = 0; i < weatherArray.length(); i++) {
            //  resultStrs = "";
            // For now, using the format "Day, description, hi/low"
            String day;
            Double majimbo;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".

            Long dateTime = dayForecast.getLong(UKAWA_DATETIME);
            String ukawa_id_ui_pane = dayForecast.getString(UKAWA_ID_UI_PANEL);


            // day = getReadableDateString(dateTime);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject mainObject = dayForecast.getJSONObject(UKAWA_MAIN_NEW);
            String ukawa_title = mainObject.getString(UKAWA_TTTLE);

            String ukawa_author = mainObject.getString(UKAWA_AUTHOR);
            String ukawa_comments = mainObject.getString(UKAWA_COMMENTS);
            String ukawa_desc = mainObject.getString(UKAWA_DESC);
            String ukawa_image = mainObject.getString(UKAWA_IMAGE);
            Double ukawa_id = mainObject.getDouble(UKAWA_ID);
            String ukawa_likes = mainObject.getString(UKAWA_LIKE);

            // blogs_name is in a child array called "blogs", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(UKAWA_UBUNGE).getJSONObject(0);
            majimbo = weatherObject.getDouble(UBUNGE_MAJIMBO);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(UKAWA_BLOGS);
            String blogs_news = temperatureObject.getString(BLOGS_NAME);



            ContentValues weatherValues = new ContentValues();

            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_LOC_KEY, locationID);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_DATETEXT,
                    UkawaContract.getDbDateString(new Date(dateTime*1L)));
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_DESC, ukawa_desc);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_TITLE, ukawa_title);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_NEWS_REPORTER, ukawa_author);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_IMAGE, ukawa_image);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_LIKE_VIEW, ukawa_likes);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_UKAWA_ID, ukawa_id);
            weatherValues.put(UkawaContract.UkawaEntry.COLUMN_UKAWA_ID_UI, ukawa_id_ui_pane);
            // weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, description);
            //  weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherId);


            cVVector.add(weatherValues);
            //  highAndLow = formatHighLows(high, low);
            // resultStrs[i] = ukawa_title + " - " + majimbo + " - " + ukawa_author;

            Log.v("The value is: ", ukawa_author);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(UkawaContract.UkawaEntry.CONTENT_URI, cvArray);
        }
        return null;

    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        // Getting the zipcode to send to the API
        String locationQuery = Utility.getPreferredLocation(getContext());


        // If there's no zip code, there's nothing to look up.  Verify size of params.
   /*     if (params.length == 0) {
            return null;
        }
*/
       // String locationQuery = intent.getStringExtra(LOCATION_QUERY_EXTRA);

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 7;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //"http://gdgexpertz.000webhostapp.com/data.json";
            final String FORECAST_BASE_URL =
                    "http://gdgexpertz.000webhostapp.com";

            final String QUERY_PARAM = "ukawa";


             /*   Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .build();
*/

            // URL url = new URL(builtUri.toString());

            Uri builtUri = Uri.parse(FORECAST_BASE_URL)
                    .buildUpon()
                    .appendPath(locationQuery).build();
            Log.d("link is", builtUri.toString());
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.

            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.

            }
            forecastJsonStr = buffer.toString();
            // return new String[]{forecastJsonStr};
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getWeatherDataFromJson(forecastJsonStr, locationQuery);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    private void notifyWeather() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        long lastSync = prefs.getLong(lastNotificationKey, 0);

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
            // Last sync was more than 1 day ago, let's send a notification with the weather.
            String locationQuery = Utility.getPreferredLocation(context);

            Uri ukawaUri = UkawaContract.UkawaEntry.buildUkawaLocationWithDate(locationQuery, UkawaContract.getDbDateString(new Date()));

            // we'll query our contentProvider, as always
            Cursor cursor = context.getContentResolver().query(ukawaUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

            if (cursor.moveToFirst()) {
                int weatherId = cursor.getInt(INDEX_WEATHER_ID);
                String high = cursor.getString(INDEX_AUTHOR);
                String low = cursor.getString(INDEX_TITLE);
                String desc = cursor.getString(INDEX_SHORT_DESC);

              //  Drawable drawable  = getContext().getResources().getDrawable(R.drawable.ic_launcher);
              //  imgView.setImageDrawable(drawable);
                int iconId = R.drawable.ic_launcher;
                String title = context.getString(R.string.app_name);

                // Define the text of the forecast.
              /*  String contentText = String.format(context.getString(R.string.format_notification),
                        desc,
                        Utility.formatTemperature(context, high),
                        Utility.formatTemperature(context, low));*/

                String contentText =  "hotnews";

                // NotificationCompatBuilder is a very convenient way to build backward-compatible
                // notifications.  Just throw in some data.
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(iconId)
                                .setContentTitle(title)
                                .setContentText(contentText);

                // Make something interesting happen when the user clicks on the notification.
                // In this case, opening the app is sufficient.
                Intent resultIntent = new Intent(context, MainLearningActivity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());


                //refreshing last sync
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.commit();
            }
        }

    }




    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        UkawaSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}
