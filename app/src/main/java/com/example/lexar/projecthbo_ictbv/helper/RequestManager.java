package com.example.lexar.projecthbo_ictbv.helper;

import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.error.BadCodeException;
import com.example.lexar.projecthbo_ictbv.model.ReturnPackage;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * RequestManager.
 *
 * This class executes all GET and POST-requests to our API.
 *
 * Created by wesselperik on 22/06/2018.
 */
public class RequestManager {

    /**
     * Method to send a POST-request to our API.
     * @param jsonObject the JSONObject to be posted
     * @param link the URL to POST to
     * @return the response from our API
     * @throws Exception thrown when a bad resultcode is received
     */
    public static ReturnPackage executePost(JSONObject jsonObject, String link) throws Exception {
        Assert.that(jsonObject != null, "Null json object");
        Assert.that(link != null, "Null api link");
        Assert.that(!link.isEmpty(), "Api link is empty");
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        Log.i("JSON", jsonObject.toString());
        DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
        dataOutputStream.writeBytes(jsonObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        int code = conn.getResponseCode();
        String message = conn.getResponseMessage();
        Object json = new JSONTokener(stringBuilder.toString()).nextValue();
        conn.disconnect();
        if (code != 200){
            throw new BadCodeException(code, message);
        }
        return new ReturnPackage(json, code);
    }

    /**
     * Method to send a POST-request to our API.
     * @param link the URL to GET from
     * @return the response from our API
     * @throws Exception thrown when a bad resultcode is received
     */
    public static ReturnPackage executeGet(String link) throws Exception {
        Assert.that(link != null, "Null api link");
        Assert.that(!link.isEmpty(), "Api link is empty");
        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        Object json = new JSONTokener(stringBuilder.toString()).nextValue();
        int code = urlConnection.getResponseCode();
        String message = urlConnection.getResponseMessage();
        urlConnection.disconnect();
        if (code != 200){
            throw new BadCodeException(code, message);
        }
        return new ReturnPackage(json, code);
    }
}
