package sokohuru.muchbeer.king.sokohuruhidescrollbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaDbHelper;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.UkawaEntry;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.LocationEntry;

import static android.R.attr.name;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.drawable.ukawa;

/**
 * Created by muchbeer on 11/18/2016.
 */

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();


    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(UkawaDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new UkawaDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        // Test data we're going to insert into the DB to see if it works.
        int locationId = 2;
        String testLocationSetting = "dodoma";
        String testCityName = "Dodoma";
        String testMbunge = "John Mnyika";
        String testDiwani = "Katala";

        //Ukawa News
        String ukawaDateText = "20141212";
        String ukawaTitleText = "Mnyika";
        String ukawaDescText = "Uyu mbunge ni msumbufu sana";
        String ukawaCommentsText = "Anfaa kufanya ivo";
        String ukawaLikeViewText="like";
        String ukawaNewsReporterText="George";
        String ukawaImageText="httpsukawatz";
        int ukawaLocKey = 2;

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        UkawaDbHelper dbHelper = new UkawaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        values.put(LocationEntry.COLUMN_MBUNGE, testMbunge);
        values.put(LocationEntry.COLUMN_DIWANI, testDiwani);

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Specify which columns you want.
        String[] columns = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_CITY_NAME,
                LocationEntry.COLUMN_MBUNGE,
                LocationEntry.COLUMN_DIWANI
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.
            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String locationSetting = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex((LocationEntry.COLUMN_CITY_NAME));
            String nameCity = cursor.getString(nameIndex);

            int mbungeIndex = cursor.getColumnIndex((LocationEntry.COLUMN_MBUNGE));
            String saveMbunge = cursor.getString(mbungeIndex);

            int diwaniIndex = cursor.getColumnIndex((LocationEntry.COLUMN_DIWANI));
            String saveDiwani = cursor.getString(diwaniIndex);

            // Hooray, data was returned!  Assert that it's the right data, and that the database
            // creation code is working as intended.
            // Then take a break.  We both know that wasn't easy.
            assertEquals(testCityName, nameCity);
            assertEquals(testLocationSetting, locationSetting);
            assertEquals(testMbunge, saveMbunge);
            assertEquals(testDiwani, saveDiwani);

            // Fantastic.  Now that we have a location, add some weather!
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues ukawaValues = new ContentValues();
        ukawaValues.put(UkawaEntry.COLUMN_LOC_KEY, locationRowId);
        ukawaValues.put(UkawaEntry.COLUMN_DATETEXT, ukawaDateText);

        ukawaValues.put(UkawaEntry.COLUMN_TITLE, ukawaTitleText);
        ukawaValues.put(UkawaEntry.COLUMN_DESC, ukawaDescText);
        ukawaValues.put(UkawaEntry.COLUMN_COMMENTS, ukawaCommentsText);
        ukawaValues.put(UkawaEntry.COLUMN_LIKE_VIEW, ukawaLikeViewText);
        ukawaValues.put(UkawaEntry.COLUMN_NEWS_REPORTER, ukawaNewsReporterText);
        ukawaValues.put(UkawaEntry.COLUMN_IMAGE, ukawaImageText);

        long ukawaRowId = db.insert(UkawaEntry.TABLE_NAME, null, ukawaValues);
        assertTrue(ukawaRowId != -1);
        Log.d("UKAWA ROW ID", "New row id: " + locationRowId);

        // A cursor is your primary interface to the query results.

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Specify which columns you want.
        String[] columnUkawa = {
                UkawaEntry._ID,
                UkawaEntry.COLUMN_DATETEXT,
                UkawaEntry.COLUMN_TITLE,
                UkawaEntry.COLUMN_DESC,
                UkawaEntry.COLUMN_COMMENTS,
                UkawaEntry.COLUMN_LIKE_VIEW,
                UkawaEntry.COLUMN_IMAGE,
                UkawaEntry.COLUMN_LOC_KEY,
                UkawaEntry.COLUMN_NEWS_REPORTER
        };
        Cursor ukawaCursor = db.query(
                UkawaEntry.TABLE_NAME,  // Table to Query
                columnUkawa, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        if (!ukawaCursor.moveToFirst()) {
            fail("No weather data returned!");
        }

        int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
        String locationSetting = cursor.getString(locationIndex);


        assertEquals(locationRowId, ukawaCursor.getInt(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_LOC_KEY)));
        
        assertEquals(ukawaDateText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_DATETEXT)));

        assertEquals(ukawaTitleText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_TITLE)));

        assertEquals(ukawaDescText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_DESC)));

        assertEquals(ukawaNewsReporterText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_NEWS_REPORTER)));


        assertEquals(ukawaImageText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_IMAGE)));


        assertEquals(ukawaCommentsText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_COMMENTS)));

        assertEquals(ukawaLikeViewText, ukawaCursor.getString(
                ukawaCursor.getColumnIndex(UkawaEntry.COLUMN_LIKE_VIEW)));


        ukawaCursor.close();
        dbHelper.close();
    }

}
