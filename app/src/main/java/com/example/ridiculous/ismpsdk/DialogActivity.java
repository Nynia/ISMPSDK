package com.example.ridiculous.ismpsdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DialogActivity extends Activity {
    private String productName;
    private int productPrice;
    private String phoneNum;
    private String providerName;
    private String orderId;
    private ProgressDialog progressDialog;
    private SMSContentObserver smsContentObserver;
    private EditText editTextVerCode;
    String strVerCode;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    String verifyCode = (String)msg.obj;
                    Log.d("vercode", verifyCode);
                    editTextVerCode.setText(verifyCode);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        smsContentObserver = new SMSContentObserver(this, handler);
        registerSmsContentObservers();
        Bundle bundle = getIntent().getBundleExtra("orderinfo");

        productName = bundle.getString("productName");
        orderId = bundle.getString("orderId");
        productPrice = bundle.getInt("price");
        phoneNum = bundle.getString("phoneNum");
        providerName = bundle.getString("providerName");

        Log.d("DialogActivity", providerName);
        editTextVerCode = (EditText) findViewById(R.id.et_vercode);
        progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("获取验证码");
        progressDialog.setMessage("正在获取验证码...");
        progressDialog.show();

        TextView tv_name = (TextView) findViewById(R.id.tv_product_name);
        tv_name.setText(productName);

        TextView tv_price = (TextView) findViewById(R.id.tv_product_price);
        tv_price.setText(productPrice/1000 + "元/月");

        TextView tv_phone = (TextView) findViewById(R.id.tv_phone_num);
        tv_phone.setText(phoneNum);

        TextView tv_provider = (TextView) findViewById(R.id.tv_provider_name);
        tv_provider.setText(providerName);

        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strVerCode = editTextVerCode.getText().toString();
                RequestParams params = new RequestParams();
                params.put("action", "subscribe");
                params.put("phoneNum", phoneNum);
                params.put("orderId", orderId);
                params.put("verCode", strVerCode);
                HttpAsyncClient.post("", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("response", response.toString());
                        Toast.makeText(DialogActivity.this, response.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                DialogActivity.this.finish();
            }
        });
        ImageButton btn_close = (ImageButton) findViewById(R.id.img_entry_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogActivity.this.finish();
            }
        });

//        long start = System.currentTimeMillis();
//        strVerCode = editTextVerCode.getText().toString();
//        while (true) {
//            strVerCode = editTextVerCode.getText().toString();
//            Log.d("debug", strVerCode);
//            if (!strVerCode.equals("")) {
//                break;
//            }
//            if (System.currentTimeMillis() - start > 5000) {
//                break;
//            }
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterSmsContentObservers();
    }

    private void registerSmsContentObservers() {
        Uri smsUri = Uri.parse("content://sms");
        //smsContentObserver是SmsContentObserver的一个实例，可以在onCreate中初始化
        getContentResolver().registerContentObserver(smsUri, true, smsContentObserver);
    }

    private void unRegisterSmsContentObservers() {
        getContentResolver().unregisterContentObserver(smsContentObserver);
    }
}
