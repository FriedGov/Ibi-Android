package com.example.lexar.projecthbo_ictbv.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lexar.projecthbo_ictbv.error.Assert;

import java.util.Locale;

/**
 * User.
 *
 * Our user model.
 */
public class User  {
    /**
     * User has a location and a ID.
     */
    private static User ourInstance = null;
    private static int userID = 0;
    private double longitude, latitude;
    private final String userIdKey = "userIdKey";
    private static final String SHARED_PREF_KEY = "SHARED_PREF_KEY";
    private final String languageIdKey = "languageIdKey";
    private final int dutchId =  1;
    private final int englishId =  2;
    private int languageId;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    /*
     * Initialise model, if not done already.
     */
    public static User getInstance() {
        if (ourInstance == null) {
            ourInstance = new User();
        }
        return ourInstance;
    }


    public void saveUser(int id) {
        Assert.that(id > 0, "Bad user id");
        edit.putInt(languageIdKey, languageId);
        edit.putInt(userIdKey, id);
        edit.apply();
    }

    /**
     * Load up the user id. If there is none return false.
     * @param context
     * @return
     */
    public boolean init(Context context) {
        Assert.that(context != null, "null context");
        determineLanguageId();
        pref = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        edit = pref.edit();
        int id = pref.getInt(userIdKey, 0);
        if (id == 0) {
            return false;
        } else {
            userID = id;
            return true;
        }
    }


    public void delete(){
        edit.clear().commit();
    }

    private void determineLanguageId(){
        String local = Locale.getDefault().getISO3Language();
        if (local.equals("nld")){
            languageId = dutchId;
        } else {
            languageId = englishId;
        }
    }

    private User() {
    }

    public int getLanguageId() {
        return languageId;
    }

    public int getUserID() {
        return userID;
    }

    public void setNewLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        System.out.println("Longitude, " + longitude + " Latitude, " + latitude);
    }

}
