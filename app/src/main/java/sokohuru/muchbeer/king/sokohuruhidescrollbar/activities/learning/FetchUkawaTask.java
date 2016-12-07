package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.model.UkawaList;

import static android.R.attr.description;
import static android.R.attr.value;

/**
 * Created by muchbeer on 11/25/2016.
 */

public class FetchUkawaTask extends AsyncTask<String, Void, String[]> {

    private static final String UKAWA_BASE_URL =  "https://ukawa-b0f1e.firebaseio.com/?location=date/";

    private final String LOG_TAG = FetchUkawaTask.class.getSimpleName();

    private final Context mContext;

    String locationFirebase, mbungeFirebase, diwanFirebase, cdnmFirebase, authorFirebase;

    String[] valueToResult, taketoMainThread;

    ArrayList<String> addValueToArray = new ArrayList<String>();

    private ArrayAdapter<String> ukawaAdapter;
    private DatabaseReference databaseReference;
    private Firebase displayFirebase;

    public FetchUkawaTask(Context context, ArrayAdapter<String> mukawaAdapter) {
        mContext = context;
        ukawaAdapter = mukawaAdapter;
    }


    private String getUkawaDataFromJson(String[] path_url_location)
    {

        final String QUERY_LOCATION_PARAM = "kinyerezi";


        Uri builtUri = Uri.parse(UKAWA_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_LOCATION_PARAM, path_url_location[0])
                .build();

        try {
            URL url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String changeUrlToString = builtUri.toString();

        Log.v(LOG_TAG, "Built URI " + builtUri.toString());

        // Create the request to OpenWeatherMap, and open the connection
                    /*
      for (String s : resultStrs) {
          Log.v(LOG_TAG, "Ukawa New entry: " + s);
      }
      */

        return changeUrlToString;

        //   return resultStrs;

    }
    @Override
    protected String[] doInBackground(final String... params) {


        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        final String locationQuery = params[0];

              displayFirebase = new Firebase(getUkawaDataFromJson(params));

      //  displayFirebase = new Firebase(getUkawaDataFromJson(params)).child("gd001/" +"alist");


        //  params = getUkawaDataFromJson(params);

        displayFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // You can use getValue to deserialize the data at dataSnapshot
                // into a UkawaList.
                UkawaList ukawaList = dataSnapshot.getValue(UkawaList.class);

             //   Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();


                // String name = map.get("Name");
                //  value = dataSnapshot.child("News").getKey();
              //  String  value = dataSnapshot.getValue(String.class);
            if(ukawaList !=null) {
                cdnmFirebase = ukawaList.getCdnm();
                locationFirebase = ukawaList.getLocation();
                mbungeFirebase = ukawaList.getMbunge();
                diwanFirebase = ukawaList.getDiwan();
            }

                if (ukawaList.getAuthor() != null) {

                    authorFirebase = ukawaList.getAuthorLong();
                    Log.v("SUCCESS PART 2", "This is to show am under Nested Ukawa New: " + authorFirebase);

                } else {
                    Log.v("ERROR ON UKAWA NEWS", "Try check the logs: ");

                }

                Log.v("SUCCESS PART 1", "This is to show I know things: " + locationFirebase);


                addValueToArray.add(mbungeFirebase);
                addValueToArray.add(diwanFirebase);
                addValueToArray.add(cdnmFirebase);
                addValueToArray.add(locationFirebase);
              //  addValueToArray.add(value);
                valueToResult = new String[addValueToArray.size()];

                addValueToArray.toArray(valueToResult);
                //    a.toArray(myArray);
                // Insert the location into the database.
              long locationID = addLocation(locationQuery, locationFirebase, mbungeFirebase, diwanFirebase);


                Log.v("LOCATION_SETTING", locationQuery);

                for(String addValue : addValueToArray) {
                    Log.v("ON_BACKGROUND: ", addValue);
                    // valueToResult = addValue;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        return valueToResult;
    }


    @Override
    protected void onPostExecute(String[] result) {

        if (result != null) {
            // Toast.makeText(getActivity(), "You have pass onPostExecute: ", Toast.LENGTH_LONG).show();

            Log.v("onPostExecute", result[0]);
            ukawaAdapter.clear();
            for(String displayNew : result) {
                ukawaAdapter.add(displayNew);
            }
            // New data is back from the server.  Hooray!
        }
    }

    private long addLocation(String locationSetting, String cityName, String mbunge, String diwani) {

        Log.v(LOG_TAG, "inserting " + cityName + ", with coord: " + mbunge + ", " + diwani);

        // First, check if the location with this city name exists in the db
        Cursor cursor = mContext.getContentResolver().query(
                UkawaContract.LocationEntry.CONTENT_URI,
                new String[]{UkawaContract.LocationEntry._ID},
                UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (cursor.moveToFirst()) {
            Log.v(LOG_TAG, "Found it in the database!");
            int locationIdIndex = cursor.getColumnIndex(UkawaContract.LocationEntry._ID);
            return cursor.getLong(locationIdIndex);
        } else {
            Log.v(LOG_TAG, "Didn't find it in the database, inserting now!");
            ContentValues locationValues = new ContentValues();
            locationValues.put(UkawaContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(UkawaContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(UkawaContract.LocationEntry.COLUMN_MBUNGE, mbunge);
            locationValues.put(UkawaContract.LocationEntry.COLUMN_DIWANI, diwani);

            Uri locationInsertUri = mContext.getContentResolver()
                    .insert(UkawaContract.LocationEntry.CONTENT_URI, locationValues);

            return ContentUris.parseId(locationInsertUri);
        }
    }
}

