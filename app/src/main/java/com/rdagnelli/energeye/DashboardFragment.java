package com.rdagnelli.energeye;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sccomponents.gauges.ScGauge;
import com.sccomponents.gauges.ScLinearGauge;
import com.sccomponents.gauges.ScNotches;

import java.util.ArrayList;
import java.util.Random;


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

    private OnFragmentInteractionListener mListener;

    LineChart chart1;
    ScLinearGauge gauge;

    private long referenceTimestamp;

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
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        //Place here view.findViewById

        chart1 = (LineChart) view.findViewById(R.id.chart1);
        gauge = (ScLinearGauge) view.findViewById(R.id.gauge1);

        setupChart(chart1);
        setupGauge(gauge);
        return view;
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
            Toast.makeText(context, "Dashboard Fragment Attached", Toast.LENGTH_SHORT).show();
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


    public void setupChart(LineChart chart) {
        ArrayList<Record> records = new ArrayList<>();
        records.add(new Record("DEVICEID", 2017,1,7,0,0,0,1));
        records.add(new Record("DEVICEID", 2017,1,7,3,14,0,2));
        records.add(new Record("DEVICEID", 2017,1,7,6,21,55,2));
        records.add(new Record("DEVICEID", 2017,1,7,9,49,0,2));
        records.add(new Record("DEVICEID", 2017,1,7,12,25,0,3));
        records.add(new Record("DEVICEID", 2017,1,7,15,33,0,3));
        records.add(new Record("DEVICEID", 2017,1,7,19,45,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,19,55,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,20,5,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,20,15,33,3));
        records.add(new Record("DEVICEID", 2017,1,7,20,25,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,20,35,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,20,45,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,20,55,33,1));
        records.add(new Record("DEVICEID", 2017,1,7,21,5,33,1));
        records.add(new Record("DEVICEID", 2017,1,7,21,15,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,21,25,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,21,35,33,2));
        records.add(new Record("DEVICEID", 2017,1,7,21,45,33,3));
        records.add(new Record("DEVICEID", 2017,1,7,21,55,33,3));
        records.add(new Record("DEVICEID", 2017,1,7,22,5,33,1));
        records.add(new Record("DEVICEID", 2017,1,7,22,15,33,1));
        ArrayList<Entry> entries = addData(chart, records);
        LineDataSet set = new LineDataSet(entries, "LineDataSet");
        LineData data = new LineData(set);

        IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(referenceTimestamp);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1600000f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        MyMarkerView myMarkerView = new MyMarkerView(getContext(), R.layout.custom_marker_view, referenceTimestamp);
        chart.setMarker(myMarkerView);
        chart.setPinchZoom(true); //scaling x and y axis at the same time
        chart.setData(data);
        chart.invalidate(); //refresh
    }

    public void setupBarChart(BarChart chart) {
        ArrayList<BarEntry> entries = addData(chart, null);
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


        chart.setPinchZoom(true); //scaling x and y axis at the same time
        chart.setData(data);
        chart.setFitBars(true);
        chart.invalidate();//refresh
    }

    public ArrayList<Entry> addData(LineChart chart, ArrayList<Record> records){
        ArrayList<Entry> entries = new ArrayList<Entry>();

        referenceTimestamp = records.get(0).getTimestamp();

        for(Record record : records){
            long currTimeStamp = record.getTimestamp();
            float x  = currTimeStamp - referenceTimestamp;
            entries.add(new Entry(currTimeStamp,record.getY()));
        }

        return entries;
    }

    public ArrayList<BarEntry> addData(BarChart chart, Record[] records){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
/*        for(Record record : records){
            entries.add(new BarEntry(record.getX(),record.getY()));
        }
  */    entries.add(new BarEntry(1f, 3.2f));
        entries.add(new BarEntry(2f, 3.4f));
        entries.add(new BarEntry(30f, 3.7f));
        entries.add(new BarEntry(32f, 2f));
        entries.add(new BarEntry(50f, 1f));
        entries.add(new BarEntry(100f, 1.5f));
        entries.add(new BarEntry(364f, 1.7f));
        entries.add(new BarEntry(366f, 1f));
        return entries;
    }


    public void setupGauge(ScLinearGauge gauge){
        assert gauge != null;

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
        gauge.setHighValue(75);
    }
}
