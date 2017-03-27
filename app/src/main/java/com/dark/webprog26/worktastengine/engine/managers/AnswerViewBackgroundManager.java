package com.dark.webprog26.worktastengine.engine.managers;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by webpr on 27.03.2017.
 */

public class AnswerViewBackgroundManager {

    public static void setAnswerTextViewBackground(TextView[] answerTextViews, Drawable backgroundDrawable){
        for(TextView answerTextView: answerTextViews){
            answerTextView.setBackground(backgroundDrawable);
        }
    }

    public static void setAnswerTextViewBackground(TextView answerTextView, Drawable backgroundDrawable){
            answerTextView.setBackground(backgroundDrawable);
    }
}
