package Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerveauroyal.R;

import helper.AccountHelper;
import helper.AvatarHelper;
import helper.MatchHelper;
import helper.RankHelper;
import model.Match;
import model.Question;

public class MatchActivity extends Activity {
    private Match match;
    private int timeleft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);


    }

    private void initializeWaitingPanel() {

    }

    private void initializeMatchModel() {
        match = new Match();

    }

    private void initializeMatch() {

        initializeMatchModel();
        initializeUser();
        refreshScoreBar();
    }

    private void refreshScoreBar() {
        ImageView scoreBar1 = (ImageView) findViewById(R.id.scoreBar1);
        ImageView scoreBar2 = (ImageView) findViewById(R.id.scoreBar2);
        TextView score1 = (TextView) findViewById(R.id.score1);
        TextView score2 = (TextView) findViewById(R.id.score2);


        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                15,
                (int) Math.round(MatchHelper.getScoreBarLenght(match.score1)));
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                15,
                (int) Math.round(MatchHelper.getScoreBarLenght(match.score2)));

        score1.setText(match.score1);
        score2.setText(match.score2);
        scoreBar1.setLayoutParams(params1);
        scoreBar2.setLayoutParams(params2);
    }

    private void initializeUser() {
        ImageView avatar1 = (ImageView) findViewById(R.id.avatar1);
        ImageView avatar2 = (ImageView) findViewById(R.id.avatar2);
        TextView nickname1 = (TextView) findViewById(R.id.nickname1);
        TextView nickname2 = (TextView) findViewById(R.id.nickname2);
        ImageView rankImage1 = (ImageView) findViewById(R.id.rankImage1);
        ImageView rankImage2 = (ImageView) findViewById(R.id.rankImage2);
        TextView rankName1 = (TextView) findViewById(R.id.rankName1);
        TextView rankName2 = (TextView) findViewById(R.id.rankName2);


        avatar1.setImageResource(AvatarHelper.getAvatarDrawableId(match.user1.getAvatar(), this));
        avatar2.setImageResource(AvatarHelper.getAvatarDrawableId(match.user2.getAvatar(), this));
        nickname1.setText(match.user1.getnickname());
        nickname2.setText(match.user2.getnickname());
        rankImage1.setImageResource(RankHelper.getRankDrawableId(match.user1.getRank(), this));
        rankImage2.setImageResource(RankHelper.getRankDrawableId(match.user2.getRank(), this));
        rankName1.setText(RankHelper.getRankName(match.user1.getRank()));
        rankName2.setText(RankHelper.getRankName(match.user2.getRank()));

    }


    private void initializeQuestion(Question question) {
        TextView questionText = (TextView) findViewById(R.id.questionText);
        final TextView countDown = (TextView) findViewById(R.id.countDown);
        Button option1 = (Button) findViewById(R.id.option1);
        Button option2 = (Button) findViewById(R.id.option2);
        Button option3 = (Button) findViewById(R.id.option3);
        Button option4 = (Button) findViewById(R.id.option4);

        questionText.setText(question.getText());
        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        option4.setText(question.getOption4());
        option1.setBackground(getDrawable(R.drawable.button_white));
        option2.setBackground(getDrawable(R.drawable.button_white));
        option3.setBackground(getDrawable(R.drawable.button_white));
        option4.setBackground(getDrawable(R.drawable.button_white));

        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText(String.valueOf(millisUntilFinished / 1000));
                timeleft = (int) millisUntilFinished / 1000;

            }

            public void onFinish() {
                mTextField.setText("done!");
            }


        }.start();
    }

    public void chooseOption(View view) {
        Button buttonChosen = (Button) findViewById(view.getId());
        buttonChosen.setBackground(getDrawable(R.drawable.button_white_storkeGreen));
        sendMyChoiceToServer();
        if (answerright) {
            match.score1 += timeleft * 10;
        }
    }

    private void questionFinish() {
        Button option1 = (Button) findViewById(R.id.option1);
        Button option2 = (Button) findViewById(R.id.option2);
        Button option3 = (Button) findViewById(R.id.option3);
        Button option4 = (Button) findViewById(R.id.option4);
        option1.setBackground(getDrawable(R.drawable.button_white));
        option2.setBackground(getDrawable(R.drawable.button_white));
        option3.setBackground(getDrawable(R.drawable.button_white));
        option4.setBackground(getDrawable(R.drawable.button_white));


//add code here

        match.score2 =?;
        nextquestion();
        refreshScoreBar();
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                initializeQuestion();
            }
        }.start();
    }

    private void matchOver() {
        sendResultToServer();

        //go to winner page with data
        Intent returnIntent = new Intent();
        returnIntent.putExtra("avatar1",match.user1.getAvatar());
        returnIntent.putExtra("avatar2",match.user2.getAvatar());
        returnIntent.putExtra("nickName1",match.user1.getnickname());
        returnIntent.putExtra("nickName2",match.user2.getnickname());
        returnIntent.putExtra("rank1",match.user1.getRank());
        returnIntent.putExtra("rank2",match.user2.getRank());
        returnIntent.putExtra("score1",match.score1);
        returnIntent.putExtra("score2",match.score2);
        setResult(1,returnIntent);
        finish();
    }
    public void backToStartGame(View view) {
        finish();

    }

    private void removeWaitPanel(){
        LinearLayout waitPanel=(LinearLayout) findViewById(R.id.waitPanel);
        RelativeLayout root=(RelativeLayout) findViewById(R.id.root);
        root.removeView(waitPanel);
    }
}
