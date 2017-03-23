package com.dark.webprog26.worktastengine.engine.managers;


import android.util.Log;

import com.dark.webprog26.worktastengine.engine.Answer;

import java.util.List;

/**
 * Created by webpr on 23.03.2017.
 */

public class CorrectnessManager {

    private static final String TAG = "CorrectnessManager";

    private List<Answer> mAnswers;
    private int mSize;

    public CorrectnessManager(List<Answer> mAnswers) {
        this.mAnswers = mAnswers;
        this.mSize = mAnswers.size();
    }

    public boolean[] getNeedHelpIndexes(){
        boolean[] incorrectnessArray = new boolean[mSize];
        if(isAnyAnswerCorrect(mAnswers)){
            for(int i = 0; i < mSize; i++){
                incorrectnessArray[i] = false;
            }
            Log.i(TAG, "All th answers are correct");
            return incorrectnessArray;
        } else if(isNumberOfIncorrectAnswersGreaterThanOne(mAnswers) || areThereEqualCorrectQuestions(mAnswers)){
            for(int i = 0; i < mSize; i++){
                if(mAnswers.get(i).getPoints() == 0){
                    incorrectnessArray[i] = true;
                } else {
                    incorrectnessArray[i] = false;
                }
            }
            return incorrectnessArray;
        } else {
            int correctAnswerIndex = getCorrectAnswerIndex(mAnswers);

            for(int i = 0; i < mSize; i++){
                if(i == correctAnswerIndex){
                    incorrectnessArray[i] = false;
                } else {
                    incorrectnessArray[i] = true;
                }
            }
            return incorrectnessArray;
        }
    }

    private boolean isAnyAnswerCorrect(List<Answer> answers){
        double firstAnswerPoints = answers.get(0).getPoints();

        for(Answer answer: answers){
            if(answer.getPoints() != firstAnswerPoints){
                return false;
            }
        }
        return true;
    }

    private int getCorrectAnswerIndex(List<Answer> answers){
        int correctAnswerIndex = 0;
        double firstAnswerPoints = answers.get(0).getPoints();

        for(int i = 0; i < mSize; i++){
            if(mAnswers.get(i).getPoints() > firstAnswerPoints){
                correctAnswerIndex = i;
            }
        }
        Log.i(TAG, "correct answer index is " + correctAnswerIndex);

        return correctAnswerIndex;
    }

    private boolean isNumberOfIncorrectAnswersGreaterThanOne(List<Answer> answers){
        if(isAnyAnswerCorrect(answers)){
            return false;
        }

        int incorrectQuestionsNumber = 0;
        for(Answer answer: answers){
            if(answer.getPoints() == 0){
                incorrectQuestionsNumber++;
            }
        }
        return incorrectQuestionsNumber > 1;
    }

    private boolean areThereEqualCorrectQuestions(List<Answer> answers){

        int numberOfEqualCorrectAnswers = 0;

        if(isAnyAnswerCorrect(answers)){
            return false;
        }

        int firstCorrectIndex = 0;
        double correctPointsNum = 0;

        for(int i = 0; i < mSize; i++){
            if(mAnswers.get(i).getPoints() > 0){
                firstCorrectIndex = i;
                correctPointsNum = mAnswers.get(firstCorrectIndex).getPoints();
                break;
            }
        }

        for(int i = 0; i < mSize; i++){
            if(mAnswers.get(i).getPoints() == correctPointsNum){
                numberOfEqualCorrectAnswers++;
            }
        }
    return numberOfEqualCorrectAnswers > 1;
    }
}
