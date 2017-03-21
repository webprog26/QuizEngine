package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 10.03.2017.
 */

public class Answer {

    //Answer instance

    //Fields declared as public. It hurts the encapsulation principle,
    // but necessary to fill them with values directly from FirebaseDatabase
    public long id;
    public String mAnswerText;
    public boolean isCorrect;
    public long nextQuestionId;
    public double mPoints;

    //Default empty constructor is another FirebaseDatabase condition
    public Answer() {
    }

    public Answer(long id, String mAnswerText, double mPoints, long nextQuestionId) {
        this.id = id;
        this.mAnswerText = mAnswerText;
        this.mPoints = mPoints;
        this.nextQuestionId = nextQuestionId;
    }

    public String getAnswerText() {
        return mAnswerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNextQuestionId() {
        return nextQuestionId;
    }

    public double getPoints() {
        return mPoints;
    }

    public void setPoints(double points) {
        this.mPoints = points;
    }

    public void setNextQuestionId(long nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
    }
}
