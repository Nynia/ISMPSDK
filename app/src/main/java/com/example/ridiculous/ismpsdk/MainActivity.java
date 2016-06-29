package com.example.ridiculous.ismpsdk;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.btn_pay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GXPay gxPay = new GXPay(MainActivity.this, gxPayCallBack);
                String spid = "1912";
                String secret = "7cb3a719e2a524f58647";
                gxPay.init(spid, secret);
                gxPay.pay("111000000000000185596","1");
            }
        });
    }
    private GXPayCallBack gxPayCallBack = new GXPayCallBack() {
        @Override
        public void execute(int result, String msg) {
            Toast.makeText(getApplicationContext(), "callback",
                    Toast.LENGTH_SHORT).show();
        }
    };
}
