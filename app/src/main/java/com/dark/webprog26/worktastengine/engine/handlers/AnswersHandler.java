package com.dark.webprog26.worktastengine.engine.handlers;

import android.view.View;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.events.QuestionAnsweredEvent;

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

    public AnswersHandler(List<Answer> answers) {
        this.mAnswers = answers;
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new QuestionAnsweredEvent(getAnswer((int) v.getTag())));
    }

    protected Answer getAnswer(int i){
        return mAnswers.get(i);
    }
}
