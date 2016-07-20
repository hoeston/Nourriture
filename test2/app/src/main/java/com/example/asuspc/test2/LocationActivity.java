package com.example.asuspc.test2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.asuspc.test2.application.LocationApplication;
import com.example.asuspc.test2.service.LocationService;
import com.example.asuspc.test2.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 单点定位示例，用来展示基本的定位结果，配置在LocationService.java中
 * 默认配置也可以在LocationService中修改
 * 默认配置的内容自于开发者论坛中对开发者长期提出的疑问内容
 *
 * @author baidu
 */
public class LocationActivity extends Activity {
    private LocationService locationService;
    private TextView LocationResult;
    private Button startLocation;
    private static ProgressDialog locationProgress;
    private static final int MSG_LOCATION_RESULT = 0;
    private static Handler mHandler;
    boolean hasStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (mHandler == null) {
            mHandler = new Handler(getApplicationContext().getMainLooper()) {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_LOCATION_RESULT:
                            locationProgress.dismiss();
                            JSONObject json = (JSONObject) msg.obj;
                            handleLocationResult(json);
                            break;
                    }
                }
            };
        }

        setContentView(R.layout.location);
        LocationResult = (TextView) findViewById(R.id.textView1);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        startLocation = (Button) findViewById(R.id.addfence);

    }

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (LocationResult != null)
                LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的

        //注册监听
        int type = getIntent().getIntExtra("from", 0);



        startLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //if(locationService)
                if(hasStart == false){
                    locationService.registerListener(mListener);
                    locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                    locationService.start();// 定位SDK
                    hasStart = true;
                }


                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    //startLocation.setText(getString(R.string.stoplocation));

            }
        });
    }


    /*****

     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {


                locate(location.getAddrStr());


                //测试用
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                logMsg(sb.toString());
            }
        }

    };



        private void handleLocationResult(JSONObject json) {


            int returnCode = -1;
            String message = "服务器异常";
            try {
                returnCode = json.getInt("return_code");
                message = json.getString("message");
                onStop();
                hasStart = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (returnCode) {
                case 0:

                    onLocationSuccess();
                    break;
                default:
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    break;
            }
        }

        private void onLocationSuccess() {
            Toast.makeText(this, "定位成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //onStop();
            finish();
        }


        private void locate(final String addr) {
            locationProgress = ProgressDialog.show(this, null, "定位中...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessage(MSG_LOCATION_RESULT, connectServer(addr));
                }
            }).start();
        }


        private static JSONObject connectServer(final String addr) {
            String serverUrl = "search_restaurant";
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("addr", addr));
            return HttpUtil.request(serverUrl, params);
        }

        private void sendMessage(int what, Object obj) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }


}
