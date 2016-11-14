package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

import static android.R.attr.format;

public class MainFire extends AppCompatActivity {

    private static final String LOG_TAG = "CONNECT URL";
    TextView txtDisplay;
    private Firebase displayData;
    String value, name, age, location, mkoa;
    private ListView displayListView;
    private ArrayList<String> ukawaNews = new ArrayList<>();

    final String UKAWA_BASE_URL =
            "https://ukawa-b0f1e.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fire);

        // txtDisplay = (TextView) findViewById(R.id.txt_display);
        displayListView = (ListView) findViewById(R.id.list_display);

        displayData = new Firebase(UKAWA_BASE_URL);

        mkoa = "date";

        displayData = new Firebase(getLocation(mkoa));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ukawaNews);

        displayListView.setAdapter(adapter);
        displayData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                value = dataSnapshot.getValue(String.class);
                ukawaNews.add(value);
                adapter.notifyDataSetChanged();
                //  Log.v("SUCCESS", value);

                //  Map<String, String> map = dataSnapshot.getValue(Map.class);
                // String name = map.get("Name");
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

    public String getLocation(String location) {

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //  final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_LOCATION_PARAM = "location";
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