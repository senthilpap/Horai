package com.example.senthilkumar.horai;

import java.util.Date;

public class CurrentHoraiItem {
    public int selectItemBasedOnTime () {
        Date date = new Date();
        int hours = date.getHours();
        int min = date.getMinutes();
        Date sunraise = GetHoraiList.lastsunraise;
        int sunraiseMin = 0;
        int temp = hours - 5;
        if (sunraise != null) {
            sunraiseMin = sunraise.getMinutes();
            if (sunraiseMin >= min & temp != 0){
                temp = temp -1;
            }
        }


        switch (temp) {
            case 0: return 24;
            case -1: return 23;
            case -2: return 22;
            case -3: return 21;
            case -4: return 20;
            case -5: return 19;
            default: return temp;
        }
    }
}
