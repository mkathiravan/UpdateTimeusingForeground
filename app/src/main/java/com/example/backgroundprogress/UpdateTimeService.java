package com.example.backgroundprogress;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateTimeService extends Service {

    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static final int notify = 120000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    public static String str_receiver = "com.countdowntimerservice.receiver";
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    final static String MY_ACTION = "MY_ACTION";

    String strDate;
    Intent intent;

    private static final String PACKAGE_NAME = "com.example.backgroundprogress";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";


    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onCreate() {
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        intent = new Intent(str_receiver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getNotification();


        return START_NOT_STICKY;
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, UpdateTimeService.class);


        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launch, getString(R.string.launch_activity),
                        activityPendingIntent)
                .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentTitle("TEST")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Sample")
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }



    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    strDate = simpleDateFormat.format(calendar.getTime());
                    Log.d("strDate", strDate);

                    fn_update(strDate);
                    Toast.makeText(UpdateTimeService.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private void fn_update(String str_time){
        intent.setAction(MY_ACTION);
        intent.putExtra("time",str_time);
        sendBroadcast(intent);
    }

}
