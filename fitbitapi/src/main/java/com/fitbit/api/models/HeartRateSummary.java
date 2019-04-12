package com.fitbit.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bowen Xu on 12/26/2017.
 */

public class HeartRateSummary {
    @SerializedName("activities-heart")
    @Expose
    private List<Object> activities_heart = new ArrayList<Object>();
    @SerializedName("activities-heart-intraday")
    @Expose
    private HeartRate heartrate;

    public List<Object> getActivities_heart() {
        return activities_heart;
    }

    public void setActivities_heart(List<Object> activities_heart) {
        this.activities_heart = activities_heart;
    }

    public HeartRate getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(HeartRate heartrate) {
        this.heartrate = heartrate;
    }
}
