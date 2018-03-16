package com.rdagnelli.energeye.label_formatter;

import com.jjoe64.graphview.DefaultLabelFormatter;

/**
 * Created by robbo on 16/03/2018.
 */

public class YearLabelFormatter extends DefaultLabelFormatter {
    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            String label="";
            if(value==0){
                label = "Gen";
            }else if(value==1){
                label = "Feb";
            }else if(value==2){
                label = "Mar";
            }else if(value==3){
                label = "Apr";
            }else if(value==4){
                label = "Mag";
            }else if(value==5){
                label = "Giu";
            }else if(value==6){
                label = "Lug";
            }else if(value==7){
                label = "Ago";
            }else if(value==8){
                label = "Set";
            }else if(value==9){
                label = "Ott";
            }else if(value==10){
                label = "Nov";
            }else if(value==11){
                label = "Dic";
            }
            return label;
        } else {
            return super.formatLabel(value, isValueX);
        }
    }
}
