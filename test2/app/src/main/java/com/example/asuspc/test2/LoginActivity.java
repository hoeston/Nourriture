package com.example.asuspc.test2;

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

import com.example.asuspc.test2.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.asuspc.test2.util.HandlerUtil.sendMessage;

public class LoginActivity extends AppCompatActivity {

    private Button buttonRegister = null;
    private Button buttonLogin = null;
    private Button buttonSelect = null;
    private static ProgressDialog loginProgress;
    private static final int MSG_LOGIN_RESULT = 0;
    private static Handler mHandler;

    private void handleLoginResult(JSONObject json) {
        /*
         * login_result:
		 * -1：登陆失败，未知错误！
		 * 0: 登陆成功！
		 * 1：登陆失败，用户名或密码错误！
		 * 2：登陆失败，用户名不存在！
		 * */
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
                onLoginSuccess();
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
                        case MSG_LOGIN_RESULT:
                            loginProgress.dismiss();
                            JSONObject json = (JSONObject) msg.obj;
                            handleLoginResult(json);
                            break;
                    }
                }
            };
        }

        setContentView(R.layout.activity_login);

        buttonRegister = (Button) findViewById(R.id.toRegisterBtn);
        buttonLogin = (Button) findViewById(R.id.loginBtn);

        final EditText usernameEditText = (EditText) findViewById(R.id.userNameEt);
        final EditText passwordEditText = (EditText) findViewById(R.id.pwdEt);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               login(usernameEditText.getText().toString(),
                                                       passwordEditText.getText().toString());
                                           }
                                       }
        );

        buttonSelect.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
                                                LoginActivity.this.startActivity(intent);
                                            }
                                        }
        );

    }

    private void onLoginSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        /*try {
            intent.putExtra("username", json.getString("username"));
            intent.putExtra("gender", json.getString("gender"));
            intent.putExtra("age", json.getInt("age"));
            intent.putExtra("phone", json.getString("phone"));
            intent.putExtra("email", json.getString("email"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        startActivity(intent);
        finish();
    }


    private void login(final String username, final String password) {
        loginProgress = ProgressDialog.show(this, null, "登陆中...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(mHandler, MSG_LOGIN_RESULT, connectServer(username, password));
            }
        }).start();
    }


    private static JSONObject connectServer(final String username, final String password) {
        String serverUrl = "login_user";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        return HttpUtil.request(serverUrl, params);
    }


}
