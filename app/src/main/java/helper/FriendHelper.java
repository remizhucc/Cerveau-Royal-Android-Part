package helper;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import activity.LoginActivity;

public class FriendHelper {
    public static void addFriend(int myId, int friendId, StringCallback callback){
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/friends";

        JSONObject json=new JSONObject();
        try {
            json.put("id", myId);
            json.put("friendId", friendId);
        }catch (JSONException e){
            System.out.println(e.getStackTrace());
        }
        OkHttpUtils.post()
                .url(url)
                .addParams("JSON", json.toString())
                .build()
                .execute(callback);
    }
}
