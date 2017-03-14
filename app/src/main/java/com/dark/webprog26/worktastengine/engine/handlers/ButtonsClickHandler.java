package com.dark.webprog26.worktastengine.engine.handlers;

import android.view.View;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswerGivenListener;

/**
 * Created by webpr on 10.03.2017.
 */

public class ButtonsClickHandler implements View.OnClickListener {

    private static final String TAG = "ClickHandler";
    private AnswerGivenListener mAnswerGivenListener;



    public ButtonsClickHandler(AnswerGivenListener answerGivenListener) {
        this.mAnswerGivenListener = answerGivenListener;
    }

    @Override
    public void onClick(View v) {
        mAnswerGivenListener.onAnswerGiven((boolean) v.getTag());
    }
}
