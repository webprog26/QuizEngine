package com.dark.webprog26.worktastengine.engine.events;

import com.dark.webprog26.worktastengine.engine.Question;

import java.util.List;

/**
 * Created by webpr on 20.03.2017.
 */

public class QuestionsReadFromJSONEvent {

    private final List<Question> mQuestionsList;

    public QuestionsReadFromJSONEvent(List<Question> questionsList) {
        this.mQuestionsList = questionsList;
    }

    public List<Question> getQuestionsList() {
        return mQuestionsList;
    }
}
