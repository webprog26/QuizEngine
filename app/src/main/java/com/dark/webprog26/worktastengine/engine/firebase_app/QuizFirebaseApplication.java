package com.dark.webprog26.worktastengine.engine.firebase_app;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by webpr on 20.03.2017.
 */

public class QuizFirebaseApplication extends Application {

    private static FirebaseDatabase mFirebaseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Enable FirebaseDatabase to work offline
        mFirebaseDatabase.setPersistenceEnabled(true);
    }

    /**
     * Returns initialized {@link FirebaseDatabase} instance to read questions from it.
     * @return FirebaseDatabase
     */
    public static FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase;
    }
}
