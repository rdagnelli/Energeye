package com.rdagnelli.energeye.runnable;

import android.os.Handler;
import android.view.View;

import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rdagnelli.energeye.AppController;
import com.rdagnelli.energeye.R;
import com.rdagnelli.energeye.SessionHandler;
import com.rdagnelli.energeye.dao.LoadLastStringRequest;
import com.sccomponents.gauges.ScLinearGauge;

import java.util.ArrayList;

/**
 * Created by robbo on 05/03/2018.
 */

public class LoadLastRunnable implements Runnable {
    View view;
    Handler handler;
    ScLinearGauge gauge;
    GraphView graph;

    public LoadLastRunnable(View view, Handler handler, ScLinearGauge gauge, GraphView graph) {
        this.view = view;
        this.handler = handler;
        this.gauge = gauge;
        this.graph = graph;
    }

    @Override
    public void run() {
        gaugeRequest(view);
        handler.postDelayed(this, 5000);
    }

    public void gaugeRequest(View view){
        ArrayList<Object> params = new ArrayList<>();
        params.add(view);
        params.add(SessionHandler.device);
        params.add(view.findViewById(R.id.cons_value1));
        params.add(view.findViewById(R.id.eur_value1));
        params.add(gauge);
        params.add(graph);

        StringRequest stringRequest = new LoadLastStringRequest().getStringRequest(params);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
