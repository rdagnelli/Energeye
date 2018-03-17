package com.rdagnelli.energeye.entity;

import java.util.ArrayList;

/**
 * Created by robbo on 10/03/2018.
 */

public class FareMono extends Fare {

    private Double f0;

    public FareMono(int id, String name, Double f0){
        super.id = id;
        super.name = name;
        this.f0 = f0;
    }

    @Override
    public Double toEuro(ArrayList<Record> records) {
        double pulses = records.size();
        return pulses / 1000 *f0;
    }

    @Override
    public Double toEuro(Record record) {
        return record.getConsumption() * f0 / 1000;
    }

    @Override
    public Double toEuro(double watthF1, double watthF23) {
        return (watthF1+watthF23)*f0/1000;
    }
}
