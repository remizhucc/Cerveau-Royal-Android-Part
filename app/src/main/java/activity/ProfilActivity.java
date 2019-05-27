package activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import helper.AccountHelper;
import helper.AvatarHelper;
import helper.RankHelper;

public class ProfilActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        initializeActivity();


    }

    private void initializeActivity(){
        ImageView avatarImage = (ImageView) findViewById(R.id.avatar);
        TextView nickname = (TextView) findViewById(R.id.nickname);
        TextView email = (TextView) findViewById(R.id.email);
        ImageView rankImage = (ImageView) findViewById(R.id.rankImage);
        TextView rankName = (TextView) findViewById(R.id.rankName);


        avatarImage.setImageResource(AvatarHelper.getAvatarDrawableId(AccountHelper.getMyAvatarFromPreferences(this),this));
        nickname.setText(AccountHelper.getMyNicknameFromPreferences(this));
        email.setText(AccountHelper.getMyEmailFromPreferences(this));
        rankImage.setImageResource(RankHelper.getRankDrawableId(AccountHelper.getMyRankFromPreferences(this),this));
        rankName.setText(RankHelper.getRankName(AccountHelper.getMyRankFromPreferences(this)));

        TextView winSubject1 = (TextView) findViewById(R.id.win_geography);
        TextView winSubject2 = (TextView) findViewById(R.id.win_literature);
        TextView winSubject3 = (TextView) findViewById(R.id.win_math);
        TextView winSubject4 = (TextView) findViewById(R.id.win_history);
        TextView winSubject5 = (TextView) findViewById(R.id.win_art);
        TextView winSubject6 = (TextView) findViewById(R.id.win_music);
        TextView winSubject7 = (TextView) findViewById(R.id.win_english);
        TextView winSubject8 = (TextView) findViewById(R.id.win_commonsense);
        TextView loseSubject1 = (TextView) findViewById(R.id.lose_geography);
        TextView loseSubject2 = (TextView) findViewById(R.id.lose_literature);
        TextView loseSubject3 = (TextView) findViewById(R.id.lose_math);
        TextView loseSubject4 = (TextView) findViewById(R.id.lose_history);
        TextView loseSubject5 = (TextView) findViewById(R.id.lose_art);
        TextView loseSubject6 = (TextView) findViewById(R.id.lose_music);
        TextView loseSubject7 = (TextView) findViewById(R.id.lose_english);
        TextView loseSubject8 = (TextView) findViewById(R.id.lose_commonsense);

    }

    public void back(View view) {
        finish();
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
