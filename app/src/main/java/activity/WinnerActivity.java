package activity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
import helper.AvatarHelper;
import helper.FriendHelper;
import helper.InvitationHelper;
import helper.NavigationBarHelper;
import helper.RankHelper;
import model.Constant;
import okhttp3.Call;
import service.MusicService;
import okhttp3.Callback;
import okhttp3.Response;

public class WinnerActivity extends Activity {
    Boolean addFriendEnable;

    ReceiveInvitationBroadcastReceiver invitationReceiver;

    private Intent MusicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winner);
        NavigationBarHelper.hideSystemUI(this);
        initializeActivity();


    }

    private void initializeActivity() {
        int dataAvatar1 = getIntent().getIntExtra("avatar1", 1);
        int dataAvatar2 = getIntent().getIntExtra("avatar2", 1);
        String dataNickName1 = getIntent().getStringExtra("nickName1");
        String dataNickName2 = getIntent().getStringExtra("nickName2");
        String dataRank1 = getIntent().getStringExtra("rank1");
        String dataRank2 = getIntent().getStringExtra("rank2");
        int dataScore1 = getIntent().getIntExtra("score1", 0);
        int dataScore2 = getIntent().getIntExtra("score2", 0);
        boolean offline = getIntent().getBooleanExtra("offline", false);

        ImageView avatar1 = (ImageView) findViewById(R.id.avatar1);
        ImageView avatar2 = (ImageView) findViewById(R.id.avatar2);
        TextView nickname1 = (TextView) findViewById(R.id.nickname1);
        TextView nickname2 = (TextView) findViewById(R.id.nickname2);
        ImageView rankImage1 = (ImageView) findViewById(R.id.rankImage1);
        ImageView rankImage2 = (ImageView) findViewById(R.id.rankImage2);
        TextView rankName1 = (TextView) findViewById(R.id.rankName1);
        TextView rankName2 = (TextView) findViewById(R.id.rankName2);
        TextView score1 = (TextView) findViewById(R.id.score1);
        TextView score2 = (TextView) findViewById(R.id.score2);

        LinearLayout root = (LinearLayout) findViewById(R.id.rootLinearLayout);
        ImageView crown1 = (ImageView) findViewById(R.id.crown1);
        ImageView crown2 = (ImageView) findViewById(R.id.crown2);


        avatar1.setImageResource(AvatarHelper.getAvatarDrawableId(dataAvatar1, this));
        avatar2.setImageResource(AvatarHelper.getAvatarDrawableId(dataAvatar2, this));
        nickname1.setText(dataNickName1);
        nickname2.setText(dataNickName2);
        rankImage1.setImageResource(RankHelper.getRankDrawableId(Constant.RANK.valueOf(dataRank1), this));
        rankImage2.setImageResource(RankHelper.getRankDrawableId(Constant.RANK.valueOf(dataRank2), this));
        rankName1.setText(RankHelper.getRankName(Constant.RANK.valueOf(dataRank1)));
        rankName2.setText(RankHelper.getRankName(Constant.RANK.valueOf(dataRank2)));
        score1.setText(String.valueOf(dataScore1));
        if (offline) {
            String sc2 = String.valueOf(dataScore2);
            sc2 += "(Escape)";
            score2.setText(sc2);
        } else {
            score2.setText(String.valueOf(dataScore2));
        }
        //set winner crown and music
        if (offline){
            crown1.setVisibility(View.INVISIBLE);
            addMusic(true);
        }else if (dataScore1 > dataScore2) {
            crown2.setVisibility(View.INVISIBLE);
            addMusic(true);
        } else {
            crown1.setVisibility(View.INVISIBLE);
            addMusic(false);
        }

        if (getIntent().getExtras().getBoolean("withFriend")) {
            addFriendEnable = false;
            hideAddFriendButton();
        } else {
            addFriendEnable = true;
        }

    }

    public void addMusic(boolean isWin){
        MusicIntent = new Intent(this, MusicService.class);
        Bundle bundle  = new Bundle();
        if(isWin){
            bundle.putInt("musicUrl", R.raw.victory);
        } else{
            bundle.putInt("musicUrl", R.raw.failure);
        }

        bundle.putBoolean("isLoop",true);
        MusicIntent.putExtras(bundle);
        startService(MusicIntent);
    }
    public void addFriend(View view) {
        if (addFriendEnable) {
            FriendHelper.addFriend(getIntent().getIntExtra("id1", 0),
                    getIntent().getStringExtra("opponentEmail"),
                    new Callback() {
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
                                    addFriendEnable = true;
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            setAddFriendButtonGrey();
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(WinnerActivity.this, "Failed add friend", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                System.out.println(e.getStackTrace());
                            }
                        }

                    });
        } else {
            Toast.makeText(WinnerActivity.this, "Already your friend", Toast.LENGTH_SHORT).show();
        }
    }

    public void leave(View view) {
        AccountHelper.setMyInformationFromServer(AccountHelper.getMyEmailFromPreferences(WinnerActivity.this), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                AccountHelper.setPreferences(responseString, WinnerActivity.this);
                Intent intent = new Intent(WinnerActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });
    }

    public void tryAgain(View view) {
        Intent intent = new Intent(WinnerActivity.this, StartGameActivity.class);
        intent.putExtra("subject", getIntent().getIntExtra("subject", 0));
        intent.putExtra("withUser", true);
        intent.putExtra("avatar", getIntent().getIntExtra("avatar_player2", 0));
        intent.putExtra("nickname", getIntent().getStringExtra("nickname_player2"));
        startActivity(intent);

    }

    private void hideAddFriendButton(){
        Button addFriendButton = (Button) findViewById(R.id.button_addFriend);
        addFriendButton.setVisibility(View.GONE);
    }

    private void setAddFriendButtonGrey() {
        Button addFriendButton = (Button) findViewById(R.id.button_addFriend);
        addFriendButton.setBackground(getDrawable(R.drawable.button_grey));
    }

    @Override
    protected void onResume() {
        invitationReceiver = InvitationHelper.registerInvitationReceiver(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        InvitationHelper.unRegisterInvitationReceiver(this, invitationReceiver);
        super.onPause();
    }


}
