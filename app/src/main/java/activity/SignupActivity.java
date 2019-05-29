package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cerveauroyal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import helper.ActivityHelper;
import okhttp3.Call;
import okhttp3.Request;

import static helper.AccountHelper.isSignUpInServerSuccess;
import static helper.AccountHelper.isPasswordAndComfirmPasswordMatch;
import static helper.AccountHelper.setMyInformationFromServer;

public class SignupActivity extends Activity {
    private int avatar;
    private static Context context;
    private String deviceToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //default avatar
        context = getApplicationContext();
        avatar = -1;

        //add listener to button
        Button buttonSignUp = (Button) findViewById(R.id.button_signup);
        buttonSignUp.setOnTouchListener(new ActivityHelper.GreenButtonListener());

    }

    public void trySignup(View view) {
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        EditText confirmPasswordEditText = (EditText) findViewById(R.id.confirmPassword);
        EditText nicknameEditText = (EditText) findViewById(R.id.nickname);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String nickname = nicknameEditText.getText().toString();


        if (!email.isEmpty() || !password.isEmpty() || !confirmPassword.isEmpty() || !nickname.isEmpty() || avatar == 0) {
            if (isPasswordAndComfirmPasswordMatch(confirmPassword, password)) {
                signUpProcess(email, password, nickname, avatar);

            } else {
                //LENGTH_SHORT 2s LONG 3.5s
                Toast.makeText(SignupActivity.this, "Comfirm password doesn't match!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignupActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUpProcess(String email, String password, String nickname, Integer avatar) {
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/user";
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            json.put("avatar", avatar);
            json.put("nickname", nickname);
            String token = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            json.put("deviceToken", token);
        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        OkHttpUtils.post()
                .url(url)
                .addParams("JSON", json.toString())
                .build()
                .execute(new SignUpProcessCallback(email));
    }

    public class SignUpProcessCallback extends StringCallback {
        String email;

        SignUpProcessCallback(String email) {
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
                Boolean success = json.getBoolean("success");
                if (success) {
                    AccountHelper.setMyInformationFromServer(email, new StringCallback() {
                        @Override
                        public void onResponse(String response) {
                            AccountHelper.setPreferences(response, SignupActivity.this);
                            Intent intent = new Intent(SignupActivity.this, IndexActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Call call, Exception e) {

                        }
                    });

                } else {
                    Toast.makeText(SignupActivity.this, "Singup failure", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void setAllAvatarBgWhite() {
        ImageView background_avatar1 = (ImageView) findViewById(R.id.background_avatar1);
        ImageView background_avatar2 = (ImageView) findViewById(R.id.background_avatar2);
        ImageView background_avatar3 = (ImageView) findViewById(R.id.background_avatar3);
        ImageView background_avatar4 = (ImageView) findViewById(R.id.background_avatar4);
        ImageView background_avatar5 = (ImageView) findViewById(R.id.background_avatar5);
        ImageView background_avatar6 = (ImageView) findViewById(R.id.background_avatar6);
        ImageView background_avatar7 = (ImageView) findViewById(R.id.background_avatar7);
        ImageView background_avatar8 = (ImageView) findViewById(R.id.background_avatar8);
        background_avatar1.setImageResource(R.drawable.background_cercle);
        background_avatar2.setImageResource(R.drawable.background_cercle);
        background_avatar3.setImageResource(R.drawable.background_cercle);
        background_avatar4.setImageResource(R.drawable.background_cercle);
        background_avatar5.setImageResource(R.drawable.background_cercle);
        background_avatar6.setImageResource(R.drawable.background_cercle);
        background_avatar7.setImageResource(R.drawable.background_cercle);
        background_avatar8.setImageResource(R.drawable.background_cercle);

    }

    public void chooseAvatar1(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar1 = (ImageView) findViewById(R.id.background_avatar1);
        background_avatar1.setImageResource(R.drawable.background_cercle_green);
        avatar = 1;
    }

    public void chooseAvatar2(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar2);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 2;
    }

    public void chooseAvatar3(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar3);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 3;
    }

    public void chooseAvatar4(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar4);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 4;
    }

    public void chooseAvatar5(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar5);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 5;
    }

    public void chooseAvatar6(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar6);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 6;
    }

    public void chooseAvatar7(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar7);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 7;
    }

    public void chooseAvatar8(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar = (ImageView) findViewById(R.id.background_avatar8);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar = 8;
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}
