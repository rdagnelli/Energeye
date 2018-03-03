package com.rdagnelli.energeye;

import android.support.v7.widget.RecyclerView;
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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by darkb on 01/06/2016.
 */
public class LoadRecordsStringRequest implements DaoInterface {

    ArrayList<Record> records;
    private String deviceID;
    private View view;
    private Date day;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series;

    @Override
    public StringRequest getStringRequest(ArrayList<Object> params) {
        view = (View) params.get(0);
        records = (ArrayList<Record>) params.get(1);
        day = (Date) params.get(2);
        deviceID = (String) params.get(3);
        graph = (GraphView) params.get(4);
        series = (LineGraphSeries<DataPoint>) params.get(5);


        String url = "http://www.energeye.altervista.it/loadRecords.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", response);

                if(response.equals("error")){
                }else {
                    try {
                        JSONArray objArray = new JSONArray(response);
                        for (int i = 0; i < objArray.length(); i++) {
                            JSONObject obj = objArray.getJSONObject(i);

                            String recordID = obj.getString("record_id");
                            String deviceID = obj.getString("device_id");
                            int year = obj.getInt("year");
                            int month = obj.getInt("month");
                            int day = obj.getInt("day");
                            int hour = obj.getInt("hour");
                            int minute = obj.getInt("minute");
                            int second = obj.getInt("second");
                            int consumption = obj.getInt("consumption");

                            Record currRecord = new Record(recordID, deviceID, year, month, day, hour, minute, second, consumption);
                            records.add(currRecord);
//TODO UPDATE CHART
                        }
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
                params.put("day", (String) DateFormat.format("dd", day));
                params.put("month", (String) DateFormat.format("MM", day));
                params.put("year", (String) DateFormat.format("yy", day));

                return params;
            }
        };
        return strReq;
    }
}
