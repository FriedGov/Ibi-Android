package com.example.lexar.projecthbo_ictbv.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Question.
 *
 * Our (parcelable) question model.
 */

public class Question implements Parcelable {
    private int id, languageId, categoryId;
    private String description, questionType;
    public static final String ID_KEY = "id";
    public static final String DESCRIPTION_KEY = "description";
    public static final String CATEGORY_KEY = "categoryId";
    public static final String TYPE_KEY = "questionType";
    public static final String LANGUAGE_KEY = "languageId";

    public Question(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt(ID_KEY);
        this.description = jsonObject.getString(DESCRIPTION_KEY);
        this.categoryId = jsonObject.getInt(CATEGORY_KEY);
        this.questionType = jsonObject.getString(TYPE_KEY);
        this.languageId = jsonObject.getInt(LANGUAGE_KEY);
    }

    public Question(Parcel in) {
        this.id = in.readInt();
        this.description = in.readString();
        this.categoryId = in.readInt();
        this.questionType = in.readString();
        this.languageId = in.readInt();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getQuestionType() {
        return questionType;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.description);
        dest.writeInt(this.categoryId);
        dest.writeString(this.questionType);
        dest.writeInt(this.languageId);
    }
}
