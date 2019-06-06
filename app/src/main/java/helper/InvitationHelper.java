package helper;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import activity.InvitationFragment;
import broadcastReceiver.ReceiveInvitationBroadcastReceiver;

public class InvitationHelper {
    public static void sendData(InvitationFragment invitation, String jsonString) {
        try{
        JSONObject json = new JSONObject(jsonString);
        Bundle bundle = new Bundle();
        bundle.putString("user", json.getString("user"));
        bundle.putString("matchId", json.getString("matchId"));
        bundle.putString("subject", json.getString("subject"));
        invitation.setArguments(bundle);
        }catch (JSONException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static ReceiveInvitationBroadcastReceiver registerInvitationReceiver(Activity activity){
        ReceiveInvitationBroadcastReceiver receiver=new ReceiveInvitationBroadcastReceiver(activity);
        IntentFilter filter = new IntentFilter("android.intent.action.InvitationReceiver");
        activity.registerReceiver(receiver, filter);
        return receiver;
    }
    public static void unRegisterInvitationReceiver(Activity activity,ReceiveInvitationBroadcastReceiver invitationReceiver){
        activity.unregisterReceiver(invitationReceiver);
    }
}
