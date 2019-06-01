package helper;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;

public class FriendHelper {
    public static void addFriend(int myId, int friendId, Callback callback){
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/friends";

        JSONObject json=new JSONObject();
        try {
            json.put("id", myId);
            json.put("friendId", friendId);
        }catch (JSONException e){
            System.out.println(e.getStackTrace());
        }
        RequestHelper.httpPostRequest(url,json.toString(),callback);

    }
}
