package com.rdagnelli.energeye.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rdagnelli.energeye.AppController;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.dao.LoadFaresStringRequest;
import com.rdagnelli.energeye.dao.ValidateDeviceIDStringRequest;

import java.util.ArrayList;

public class FirstTimeActivity extends AppCompatActivity {

    Button confirmButton;
    EditText deviceIDEditText;
    MaterialSpinner fareSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        confirmButton = findViewById(R.id.button_ft);
        deviceIDEditText = findViewById(R.id.device_id_ft);

        setupFareSpinner();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deviceIDEditText.getText().length() == 8) {
                    String deviceID = deviceIDEditText.getText().toString();
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(FirstTimeActivity.this);
                    params.add(deviceID);
                    StringRequest stringRequest = new ValidateDeviceIDStringRequest().getStringRequest(params);
                    AppController.getInstance().addToRequestQueue(stringRequest);
                }else{
                    Toast.makeText(FirstTimeActivity.this,"Codice errato", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void setupFareSpinner() {
        fareSpinner = findViewById(R.id.fare_ft);

        ArrayList<Object> params = new ArrayList<>();
        params.add(getWindow().getDecorView().getRootView());
        params.add("SPINNER");
        params.add(fareSpinner);
        StringRequest stringRequest = new LoadFaresStringRequest().getStringRequest(params);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {

    }
}
