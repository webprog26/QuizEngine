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
import android.widget.Toast;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.Quiz;
import com.dark.webprog26.worktastengine.engine.events.GameOverEvent;
import com.dark.webprog26.worktastengine.engine.events.NextQuestionEvent;
import com.dark.webprog26.worktastengine.engine.events.QuestionAnsweredEvent;
import com.dark.webprog26.worktastengine.engine.events.QuestionsReadFromJSONEvent;
import com.dark.webprog26.worktastengine.engine.events.ReadJSONFromAssetsEvent;
import com.dark.webprog26.worktastengine.engine.managers.FirebaseManager;
import com.dark.webprog26.worktastengine.engine.managers.ReadFromJSONManager;
import com.dark.webprog26.worktastengine.engine.managers.ReadJSONFromAssetsManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {

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
    @BindView(R.id.btnSkipQuestion)
    Button mBtnSkipQuestion;
    @BindView(R.id.btnResumeQuiz)
    Button mBtnResumeQuestion;
    @BindView(R.id.btnRestart)
    Button mBtnRestart;
    @BindView(R.id.pbQuizLoading)
    ProgressBar mPbQuizLoading;

    private Quiz mQuiz;
    private FirebaseManager mFirebaseManager;
    private boolean isAnswerGiven = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //Binding QuizActivity to ButterKnife instance
        ButterKnife.bind(this);

        //Answer and control buttons are invisible, until first question will be loaded
        for(Button button: mButtons){
            button.setVisibility(View.INVISIBLE);
        }
        mBtnResumeQuestion.setVisibility(View.INVISIBLE);
        mBtnSkipQuestion.setVisibility(View.INVISIBLE);

        //Initializing FirebaseManager instance to perform quiz-database connection possible
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mFirebaseManager = new FirebaseManager(sharedPreferences);

        //Reading game scores saved in SharedPrefernces and update game
        int totalAnswersGiven = sharedPreferences.getInt(Quiz.TOTAL_ANSWERS_GIVEN, 0);
        updateAnswersCount(totalAnswersGiven);
        updatePointsScoredCount(Double.parseDouble(sharedPreferences.getString(Quiz.CURRENT_POINTS, "0")));

        //Initializing Quiz instance
        mQuiz = new Quiz(mButtons, mTvQuestion, sharedPreferences, mFirebaseManager);

        mBtnResumeQuestion.setEnabled(isAnswerGiven);
        mBtnResumeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseManager.getNextQuestion(mQuiz.getNextQuestionId());
                if(isAnswerGiven){
                    isAnswerGiven = false;
                }
                mBtnResumeQuestion.setEnabled(isAnswerGiven);
            }
        });
        //Todo realize skipping mechanic
        mBtnSkipQuestion.setEnabled(false);
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
            //Somebody may think that it is useless: to check does data exists everytime quiz resumes,
            //but what if the user deletes app data, while it is paused? By this reason, i'm sure it is necessary step
            mFirebaseManager.getQuestionsFromFirebaseDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //App is paused, save stats
        mQuiz.pause();
    }

    /**
     * Updates given answers count on user screen
     * @param answersCount int
     */
    private void updateAnswersCount(int answersCount) {
        mTvAnswersGiven.setText(getString(R.string.answer_given, answersCount));
    }

    /**
     * Updates scored point count on user screen
     * @param pointsScored double
     */
    private void updatePointsScoredCount(double pointsScored) {
        mTvPoints.setText(getString(R.string.points_scored, pointsScored));
    }

    @Override
    protected void onStop() {
        //Unregister EventBus to avoid memory leaks
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Handles QuestionsReadFromJSONEvent. Calls {@link FirebaseManager} uploadValuesToDb() method
     * @param questionsReadFromJSONEvent {@link QuestionsReadFromJSONEvent}
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onQuestionsReadFromJSONEvent(QuestionsReadFromJSONEvent questionsReadFromJSONEvent){
        Log.i(ReadFromJSONManager.TRACK_JSON, "Handling QuestionsReadFromJSONEvent in QuizActivity");
        Log.i(ReadFromJSONManager.TRACK_JSON, "Calling FirebaseManager uploadValuesToDb method in QuizActivity");
        mFirebaseManager.uploadValuesToDb(questionsReadFromJSONEvent.getQuestionsList());
    }

    /**
     * Handles NextQuestionEvent. Manages progress bars and answer-buttons visibility,
     * resumes quiz with next question by calling Quiz method resume();
     * @param nextQuestionEvent {@link NextQuestionEvent}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextQuestionEvent(NextQuestionEvent nextQuestionEvent){

        //Showed while data is uploading to database. Normally at the first app's launch
        if(mPbQuizLoading.getVisibility() == View.VISIBLE){
            mPbQuizLoading.setVisibility(View.GONE);
        }

        //Quiz has been loaded, so we should make quiz-control buttons visible
        if(mBtnResumeQuestion.getVisibility() == View.INVISIBLE){
            mBtnResumeQuestion.setVisibility(View.VISIBLE);
        }
        if(mBtnSkipQuestion.getVisibility() == View.INVISIBLE){
            mBtnSkipQuestion.setVisibility(View.VISIBLE);
        }

        //Answer and control buttons are invisible, until next question will be loaded
        for(Button button: mButtons){
            button.setVisibility(View.INVISIBLE);
        }

        mQuiz.resume(nextQuestionEvent.getQuestion());
    }

    /**
     * Handles QuestionAnsweredEvent. Updates scores and user progress, calls {@link FirebaseManager} getNextQuestion() method
     * based on next question id from chosen answer
     * @param questionAnsweredEvent {@link QuestionAnsweredEvent}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuestionAnsweredEvent(QuestionAnsweredEvent questionAnsweredEvent){
       if(!isAnswerGiven){
           isAnswerGiven = true;
           Log.i(TAG, "need help = " + questionAnsweredEvent.needsHelp());
           //Enabling resume quiz button
           mBtnResumeQuestion.setEnabled(isAnswerGiven);
           Answer answer = questionAnsweredEvent.getAnswer();
           mQuiz.setNextQuestionId(answer.getNextQuestionId());
           int totalAnswersGiven = mQuiz.getTotalAnswersCount();
           mQuiz.setTotalAnswersCount(totalAnswersGiven + 1);
           mQuiz.setCurrentPointsCount(mQuiz.getCurrentPointsCount() + answer.getPoints());
           //updating scores on the user screen
           updateAnswersCount(mQuiz.getTotalAnswersCount());
           updatePointsScoredCount(mQuiz.getCurrentPointsCount());

           //Ten questions answered. Show offer
           if(totalAnswersGiven == Quiz.BUY_FULL_VERSION_OFFER_MARKER){
               Toast.makeText(this, getString(R.string.buy_full_version_offer), Toast.LENGTH_SHORT).show();
           }

           //Increasing required questions count
           if(questionAnsweredEvent.isIsQuestionRequired()){
               mQuiz.setRequiredQuestionsPassed(mQuiz.getRequiredQuestionsPassed() + 1);
           }
       }
    }

    /**
     * Handles ReadJSONFromAssetsEvent. Calls {@link ReadFromJSONManager} jsonReadFromAssets() asynchronously
     * @param readJSONFromAssetsEvent {@link ReadJSONFromAssetsEvent}
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onReadJSONFromAssetsEvent(ReadJSONFromAssetsEvent readJSONFromAssetsEvent){
        Log.i(ReadFromJSONManager.TRACK_JSON, "Handling ReadJSONFromAssetsEvent in QuizActivity");
        ReadFromJSONManager.jsonReadFromAssets(ReadJSONFromAssetsManager.loadJSONFromAsset(getAssets(), readJSONFromAssetsEvent.getJSONFileName()));
    }

    /**
     * Handles GameOverEvent. Informs user that game is over, gives him possibility to restart the quiz.
     * Resets quiz state by calling Quiz resetQuiz() method, resets game stats to the default state
     * @param gameOverEvent {@link GameOverEvent}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameOverEvent(GameOverEvent gameOverEvent){
        mTvQuestion.setText(getString(R.string.game_over));
        mBtnResumeQuestion.setEnabled(false);

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
                if(isAnswerGiven){
                    isAnswerGiven = false;
                }
                v.setVisibility(View.GONE);
            }
        });
    }
}
