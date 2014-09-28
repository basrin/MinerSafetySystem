package com.estimote.examples.demos;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.os.CountDownTimer;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.connection.BeaconConnection;

import java.util.List;
import java.util.Random;
import java.util.Timer;

import java.util.concurrent.TimeUnit;

import static com.estimote.sdk.BeaconManager.MonitoringListener;

/**
 * Demo that shows how to use region monitoring. Two important steps are:
 * <ul>
 * <li>start monitoring region, in example in {@link #onResume()}</li>
 * <li>respond to monitoring changes by registering {@link MonitoringListener} in {@link BeaconManager}</li>
 * </ul>
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class NotifyDemoActivity extends Activity {

    private static final String TAG = NotifyDemoActivity.class.getSimpleName();
    private static final int NOTIFICATION_ID = 123;

    public BeaconConnection.BeaconCharacteristics beaconChars;

    private BeaconManager beaconManager;
    private NotificationManager notificationManager;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_demo);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
        region = new Region("regionId", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        beaconManager = new BeaconManager(this);

        // Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
        // In order for this demo to be more responsive and immediate we lower down those values.
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);



        beaconManager.setMonitoringListener(new MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                postNotification("Entered region");
                postNotificationCO2("CO2 Emissions Stable", true);
                postNotificationTemperature("25");
//                postNotificationBattery(String.valueOf(beaconChars.getBatteryPercent()));
            }

            @Override
            public void onExitedRegion(Region region) {
                postNotification("Exited region");
                postNotificationCO2("CO2 Emissions Stable", false);
                postNotificationTemperature("25");
               // postNotificationBattery();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        notificationManager.cancel(NOTIFICATION_ID);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(region);
                } catch (RemoteException e) {
                    Log.d(TAG, "Error while starting monitoring");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(NOTIFICATION_ID);
        beaconManager.disconnect();
        super.onDestroy();
    }

    private void postNotification(String msg) {
        Intent notifyIntent = new Intent(NotifyDemoActivity.this, NotifyDemoActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(NotifyDemoActivity.this, 0, new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(NotifyDemoActivity.this)
                .setSmallIcon(R.drawable.beacon_gray)
                .setContentTitle("Notify Demo")
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(NOTIFICATION_ID, notification);

        TextView statusTextView = (TextView) findViewById(R.id.status);
        statusTextView.setText(msg);
    }


    private void postNotificationCO2(final String displayMessage, boolean inRange) {

        Intent notifyIntent = new Intent(NotifyDemoActivity.this, NotifyDemoActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(NotifyDemoActivity.this, 0, new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationForCO2 = new Notification.Builder(NotifyDemoActivity.this)
                .setContentText(displayMessage)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notificationForCO2);

        final TextView statusTextView = (TextView) findViewById(R.id.co2status);
        final TextView statusTextViewTemperature = (TextView) findViewById(R.id.temperaturestatus);
        statusTextView.setText(displayMessage);

        if (inRange) {
            new CountDownTimer(10000, 1000) {
                Random random = new Random();

                @Override
                public void onTick(long l) {
                    int temperature = random.nextInt(3) + 21;
                    statusTextViewTemperature.setText(String.valueOf(temperature));
                }

                @Override
                public void onFinish() {
                    statusTextView.setText("CO2 level is too high!");
                    //MYO Vibrate

                }
            }.start();
        } else {
            // Stop vibration of MYO
        }

    }

    private void postNotificationTemperature(final String displayMessage) {

        Intent notifyIntent = new Intent(NotifyDemoActivity.this, NotifyDemoActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(NotifyDemoActivity.this, 0, new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationForCO2 = new Notification.Builder(NotifyDemoActivity.this)
                .setContentText(displayMessage)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notificationForCO2);

        final TextView statusTextView = (TextView) findViewById(R.id.temperaturestatus);
        statusTextView.setText(displayMessage);

    }

    private void postNotificationBattery(final String displayMessage) {

        Intent notifyIntent = new Intent(NotifyDemoActivity.this, NotifyDemoActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(NotifyDemoActivity.this, 0, new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationForCO2 = new Notification.Builder(NotifyDemoActivity.this)
                .setContentText(displayMessage)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notificationForCO2);

        final TextView statusTextView = (TextView) findViewById(R.id.batterystatus);
        statusTextView.setText(displayMessage);

    }
}
