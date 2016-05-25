package rtrk.pnrs.adapter;

import android.graphics.drawable.Drawable;


/**
 * Created by nora on 4/13/2016.
 */
public class Result {

    public String time1;
    public String time2;
    public String time3;

    public Drawable black;
    public Drawable white;
    public Drawable result;

    public Result(String t1, String t2, String t3, Drawable res, Drawable b, Drawable w) {
        this.time1 = t1;
        this.time2 = t2;
        this.time3 = t3;
        this.black = b;
        this.white = w;
        this.result = res;
    }
}
