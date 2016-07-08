package com.example.ridiculous.ismpsdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ridiculous on 2016/6/1.
 */
public class GXPay {
    private Context context;
    private String spId;
    private String spSecret;
    private GXPayCallBack gxPayCallBack;
    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle info = (Bundle) msg.obj;
                    Intent intent = new Intent(context, DialogActivity.class);
                    intent.putExtra("orderinfo", info);
                    context.startActivity(intent);
                    break;
                default:
                    break;
            }

        };
    };

    public GXPay(Context context, GXPayCallBack paramCtePayCallBack) {
        this.context = context;
        this.gxPayCallBack = paramCtePayCallBack;
    }
    public void init(String spId, String spSecret) {
        this.spId = spId;
        this.spSecret = spSecret;
    }
    public void pay(String productId, final String orderType) {
        String timestamp = ToolUtill.getTimestamp();
        String token = ToolUtill.SHA1(spId + timestamp + spSecret);
        RequestParams params = new RequestParams();
        params.put("action", "subscribe");
        params.put("spId", spId);
        params.put("productId", productId);
        params.put("orderType", String.valueOf(orderType));
        params.put("timestamp", timestamp);
        params.put("accessToken", token);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在发起订购请求...");
        progressDialog.show();
        HttpAsyncClient.post("", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("response", response.toString());
                progressDialog.dismiss();
                try {
                    if (response.getString("err_code").equals("0")) {
                        JSONObject orderinfo = response.getJSONObject("orderinfo");
                        Log.d("orderinfo", orderinfo.toString());
                        Bundle bundle = new Bundle();
                        bundle.putString("productName", orderinfo.getString("productName"));
                        bundle.putString("orderId", orderinfo.getString("orderId"));
                        bundle.putInt("price", Integer.parseInt(orderinfo.getString("price")));
                        bundle.putString("phoneNum", orderinfo.getString("phoneNum"));
                        bundle.putString("providerName", orderinfo.getString("providerName"));
                        Message message = Message.obtain();
                        message.arg1 = 0;
                        message.arg2 = 0;
                        message.obj = bundle;
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    else {
                        JSONObject orderinfo = response.getJSONObject("orderinfo");

                        if (orderinfo == null) {
                            Toast.makeText(context, response.getString("err_msg"), Toast.LENGTH_SHORT).show();
                        }
                        else {
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
