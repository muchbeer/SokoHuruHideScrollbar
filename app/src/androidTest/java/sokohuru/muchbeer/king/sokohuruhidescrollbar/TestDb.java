package sokohuru.muchbeer.king.sokohuruhidescrollbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

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
    // Test data we're going to insert into the DB to see if it works.
    int locationId = 2;
   public static final String testLocationSetting = "dodoma";
    public static final String testCityName = "Dodoma";
    public static final String testMbunge = "John Mnyika";
    public static final String testDiwani = "Katala";

    //Ukawa News
    public static final String ukawaDateText = "20141212";
    public static final String ukawaTitleText = "Mnyika";
    public static final String ukawaDescText = "Uyu mbunge ni msumbufu sana";
    public static final String ukawaCommentsText = "Anfaa kufanya ivo";
    public static final  String ukawaLikeViewText="like";
    public static final String ukawaNewsReporterText="George";
    public static final String ukawaImageText="httpsukawatz";


    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(UkawaDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new UkawaDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {



        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        UkawaDbHelper dbHelper = new UkawaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues testLocationValues = createNorthPoleLocationValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testLocationValues);

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
        Cursor cursorLocation = db.query(
                LocationEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
//simplified form
        validateCursor(cursorLocation, testLocationValues);


            // Fantastic.  Now that we have a location, add some weather!
        ContentValues ukawaNewValues = createUkawaValues(locationRowId);

        long ukawaRowId = db.insert(UkawaEntry.TABLE_NAME, null, ukawaNewValues);
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

//ukawaSimplified value
        validateCursor(ukawaCursor, ukawaNewValues);

           dbHelper.close();
    }
    static ContentValues createUkawaValues(long locationRowId) {
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

        return ukawaValues;
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
}
