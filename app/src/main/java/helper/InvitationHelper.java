package helper;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import activity.InvitationFragment;

public class InvitationHelper {
    public static void sendData(InvitationFragment invitation, String jsonString) {
        try{
        JSONObject json = new JSONObject(jsonString);
        Bundle bundle = new Bundle();
        bundle.putString("user", json.getJSONObject("user").toString());
        bundle.putString("agentName", json.getString("agentName"));
        invitation.setArguments(bundle);
        }catch (JSONException e){
            System.out.println(e.getStackTrace());
        }
    }
}
