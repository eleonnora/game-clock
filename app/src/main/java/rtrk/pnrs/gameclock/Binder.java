package rtrk.pnrs.gameclock;

import android.os.RemoteException;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
/**
 * Created by nora on 5/24/2016.
 */
public class Binder extends IBinderExample.Stub{

    private ICallback mCallback;
    private CallbackCaller mCaller;

    public static final int WHITE = 1;
    public static final int BLACK = 2;

    private long whiteTime, blackTime;
    private boolean isWhiteTurn = false, doRun = true;

    private Handler mHandler = null;

    @Override
    public void start(long time, ICallback callback) throws RemoteException {
        mCaller = new CallbackCaller();
        whiteTime = time;
        blackTime = time;
        this.mCallback = callback;
        mHandler = new Handler(Looper.getMainLooper());
        isWhiteTurn = true;
        doRun = true;
        mCaller.run();
    }

    @Override
    public void stop() {
        mHandler.removeCallbacks(mCaller);
        //mHandler = null;
    }

    @Override
    //Unable running!!!!!!!!!!!!!
    public void setTime(long white, long black){
        whiteTime = white;
        blackTime = black;
        doRun = false;
    }
    @Override
    public void turn() throws RemoteException {
        if(isWhiteTurn == false)
            isWhiteTurn = true;
        else
            isWhiteTurn = false;
    }


    private class CallbackCaller implements Runnable {

        private static final long PERIOD = 1L;


        @Override
        public void run() {

            if (doRun) {
                if (isWhiteTurn) {
                    if (whiteTime <= 0) {
                        try {
                            mCallback.timeEnd(1);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        whiteTime -= 1000;
                        try {
                            mCallback.timeChange(1, whiteTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    if (blackTime <= 0) {
                        try {
                            mCallback.timeEnd(2);
                            Log.d("LOSELOSE ", "AAAAAAAAAAAA BREEEE");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        blackTime -= 1000;
                        try {
                            mCallback.timeChange(2, blackTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                }

                mHandler.postDelayed(this, PERIOD);
            }
        }
    }
}
