package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 15.03.2017.
 */

public abstract class Question {

    private String mQuestionString;
    private Answer[] mAnswers;
    private int mPoints;
    private int mAnswersNum;

    public Question(String mQuestionString, Answer[] answers, int points) {
        this.mQuestionString = mQuestionString;
        this.mAnswers = answers;
        this.mPoints = points;
    }

    protected String getQuestionString() {
        return mQuestionString;
    }

    protected Answer[] getAnswers() {
        return mAnswers;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int mPoints) {
        this.mPoints = mPoints;
    }

    public int getAnswersNum() {
        return mAnswersNum;
    }

    public void setAnswersNum(int mAnswersNum) {
        this.mAnswersNum = mAnswersNum;
    }
}
