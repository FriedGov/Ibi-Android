package com.example.lexar.projecthbo_ictbv.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.error.BadCodeException;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.model.User;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * DeleteAccountTask.
 *
 * Task to delete the user's account.
 */

public class DeleteAccountTask extends AsyncTask<Object, Double, Boolean> {

    private DeleteAccountCallbacks deleteAcountCallbacks;


    public DeleteAccountTask(DeleteAccountCallbacks callback) {
        Assert.that(callback != null, "Null callback");
        this.deleteAcountCallbacks = callback;
    }

    /**
     * Callback telling the if the delete succeeded or not.
     */
    public interface DeleteAccountCallbacks {
        void deleteSuccessful();
        void deleteFailed();
    }


    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL +
                    "/api/accounts/remove/" + User.getInstance().getUserID();
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            int code = urlConnection.getResponseCode();
            String message = urlConnection.getResponseMessage();
            urlConnection.disconnect();
            if (code != 200) {
                throw new BadCodeException(code, message);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
         deleteAcountCallbacks.deleteSuccessful();
        } else {
            deleteAcountCallbacks.deleteFailed();
        }
    }
}
