package com.dark.webprog26.worktastengine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ReferenceActivity extends AppCompatActivity {

    private static final String TAG = "ReferenceActivity";

    //Reference index to read reference from database
    public static final String REFERENCE_INDEX = "reference_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        Intent referenceIntent = getIntent();
        if(referenceIntent != null){
            int referenceIndex = referenceIntent.getIntExtra(REFERENCE_INDEX, -1);
            if(referenceIndex != -1){
                //we've got an index, load reference
                Log.i(TAG, "received index is " + referenceIndex);
            }
        }
    }
}
