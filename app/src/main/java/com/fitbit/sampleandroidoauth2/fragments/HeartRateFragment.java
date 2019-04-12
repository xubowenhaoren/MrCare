package com.fitbit.sampleandroidoauth2.fragments;


import com.fitbit.api.loaders.ResourceLoaderResult;
import com.fitbit.api.models.HeartRateData;
import com.fitbit.api.models.HeartRateSummary;
import com.fitbit.api.models.HeartRate;
import com.fitbit.api.services.HeartRateService;
import com.fitbit.sampleandroidoauth2.R;
import com.fitbit.sampleandroidoauth2.UserDataActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import java.util.List;
import java.util.ArrayList;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bowen Xu on 12/27/2017.
 */

public class HeartRateFragment extends InfoFragment<HeartRateSummary> {
    private HeartRateService hrs = new HeartRateService();
    private int avg;
    @Override
    public int getTitleResourceId() {
        return R.string.heartrate;
    }

    @Override
    protected int getLoaderId() {
        return 5;
    }
    @Override
    public Loader<ResourceLoaderResult<HeartRateSummary>> onCreateLoader(int id, Bundle args) {
        hrs.updateTime();
        Log.d("At"+ hrs.getTime1()+" - "+hrs.getTime2()+", actual JSON URL",hrs.toString());
        return hrs.getHeartRateSummaryLoader(getActivity());
    }
    public void notifyUser() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getContext())
                        .setSmallIcon(R.drawable.fitbit_logo)
                        .setContentTitle("Abnormal heartrate warning")
                        .setContentText("Mr Care has detected an abnormal heartrate reading.")
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(this.getContext(), this.getActivity().getClass());
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this.getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
    public void notifyUserNormal() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getContext())
                        .setSmallIcon(R.drawable.fitbit_logo)
                        .setContentTitle("Checks")
                        .setContentText("Everything looks okay.")
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(this.getContext(), this.getActivity().getClass());
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this.getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }



    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<HeartRateSummary>> loader, ResourceLoaderResult<HeartRateSummary> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindActivityData(data.getResult());
        }

    }

    public void bindActivityData(HeartRateSummary heartRateSummary) {
        StringBuilder stringBuilder = new StringBuilder();
        HeartRate heartrate= heartRateSummary.getHeartrate();
        stringBuilder.append("<b>Mr Care</b> ");
        stringBuilder.append("<br />");
        Log.d(hrs.getTime1()+" - "+hrs.getTime2(),"heartrates");
        avg = getHeartRateAvg(heartrate.getHeartrates());
        stringBuilder.append(hrs.getTime1()+" - "+hrs.getTime2());
        stringBuilder.append("<br />");

        stringBuilder.append("Average heart rate in the last minute: "+ avg);
        stringBuilder.append("<br />");
        stringBuilder.append("<b>Status: "+getStatus()+"</b>");
        setMainText(stringBuilder.toString());
    }

    public Integer getHeartRateAvg(List<HeartRateData> heartrates) {
        int sum = 0;
        if (!heartrates.isEmpty()) {
            for (HeartRateData data : heartrates) {
                sum += data.getValue();
                Log.d("HRrecord",data.getValue()+ " ");
            }
            return sum/heartrates.size();
        }
        return sum;

    }

    public String getStatus() {
        if (avg<60 || avg > 110) {
            notifyUser();
            return "ABNORMAL";
        }
        return "normal";
    }



}
