package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by muchbeer on 11/12/2016.
 */

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       /* Firebase.setAndroidContext(this);*/

        if(!FirebaseApp.getApps(this).isEmpty()) {

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
