package com.dark.webprog26.worktastengine.engine.events;

import com.dark.webprog26.worktastengine.engine.Answer;

/**
 * Created by webpr on 20.03.2017.
 */

public class QuestionAnsweredEvent {

    /**
     * This event takes place when usere answers the question. {@link Answer} instance contains next question id
     * to be used for reading next question from FirebaseDatabase
     */

    private final Answer mAnswer;
    private final boolean mIsQuestionRequired;

    public QuestionAnsweredEvent(Answer answer, boolean isQuestionRequired) {
        this.mAnswer = answer;
        this.mIsQuestionRequired = isQuestionRequired;
    }

    public Answer getAnswer() {
        return mAnswer;
    }

    public boolean isIsQuestionRequired() {
        return mIsQuestionRequired;
    }
}
