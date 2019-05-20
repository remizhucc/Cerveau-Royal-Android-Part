package Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cerveauroyal.R;

import static helper.AccountHelper.isSignUpInServerSuccess;
import static helper.AccountHelper.isPasswordAndComfirmPasswordMatch;
import static helper.AccountHelper.setMyInformationFromServer;

public class SignupActivity extends Activity {
    int avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //default avatar
        avatar = -1;
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


        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nickname.isEmpty() || avatar == 0) {
            if (isPasswordAndComfirmPasswordMatch(confirmPassword, password)) {
                try {
                    if (isSignUpInServerSuccess(email, password, nickname, avatar)) {
                        setMyInformationFromServer(email);
                        Intent intent = new Intent(SignupActivity.this, IndexActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                }
            } else {
                //LENGTH_SHORT 2s LONG 3.5s
                Toast.makeText(SignupActivity.this, "Comfirm password doesn't match!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignupActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void setAllAvatarBgWhite(){
        ImageView background_avatar1=(ImageView) findViewById(R.id.background_avatar1);
        ImageView background_avatar2=(ImageView) findViewById(R.id.background_avatar2);
        ImageView background_avatar3=(ImageView) findViewById(R.id.background_avatar3);
        ImageView background_avatar4=(ImageView) findViewById(R.id.background_avatar4);
        ImageView background_avatar5=(ImageView) findViewById(R.id.background_avatar5);
        ImageView background_avatar6=(ImageView) findViewById(R.id.background_avatar6);
        ImageView background_avatar7=(ImageView) findViewById(R.id.background_avatar7);
        ImageView background_avatar8=(ImageView) findViewById(R.id.background_avatar8);
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
        ImageView background_avatar1=(ImageView) findViewById(R.id.background_avatar1);
        background_avatar1.setImageResource(R.drawable.background_cercle_green);
        avatar=1;
    }
    public void chooseAvatar2(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar2);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=2;
    }
    public void chooseAvatar3(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar3);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=3;
    }
    public void chooseAvatar4(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar4);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=4;
    }
    public void chooseAvatar5(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar5);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=5;
    }
    public void chooseAvatar6(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar6);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=6;
    }
    public void chooseAvatar7(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar7);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=7;
    }
    public void chooseAvatar8(View view) {
        setAllAvatarBgWhite();
        ImageView background_avatar=(ImageView) findViewById(R.id.background_avatar8);
        background_avatar.setImageResource(R.drawable.background_cercle_green);
        avatar=8;
    }



}
