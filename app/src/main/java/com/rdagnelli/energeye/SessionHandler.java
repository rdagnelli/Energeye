package com.rdagnelli.energeye;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by robbo on 04/02/2018.
 */

public class SessionHandler {


    public static ArrayList<String> devices = new ArrayList<>();

    private static final SessionHandler ourInstance = new SessionHandler();

    public static SessionHandler getInstance() {
        return ourInstance;
    }

    private SessionHandler() {
    }


}
