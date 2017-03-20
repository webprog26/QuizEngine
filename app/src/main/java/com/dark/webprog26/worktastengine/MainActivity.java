package com.dark.webprog26.worktastengine;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dark.webprog26.worktastengine.fragments.FragmentGreetings;

public class MainActivity extends AppCompatActivity {

    private static final String GREETINGS_FRAGMENT_TAG = "greetings_fragment_tag";

    @Override
    protected void onStart() {
        super.onStart();
        //App entry point
        //Show splash
        getWindow().setBackgroundDrawableResource(R.drawable.f_screen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Hide splash
        getWindow().setBackgroundDrawableResource(R.drawable.app_bg);
        //Loading Fragment with greetings and the button to start Quiz (QuizActivity)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentGreetings fragmentGreetings = (FragmentGreetings) fragmentManager.findFragmentByTag(GREETINGS_FRAGMENT_TAG);
        if(fragmentGreetings == null){
            fragmentGreetings = new FragmentGreetings();
            fragmentManager.beginTransaction().add(R.id.activity_main, fragmentGreetings, GREETINGS_FRAGMENT_TAG).commit();
        }
    }
}
