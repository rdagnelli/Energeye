package com.rdagnelli.energeye.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rdagnelli.energeye.AppController;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.SessionHandler;
import com.rdagnelli.energeye.dao.LoadLastStringRequest;
import com.rdagnelli.energeye.fragments.DashboardFragment;
import com.rdagnelli.energeye.fragments.ReportFragment;
import com.rdagnelli.energeye.fragments.SettingsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_report:
                    transaction.replace(R.id.content, new ReportFragment()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.content, new DashboardFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    transaction.replace(R.id.content, new SettingsFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionHandler.device = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_device_id", null);
        if(SessionHandler.device == null){
            Intent intent = new Intent(this, FirstTimeActivity.class);
            startActivity(intent);
            finish();
        }

        Toast.makeText(this, "Bentornato " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

        SessionHandler.bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        SessionHandler.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SessionHandler.bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new DashboardFragment()).commit();



    }



}
