package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.testing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.Util;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final TextView txtname = (TextView) findViewById(R.id.ediname);
        final TextView txtaddress = (TextView) findViewById(R.id.ediAddress);
        final TextView txtdisctic = (TextView) findViewById(R.id.edidisctict);



        Firebase listActivity = new Firebase(Util.FIREBASE_URL).child("activeList");
        listActivity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               /* String name = (String) dataSnapshot.getValue();
                txtname.setText(name);*/

                TestingList testValuePOJO = dataSnapshot.getValue(TestingList.class);



                // If there was no data at the location we added the listener, then
                // shoppingList will be null.
                if (testValuePOJO != null) {
                    // If there was data, take the TextViews and set the appropriate values.
                    txtname.setText(testValuePOJO.getName());
                    txtaddress.setText(testValuePOJO.getAddress());


                    if (testValuePOJO.getDistrictz() != null) {
                        txtdisctic.setText(testValuePOJO.getDistrictzLong());
                               /* Utils.SIMPLE_DATE_FORMAT.format(
                                        new Date(shoppingList.getTimestampLastChangedLong())));*/
                    } else {
                        txtdisctic.setText("Namad");
                    }

                }
            /*    txtname.setText(testValuePOJO.getName());
                txtaddress.setText(testValuePOJO.getAddress());
*/
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
