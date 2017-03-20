package com.dark.webprog26.worktastengine.engine.events;

import com.dark.webprog26.worktastengine.engine.Question;

/**
 * Created by webpr on 20.03.2017.
 */

public class NextQuestionEvent {

    private final Question mQuestion;

    public NextQuestionEvent(Question question) {
        this.mQuestion = question;
    }

    public Question getQuestion() {
        return mQuestion;
    }
}
