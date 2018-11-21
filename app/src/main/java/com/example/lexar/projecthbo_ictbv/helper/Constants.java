package com.example.lexar.projecthbo_ictbv.helper;

/**
 * Constants
 *
 * This class contains some variables used in different places in the app.
 * The first part of this file can be edited without any problems. Do not edit the constants beneath
 * the warning, unless you know what you're doing!
 *
 * Created by wesselperik on 22-6-2018.
 */

public class Constants {

    // API constants
    public static final String API_PROTOCOL = "http"; // the API protocol (http/https)
    public static final String API_URL = "hboict.perik.me:3000"; // the URL to the API

    // location service constants
    public static final int
            MIN_UPDATE_TIME_MILLIS = 30000; // the minimum update delay in milliseconds
    public static final int
            MIN_UPDATE_DISTANCE_METERS = 100; // the minimum update distance in meters
    public static final double
            MAX_DISTANCE_PLACE_QUESTIONS = 0.1; // max distance a place can be

    // ------------------------------------------------------------------------------------------ //
    // ----------------------- DO NOT EDIT ANYTHING UNDERNEATH THIS LINE! ----------------------- //
    // ------------------------------------------------------------------------------------------ //

    public static final String QUESTION_TYPE_OPEN = "Open question";
    public static final String QUESTION_TYPE_MULTIPLE_CHOICE = "Multiple choice";
    public static final String QUESTION_TYPE_RATING = "Rating question";
    public static final String QUESTION_TEXT = "QUESTION_TEXT";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    public static final String QUESTION_ID_KEY = "QUESTION_ID_KEY";
    public static final String LOCATION_ID_KEY = "LOCATION_ID_KEY";

    public static final int REQUEST_INTERNET_PERMISSIONS = 98;
    public static final int REQUEST_LOCATION_PERMISSIONS = 99;

    public static final String NOTIFICATION_CHANNEL_LOCATION_STATUS = "ibi_location_status";
    public static final String NOTIFICATION_CHANNEL_QUESTIONS = "ibi_location_questions";

    public static final int NOTIFICATION_ID_LOCATION_STATUS = 101;
    public static final int NOTIFICATION_ID_QUESTIONS = 102;
}
