package com.rdagnelli.energeye;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by robbo on 04/02/2018.
 */

public class SessionHandler {

    private static final SessionHandler ourInstance = new SessionHandler();

    public static SessionHandler getInstance() {
        return ourInstance;
    }

    private SessionHandler() {
    }


}
