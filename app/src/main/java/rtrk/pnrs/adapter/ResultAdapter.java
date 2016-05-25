package rtrk.pnrs.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rtrk.pnrs.gameclock.R;

/**
 * Created by nora on 4/13/2016.
 */
public class ResultAdapter extends BaseAdapter {

    public Context con;
    public ArrayList<Result> mList;

    public ResultAdapter(Context t) {
        mList = new ArrayList<Result>();
        con = t;
    }

    public void add(Result object) {
        mList.add(object);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater Inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = Inflater.inflate(R.layout.list_item, null);

            ImageView image1 = (ImageView) view.findViewById(R.id.imageView3);
            ImageView image2 = (ImageView) view.findViewById(R.id.imageView2);
            ImageView image3 = (ImageView) view.findViewById(R.id.imageView);
            TextView text1 = (TextView) view.findViewById(R.id.textView3);
            TextView text2 = (TextView) view.findViewById(R.id.textView2);
            TextView text3 = (TextView) view.findViewById(R.id.textView);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image1 = image1;
            viewHolder.image2 = image2;
            viewHolder.image3 = image3;
            viewHolder.text1 = text1;
            viewHolder.text2 = text2;
            viewHolder.text3 = text3;

            view.setTag(viewHolder);
        }

        Result result = (Result) getItem(i);

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.image1.setImageDrawable(result.white);
        holder.image2.setImageDrawable(result.result);
        holder.image3.setImageDrawable(result.black);
        holder.text1.setText(result.time1);
        holder.text2.setText(result.time2);
        holder.text3.setText(result.time3);

        return view;
    }
}
