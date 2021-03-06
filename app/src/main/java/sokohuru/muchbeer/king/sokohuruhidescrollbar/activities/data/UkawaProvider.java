package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.drawable.ukawa;

/**
 * Created by muchbeer on 11/19/2016.
 */

public class UkawaProvider extends ContentProvider {

    private UkawaDbHelper ukawaOpenHelper;
    // first we have to create a join method to join two method
    private static final SQLiteQueryBuilder sUkawaByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

    static {
       // sUkawaByLocationSettingQueryBuilder = new SQLiteQueryBuilder;
        sUkawaByLocationSettingQueryBuilder.setTables(
                UkawaContract.UkawaEntry.TABLE_NAME + " INNER JOIN " +
                        UkawaContract.LocationEntry.TABLE_NAME +
                        " ON " + UkawaContract.UkawaEntry.TABLE_NAME +
                        "." + UkawaContract.UkawaEntry.COLUMN_LOC_KEY +
                        " = " + UkawaContract.LocationEntry.TABLE_NAME +
                        "." + UkawaContract.LocationEntry._ID
        );
    }

    //This is where people will be able to set their city
    //the ? will be replaced by the QUERY PARAMETER
    private static final String sLocationSettingSelection =
            UkawaContract.LocationEntry.TABLE_NAME+
                    "." + UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

    //This method will have to check wether the date will be greater or equal to parameter
    private static final String sLocationSettingWithStartDateSelection =
            UkawaContract.LocationEntry.TABLE_NAME+
                    "." + UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    UkawaContract.UkawaEntry.COLUMN_DATETEXT + " >= ? ";

    private static final String sLocationSettingAndDaySelection =
            UkawaContract.LocationEntry.TABLE_NAME +
                    "." + UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    UkawaContract.UkawaEntry.COLUMN_DATETEXT + " = ? ";

    //Next will add the method to get ukawa by location entry using the same query Builder
    //Note, we fetch our parameter from our uri

    // The URI Matcher used by this content provider.
    private Cursor getUkawaByLocationSetting(Uri uri, String[] projection, String sortOrder) {

        String locationSetting = UkawaContract.UkawaEntry.getLocationSettingFromUri(uri);
        String startDate = UkawaContract.UkawaEntry.getStartDateFromUri(uri);

        //and build string array that will be substituted from our query
        String[] selectionArgs;
        String selection;

        if (startDate == null) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{locationSetting};
        } else {
            selectionArgs = new String[]{locationSetting, startDate};
            selection = sLocationSettingWithStartDateSelection;
        }

        return sUkawaByLocationSettingQueryBuilder.query(ukawaOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getUkawaByLocationSettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = UkawaContract.UkawaEntry.getLocationSettingFromUri(uri);
        String date = UkawaContract.UkawaEntry.getDateFromUri(uri);

        return sUkawaByLocationSettingQueryBuilder.query(ukawaOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, date},
                null,
                null,
                sortOrder
        );
    }
    private static final UriMatcher sUriMatcher = buildUriMatcher();



    private static final int UKAWA = 100;
    private static final int UKAWA_WITH_LOCATION = 101;
    private static final int UKAWA_WITH_LOCATION_AND_DATE = 102;

    private static final int LOCATION = 300;
    private static final int LOCATION_ID = 301;

    private static UriMatcher buildUriMatcher() {

        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UkawaContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, UkawaContract.PATH_UKAWA, UKAWA);
        matcher.addURI(authority, UkawaContract.PATH_UKAWA + "/*", UKAWA_WITH_LOCATION);
        matcher.addURI(authority, UkawaContract.PATH_UKAWA + "/*/*", UKAWA_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, UkawaContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, UkawaContract.PATH_LOCATION + "/#", LOCATION_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        ukawaOpenHelper = new UkawaDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            // "ukawa/*/*"
            case UKAWA_WITH_LOCATION_AND_DATE: {

                //This is under investigation
                retCursor = getUkawaByLocationSettingAndDate(uri, projection, sortOrder);

                break;
            }
            // "ukawa/*"
            case UKAWA_WITH_LOCATION: {
                retCursor = getUkawaByLocationSetting(uri, projection, sortOrder);

                break;
            }
            // "ukawa"
            case UKAWA: {
                retCursor = ukawaOpenHelper.getReadableDatabase().query(
                        UkawaContract.UkawaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location/*"
            case LOCATION_ID: {
                retCursor = ukawaOpenHelper.getReadableDatabase().query(
                        UkawaContract.LocationEntry.TABLE_NAME,
                        projection,
                        UkawaContract.LocationEntry._ID + "= '" + ContentUris.parseId(uri) +" '",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case LOCATION: {
                retCursor = ukawaOpenHelper.getReadableDatabase().query(
                        UkawaContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }




    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case UKAWA_WITH_LOCATION_AND_DATE:
                return UkawaContract.UkawaEntry.CONTENT_ITEM_TYPE;

            case UKAWA_WITH_LOCATION:
                return UkawaContract.UkawaEntry.CONTENT_TYPE;

            case UKAWA:
                return UkawaContract.UkawaEntry.CONTENT_TYPE;
            case LOCATION:
                return UkawaContract.LocationEntry.CONTENT_TYPE;

            case LOCATION_ID:
                return UkawaContract.LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)

    {


        final SQLiteDatabase db = ukawaOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case UKAWA: {
                long _id = db.insert(UkawaContract.UkawaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = UkawaContract.UkawaEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LOCATION: {
                long _id = db.insert(UkawaContract.LocationEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = UkawaContract.LocationEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = ukawaOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case UKAWA:
                rowsDeleted = db.delete(
                        UkawaContract.UkawaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION:
                rowsDeleted = db.delete(
                        UkawaContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = ukawaOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case UKAWA:
                rowsUpdated = db.update(UkawaContract.UkawaEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LOCATION:
                rowsUpdated = db.update(UkawaContract.LocationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = ukawaOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UKAWA:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(UkawaContract.UkawaEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:

                return super.bulkInsert(uri, values);
        }
    }
}
