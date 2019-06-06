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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import helper.AccountHelper;
import helper.ActivityHelper;
import helper.RequestHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static helper.AccountHelper.getMyTokenFromPreferences;
import static helper.AccountHelper.isPasswordAndComfirmPasswordMatch;

public class SignupActivity extends Activity {
    private int avatar;
    private static Context context;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        activity=this;
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
            json.put("deviceToken", getMyTokenFromPreferences(this));
        } catch (org.json.JSONException e) {
            System.out.println(e.getStackTrace());
        }
        RequestHelper.httpPostRequest(url,json.toString(),new SignUpProcessCallback(email));
    }

    public class SignUpProcessCallback implements Callback {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseString = response.body().string();
            try {
                JSONObject json = new JSONObject(responseString);
                Boolean success = json.getBoolean("success");
                if (success) {
                    AccountHelper.setMyInformationFromServer(email, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseString = response.body().string();
                            AccountHelper.setPreferences(responseString, SignupActivity.this);
                            Intent intent = new Intent(SignupActivity.this, IndexActivity.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Singup failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        String email;

        SignUpProcessCallback(String email) {
            super();
            this.email = email;
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
