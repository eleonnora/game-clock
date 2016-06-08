package rtrk.pnrs.gameclock;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import rtrk.pnrs.adapter.Result;
import rtrk.pnrs.adapter.ResultAdapter;

public class StatisticActivity extends Activity {

    ResultAdapter resultAdapter;
    public ListView view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        view1 = (ListView) findViewById(R.id.listVieww);
        resultAdapter = new ResultAdapter(this);
        
        view1.setAdapter(resultAdapter);

        Result[] results = readResults();

        resultAdapter.update(results);
    }

    public Result[] readResults() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://rtrk.pnrs.resultprovider/results"), null, null, null,
                null);
        String[] res = new String[3];
        Result[] results = new Result[cursor.getCount()];
        int i = 0;

        cursor.moveToFirst();

        if (cursor.getCount() <= 0) {
            return null;
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            res[0] = cursor.getString(cursor.getColumnIndex("WhiteTime"));
            res[1] = cursor.getString(cursor.getColumnIndex("BlackTime"));
            res[2] = cursor.getString(cursor.getColumnIndex("Result"));

            results[i] = new Result(res[0], res[1], res[2]);

            results[i].setBlack(getResources().getDrawable(R.drawable.white_pawn));
            results[i].setWhite(getResources().getDrawable(R.drawable.black_pawn));
            if (res[2].equals("Draw"))
                results[i].setResultImg(getResources().getDrawable(R.drawable.draw));
            else if (res[2].equals("White Player Won"))
                results[i].setResultImg(getResources().getDrawable(R.drawable.white_win));

            else
                results[i].setResultImg(getResources().getDrawable(R.drawable.black_win));

            i++;
        }

        cursor.close();
        return results;
    }
}
