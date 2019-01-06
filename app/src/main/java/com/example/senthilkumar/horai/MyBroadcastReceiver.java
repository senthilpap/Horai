package com.example.senthilkumar.horai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

public class MyBroadcastReceiver extends BroadcastReceiver {
    GetHoraiList getHoraiList = new GetHoraiList();
    CurrentHoraiItem currentHoraiItem = new CurrentHoraiItem();
    ArrayList<HashMap> list;
    public static boolean isServiceRunning = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Date today = new Date();
        if (today.getHours() == 6){
            sunRaiseNotification(context);
        }
        list = getHoraiList.get(context);
        notification(context);
        isServiceRunning = true;
    }

    private void sunRaiseNotification (Context context) {
        Date today = new Date();
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String sunraiseString = pref.getString("sunraise", null);
        Date lastsunraise = new Date(sunraiseString);
        if (lastsunraise.getDay() != today.getDay()) {
            new WeatherApi().execute(context.getApplicationContext());
            Toast.makeText(context, "Sun raise broadcast", Toast.LENGTH_LONG).show();
        }
    }

    private void notification (Context context){
        HashMap item =  list.get(currentHoraiItem.selectItemBasedOnTime());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "hhhh");
        String horaiResult = item.get("Third").toString();
        if ( horaiResult.equalsIgnoreCase("Good")) {
            mBuilder.setSmallIcon(R.drawable.ic_stat_arrow_upward);
        } else if ( horaiResult.equalsIgnoreCase("Bad")) {
            mBuilder.setSmallIcon(R.drawable.ic_stat_arrow_downward);
        } else {
            mBuilder.setSmallIcon(R.drawable.ic_stat_arrow_forward);
        }
        mBuilder.setContentTitle("Horai Time");
        String displayText = item.get("First") + " " + item.get("Second")+ " " + item.get("Third");
        mBuilder.setContentText(displayText);
        mBuilder.setPriority(PRIORITY_DEFAULT);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, mBuilder.build());
    }


}
