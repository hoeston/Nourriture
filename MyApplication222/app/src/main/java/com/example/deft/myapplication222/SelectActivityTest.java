package com.example.deft.myapplication222;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class SelectActivityTest extends AppCompatActivity {

    private Button buttonSelect = null;
    private static ProgressDialog selectProgress;
    private static final int MSG_SELECT_RESULT = 0;
    private static Handler mHandler;

    private void handleSelectResult(JSONObject json) {


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
                onSelectSuccess();
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
                        case MSG_SELECT_RESULT:
                            selectProgress.dismiss();
                            JSONObject json = (JSONObject) msg.obj;
                            handleSelectResult(json);
                            break;
                    }
                }
            };
        }

        setContentView(R.layout.activity_select_activity_test);

        buttonSelect = (Button) findViewById(R.id.select);

        //final EditText dishEditText = (EditText) findViewById(R.id.account);

        buttonSelect.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                //String dish = dishEditText.getText().toString();
                                                String dish = "a";
                                                select(dish);


                                            }
                                        }
        );
    }

    private void onSelectSuccess() {
        Toast.makeText(this, "搜索成功", Toast.LENGTH_LONG).show();
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


    private void select(final String dish) {
        selectProgress = ProgressDialog.show(this, null, "查找中...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(MSG_SELECT_RESULT, connectServer(dish));
            }
        }).start();
    }


    private static JSONObject connectServer(final String dish) {
        String SERVER_URL = "http://43.241.236.209/db_addUser.php";
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("dish", dish));

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
