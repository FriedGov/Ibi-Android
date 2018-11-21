package com.example.lexar.projecthbo_ictbv.model;

import android.os.Build;

import com.example.lexar.projecthbo_ictbv.error.Assert;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Campaign.
 *
 * Our campaign model.
 */

public class Campaign {
    private int id;
    private String name, startDateString, endDateString;
    private LocalDate startDate, endDate;

    public Campaign(JSONObject jsonObject) throws Exception {
        Assert.that(jsonObject != null, "Null jsonObject");
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        this.id = jsonObject.getInt("id");
        this.name = jsonObject.getString("name");
        String tempStart = jsonObject.getString("startDate");
        this.startDateString = tempStart.substring(0, tempStart.indexOf("T"));
        String tempEnd = jsonObject.getString("endDate");
        this.endDateString = tempEnd.substring(0, tempEnd.indexOf("T"));
        dateStringToDateObject(startDateString, endDateString);
    }

    private void dateStringToDateObject(String startDateString, String endDateString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
            startDate = LocalDate.parse(startDateString, formatter);
            endDate = LocalDate.parse(endDateString, formatter);
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
