package rtrk.pnrs.gameclock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultAdapter = new ResultAdapter(this);

        timeToPlay = 900000;

        //Start service
        Intent intent = new Intent(this, BindService.class);
        if (!bindService(intent, this, Context.BIND_AUTO_CREATE)) {
            Log.d("BIND: ", "bind failed");
        }

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

    @Override
    public void onDestroy() {
        if (service != null)
            unbindService(this);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;

       /* switch (v.getId()) {
            case R.id.Start:
                startClick();

            case R.id.Statistic:
                Intent statisticIntent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(statisticIntent);

            case R.id.Setup:
                Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(setupIntent);

            case R.id.DrawWhite:
                drawWhiteClick();

            case R.id.DrawBlack:
                drawBlackClick();

            case R.id.WhitePlayer:
                whitePlayerClick();

            case R.id.BlackPlayer:
                blackPlayerClick();

            case R.id.LoseBlack:
                loseBlackClick();

            case R.id.LoseWhite:
                try {
                    loseWhiteClick();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

        }
    }*/
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

    public void startClick() {
        drawBlack = true;
        drawWhite = true;

        try {
            service.start(timeToPlay, mListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        start.setEnabled(false);
        setup.setEnabled(false);
        statistic.setEnabled(false);
        wp.setEnabled(true);
        lw.setEnabled(true);
        dw.setEnabled(true);

        blackWin.setVisibility(View.INVISIBLE);
        blackLose.setVisibility(View.INVISIBLE);
        blackDraw.setVisibility(View.INVISIBLE);
        whiteWin.setVisibility(View.INVISIBLE);
        whiteLose.setVisibility(View.INVISIBLE);
        whiteDraw.setVisibility(View.INVISIBLE);

    }

    public void drawWhiteClick() {
        wp.setEnabled(false);
        lw.setEnabled(false);
        dw.setEnabled(false);

        drawWhite = false;

        if (drawBlack) {
            bp.setEnabled(true);
            lb.setEnabled(true);
            db.setEnabled(true);

            try {
                service.turn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            setup.setEnabled(true);
            statistic.setEnabled(true);
            start.setEnabled(true);
            blackDraw.setVisibility(View.VISIBLE);
            whiteDraw.setVisibility(View.VISIBLE);
            Result res = new Result("00:01:33", "Draw", "00:05:03", getResources().getDrawable(R.drawable.draw), getResources().getDrawable(R.drawable.black_pawn), getResources().getDrawable(R.drawable.white_pawn));
            resultAdapter.add(res);

            try {
                service.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void whitePlayerClick() {
        lw.setEnabled(false);
        dw.setEnabled(false);
        lb.setEnabled(true);
        db.setEnabled(true);
        bp.setEnabled(true);
        wp.setEnabled(false);

        try {
            service.turn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loseWhiteClick() throws RemoteException {
        setup.setEnabled(true);
        statistic.setEnabled(true);
        start.setEnabled(true);
        lw.setEnabled(false);
        dw.setEnabled(false);
        lb.setEnabled(false);
        db.setEnabled(false);

        bp.setEnabled(false);
        wp.setEnabled(false);

        whiteLose.setVisibility(View.VISIBLE);
        blackWin.setVisibility(View.VISIBLE);

        Result res = new Result("00:05:24", "Black Player Win", "00:06:04", getResources().getDrawable(R.drawable.black_win),
                getResources().getDrawable(R.drawable.black_pawn), getResources().getDrawable(R.drawable.white_pawn));
        resultAdapter.add(res);

        try {
            service.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void loseBlackClick() {
        setup.setEnabled(true);
        statistic.setEnabled(true);
        start.setEnabled(true);

        lw.setEnabled(false);
        dw.setEnabled(false);
        lb.setEnabled(false);
        db.setEnabled(false);
        bp.setEnabled(false);
        wp.setEnabled(false);

        blackLose.setVisibility(View.VISIBLE);
        whiteWin.setVisibility(View.VISIBLE);

        Result res = new Result("00:04:33", "White Player Win", "00:02:41", getResources().getDrawable(R.drawable.white_win),
                getResources().getDrawable(R.drawable.black_pawn), getResources().getDrawable(R.drawable.white_pawn));
        resultAdapter.add(res);

        try {
            service.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void drawBlackClick() {
        bp.setEnabled(false);
        lb.setEnabled(false);
        db.setEnabled(false);

        drawBlack = false;

        if (drawWhite) {
            wp.setEnabled(true);
            lw.setEnabled(true);
            dw.setEnabled(true);
            try {
                service.turn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            setup.setEnabled(true);
            statistic.setEnabled(true);
            start.setEnabled(true);
            whiteDraw.setVisibility(View.VISIBLE);
            blackDraw.setVisibility(View.VISIBLE);
            Result res = new Result("00:04:33", "Draw", "00:02:41", getResources().getDrawable(R.drawable.draw),
                    getResources().getDrawable(R.drawable.black_pawn), getResources().getDrawable(R.drawable.white_pawn));
            resultAdapter.add(res);
            try {
                service.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void blackPlayerClick() {
        lw.setEnabled(true);
        dw.setEnabled(true);
        lb.setEnabled(false);
        db.setEnabled(false);
        bp.setEnabled(false);
        wp.setEnabled(true);
        try {
            service.turn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            if (player == 1) {
                //service.setTime(1,1);
                loseWhiteClick();
            } else {
                //service.setTime(1,1);
                loseBlackClick();
            }
        }
    }
}
