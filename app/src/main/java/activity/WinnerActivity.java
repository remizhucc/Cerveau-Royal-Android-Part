package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerveauroyal.R;

import helper.AvatarHelper;
import helper.RankHelper;
import model.Constant;

public class WinnerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        initializeActivity();


    }

    private void initializeActivity() {
        int dataAvatar1 = getIntent().getIntExtra("avatar1", 1);
        int dataAvatar2 = getIntent().getIntExtra("avatar1", 1);
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

        LinearLayout root = (LinearLayout) findViewById(R.id.root);
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

    }
    public void addFriend(View view) {

    }
    public void leave(View view) {
        Intent intent = new Intent(WinnerActivity.this,
                IndexActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
    public void tryAgain(View view) {

    }
}
