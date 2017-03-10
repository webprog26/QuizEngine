package com.dark.webprog26.worktastengine.engine.handlers;

import android.view.View;
import android.widget.Button;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswerGivenListener;

/**
 * Created by webpr on 10.03.2017.
 */

public class ButtonsClickHandler implements View.OnClickListener {

    private static final String TAG = "ClickHandler";
    private AnswerGivenListener mAnswerGivenListener;

    private Answer[] mAnswers;
    private Button[] mButtons;

    public ButtonsClickHandler(Answer[] mAnswers, Button[] buttons, AnswerGivenListener answerGivenListener) {
        this.mAnswers = mAnswers;
        this.mButtons = buttons;
        this.mAnswerGivenListener = answerGivenListener;

        for(int i = 0; i < mButtons.length; i++){
            mButtons[i].setText(mAnswers[i].getAnswerText());
            mButtons[i].setTag(mAnswers[i].isCorrect());
            mButtons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        mAnswerGivenListener.onAnswerGiven((boolean) v.getTag());
    }
}
