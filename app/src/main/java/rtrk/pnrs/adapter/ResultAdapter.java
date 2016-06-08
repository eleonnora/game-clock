package rtrk.pnrs.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
    private ImageView resources;

    public ResultAdapter(Context t) {
        mList = new ArrayList<Result>();
        con = t;
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

    public void update(Result[] results) {
        mList.clear();
        int count = 0;

        if (results != null) {
            count = results.length;
            for (int i = 0; i < count; i++) {
                mList.add(results[i]);
            }
        }
        notifyDataSetChanged();
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

        holder.image1.setImageDrawable(result.black);
        holder.image2.setImageDrawable(result.resultImg);
        holder.image3.setImageDrawable(result.white);
        holder.text1.setText(result.getWhiteTime());
        holder.text2.setText(result.getBlackTime());
        holder.text3.setText(result.getFinalResult());

        return view;
    }

    public class ViewHolder {

        public TextView text1;
        public TextView text2;
        public TextView text3;

        public ImageView image1;
        public ImageView image2;
        public ImageView image3;
    }


}
