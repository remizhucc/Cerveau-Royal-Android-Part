package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.cerveauroyal.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import helper.AccountHelper;
import model.Friends;
import okhttp3.Call;
import okhttp3.Request;

public class FriendsActivity extends Activity {
      private static final String TAG = "FriendsActivity";
      private List<ProfilFriends> friend = new ArrayList<>();
      private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friends);
        id = AccountHelper.getMyIdFromPreferences(this);
        try {
            getFriends(id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getFriends(int id) throws UnsupportedEncodingException {
        //String url = "http://你电脑的ip地址:8080/FirstServletDemo/servlet/HelloServlet";
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/login";
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        OkHttpUtils.get()
                .url(url)
                .addParams("JSON", URLEncoder.encode(json.toString(), "utf-8"))
                .build()
                .execute(new FriendsActivity.LoginProcessCallback());
    }

    public class LoginProcessCallback extends StringCallback {

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
        }

        @Override
        public void onAfter() {
            super.onAfter();
        }

        @Override
        public void onError(Call call, Exception e) {
            //do some thing lisk this
            //myText.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject json = new JSONObject(response);
                Boolean success= json.getBoolean("success");
                if (success) {
              //      friend = json.getJSONArray("friends");
                    FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this,R.layout.listfriends,friend);
                    ListView listview = (ListView) findViewById(R.id.list_view);
                    listview.setAdapter(adapter);


                } else {
                    Toast.makeText(FriendsActivity.this, "Wrong authentication", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(FriendsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FailToLogIn:" + e.getMessage());
            }
        }


    }
}
