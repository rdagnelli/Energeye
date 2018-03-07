package com.rdagnelli.energeye.dao;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.Record;
import com.rdagnelli.energeye.SessionHandler;
import com.sccomponents.gauges.ScLinearGauge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by darkb on 01/06/2016.
 */
public class LoadLastStringRequest implements DaoInterface {

    private View view;
    private String deviceID;
    private TextView cons;
    private TextView eur;
    private ScLinearGauge gauge;
    private GraphView graph;

    @Override
    public StringRequest getStringRequest(ArrayList<Object> params) {
        view = (View) params.get(0);
        deviceID = (String) params.get(1);
        cons = (TextView) params.get(2);
        eur = (TextView) params.get(3);
        gauge = (ScLinearGauge) params.get(4);
        graph = (GraphView) params.get(5);


        String url = "http://www.energeye.altervista.org/loadLast.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", response);
                Toast.makeText(view.getContext(), response, Toast.LENGTH_SHORT).show();
                if(response.equals("error")){
                }else {
                    try {
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


                            Record record= new Record(recordID, deviceID, year, month, day, hour, minute, second, consumption);
                            updateTextView(record);
                            updateGauge(record);
                            updateGraph(record);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("LoadLast", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(view.getContext(), "Si è verificato un errore nel caricamento dei dati", Toast.LENGTH_LONG).show();
            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", deviceID);

                return params;
            }
        };
        return strReq;
    }


    private void updateTextView(Record record) {
        double watt = (double) record.getConsumption();
        double kwatt = watt/1000;

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        String output = formatter.format(kwatt);
        cons.setText(String.valueOf(output));
    }

    private void updateGauge(Record record) {
        int percentage = 100 * record.getConsumption()/SessionHandler.MAX_CONS;
        gauge.setHighValue(percentage);
    }


    private void updateGraph(Record record) {
        if(!SessionHandler.lastRecordPrevID.equals(record.getRecordID()) && SessionHandler.dashboardSeries != null) {
            SessionHandler.dashboardSeries.appendData(new DataPoint(record.getDateTime().getTime(), record.getY()/1000), true, 2000);

        }
    }
}
