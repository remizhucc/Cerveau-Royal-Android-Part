package service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import helper.AccountHelper;

public class ReceiveInvitationService extends FirebaseMessagingService {
    private static final String TAG = "ReceiveInvitationService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AccountHelper.setMyTokenFromPreferences(this,s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String json=remoteMessage.getData().get("JSON");
        sendMessageToActivity(json);

    }
    private void sendMessageToActivity(String msgJson) {
        Intent intent = new Intent("Invitation");
        intent.putExtra("json",msgJson);
        intent.setAction("InvitationReceiver");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}