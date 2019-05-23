package Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cerveauroyal.R;


import static helper.AccountHelper.isAuthentic;
import static helper.AccountHelper.setMyInformationFromServer;

public class LoginActivity extends Activity {

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }


    public void directToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void tryLogin(View view) {
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(email.isEmpty()||password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        }else{
            try {
                if (isAuthentic(email, password)) {
                    setMyInformationFromServer(email);
                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                } else {
                    //LENGTH_SHORT 2s LONG 3.5s
                    Toast.makeText(LoginActivity.this, "Wrong authentication", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
