package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by muchbeer on 11/12/2016.
 */

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       Firebase.setAndroidContext(this);

       Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);


       /* Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);

        if(!FirebaseApp.getApps(this).isEmpty()) {

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }*/
    }
}
