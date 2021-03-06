package com.dark.webprog26.worktastengine.engine.handlers;

import android.util.Log;
import android.view.View;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.Question;
import com.dark.webprog26.worktastengine.engine.events.QuestionAnsweredEvent;
import com.dark.webprog26.worktastengine.engine.managers.CorrectnessManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by webpr on 10.03.2017.
 */

public class AnswersHandler implements View.OnClickListener {

    private static final String TAG = "ClickHandler";

    /**
     * Handles clicks on answers buttons, and runs {@link QuestionAnsweredEvent}
     * that contains answer chosen by the user
     */

    private List<Answer> mAnswers;
    private long mQuestionType;
    private CorrectnessManager mCorrectnessManager;

    public AnswersHandler(List<Answer> answers, long questionType) {
        this.mAnswers = answers;
        this.mQuestionType = questionType;
        mCorrectnessManager = new CorrectnessManager(mAnswers);
    }

    @Override
    public void onClick(View v) {
        int answerIndex = (int) v.getTag();
        long nextQuestionId = getAnswer(answerIndex).getNextQuestionId();
        Log.i(TAG, "next question id is " + nextQuestionId);
        EventBus.getDefault().post(new QuestionAnsweredEvent(getAnswer(answerIndex),
                                                            isQuestionRequired(mQuestionType),
                                                            mCorrectnessManager.getNeedHelpIndexes()[answerIndex],
                                                            answerIndex));
    }

    protected Answer getAnswer(int i){
        return mAnswers.get(i);
    }

    protected boolean isQuestionRequired(long type){
        return type == Question.REQUIRED_QUESTION;
    }
}
