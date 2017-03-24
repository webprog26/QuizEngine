package com.dark.webprog26.worktastengine;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.dark.webprog26.worktastengine.engine.Quiz;

public class ReferenceActivity extends AppCompatActivity {

    private static final String TAG = "ReferenceActivity_TAG";

    //Reference index to read reference from database
    public static final String REFERENCE_INDEX = "reference_index";
    private static final long INCORRECT_REFERENCE_INDEX = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        Intent referenceIntent = getIntent();
        if(referenceIntent != null){
            long referenceIndex = referenceIntent.getLongExtra(REFERENCE_INDEX, INCORRECT_REFERENCE_INDEX);
            if(referenceIndex != INCORRECT_REFERENCE_INDEX){
                //we've got an index, load reference
                Log.i(TAG, "received index is " + referenceIndex);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {
            notifyQuizofShouldRetryWithCurrentQuestion();
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void notifyQuizofShouldRetryWithCurrentQuestion(){
        PreferenceManager.getDefaultSharedPreferences(ReferenceActivity.this).edit()
                .putBoolean(Quiz.IS_RETRYING, true).apply();
    }
}
