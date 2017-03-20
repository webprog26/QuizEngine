package com.dark.webprog26.worktastengine.engine.events;

import com.dark.webprog26.worktastengine.engine.Answer;

/**
 * Created by webpr on 20.03.2017.
 */

public class QuestionAnsweredEvent {

    private final Answer mAnswer;

    public QuestionAnsweredEvent(Answer answer) {
        this.mAnswer = answer;
    }

    public Answer getAnswer() {
        return mAnswer;
    }
}
