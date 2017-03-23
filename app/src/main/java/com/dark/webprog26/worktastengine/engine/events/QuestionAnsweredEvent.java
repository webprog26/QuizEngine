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
    private final boolean mNeedsHelp;

    public QuestionAnsweredEvent(Answer answer, boolean isQuestionRequired, boolean needsHelp) {
        this.mAnswer = answer;
        this.mIsQuestionRequired = isQuestionRequired;
        this.mNeedsHelp = needsHelp;
    }

    public Answer getAnswer() {
        return mAnswer;
    }

    public boolean isIsQuestionRequired() {
        return mIsQuestionRequired;
    }

    public boolean needsHelp() {
        return mNeedsHelp;
    }
}
