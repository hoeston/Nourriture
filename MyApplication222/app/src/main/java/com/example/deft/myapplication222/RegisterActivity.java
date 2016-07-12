package com.example.deft.myapplication222;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.deft.myapplication222.LoginActivityTest.MSG_LOGIN_RESULT;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonRegister = null;
    private static ProgressDialog registerProgress;
    private static final int MSG_REGISTER_RESULT = 0;
    private static Handler mHandler;

    private void handleRegisterResult(JSONObject json) {


        int returnCode = -1;
        String message = "服务器异常";
        try {
            returnCode = json.getInt("return_code");
            message = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (returnCode) {
            case 0:
                onRegisterSuccess();
                break;
            default:
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mHandler == null) {
            mHandler = new Handler(getApplicationContext().getMainLooper()) {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_REGISTER_RESULT:
                            registerProgress.dismiss();
                            JSONObject json = (JSONObject) msg.obj;
                            handleRegisterResult(json);
                            break;
                    }
                }
            };
        }

        setContentView(R.layout.activity_register);

        buttonRegister = (Button) findViewById(R.id.register);

        final EditText usernameEditText = (EditText) findViewById(R.id.account);
        final EditText passwordEditText = (EditText) findViewById(R.id.pwd);
        final EditText repeatEditText = (EditText) findViewById(R.id.rpwd);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               String username = usernameEditText.getText().toString();
                                               String password = passwordEditText.getText().toString();
                                               String repeatpassword = repeatEditText.getText().toString();
                                               if(repeatpassword .equals(password)){
                                                   register(username,password);
                                               }else{

                                               }
                                           }
                                       }
        );
    }

    private void onRegisterSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity222.class);
        startActivity(intent);
        finish();
    }


    private void register(final String username, final String password) {
        registerProgress = ProgressDialog.show(this, null, "注册中...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(MSG_LOGIN_RESULT, connectServer(username, password));
            }
        }).start();
    }


    private static JSONObject connectServer(final String username, final String password) {
        String SERVER_URL = "http://43.241.236.209/db_addUser.php";
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }


}
