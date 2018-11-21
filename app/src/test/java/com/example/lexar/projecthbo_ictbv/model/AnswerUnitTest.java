package com.example.lexar.projecthbo_ictbv.model;

import com.example.lexar.projecthbo_ictbv.model.Answer;

import org.junit.Test;


public class AnswerUnitTest {

    /**
     * #1 Try putting in a negative answer id
     */
    @Test(expected = AssertionError.class)
    public void answerNegativeId() {
        Answer answer = new Answer(-1, 1, "Antwoordveld");
    }
    /**
     * #2 Try putting in a negative language id
     */
    @Test(expected = AssertionError.class)
    public void answerNegativeLanguageId() {
        Answer answer = new Answer(1, -1, "Antwoordveld");
    }

    /**
     * #3 Try putting in an empty string
     */
    @Test(expected = AssertionError.class)
    public void answerEmptyTextField() {
        Answer answer = new Answer(1, 1, "");
    }
}
