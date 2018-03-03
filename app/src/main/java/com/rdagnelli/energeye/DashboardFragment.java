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

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sccomponents.gauges.ScGauge;
import com.sccomponents.gauges.ScLinearGauge;
import com.sccomponents.gauges.ScNotches;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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


        setupChart(view);
        setupGauge(view);
        return view;
    }

    private void setupChart(View view) {
        GraphView graph = (GraphView) view.findViewById(R.id.chart1);

        LineGraphSeries<DataPoint> series = null;

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(),format));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

       /*//set manual x bounds to have nice steps
        graph.getViewport().setMinX(1517616000000L);
        graph.getViewport().setMaxX(d1.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
*/
        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        SessionHandler.devices.add("FH2W5PWL");
        ArrayList<Record> records = new ArrayList<>();
        Date today = Calendar.getInstance().getTime();
        ArrayList<Object> params = new ArrayList<>();
        params.add(view);
        params.add(records);
        params.add(today);
        params.add(SessionHandler.devices.get(0));
        params.add(graph);
        params.add(series);


        StringRequest stringRequest = new LoadRecordsStringRequest().getStringRequest(params);
        AppController.getInstance().addToRequestQueue(stringRequest);



       // graph.addSeries(series);
        // set date label formatter


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
        gauge.setHighValue(75);
    }
}
