package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import helper.AccountHelper;
import helper.AvatarHelper;

public class StartGameActivity extends Activity {
    int subject;
    boolean withUser;
    int userId;
    String source;
    int avatar_player1;
    String nom_player1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame);
        ImageView avatar1=(ImageView) findViewById(R.id.avatar_player1);
        TextView nickname1 = (TextView) findViewById(R.id.nom_player1);
        avatar1.setImageResource(AvatarHelper.getAvatarDrawableId(AccountHelper.getMyAvatarFromPreferences(this), this));
        nickname1.setText(AccountHelper.getMyNicknameFromPreferences(this));
        Intent i = getIntent();
        //source = i.getStringExtra("source");

        withUser = i.getBooleanExtra("withUser",false);
        if (withUser)
            userId = i.getIntExtra("userId",-1);
        //default subject
        subject = -1;

        //Add listner to button
        Button buttonStartGame = (Button)findViewById(R.id.button_startGame);
        buttonStartGame.setOnTouchListener(new ActivityHelper.RedButtonListener());
    }

    public void startGame(View view) {
        Intent intent = new Intent(StartGameActivity.this, MatchActivity.class);
        intent.putExtra("subject",subject);
        intent.putExtra("withUser",withUser);
        if (withUser)
            intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void setAllSubjectBgWhite(){
        ImageView background_geography=(ImageView) findViewById(R.id.geography);
        ImageView background_literature=(ImageView) findViewById(R.id.literature);
        ImageView background_math=(ImageView) findViewById(R.id.math);
        ImageView background_history=(ImageView) findViewById(R.id.history);
        ImageView background_art=(ImageView) findViewById(R.id.art);
        ImageView background_music=(ImageView) findViewById(R.id.music);
        ImageView background_english=(ImageView) findViewById(R.id.english);
        ImageView background_commonSense=(ImageView) findViewById(R.id.commonSense);
        background_geography.setImageResource(R.drawable.background_cercle_grey);
        background_literature.setImageResource(R.drawable.background_cercle_grey);
        background_math.setImageResource(R.drawable.background_cercle_grey);
        background_history.setImageResource(R.drawable.background_cercle_grey);
        background_art.setImageResource(R.drawable.background_cercle_grey);
        background_music.setImageResource(R.drawable.background_cercle_grey);
        background_english.setImageResource(R.drawable.background_cercle_grey);
        background_commonSense.setImageResource(R.drawable.background_cercle_grey);

    }
    public void chooseSubject1(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject1=(ImageView) findViewById(R.id.geography);
        background_subject1.setImageResource(R.drawable.background_cercle_green);
        subject=1;
    }
    public void chooseSubject2(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject2=(ImageView) findViewById(R.id.literature);
        background_subject2.setImageResource(R.drawable.background_cercle_green);
        subject=2;
    }
    public void chooseSubject3(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject3=(ImageView) findViewById(R.id.math);
        background_subject3.setImageResource(R.drawable.background_cercle_green);
        subject=3;
    }
    public void chooseSubject4(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject4=(ImageView) findViewById(R.id.history);
        background_subject4.setImageResource(R.drawable.background_cercle_green);
        subject=4;
    }
    public void chooseSubject5(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject5=(ImageView) findViewById(R.id.art);
        background_subject5.setImageResource(R.drawable.background_cercle_green);
        subject=5;
    }
    public void chooseSubject6(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject6=(ImageView) findViewById(R.id.music);
        background_subject6.setImageResource(R.drawable.background_cercle_green);
        subject=6;
    }
    public void chooseSubject7(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject7=(ImageView) findViewById(R.id.english);
        background_subject7.setImageResource(R.drawable.background_cercle_green);
        subject=7;
    }
    public void chooseSubject8(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject8=(ImageView) findViewById(R.id.commonSense);
        background_subject8.setImageResource(R.drawable.background_cercle_green);
        subject=8;
    }

    public void backToIndex(View view){
        Intent intent=new Intent(StartGameActivity.this,IndexActivity.class);
        startActivity(intent);
    }
}
