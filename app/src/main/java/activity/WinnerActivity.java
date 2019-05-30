package activity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import helper.AvatarHelper;
import helper.FriendHelper;
import helper.InvitationHelper;
import helper.RankHelper;
import model.Constant;
import okhttp3.Call;

public class WinnerActivity extends Activity {
    Boolean addFriendEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        InvitationHelper.registerInvitationReceiver(this);

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

        ImageView avatar1 = (ImageView) findViewById(R.id.avatar1);
        ImageView avatar2 = (ImageView) findViewById(R.id.avatar2);
        TextView nickname1 = (TextView) findViewById(R.id.nickname1);
        TextView nickname2 = (TextView) findViewById(R.id.nickname2);
        ImageView rankImage1 = (ImageView) findViewById(R.id.rankImage1);
        ImageView rankImage2 = (ImageView) findViewById(R.id.rankImage2);
        TextView rankName1 = (TextView) findViewById(R.id.rankName1);
        TextView rankName2 = (TextView) findViewById(R.id.rankName2);

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

//set winner crown
        if (dataScore1 > dataScore2) {
            crown2.setVisibility(View.INVISIBLE);
        } else {
            crown1.setVisibility(View.INVISIBLE);
        }

        if (getIntent().getExtras().getBoolean("withFriend")) {
            addFriendEnable = false;
            setAddFriendButtonGrey();
        } else {
            addFriendEnable = true;
        }

    }

    public void addFriend(View view) {
        if (addFriendEnable) {
            FriendHelper.addFriend(getIntent().getIntExtra("id1", 0),
                    getIntent().getIntExtra("id2", 0),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                Boolean success = json.getBoolean("success");
                                if (success) {
                                    addFriendEnable = true;
                                    setAddFriendButtonGrey();
                                } else {
                                    Toast.makeText(WinnerActivity.this, "Failed add friend", Toast.LENGTH_SHORT).show();
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
        try {
            AccountHelper.setMyInformationFromServer(AccountHelper.getMyEmailFromPreferences(WinnerActivity.this), new StringCallback() {
                @Override
                public void onResponse(String response) {
                    AccountHelper.setPreferences(response, WinnerActivity.this);
                    Intent intent = new Intent(WinnerActivity.this, IndexActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(Call call, Exception e) {

                }
            });
        }catch (UnsupportedEncodingException e){
            System.out.println(e.getStackTrace());
        }


    }

    public void tryAgain(View view) {
        Intent intent = new Intent(WinnerActivity.this, StartGameActivity.class);
        intent.putExtra("subject", getIntent().getIntExtra("subject", 0));
        intent.putExtra("withUser", true);
        intent.putExtra("avatar", getIntent().getIntExtra("avatar2", 0));
        intent.putExtra("rank", getIntent().getStringExtra("rank2"));
        intent.putExtra("nickname", getIntent().getStringExtra("rank2"));
        intent.putExtra("id", getIntent().getIntExtra("rank2", 0));
        startActivity(intent);

    }

    private void setAddFriendButtonGrey() {
        Button addFriendButton = (Button) findViewById(R.id.button_addFriend);
        addFriendButton.setBackground(getDrawable(R.drawable.button_grey));
    }
    @Override
    protected void onResume() {
        InvitationHelper.registerInvitationReceiver(this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        InvitationHelper.unRegisterInvitationReceiver(this);

        super.onPause();
    }
}
