// IGameClock.aidl
package rtrk.pnrs.gameclock;
import rtrk.pnrs.gameclock.IGameTimeListener;

// Declare any non-default types here with import statements

interface IGameClock {

       void start(long time, in IGameTimeListener listener);
       void setTime(long white, long black);
       void stop();
       void turn();
       long getTime(int player);
}
