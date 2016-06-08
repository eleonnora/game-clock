package rtrk.pnrs.gameclock;

/**
 * Created by nora on 6/8/2016.
 */
public class Gameclockl {

    public native long incrementTime(long time, long increment);

    public native long decrementTime(long time, long decrement);

    /*static{
        loadLibrary();
    }*/

}
