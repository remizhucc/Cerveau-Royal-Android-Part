package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
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
        RequestHelper.httpGetRequest(url,json.toString(),new FriendsActivity.LoginProcessCallback());
    }

    public class LoginProcessCallback implements Callback {
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
                    Toast.makeText(FriendsActivity.this, "Wrong authentication", Toast.LENGTH_SHORT).show();
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

    public void addFriend(){

    }


    public void backFriendToIndex(View view){
        Intent intent=new Intent(FriendsActivity.this, IndexActivity.class);
        startActivity(intent);
    }
}
