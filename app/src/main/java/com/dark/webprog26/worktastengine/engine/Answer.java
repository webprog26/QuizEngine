package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 10.03.2017.
 */

public class Answer {

    private String mAnswerText;
    private boolean isCorrect;

    public Answer(String answerText, boolean isCorrect) {
        this.mAnswerText = answerText;
        this.isCorrect = isCorrect;
    }

    public String getAnswerText() {
        return mAnswerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
