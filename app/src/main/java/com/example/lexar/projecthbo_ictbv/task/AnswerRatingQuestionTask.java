package com.example.lexar.projecthbo_ictbv.task;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RatingBar;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.fragment.QuestionFragment;
import com.example.lexar.projecthbo_ictbv.fragment.RatingQuestionFragment;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;
import com.example.lexar.projecthbo_ictbv.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * AnswerRatingQuestionTask.
 *
 * Task to post a rating-question answer from the user to the API
 */

public class AnswerRatingQuestionTask extends AsyncTask<Object, Double, Float> {

    private AnswerRatingCallback answerRatingCallback;


    public AnswerRatingQuestionTask(AnswerRatingCallback callback) {
        Assert.that(callback != null, "Null callback");
        this.answerRatingCallback = callback;
    }

    /**
     * Callback that sends the average rating.
     */
    public interface AnswerRatingCallback {
        void answeredRating(float averageRating);
    }


    @Override
    protected Float doInBackground(Object... objects) {
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL +
                    "/api/answers/answer-rating";
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("rating", objects[0]);
            jsonParam.put("languageId", User.getInstance().getLanguageId());
            jsonParam.put("questionId", objects[1]);
            jsonParam.put("accountId",
                    User.getInstance().getUserID());
            jsonParam.put("placeId", objects[2]);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonParam.put("time", simpleDateFormat.format(calendar.getTime()));
            ReturnPackage returnPackage = RequestManager.executePost(jsonParam, link);
            JSONArray jsonObject = (JSONArray) returnPackage.getJson();
            return BigDecimal.valueOf(jsonObject.getJSONObject
                    (0).getDouble("average")).floatValue();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return Float.valueOf(-1);
        }
    }

    @Override
    protected void onPostExecute(Float f) {
        super.onPostExecute(f);
        answerRatingCallback.answeredRating(f);
    }
}
