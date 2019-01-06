package com.example.senthilkumar.horai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

public class WeatherApi extends AsyncTask<Context, Void, String> {
    Context ctx = null;
    @Override
    protected String doInBackground(Context...params) {
        ctx = params[0];
        String forecastJsonStr = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=13.09&lon=80.28&appid=99aa4af25b0452f3f84033e4e934a29b");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            try {
                Thread.currentThread().sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int responceCode = urlConnection.getResponseCode();


            InputStreamReader isw = null;
            InputStream in = null;
            if(responceCode == HttpURLConnection.HTTP_OK) {
                in = urlConnection.getInputStream();
            }

            StringBuffer buffer = new StringBuffer();
            if (in == null) {
                // Nothing to do.
                return null;
            }
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            return forecastJsonStr;
        } catch (MalformedURLException ex) {
            Log.e("httptest",Log.getStackTraceString(ex));
            return  null;
        } catch (IOException ex) {
            Log.e("httptest",Log.getStackTraceString(ex));
            return  null;
        } finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPostExecute(String weatherString) {
        super.onPostExecute(weatherString);
        JSONObject  weatherObj = null;
        try {
            weatherObj= new JSONObject(weatherString);
            JSONObject sysObj = new JSONObject(weatherObj.get("sys").toString());
            String sunraisetimestamp = sysObj.get("sunrise").toString();
            long timeStamp = Integer.parseInt(sunraisetimestamp);
            Date date1 = new Date ();
            date1.setTime((long) timeStamp * 1000);

            SharedPreferences pref = ctx.getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("sunraise", date1.toString());
            editor.commit();
            sendNotification(date1.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification (String sunraisetime) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, "sunraise");
        mBuilder.setSmallIcon(R.drawable.ic_stat_web_sunny);
        mBuilder.setContentTitle("Sun Raise Time!");
        String displayText = sunraisetime;
        mBuilder.setContentText(sunraisetime);
        mBuilder.setPriority(PRIORITY_DEFAULT);
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(1, mBuilder.build());
    }
}
