package rtrk.pnrs.gameclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by nora on 4/26/2016.
 */
public class AnalogClockView extends View {

    private Paint mPaint = new Paint();
    public int mMin, mHour;
    private boolean mInd;
    public String mPlayer;

    public float radius;

    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.rgb(255, 117, 56));
        mMin = 31;
        mHour = 3;
        mInd = true;
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.rgb(255, 117, 56));
        mMin = 31;
        mHour = 3;
        mInd = true;
    }

    public AnalogClockView(Context context) {
        super(context);
        mPaint.setColor(Color.rgb(255, 117, 56));
        mMin = 31;
        mHour = 3;
        mInd = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = Math.min(getWidth(), getHeight()) / 2;

        Path mPathHour = new Path();
        Path mPathMin = new Path();

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.rgb(255, 117, 56));

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);

        mPaint.setColor(Color.rgb(255, 182, 83));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - 12, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 5, mPaint);

        //Velika kazaljka
        canvas.save();
        canvas.rotate(mMin * 6, getWidth() / 2, getHeight() / 2);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPathMin.setFillType(Path.FillType.EVEN_ODD);
        mPathMin.moveTo(getWidth() / 2, getHeight() / 2);
        mPathMin.lineTo(getWidth() / 2 + 7, getHeight() / 2 - 0.6f * radius);
        mPathMin.lineTo(getWidth() / 2, getHeight() / 2 - 0.75f * radius);
        mPathMin.lineTo(getWidth() / 2 - 7, getHeight() / 2 - 0.6f * radius);
        mPathMin.lineTo(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mPathMin, mPaint);
        canvas.restore();

        //Mala kazaljka
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPathMin.setFillType(Path.FillType.EVEN_ODD);
        canvas.rotate(mHour * 30 + (float) (mMin / 2), getWidth() / 2, getHeight() / 2);
        mPathHour.moveTo(getWidth() / 2, getHeight() / 2);
        mPathHour.lineTo(getWidth() / 2 + 7, getHeight() / 2 - 0.4f * radius);
        mPathHour.lineTo(getWidth() / 2, getHeight() / 2 - 0.55f * radius);
        mPathHour.lineTo(getWidth() / 2 - 7, getHeight() / 2 - 0.4f * radius);
        mPathHour.lineTo(getWidth() / 2, getHeight() / 2);
        canvas.drawPath(mPathHour, mPaint);
        canvas.restore();

        for (int i = 0; i <= 60; i++) {
            canvas.save();
            canvas.rotate(i * 6, getWidth() / 2, getHeight() / 2);
            if (i % 5 == 0) {
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.rotate(360 - i * 6, getWidth() / 2, 23);
                canvas.drawRect(getWidth() / 2 - 3, 20, getWidth() / 2 + 3, 26, mPaint);
                mPaint.setStrokeWidth(1);
                mPaint.setTextSize(40);
                canvas.rotate(360 + i * 6, getWidth() / 2, 23);
                canvas.rotate(360 - i * 6, getWidth() / 2, 45);
                if (i != 0)
                    canvas.drawText("" + i / 5, getWidth() / 2 - 15, 60, mPaint);
                canvas.restore();
            } else {
                canvas.rotate(360 - i * 6, getWidth() / 2, 22);
                canvas.drawRect(getWidth() / 2 - 1, 22, getWidth() / 2 + 1, 24, mPaint);
                canvas.restore();
            }
        }

        if (!this.isEnabled()) {
            mPaint.setColor(Color.BLACK);
            mPaint.setAlpha(100);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double distance;
        distance = Math.sqrt((getWidth() / 2 - event.getX()) * (getWidth() / 2 - event.getX()) + (getHeight() / 2 - event.getY()) * (getHeight() / 2 - event.getY()));

        if (this.isEnabled()) {
            if (distance <= radius) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mPlayer == "White") {
                        MainActivity.whitePlayerClick();
                        invalidate();
                    } else if (mPlayer == "Black") {
                        MainActivity.blackPlayerClick();
                        invalidate();
                    }
                    invalidate();
                }
            }
            return true;
        } else
            return true;
    }

    void setClock(int hour, int min) {
        mHour = hour;
        mMin = min;
        invalidate();
    }
}
