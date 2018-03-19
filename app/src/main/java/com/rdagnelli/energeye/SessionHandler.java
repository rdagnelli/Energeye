package com.rdagnelli.energeye;

import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.entity.Fare;

import java.util.ArrayList;

/**
 * Created by robbo on 04/02/2018.
 */

public class SessionHandler {

    public static int MAX_CONS = 7200;
    public static String lastRecordPrevID ="";

    public static BottomNavigationView bottomNavigationView;
    public static LineGraphSeries<DataPoint> dashboardSeries;
    public static LineGraphSeries<DataPoint> reportDaySeries;
    public static LineGraphSeries<DataPoint> reportMonthSeries;
    public static LineGraphSeries<DataPoint> reportYearSeries;
    public static String device;
    public static Fare selectedFare;

    private static final SessionHandler ourInstance = new SessionHandler();

    public static SessionHandler getInstance() {
        return ourInstance;
    }

    private SessionHandler() {
    }


}
