package com.example.lexar.projecthbo_ictbv.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.fragment.QuestionFragment;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.User;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Task to post a open-question answer from a user to the API.
 */
public class AnswerOpenQuestion extends AsyncTask<Object, Double, Boolean> {

    private QuestionFragment.QuestionCallbacks openQuestionCallback;

    public AnswerOpenQuestion(QuestionFragment.QuestionCallbacks callback) {
        Assert.that(callback != null, "Null callback");
        openQuestionCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            String link = "http://" + Constants.API_URL + "/api/answers/answeropen";
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("text", objects[0]);
            jsonParam.put("languageId", 1);
            jsonParam.put("questionId", objects[1]);
            jsonParam.put("accountId", User.getInstance().getUserID());
            jsonParam.put("placeId", objects[2]);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonParam.put("time", simpleDateFormat.format(calendar.getTime()));
            RequestManager.executePost(jsonParam, link);
            return true;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            //Go back to the question activity
           openQuestionCallback.answerCallback();
        } else {
            //Enable the button again if some kind of error occured
            openQuestionCallback.errorCallback();
        }
    }
}
