package com.example.senthilkumar.horai;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.example.senthilkumar.horai.Constant.FIRST_COLUMN;
import static com.example.senthilkumar.horai.Constant.FOURTH_COLUMN;
import static com.example.senthilkumar.horai.Constant.SECOND_COLUMN;
import static com.example.senthilkumar.horai.Constant.THIRD_COLUMN;

public class IndepthCalc {

    public ArrayList<HashMap> getList(HashMap item) {
        ArrayList<HashMap> resultList = new ArrayList<HashMap>();
        resultList.add(item);
        String[] horaiInterval = item.get(FIRST_COLUMN).toString().split("to");
        HashMap timeList = getTimeList(horaiInterval[0]);
        HashMap<Integer, String> detailList = getDetailList(item.get(SECOND_COLUMN).toString());
        HashMap matchingResult = getMatchingResult(item.get(SECOND_COLUMN).toString(),detailList);
        for (int i =0; i < 5; i++) {
            HashMap temp = new HashMap();
            temp.put(FIRST_COLUMN, timeList.get(i));
            temp.put(SECOND_COLUMN, item.get(SECOND_COLUMN));
            temp.put(THIRD_COLUMN, matchingResult.get(i));
            temp.put(FOURTH_COLUMN, detailList.get(i));
            resultList.add(temp);
        }

        return resultList;
    }

    private  HashMap getMatchingResult(String planet, HashMap<Integer, String > detailList) {
        HashMap matchingList = new HashMap();
        HashMap<String, String[]> unMatchedPlanets = getUnMatchedPlanets();
        String[] unMatchedList = unMatchedPlanets.get(planet);


        for (int i=0; i<5; i++) {
            String value = detailList.get(i);

            Set<String> set = new HashSet<String>(Arrays.asList(unMatchedList));
            if(set.contains(value)) {
                matchingList.put(i, "Bad");
            } else {
                matchingList.put(i, "Good");
            }
        }

        return  matchingList;
    }

    private HashMap getUnMatchedPlanets () {
        HashMap<String, String[]> unMatchedPlanets = new HashMap<>();
        String[]  sun = {"Venus", "Saturn"};
        String[]  moon = {"Venus", "Saturn", "Mercury"};
        String[]  mars = {"Mercury", "Saturn"};
        String[]  mercury = {"Jupitor", "Moon", "Mars"};
        String[]  jupitor = {"Venus", "Mercury"};
        String[]  venus = {"Sun", "Mercury", "Jupitor"};
        String[]  saturn = {"Mars", "Sun", "Moon"};

        unMatchedPlanets.put("Sun", sun);
        unMatchedPlanets.put("Moon", moon);
        unMatchedPlanets.put("Mars", mars);
        unMatchedPlanets.put("Mercury", mercury);
        unMatchedPlanets.put("Jupitor", jupitor);
        unMatchedPlanets.put("Venus", venus);
        unMatchedPlanets.put("Saturn", saturn);

        return  unMatchedPlanets;
    }

    private HashMap  getDetailList (String planet) {
        HashMap<Integer, String> horaiPlanets = new HashMap();
        String[] planets = {"Sun", "Moon", "Mars", "Mercury", "Jupitor", "Venus", "Saturn"};

        int size = planets.length;
        int matchIndex = 0;
        for (int i=0; i<size; i++)
        {
            if(horaiPlanets.size() > 4) {
                return  horaiPlanets;
            } else if (planet.equalsIgnoreCase(planets[i]) | (matchIndex !=0)) {
                horaiPlanets.put(matchIndex, planets[i]);
                matchIndex++;
            }
        }

        for (int j=0; j < 5; j++) {
            int planetCount = horaiPlanets.size();
            if (planetCount < 5) {
                horaiPlanets.put(matchIndex,planets[j]);
                matchIndex++;
            }
        }
        return horaiPlanets;
    }

    private HashMap getTimeList (String sTime) {

        DateFormat sdf = new SimpleDateFormat("hh.mm");
        Date startTime = null;
        Calendar calendar = Calendar.getInstance();


        try {
            startTime = sdf.parse(sTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(startTime);

        HashMap temp = new HashMap();
        for (int i =0; i < 6; i++) {
            String  result = String.format("%,02d",calendar.getTime().getHours()) + "." +
                    String.format("%,02d", calendar.getTime().getMinutes());
            temp.put(i, result );
            calendar.add(Calendar.MINUTE, 12);
        }

        HashMap temp1 = new HashMap();
        for (int j =0; j < 5; j++) {
            String timeFormat = temp.get(j) + " to " + temp.get(j+1);
            temp1.put(j, timeFormat);
        }

        return  temp1;
    }
}
