package com.example.lexar.projecthbo_ictbv.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.helper.RequestManager;
import com.example.lexar.projecthbo_ictbv.model.Campaign;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;
import com.example.lexar.projecthbo_ictbv.helper.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ActiveCampaignsTask
 *
 * Task to get the active campaigns
 *
 * Created by wesselperik on 22/06/2018.
 */

public class ActiveCampaignsTask extends AsyncTask<Object, Double, ArrayList<Campaign>> {

    private Callback callback;

    public ActiveCampaignsTask(Callback callback) {
        Assert.that(callback != null, "Null callback");
        this.callback = callback;
    }

    @Override
    protected ArrayList<Campaign> doInBackground(Object... objects) {
        try {
            String link = Constants.API_PROTOCOL + "://" + Constants.API_URL +
                    "/api/campaigns/get-active-campaigns";
            ReturnPackage returnPackage = RequestManager.executeGet(link);
            ArrayList<Campaign> tempCampaigns = new ArrayList<>();
            Object json = returnPackage.getJson();
            if (json instanceof JSONObject) {
                JSONObject jo = new JSONObject(json.toString());
                tempCampaigns.add(new Campaign(jo));
            } else if (json instanceof JSONArray) {
                JSONArray ar = new JSONArray(json.toString());
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject object = ar.getJSONObject(i);
                    tempCampaigns.add(new Campaign(object));
                }
            }
            return tempCampaigns;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            if (callback != null) {
                callback.onLoadingError();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Campaign> arrayList) {
        super.onPostExecute(arrayList);
        if (arrayList != null && arrayList.size() != 0 && callback != null) {
            callback.onCampaignsLoaded(arrayList);
        }
    }

    /**
     * Callbacks for keeping the mainactivity up-to-date.
     */
    public interface Callback {
        void onCampaignsLoaded(ArrayList<Campaign> campaigns);
        void onLoadingError();
    }
}
