package com.dark.webprog26.worktastengine.engine;

import java.util.List;

/**
 * Created by webpr on 15.03.2017.
 */

public class Question {

    //Questions types
    public static final int REQUIRED_QUESTION = 0;
    public static final int OPTIONAL_QUESTION = 1;

    //Whatever Question instance is formed from the FirebaseDatabase just like the Answer instance, it has no public fields
    //The reason is it has List of answers, so it couldn't ne constructed automatically
    private long mId;
    private String mQuestionString;
    private List<Answer> mAnswers;
    private long mQuestionType;
    private long mAnswersNum = 0;
    private String mQuestionImageName = null;
    private boolean hasImage;

    public Question(long id, String mQuestionString, List<Answer> answers, long questionType, String questionImageName) {
        this.mId = id;
        this.mQuestionString = mQuestionString;
        this.mAnswers = answers;
        this.mQuestionImageName = questionImageName;
        this.hasImage = (questionImageName != null && !questionImageName.equalsIgnoreCase("null"));
        if(answers != null){
            this.mAnswersNum = answers.size();
        }
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getQuestionString() {
        return mQuestionString;
    }

    public void setQuestionString(String mQuestionString) {
        this.mQuestionString = mQuestionString;
    }

    public List<Answer> getAnswers() {
        return mAnswers;
    }


    public long getQuestionType() {
        return mQuestionType;
    }

    public void setQuestionType(long mQuestionType) {
        this.mQuestionType = mQuestionType;
    }

    public long getAnswersNum() {
        return mAnswersNum;
    }

    public String getQuestionImageName() {
        return mQuestionImageName;
    }


    public boolean isHasImage() {
        return hasImage;
    }

    public boolean shallShowHelp(){
        return false;
    }

    //Just for testing mode
    @Override
    public String toString() {
        return "question with id" + getId() + "\n"
                + "text " + getQuestionString() + "\n"
                + "type " + getQuestionType() + "\n"
                + "has " + getAnswersNum() + " answers" + "\n"
                + "has image " + isHasImage();
    }
}
