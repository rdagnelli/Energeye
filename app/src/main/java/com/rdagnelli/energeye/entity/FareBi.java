package com.rdagnelli.energeye.entity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by robbo on 10/03/2018.
 */

public class FareBi extends Fare {

    private Double f1;
    private Double f23;

    public FareBi(int id, String name, Double f1, Double f23){
        super.id = id;
        super.name = name;
        this.f1 = f1;
        this.f23 = f23;
    }

    /*
    Days of week are represented as:
        SUNDAY = 1
        MONDAY = 2
        TUESDAY = 3
        WEDNESDAY = 4
        THURSDAY = 5
        FRIDAY = 6
        SATURDAY = 7
     */
    @Override
    public Double toEuro(ArrayList<Record> records) {
        Double sumF1 = 0.0;
        Double sumF23 = 0.0;

        for(Record record : records){
            if(record.getDateTime().get(Calendar.DAY_OF_WEEK) > 1 && record.getDateTime().get(Calendar.DAY_OF_WEEK) < 7 &&
                record.getDateTime().get(Calendar.HOUR_OF_DAY) >= 8 &&  record.getDateTime().get(Calendar.HOUR_OF_DAY) <= 19){ //from monday to friday  8.00 to 19.00
                sumF1+= record.getConsumption();
            }else{
                sumF23+= record.getConsumption();
            }
        }
        Double result = ((sumF1 * f1) + (sumF23 * f23)) / 1000;
        return result;
    }
}
