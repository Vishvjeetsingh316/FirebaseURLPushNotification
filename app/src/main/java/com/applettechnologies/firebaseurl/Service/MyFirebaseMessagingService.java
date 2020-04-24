package com.applettechnologies.firebaseurl.Service;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.applettechnologies.firebaseurl.Common.Config;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        handleMessage(remoteMessage.getData().get(Config.STR_KEY));
    }

    private void handleMessage(String message) {
        Intent pushNotification = new Intent(Config.STR_PUSH);
        pushNotification.putExtra(Config.STR_MESSAGE,message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }


}
