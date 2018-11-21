package com.example.lexar.projecthbo_ictbv.task;

import android.app.Fragment;
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
 * AnswerMultipleChoiceQuestionTask.
 *
 * Task to post the multiplechoice answer from a user to the API
 */

public class AnswerMultipleChoiceQuestionTask extends AsyncTask<Object, Double, Boolean> {

    private AnswerMultipleChoiceQuestionCallbacks answerMultipleChoiceQuestionCallbacks;


    public AnswerMultipleChoiceQuestionTask(AnswerMultipleChoiceQuestionCallbacks callback) {
        Assert.that(callback != null, "Null callback");
        this.answerMultipleChoiceQuestionCallbacks = callback;
    }

    /**
     * Callback that sends if the answering succeeded.
     */
    public interface AnswerMultipleChoiceQuestionCallbacks{
        void answerQuestion(boolean succeeded);
    }


    @Override
    protected Boolean doInBackground(Object... objects) {
        assert objects.length == 3;
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL + "/api/given-answers";
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("questionId", objects[0]);
            jsonParam.put("accountId", User.getInstance().getUserID());
            jsonParam.put("answerId", objects[1]);
            jsonParam.put("placeId", objects[2]);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonParam.put("time", simpleDateFormat.format(calendar.getTime()));
            Log.i("JSON", jsonParam.toString());
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
        answerMultipleChoiceQuestionCallbacks.answerQuestion(aBoolean);
    }
}
