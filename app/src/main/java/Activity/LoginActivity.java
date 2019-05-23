package Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cerveauroyal.R;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import model.User;
import okhttp3.Call;
import okhttp3.Request;

import static android.content.ContentValues.TAG;
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

    public void tryLogin(View view) throws UnsupportedEncodingException {
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(email.isEmpty()||password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        }else{
//            try {
//                    if (isAuthentic(email, password)) {
//                        setMyInformationFromServer(email);
//                        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
//                        startActivity(intent);
//                    } else {
//                        //LENGTH_SHORT 2s LONG 3.5s
//                        Toast.makeText(LoginActivity.this, "Wrong authentication", Toast.LENGTH_SHORT).show();
//                    }
//                }catch(Exception e){
//                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                }
                TestIsAuthentic(email, password);
        }
    }

    //just a exemple for you, it's ugly
    public void TestIsAuthentic(String email, String password) throws UnsupportedEncodingException {
        //String url = "http://你电脑的ip地址:8080/FirstServletDemo/servlet/HelloServlet";
        String url = "http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/user";
        String JSON = "{\"email\": \""+ email +"\",\"password\":\"" + password +"\"}";
        JSON = URLEncoder.encode(JSON, "utf-8");

        //first method ,not cool
//        OkHttpUtils.get()
//                .url(url + "?JSON=" + JSON)
//                .build()
//                .execute(new MyStringCallback());
        //second method ,cooler
        OkHttpUtils.get()
                .url(url)
                .addParams("JSON",JSON)
                .build()
                .execute(new MyStringCallback());

    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
            setTitle("loading...");
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
            setTitle("login");
        }

        @Override
        public void onError(Call call, Exception e)
        {
            //do some thing lisk this
            //myText.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response)
        {
//            setMyInformationFromServer(email);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = null; // read Json
            try {
                rootNode = mapper.readTree(response);
                Boolean isLogin = rootNode.path("isLogin").asBoolean();
//                HashMap<String,Object> modelMap = mapper.readValue(response,new TypeReference<HashMap<String,Object>>(){});

//                if((Boolean)modelMap.get("isLogin")){
                if(isLogin){
                    //maybe there is a way more simple to get user
//                    String userString = (String)modelMap.get("user");
//                    User user = mapper.readValue(userString, User.class);
                    rootNode.path("user").asText();
                    String userString = rootNode.path("user").asText();
                    User user = User.read(rootNode.path("user").asText());
                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void inProgress(float progress)
        {
            Log.e(TAG, "inProgress:" + progress);
        }


    }
}
