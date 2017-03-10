package com.dark.webprog26.worktastengine.engine;

import java.util.Arrays;

/**
 * Created by webpr on 10.03.2017.
 */

public class Question {

    private String mQuestionString;
    private Answer[] mAnswers;

    public Question(String mQuestionString, Answer[] answers) {
        this.mQuestionString = mQuestionString;
        this.mAnswers = answers;
    }

    public String getQuestionString() {
        return mQuestionString;
    }

    public Answer[] getAnswers() {
        return mAnswers;
    }

    @Override
    public String toString() {
        return getQuestionString() + "/n" + Arrays.toString(getAnswers());
    }
}
