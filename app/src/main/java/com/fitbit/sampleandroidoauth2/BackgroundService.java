package com.fitbit.sampleandroidoauth2;

import android.app.Notification;
import android.app.Service;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bowen Xu on 12/29/2017.
 */

public class BackgroundService extends Service {
    public boolean switchON = true;
    public static final long NOTIFY_INTERVAL = 90 * 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;


    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        BackgroundService getService() {
            // Return this instance of MyService so clients can call public methods
            return BackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        useForeground();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 600, NOTIFY_INTERVAL);

    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(switchON) {
                        serviceCallbacks.update();
                    }
                }

            });
        }
    }
    public void runActivity() {
        Intent i = new Intent();
        i.setClass(this, UserDataActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }
    /*
    public void notifyUser() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.fitbit_logo)
                        .setContentTitle("Abnormal heartrate warning")
                        .setContentText("Mr Care has detected an abnormal heartrate reading.")
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(getApplicationContext(), UserDataActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
    */
    public void useForeground() {
        Intent notificationIntent = new Intent(getApplicationContext(), UserDataActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
    /* Method 01
     * this method must SET SMALLICON!
     * otherwise it can't do what we want in Android 4.4 KitKat,
     * it can only show the application info page which contains the 'Force Close' button.*/
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.fitbit_logo)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(pendingIntent);
        Notification notification = mNotifyBuilder.build();

    /* Method 02
    Notification notification = new Notification(R.drawable.ic_launcher, tickerText,
            System.currentTimeMillis());
    notification.setLatestEventInfo(PlayService.this, getText(R.string.app_name),
            currSong, pendingIntent);
    */
    startForeground(0, notification);
    }
}
