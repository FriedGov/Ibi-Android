package com.example.lexar.projecthbo_ictbv.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.Answer;
import com.example.lexar.projecthbo_ictbv.model.User;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.task.AnswerMultipleChoiceQuestionTask;
import com.example.lexar.projecthbo_ictbv.task.GetMultipleChoiceAnswersTask;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for answering the multiple choice questions.
 */
public class MultipleChoiceQuestionFragment extends QuestionFragment
        implements GetMultipleChoiceAnswersTask.GetAnswersCallbacks,
        AnswerMultipleChoiceQuestionTask.AnswerMultipleChoiceQuestionCallbacks {
    private int questionId;
    private int placeId;
    @BindView(R.id.radiogroup_mcRadio)
    RadioGroup answersGroup;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Assert.that(inflater != null, "Null inflater while inflating layout");
        View view = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        ButterKnife.bind(this, view);
        questionId = getArguments().getInt(Constants.QUESTION_ID_KEY);
        placeId = getArguments().getInt(Constants.LOCATION_ID_KEY);
        GetMultipleChoiceAnswersTask getAnswers = new GetMultipleChoiceAnswersTask(this);
        getAnswers.execute(questionId);
        return view;
    }

    /**
     * Method to add an answer to the fragment.
     */
    @Override
    public void addAnswer() {
        if (answersGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), getString(R.string.no_buttons_checked),
                    Toast.LENGTH_LONG).show();
            questionCallbacks.errorCallback();
        } else {
            int id = answersGroup.getCheckedRadioButtonId();
            AnswerMultipleChoiceQuestionTask answerQuestion2 =
                    new AnswerMultipleChoiceQuestionTask(this);
            answerQuestion2.execute(questionId, id, placeId);
        }
    }

    /**
     * Called when the answers task has completed.
     *
     * @param answers the list with answers
     */
    @Override
    public void receiveAnswers(ArrayList<Answer> answers) {
        Assert.that(answers != null, "Answers are null");
        Assert.that(answers.size() != 0, "Answers size is 0");
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId(answer.getId());
            rdbtn.setText(answer.getText());
            answersGroup.addView(rdbtn);
        }
    }


    /**
     * Called when a user answers a question.
     *
     * @param succeeded boolean if the answer is valid
     */
    @Override
    public void answerQuestion(boolean succeeded) {
        if (succeeded) {
            //Go back to the question activity
            questionCallbacks.answerCallback();
        } else {
            questionCallbacks.errorCallback();
        }
    }
}
