// IBinderExample.aidl
package rtrk.pnrs.gameclock;

import rtrk.pnrs.gameclock.ICallback;

// Declare any non-default types here with import statements

interface IBinderExample {

    void start(long time, in ICallback listener);
    void setTime(long white, long black);
    void stop();
    void turn();

}
