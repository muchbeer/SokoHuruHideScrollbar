package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.sync;

import android.content.Intent;
import android.os.IBinder;
import android.app.Service;

import java.net.Authenticator;

/**
 * Created by muchbeer on 1/20/2017.
 */

public class UkawaAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private UkawaAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new UkawaAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}