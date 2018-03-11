package com.rdagnelli.energeye.entity;

import java.util.ArrayList;

/**
 * Created by robbo on 10/03/2018.
 */

public abstract class Fare {
    protected int id;
    protected String name;

    public int getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public abstract Double toEuro(ArrayList<Record> records);
}
