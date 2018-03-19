package com.rdagnelli.energeye.label_formatter;

import com.jjoe64.graphview.DefaultLabelFormatter;

/**
 * Created by robbo on 18/03/2018.
 */

public class MonthLabelFormatter extends DefaultLabelFormatter {
    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            return super.formatLabel(value, isValueX);
        } else {
            return super.formatLabel(value, isValueX);
        }
    }
}
