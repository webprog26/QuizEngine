package com.dark.webprog26.worktastengine.engine.interfaces;

/**
 * Created by webpr on 10.03.2017.
 */

public interface AnswersCountListener {

    public void updateAnswersCount(int answersCount);
    public void updateCorrectAnswersCount(int correctAnswersCount);
    public void gameIsOver();
}
