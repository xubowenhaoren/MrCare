package com.fitbit.api.services;

import com.fitbit.api.exceptions.MissingScopesException;
import com.fitbit.api.exceptions.TokenExpiredException;
import com.fitbit.api.loaders.ResourceLoaderFactory;
import com.fitbit.api.loaders.ResourceLoaderResult;
import com.fitbit.api.models.HeartRateSummary;
import com.fitbit.authentication.Scope;

import android.app.Activity;
import android.content.Loader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bowen Xu on 12/26/2017.
 */

public class HeartRateService {
    private final static String ACTIVITIES_URL = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1sec/time/%s/%s.json";
    private static final ResourceLoaderFactory<HeartRateSummary> USER_ACTIVITIES_LOADER_FACTORY = new ResourceLoaderFactory<>(ACTIVITIES_URL, HeartRateSummary.class);
    private String time1;
    private String time2;
    public Loader<ResourceLoaderResult<HeartRateSummary>> getHeartRateSummaryLoader(Activity activityContext) throws MissingScopesException, TokenExpiredException {
        return USER_ACTIVITIES_LOADER_FACTORY.newResourceLoader(activityContext, new Scope[]{Scope.heartrate},time1,time2);
    }
    public String getTime1() {
        return time1;
    }
    public String getTime2() {
        return time2;
    }
    public void updateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        cal.add(Calendar.MINUTE, -1);
        Date oneMinuteBack = cal.getTime();
        cal.add(Calendar.MINUTE, -1);
        Date twoMinutesBack = cal.getTime();
        time1 = sdf.format(twoMinutesBack);
        time2 = sdf.format(oneMinuteBack);
    }
    public String toString() {
        return String.format(ACTIVITIES_URL, time1,time2);
    }
}
