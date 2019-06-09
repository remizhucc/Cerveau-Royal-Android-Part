package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helper.AccountHelper;
import helper.AvatarHelper;
import helper.MatchHelper;
import helper.NavigationBarHelper;
import helper.RankHelper;
import helper.RequestHelper;
import model.Constant;
import model.Match;
import model.Question;
import model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MatchActivity extends Activity {
    private Match match;
    private int timeleft;
    private CountDownTimer countdown;
    private int myChoice;
    private boolean offline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
        NavigationBarHelper.hideSystemUI(this);
        JSONObject json;
        try {
            json = new JSONObject(getIntent().getStringExtra("json"));
            initializeMatch(json);
            nextRound();
        } catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }
    }


    private void initializeMatchModel(JSONObject json) {
        match = new Match();
        match.questions = new ArrayList<>();
        match.score1 = 0;
        match.score2 = 0;
        match.round = 0;
        match.user1 = new User(AccountHelper.getMyIdFromPreferences(MatchActivity.this),
                AccountHelper.getMyNicknameFromPreferences(MatchActivity.this),
                AccountHelper.getMyAvatarFromPreferences(MatchActivity.this),
                AccountHelper.getMyEmailFromPreferences(MatchActivity.this),
                Constant.RANK.valueOf(AccountHelper.getMyRankFromPreferences(MatchActivity.this)));
        try {
            match.withFriend = json.getBoolean("withFriend");
            match.user2 = User.read(json.getString("opponent"));
            String questionsString = json.getString("questions");
            JSONArray questions = new JSONArray(questionsString);
            match.subject = json.getInt("subject");
            for (int i = 0; i < questions.length(); i++) {
                match.questions.add(Question.read(questions.getJSONObject(i).toString()));
            }
            match.matchId = json.getString("matchId");
        } catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }


    }

    private void initializeMatch(JSONObject json) {

        initializeMatchModel(json);
        initializeUser();
        refreshScoreBar();
    }

    private void refreshScoreBar() {
        ImageView scoreBar1 = (ImageView) findViewById(R.id.scoreBar1);
        ImageView scoreBar2 = (ImageView) findViewById(R.id.scoreBar2);
        TextView score1 = (TextView) findViewById(R.id.score1);
        TextView score2 = (TextView) findViewById(R.id.score2);

        scoreBar1.getLayoutParams().height= (int) Math.round(MatchHelper.getScoreBarLenght(match.score1));
        scoreBar2.getLayoutParams().height= (int) Math.round(MatchHelper.getScoreBarLenght(match.score2));
        scoreBar1.requestLayout();
        scoreBar2.requestLayout();


        score1.setText(String.valueOf(match.score1));
        score2.setText(String.valueOf(match.score2));
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

    private void nextRound() {
        if (match.round == 10) {
            matchOver();
        } else {
            match.round++;
            myChoice = 0;
            initializeQuestion(match.questions.get(match.round - 1));
            if (countdown != null) {
                countdown.cancel();
            }
            countdown = new CountDownTimer(15000, 1000) {
                public void onTick(long millisUntilFinished) {
                    TextView countDownTextView = (TextView) findViewById(R.id.countDown);

                    countDownTextView.setText(String.valueOf(millisUntilFinished / 1000));
                    timeleft = (int) millisUntilFinished / 1000;

                }

                public void onFinish() {
                    if (myChoice == 0) {
                        sendMyChoiceToServer(0);
                    }
                }
            }.start();
        }
    }


    private void initializeQuestion(Question question) {

        TextView questionText = (TextView) findViewById(R.id.questionText);
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


    }

    public void chooseOption(View view) {
        if (timeleft > 0 && myChoice == 0) {
            switch (view.getId()) {
                case R.id.option1:
                    myChoice = 1;
                    break;
                case R.id.option2:
                    myChoice = 2;
                    break;
                case R.id.option3:
                    myChoice = 3;
                    break;
                case R.id.option4:
                    myChoice = 4;
                    break;
            }
            Button buttonChosen = (Button) findViewById(view.getId());
            buttonChosen.setBackground(getDrawable(R.drawable.button_white_storke_green));
            if (match.questions.get(match.round - 1).getAnswer() == myChoice) {
                match.score1 += timeleft * 10;
            }
            if (countdown != null) {
                countdown.cancel();
            }

            sendMyChoiceToServer(myChoice);
        }

    }


    private void matchOver() {

        //go to winner page with data
        Intent intent = new Intent(MatchActivity.this, WinnerActivity.class);
        intent.putExtra("offline", offline);
        intent.putExtra("avatar1", match.user1.getAvatar());
        intent.putExtra("avatar2", match.user2.getAvatar());
        intent.putExtra("nickName1", match.user1.getnickname());
        intent.putExtra("nickName2", match.user2.getnickname());
        intent.putExtra("rank1", match.user1.getRank().toString());
        intent.putExtra("rank2", match.user2.getRank().toString());
        intent.putExtra("score1", match.score1);
        intent.putExtra("score2", match.score2);
        intent.putExtra("id1", match.user1.getId());
        intent.putExtra("id2", match.user2.getId());
        intent.putExtra("opponentEmail", match.user2.getEmail());
        intent.putExtra("withFriend", match.withFriend);
        intent.putExtra("subject", match.subject);
        startActivity(intent);

    }

    public void backToStartGame(View view) {
        Intent intent = new Intent(MatchActivity.this, StartGameActivity.class);
        startActivity(intent);
    }

    private String buildChoiceJsonString(int choice) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", AccountHelper.getMyIdFromPreferences(MatchActivity.this));
            json.put("matchId", match.matchId);
            json.put("index", match.round);
            json.put("answer", choice);
            json.put("score", match.score1);
        } catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }
        return json.toString();
    }

    private void sendMyChoiceToServer(int choice) {
        RequestHelper.httpPostRequest("http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/match",
                buildChoiceJsonString(choice),
                new getOpponentChoiceCallback());
    }


    private class getOpponentChoiceCallback implements Callback {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseString = response.body().string();
            try {
                JSONObject json = new JSONObject(responseString);
                if (json.getBoolean("stop")) {

                    offline = true;
                    matchOver();
                } else {
                    final int opponentChoice = json.getInt("answer");
                    match.score2 = json.getInt("score");


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setRoundResult(myChoice, opponentChoice);
                            refreshScoreBar();
                            new CountDownTimer(5000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    nextRound();
                                }


                            }.start();
                        }
                    });


                }
            } catch (JSONException e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    private void setRoundResult(int choice1, int choice2) {

        int correction = match.questions.get(match.round - 1).getAnswer();

        Button option1 = (Button) findViewById(R.id.option1);
        Button option2 = (Button) findViewById(R.id.option2);
        Button option3 = (Button) findViewById(R.id.option3);
        Button option4 = (Button) findViewById(R.id.option4);

        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(option1);
        buttons.add(option2);
        buttons.add(option3);
        buttons.add(option4);

        option1.setBackground(getDrawable(R.drawable.button_grey));
        option2.setBackground(getDrawable(R.drawable.button_grey));
        option3.setBackground(getDrawable(R.drawable.button_grey));
        option4.setBackground(getDrawable(R.drawable.button_grey));
        for (int i = 1; i <= 4; i++) {
            if (correction == i) {
                if(choice1 == choice2 && choice1 == i){
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_white_storke_gradual));
                } else if (choice1 == i) {
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_white_storke_green));
                } else if (choice2 == i) {
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_white_storke_red));
                } else {
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_white));
                }
            } else {
                if(choice1 == choice2 && choice1 == i){
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_grey_storke_gradual));
                } else if (choice1 == i) {
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_grey_stroke_green));
                } else if (choice2 == i) {
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_grey_stroke_red));
                } else {
                    buttons.get(i - 1).setBackground(getDrawable(R.drawable.button_grey));
                }
            }
        }

    }
}
