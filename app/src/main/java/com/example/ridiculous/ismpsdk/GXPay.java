package com.example.ridiculous.ismpsdk;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ridiculous on 2016/6/1.
 */
public class GXPay {
    private Context context;
    private String spId;
    private String spSecret;
    private String baseUrl;
    private GXPayCallBack gxPayCallBack;
    private ConfirmDialog.Builder confirmDialogBuilder;
    private VercodeDialog.Builder vercodeDialogBuilder;
    public GXPay(Context context, GXPayCallBack paramCtePayCallBack) {
        this.context = context;
        this.gxPayCallBack = paramCtePayCallBack;
        this.confirmDialogBuilder = new ConfirmDialog.Builder(context);
        this.vercodeDialogBuilder = new VercodeDialog.Builder(context);
    }
    public void init(String spId, String spSecret) {
        this.spId = spId;
        this.spSecret = spSecret;
        this.baseUrl = "http://202.102.41.186:9250/ISMPOpen/serviceOrder";
    }
    public void pay(String productId, String orderType) {
        //Thread orderThread = new Thread(new FirstPayTask(productId, orderType));
        //orderThread.start();
        String timestamp = ToolUtill.getTimestamp();
        String token = Encrypt.SHA1(spId + timestamp + spSecret);
        RequestParams params = new RequestParams();
        params.put("action", "subscribe");
        params.put("spId", spId);
        params.put("productId", productId);
        params.put("orderType", String.valueOf(orderType));
        params.put("timestamp", timestamp);
        params.put("accessToken", token);
        HttpAsyncClient.post("", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("response", response.toString());
                try {
                    if (response.getString("err_code").equals("0")) {
                        JSONObject orderinfo = response.getJSONObject("orderinfo");
                        Log.d("orderinfo", orderinfo.toString());
                        MyHandler myHandler = new MyHandler(confirmDialogBuilder, null, context);

                        Message message = Message.obtain();
                        message.arg1 = 0;
                        message.arg2 = 0;
                        message.obj = orderinfo;
                        message.what = 0;
                        myHandler.sendMessage(message);
                    }
                    else {
                        JSONObject orderinfo = response.getJSONObject("orderinfo");
                        if (orderinfo == null) {
                            Toast.makeText(context, response.getString("err_msg"), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            MyHandler myHandler = new MyHandler(confirmDialogBuilder, vercodeDialogBuilder, context);
                            Message message = Message.obtain();
                            message.obj = orderinfo;
                            message.what = 1;
                            myHandler.sendMessage(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    class FirstPayTask implements Runnable {
        private String productId;
        private String orderType;
        public FirstPayTask(String productId, String orderType) {
            this.orderType = orderType;
            this.productId = productId;
        }
        @Override
        public void run() {
            Looper.prepare();
            String timestamp = ToolUtill.getTimestamp();
            String token = Encrypt.SHA1(spId + timestamp + spSecret);
            /*
            Map<String, String> params = new HashMap<>();
            params.put("action", "subscribe");
            params.put("spId", spId);
            params.put("productId", productId);
            params.put("orderType", String.valueOf(orderType));
            params.put("timestamp", timestamp);
            params.put("accessToken", token);
            String result = HttpUtils.submitPostData(baseUrl, params, "utf-8");
            Log.d("result", result);
            */
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            /*
            JSONObject json = JSON.parseObject(result);
            //System.out.println(json);
            if (json.getString("err_code").equals("0")) {
                JSONObject orderinfo = json.getJSONObject("orderinfo");
                MyHandler myHandler = new MyHandler(confirmDialogBuilder, context);

                Message message = Message.obtain();
                message.arg1 = 0;
                message.arg2 = 0;
                message.obj = orderinfo;
                message.what = 0;
                myHandler.sendMessage(message);
            }
            else {
                JSONObject orderinfo = json.getJSONObject("orderinfo");
                if (orderinfo == null) {
                    Toast.makeText(context, json.getString("err_msg"), Toast.LENGTH_SHORT).show();
                }
                else {
                    MyHandler myHandler = new MyHandler(vercodeDialogBuilder, context);
                    Message message = Message.obtain();
                    message.obj = orderinfo;
                    message.what = 1;
                    myHandler.sendMessage(message);
                }
            }
            */
            Looper.loop();
        }
    }
}
