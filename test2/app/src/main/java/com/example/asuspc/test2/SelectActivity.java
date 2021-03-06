package com.example.asuspc.test2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.asuspc.test2.modal.Dish;
import com.example.asuspc.test2.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    private ImageButton buttonSelect = null;
    private static ProgressDialog selectProgress;
    private static final int MSG_SELECT_RESULT = 0;
    private static Handler mHandler;

    private void handleSelectResult(JSONObject json) {


        int returnCode = json.getInteger("return_code");
        String message = json.getString("message");
        List<Dish> dishes = JSON.parseArray(json.getJSONArray("data").toJSONString(), Dish.class);
        switch (returnCode) {
            case 0:
                onSelectSuccess(dishes);
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

        setContentView(R.layout.titlebar);

        buttonSelect = (ImageButton) findViewById(R.id.id_query);

        //final EditText dishEditText = (EditText) findViewById(R.id.account);

        buttonSelect.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                //String dish = dishEditText.getText().toString();
                                                String dish = "1";
                                                select(dish);


                                            }
                                        }
        );
    }

    private void onSelectSuccess(List<Dish> data) {
        //Toast.makeText(this, data.size(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, data.get(0).getName(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "xixi", Toast.LENGTH_LONG).show();
    }


    private void select(final String dishname) {
        selectProgress = ProgressDialog.show(this, null, "查找中...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(MSG_SELECT_RESULT, connectServer(dishname));
            }
        }).start();
    }


    private static com.alibaba.fastjson.JSONObject connectServer(final String name) {
        String serverUrl = "search_dish";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", name));
        return HttpUtil.request2(serverUrl, params);
    }

    private void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }


}
