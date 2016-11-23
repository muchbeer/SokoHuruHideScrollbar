package sokohuru.muchbeer.king.sokohuruhidescrollbar;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.Test;

import java.util.Map;
import java.util.Set;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.LocationEntry;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract.UkawaEntry;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaDbHelper;

import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.createNorthPoleLocationValues;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.createUkawaValues;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.testCityName;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.testDiwani;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.testLocationSetting;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.testMbunge;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.TestDb.validateCursor;

/**
 * Created by muchbeer on 11/18/2016.
 */

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    // Test data we're going to insert into the DB to see if it works.
    int locationId = 2;

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(UkawaDbHelper.DATABASE_NAME);
    }

    public void testInsertReadProvider() {



        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        UkawaDbHelper dbHelper = new UkawaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues testLocationValues = createNorthPoleLocationValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testLocationValues);

     //   Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testLocationValues);
      //  long locationRowId = ContentUris.parseId(locationUri);

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
       // Cursor cursorLocation = mContext.getContentResolver().query(

        Cursor cursorLocation =mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause

                null// columns to group by

        );
//simplified form

        if(cursorLocation.moveToFirst()) {
            validateCursor(cursorLocation, testLocationValues);

        }

        // Now see if we can successfully query if we include the row id
//This is done by returning basically a very single item from our entire table location
        cursorLocation = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(locationRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        if(cursorLocation.moveToFirst()) {
            validateCursor(cursorLocation, testLocationValues);

            //I have paste inside the ukawaNew content value
            // Fantastic.  Now that we have a location, add some ukawa!
            ContentValues ukawaNewValues = TestDb.createUkawaValues(locationRowId);

            long ukawaRowId = db.insert(UkawaEntry.TABLE_NAME, null, ukawaNewValues);

        /*
        Uri ukawaRowIdUri = mContext.getContentResolver()
                .insert(UkawaEntry.CONTENT_URI, ukawaNewValues);

        long ukawaRowId = ContentUris.parseId(ukawaRowIdUri);
         */

            assertTrue(ukawaRowId != -1);
            Log.d("UKAWA ROW ID", "New row id: " + ukawaRowId);

            // A cursor is your primary interface to the query results.

            // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
            // the round trip.

            // Specify which columns you want.
            String[] columnUkawa = {

                    UkawaEntry.COLUMN_DATETEXT,
                    UkawaEntry.COLUMN_TITLE,
                    UkawaEntry.COLUMN_DESC,
                    UkawaEntry.COLUMN_COMMENTS,
                    UkawaEntry.COLUMN_LIKE_VIEW,
                    UkawaEntry.COLUMN_IMAGE,
                    UkawaEntry.COLUMN_LOC_KEY,
                    UkawaEntry.COLUMN_NEWS_REPORTER
            };

            // Cursor ukawaCursor = mContext.getContentResolver().query(
            Cursor ukawaCursor = mContext.getContentResolver().query(
                    UkawaEntry.CONTENT_URI,  // Table to Query
                    columnUkawa, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null

                    // columns to group by

            );

            if(ukawaCursor.moveToFirst()) {
                validateCursor(ukawaCursor, ukawaNewValues);
            } else {
                fail("No ukawa News returned!");
            }

            //its good practice to close your cursor
            ukawaCursor.close();


            //A Cursor is your primary interface to the query result
            // Add the location values in with the weather data so that we can make
            // sure that the join worked and we actually get all the values back
// Get the joined Weather and Location data
            ukawaCursor = mContext.getContentResolver().query(
                    UkawaEntry.buildUkawaLocationWithDate(TestDb.testCityName, TestDb.ukawaDateText),
                    null,
                    null,
                    null,
                    null
            );

            if(ukawaCursor.moveToFirst()) {
                validateCursor(ukawaCursor, ukawaNewValues);
            } else {
                fail("No ukawa News returned!");
            }
        } else {
            //this one is for the cursor upper
            fail("No ukawa News returned!");
        }


//ukawaSimplified value



        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
//        addAllContentValues(ukawaNewValues, testLocationValues);



        //Use for future testing
        /*
        // Get the joined Weather and Location data with a start date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(
                        TestDb.TEST_LOCATION, TestDb.TEST_DATE),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestDb.validateCursor(weatherCursor, weatherValues);



        // Get the joined Weather data for a specific date
        ukawaCursor = mContext.getContentResolver().query(
                UkawaEntry.buildUkawaLocationWithDate(TestDb.testCityName, TestDb.ukawaDateText),
                null,
                null,
                null,
                null
        );
        validateCursor(ukawaCursor, ukawaNewValues);

          ukawaCursor = mContext.getContentResolver().query(
                    UkawaEntry.buildUkawaLocation("Dodoma"),
                    columnUkawa, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );
    */



        dbHelper.close();
    }

    static ContentValues createNorthPoleLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        values.put(LocationEntry.COLUMN_MBUNGE, testMbunge);
        values.put(LocationEntry.COLUMN_DIWANI, testDiwani);

        return values;
    }
    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }

    public void testGetType() {
        // content://sokohuru.muchbeer.king.sokohuruhidescrollbar/ukawa/
        String type = mContext.getContentResolver().getType(UkawaEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/ukawa
        assertEquals(UkawaEntry.CONTENT_TYPE, type);


        String testLocation = "Dodoma";
        // content://sokohuru.muchbeer.king.sokohuruhidescrollbar/dodoma
        type = mContext.getContentResolver().getType(
                UkawaEntry.buildUkawaLocation(testLocation));
        // vnd.android.cursor.dir/sokohuru.muchbeer.king.sokohuruhidescrollbar/dodoma
        assertEquals(UkawaEntry.CONTENT_TYPE, type);



        String testDate = "20140612";
        // content://com.example.android.sunshine.app/ukawa/Dodoma/20140612
        type = mContext.getContentResolver().getType(
                UkawaEntry.buildUkawaLocationWithDate(testLocation, testDate));
        // vnd.android.cursor.item/com.example.android.sunshine.app/ukawa
        assertEquals(UkawaEntry.CONTENT_ITEM_TYPE, type);


        // content://sokohuru.muchbeer.king.sokohuruhidescrollbar/location/
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/sokohuru.muchbeer.king.sokohuruhidescrollbar/location
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/location/1
        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        // vnd.android.cursor.item/sokohuru.muchbeer.king.sokohuruhidescrollbar/location
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    // The target api annotation is needed for the call to keySet -- we wouldn't want
    // to use this in our app, but in a test it's fine to assume a higher target.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet()) {
            destination.put(key, source.getAsString(key));
        }
    }
}
