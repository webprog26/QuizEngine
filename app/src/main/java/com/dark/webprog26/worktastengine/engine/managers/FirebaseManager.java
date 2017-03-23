package com.dark.webprog26.worktastengine.engine.managers;

import android.content.SharedPreferences;
import android.util.Log;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.Question;
import com.dark.webprog26.worktastengine.engine.Quiz;
import com.dark.webprog26.worktastengine.engine.events.GameOverEvent;
import com.dark.webprog26.worktastengine.engine.events.NextQuestionEvent;
import com.dark.webprog26.worktastengine.engine.events.ReadJSONFromAssetsEvent;
import com.dark.webprog26.worktastengine.engine.firebase_app.QuizFirebaseApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webpr on 20.03.2017.
 */

public class FirebaseManager {

    private static final String TAG = "FirebaseManager";

    //Firebase database root name
    private static final String FIREBASE_DATABASE_ROOT = "questions";
    //JSON file name in assets dir
    private static final String JSON_FILE_NAME = "data.json";

    private SharedPreferences mSharedPreferences;

    //Question fields to read'em from database
    private static final String QUESTION_ID  ="id";
    private static final String QUESTION_ANSWERS_NUM = "answersNum";
    private static final String QUESTION_ANSWERS = "answers";
    private static final String QUESTION_STRING = "questionString";
    private static final String QUESTION_TYPE = "questionType";
    private static final String QUESTION_IMAGE_NAME = "questionImageName";

    //Firebase references
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    //Question id as a key to read next question from
    private long mNextQuestionId;

    public FirebaseManager(SharedPreferences sharedPreferences) {
        //Static FirebaseDatabase instance received from QuizFirebaseApplication.class
        mDatabase = QuizFirebaseApplication.getFirebaseDatabase();
        //DatabaseReference instance to perform requests to database
        mReference = mDatabase.getReference(FIREBASE_DATABASE_ROOT);
        this.mSharedPreferences = sharedPreferences;
        //When com.dark.webprog26.worktastengine.QuizActivity comes to paused state it writes question id to SharedPreferences
        //When app resumes it's work or restarts FirebaseManager reads question id value from SharedPreferences
        //If there is no previously saved question id, for example when app starts for the first time
        // or if the user deletes app's data manually quiz will start from the first question, which id = 0
        this.mNextQuestionId = mSharedPreferences.getLong(Quiz.NEXT_QUESTION_ID, 0);
        if(mNextQuestionId == Question.LAST_QUESTION_ID){
            mNextQuestionId = 0;
        }
        Log.i(TAG, "FirebaseManager initialized");
    }

    /**
     * This method add {@link ValueEventListener} to {@link DatabaseReference} and first of all
     * checks is there any data in database at all. If database has data, then it calls getNextQuestion(long questionIndex) method,
     * otherwise runs new ReadJSONFromAssetsEvent
     */
    public void checkIsFirebaseAlreadyFilled(){
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    //Database exists. Get next question from it
                    Log.i(TAG, "database exists. Loading question with following id " + mNextQuestionId);
                    getNextQuestion(mNextQuestionId);
                } else {
                    Log.i(TAG, "database not exists. Posting new ReadJSONFromAssetsEvent to read from file " + JSON_FILE_NAME);
                    //Database not exists. Read values from JSON file stored in device assets directory
                    EventBus.getDefault().post(new ReadJSONFromAssetsEvent(JSON_FILE_NAME));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    /**
     * Uploads Question instances to database.
     * @param questionList {@link List}
     */
    public void uploadValuesToDb(List<Question> questionList){
        Log.i(TAG, "uploading values to db");
        for(Question question: questionList){
            Log.i(TAG, question.toString());
            //Upload values to database
            mReference.child(String.valueOf(question.getId())).setValue(question);
        }
    }

    /**
     * Reads question from database and constructs {@link Question} instance.
     * Runs new NextQuestionEvent.
     * @param questionIndex
     */
    public void getNextQuestion(long questionIndex){
        if(questionIndex == Question.LAST_QUESTION_ID){
            //We've reached the last question which answers has nextQuestionId = -1
            //Run new GameOverEvent()
            EventBus.getDefault().post(new GameOverEvent());
            return;
        }
        Log.i(TAG, "getNextQuestion() method showed question index " + questionIndex);
        mReference.child(String.valueOf(questionIndex)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long answersNum = (long) dataSnapshot.child(QUESTION_ANSWERS_NUM).getValue();
                List<Answer> answers  = new ArrayList<Answer>();
                for(int i = 0; i < answersNum; i++){
                    Answer answer = dataSnapshot.child(QUESTION_ANSWERS).child(String.valueOf(i)).getValue(Answer.class);
                    answers.add(answer);
                }
                Question question = new Question(

                        (long) dataSnapshot.child(QUESTION_ID).getValue(),
                        String.valueOf(dataSnapshot.child(QUESTION_STRING).getValue()),
                        answers,
                        (long) dataSnapshot.child(QUESTION_TYPE).getValue(),
                        String.valueOf(dataSnapshot.child(QUESTION_IMAGE_NAME).getValue())

                );
                Log.i(TAG, question.toString());
                EventBus.getDefault().post(new NextQuestionEvent(question));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, databaseError.getMessage());
            }
        });
    }
}
