package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.data.UkawaContract;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.model.UkawaList;

import static android.R.attr.data;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.drawable.ukawa;

/**
 * Created by muchbeer on 11/16/2016.
 */

public class UkawaNewsFragment2 extends Fragment {

    private static final String LOG_TAG = "CONNECT URL";
    TextView txtDisplay;



    private ListView displayListView;
    private ArrayList<String> ukawaNews = new ArrayList<>();

    ArrayList<String> addValueToArray = new ArrayList<String>();
    String[] valueToResult, taketoMainThread;

    private ArrayAdapter<String> ukawaAdapter;
    private Context mContext;

/*   public UkawaNewsFragment2(Context context, ArrayAdapter<String> forecastAdapter) {
        mContext = context;
       ukawaAdapter = forecastAdapter;
    }*/



    final String UKAWA_BASE_URL =
            "https://ukawa-b0f1e.firebaseio.com/?location=date/";

    final String UKAWA_BASE_URL2 = "https://ukawa-b0f1e.firebaseio.com/";
    public UkawaNewsFragment2(){
        //mContext = context;

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
           updateNews();

           // Toast.makeText(getActivity(), "Now I can reach the menu", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void updateNews() {

        String location = Utility.getPreferredLocation(getActivity());
      //  new FetchWeatherTask(getActivity(), mForecastAdapter).execute(location);

        FetchUkawaTask weatherTask = new FetchUkawaTask(getActivity(), ukawaAdapter);
        weatherTask.execute(location);
    }


    @Override
    public void onStart() {
        super.onStart();
      //  updateNews();
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

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, ukawaNews);
                startActivity(intent);
            }
        });

        addUkawaNewList();
        return rootView;
    }


    /**
     * Add new active list
     */
    public void addUkawaNewList() {
        // Get the reference to the root node in Firebase
        Firebase ref = new Firebase(UKAWA_BASE_URL2);
        // Get the string that the user entered into the EditText and make an object with it
        // We'll use "Anonymous Owner" for the owner because we don't have user accounts yet
     //   String userEnteredName = mEditTextListName.getText().toString();
        String owner = "Anonymous Owner";
        UkawaList currentList = new UkawaList();


//Adding values
       currentList.setTesting1("Doto");

        // Go to the "activeList" child node of the root node.
        // This will create the node for you if it doesn't already exist.
        // Then using the setValue menu it will serialize the ShoppingList POJO
        ref.child("activeNews").setValue(currentList);

}


    }
