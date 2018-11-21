package com.example.lexar.projecthbo_ictbv.task;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.Answer;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * TotalAnswersTask.
 *
 * Task to count the total answers from a given question.
 */

public class TotalAnswersTask extends AsyncTask<Integer, Double, ArrayList<Answer>> {

    private TotalAnswersTaskCallbacks totalAnswersTaskCallbacks;


    public TotalAnswersTask(TotalAnswersTaskCallbacks callback) {
        Assert.that(callback != null, "Null callback");
        this.totalAnswersTaskCallbacks = callback;
    }

    /**
     * Callback for returning the answer stats.
     */
    public interface TotalAnswersTaskCallbacks{
        void onGetAnswers(ArrayList<Answer> answers);
    }


    @Override
    protected ArrayList<Answer> doInBackground(Integer... integers) {
        assert integers.length == 1;
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL +
                    "/api/given-answers/multiple-choice-question-answers/"
                    + integers[0] + "/" + integers[1];
            ReturnPackage returnPackage = RequestManager.executeGet(link);
            ArrayList<Answer> tempAnswers = new ArrayList<>();
            JSONArray ar = (JSONArray) returnPackage.getJson();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject object = ar.getJSONObject(i);
                int id = object.getInt("answerId");
                String tAmount = object.getString("amount");
                int amount = 0;
                if (!tAmount.equals("null")) {
                    amount = Integer.parseInt(tAmount);
                }
                String text = object.getString("text");
                tempAnswers.add(new Answer(id, text, amount));
            }
            return tempAnswers;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Answer> arrayList) {
        totalAnswersTaskCallbacks.onGetAnswers(arrayList);
        super.onPostExecute(arrayList);
    }
}

