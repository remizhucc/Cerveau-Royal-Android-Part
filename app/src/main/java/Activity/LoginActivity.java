package Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cerveauroyal.R;

import static helper.AccountHelper.isAuthentic;

public class LoginActivity extends Activity {
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
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (isAuthentic(email, password)) {
            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
            startActivity(intent);
        } else {
            //LENGTH_SHORT 2s LONG 3.5s
            Toast.makeText(LoginActivity.this, "This is my Toast message!", Toast.LENGTH_SHORT).show();
        }
    }
}
