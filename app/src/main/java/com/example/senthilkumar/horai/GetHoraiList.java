package com.example.senthilkumar.horai;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class GetHoraiList {
    public static Date lastsunraise = null;
    private static  ArrayList<HashMap> list;

    public ArrayList<HashMap> get (Context ctx, int... weekday) {
        String day = getCurrentDay(ctx);
        if (weekday.length == 0 && (list != null) && isListAvailable (day)) {
            return list;
        }
        int id = selectDay(day);
        if (weekday.length == 1) {
            if (id != weekday[0])
                id = weekday[0];
        }
        InputStream inputStream = ctx.getResources().openRawResource(id);
        CSVFile csvFile = new CSVFile(inputStream);
        list = csvFile.read();

        if (lastsunraise == null)
            return  list;

        return changeSunRaiseTime(list, ctx);
    }

    private boolean isListAvailable (String day) {
        String planet = list.get(1).get("Second").toString();

        if (day.equalsIgnoreCase( "sunday") & planet.equalsIgnoreCase( "Sun")) {
            return  true;
        } else if (day.equalsIgnoreCase("sunday") & planet.equalsIgnoreCase("Sun")) {
            return  true;
        } else if (day.equalsIgnoreCase("monday") & planet.equalsIgnoreCase( "Moon")) {
            return  true;
        } else if (day.equalsIgnoreCase( "tuesday") & planet.equalsIgnoreCase( "Mars")) {
            return  true;
        } else if (day.equalsIgnoreCase( "wednesday") & planet.equalsIgnoreCase( "Mercury")) {
            return  true;
        } else if (day.equalsIgnoreCase( "thursday") & planet.equalsIgnoreCase( "Jupitor")) {
            return  true;
        } else if (day.equalsIgnoreCase( "friday") & planet.equalsIgnoreCase( "Venus")) {
            return  true;
        } else if (day.equalsIgnoreCase( "saturday") & planet.equalsIgnoreCase( "Saturn")) {
            return  true;
        } else {
            return  false;
        }
    }

    private ArrayList<HashMap> changeSunRaiseTime (ArrayList<HashMap> horaiList, Context ctx) {
        ArrayList<HashMap> list1 = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastsunraise);

        for (int i = 1; i < horaiList.size(); i++) {
            String timeInterval1 = convertAMPM(calendar, false);
            calendar.add(Calendar.HOUR, 1);
            String timeInterval2 = convertAMPM(calendar,true );
            horaiList.get(i).put("First", timeInterval1+timeInterval2);
        }

        return  horaiList;
    }

    public String convertAMPM(Calendar calendar, boolean isAMPM) {
        String am_pm = "" ;
        int hourOfDay = calendar.getTime().getHours();
        int min = calendar.getTime().getMinutes();

        if (isAMPM) {
            if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";
        }
        hourOfDay = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;

        return String .format("%,02d", hourOfDay) + "." +
                String .format("%,02d", min) + " "+ am_pm;
    }

    public String getCurrentDay(Context ctx){
        SharedPreferences pref = ctx.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String sunraiseString = pref.getString("sunraise", null);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        Date now = new Date();

        if (sunraiseString == null) {
            return  sdf.format(now).toString();
        } else {
            lastsunraise = new Date(sunraiseString);
        }

        String dayOfTheWeek = null;
        if (now.getHours() > lastsunraise.getHours()) {
            dayOfTheWeek = sdf.format(now);
        } else if (now.getHours() == lastsunraise.getHours()) {
            if (now.getMinutes() >= lastsunraise.getMinutes()) {
                dayOfTheWeek = sdf.format(now);
            } else {
                dayOfTheWeek = sdf.format(yesterday());
            }
        }
        else {
            dayOfTheWeek = sdf.format(yesterday());
        }

        return  dayOfTheWeek.toLowerCase();
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private int selectDay (String day) {
        switch (day) {
            case "sunday":
                return R.raw.sunday;
            case "monday":
                return R.raw.monday;
            case "tuesday":
                return R.raw.tuesday;
            case "wednesday":
                return R.raw.wednesday;
            case "thursday":
                return R.raw.thursday;
            case "friday":
                return R.raw.friday;
            case "saturday":
                return R.raw.saturday;
            default:
                return R.raw.sunday;
        }
    }
}
