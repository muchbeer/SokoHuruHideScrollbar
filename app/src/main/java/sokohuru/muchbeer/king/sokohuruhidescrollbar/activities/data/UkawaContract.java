package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data;

import android.provider.BaseColumns;

/**
 * Created by muchbeer on 11/18/2016.
 */

public class UkawaContract {

    /* Inner class that defines the table contents of the location table */
    public static final class LocationEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "location";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_CITY_NAME = "city_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_MBUNGE = "ukawa_mbunge";
        public static final String COLUMN_DIWANI = "ukawa_diwani";
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class UkawaEntry implements BaseColumns {

        public static final String TABLE_NAME = "ukawa";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT = "ukawa_date";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_UKAWA_ID = "ukawa_id";

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
    }
}
