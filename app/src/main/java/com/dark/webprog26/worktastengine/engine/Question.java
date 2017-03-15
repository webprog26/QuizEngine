package com.dark.webprog26.worktastengine.engine;

/**
 * Created by webpr on 15.03.2017.
 */

public abstract class Question {

    public static final int FIRST_ORDER_QUESTION = 0;
    public static final int SECOND_ORDER_QUESTION = 1;

    private String mQuestionString;
    private Answer[] mAnswers;
    protected int mQuestionType;

    public Question(String mQuestionString, Answer[] answers) {
        this.mQuestionString = mQuestionString;
        this.mAnswers = answers;
        try {
            setQuestionType();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected String getQuestionString() {
        return mQuestionString;
    }

    protected Answer[] getAnswers() {
        return mAnswers;
    }

    public int getQuestionType() {
        return mQuestionType;
    }

    public void setQuestionType() throws Exception {
        int questionType = setCurrentQuestionType();
        if(questionType != FIRST_ORDER_QUESTION && questionType != SECOND_ORDER_QUESTION){
            throw new Exception("Wrong question type");
        } else {
            this.mQuestionType = questionType;
        }
    }

    protected abstract int setCurrentQuestionType();

    protected abstract boolean shallShowHelp();
}
