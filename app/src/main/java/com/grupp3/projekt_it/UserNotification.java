package com.grupp3.projekt_it;

/**
 * Created by Daniel on 2015-05-06.
 */
public class UserNotification {
    int id;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    String title;
    String text;

    public UserNotification(int id, int year, int month, int day, int hour, int minute, String title, String text) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
