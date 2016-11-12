package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 11/12/2016.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }
        /**
         * A placeholder fragment containing a simple view.
         */
        public static class PlaceholderFragment extends Fragment {

            public PlaceholderFragment() {
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

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

                // Now that we have some dummy forecast data, create an ArrayAdapter.
                // The ArrayAdapter will take data from a source (like our dummy forecast) and
                // use it to populate the ListView it's attached to.
                ArrayAdapter<String> forecastAdapter =
                        new ArrayAdapter<String>(
                                getActivity(), // The current context (this activity)
                                R.layout.list_item_ukawa, // The name of the layout ID.
                                R.id.list_item_ukawa_textview, // The ID of the textview to populate.
                                weekForecast);

                View rootView = inflater.inflate(R.layout.fragment_main_new, container, false);

                // Get a reference to the ListView, and attach this adapter to it.
                ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
                listView.setAdapter(forecastAdapter);


                return rootView;
            }
        }

}
