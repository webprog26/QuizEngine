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

    //Constants to save necessary data in SharedPreferences
    public static final String SAVED_QUESTION_ID = "saved_question_id";
    public static final String NEXT_QUESTION_ID = "next_question_id";
    public static final String TOTAL_ANSWERS_GIVEN = "total_answers_given";
    public static final String CURRENT_POINTS = "answers_given";

    public static final String REQUIRED_QUESTIONS_PASSED = "required_questions_passed";

    public static final int BUY_FULL_VERSION_OFFER_MARKER = 10;

    private static final String TAG = "Quiz_TAG";

    private long mQuestionId;
    private long mNextQuestionId = 0;
    private int totalAnswersCount;
    private double currentPointsCount;
    private TextView mQuestionTextView;
    private Button[] mButtons;
    private SharedPreferences mSharedPreferences;
    private FirebaseManager mFirebaseManager;
    private int mRequiredQuestionsPassed;

    public Quiz(Button[] buttons,
                TextView questionTextView, SharedPreferences sharedPreferences, FirebaseManager firebaseManager) {
        this.mQuestionTextView = questionTextView;
        this.mButtons = buttons;
        this.mSharedPreferences = sharedPreferences;
        this.mFirebaseManager = firebaseManager;
        this.mRequiredQuestionsPassed = mSharedPreferences.getInt(REQUIRED_QUESTIONS_PASSED, 0);

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
            update(question);
    }

    /**
     * Update questions and answers variants
     */
    private void update(Question question){
        Log.i(TAG, "next quesrion id is " + getNextQuestionId());
//        Log.i(TAG, "total answers given " + getTotalAnswersCount());
//        Log.i(TAG, "current points number " + getCurrentPointsCount());
//        Log.i(TAG, "required questions passed " + getRequiredQuestionsPassed());
//        Log.i(TAG, "loaded in Quiz\n" + question.toString());
        setQuestionId(question.getId());//will be saved in shared preferences to continue quiz if interrupted
        long questionType = question.getQuestionType();//to recognize required or optional questions
        List<Answer> answers = question.getAnswers();//getting answers from current questions
        AnswersHandler answersHandler = new AnswersHandler(answers, questionType);//initializing answer-buttons click handler
                                                                                   // it requires answers and question type

        for (Answer answer: answers){
            Log.i(TAG, answer.getAnswerText());
        }

        mQuestionTextView.setText(question.getQuestionString());//setting question text to TextView

        //initialized answer buttons
        for(int i = 0; i < question.getAnswersNum(); i++){
            Answer answer = answers.get(i);//getting answer from list
                mButtons[i].setText(answer.getAnswerText());//setting answer text to button
                mButtons[i].setTag(i);//tag to recognize chosen answer in listener
                mButtons[i].setOnClickListener(answersHandler);//setting listener
            //questions may have different number of answers
            //"inactive" buttons should be hidden
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
     * Saves values needed fro restart/resuming the quiz in {@link SharedPreferences}
     */
    private void saveStats(){
        mSharedPreferences.edit().putLong(SAVED_QUESTION_ID, mQuestionId).apply();
        mSharedPreferences.edit().putInt(TOTAL_ANSWERS_GIVEN, totalAnswersCount).apply();
        mSharedPreferences.edit().putString(CURRENT_POINTS, String.valueOf(currentPointsCount)).apply();
        mSharedPreferences.edit().putInt(REQUIRED_QUESTIONS_PASSED, mRequiredQuestionsPassed).apply();
        mSharedPreferences.edit().putLong(NEXT_QUESTION_ID, getNextQuestionId()).apply();
    }

    /**
     * Resets quiz to it's default state when restarting
     */
    public void resetQuiz(){
        setTotalAnswersCount(0);
        setCurrentPointsCount(0);
        setRequiredQuestionsPassed(0);
        saveStats();
        mFirebaseManager.getNextQuestion(0);
    }

    public int getRequiredQuestionsPassed() {
        return mRequiredQuestionsPassed;
    }

    public void setRequiredQuestionsPassed(int mRequiredQuestionsPassed) {
        this.mRequiredQuestionsPassed = mRequiredQuestionsPassed;
    }

    public long getNextQuestionId() {
        return mNextQuestionId;
    }

    public void setNextQuestionId(long mNextQuestionId) {
        this.mNextQuestionId = mNextQuestionId;
    }

    public long getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(long questionId) {
        this.mQuestionId = questionId;
    }
}
