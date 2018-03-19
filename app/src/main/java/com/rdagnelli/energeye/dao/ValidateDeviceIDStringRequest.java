package com.rdagnelli.energeye.dao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.SessionHandler;
import com.rdagnelli.energeye.activities.FirstTimeActivity;
import com.rdagnelli.energeye.activities.MainActivity;
import com.rdagnelli.energeye.entity.Fare;
import com.rdagnelli.energeye.entity.FareBi;
import com.rdagnelli.energeye.entity.FareMono;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by darkb on 01/06/2016.
 */
public class ValidateDeviceIDStringRequest implements DaoInterface {

    private AppCompatActivity activity;
    private String deviceID;

    @Override
    public StringRequest getStringRequest(ArrayList<Object> params) {
        activity = (AppCompatActivity) params.get(0);
        deviceID = (String) params.get(1);


        String url = "http://www.energeye.altervista.org/validateDeviceID.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", response);

                if(response.equals("error")){
                    Toast.makeText(activity, "Codice non trovato", Toast.LENGTH_LONG).show();
                }else {
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("pref_device_id", deviceID).commit();
                    SessionHandler.device = deviceID;
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("LoadRecords", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(activity, "Si è verificato un errore nel caricamento dei dati", Toast.LENGTH_LONG).show();
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

}
