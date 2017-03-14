package com.dark.webprog26.worktastengine;

import com.dark.webprog26.worktastengine.engine.Answer;
import com.dark.webprog26.worktastengine.engine.handlers.ButtonsClickHandler;
import com.dark.webprog26.worktastengine.engine.interfaces.AnswerGivenListener;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by webpr on 14.03.2017.
 */

public class AnswersTest {


    private static final int ASNWERS_COUNT = 4;
    private Answer[] mAnswers;
    private AnswerGivenListener mAnswerGivenListener = new AnswerGivenListener() {
        @Override
        public void onAnswerGiven(boolean isCorrect) {
            //
        }
    };

    private ButtonsClickHandlerUnderTest mButtonsClickHandlerUnderTest;

    private class ButtonsClickHandlerUnderTest extends ButtonsClickHandler{

        ButtonsClickHandlerUnderTest(Answer[] answers, AnswerGivenListener answerGivenListener) {
            super(answers, answerGivenListener);
        }

        @Override
        protected boolean getAnswer(int i) {
            return super.getAnswer(i);
        }
    }

    @Before
    public void setUp() throws Exception {
        mAnswers = new Answer[ASNWERS_COUNT];
        mButtonsClickHandlerUnderTest = new ButtonsClickHandlerUnderTest(mAnswers, mAnswerGivenListener);
    }

    @Test
    public void testFirstQuestion(){
        mAnswers[0] = new Answer("Vova", true);
        mAnswers[1] = new Answer("Misha", false);
        mAnswers[2] = new Answer("Ivan", false);
        mAnswers[3] = new Answer("George", false);

        Assert.assertTrue(mButtonsClickHandlerUnderTest.getAnswer(0));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(1));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(2));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(3));
    }

    @Test
    public void testSecondQuestion(){
        mAnswers[0] = new Answer("1990", false);
        mAnswers[1] = new Answer("1983", true);
        mAnswers[2] = new Answer("1812", false);
        mAnswers[3] = new Answer("2017", false);

        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(0));
        Assert.assertTrue(mButtonsClickHandlerUnderTest.getAnswer(1));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(2));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(3));
    }

    @Test
    public void testThirdQuestion(){
        mAnswers[0] = new Answer("Guitar", true);
        mAnswers[1] = new Answer("Bandjo", false);
        mAnswers[2] = new Answer("Viola", false);
        mAnswers[3] = new Answer("Balalayka", false);

        Assert.assertTrue(mButtonsClickHandlerUnderTest.getAnswer(0));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(1));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(2));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(3));
    }

    @Test
    public void testFourthQuestion(){
        mAnswers[0] = new Answer("Winter", false);
        mAnswers[1] = new Answer("Spring", false);
        mAnswers[2] = new Answer("Summer", true);
        mAnswers[3] = new Answer("Autumn", false);

        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(0));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(1));
        Assert.assertTrue(mButtonsClickHandlerUnderTest.getAnswer(2));
        Assert.assertFalse(mButtonsClickHandlerUnderTest.getAnswer(3));
    }

    @After
    public void finalizeTest(){
        mAnswers = null;
        mAnswerGivenListener = null;
        mButtonsClickHandlerUnderTest = null;
    }
}
