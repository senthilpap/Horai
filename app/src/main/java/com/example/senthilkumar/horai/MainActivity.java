package com.example.senthilkumar.horai;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap> list;
    private NotificationService notifyService;
    private  GetHoraiList getHoraiList = new GetHoraiList();
    private  CurrentHoraiItem currentHoraiItem = new CurrentHoraiItem();
    boolean mBound = false;
    private  Context mcontext;
    @Override
    public void onStart() {
        super.onStart();
        mcontext = this;
        if (!MyBroadcastReceiver.isServiceRunning) {
            Intent intent = new Intent(this, NotificationService.class);
            startService(intent);
            sunRaise();
        }
    }

    private  void loadWidget () {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                MainActivity a = (MainActivity) mcontext;
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mcontext, "New task created", Toast.LENGTH_LONG).show();

                    }
                });
                return  null;
            }
        }.execute();
    }

    private void sunRaise () {
        new WeatherApi().execute(this.getApplicationContext());
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unbindService(mConnection);
//        mBound = false;
//        stopService(new Intent(getBaseContext(), NotificationService.class));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String day = getHoraiList.getCurrentDay(this);
        selectDay(day);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectHoraiGridItem();
    }

    private void loadGrid (int day) {
        ListView listView = findViewById(R.id._horaiTimes);

        list = getHoraiList.get(this, day);
        listviewAdapter adapter = new listviewAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setSelector(R.drawable.selector);
        selectHoraiGridItem ();
        showDetail(listView);
//        sunRaise();
        showRaahuTime();
    }

    private  void showRaahuTime () {
        ArrayList<HashMap> raahuList = getHoraiList.get(this, R.raw.raahu);
    }

    private void sendNotification (String sunraisetime) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "raahu");
        mBuilder.setSmallIcon(R.drawable.ic_stat_web_sunny);
        mBuilder.setContentTitle("Raahu Time!");
        String displayText = sunraisetime;
        mBuilder.setContentText(sunraisetime);
        mBuilder.setPriority(PRIORITY_DEFAULT);
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
    }

    private void showDetail (ListView listView) {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos,
                                    long id) {
                Intent i=new Intent(getApplicationContext(), detail.class);
                i.putExtra("Position", pos);
                startActivity(i);
            }
        });
    }

    private void selectHoraiGridItem () {
        ListView listView = findViewById(R.id._horaiTimes);
        listView.setItemChecked(currentHoraiItem.selectItemBasedOnTime(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.days, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sunday:
                loadGrid(R.raw.sunday);
                return true;
            case R.id.monday:
                loadGrid(R.raw.monday);
                return true;
            case R.id.tuesday:
                loadGrid(R.raw.tuesday);
                return true;
            case R.id.wednesday:
                loadGrid(R.raw.wednesday);
                return true;
            case R.id.thursday:
                loadGrid(R.raw.thursday);
                return true;
            case R.id.friday:
                loadGrid(R.raw.friday);
                return true;
            case R.id.saturday:
                loadGrid(R.raw.saturday);
                return true;
            case R.id.raahu:
                loadGrid(R.raw.raahu);
                return true;
            case R.id.refreshSunRaise:
                new WeatherApi().execute(this.getApplicationContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean selectDay (String day) {
        switch (day) {
            case "sunday":
                loadGrid(R.raw.sunday);
                return true;
            case "monday":
                loadGrid(R.raw.monday);
                return true;
            case "tuesday":
                loadGrid(R.raw.tuesday);
                return true;
            case "wednesday":
                loadGrid(R.raw.wednesday);
                return true;
            case "thursday":
                loadGrid(R.raw.thursday);
                return true;
            case "friday":
                loadGrid(R.raw.friday);
                return true;
            case "saturday":
                loadGrid(R.raw.saturday);
                return true;
            default:
                loadGrid(R.raw.sunday);
                return true;
        }
    }
}
