package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by muchbeer on 1/26/2017.
 */

public class UkawaSyncService  extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static UkawaSyncAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new UkawaSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}
