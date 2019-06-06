package service;

import android.content.Intent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(remoteMessage.getData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        sendMessageToActivity(json);

    }
    private void sendMessageToActivity(String msgJson) {
        Intent intent = new Intent("Invitation");
        intent.putExtra("json",msgJson);
        intent.setAction("android.intent.action.InvitationReceiver");
        sendBroadcast(intent);
    }
}