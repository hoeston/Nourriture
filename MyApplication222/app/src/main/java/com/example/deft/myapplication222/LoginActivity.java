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

public class LoginActivity extends AppCompatActivity {

    private Button buttonRegister = null;
    private Button buttonLogin = null;
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

        setContentView(R.layout.content_main222);

        buttonRegister = (Button) findViewById(R.id.button9);
        buttonLogin = (Button) findViewById(R.id.in);
        Button buttonSelect = (Button)findViewById(R.id.select);

        final EditText usernameEditText = (EditText) findViewById(R.id.account);
        final EditText passwordEditText = (EditText) findViewById(R.id.pwd);
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
                                               Intent intent = new Intent(LoginActivity.this, SelectActivityTest.class);
                                               LoginActivity.this.startActivity(intent);
                                           }
                                       }
        );

    }

    private void onLoginSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity222.class);
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
                sendMessage(MSG_LOGIN_RESULT, connectServer(username, password));
            }
        }).start();
    }


    private static JSONObject connectServer(final String username, final String password) {
        String SERVER_URL = "http://43.241.236.209/api_login.php";
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
