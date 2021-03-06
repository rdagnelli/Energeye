package com.rdagnelli.energeye.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jjoe64.graphview.GraphView;
import com.rdagnelli.energeye.AppController;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.SessionHandler;
import com.rdagnelli.energeye.activities.FirstTimeActivity;
import com.rdagnelli.energeye.activities.MainActivity;
import com.rdagnelli.energeye.dao.LoadFaresStringRequest;
import com.rdagnelli.energeye.dao.LoadRecordsDayStringRequest;
import com.rdagnelli.energeye.runnable.LoadLastRunnable;
import com.sccomponents.gauges.ScGauge;
import com.sccomponents.gauges.ScLinearGauge;
import com.sccomponents.gauges.ScNotches;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String typedText="";

    private OnFragmentInteractionListener mListener;

    View view;
    ScLinearGauge gauge;
    GraphView graph;

    MaterialSpinner fareSpinner;
    LoadLastRunnable loadLastRunnable;

    TextView loadingDay;

    private Handler handler;
    private TextView dayKwhTot;
    private TextView dayEurTot;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        //Place here view.findViewById


        setupFareSpinner(view);
        setupGauge(view);
        setupGraph(view);

        return view;
    }


    @Override
    public void onResume() {
        scheduleLoadLast(view);

        super.onResume();
    }
    @Override
    public void onPause() {
        handler.removeCallbacks(loadLastRunnable); //stop handler when activity not visible
        super.onPause();
    }

    private void setupGraph(View view) {
        loadingDay = (TextView) view.findViewById(R.id.loading_dashboard);
        dayEurTot = (TextView) view.findViewById(R.id.eur_value2);
        dayKwhTot = (TextView) view.findViewById(R.id.cons_value2);
        graph = (GraphView) view.findViewById(R.id.chart1);

        Date currentDate = Calendar.getInstance().getTime();

        ArrayList<Object> params = new ArrayList<>();
        params.add(view);
        params.add(currentDate);
        params.add(SessionHandler.device);
        params.add(graph);
        params.add(dayKwhTot);
        params.add(dayEurTot);
        params.add(loadingDay);

        StringRequest stringRequest = new LoadRecordsDayStringRequest().getStringRequest(params);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public void scheduleLoadLast(final View view) {
        handler = new Handler();
        loadLastRunnable = new LoadLastRunnable(view,handler,gauge, graph);
        handler.postDelayed(loadLastRunnable, 5000);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void setupGauge(View view){
        gauge = (ScLinearGauge) view.findViewById(R.id.gauge1);

        // Remove all features
        gauge.removeAllFeatures();

        // Take in mind that when you tagged a feature after this feature inherit the principal
        // characteristic of the identifier.
        // For example in the case of the BASE_IDENTIFIER the feature notches (always) will be
        // settle as the color and stroke size settle for the base (in xml or via code).

        // Create the base notches.
        ScNotches base = (ScNotches) gauge.addFeature(ScNotches.class);
        base.setTag(ScGauge.BASE_IDENTIFIER);
        base.setCount(50);
        base.setLength(gauge.dipToPixel(18));

        // Note that I will create two progress because to one will add the blur and to the other
        // will be add the emboss effect.

        // Create the progress notches.
        ScNotches notches = (ScNotches) gauge.addFeature(ScNotches.class);
        notches.setTag(ScGauge.PROGRESS_IDENTIFIER);
        notches.setCount(50);
        notches.setLength(gauge.dipToPixel(18));

        // Set the value
        gauge.setHighValue(0);
    }

    private void setupFareSpinner(View view) {
        fareSpinner = (MaterialSpinner) view.findViewById(R.id.fare_picker_spinner_dashboard);


        ArrayList<Object> params = new ArrayList<>();
        params.add(view);
        params.add("SPINNER");
        params.add(fareSpinner);
        StringRequest stringRequest = new LoadFaresStringRequest().getStringRequest(params);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
