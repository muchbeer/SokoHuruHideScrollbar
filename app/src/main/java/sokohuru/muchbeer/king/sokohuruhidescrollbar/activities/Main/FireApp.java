package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.main;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by muchbeer on 11/12/2016.
 */

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
