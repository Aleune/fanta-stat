package com.test.romain.fanta_stat;

import android.util.Log;

/**
 * Created by Romain on 14/01/2018.
 */

public class Date {
    /*  Format: Tue May 01 18:53:09 EEST 2012  */
    private String day;
    private String month;
    private int year;
    private int hour;
    private int dayNumber;
    private int minutes;
    private int seconds;
    private String toStr;

    Date(String date){
        this.toStr = date;
        Log.d("TETTTT", date.toString());
        String[] splitDate = date.split(" ");
        this.day = splitDate[0];
        this.month = splitDate[1];
        this.dayNumber = Integer.parseInt(splitDate[2]);
        this.year = Integer.parseInt(splitDate[5]);

        String splitTime[] = splitDate[3].split(":");

        this.hour = Integer.parseInt(splitTime[0]);
        this.minutes = Integer.parseInt(splitTime[1]);
        this.seconds = Integer.parseInt(splitTime[2]);


    }



    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public int getHour() {
        return hour;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String toString(){
        return toStr;
    }
}
