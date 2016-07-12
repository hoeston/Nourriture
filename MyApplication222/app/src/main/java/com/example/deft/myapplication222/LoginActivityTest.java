package com.example.deft.myapplication222;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivityTest extends Activity implements OnClickListener {
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;
    private Button createButton;

    private ProgressDialog loginProgress;

    public static final int MSG_LOGIN_RESULT = 0;

    public String serverUrl = "http://43.241.236.209/db_addUser.php";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_LOGIN_RESULT:
                    loginProgress.dismiss();
                    JSONObject json = (JSONObject) msg.obj;
                    handleLoginResult(json);
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(getApplicationContext(), "默认Toast样式",
//                Toast.LENGTH_SHORT).show();
        setContentView(R.layout.content_main222);
        System.out.println("ttttttttttttt");
        initViews();
    }
    private void initViews() {
        loginUsername = (EditText)findViewById(R.id.account);
        loginPassword = (EditText)findViewById(R.id.pwd);
        loginButton   = (Button)findViewById(R.id.in);
        createButton  = (Button)findViewById(R.id.button9);

        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "默认Toast样式",
                Toast.LENGTH_SHORT).show();
        switch(v.getId()) {
            case R.id.in:
                handleLogin();
                break;
            case R.id.button9:
                handleCreateCount();
                break;
            default:
                break;
        }

    }
    private void handleLogin() {
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        login(username, password);
    }
    private void login(final String username, final String password) {
        loginProgress = new ProgressDialog(this);
        loginProgress.setCancelable(false);
        loginProgress.setCanceledOnTouchOutside(false);
        loginProgress.show(this, null, "登陆中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("bibibi", "start network!");
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(serverUrl);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                HttpResponse httpResponse = null;
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = client.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode() == 200) {
                        Log.d("bibibi", "network OK!");
                        HttpEntity entity = httpResponse.getEntity();
                        String entityString = EntityUtils.toString(entity);
                        String jsonString = entityString.substring(entityString.indexOf("{"));
                        Log.d("bibibi", "entity = " + jsonString);
                        JSONObject json = new JSONObject(jsonString);
                        sendMessage(MSG_LOGIN_RESULT, json);
                        Log.d("bibibi", "json = " + json);
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.d("bibibi", "UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.d("bibibi", "ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("bibibi", "IOException");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d("bibibi", "IOException");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void handleCreateCount() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleLoginResult(JSONObject json){
		/*
		 * login_result:
		 * -1：登陆失败，未知错误！
		 * 0: 登陆成功！
		 * 1：登陆失败，用户名或密码错误！
		 * 2：登陆失败，用户名不存在！
		 * */
        int resultCode = -1;
        try {
            resultCode = json.getInt("result_code");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch(resultCode) {
            case 0:
                onLoginSuccess(json);
                break;
            case 1:
                Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(this, "用户名不存在！", Toast.LENGTH_LONG).show();
                break;
            case -1:
            default:
                Toast.makeText(this, "登陆失败！未知错误！", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void onLoginSuccess(JSONObject json) {
        Intent intent = new Intent(this, MainActivity222.class);

        try {
            intent.putExtra("username", json.getString("username"));
            intent.putExtra("gender", json.getString("gender"));
            intent.putExtra("age", json.getInt("age"));
            intent.putExtra("phone", json.getString("phone"));
            intent.putExtra("email", json.getString("email"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startActivity(intent);
        finish();
    }
    private void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }
}