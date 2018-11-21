package com.example.lexar.projecthbo_ictbv.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.error.Assert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Location.
 *
 * Our (parcelable) location model.
 */

public class Location implements Parcelable {
    private int id;
    private String name, image;
    private double latitude, longitude;
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String IMAGE_KEY = "image";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";

    public Location(JSONObject jsonObject) throws JSONException {
        Assert.that(jsonObject != null, "Null json object");
        this.id = jsonObject.getInt(ID_KEY);
        this.name = jsonObject.getString(NAME_KEY);
        this.image = jsonObject.getString(IMAGE_KEY);
        this.latitude = jsonObject.getDouble(LATITUDE_KEY);
        this.longitude = jsonObject.getDouble(LONGITUDE_KEY);
    }

    public Location(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static final Creator CREATOR = new Creator() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }
}
