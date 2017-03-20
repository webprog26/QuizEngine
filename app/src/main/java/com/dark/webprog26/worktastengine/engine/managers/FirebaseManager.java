package com.dark.webprog26.worktastengine.engine.managers;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.Question;
import com.dark.webprog26.worktastengine.engine.Quiz;
import com.dark.webprog26.worktastengine.engine.events.FirebaseFilledWithValuesEvent;
import com.dark.webprog26.worktastengine.engine.events.NextQuestionEvent;
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

    private static final String QUESTION_ID  ="id";
    private static final String QUESTION_ANSWERS_NUM = "answersNum";
    private static final String QUESTION_ANSWERS = "answers";
    private static final String QUESTION_STRING = "questionString";
    private static final String QUESTION_TYPE = "questionType";
    private static final String QUESTION_IMAGE_NAME = "questionImageName";

    //Firebase references
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    //for test
    private long mNextQuestionId;

    public FirebaseManager(SharedPreferences sharedPreferences) {
        mDatabase = QuizFirebaseApplication.getFirebaseDatabase();
        mReference = mDatabase.getReference(FIREBASE_DATABASE_ROOT);
        this.mSharedPreferences = sharedPreferences;
        this.mNextQuestionId = mSharedPreferences.getLong(Quiz.SAVED_QUESTION_ID, 0);
    }

    public void checkIsFirebaseAlreadyFilled(final AssetManager assetManager){
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    Log.i(TAG, "database exists");
                    //Database exists
                    getNextQuestion(mNextQuestionId);
                } else {
                    //Database not exists
                    Log.i(TAG, "database not exists");
                    ReadFromJSONManager.jsonReadFromAssets(ReadJSONFromAssetsManager.loadJSONFromAsset(assetManager, JSON_FILE_NAME));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    public void uploadValuesToDb(List<Question> questionList){
        for(Question question: questionList){
            //Upload values to database
            mReference.child(String.valueOf(question.getId())).setValue(question);
        }
        EventBus.getDefault().post(new FirebaseFilledWithValuesEvent());
    }

    public void getNextQuestion(long questionIndex){
        Log.i(TAG, "next question index " + questionIndex);
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
