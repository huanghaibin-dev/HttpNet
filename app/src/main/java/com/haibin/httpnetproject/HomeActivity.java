package com.haibin.httpnetproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haibin
 * on 2016/9/24.
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
        try {
            httpNet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void httpNet() throws IOException {
        Request request = new Request.Builder()
                .url("https://my.oschina.net/huanghaibin")
                .build();
        Response response = new OkHttpClient().newCall(request)
                .execute();
        if (response != null) {

        }

    }
}
