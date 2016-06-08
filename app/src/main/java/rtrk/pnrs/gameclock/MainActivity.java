package rtrk.pnrs.gameclock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import rtrk.pnrs.adapter.Result;
import rtrk.pnrs.adapter.ResultAdapter;
import rtrk.pnrs.gameclock.AnalogClockView;

public class MainActivity extends Activity implements View.OnClickListener, ServiceConnection {

    public boolean drawWhite, drawBlack = true;

    private TextView blackWin, blackLose, blackDraw;
    private TextView whiteWin, whiteLose, whiteDraw;

    public static View lw, dw, start, setup, statistic, lb, db;

    public static AnalogClockView bp, wp;

    static ResultAdapter resultAdapter;

    public static IGameClock service;
    public GameTimeListener mListener = new GameTimeListener();

    private long timeToPlay;

    private long[] times = new long[2];
    private String[] time = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultAdapter = new ResultAdapter(this);

        timeToPlay = 1800000;

        Intent intent = new Intent(this, BindService.class);
        if (!bindService(intent, this, Context.BIND_AUTO_CREATE)) {
            Log.d("BIND: ", "bind failed");
        }

        onStartSet();

        deleteResults();
    }


    @Override
    public void onDestroy() {
        if (service != null)
            unbindService(this);

        deleteResults();
        super.onDestroy();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        Result[] results = readResults();
        resultAdapter.update(results);
    }*/


    @Override
    public void onClick(View v) {

        if (v == findViewById(R.id.Start)) {
            startClick();
        } else if (v == findViewById(R.id.Statistic)) {
            Intent statisticIntent = new Intent(MainActivity.this, StatisticActivity.class);
            startActivity(statisticIntent);
        } else if (v == findViewById(R.id.Setup)) {
            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
            startActivity(setupIntent);
        } else if (v == findViewById(R.id.DrawWhite)) {
            drawWhiteClick();
        } else if (v == findViewById(R.id.WhitePlayer)) {
            whitePlayerClick();
        } else if (v == findViewById(R.id.LoseWhite)) {
            try {
                loseWhiteClick();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v == findViewById(R.id.LoseBlack)) {
            loseBlackClick();
        } else if (v == findViewById(R.id.DrawBlack)) {
            drawBlackClick();
        } else if (v == findViewById(R.id.BlackPlayer)) {
            blackPlayerClick();
        }
    }

    public void onStartSet() {

        blackWin = (TextView) findViewById(R.id.Blackw);
        blackLose = (TextView) findViewById(R.id.Blackl);
        blackDraw = (TextView) findViewById(R.id.Blackd);
        whiteWin = (TextView) findViewById(R.id.Whitew);
        whiteLose = (TextView) findViewById(R.id.Whitel);
        whiteDraw = (TextView) findViewById(R.id.Whited);

        blackWin.setVisibility(View.INVISIBLE);
        blackLose.setVisibility(View.INVISIBLE);
        blackDraw.setVisibility(View.INVISIBLE);
        whiteWin.setVisibility(View.INVISIBLE);
        whiteLose.setVisibility(View.INVISIBLE);
        whiteDraw.setVisibility(View.INVISIBLE);

        lw = findViewById(R.id.LoseWhite);
        dw = findViewById(R.id.DrawWhite);
        wp = (AnalogClockView) findViewById(R.id.WhitePlayer);
        wp.mPlayer = "White";
        bp = (AnalogClockView) findViewById(R.id.BlackPlayer);
        bp.mPlayer = "Black";

        bp.setEnabled(false);
        wp.setEnabled(false);

        start = findViewById(R.id.Start);
        setup = findViewById(R.id.Setup);
        statistic = findViewById(R.id.Statistic);
        lb = findViewById(R.id.LoseBlack);
        db = findViewById(R.id.DrawBlack);

        lw.setOnClickListener(this);
        lw.setEnabled(false);
        dw.setOnClickListener(this);
        dw.setEnabled(false);

        start.setOnClickListener(this);
        setup.setOnClickListener(this);
        statistic.setOnClickListener(this);

        lb.setOnClickListener(this);
        lb.setEnabled(false);
        db.setOnClickListener(this);
        db.setEnabled(false);

        wp.setClock(12, 30);
        bp.setClock(12, 30);

    }

    public void deleteResults() {
        ContentResolver resolver = getContentResolver();
        resolver.delete(Uri.parse("content://rtrk.pnrs.resultprovider/results"), null,
                null);
    }

    private void insert(Result result) {
        ContentValues values = new ContentValues();
        values.put("WhiteTime", result.whiteTime);
        values.put("BlackTime", result.blackTime);
        values.put("Result", result.finalResult);

        ContentResolver resolver = getContentResolver();
        resolver.insert(Uri.parse("content://rtrk.pnrs.resultprovider/results"), values);
    }

    public void addResults(String whoWon) {

        try {
            times[0] = service.getTime(1);
            times[1] = service.getTime(2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        time = setingTimes(times);
        Result res = new Result(time[0], whoWon, time[1],
                getResources().getDrawable(R.drawable.white_pawn),
                getResources().getDrawable(R.drawable.black_pawn),
                getResources().getDrawable(R.drawable.black_win));

        insert(res);
    }

    public String[] setingTimes(long times[]) {
        String[] strings = new String[2];
        String[] ifTwoDigits = new String[3];

        int hour, min, timeSec;

        for (int i = 0; i < 2; i++) {

            hour = (int) TimeUnit.MILLISECONDS.toHours(times[i]);
            min = (int) TimeUnit.MILLISECONDS.toMinutes(times[i]);
            timeSec = (int) (TimeUnit.MILLISECONDS.toSeconds(times[i])
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(times[i])));

            if(hour < 10)
                ifTwoDigits[0] = "0" + hour;
            else
                ifTwoDigits[0]  = "" + hour;

            if(min < 10)
                ifTwoDigits[1] = "0" + min;
            else
                ifTwoDigits[1]  = "" + min;

            if(timeSec < 10)
                ifTwoDigits[2] = "0" + timeSec;
            else
                ifTwoDigits[2]  = "" + timeSec;

            strings[i] = ifTwoDigits[0] + ":" + ifTwoDigits[1] + ":" + ifTwoDigits[2];

        }

        return strings;
    }

    public void startClick() {
        drawBlack = true;
        drawWhite = true;

        try {
            service.start(timeToPlay, mListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        setOptions(false);
        setWhiteButtons(true);

        blackWin.setVisibility(View.INVISIBLE);
        blackLose.setVisibility(View.INVISIBLE);
        blackDraw.setVisibility(View.INVISIBLE);
        whiteWin.setVisibility(View.INVISIBLE);
        whiteLose.setVisibility(View.INVISIBLE);
        whiteDraw.setVisibility(View.INVISIBLE);

    }

    public void drawWhiteClick() {
        setWhiteButtons(false);

        drawWhite = false;

        if (drawBlack) {
            setBlackButtons(true);

            try {
                service.turn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            setOptions(true);
            blackDraw.setVisibility(View.VISIBLE);
            whiteDraw.setVisibility(View.VISIBLE);

            addResults("Draw");

            try {
                service.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            wp.setClock(12, 30);
            bp.setClock(12, 30);
        }
    }

    public static void whitePlayerClick() {
        setWhiteButtons(false);
        setBlackButtons(true);

        try {
            service.turn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loseWhiteClick() throws RemoteException {
        setOptions(true);
        setWhiteButtons(false);
        setBlackButtons(false);
        whiteLose.setVisibility(View.VISIBLE);
        blackWin.setVisibility(View.VISIBLE);

        addResults("Black Player Win");

        try {
            service.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        wp.setClock(12, 30);
        bp.setClock(12, 30);

    }

    public void loseBlackClick() {
        setOptions(true);
        setBlackButtons(false);
        setWhiteButtons(false);

        blackLose.setVisibility(View.VISIBLE);
        whiteWin.setVisibility(View.VISIBLE);

        addResults("White Player Win");

        try {
            service.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        wp.setClock(12, 30);
        bp.setClock(12, 30);

    }

    public void drawBlackClick() {
        setBlackButtons(false);
        drawBlack = false;

        if (drawWhite) {
            setWhiteButtons(true);
            try {
                service.turn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            setOptions(true);
            whiteDraw.setVisibility(View.VISIBLE);
            blackDraw.setVisibility(View.VISIBLE);

            addResults("Draw");

            try {
                service.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            wp.setClock(12, 30);
            bp.setClock(12, 30);
        }
    }

    public static void blackPlayerClick() {
        setWhiteButtons(true);
        setBlackButtons(false);
        try {
            service.turn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setOptions(boolean set) {
        start.setEnabled(set);
        setup.setEnabled(set);
        statistic.setEnabled(set);
    }

    public static void setWhiteButtons(boolean set) {
        wp.setEnabled(set);
        lw.setEnabled(set);
        dw.setEnabled(set);
    }

    public static void setBlackButtons(boolean set) {
        bp.setEnabled(set);
        lb.setEnabled(set);
        db.setEnabled(set);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MainActivity.service = IGameClock.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        service = null;
    }

    private class GameTimeListener extends IGameTimeListener.Stub {


        @Override
        public void onTimeChange(int player, long time) throws RemoteException {
            int hour, min, timeSec;
            timeSec = (int) time / 1000;
            hour = timeSec / 60 / 60;
            min = timeSec / 60;

            if (player == 1) {
                wp.setClock(hour, min);
            } else {
                bp.setClock(hour, min);
            }
        }

        @Override
        public void onTimesUp(int player) throws RemoteException {
            if (player == 1)
                loseWhiteClick();
            else
                loseBlackClick();
        }
    }
}
