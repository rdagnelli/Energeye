package com.rdagnelli.energeye.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.AppController;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.SessionHandler;
import com.rdagnelli.energeye.dao.LoadRecordsDayStringRequest;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabHost tabHost;
    EditText dateD;
    EditText dateMY;
    Spinner dateY;

    GraphView dayGraph;
    GraphView monthGraph;
    GraphView yearGraph;

    View view;

    private OnFragmentInteractionListener mListener;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
        view = inflater.inflate(R.layout.fragment_report, container, false);

        setupTabs(view);
        setupDatePickers(view);
        setupGraphs(view);

        return view;
    }

    private void setupGraphs(View view) {
        dayGraph = view.findViewById(R.id.day_graph);
        monthGraph = view.findViewById(R.id.month_graph);
        yearGraph = view.findViewById(R.id.year_graph);

        SessionHandler.reportDaySeries = new LineGraphSeries<>();
        SessionHandler.reportDaySeries = new LineGraphSeries<>();
        SessionHandler.reportDaySeries = new LineGraphSeries<>();





    }

    private void setupTabs(View view) {
        tabHost = (TabHost) view.findViewById(R.id.tabhost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Giorno");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Giorno");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Mese");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Mese");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Anno");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Anno");
        tabHost.addTab(spec);

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void setupDatePickers(final View view) {
        dateD = (EditText) view.findViewById(R.id.day_date);
        dateD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReportFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setAccentColor(getResources().getColor(R.color.mdtp_accent_color));
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });


        dateMY = (EditText) view.findViewById(R.id.month_date);
        dateMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getContext(), new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year, int month) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");

                        dateMY.setText(dateFormat.format(calendar.getTime()));

                        ArrayList<Object> params = new ArrayList<>();
                        params.add(view);
                        params.add(calendar.getTime());
                        params.add(SessionHandler.devices.get(0));
                        params.add(monthGraph);

                        StringRequest stringRequest = new LoadRecordsDayStringRequest().getStringRequest(params);
                        AppController.getInstance().addToRequestQueue(stringRequest);
                    }
                },
                R.style.MyDialogTheme);
                yearMonthPickerDialog.show();
            }
        });
        dateY = (Spinner) view.findViewById(R.id.year_date);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.year_array, R.layout.spinner_style);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateY.setAdapter(adapter);
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
            Toast.makeText(context, "Analysis Fragment Attached", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //Handles Date set for Day Report Fragment
    @Override
    public void onDateSet(DatePickerDialog dpd, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        dateD.setText(currentDateString);


        ArrayList<Object> params = new ArrayList<>();
        params.add(view);
        params.add(c.getTime());
        params.add(SessionHandler.devices.get(0));
        params.add(dayGraph);

        StringRequest stringRequest = new LoadRecordsDayStringRequest().getStringRequest(params);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    //Year spinner overrides
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
        if (i != 0) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.parseInt(adapterView.getItemAtPosition(i).toString()));
            ArrayList<Object> params = new ArrayList<>();
            params.add(view);
            params.add(id);
            params.add(SessionHandler.devices.get(0));
            params.add(yearGraph);

            StringRequest stringRequest = new LoadRecordsDayStringRequest().getStringRequest(params);
            AppController.getInstance().addToRequestQueue(stringRequest);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

}
