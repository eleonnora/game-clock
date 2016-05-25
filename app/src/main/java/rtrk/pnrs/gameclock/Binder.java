package rtrk.pnrs.gameclock;

import android.os.RemoteException;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
/**
 * Created by nora on 5/24/2016.
 */
public class Binder extends IGameClock.Stub{

    private IGameTimeListener mListener;
    private CallbackCaller mCaller;

    public static final int WHITE = 1;
    public static final int BLACK = 2;

    private long whiteTime, blackTime;
    private boolean isWhiteTurn = false, doRun = true;

    private Handler mHandler = null;

    @Override
    public void start(long time, IGameTimeListener listener) throws RemoteException {
        mCaller = new CallbackCaller();
        whiteTime = time;
        blackTime = time;
        this.mListener = listener;
        mHandler = new Handler(Looper.getMainLooper());
        isWhiteTurn = true;
        doRun = true;
        mCaller.run();
    }

    @Override
    public void stop() {
        mHandler.removeCallbacks(mCaller);
        //mHandler = null;
        doRun = false;
    }


    @Override
    public void setTime(long white, long black){
        whiteTime = white;
        blackTime = black;
    }

    @Override
    public void turn() throws RemoteException {
        if(isWhiteTurn == false)
            isWhiteTurn = true;
        else
            isWhiteTurn = false;
    }

    @Override
    public long getTime(int player) throws RemoteException {
        if(player == WHITE)
            return whiteTime;
        else
            return blackTime;
    }


    private class CallbackCaller implements Runnable {

        private static final long PERIOD = 1L;


        @Override
        public void run() {

            if (!doRun)
                return;

                if (isWhiteTurn) {
                    if (whiteTime <= 0) {
                        try {
                            mListener.onTimesUp(1);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        whiteTime -= 1000;
                        try {
                            mListener.onTimeChange(1, whiteTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    if (blackTime <= 0) {
                        try {
                            mListener.onTimesUp(2);
                            Log.d("LOSELOSE ", "AAAAAAAAAAAA BREEEE");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        blackTime -= 1000;
                        try {
                            mListener.onTimeChange(2, blackTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                }

                mHandler.postDelayed(this, PERIOD);
            }
        }

}
