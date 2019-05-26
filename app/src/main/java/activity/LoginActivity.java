package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cerveauroyal.R;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import model.User;
import okhttp3.Call;
import okhttp3.Request;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = getApplicationContext();
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
                    AccountHelper.setMyInformationFromServer(email, LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong authentication", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FailToLogIn:" + e.getMessage());
            }
        }


    }
}
