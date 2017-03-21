package com.dark.webprog26.worktastengine.engine;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dark.webprog26.worktastengine.engine.events.GameOverEvent;
import com.dark.webprog26.worktastengine.engine.handlers.AnswersHandler;
import com.dark.webprog26.worktastengine.engine.managers.FirebaseManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by webpr on 10.03.2017.
 */

public class Quiz {

    //This class manages the quiz

    public static final int QUESTIONS_NUMBER = 4;//only 4 questions in test mode

    //Constants to save necessary data in SharedPreferences
    public static final String SAVED_QUESTION_ID = "saved_question_id";
    public static final String TOTAL_ANSWERS_GIVEN = "total_answers_given";
    public static final String CURRENT_POINTS = "answers_given";

    private static final String TAG = "Quiz_TAG";

    private long mQuestionId;
    private int totalAnswersCount;
    private double currentPointsCount;
    private TextView mQuestionTextView;
    private Button[] mButtons;
    private SharedPreferences mSharedPreferences;
    private FirebaseManager mFirebaseManager;

    public Quiz(Button[] buttons,
                TextView questionTextView, SharedPreferences sharedPreferences, FirebaseManager firebaseManager) {
        this.mQuestionTextView = questionTextView;
        this.mButtons = buttons;
        this.mSharedPreferences = sharedPreferences;
        this.mFirebaseManager = firebaseManager;

        initializeQuiz(mSharedPreferences);
    }

    /**
     * initializes the Quiz with previously saved values, If there is no saved values, initializes by default
     * @param sharedPreferences {@link SharedPreferences}
     */
    private void initializeQuiz(SharedPreferences sharedPreferences){
        setCurrentPointsCount(Double.parseDouble(sharedPreferences.getString(CURRENT_POINTS, "0")));
        setTotalAnswersCount(sharedPreferences.getInt(TOTAL_ANSWERS_GIVEN, 0));
    }

    /**
     * Runs due to {@link com.dark.webprog26.worktastengine.QuizActivity} lifecycle onPause() callback
     * calls saveStats() method
     */
    public void pause(){
       saveStats();
    }

    /**
     * Runs due to {@link com.dark.webprog26.worktastengine.QuizActivity} lifecycle onResume() callback.
     * checks does game has next question via hasNextQuestion() method and based on the value it returns
     * updates quiz by calling update() method or ends the game by calling gameOver() method
     * @param question {@link Question}
     */
    public void resume(Question question){
        if(!hasNextQuestion()){
            gameOver();
        } else {
            update(question);
        }
    }

    /**
     * Update questions and answers variants
     */
    private void update(Question question){
        this.mQuestionId = question.getId();
        List<Answer> answers = question.getAnswers();
        AnswersHandler answersHandler = new AnswersHandler(answers);
        for (Answer answer: answers){
            Log.i(TAG, answer.getAnswerText());
        }
        mQuestionTextView.setText(question.getQuestionString());
        for(int i = 0; i < question.getAnswersNum(); i++){
            Answer answer = answers.get(i);
                mButtons[i].setText(answer.getAnswerText());
                mButtons[i].setTag(i);
                mButtons[i].setOnClickListener(answersHandler);
            if(mButtons[i].getVisibility() == View.INVISIBLE){
                mButtons[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public int getTotalAnswersCount() {
        return totalAnswersCount;
    }

    public void setTotalAnswersCount(int totalAnswersCount) {
        this.totalAnswersCount = totalAnswersCount;
    }

    public double getCurrentPointsCount() {
        return currentPointsCount;
    }

    public void setCurrentPointsCount(double currentPointsCount) {
        this.currentPointsCount = currentPointsCount;
    }

    /**
     * Checks does game has next question by comparing totalAnswersCount with QUESTIONS_NUMBER
     * @return boolean
     */
    public boolean hasNextQuestion(){
        return totalAnswersCount < QUESTIONS_NUMBER;
    }

    /**
     * Saves values needed fro restart/resuming the quiz in {@link SharedPreferences}
     */
    private void saveStats(){
        mSharedPreferences.edit().putLong(SAVED_QUESTION_ID, mQuestionId).apply();
        mSharedPreferences.edit().putInt(TOTAL_ANSWERS_GIVEN, totalAnswersCount).apply();
        mSharedPreferences.edit().putString(CURRENT_POINTS, String.valueOf(currentPointsCount)).apply();
    }

    /**
     * Runs new GameOverEvent()
     */
    public void gameOver(){
        EventBus.getDefault().post(new GameOverEvent());
    }

    /**
     * Resets quiz to it's default state when restarting
     */
    public void resetQuiz(){
        setTotalAnswersCount(0);
        setCurrentPointsCount(0);
        saveStats();
        mFirebaseManager.getNextQuestion(0);
    }
}
