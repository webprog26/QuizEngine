package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 10.03.2017.
 */

public class Answer {

    private String mAnswerText;
    private boolean isCorrect;
    private int mPoints;

    public Answer(String answerText, boolean isCorrect, int points) {
        this.mAnswerText = answerText;
        this.isCorrect = isCorrect;
        this.mPoints = points;
    }

    public String getAnswerText() {
        return mAnswerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int mPoints) {
        this.mPoints = mPoints;
    }
}
