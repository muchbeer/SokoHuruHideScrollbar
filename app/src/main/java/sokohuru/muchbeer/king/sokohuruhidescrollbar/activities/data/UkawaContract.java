package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.util.ISO8601Utils.format;

/**
 * Created by muchbeer on 11/18/2016.
 */

public class UkawaContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaProvider";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_UKAWA = "ukawa";
    public static final String PATH_LOCATION = "location";


    /* Inner class that defines the table contents of the location table */
    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;


        // Table name
        public static final String TABLE_NAME = "location";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_LOCATION_SETTING = "location_setting";


        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_CITY_NAME = "habari";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_MBUNGE = "moto";
        public static final String COLUMN_DIWANI = "current";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class UkawaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_UKAWA).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_UKAWA;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_UKAWA;


        public static String TABLE_NAME = "ukawa";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT = "ukawa_date";

        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT_ID = "ukawa_date";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_UKAWA_ID = "ukawa_id";

        //set user inter id separate ui
        public static final String COLUMN_UKAWA_ID_UI = "flip_id";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_DESC = "ukawa_desc";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_TITLE = "ukawa_title";
        public static final String COLUMN_NEWS_REPORTER = "ukawa_author";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_LIKE_VIEW = "ukawa_likes";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_COMMENTS = "comments";


        // Windspeed is stored as a float representing windspeed  mph
        public static final String COLUMN_IMAGE = "ukawa_image";

        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
      //  public static final String COLUMN_DEGREES = "degrees";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //This is almight uri that is basically targeting location editing
        public static Uri buildUkawaLocation(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
        }

        public static Uri buildUkawaLocationWithStartDate(String locationSetting, String startDate) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATETEXT, startDate).build();
        }

        public static Uri buildUkawaLocationWithDate(String locationSetting, String date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).appendPath(date).build();
        }

        public static Uri buildUkawaLocationWithUiPanel(String locationSetting, String flip_id) {

            return CONTENT_URI.buildUpon().appendPath(locationSetting).appendPath(flip_id).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATETEXT);
        }

        public static String getStartKeyFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_UKAWA_ID);
        }
    }

    public static final String DATE_FORMAT = "MMM dd, yyyy hh:mm";

    public static String getDbDateString(Date date) {
//Because the API returns a unix timestamp (measured in seconds)
//it must be converted to milliseconds in order to be converted to valid date
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

       // sdf.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));

      //  calendar.setTimeInMillis(date);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }


}
