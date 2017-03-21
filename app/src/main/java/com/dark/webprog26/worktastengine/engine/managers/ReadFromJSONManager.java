package com.dark.webprog26.worktastengine.engine.managers;

import android.util.Log;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.Question;
import com.dark.webprog26.worktastengine.engine.events.QuestionsReadFromJSONEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webpr on 20.03.2017.
 */

public class ReadFromJSONManager {

    /**
     * Reads JSON file with questions/answers from device assets directory
     */


    private static final String TAG = "JSONManager";

    private static final String QUESTIONS_ROOT = "questions";
    private static final String ANSWERS = "answers";
    private static final String ANSWER_ID = "id";
    private static final String ANSWER_TEXT = "text";
    private static final String ANSWER_POINTS = "points";
    private static final String ANSWER_NEXT_QUESTION_ID = "nextQuestionId";

    private static final String QUESTION_ID = "id";
    private static final String QUESTION_TEXT = "text";
    private static final String QUESTION_TYPE = "type";
    private static final String QUESTION_IMAGE_NAME = "questionImageName";

    /**
     * Reads JSON file with questions/answers from device assets directory
     * @param jsonString {@link String}
     */
    public static void jsonReadFromAssets(String jsonString) {
        //Initializes collection, this time ArrayList, to store Question instances, that have been read
        List<Question> questionsList = new ArrayList<>();
        if(jsonString != null){
            JSONObject questionsJSONObject = null;
            try {
                questionsJSONObject = new JSONObject(jsonString);
            } catch (JSONException e){
                e.printStackTrace();
            }

            if(questionsJSONObject != null){
                JSONArray jsonArray = null;
                try{
                    jsonArray = questionsJSONObject.getJSONArray(QUESTIONS_ROOT);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                if(jsonArray != null){
                    JSONObject singleQuestionObject = null;
                    try{
                        for (int i = 0; i < jsonArray.length(); i++) {
                            singleQuestionObject = jsonArray.getJSONObject(i);
                            Question question = null;
                            if(singleQuestionObject != null){
                                JSONArray jsonAnswers = singleQuestionObject.getJSONArray(ANSWERS);
                                int answersLength = jsonAnswers.length();
                                List<Answer> answers = new ArrayList<>();
                                for(int j = 0; j < answersLength; j++){
                                    JSONObject answerObject = jsonAnswers.getJSONObject(j);
                                    Answer answer = new Answer(answerObject.getLong(ANSWER_ID),
                                            answerObject.getString(ANSWER_TEXT),
                                            answerObject.getDouble(ANSWER_POINTS),
                                            answerObject.getLong(ANSWER_NEXT_QUESTION_ID));
                                    answers.add(answer);
                                }
                                //Construct new Question instance
                                question = new Question(
                                        singleQuestionObject.getLong(QUESTION_ID),
                                        singleQuestionObject.getString(QUESTION_TEXT),
                                        answers,
                                        singleQuestionObject.getInt(QUESTION_TYPE),
                                        singleQuestionObject.getString(QUESTION_IMAGE_NAME));
                            }
                            if(question != null){
                                //add question instance to List
                                questionsList.add(question);
                            }
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            } else {
                Log.i(TAG, "jsonObject is null");
            }
        } else {
            Log.i(TAG, "jsonString is null");
        }
        //Run new QuestionsReadFromJSONEvent with questions List as a parameter
        EventBus.getDefault().post(new QuestionsReadFromJSONEvent(questionsList));
    }
}
