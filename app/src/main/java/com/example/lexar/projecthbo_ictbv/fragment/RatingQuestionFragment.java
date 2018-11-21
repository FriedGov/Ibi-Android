package com.example.lexar.projecthbo_ictbv.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;
import com.example.lexar.projecthbo_ictbv.model.User;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.task.AnswerRatingQuestionTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for answering the open questions.
 */
public class RatingQuestionFragment extends QuestionFragment
        implements AnswerRatingQuestionTask.AnswerRatingCallback{

    private int questionId;
    private int clicks = 0;
    private int placeId = 0;
    private RatingCallBacks ratingCallBacks;
    @BindView(R.id.rb_starRating)
    RatingBar ratingBar;
    @BindView(R.id.textView)
    TextView textView;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Assert.that(inflater != null, "Null inflater");
        View view = inflater.inflate(R.layout.fragment_ratings, container, false);
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
        super.addAnswer();
        if (clicks == 0) {
            AnswerRatingQuestionTask addAnswer =
                    new AnswerRatingQuestionTask(this);
            addAnswer.execute((ratingBar.getRating()), questionId, placeId);
            clicks++;
        } else {
            questionCallbacks.answerCallback();
        }
    }

    /**
     * Method to set the average rating.
     * @param averageRating the average rating
     */
    @Override
    public void answeredRating(float averageRating) {
        if (averageRating != -1) {
            ratingBar.setRating(averageRating);
            ratingBar.setClickable(false);
            ratingBar.setIsIndicator(true);
            textView.setText(getString(R.string.average_rating));
            ratingCallBacks.answerRating();
        } else {
            questionCallbacks.errorCallback();
        }
    }

    /**
     * Specific rating callbacks.
     */
    public interface RatingCallBacks{
        void answerRating();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof RatingCallBacks){
            ratingCallBacks = (RatingCallBacks) activity;
        }
    }


}
