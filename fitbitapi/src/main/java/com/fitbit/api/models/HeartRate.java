package com.fitbit.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bowen Xu on 12/26/2017.
 */

public class HeartRate {

    @SerializedName("dataset")
    @Expose
    private List<HeartRateData> heartrates = new ArrayList<HeartRateData>();
    @SerializedName("datasetInterval")
    @Expose
    private Integer datasetInterval;
    @SerializedName("datasetType")
    @Expose
    private String datasetType;

    public List<HeartRateData> getHeartrates() {
        return heartrates;
    }

    public void setHeartrates(List<HeartRateData> heartrates) {
        this.heartrates = heartrates;
    }

    public Integer getDatasetInterval() {
        return datasetInterval;
    }

    public void setDatasetInterval(Integer datasetInterval) {
        this.datasetInterval = datasetInterval;
    }

    public String getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(String datasetType) {
        this.datasetType = datasetType;
    }
}
