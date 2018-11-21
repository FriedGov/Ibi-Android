package com.example.lexar.projecthbo_ictbv.model;

import com.example.lexar.projecthbo_ictbv.error.Assert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Answer.
 *
 * Our answer model.
 */

public class Answer {
    private int id;
    private String text;
    private int amount;

    public Answer(int id, int languageId, String text) {
        Assert.that(id >=  0, "Negative answerID");
        Assert.that(languageId >=  0, "Negative languageID");
        Assert.that(!text.isEmpty(), "No answer text");
        this.id = id;
        this.text = text;
    }

    public Answer(JSONObject jsonObject) throws JSONException {
        Assert.that(jsonObject != null, "Null json object");
        this.id = jsonObject.getInt("id");
        this.text = jsonObject.getString("text");
    }

    public Answer(int id, String text, int amount) {
        Assert.that(!text.isEmpty(),  "Empty text");
        Assert.that(id >=  0, "Negative answerID");
        Assert.that(amount >=  0, "Negative amount");
        this.id = id;
        this.text = text;
        this.amount = amount;
    }


    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }


    public String getText() {
        return text;
    }
}
