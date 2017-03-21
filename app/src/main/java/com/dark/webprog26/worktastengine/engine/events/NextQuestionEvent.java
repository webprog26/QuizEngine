package com.dark.webprog26.worktastengine.engine.events;

import com.dark.webprog26.worktastengine.engine.Question;

/**
 * Created by webpr on 20.03.2017.
 */

public class NextQuestionEvent {

    /**
     * As a part of {@link com.dark.webprog26.worktastengine.engine.managers.FirebaseManager} this event takes place
     * when QuizActivity needs next question to read from FirebaseDatabase
     */

    private final Question mQuestion;

    public NextQuestionEvent(Question question) {
        this.mQuestion = question;
    }

    public Question getQuestion() {
        return mQuestion;
    }
}
