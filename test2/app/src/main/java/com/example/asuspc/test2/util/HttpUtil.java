package com.example.asuspc.test2.util;

import android.graphics.Bitmap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static com.example.asuspc.test2.constant.Constant.URL_ROOT;


/**
 * Created by niezeshu on 16/7/12.
 */
public class HttpUtil {

    public static JSONObject request(String url, List<NameValuePair> params) {
        String SERVER_URL = URL_ROOT + url;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        try {
//            httpPost.setEntity(new FileEntity());
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

    public static com.alibaba.fastjson.JSONObject requestPic(String url, Bitmap[] bm){
        int count = 0;
        String SERVER_URL = URL_ROOT + url;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        try {
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            while (bm[count] != null)
            {
                File file=new File("/sdcard/" + String.valueOf(count)
                        + ".jpg");//将要保存图片的路径
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm[count].compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    FileBody cbFile = new FileBody(file, "image/jpg");
                    reqEntity.addPart(String.valueOf(count), cbFile);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                count++;
            }
            httpPost.setEntity(reqEntity);
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return com.alibaba.fastjson.JSON.parseObject(EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static com.alibaba.fastjson.JSONObject request2(String url, List<NameValuePair> params) {
        String SERVER_URL = URL_ROOT + url;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return com.alibaba.fastjson.JSON.parseObject(EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private static final String APPLICATION_JSON = "application/json";

    public static JSONObject jsonRequest(String url, Object body) {
        String SERVER_URL = URL_ROOT + url;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("body", body);
            String s = jsonObject.toString();
            System.out.println(s);
            URLEncoder.encode(s, HTTP.UTF_8);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity stringEntity = new StringEntity(s, ContentType.APPLICATION_JSON);
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPost.setEntity(stringEntity);
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
}
