package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import helper.ActivityHelper;
import helper.RequestHelper;
import model.User;
import service.MusicService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import service.ReceiveInvitationService;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private Intent MusicIntent;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        activity=this;

        //Start service
        Intent serviceIntent = new Intent(this, ReceiveInvitationService.class);
        startService(serviceIntent);

        //Create Listener to button
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnTouchListener(new ActivityHelper.GreenButtonListener());
        Button buttonSignUp = (Button) findViewById(R.id.button_signup);
        buttonSignUp.setOnTouchListener(new ActivityHelper.GreyButtonListener());

        //Add music
        addLoginMusic();
    }

    private void addLoginMusic(){
        MusicIntent = new Intent(this, MusicService.class);
        Bundle bundle  = new Bundle();
        bundle.putInt("musicUrl", R.raw.rockit_sting);
        bundle.putBoolean("isLoop",false);
        MusicIntent.putExtras(bundle);
        startService(MusicIntent);
    }

    public void directToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        stopService(MusicIntent);
    }

    public void tryLogin(View view) throws UnsupportedEncodingException {
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        } else {
            loginProcess(email, password);
        }
    }

    public void loginProcess(String email, String password) throws UnsupportedEncodingException {
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/login";
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);

            json.put("deviceToken", AccountHelper.getMyTokenFromPreferences(this));
        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        RequestHelper.httpGetRequest(url, json.toString(), new LoginProcessCallback(email));

        //circle
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    public class LoginProcessCallback implements Callback {
        String email;

        LoginProcessCallback(String email) {
            super();
            this.email = email;
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseString = response.body().string();
            try {
                JSONObject json = new JSONObject(responseString);
                Boolean isLogin = json.getBoolean("success");
                if (isLogin) {
                    AccountHelper.setMyInformationFromServer(email, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseString = response.body().string();
                            AccountHelper.setPreferences(responseString, LoginActivity.this);
                            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                            startActivity(intent);
                            stopService(MusicIntent);
                        }
                    });

                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Wrong authentication", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "FailToLogIn:" + e.getMessage());
            }
        }


    }


    //keyboard hide automatically
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (ActivityHelper.isShouldHideInput(v, ev)) {
                if (ActivityHelper.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
