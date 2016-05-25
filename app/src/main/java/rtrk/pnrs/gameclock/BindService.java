package rtrk.pnrs.gameclock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by nora on 5/24/2016.
 */

public class BindService extends Service {

    private Binder mBinder;

    @Override
    public IBinder onBind(Intent intent) {

        if (mBinder == null) {
            mBinder = new Binder();
        }
        return (IBinder) mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBinder.stop();

        mBinder = null;
        return super.onUnbind(intent);
    }
}
