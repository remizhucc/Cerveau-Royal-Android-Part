package helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import activity.IndexActivity;
import activity.LoginActivity;
import model.User;
import okhttp3.Call;
import okhttp3.Request;

import static android.content.Context.MODE_PRIVATE;

public class AccountHelper {

    //preferences
    public static int getMyIdFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        int id = userInformation.getInt("id", 0);  //second parameter default value
        return id;
    }

    public static String getMyEmailFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String email = userInformation.getString("email", null);  //second parameter default value
        return email;
    }

    public static String getMyNicknameFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String nickname = userInformation.getString("nickname", null);  //second parameter default value
        return nickname;
    }

    public static int getMyAvatarFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        int avatar = userInformation.getInt("avatar", 0);  //second parameter default value
        return avatar;
    }

    public static String getMyRankFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String rank = userInformation.getString("rank", null);  //second parameter default value
        return rank;
    }


    //login page

    public static void setMyInformationFromServer(String email,  Callback callback) throws UnsupportedEncodingException {
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/user";
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        OkHttpUtils.get()
                .url(url)
                .addParams("JSON", URLEncoder.encode(json.toString(), "utf-8"))
                .build()
                .execute(callback);
    }


    public static void setPreferences(String response,Activity activity){

        try {
            JSONObject json = new JSONObject(response);
            User user = User.read(json.getString("user"));
            SharedPreferences.Editor editor = activity.getSharedPreferences("user", 0).edit();
            editor.putString("email", user.getEmail());
            editor.putString("nickname", user.getnickname());
            editor.putInt("avatar", user.getAvatar());
            editor.putString("rank", user.getRank().toString());
            editor.putInt("id", user.getId());
            editor.apply();
        }catch (Exception e){

        }
    }

    //signup page

    public static boolean isPasswordAndComfirmPasswordMatch(String password, String password2) {
        return password.equals(password2);
    }

    public static boolean isSignUpInServerSuccess(String email, String password, String nickname, int avatar) {

        return true;
    }
}
