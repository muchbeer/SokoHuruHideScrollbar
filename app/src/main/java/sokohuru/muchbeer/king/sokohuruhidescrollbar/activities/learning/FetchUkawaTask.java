package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;

/**
 * Created by muchbeer on 11/25/2016.
 */

public class FetchUkawaTask extends AsyncTask<String, Void, String[]> {

    private static final String UKAWA_BASE_URL =  "https://ukawa-b0f1e.firebaseio.com/?location=date/";

    private final String LOG_TAG = FetchUkawaTask.class.getSimpleName();

    private final Context mContext;

    String locationFirebase, mbungeFirebase, diwanFirebase, cdnmFirebase;

    String[] valueToResult, taketoMainThread;

    ArrayList<String> addValueToArray = new ArrayList<String>();

    private ArrayAdapter<String> ukawaAdapter;
    private DatabaseReference databaseReference;

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

        //      displayData = new Firebase(getUkawaDataFromJson(params));
        //  params = getUkawaDataFromJson(params);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(getUkawaDataFromJson(params));



        /*
            FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                        getActivity(),
                        String.class,
                        R.layout.list_item_ukawa,
                       //  R.id.list_item_ukawa_textview,
                    databaseReference
                     ) {
                @Override
                protected void populateView(View v, String model, int position) {
                        TextView displayText = (TextView) v.findViewById(R.id.list_item_ukawa_textview);
                        displayText.setText(model);
                }
            };

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                   *//* Post post = dataSnapshot.getValue(Post.class);
                    System.out.println(post);*//*
                    //  Map<String, String> map = dataSnapshot.getValue(Map.class);
                    // value = map.get("name");
                    Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();

                    // String name = map.get("Name");
                    //  value = dataSnapshot.child("News").getKey();
                    //   value = dataSnapshot.getValue(String.class);

                    cdnmFirebase = newPost.get("cdnm").toString();
                    locationFirebase = newPost.get("location").toString();
                    mbungeFirebase = newPost.get("mbunge").toString();
                    diwanFirebase = newPost.get("diwan").toString();
                    //  ukawaNews.add("Name:>  " + value+ "  age:> " +age);
                    //  newsAdapter.notifyDataSetChanged();
                    Log.v("SUCCESS", "This is to show I know things: " + locationFirebase);

                    addValueToArray.add(mbungeFirebase);
                    addValueToArray.add(diwanFirebase);
                    addValueToArray.add(cdnmFirebase);
                    addValueToArray.add(locationFirebase);
                    valueToResult = new String[addValueToArray.size()];

                    addValueToArray.toArray(valueToResult);
                    //    a.toArray(myArray);
                    // Insert the location into the database.
                    // long locationID = addLocation(locationQuery, locationFirebase, mbungeFirebase, diwanFirebase);


                    Log.v("LOCATION_SETTING", locationQuery);

                    for(String addValue : addValueToArray) {
                        Log.v("ON_BACKGROUND: ", addValue);
                        // valueToResult = addValue;
                    }
       }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                        Toast.makeText(getActivity(), "The read faile because: " + databaseError.getCode(), Toast.LENGTH_LONG).show();
                }
            });*/


        databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();

                // String name = map.get("Name");
                //  value = dataSnapshot.child("News").getKey();
                //   value = dataSnapshot.getValue(String.class);

                cdnmFirebase = newPost.get("cdnm").toString();
                locationFirebase = newPost.get("location").toString();
                mbungeFirebase = newPost.get("mbunge").toString();
                diwanFirebase = newPost.get("diwan").toString();
                //  ukawaNews.add("Name:>  " + value+ "  age:> " +age);
                //  newsAdapter.notifyDataSetChanged();
                Log.v("SUCCESS", "This is to show I know things: " + locationFirebase);

                addValueToArray.add(mbungeFirebase);
                addValueToArray.add(diwanFirebase);
                addValueToArray.add(cdnmFirebase);
                addValueToArray.add(locationFirebase);
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
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
           /* displayData.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    //  Map<String, String> map = dataSnapshot.getValue(Map.class);
                    // value = map.get("name");
                    Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();

                    // String name = map.get("Name");
                    //  value = dataSnapshot.child("News").getKey();
                    //   value = dataSnapshot.getValue(String.class);

                    cdnmFirebase = newPost.get("cdnm").toString();
                    locationFirebase = newPost.get("location").toString();
                    mbungeFirebase = newPost.get("mbunge").toString();
                   diwanFirebase = newPost.get("diwan").toString();
                  //  ukawaNews.add("Name:>  " + value+ "  age:> " +age);
                  //  newsAdapter.notifyDataSetChanged();
                    Log.v("SUCCESS", "This is to show I know things: " + locationFirebase);

                    addValueToArray.add(mbungeFirebase);
                    addValueToArray.add(diwanFirebase);
                    addValueToArray.add(cdnmFirebase);
                    addValueToArray.add(locationFirebase);
                  valueToResult = new String[addValueToArray.size()];

                    addValueToArray.toArray(valueToResult);
                //    a.toArray(myArray);
                    // Insert the location into the database.
                  // long locationID = addLocation(locationQuery, locationFirebase, mbungeFirebase, diwanFirebase);


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

*/
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

