package com.example.vendedores;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

@SuppressLint("NewApi")
public class GcmIntentService extends IntentService{

	 public static final int NOTIFICATION_ID = 1;
	 private static final String TAG = null;
	 private NotificationManager mNotificationManager;
	 NotificationCompat.Builder builder;
	    
	public GcmIntentService() {
        super("GcmIntentService");
    }

	@Override
	protected void onHandleIntent(Intent arg0) {
		Bundle extras =arg0.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(arg0);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                
            	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
            	        .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
            	        .setContentTitle(extras.get("titulo").toString())
            	        .setTicker(extras.get("titulo").toString())
            	        .setContentText(extras.get("mensaje").toString());
            	// Creates an explicit intent for an Activity in your app

            	Intent resultIntent = new Intent(this, Login.class);
            	resultIntent.putExtra("mensaje",extras.get("mensaje").toString());
            	resultIntent.putExtra("titulo",extras.get("titulo").toString());

            	// The stack builder object will contain an artificial back stack for the
            	// started Activity.
            	// This ensures that navigating backward from the Activity leads out of
            	// your application to the Home screen.
            	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            	// Adds the back stack for the Intent (but not the Intent itself)
            	stackBuilder.addParentStack(Login.class);
            	// Adds the Intent that starts the Activity to the top of the stack
            	stackBuilder.addNextIntent(resultIntent);
            	PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            	mBuilder.setContentIntent(resultPendingIntent);
            	NotificationManager mNotificationManager =
            	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            	// mId allows you to update the notification later on.
            	mNotificationManager.notify(1, mBuilder.build());
            	
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(arg0);
		
	}
	
	// Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
        this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, GCMActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
