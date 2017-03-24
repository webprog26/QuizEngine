package com.dark.webprog26.worktastengine.engine.managers;


import android.util.Log;

import com.dark.webprog26.worktastengine.engine.Answer;

import java.util.List;

/**
 * Created by webpr on 23.03.2017.
 */

public class CorrectnessManager {

    /**
     *
     */

    private static final String TAG = "CorrectnessManager";

    private List<Answer> mAnswers;
    private int mSize;

    public CorrectnessManager(List<Answer> mAnswers) {
        this.mAnswers = mAnswers;
        this.mSize = mAnswers.size();
    }

    public boolean[] getNeedHelpIndexes(){
        boolean[] incorrectnessArray = new boolean[mSize];
        if(isIncorrectAnswerHasPriceGreaterThanZero(mAnswers)){
            int correctAnswerIndex = getCorrectAnswerIndex(mAnswers);
            for(int i = 0; i < mSize; i++){
                if(i == correctAnswerIndex){
                    incorrectnessArray[i] = false;
                } else {
                    incorrectnessArray[i] = true;
                }
            }
            return incorrectnessArray;
        } else if(areThereOneOrMoreIncorrectAnswersWithZeroPrice(mAnswers)){
            for(int i = 0; i < mSize; i++){
                int correctAnswerIndex = getCorrectAnswerIndex(mAnswers);
                if(i != correctAnswerIndex && mAnswers.get(i).getPoints() < mAnswers.get(correctAnswerIndex).getPoints() || mAnswers.get(i).getPoints() < mAnswers.get(correctAnswerIndex).getPoints()){
                    incorrectnessArray[i] = true;
                } else {
                    incorrectnessArray[i] = false;
                }
            }
            return incorrectnessArray;
        } else {
            for(int i = 0; i < mSize; i++){
                incorrectnessArray[i] = false;
            }
            Log.i(TAG, "All th answers are correct");
            return incorrectnessArray;
        }
    }

    /**
     * Checks that all the answers have the same price
     * @param answers {@link List}
     * @return boolean
     */
    private boolean isAnyAnswerCorrect(List<Answer> answers){
        //Getting the first answer
        double firstAnswerPoints = answers.get(0).getPoints();

        for(Answer answer: answers){
            //if we got any difference, return false
            if(answer.getPoints() != firstAnswerPoints){
                return false;
            }
        }
        //Otherwise all the answers have the same price, so return true
        return true;
    }

    /**
     * Checks that all the answers has price > 0, but the are not the same
     * @param answers {@link List}
     * @return boolean
     */
    private boolean isIncorrectAnswerHasPriceGreaterThanZero(List<Answer> answers){
        if(isAnyAnswerCorrect(answers) || !areAllTheAnswersGreaterThanZero(answers)){
            return false;
        }
        return true;
    }

    /**
     * Checks that all the answers have price > 0
      * @param answers {@link List}
     * @return boolean
     */
    private boolean areAllTheAnswersGreaterThanZero(List<Answer> answers){
        for(Answer answer: answers){
            if(answer.getPoints() == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks that one or more answers has their price = 0 and one more answers has their price > 0;
     * @param answers {@link List}
     * @return boolean
     */
    private boolean areThereOneOrMoreIncorrectAnswersWithZeroPrice(List<Answer> answers){
        if(isAnyAnswerCorrect(answers) || areAllTheAnswersGreaterThanZero(answers)){
            return false;
        }

        int zeroPriceAnswersCount = 0;
        int nonZeroPriceAnswersCount = 0;

        for(Answer answer: answers){
            if(answer.getPoints() == 0){
                zeroPriceAnswersCount++;
            } else {
                nonZeroPriceAnswersCount++;
            }
        }
        return zeroPriceAnswersCount != 0 && nonZeroPriceAnswersCount != 0;
    }

    /**
     * Finds index of the Answer with the price greater than others
     * @param answers {@link List}
     * @return int
     */
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
}
