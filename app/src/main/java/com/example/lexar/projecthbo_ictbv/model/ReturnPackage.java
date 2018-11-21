package com.example.lexar.projecthbo_ictbv.model;

import com.example.lexar.projecthbo_ictbv.error.Assert;

/**
 * ReturnPackage.
 *
 * Our return package model, to convert the API response into a model.
 */
public class ReturnPackage {

    private Object json;
    private int code;

    public ReturnPackage(Object json, int code) {
        Assert.that(json != null, "Null json");
        this.json = json;
        this.code = code;
    }

    public Object getJson() {
        return json;
    }

    public int getCode() {
        return code;
    }
}
