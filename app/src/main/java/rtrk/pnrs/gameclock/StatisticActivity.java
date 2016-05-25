package rtrk.pnrs.gameclock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import rtrk.pnrs.adapter.Result;
import rtrk.pnrs.adapter.ResultAdapter;

public class StatisticActivity extends Activity {

    //static ResultAdapter resultAdapter;
    public ListView view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        view1 = (ListView) findViewById(R.id.listVieww);
        view1.setAdapter(MainActivity.resultAdapter);


    }
}
