package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
import helper.AvatarHelper;
import helper.InvitationHelper;
import helper.NavigationBarHelper;
import helper.RankHelper;
import model.Constant;
import model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfilActivity extends Activity {
    ReceiveInvitationBroadcastReceiver invitationReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        NavigationBarHelper.hideSystemUI(this);
        try {
            initializeActivity();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void initializeActivity() throws UnsupportedEncodingException {
        ImageView avatarImage = (ImageView) findViewById(R.id.avatar);
        TextView nickname = (TextView) findViewById(R.id.nickname);
        TextView email = (TextView) findViewById(R.id.email);
        ImageView rankImage = (ImageView) findViewById(R.id.rankImage);
        TextView rankName = (TextView) findViewById(R.id.rankName);

        avatarImage.setImageResource(AvatarHelper.getAvatarDrawableId(AccountHelper.getMyAvatarFromPreferences(this), this));
        nickname.setText(AccountHelper.getMyNicknameFromPreferences(this));
        email.setText(AccountHelper.getMyEmailFromPreferences(this));
        rankImage.setImageResource(RankHelper.getRankDrawableId(Constant.RANK.valueOf(AccountHelper.getMyRankFromPreferences(this)), this));
        rankName.setText(RankHelper.getRankName(Constant.RANK.valueOf(AccountHelper.getMyRankFromPreferences(this))));


        final TextView winSubject1 = (TextView) findViewById(R.id.win_geography);
        final TextView winSubject2 = (TextView) findViewById(R.id.win_literature);
        final TextView winSubject3 = (TextView) findViewById(R.id.win_math);
        final TextView winSubject4 = (TextView) findViewById(R.id.win_history);
        final TextView winSubject5 = (TextView) findViewById(R.id.win_art);
        final TextView winSubject6 = (TextView) findViewById(R.id.win_music);
        final TextView winSubject7 = (TextView) findViewById(R.id.win_english);
        final TextView winSubject8 = (TextView) findViewById(R.id.win_commonsense);
        final TextView loseSubject1 = (TextView) findViewById(R.id.lose_geography);
        final TextView loseSubject2 = (TextView) findViewById(R.id.lose_literature);
        final TextView loseSubject3 = (TextView) findViewById(R.id.lose_math);
        final TextView loseSubject4 = (TextView) findViewById(R.id.lose_history);
        final TextView loseSubject5 = (TextView) findViewById(R.id.lose_art);
        final TextView loseSubject6 = (TextView) findViewById(R.id.lose_music);
        final TextView loseSubject7 = (TextView) findViewById(R.id.lose_english);
        final TextView loseSubject8 = (TextView) findViewById(R.id.lose_commonsense);

        AccountHelper.setMyInformationFromServer(AccountHelper.getMyEmailFromPreferences(this), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                JSONObject json = null;
                try {
                    json = new JSONObject(responseString);
                    final User user = User.read(json.getString("user"));
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            winSubject1.setText(String.valueOf(user.getNumWinGeography()));
                            winSubject2.setText(String.valueOf(user.getNumWinLiterature()));
                            winSubject3.setText(String.valueOf(user.getNumWinMath()));
                            winSubject4.setText(String.valueOf(user.getNumWinHistory()));
                            winSubject5.setText(String.valueOf(user.getNumWinArt()));
                            winSubject6.setText(String.valueOf(user.getNumWinMusic()));
                            winSubject7.setText(String.valueOf(user.getNumWinEnglish()));
                            winSubject8.setText(String.valueOf(user.getNumWinCommonsense()));
                            loseSubject1.setText(String.valueOf(user.getNumLoseGeography()));
                            loseSubject2.setText(String.valueOf(user.getNumLoseLiterature()));
                            loseSubject3.setText(String.valueOf(user.getNumLoseMath()));
                            loseSubject4.setText(String.valueOf(user.getNumLoseHistory()));
                            loseSubject5.setText(String.valueOf(user.getNumLoseArt()));
                            loseSubject6.setText(String.valueOf(user.getNumLoseMusic()));
                            loseSubject7.setText(String.valueOf(user.getNumLoseEnglish()));
                            loseSubject8.setText(String.valueOf(user.getNumLoseCommonsense()));

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public void back(View view) {
        Intent intent = new Intent(ProfilActivity.this, IndexActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
