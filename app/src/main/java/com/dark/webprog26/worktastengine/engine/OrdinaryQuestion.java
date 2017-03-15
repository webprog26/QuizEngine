package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 10.03.2017.
 */

public class OrdinaryQuestion extends Question {

    public OrdinaryQuestion(String mQuestionString, Answer[] answers) {
        super(mQuestionString, answers);
    }

    @Override
    protected int setCurrentQuestionType() {
        return Question.SECOND_ORDER_QUESTION;
    }
}
