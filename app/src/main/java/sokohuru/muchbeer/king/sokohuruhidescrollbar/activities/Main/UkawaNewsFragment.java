package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

import static android.R.attr.data;

/**
 * Created by muchbeer on 11/14/2016.
 */

public class UkawaNewsFragment extends Fragment {

    TextView txtDisplay;
    private Firebase displayData;
    String value, name, age, location;
    private ListView displayListView;
    private ArrayList<String> ukawaNews = new ArrayList<>();

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

        //set Firebase variables
        displayData = new Firebase("https://ukawa-b0f1e.firebaseio.com/News");

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
                value = dataSnapshot.getValue(String.class);
                ukawaNews.add(value);
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
}
