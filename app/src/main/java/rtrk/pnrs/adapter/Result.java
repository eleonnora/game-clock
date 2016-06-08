package rtrk.pnrs.adapter;

import android.graphics.drawable.Drawable;

import rtrk.pnrs.gameclock.R;

/**
 * Created by nora on 4/13/2016.
 */
public class Result {

    public String whiteTime;
    public String finalResult;
    public String blackTime;

    public Drawable white;
    public Drawable black;
    public Drawable resultImg;

    public Result(String t1, String t2, String t3) {
        this.whiteTime = t1;
        this.finalResult = t2;
        this.blackTime = t3;
    }

    public Result(String whiteTime, String finalResult, String blackTime, Drawable white, Drawable black, Drawable resultImg) {
        this.whiteTime = whiteTime;
        this.finalResult = finalResult;
        this.blackTime = blackTime;
        this.white = white;
        this.black = black;
        this.resultImg = resultImg;
    }

    public String getWhiteTime() {
        return whiteTime;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public String getBlackTime() {
        return blackTime;
    }

    public Drawable getBlack() {
        return black;
    }

    public Drawable getWhite() {
        return white;
    }

    public Drawable getResultImg() {
        return resultImg;
    }

    public void setWhite(Drawable white) {
        this.white = white;
    }

    public void setBlack(Drawable black) {
        this.black = black;
    }

    public void setResultImg(Drawable resultImg) {
        this.resultImg = resultImg;
    }
}
