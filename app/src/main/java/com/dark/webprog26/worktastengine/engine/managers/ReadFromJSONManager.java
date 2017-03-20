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

    private static final String TAG = "JSONManager";

    public static void jsonReadFromAssets(String jsonString) {
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
                    jsonArray = questionsJSONObject.getJSONArray("questions");
                    Log.i(TAG, "Array has " + jsonArray.length() + " questions");
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
                                JSONArray jsonAnswers = singleQuestionObject.getJSONArray("answers");
                                int answersLength = jsonAnswers.length();
                                List<Answer> answers = new ArrayList<>();
                                for(int j = 0; j < answersLength; j++){
                                    JSONObject answerObject = jsonAnswers.getJSONObject(j);
                                    Answer answer = new Answer(answerObject.getLong("id"),
                                            answerObject.getString("text"),
                                            answerObject.getDouble("points"),
                                            answerObject.getLong("nextQuestionId"));
                                    answers.add(answer);
                                }
                                question = new Question(
                                        singleQuestionObject.getLong("id"),
                                        singleQuestionObject.getString("text"),
                                        answers,
                                        singleQuestionObject.getInt("type"),
                                        singleQuestionObject.getString("questionImageName"));
                            }
                            if(question != null){
                                Log.i(TAG, question.toString());
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

        EventBus.getDefault().post(new QuestionsReadFromJSONEvent(questionsList));
    }
}
