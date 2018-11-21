package com.example.lexar.projecthbo_ictbv.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.task.AnswerOpenQuestion;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Fragment for answering the open questions.
 */
public class OpenQuestionFragment extends QuestionFragment {

    private int questionId;
    private int placeId;


    @BindView(R.id.et_questionAnswer)
    EditText questionAnswer;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Assert.that(inflater != null , "Null inflater while inflating layout");
        View view = inflater.inflate(R.layout.fragment_open_question, container, false);
        ButterKnife.bind(this, view);
        questionId = getArguments().getInt(Constants.QUESTION_ID_KEY);
        placeId = getArguments().getInt(Constants.LOCATION_ID_KEY);
        return view;
    }

    /**
     * Method to post a new answer.
     */
    @Override
    public void addAnswer() {
        //Give an error message if the edit text is empty
        if (questionAnswer.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.no_answer_given),
                    Toast.LENGTH_LONG).show();
        } else {
            AnswerOpenQuestion add = new AnswerOpenQuestion(questionCallbacks);
            add.execute(questionAnswer.getText().toString(), questionId, placeId);
        }
    }

}
