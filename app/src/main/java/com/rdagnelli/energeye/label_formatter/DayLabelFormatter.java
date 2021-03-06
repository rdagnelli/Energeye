package com.rdagnelli.energeye.label_formatter;

import com.jjoe64.graphview.DefaultLabelFormatter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by robbo on 16/03/2018.
 */

public class DayLabelFormatter extends DefaultLabelFormatter {
    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            String label="";
            Date date = new Date((long) value);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String h = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            if(Integer.parseInt(h)<10) h="0" + h;
            String m = String.valueOf(5*(Math.round(c.get(Calendar.MINUTE)/5)));
            if(Integer.parseInt(m)<10) m="0" + m;
            return h+":"+m;
        } else {
            return super.formatLabel(value, isValueX);
        }
    }

}
