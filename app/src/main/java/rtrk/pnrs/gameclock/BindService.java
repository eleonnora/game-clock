package rtrk.pnrs.gameclock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by nora on 5/24/2016.
 */

public class BindService extends Service {

    private GameClock mGameclock;

    @Override
    public IBinder onBind(Intent intent) {

        if (mGameclock == null) {
            mGameclock = new GameClock();
        }
        return (IBinder) mGameclock;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mGameclock.stop();

        mGameclock = null;
        return super.onUnbind(intent);
    }
}
