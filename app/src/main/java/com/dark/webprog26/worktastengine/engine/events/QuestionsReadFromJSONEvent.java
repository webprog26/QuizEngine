package com.dark.webprog26.worktastengine.engine.events;

import com.dark.webprog26.worktastengine.engine.Question;

import java.util.List;

/**
 * Created by webpr on 20.03.2017.
 */

public class QuestionsReadFromJSONEvent {

    /**
     * This event takes place when all the values from JSON file stored in device assets directory has been read.
     * Yandling this QuizActivity calls {@link com.dark.webprog26.worktastengine.engine.managers.FirebaseManager}
     * method uploadValuesToDb(List<Question> questionList)
     */

    private final List<Question> mQuestionsList;

    public QuestionsReadFromJSONEvent(List<Question> questionsList) {
        this.mQuestionsList = questionsList;
    }

    public List<Question> getQuestionsList() {
        return mQuestionsList;
    }
}
