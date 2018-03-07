package com.rdagnelli.energeye.dao;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.Record;
import com.rdagnelli.energeye.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by darkb on 01/06/2016.
 */
public class LoadRecordsDayStringRequest implements DaoInterface {

    ArrayList<Record> records;
    private String deviceID;
    private View view;
    private Date date;
    private GraphView graph;

    @Override
    public StringRequest getStringRequest(ArrayList<Object> params) {
        view = (View) params.get(0);
        date = (Date) params.get(1);
        deviceID = (String) params.get(2);
        graph = (GraphView) params.get(3);


        String url = "http://www.energeye.altervista.org/loadRecordsDay.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", response);

                if(response.equals("error")){
                    Toast.makeText(view.getContext(), "Si è verificato un errore", Toast.LENGTH_LONG);
                }else {
                    try {
                        records = new ArrayList<>();
                        JSONArray objArray = new JSONArray(response);
                        for (int i = 0; i < objArray.length(); i++) {
                            JSONObject obj = objArray.getJSONObject(i);

                            String recordID = obj.getString("ID");
                            String deviceID = obj.getString("Device_ID");
                            int year = obj.getInt("Year");
                            int month = obj.getInt("Month");
                            int day = obj.getInt("Day");
                            int hour = obj.getInt("Hour");
                            int minute = obj.getInt("Minute");
                            int second = obj.getInt("Second");
                            int consumption = obj.getInt("Watt");

                            Record currRecord = new Record(recordID, deviceID, year, month, day, hour, minute, second, consumption);
                            records.add(currRecord);
                        }
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
                params.put("day", (String) DateFormat.format("dd", date));
                params.put("month", (String) DateFormat.format("MM", date));
                params.put("year", (String) DateFormat.format("yy", date));

                return params;
            }
        };
        return strReq;
    }

    private void drawGraph() {

        DataPoint[] dataPoints = new DataPoint[records.size()];
        for(int i=0; i<dataPoints.length; i++){
            dataPoints[i] = new DataPoint(records.get(dataPoints.length - i -1).getDateTime().getTime(), records.get(dataPoints.length - i -1).getY()/1000);
        }
        SessionHandler.dashboardSeries = new LineGraphSeries<>(dataPoints);
        graph.addSeries( SessionHandler.dashboardSeries);


        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(view.getContext(),format));
        graph.getViewport().setMinX(dataPoints[0].getX());
        graph.getViewport().setMaxX(dataPoints[dataPoints.length-1].getX());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        graph.getGridLabelRenderer().setHumanRounding(true);

    }


}
