package activity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cerveauroyal.R;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import helper.ActivityHelper;
import model.User;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = getApplicationContext();

        //create Listener to button
        Button buttonLogin = (Button)findViewById(R.id.button_login);
        buttonLogin.setOnTouchListener(new ActivityHelper.BlueButtonListener());
        Button buttonSignUp = (Button)findViewById(R.id.button_signup);
        buttonSignUp.setOnTouchListener(new ActivityHelper.GreyButtonListener());
    }


    public void directToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
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
            //String url = "http://你电脑的ip地址:8080/FirstServletDemo/servlet/HelloServlet";
            String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/login";
            JSONObject json = new JSONObject();
            try {
                json.put("email", email);
                json.put("password", password);

                String token = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                json.put("deviceToken", token);
            } catch (org.json.JSONException e) {
                System.out.println(e.getStackTrace());
            }
            OkHttpUtils.get()
                    .url(url)
                    .addParams("JSON", URLEncoder.encode(json.toString(), "utf-8"))
                    .build()
                    .execute(new LoginProcessCallback(email));

            //circle
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        public class LoginProcessCallback extends StringCallback {
            String email;

            LoginProcessCallback(String email) {
                super();
                this.email = email;
            }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
        }

        @Override
        public void onAfter() {
            super.onAfter();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }

        @Override
        public void onError(Call call, Exception e) {
            //do some thing lisk this
            //myText.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject json = new JSONObject(response);
                Boolean isLogin = json.getBoolean("success");
                if (isLogin) {
                    AccountHelper.setMyInformationFromServer(email, new StringCallback() {
                        @Override
                        public void onResponse(String response) {
                            AccountHelper.setPreferences(response, LoginActivity.this);
                            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Call call, Exception e) {

                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Wrong authentication", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                if(ActivityHelper.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
