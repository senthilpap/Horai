package com.example.senthilkumar.horai;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * WidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";
    private GetHoraiList getHoraiList;
    private CurrentHoraiItem currentHoraiItem = new CurrentHoraiItem();
    private  IndepthCalc indepthCalc = new IndepthCalc();

    List<String> mCollection = new ArrayList<>();
    ArrayList<HashMap> list = null;
    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {

        mContext = context;
        getHoraiList = new GetHoraiList();
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);


        String[] timeInterval = list.get(0).get("First").toString().split(" ");
        String[]  time = timeInterval[0].split("\\.");
        Date today = new Date();
        int min = today.getMinutes();
        int result = 0;
        int hourOfDay = today.getHours();
        hourOfDay = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
        if (Integer.parseInt(time[0]) == hourOfDay) {
            result = min - Integer.parseInt(time[1]);
        } else {
            result = (60-Integer.parseInt(time[1])) + min;
        }

        int stage = 0;
        if (result <= 12) {
            stage = 1;
        } else if (result <= 24) {
            stage = 2;
        } else if (result <= 36) {
            stage = 3;
        } else if (result <= 48) {
            stage = 4;
        } else {
            stage = 5;
        }
        String timeWithPlanet = String.format("%1$-"+17+"s",list.get(position).get("First")) +
                String.format("%1$-"+10+"s", list.get(position).get("Fourth") );
        if (position == stage) {
            timeWithPlanet = timeWithPlanet + "                  " +
                    String.format("%1$-"+7+"s", list.get(position).get("Third"));
            view.setTextColor(android.R.id.text1, Color.parseColor("#ffff00"));
        } else {
            timeWithPlanet = timeWithPlanet +
                    String.format("%1$-"+7+"s", list.get(position).get("Third"));
            view.setTextColor(android.R.id.text1, Color.parseColor("#ffffff"));
        }

        view.setTextViewText(android.R.id.text1, timeWithPlanet);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        list = getHoraiList.get(mContext);
        HashMap item =  list.get(currentHoraiItem.selectItemBasedOnTime());
        list = indepthCalc.getList(item);
    }

}
