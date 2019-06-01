package activity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
import model.ProfilFriends;
import model.User;
import okhttp3.Call;
import okhttp3.Request;

public class FriendsActivity extends Activity {
    private static final String TAG = "FriendsActivity";
    private List<ProfilFriends> friend = new ArrayList<>();
    private int id;
    ReceiveInvitationBroadcastReceiver invitationReceiver;

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
                .execute(new FriendsActivity.GetFriendProcessCallback());
    }

    public class GetFriendProcessCallback extends StringCallback {

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
                Boolean success = json.getBoolean("success");
                if (success) {
                    JSONArray result = json.getJSONArray("friends");
                    ProfilFriends pf;
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jb = result.getJSONObject(i);
                        pf = new ProfilFriends(User.read(jb.toString()).getnickname(), User.read(jb.toString()).getAvatar());
                        friend.add(pf);

                    }

                    FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this, R.layout.listfriends, friend);
                    ListView listview = (ListView) findViewById(R.id.list_view);
                    listview.setAdapter(adapter);


                } else {
                    Toast.makeText(FriendsActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(FriendsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FailToLogIn:" + e.getMessage());
            }
        }


    }


    public void playWithFriend(int position,ListView listView){

        int userId = -1;
        ImageView avatar2=(ImageView) findViewById(R.id.avatar);
        TextView text=(TextView) findViewById(R.id.nickname);
        String nickname = text.getText().toString();
        int avatarId=0;

        Intent intent = new Intent(this, StartGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("withUser",true);
        intent.putExtra("userId",userId);
        intent.putExtra("avatar_player2",avatarId);
        intent.putExtra("nickname_player2",nickname);

        startActivity(intent);
    }

    public void addFriend(View view){
        LinearLayout addFriendPanel = (LinearLayout) findViewById(R.id.addFriendPanel);
        EditText email = (EditText) findViewById(R.id.email);
        email.setText("");
        addFriendPanel.setVisibility(View.VISIBLE);
    }

    public void sendRequestAddFriend(View view) throws UnsupportedEncodingException {
        EditText emailEditText = (EditText) findViewById(R.id.email);
        String email = emailEditText.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(FriendsActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        } else {
            AddProcess(email);
        }



        LinearLayout addFriendPanel = (LinearLayout) findViewById(R.id.addFriendPanel);
        addFriendPanel.setVisibility(View.GONE);
    }

    public void AddProcess(String email) throws UnsupportedEncodingException {
        //String url = "http://你电脑的ip地址:8080/FirstServletDemo/servlet/HelloServlet";
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/login";
        JSONObject json = new JSONObject();
        try {

            json.put("password", AccountHelper.getMyIdFromPreferences(this));
            json.put("email", email);

        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        OkHttpUtils.get()
                .url(url)
                .addParams("JSON", URLEncoder.encode(json.toString(), "utf-8"))
                .build()
                .execute(new FriendsActivity.AddProcessCallback());

        //circle
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    public class AddProcessCallback extends StringCallback {

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
        }

        @Override
        public void onAfter() {
            super.onAfter();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                Boolean success = json.getBoolean("success");
                if (success) {
                    JSONArray result = json.getJSONArray("friends");
                    ProfilFriends pf;
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jb = result.getJSONObject(i);
                        pf = new ProfilFriends(User.read(jb.toString()).getnickname(), User.read(jb.toString()).getAvatar());
                        friend.add(pf);

                    }

                    FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this, R.layout.listfriends, friend);
                    ListView listview = (ListView) findViewById(R.id.list_view);
                    listview.setAdapter(adapter);
                } else {
                    Toast.makeText(FriendsActivity.this, "Wrong Email Address", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(FriendsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FailToAddFriend:" + e.getMessage());
            }
        }


    }
    public void cancelAddFriend(View view) {
        LinearLayout addFriendPanel = (LinearLayout) findViewById(R.id.addFriendPanel);
        addFriendPanel.setVisibility(View.GONE);
    }

    public void backFriendToIndex(View view){
        Intent intent=new Intent(FriendsActivity.this, IndexActivity.class);
        startActivity(intent);
    }
}
