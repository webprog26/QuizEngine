package com.dark.webprog26.worktastengine;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.Quiz;
import com.dark.webprog26.worktastengine.engine.events.FirebaseFilledWithValuesEvent;
import com.dark.webprog26.worktastengine.engine.events.GameOverEvent;
import com.dark.webprog26.worktastengine.engine.events.NextQuestionEvent;
import com.dark.webprog26.worktastengine.engine.events.QuestionAnsweredEvent;
import com.dark.webprog26.worktastengine.engine.events.QuestionsReadFromJSONEvent;
import com.dark.webprog26.worktastengine.engine.events.ReadJSONFromAssetsEvent;
import com.dark.webprog26.worktastengine.engine.interfaces.ProgressUpdater;
import com.dark.webprog26.worktastengine.engine.managers.FirebaseManager;
import com.dark.webprog26.worktastengine.engine.managers.ProgressCountManager;
import com.dark.webprog26.worktastengine.engine.managers.ReadFromJSONManager;
import com.dark.webprog26.worktastengine.engine.managers.ReadJSONFromAssetsManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity implements ProgressUpdater {

    private static final String TAG = "QuizActivity_TAG";



    //Initializing GUI
    @BindViews({R.id.btnFirst, R.id.btnSecond, R.id.btnThird, R.id.btnFourth})
    Button[] mButtons;
    @BindView(R.id.tvQuestion)
    TextView mTvQuestion;
    @BindView(R.id.tvAnswersGiven)
    TextView mTvAnswersGiven;
    @BindView(R.id.tvPoints)
    TextView mTvPoints;
    @BindView(R.id.btnRestart)
    Button mBtnRestart;
    @BindView(R.id.pbQuizProgress)
    ProgressBar mPbQuizProgress;
    @BindView(R.id.pbQuizLoading)
    ProgressBar mPbQuizLoading;

    private Quiz mQuiz;
    private FirebaseManager mFirebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //Binding QuizActivity to ButterKnife instance
        ButterKnife.bind(this);
        mFirebaseManager = new FirebaseManager(PreferenceManager.getDefaultSharedPreferences(this));


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int totalAnswersGiven = sharedPreferences.getInt(Quiz.TOTAL_ANSWERS_GIVEN, 0);
        updateAnswersCount(totalAnswersGiven);
        updatePointsScoredCount(Double.parseDouble(sharedPreferences.getString(Quiz.CURRENT_POINTS, "0")));

        updateProgress(ProgressCountManager.getProgressCount(totalAnswersGiven, Quiz.QUESTIONS_NUMBER));

        mQuiz = new Quiz(mButtons, mTvQuestion, sharedPreferences, mFirebaseManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Registering EventBus to handle events
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setBackgroundDrawableResource(R.drawable.app_bg);
        if(mQuiz.hasNextQuestion()){
            mFirebaseManager.checkIsFirebaseAlreadyFilled();
        } else {
            mQuiz.gameOver();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQuiz.pause();
    }

    private void updateAnswersCount(int answersCount) {
        mTvAnswersGiven.setText(getString(R.string.answer_given, answersCount));
    }

    private void updatePointsScoredCount(double pointsScored) {
        mTvPoints.setText(getString(R.string.points_scored, pointsScored));
    }

    @Override
    public void updateProgress(int steps) {
        mPbQuizProgress.setProgress(steps);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onQuestionsReadFromJSONEvent(QuestionsReadFromJSONEvent questionsReadFromJSONEvent){
        mFirebaseManager.uploadValuesToDb(questionsReadFromJSONEvent.getQuestionsList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextQuestionEvent(NextQuestionEvent nextQuestionEvent){
        if(mPbQuizLoading.getVisibility() == View.VISIBLE){
            mPbQuizLoading.setVisibility(View.GONE);
        }

        if(mPbQuizProgress.getVisibility() == View.GONE){
            mPbQuizProgress.setVisibility(View.VISIBLE);
        }

        for(Button button: mButtons){
            button.setVisibility(View.INVISIBLE);
        }
        mQuiz.resume(nextQuestionEvent.getQuestion());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFirebaseFilledWithValuesEvent(FirebaseFilledWithValuesEvent firebaseFilledWithValuesEvent){
        Log.i(TAG, "FirebaseFilledWithValuesEvent");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuestionAnsweredEvent(QuestionAnsweredEvent questionAnsweredEvent){
        Answer answer = questionAnsweredEvent.getAnswer();
        mQuiz.setTotalAnswersCount(mQuiz.getTotalAnswersCount() + 1);
        mQuiz.setCurrentPointsCount(mQuiz.getCurrentPointsCount() + answer.getPoints());
        updateAnswersCount(mQuiz.getTotalAnswersCount());
        updatePointsScoredCount(mQuiz.getCurrentPointsCount());

        updateProgress(ProgressCountManager.getProgressCount(mQuiz.getTotalAnswersCount(), Quiz.QUESTIONS_NUMBER));

        mFirebaseManager.getNextQuestion(answer.getNextQuestionId());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onReadJSONFromAssetsEvent(ReadJSONFromAssetsEvent readJSONFromAssetsEvent){
        ReadFromJSONManager.jsonReadFromAssets(ReadJSONFromAssetsManager.loadJSONFromAsset(getAssets(), readJSONFromAssetsEvent.getJSONFileName()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameOverEvent(GameOverEvent gameOverEvent){
        mTvQuestion.setText(getString(R.string.game_over));

        if(mPbQuizProgress.getVisibility() == View.VISIBLE){
            mPbQuizProgress.setVisibility(View.GONE);
        }

        for(Button button: mButtons){
            button.setVisibility(View.INVISIBLE);
        }
        if(mBtnRestart.getVisibility() == View.GONE){
            mBtnRestart.setVisibility(View.VISIBLE);
        }

        mBtnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuiz.resetQuiz();
                updateAnswersCount(mQuiz.getTotalAnswersCount());
                updatePointsScoredCount(mQuiz.getCurrentPointsCount());
                updateProgress(0);
                v.setVisibility(View.GONE);
            }
        });
    }
}
