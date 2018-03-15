package com.rdagnelli.energeye.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.entity.Fare;
import com.rdagnelli.energeye.entity.FareBi;
import com.rdagnelli.energeye.entity.FareMono;
import com.rdagnelli.energeye.entity.Record;
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
public class LoadFaresStringRequest implements DaoInterface {

    private ArrayList<Fare> fares;
    private View view;
    private String purpose;
    private MaterialSpinner spinner;

    @Override
    public StringRequest getStringRequest(ArrayList<Object> params) {
        view = (View) params.get(0);
        purpose = (String) params.get(1);
        if(purpose.equalsIgnoreCase("SPINNER")){
            spinner = (MaterialSpinner) params.get(2);
        }

        String url = "http://www.energeye.altervista.org/loadFares.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", response);

                if(response.equals("error")){
                    Toast.makeText(view.getContext(), "Si è verificato un errore", Toast.LENGTH_LONG);
                }else {
                    try {
                        fares = new ArrayList<>();
                        JSONArray objArray = new JSONArray(response);
                        for (int i = 0; i < objArray.length(); i++) {
                            JSONObject obj = objArray.getJSONObject(i);

                            int ID = obj.getInt("ID");
                            String name = obj.getString("Name");
                            Double f0 = obj.getDouble("F0");
                            Double f1 = obj.getDouble("F1");
                            Double f23 = obj.getDouble("F23");

                            Fare currFare;
                            if(f0 == 0){ //bioraria
                                currFare = new FareBi(ID, name, f1, f23);
                            }else{ //monoraria
                                currFare = new FareMono(ID,name,f0);
                            }
                            fares.add(currFare);
                        }
                        if(purpose.equalsIgnoreCase("SPINNER")){
                            fillSpinner(fares);
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
                return params;
            }
        };
        return strReq;
    }

    private void fillSpinner(final ArrayList<Fare> fareArrayList) {

        final String[] fares = new String[fareArrayList.size()+1];
        fares[0] = view.getResources().getString(R.string.select_your_fare);
        for(int i =0; i<fareArrayList.size(); i++){
            fares[i+1] = fareArrayList.get(i).getName();
        }

        spinner.setItems(fares);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner ms, int position, long id, String item) {
                spinner.setText(item);
                if(position != 0) { //Seleziona una tariffa
                    SessionHandler.selectedFare = fareArrayList.get(position - 1);
                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("SELECTED_FARE_NAME", item);
                    editor.commit();

                    Toast.makeText(view.getContext(), "Tariffa \"" + SessionHandler.selectedFare.getName() + "\" selezionata", Toast.LENGTH_LONG).show();
                }
            }
        });

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        String savedFareName = sharedPreferences.getString("SELECTED_FARE_NAME",null);
        if(savedFareName != null && !savedFareName.equals("")){
            spinner.setText(savedFareName);
            for(Fare fare: fareArrayList){
                if(fare.getName().equals(savedFareName)){
                    SessionHandler.selectedFare = fare;
                }
            }
        }

    }




}
