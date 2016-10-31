package com.haibin.httpnetproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by haibin on 2016/9/24.
 */

public class HomeActivity extends AppCompatActivity {

    TextView text;
    Handler handler = new Handler();

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        text = (TextView) findViewById(R.id.text);
    }

    public void onClick(View v) {
        httpNet();
    }

    public void httpNet() {
        HttpNetClient client = new HttpNetClient();
        try {
            InputStream inputStream = getAssets().open("12306.cer");
            InputStream inputStream1 = getAssets().open("google.cer");
            client.setSslSocketFactory(inputStream, inputStream1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("GET")
                .timeout(13000)
                //.url("https://www.baidu.com/")
                .url("https://kyfw.12306.cn/otn/")
                .build();
        client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(final Response response) {
                if (response != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.show(HomeActivity.this);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Exception", e.getMessage());
            }
        });
    }
}
