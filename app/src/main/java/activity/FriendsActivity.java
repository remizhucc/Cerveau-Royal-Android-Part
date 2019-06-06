package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
import helper.FriendHelper;
import helper.RequestHelper;
import model.ProfilFriends;
import model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FriendsActivity extends Activity {
    private static final String TAG = "FriendsActivity";
    private List<ProfilFriends> friend = new ArrayList<>();
    private int id;
    ReceiveInvitationBroadcastReceiver invitationReceiver;
    ProgressBar loadingPanel;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friends);
        activity = this;
        id = AccountHelper.getMyIdFromPreferences(this);
        try {
            getFriends(id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getFriends(int id) throws UnsupportedEncodingException {
        //String url = "http://你电脑的ip地址:8080/FirstServletDemo/servlet/HelloServlet";
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/friends";
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        RequestHelper.httpGetRequest(url, json.toString(), new FriendsActivity.GetFriendProcessCallback());
    }

    public class GetFriendProcessCallback implements Callback {


        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseString = response.body().string();
            try {
                JSONObject json = new JSONObject(responseString);
                Boolean success = json.getBoolean("success");
                if (success) {
                    JSONArray result = new JSONArray(json.getString("friends"));
                    ProfilFriends pf;
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jb = result.getJSONObject(i);
                        pf = new ProfilFriends(User.read(jb.toString()).getnickname(), User.read(jb.toString()).getAvatar(),User.read(jb.toString()).getId());
                        friend.add(pf);

                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this, R.layout.listfriends, friend);
                            ListView listview = (ListView) findViewById(R.id.list_view);
                            listview.setAdapter(adapter);
                        }
                    });


                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "FailToLogIn:" + e.getMessage());
            }
        }
    }



    public void addFriend(View view) {
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
            FriendHelper.addFriend(AccountHelper.getMyIdFromPreferences(activity), email, new FriendsActivity.AddProcessCallback());
            //circle
            loadingPanel = (ProgressBar) findViewById(R.id.loadingPanel);
            loadingPanel.setVisibility(View.VISIBLE);
        }


        LinearLayout addFriendPanel = (LinearLayout) findViewById(R.id.addFriendPanel);
        addFriendPanel.setVisibility(View.GONE);
    }

    public class AddProcessCallback implements Callback {


        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseString = response.body().string();
            try {
                JSONObject json = new JSONObject(responseString);
                Boolean success = json.getBoolean("success");
                if (success) {
                    JSONArray result = new JSONArray(json.getString("friends"));
                    ProfilFriends pf;
                    friend.clear();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jb = result.getJSONObject(i);
                        pf = new ProfilFriends(User.read(jb.toString()).getnickname(), User.read(jb.toString()).getAvatar(),User.read(jb.toString()).getId());
                        friend.add(pf);

                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this, R.layout.listfriends, friend);
                            ListView listview = (ListView) findViewById(R.id.list_view);
                            listview.setAdapter(adapter);
                            loadingPanel.setVisibility(View.GONE);
                        }
                    });

                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(FriendsActivity.this, "Wrong Email Address", Toast.LENGTH_SHORT).show();
                            loadingPanel.setVisibility(View.GONE);

                        }
                    });
                }
            } catch (JSONException e) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(FriendsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "FailToAddFriend:" + e.getMessage());
            }
        }
    }

    public void cancelAddFriend(View view) {
        LinearLayout addFriendPanel = (LinearLayout) findViewById(R.id.addFriendPanel);
        addFriendPanel.setVisibility(View.GONE);
    }

    public void backFriendToIndex(View view) {
        Intent intent = new Intent(FriendsActivity.this, IndexActivity.class);
        startActivity(intent);
    }
}
