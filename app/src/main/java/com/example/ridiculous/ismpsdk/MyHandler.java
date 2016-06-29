package com.example.ridiculous.ismpsdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Ridiculous on 2016/6/20.
 */
final class MyHandler extends Handler {
    private ConfirmDialog.Builder confirmDialogBuilder;
    private VercodeDialog.Builder vercodeDialogBuilder;
    private ProgressDialog progressDialog;
    private Context context;
    private String orderId;
    private int price;
    private String phoneNum;
    private String productName;
    private String verCode;

    MyHandler(ConfirmDialog.Builder confirmDialogBuilder, VercodeDialog.Builder vercodeDialogBuilder, Context context) {
        this.confirmDialogBuilder = confirmDialogBuilder;
        this.vercodeDialogBuilder = vercodeDialogBuilder;
        this.context = context;
    }

    public final void handleMessage(final Message paramMessage) {
        if (!Thread.currentThread().isInterrupted()) {
            switch (paramMessage.what) {
                case 0:
                    JSONObject info = (JSONObject) paramMessage.obj;
                    try {
                        orderId = info.getString("orderId");
                        price = Integer.parseInt(info.getString("price"));
                        phoneNum = info.getString("phoneNum");
                        productName = info.getString("productName");
                        String showtext = String.format("您的号码是%s，即将订购%s，价格是%s元/月", phoneNum,productName, price/1000);
                        confirmDialogBuilder.setMessage(showtext);
                        confirmDialogBuilder.setTitle("确认订购");
                        confirmDialogBuilder.setBackButton("返 回", new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                                }
                            });
                        confirmDialogBuilder.setConfirmButton("确 定", new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                                //ProgressDialog progressDoalog = new ProgressDialog(context);
                                progressDialog = ProgressDialog.show(context, "In progress", "Loading");
                                RequestParams params = new RequestParams();
                                params.put("action", "subscribe");
                                params.put("phoneNum", phoneNum);
                                params.put("orderId", orderId);
                                HttpAsyncClient.post("", params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Log.d("response", response.toString());
                                        Toast.makeText(context, response.toString(),
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                //
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    confirmDialogBuilder.create().show();
                    break;
                case 1:
                    JSONObject result = (JSONObject) paramMessage.obj;
                    try {
                        orderId = result.getString("orderId");
                        price = Integer.parseInt(result.getString("price"));
                        productName = result.getString("productName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vercodeDialogBuilder.setGetButton("获取验证码", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            TextView textView = (TextView) vercodeDialogBuilder.getPhoneNumView();
                            phoneNum = textView.getText().toString();
                            RequestParams params = new RequestParams();
                            params.put("action", "subscribe");
                            params.put("phoneNum", phoneNum);
                            params.put("orderId", orderId);
                            HttpAsyncClient.post("", params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Log.d("response", response.toString());
                                    Toast.makeText(context, response.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    vercodeDialogBuilder.setPostButton("提交验证码", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            TextView textView = (TextView) vercodeDialogBuilder.getVerCodeView();
                            verCode = textView.getText().toString();
                            if (!verCode.equals("")) {
                                String showtext = String.format("您的号码是%s，即将订购%s，价格是%s元/月", phoneNum,productName, price/1000);
                                confirmDialogBuilder.setMessage(showtext);
                                confirmDialogBuilder.setTitle("确认订购");
                                confirmDialogBuilder.setBackButton("返 回", new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        dialogInterface.cancel();
                                    }
                                });
                                confirmDialogBuilder.setConfirmButton("确 定", new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        progressDialog = ProgressDialog.show(context, "In progress", "Loading");
                                        RequestParams params = new RequestParams();
                                        params.put("action", "subscribe");
                                        params.put("phoneNum", phoneNum);
                                        params.put("orderId", orderId);
                                        params.put("verCode", verCode);
                                        HttpAsyncClient.post("", params, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                Log.d("response", response.toString());
                                                Toast.makeText(context, response.toString(),
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        dialogInterface.dismiss();
                                    }
                                });
                                confirmDialogBuilder.create().show();
                                arg0.dismiss();
                            }
                            else {
                                Toast.makeText(context, "请输入验证码",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    vercodeDialogBuilder.create().show();
                    break;
                case 2:
                    break;
            }
        }
    }
}
