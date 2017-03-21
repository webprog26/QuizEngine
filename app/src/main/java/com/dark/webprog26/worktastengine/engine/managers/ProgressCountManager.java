package com.dark.webprog26.worktastengine.engine.managers;

/**
 * Created by webpr on 14.03.2017.
 */

public class ProgressCountManager {

    /**
     * Counts percents of user progress, based on number of total steps user should make, in other words - total number of questions
     * @param stepsDone int
     * @param totalSteps int
     * @return boolean
     */
    public static int getProgressCount(int stepsDone, int totalSteps){
        if(stepsDone == 0){
            return 0;
        }

        return (stepsDone * 100) / totalSteps;
    }
}
