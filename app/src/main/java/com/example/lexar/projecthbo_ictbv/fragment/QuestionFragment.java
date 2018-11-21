package com.example.lexar.projecthbo_ictbv.fragment;

import android.app.Activity;
import android.app.Fragment;

/**
 * Abstract class that contains default methods and attributes
 * a QuestionFragment should have.
 */
public abstract class QuestionFragment extends Fragment{


    protected QuestionCallbacks questionCallbacks;

    /**
     * Callbacks for the questions.
     */
    public interface QuestionCallbacks {
        void answerCallback();
        void errorCallback();
    }

    /**
     * Method to post a new answer.
     */
    public void addAnswer(){};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof QuestionCallbacks) {
            questionCallbacks = (QuestionCallbacks) activity;
        }
    }
}
