// ICallback.aidl
package rtrk.pnrs.gameclock;

// Declare any non-default types here with import statements

interface ICallback {

    void timeChange(int player, long time);
    void timeEnd(int player);
}
