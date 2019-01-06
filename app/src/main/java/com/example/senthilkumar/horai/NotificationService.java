package com.example.senthilkumar.horai;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    HashMap item = new HashMap();
    private ArrayList<HashMap> list;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }


    @Override
    public void onCreate(){
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
        startHoraiNotification();
//        getSunRaiseTime();
    }

//    @Override
//    public void onDestroy(){
//        Log.e(TAG, "onDestroy");
////        stoptimertask();
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//        super.onDestroy();
//
//
//    }

    private void startHoraiNotification() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("android.intent.action.NOTIFICATION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(), 1000*6, pendingIntent);
    }

    private void getSunRaiseTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("GET_SUN_RAISE_TIME");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
