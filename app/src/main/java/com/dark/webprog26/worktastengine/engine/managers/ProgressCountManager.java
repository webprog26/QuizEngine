package com.dark.webprog26.worktastengine.engine.managers;

/**
 * Created by webpr on 14.03.2017.
 */

public class ProgressCountManager {

    public static int getProgressCount(int stepsDone, int totalSteps){
        if(stepsDone == 0){
            return 0;
        }

        return (stepsDone * 100) / totalSteps;
    }
}
