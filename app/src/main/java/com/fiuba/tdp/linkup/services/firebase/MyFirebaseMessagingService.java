/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fiuba.tdp.linkup.services.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.SplashActivity;
import com.fiuba.tdp.linkup.views.ChatActivity;
import com.fiuba.tdp.linkup.views.OtherProfileActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages: data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // It means app is in foreground, do not show notification!
            return;
//            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            sendNotification(remoteMessage.getData().get("type"), remoteMessage.getData().get("fromUserName"), remoteMessage.getData().get("body"), remoteMessage.getData().get("fromUserUid"));

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String type, String messageFromUserName, String messageBody, String messageFromUserId) {
        Intent intent;
        if (type.compareTo("PROFILE") == 0) {
            // open other users profile because he superliked me
            intent = new Intent(this, SplashActivity.class);
            intent.putExtra(SplashActivity.NOTIFICATION_ACTIVITY, SplashActivity.OTHER_PROFILE);

            Bundle bundle = new Bundle();
            bundle.putLong(OtherProfileActivity.ID_USER, Long.parseLong(messageFromUserId));
            bundle.putBoolean(OtherProfileActivity.IS_LIKED, false);
            bundle.putBoolean(OtherProfileActivity.IS_SUPERLIKED, false);
//            intent.putExtra(OtherProfileActivity.DISTANCE, 10);
            intent.putExtras(bundle);
        } else {
            // open chat with the other user
            intent = new Intent(this, SplashActivity.class);
            intent.putExtra(SplashActivity.NOTIFICATION_ACTIVITY, SplashActivity.CHAT_ACTIVITY);
            Bundle bundle = new Bundle();
            bundle.putString(ChatActivity.CHAT_WITH_USER_ID, messageFromUserId);
            bundle.putString(ChatActivity.CHAT_WITH_USER_NAME, messageFromUserName);
            intent.putExtras(bundle);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.messenger_bubble_large_white)
                        .setContentTitle("Notificación de " + messageFromUserName)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
