package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

import static android.R.attr.data;

/**
 * Created by muchbeer on 11/16/2016.
 */

public class UkawaNewsFragment2 extends Fragment {

    private static final String LOG_TAG = "CONNECT URL";
    TextView txtDisplay;
    private Firebase displayData;
    String value, name, age, location, mkoa;
    private ListView displayListView;
    private ArrayList<String> ukawaNews = new ArrayList<>();

    ArrayList<String> addValueToArray = new ArrayList<String>();
    String[] valueToResult, taketoMainThread;

    private ArrayAdapter<String> ukawaAdapter;

    final String UKAWA_BASE_URL =
            "https://ukawa-b0f1e.firebaseio.com/?location=date/";
//   "https://ukawa-b0f1e.firebaseio.com/?location=date/News";
    public UkawaNewsFragment2(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            UkawaNewsFragment2.FetchUkawaTask weatherTask = new UkawaNewsFragment2.FetchUkawaTask();
            weatherTask.execute("place");
           // Toast.makeText(getActivity(), "Now I can reach the menu", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_new, container, false);

// Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Mon 6/23?- Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        ukawaAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_ukawa, // The name of the layout ID.
                        R.id.list_item_ukawa_textview, // The ID of the textview to populate.
                        weekForecast);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_ukawa);
        listView.setAdapter(ukawaAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ukawaNews = ukawaAdapter.getItem(position);
                Toast.makeText(getActivity(), ukawaNews, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    private class FetchUkawaTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = UkawaNewsFragment2.FetchUkawaTask.class.getSimpleName();

  private String getUkawaDataFromJson(String[] path_url_location)
                {
     //       JSONObject forecastJson = new JSONObject(forecastJsonStr);

      // Construct the URL for the OpenWeatherMap query
      // Possible parameters are avaiable at OWM's forecast API page, at
      // http://openweathermap.org/API#forecast
      //  final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
      final String QUERY_LOCATION_PARAM = "kinyerezi";
      //   final String CHANGE_URL_STRING = "mode";
      //  final String UNITS_PARAM = "units";
      //  final String DAYS_PARAM = "cnt";

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
        protected String[] doInBackground(String... params) {
             displayData = new Firebase(getUkawaDataFromJson(params));
          //  params = getUkawaDataFromJson(params);

            displayData.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    //  Map<String, String> map = dataSnapshot.getValue(Map.class);
                    // value = map.get("name");
                    Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();

                    // String name = map.get("Name");
                    //  value = dataSnapshot.child("News").getKey();
                    //   value = dataSnapshot.getValue(String.class);

                    value = newPost.get("name").toString();
                    age = newPost.get("age").toString();
                  //  ukawaNews.add("Name:>  " + value+ "  age:> " +age);
                  //  newsAdapter.notifyDataSetChanged();
                    Log.v("SUCCESS", "This is to show I know things: " + value);

                    addValueToArray.add(value);
                  valueToResult = new String[addValueToArray.size()];

                    addValueToArray.toArray(valueToResult);
                //    a.toArray(myArray);


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



            //Paste the damn
           // Log.v(LOG_TAG, "Forecast string: " + value);

            // This will only happen if there was an error getting or parsing the forecast.

            return valueToResult;
        }


        @Override
        protected void onPostExecute(String[] result) {
           // result = value;
//            result[0] = value;
          //  Toast.makeText(getActivity(), "POSTEXECUTE: "+ valueToResult[1], Toast.LENGTH_LONG).show();

//            Log.v("POST EXECUTE", valueToResult[1]);
            if (result != null) {
                Toast.makeText(getActivity(), "You have pass onPostExecute: ", Toast.LENGTH_LONG).show();

                Log.v("onPostExecute", result[0]);
                ukawaAdapter.clear();
                for(String displayNew : result) {
                    ukawaAdapter.add(displayNew);
                }
                // New data is back from the server.  Hooray!
            }
        }


    }
}
