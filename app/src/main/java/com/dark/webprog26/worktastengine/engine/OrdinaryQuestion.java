package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 10.03.2017.
 */

public class OrdinaryQuestion extends Question {

    public OrdinaryQuestion(String mQuestionString, Answer[] answers, int points) {
        super(mQuestionString, answers, points);
    }

    @Override
    protected void setQuestionType() {
        this.mQuestionType = Question.THIRD_ORDER_QUESTION;
    }
}
