package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.UkawaEntry;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.LocationEntry;

import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.UkawaEntry.COLUMN_LOC_KEY;

/**
 * Created by muchbeer on 11/18/2016.
 */

public class UkawaDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "ukawa.db";

    public UkawaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the name of the regional
        // TBD

        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LocationEntry.COLUMN_LOCATION_SETTING + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_LOCATION_SETTING_REPLACE_ID + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_MBUNGE + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_DIWANI + " TEXT NOT NULL, " +
                "UNIQUE (" + LocationEntry.COLUMN_LOCATION_SETTING +") ON CONFLICT IGNORE"+
                " );";

        final String SQL_CREATE_UKAWA_TABLE = "CREATE TABLE " + UkawaEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                UkawaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                COLUMN_LOC_KEY + " INTERGER NOT NULL, " +
                UkawaEntry.COLUMN_DATETEXT + " TEXT NULL, " +
                UkawaEntry.COLUMN_DESC + " TEXT NULL, " +
                UkawaEntry.COLUMN_UKAWA_ID + " TEXT NULL," +
                UkawaEntry.COLUMN_TITLE + " TEXT NULL, " +
                UkawaEntry.COLUMN_NEWS_REPORTER + " TEXT NULL, " +
                UkawaEntry.COLUMN_COMMENTS + " TEXT NULL, " +
                UkawaEntry.COLUMN_LIKE_VIEW + " TEXT NULL, " +
                UkawaEntry.COLUMN_IMAGE +" TEXT NULL, " +
                UkawaEntry.COLUMN_UKAWA_ID_UI + " TEXT NULL, " +


                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + UkawaEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + ") " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + UkawaEntry.COLUMN_DATETEXT + ", " +
                UkawaEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";








        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_UKAWA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UkawaEntry.TABLE_NAME);
        onCreate(db);
    }
}
