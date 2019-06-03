package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerveauroyal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import broadcastReceiver.ReceiveInvitationBroadcastReceiver;
import helper.AccountHelper;
import helper.ActivityHelper;
import helper.AvatarHelper;
import helper.InvitationHelper;
import helper.RequestHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StartGameActivity extends Activity {
    int subject;
    boolean withUser;
    int userId;
    Boolean connecting;
    Activity activity;
    int avatar_player2;
    String nom_player2;

    ReceiveInvitationBroadcastReceiver invitationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame);
        activity=this;

        ImageView avatar1 = (ImageView) findViewById(R.id.avatar_player1);
        TextView nickname1 = (TextView) findViewById(R.id.nom_player1);
        avatar1.setImageResource(AvatarHelper.getAvatarDrawableId(AccountHelper.getMyAvatarFromPreferences(this), this));
        nickname1.setText(AccountHelper.getMyNicknameFromPreferences(this));
        Intent i = getIntent();
        //source = i.getStringExtra("source");

        withUser = i.getBooleanExtra("withUser", false);
        if (withUser) {
            userId = i.getIntExtra("userId", -1);
            ImageView avatar2 = (ImageView) findViewById(R.id.avatar_player2);
            TextView nickname2 = (TextView) findViewById(R.id.nom_player2);
            avatar_player2 = i.getIntExtra("avatar_player2", 0);
            nom_player2 = i.getStringExtra("nickname_player2");
            avatar2.setImageResource(AvatarHelper.getAvatarDrawableId(avatar_player2, this));
            nickname2.setText(nom_player2);
        }
        subject = -1;
        connecting = false;

        //Add listner to button
        Button buttonStartGame = (Button) findViewById(R.id.button_startGame);
        buttonStartGame.setOnTouchListener(new ActivityHelper.RedButtonListener());
    }

    public void startGame(View view) {
        if (subject == -1) {
            Toast.makeText(this, "U should choose one subject.", Toast.LENGTH_SHORT).show();
        } else if (!connecting) {
            connecting = true;
            showWaitngPanel();

            RequestHelper.httpGetRequest(
                    "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/match",
                    buildRequestMatchInfomationJsonString(),
                    new requestMatchInformationCallback());
//                OkHttpUtils.get()
//                        .url("http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/match")
//                        .addParams("JSON", URLEncoder.encode(buildRequestMatchInfomationJsonString(), "utf-8"))
//                        .build()
//                        .execute(new requestMatchInformationCallback());

        }


    }


    public class requestMatchInformationCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseString = response.body().string();
            if (connecting) {
                try {
                    JSONObject json = new JSONObject(responseString);
                    Boolean success = json.getBoolean("success");
                    if (success) {
                        Intent intent = new Intent(StartGameActivity.this, MatchActivity.class);
                        intent.putExtra("json", json.toString());
                        startActivity(intent);

                    } else {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "Failed to find opponent", Toast.LENGTH_LONG).show();
                                new CountDownTimer(3000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        //here you can have your logic to set text to edittext
                                    }

                                    public void onFinish() {
                                        hideWaitngPanel();
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

//        @Override
//        public void onError(Call call, Exception e) {
//
//        }
//
//        @Override
//        public void onResponse(String response) {
//            if (connecting) {
//                try {
//                    JSONObject json = new JSONObject(response);
//                    Boolean success = json.getBoolean("success");
//                    if (success) {
//                        Intent intent = new Intent(StartGameActivity.this, MatchActivity.class);
//                        intent.putExtra("json", json.toString());
//                        startActivity(intent);
//
//                    } else {
//                        Toast.makeText(StartGameActivity.this, "Failed to find opponent", Toast.LENGTH_LONG).show();
//                        new CountDownTimer(3000, 1000) {
//
//                            public void onTick(long millisUntilFinished) {
//                                //here you can have your logic to set text to edittext
//                            }
//
//                            public void onFinish() {
//                                hideWaitngPanel();
//                            }
//                        }.start();
//                    }
//                } catch (JSONException e) {
//                    System.out.println(e.getStackTrace());
//                }
//            }
//        }

    }

    private String buildRequestMatchInfomationJsonString() {

        JSONObject json = new JSONObject();
        try {
            json.put("id", AccountHelper.getMyIdFromPreferences(this));
            json.put("subject", subject);
            //withUesr
            if (withUser) {
                json.put("withUser", true);
                json.put("userId", userId);
            } else {
                json.put("withUser", false);
            }
        } catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }
        return json.toString();
    }


    public void setAllSubjectBgWhite() {
        ImageView background_geography = (ImageView) findViewById(R.id.geography);
        ImageView background_literature = (ImageView) findViewById(R.id.literature);
        ImageView background_math = (ImageView) findViewById(R.id.math);
        ImageView background_history = (ImageView) findViewById(R.id.history);
        ImageView background_art = (ImageView) findViewById(R.id.art);
        ImageView background_music = (ImageView) findViewById(R.id.music);
        ImageView background_english = (ImageView) findViewById(R.id.english);
        ImageView background_commonSense = (ImageView) findViewById(R.id.commonSense);
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
        ImageView background_subject1 = (ImageView) findViewById(R.id.geography);
        background_subject1.setImageResource(R.drawable.background_cercle_green);
        subject = 1;
    }

    public void chooseSubject2(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject2 = (ImageView) findViewById(R.id.literature);
        background_subject2.setImageResource(R.drawable.background_cercle_green);
        subject = 2;
    }

    public void chooseSubject3(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject3 = (ImageView) findViewById(R.id.math);
        background_subject3.setImageResource(R.drawable.background_cercle_green);
        subject = 3;
    }

    public void chooseSubject4(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject4 = (ImageView) findViewById(R.id.history);
        background_subject4.setImageResource(R.drawable.background_cercle_green);
        subject = 4;
    }

    public void chooseSubject5(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject5 = (ImageView) findViewById(R.id.art);
        background_subject5.setImageResource(R.drawable.background_cercle_green);
        subject = 5;
    }

    public void chooseSubject6(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject6 = (ImageView) findViewById(R.id.music);
        background_subject6.setImageResource(R.drawable.background_cercle_green);
        subject = 6;
    }

    public void chooseSubject7(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject7 = (ImageView) findViewById(R.id.english);
        background_subject7.setImageResource(R.drawable.background_cercle_green);
        subject = 7;
    }

    public void chooseSubject8(View view) {
        setAllSubjectBgWhite();
        ImageView background_subject8 = (ImageView) findViewById(R.id.commonSense);
        background_subject8.setImageResource(R.drawable.background_cercle_green);
        subject = 8;
    }

    public void backToIndex(View view) {
        Intent intent = new Intent(StartGameActivity.this, IndexActivity.class);
        startActivity(intent);
    }

    private void hideWaitngPanel() {
        LinearLayout waitPanel = (LinearLayout) findViewById(R.id.waitPanel);
        waitPanel.setVisibility(View.GONE);
    }

    private void showWaitngPanel() {
        LinearLayout waitPanel = (LinearLayout) findViewById(R.id.waitPanel);
        waitPanel.setVisibility(View.VISIBLE);
    }

    public void cancelMatchOpponent(View view) {
        hideWaitngPanel();
        connecting = false;
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
