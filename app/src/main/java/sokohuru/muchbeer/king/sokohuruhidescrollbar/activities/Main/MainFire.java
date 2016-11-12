package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

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

import java.util.ArrayList;
import java.util.Map;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

public class MainFire extends AppCompatActivity {

    TextView txtDisplay;
    private Firebase displayData;
    String value, name, age, location;
    private ListView displayListView;
    private ArrayList<String> ukawaNews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fire);

       // txtDisplay = (TextView) findViewById(R.id.txt_display);
        displayListView = (ListView) findViewById(R.id.list_display);

        displayData = new Firebase("https://ukawa-b0f1e.firebaseio.com/News");

       final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ukawaNews );

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
}
