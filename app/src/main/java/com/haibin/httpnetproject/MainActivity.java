package com.haibin.httpnetproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;
import com.haibin.httpnet.core.connect.ConnectionManager;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

        try {
            InputStream inputStream = getAssets().open("12306.cer");
            InputStream inputStream1 = getAssets().open("google.cer");
            ConnectionManager.setSslSocketFactory(inputStream, inputStream1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpNetClient client = new HttpNetClient();
        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("https://kyfw.12306.cn/otn/")
                .build();
        client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    Log.e("handler", response.getBody());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Exception", e.getMessage());
            }
        });
    }

}
