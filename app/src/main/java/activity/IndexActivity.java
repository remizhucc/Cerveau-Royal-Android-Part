package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
import helper.ActivityHelper;
import helper.AvatarHelper;
import helper.InvitationHelper;
import helper.NavigationBarHelper;
import helper.RankHelper;
import model.Constant;
import service.MusicService;

public class IndexActivity extends Activity {
    ReceiveInvitationBroadcastReceiver invitationReceiver;
    private Intent MusicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        NavigationBarHelper.hideSystemUI(this);
        initializeActivity();
    }

    private void addIndexMusic(){
        MusicIntent = new Intent(this, MusicService.class);
        Bundle bundle  = new Bundle();
        bundle.putInt("musicUrl", R.raw.brighter_days);
        bundle.putBoolean("isLoop",true);
        MusicIntent.putExtras(bundle);
        startService(MusicIntent);
    }

    private void initializeActivity(){
        ImageView avatarImage = (ImageView) findViewById(R.id.avatarImage);
        TextView nickname = (TextView) findViewById(R.id.nickname);
        ImageView rankImage = (ImageView) findViewById(R.id.rankImage);
        TextView rankName = (TextView) findViewById(R.id.rankName);
        Button buttonStartGame = (Button)findViewById(R.id.button_startGame);
        Button buttonFriends = (Button)findViewById(R.id.button_friends);


        avatarImage.setImageResource(AvatarHelper.getAvatarDrawableId(AccountHelper.getMyAvatarFromPreferences(this),this));
        nickname.setText(AccountHelper.getMyNicknameFromPreferences(this));
        rankImage.setImageResource(RankHelper.getRankDrawableId(Constant.RANK.valueOf(AccountHelper.getMyRankFromPreferences(this)),this));
        rankName.setText(RankHelper.getRankName(Constant.RANK.valueOf(AccountHelper.getMyRankFromPreferences(this))));

        //add listener to button
        buttonStartGame.setOnTouchListener(new ActivityHelper.RedButtonListener());
        buttonFriends.setOnTouchListener(new ActivityHelper.GreenButtonListener());
    }

    public void directToFriends(View view) {
        Intent intent = new Intent(IndexActivity.this, FriendsActivity.class);
        startActivity(intent);
    }

    public void directToStartGame(View view) {
        Intent intent = new Intent(IndexActivity.this, StartGameActivity.class);
        startActivity(intent);
    }

    public void directToProfil(View view) {
        Intent intent = new Intent(IndexActivity.this, ProfilActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        invitationReceiver=InvitationHelper.registerInvitationReceiver(this);
        //Add music
        addIndexMusic();
        super.onResume();
    }

    @Override
    protected void onPause() {
        InvitationHelper.unRegisterInvitationReceiver(this,invitationReceiver);
        super.onPause();
    }

}
