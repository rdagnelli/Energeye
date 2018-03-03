package com.rdagnelli.energeye;

import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

/**
 * Created by darkb on 01/06/2016.
 */
public interface DaoInterface {
    public StringRequest getStringRequest(ArrayList<Object> params);
}
