package com.example.lexar.projecthbo_ictbv.task;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.Answer;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;


import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * Task for getting the possible answers for a multiplechoice question.
 */

public class GetMultipleChoiceAnswersTask extends AsyncTask<Integer, Double, ArrayList<Answer>> {

    private GetAnswersCallbacks getAnswersCallbacks;


    public GetMultipleChoiceAnswersTask(GetAnswersCallbacks callback) {
        Assert.that(callback != null, "Null callback");
        this.getAnswersCallbacks = callback;
    }

    /**
     * Callback for returning the answers.
     */
    public interface GetAnswersCallbacks {
        void receiveAnswers(ArrayList<Answer> answers);
    }

    @Override
    protected ArrayList<Answer> doInBackground(Integer... integers) {
        Assert.that(integers.length == 1, "Integer size not 1");
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL +
                    "/api/questions/" + integers[0] + "/answers";
            Log.d("AnswersTask", link);
            ReturnPackage returnPackage = RequestManager.executeGet(link);
            Log.d("AnswersTask", returnPackage.getJson().toString());
            ArrayList<Answer> tempAnswers = new ArrayList<>();
            JSONArray ar = (JSONArray) returnPackage.getJson();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject object = ar.getJSONObject(i);
                tempAnswers.add(new Answer(object));
            }
            return tempAnswers;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Answer> arrayList) {
        super.onPostExecute(arrayList);
        getAnswersCallbacks.receiveAnswers(arrayList);
    }
}

