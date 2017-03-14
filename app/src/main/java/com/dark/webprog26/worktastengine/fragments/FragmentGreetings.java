package com.dark.webprog26.worktastengine.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dark.webprog26.worktastengine.QuizActivity;
import com.dark.webprog26.worktastengine.R;

/**
 * Created by webpr on 14.03.2017.
 */

public class FragmentGreetings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_greetings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnBeginTest = (Button) view.findViewById(R.id.btnBeginTest);
        btnBeginTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuizActivity.class));
                getActivity().finish();
            }
        });
    }
}
