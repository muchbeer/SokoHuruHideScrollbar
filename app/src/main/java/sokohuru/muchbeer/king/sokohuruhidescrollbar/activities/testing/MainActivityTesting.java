package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.testing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.Util;

public class MainActivityTesting extends AppCompatActivity {


    private static final String FIREBASE_URL = "https://ukawa-b0f1e.firebaseio.com/";
    private EditText editTextName;
    private EditText editTextAddress;
    private TextView textViewPersons;
    private Button buttonSave, buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_testing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonView = (Button) findViewById(R.id.buttonView);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);

        textViewPersons = (TextView) findViewById(R.id.textViewPersons);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object
                Firebase ref = new Firebase(Util.FIREBASE_URL);

                //Getting values to store
                String name = editTextName.getText().toString();
                String address = editTextAddress.getText().toString();

                /**
                 * Set raw version of date to the ServerValue.TIMESTAMP value and save into
                 * timestampCreatedMap
                 */
                HashMap<String, Object> districtz = new HashMap<>();
                districtz.put("nestedFirebase", "Gadiel is my son");

            /* Build the shopping list */
                TestingList myCountryPeople = new TestingList(name, address,
                        districtz);


                //Creating Person object
             //  TestingList person = new TestingList();

                //Adding values
             //   person.setName(name);
               // person.setAddress(address);

                //Storing values to firebase
               // ref.child("UkawaNews").setValue(person);


                //Value event listener for realtime data update
                // Get the string that the user entered into the EditText and make an object with it
                // We'll use "Anonymous Owner" for the owner because we don't have user accounts yet
              //  String userEnteredName = mEditTextListName.getText().toString();
            //    String owner = "Anonymous Owner";
              //  ShoppingList currentList = new ShoppingList(userEnteredName, owner);

                // Go to the "activeList" child node of the root node.
                // This will create the node for you if it doesn't already exist.
                // Then using the setValue menu it will serialize the ShoppingList POJO
                ref.child("activeList").setValue(myCountryPeople);
                Toast.makeText(getApplication(), "The name will be: " + name, Toast.LENGTH_LONG).show();

            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startViewData = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(startViewData);
            }
        });
    }
}
