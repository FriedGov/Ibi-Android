package com.example.lexar.projecthbo_ictbv.task;

import android.os.AsyncTask;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;
import com.example.lexar.projecthbo_ictbv.helper.Constants;

import org.json.JSONObject;

/**
 * RegisterTask
 *
 * Task to register a new user
 *
 * Created by wesselperik on 22/06/2018.
 */

public class RegisterTask extends AsyncTask<Object, Double, Integer> {

    private Callback callback;

    public RegisterTask(Callback callback) {
        Assert.that(callback != null, "Null callback");
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(Object... objects) {
      Assert.that(objects.length == 4, "Objects length not 4");
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL + "/api/accounts";
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("gender", objects[0]);
            jsonParam.put("age", objects[1]);
            jsonParam.put("single", objects[2]);
            jsonParam.put("city", objects[3]);
            ReturnPackage returnPackage = RequestManager.executePost(jsonParam, link);
            JSONObject jsonObject = (JSONObject) returnPackage.getJson();
            int id = jsonObject.getInt("id");
            return id;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (integer != 0 && callback != null) {
            callback.onUserRegistered(integer);
        } else if (callback != null) {
            callback.onRegisterError();
        }
    }

    /**
     * Callback for telling the user if the registering worked or
     * not and saving the id of the new user.
     */
    public interface Callback {
        void onUserRegistered(int id);
        void onRegisterError();
    }
}
