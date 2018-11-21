package com.example.lexar.projecthbo_ictbv.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;

import org.json.JSONObject;

/**
 * LocationQuestionTask
 *
 * Task to get places in the near distance of the user.
 *
 * Created by wesselperik on 22/06/2018.
 */

public class LocationQuestionTask extends AsyncTask<Object, Void, JSONObject> {

    private LocationQuestionTask.Callback callback;

    public LocationQuestionTask(LocationQuestionTask.Callback callback) {
        Assert.that(callback != null, "Null callback");
        this.callback = callback;
    }

    @Override
    protected JSONObject doInBackground(Object... objects) {
        String link = Constants.API_PROTOCOL + "://" + Constants.API_URL +
                "/api/questions/" + objects[0] + "/" + objects[1] + "/" +
                objects[2] + "/" + objects[3] + "/" +
                Constants.MAX_DISTANCE_PLACE_QUESTIONS;
        Log.e("URL", link);
        try {
            ReturnPackage returnPackage = RequestManager.executeGet(link);
            Object json = returnPackage.getJson();
            return new JSONObject(json.toString());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            if (callback != null) {
                callback.onLoadingError();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        if (response != null && callback != null) {
            callback.onResponse(response);
        } else if (callback != null){
            callback.onLoadingError();
        }
    }

    /**
     * Callback for sending a response of there were questions found.
     */
    public interface Callback {
        void onResponse(JSONObject response);
        void onLoadingError();
    }
}
