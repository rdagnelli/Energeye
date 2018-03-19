package com.rdagnelli.energeye.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.v4.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.activities.LoginActivity;
import com.rdagnelli.energeye.activities.MainActivity;

public class SettingsFragment extends PreferenceFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference logoutPreference = findPreference("pref_logout");
        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Sei sicuro di voler uscire da questo account?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }})
                        .setNegativeButton("Annulla", null).show();
                return false;
            }
        });

        Preference infoPreference = findPreference("pref_info");
        infoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {try {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Informazioni App");

                    alertDialog.setMessage("Versione: " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionCode
                     + "\nL'app Energeye è stata sviluppata da Roberto D'Agnelli come progetto di tesi di laurea in Informatica e Tecnologie per la Produzione di Software dell'Università degli Studi di Bari \"Aldo Moro\" nell'anno accademico 2016/17, grazie al relatore: Prof. Paolo Buono");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
                return false;
            }
        });

        EditTextPreference deviceIDPreference = (EditTextPreference) findPreference("pref_device_id");
        if(deviceIDPreference.getText()!= null) deviceIDPreference.setSummary(deviceIDPreference.getText());
        else deviceIDPreference.setSummary(R.string.touch_here_to_insert);

        SwitchPreference alarmPreference = (SwitchPreference) findPreference("pref_alarm");
        alarmPreference.setSummaryOff(R.string.deactivated);
        alarmPreference.setSummaryOn(R.string.activated);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.OnFragmentInteractionListener) {
            mListener = (SettingsFragment.OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
