package com.rdagnelli.energeye;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Record {
    private String recordID;
    private String deviceID;
    private GregorianCalendar dateTime;
    private int consumption;

    public Record(String recordID, String deviceID, int year, int month, int day, int hour, int minute, int second, int consumption){
        this.recordID = recordID;
        this.deviceID = deviceID;
        this.dateTime = new GregorianCalendar(year,month,day,hour,minute,second);
        this.consumption = consumption;
    }

    public Record(String deviceID, GregorianCalendar dateTime, int consumption){
        this.deviceID = deviceID;
        this.dateTime = dateTime;
        this.consumption = consumption;
    }


    public long getTimestamp(){
        Timestamp timestamp = new Timestamp(dateTime.getTimeInMillis());
        return timestamp.getTime();
    }
    public String getDeviceID() {
        return deviceID;
    }


    public GregorianCalendar getDateTime() {
        return dateTime;
    }


    public int getConsumption() {
        return consumption;
    }

    public float getX(){
        return dateTime.get(Calendar.DAY_OF_MONTH);
    }

    public float getY(){
        return consumption;
    }

}
