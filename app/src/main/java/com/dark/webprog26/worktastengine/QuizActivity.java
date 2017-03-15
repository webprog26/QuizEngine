package com.dark.webprog26.worktastengine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dark.webprog26.worktastengine.engine.Quiz;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswersCountListener;
import com.dark.webprog26.worktastengine.engine.interfaces.ProgressUpdater;
import com.dark.webprog26.worktastengine.engine.interfaces.ReferenceStarter;
import com.dark.webprog26.worktastengine.engine.managers.ProgressCountManager;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity implements AnswersCountListener, ProgressUpdater, ReferenceStarter {

    @BindViews({R.id.btnFirst, R.id.btnSecond, R.id.btnThird, R.id.btnFourth})
    Button[] mButtons;
    @BindView(R.id.tvQuestion)
    TextView mTvQuestion;
    @BindView(R.id.tvAnswersGiven)
    TextView mTvAnswersGiven;
    @BindView(R.id.tvAnswersCorrect)
    TextView mTvAnswersCorrect;
    @BindView(R.id.btnRestart)
    Button mBtnRestart;
    @BindView(R.id.pbQuizProgress)
    ProgressBar mPbQuizProgress;

    private Quiz mQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int totalAnswersGiven = sharedPreferences.getInt(Quiz.TOTAL_ANSWERS_GIVEN, 0);
        mTvAnswersGiven.setText(getString(R.string.answer_given, totalAnswersGiven));
        mTvAnswersCorrect.setText(getString(R.string.correct_answers, sharedPreferences.getInt(Quiz.CORRECT_ANSWERS_GIVEN, 0)));

        updateProgress(ProgressCountManager.getProgressCount(totalAnswersGiven, Quiz.QUESTIONS_NUMBER));

        mQuiz = new Quiz(this,
                mButtons,
                mTvQuestion,
                this, this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setBackgroundDrawableResource(R.drawable.app_bg);
        mQuiz.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQuiz.pause();
    }

    @Override
    public void updateAnswersCount(int answersCount) {
        mTvAnswersGiven.setText(getString(R.string.answer_given, answersCount));
    }

    @Override
    public void updateCorrectAnswersCount(int correctAnswersCount) {
        mTvAnswersCorrect.setText(getString(R.string.correct_answers, correctAnswersCount));
    }

    @Override
    public void gameIsOver() {
        mTvQuestion.setText(getString(R.string.game_over));
        for(Button button: mButtons){
            button.setVisibility(View.GONE);
        }
        if(mBtnRestart.getVisibility() == View.GONE){
            mBtnRestart.setVisibility(View.VISIBLE);
        }

        mBtnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuiz.resetQuiz();
                v.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void updateProgress(int steps) {
        mPbQuizProgress.setProgress(steps);
    }

    @Override
    public void startReference(int index) {
        Intent referenceIntent = new Intent(this, ReferenceActivity.class);
        referenceIntent.putExtra(ReferenceActivity.REFERENCE_INDEX, index);
        startActivity(referenceIntent);
    }
}
