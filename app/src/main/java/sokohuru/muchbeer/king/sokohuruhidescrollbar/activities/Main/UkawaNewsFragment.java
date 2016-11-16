package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

import static android.R.attr.data;

/**
 * Created by muchbeer on 11/14/2016.
 */

public class UkawaNewsFragment extends Fragment {


    private static final String LOG_TAG = "CONNECT URL";
    TextView txtDisplay;
    private Firebase displayData;
    String value, name, age, location, mkoa;
    private ListView displayListView;
    private ArrayList<String> ukawaNews = new ArrayList<>();


    final String UKAWA_BASE_URL =
            "https://ukawa-b0f1e.firebaseio.com/?location=date/";


    public UkawaNewsFragment() {
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


     //   List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.

     //   displayData = new Firebase(UKAWA_BASE_URL);
//File.separator
        mkoa = "place";

      displayData = new Firebase(getLocation(mkoa));

   // displayData = new Firebase("https://ukawa-b0f1e.firebaseio.com/?location=date/News");

      //  displayData = new Firebase("https://ukawa-b0f1e.firebaseio.com/?location=date/?kinyerezi=place");
     //   https://ukawa-b0f1e.firebaseio.com/Ukawa/News/?location=mkoa
        //set Firebase variables
       // displayData = new Firebase("https://ukawa-b0f1e.firebaseio.com/News");
        Log.v("FORWARD SLASH", "/");

        final ArrayAdapter<String> newsAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_ukawa, // The name of the layout ID.
                        R.id.list_item_ukawa_textview, // The ID of the textview to populate.
                        ukawaNews);

        View rootView = inflater.inflate(R.layout.fragment_main_new, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_ukawa);
        listView.setAdapter(newsAdapter);

        FireBaseHoldData(newsAdapter);


        return rootView;
    }


    private void FireBaseHoldData(final ArrayAdapter<String> newsAdapter) {
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
                ukawaNews.add("Name:>  " + value+ "  age:> " +age);
                newsAdapter.notifyDataSetChanged();
                //  Log.v("SUCCESS", value);
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ukawa_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getLocation(String location) {

        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        //  final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String QUERY_LOCATION_PARAM = "kinyerezi";
        //   final String CHANGE_URL_STRING = "mode";
        //  final String UNITS_PARAM = "units";
        //  final String DAYS_PARAM = "cnt";

        Uri builtUri = Uri.parse(UKAWA_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_LOCATION_PARAM, location)
                .build();

        try {
            URL url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String changeUrlToString = builtUri.toString();

        Log.v(LOG_TAG, "Built URI " + builtUri.toString());

        // Create the request to OpenWeatherMap, and open the connection

        return changeUrlToString;
    }
}
