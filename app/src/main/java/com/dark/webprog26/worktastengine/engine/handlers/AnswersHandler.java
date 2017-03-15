package com.dark.webprog26.worktastengine.engine.handlers;

import android.view.View;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswerGivenListener;

/**
 * Created by webpr on 10.03.2017.
 */

public class AnswersHandler implements View.OnClickListener {

    private static final String TAG = "ClickHandler";

    private Answer[] mAnswers;
    private AnswerGivenListener mAnswerGivenListener;

    public AnswersHandler(Answer[] answers, AnswerGivenListener answerGivenListener) {
        this.mAnswers = answers;
        this.mAnswerGivenListener = answerGivenListener;
    }

    @Override
    public void onClick(View v) {
        mAnswerGivenListener.onAnswerGiven(getAnswer((int) v.getTag()), getPoints((int) v.getTag()));
    }

    protected boolean getAnswer(int i){
        return mAnswers[i].isCorrect();
    }

    protected int getPoints(int i){
        return mAnswers[i].getPoints();
    }
}
