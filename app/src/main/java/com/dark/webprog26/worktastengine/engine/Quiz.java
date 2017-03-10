package com.dark.webprog26.worktastengine.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dark.webprog26.worktastengine.R;
import com.dark.webprog26.worktastengine.engine.handlers.ButtonsClickHandler;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswerGivenListener;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswersCountListener;

/**
 * Created by webpr on 10.03.2017.
 */

public class Quiz implements AnswerGivenListener {

    private static final int ANSWERS_PER_QUESTION_NUMBER = 4;
    private static final int QUESTIONS_NUMBER = 4;//only 4 questions in test mode
    private static final String SAVED_QUESTION_INDEX = "saved_question_index";
    public static final String TOTAL_ANSWERS_GIVEN = "total_answers_given";
    public static final String CORRECT_ANSWERS_GIVEN = "answers_given";

    private static final String TAG = "Quiz";

    private int mQuestionIndex;
    private int totalAnswersCount;
    private int correctAnswersCount;
    private Question[] mQuestions = new Question[QUESTIONS_NUMBER];
    private TextView mQuestionTextView;
    private Button[] mButtons;
    private SharedPreferences mSharedPreferences;
    private AnswersCountListener mAnswersCountListener;

    public Quiz(Context context,
                Button[] buttons,
                TextView questionTextView,
                AnswersCountListener answersCountListener) {
        this.mQuestionTextView = questionTextView;
        this.mButtons = buttons;
        this.mAnswersCountListener = answersCountListener;
        initializeQuiz(context);
    }

    /**
     * Initializes Quiz with questions
     * @param context {@link Context}
     */
    private void initializeQuiz(Context context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mQuestionIndex = mSharedPreferences.getInt(SAVED_QUESTION_INDEX, 0);
        totalAnswersCount = mSharedPreferences.getInt(TOTAL_ANSWERS_GIVEN, 0);
        correctAnswersCount = mSharedPreferences.getInt(CORRECT_ANSWERS_GIVEN, 0);

        String[] questionsRes = context.getResources().getStringArray(R.array.questions);
        String[] answersRes = context.getResources().getStringArray(R.array.answers);
        String[] answersVariablesRes = context.getResources().getStringArray(R.array.answers_variables);

        int z = 0;
        int i = 0;
        for(String questionString: questionsRes){
            Answer[] answers = new Answer[ANSWERS_PER_QUESTION_NUMBER];
            for(int j = 0; j < ANSWERS_PER_QUESTION_NUMBER; j++){
                answers[j] = new Answer(answersRes[z], Boolean.parseBoolean(answersVariablesRes[z]));
                Log.i(TAG, answersRes[z] + Boolean.parseBoolean(answersVariablesRes[z]));
                z++;
            }
            mQuestions[i] = new Question(questionString, answers);
            i++;
        }
    }

    public void resume(){
        update();
    }

    public void pause(){
       saveStats();
    }

    private Question getNextQuestion(){
        return mQuestions[mQuestionIndex];
    }

    /**
     * Resets quiz
     */
    public void resetQuiz(){
        mQuestionIndex = 0;
        correctAnswersCount = 0;
        totalAnswersCount = 0;
        mAnswersCountListener.updateAnswersCount(mQuestionIndex);
        mAnswersCountListener.updateCorrectAnswersCount(correctAnswersCount);
        saveStats();
        resume();
    }

    /**
     * Update questions and answers variants
     */
    private void update(){
        Question question = getNextQuestion();

        mQuestionTextView.setText(question.getQuestionString());
        Answer[] answers = question.getAnswers();
        ButtonsClickHandler clickHandler = new ButtonsClickHandler(answers, mButtons, this);
        for(int i = 0; i < mButtons.length; i++){
            if(mButtons[i].getVisibility() == View.GONE){
                mButtons[i].setVisibility(View.VISIBLE);
            }
            mButtons[i].setText(answers[i].getAnswerText());
            mButtons[i].setTag(answers[i].isCorrect());
            mButtons[i].setOnClickListener(clickHandler);
        }
    }

    @Override
    public void onAnswerGiven(boolean isCorrect) {
        totalAnswersCount++;
        mAnswersCountListener.updateAnswersCount(totalAnswersCount);
        if(isCorrect){
           correctAnswersCount++;
           mAnswersCountListener.updateCorrectAnswersCount(getCorrectAnswersCount());
        }
        if(hasNextQuestion()){
            mQuestionIndex++;
            update();
        } else {
            mAnswersCountListener.gameIsOver();
        }
    }

    private int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    private boolean hasNextQuestion(){
        return mQuestionIndex < (QUESTIONS_NUMBER - 1);
    }

    private void saveStats(){
        mSharedPreferences.edit().putInt(SAVED_QUESTION_INDEX, mQuestionIndex).apply();
        mSharedPreferences.edit().putInt(TOTAL_ANSWERS_GIVEN, totalAnswersCount).apply();
        mSharedPreferences.edit().putInt(CORRECT_ANSWERS_GIVEN, correctAnswersCount).apply();
    }
}
