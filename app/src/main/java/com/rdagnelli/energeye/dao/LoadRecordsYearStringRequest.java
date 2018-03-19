package com.rdagnelli.energeye.dao;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.SessionHandler;
import com.rdagnelli.energeye.entity.FareMono;
import com.rdagnelli.energeye.entity.Record;
import com.rdagnelli.energeye.label_formatter.YearLabelFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by darkb on 01/06/2016.
 */
public class LoadRecordsYearStringRequest implements DaoInterface {

    private ArrayList<DataPoint> dataPoints = new ArrayList<>();
    private String deviceID;
    private View view;
    private Date date;
    private GraphView graph;
    private double sumWatthF1;
    private double sumWatthF23;
    private TextView kwhTotTextView;
    private TextView eurTotTextView;
    private TextView loading;

    @Override
    public StringRequest getStringRequest(ArrayList<Object> params) {
        view = (View) params.get(0);
        date = (Date) params.get(1);
        deviceID = (String) params.get(2);
        graph = (GraphView) params.get(3);
        kwhTotTextView = (TextView) params.get(4);
        eurTotTextView = (TextView) params.get(5);
        loading = (TextView) params.get(6);

        String url= "http://www.energeye.altervista.org/getWatthYear.php";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", response);

                if(response.equals("error")){
                    Toast.makeText(view.getContext(), "Si è verificato un errore", Toast.LENGTH_LONG);
                }else {
                    try {
                        double sumWatthF1Month[] = new double[12];
                        double sumWatthF23Month[] = new double[12];
                        JSONArray objArray = new JSONArray(response);
                        for (int i = 0; i < objArray.length(); i++) {
                            JSONObject obj = objArray.getJSONObject(i);

                            int year = obj.getInt("Year");
                            int month = obj.getInt("Month");
                            int day = obj.getInt("Day");
                            int watthF1 = obj.getInt("WatthF1");
                            int watthF23 = obj.getInt("WatthF23");


                                Calendar c = Calendar.getInstance();
                                c.set(year,month,day);
                                if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY  ){
                                    sumWatthF23 += watthF1;
                                    sumWatthF23 += watthF23;
                                    sumWatthF23Month[month-1] += watthF1;
                                    sumWatthF23Month[month-1] += watthF23;
                                }else{
                                    sumWatthF1 += watthF1;
                                    sumWatthF23 += watthF23;
                                    sumWatthF1Month[month-1] += watthF1;
                                    sumWatthF23Month[month-1] += watthF23;
                                }
                        }

                        for(int i=0; i<12; i++){
                            dataPoints.add(new DataPoint(i, (sumWatthF1Month[i] + sumWatthF23Month[i])/1000d));
                        }

                        updateKWH();
                        updateEur();
                        drawGraph();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("LoadRecords", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(view.getContext(), "Si è verificato un errore nel caricamento dei dati", Toast.LENGTH_LONG).show();
            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", deviceID);
                params.put("year", (String) DateFormat.format("yy", date));

                return params;
            }
        };
        return strReq;
    }

    private void updateKWH() {
        double kwh = Math.round(((sumWatthF1+sumWatthF23)/1000)*100.0) /100.0; //two decimal digits
        kwhTotTextView.setText(new DecimalFormat("##.##").format(kwh));
    }

    private void updateEur() {
        if(SessionHandler.selectedFare != null) {
            double eur = Math.round(SessionHandler.selectedFare.toEuro(sumWatthF1, sumWatthF23) * 100.0) / 100.0; //two decimal digits
            eurTotTextView.setText(new DecimalFormat("##.##").format(eur));
        }
    }

    private void drawGraph() {
        if (dataPoints.size() > 0) {
            graph.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DataPoint[] dp = new DataPoint[dataPoints.size()];
            for (int i = 0; i < dp.length; i++) {
                dp[i] = dataPoints.get(i);
            }

            SessionHandler.reportYearSeries = new LineGraphSeries<>(dp);
            graph.addSeries(SessionHandler.reportYearSeries);

            graph.getGridLabelRenderer().setLabelFormatter(new YearLabelFormatter());

            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(11);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScalable(true);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
            graph.getGridLabelRenderer().setNumVerticalLabels(5);
            graph.getGridLabelRenderer().setHumanRounding(true);
            graph.getGridLabelRenderer().setTextSize(36f);
        }
    }

}
