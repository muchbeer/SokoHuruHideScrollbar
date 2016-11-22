package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.drawable.ukawa;

/**
 * Created by muchbeer on 11/19/2016.
 */

public class UkawaProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private UkawaDbHelper ukawaOpenHelper;


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
                retCursor = null;
                break;
            }
            // "ukawa/*"
            case UKAWA_WITH_LOCATION: {
                retCursor = null;
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
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
