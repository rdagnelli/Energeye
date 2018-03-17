package com.rdagnelli.energeye.label_formatter;

import com.jjoe64.graphview.DefaultLabelFormatter;

/**
 * Created by robbo on 16/03/2018.
 */

public class DayLabelFormatter extends DefaultLabelFormatter {
    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            String label="";

            return label;
        } else {
            return super.formatLabel(value, isValueX);
        }
    }

}
