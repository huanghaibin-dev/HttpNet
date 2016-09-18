package com.haibin.httpnetproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpNetClient client = new HttpNetClient();
        RequestParams params = new RequestParams();
        params.put("username", "huanghaibin_dev@163.com");
        params.put("pwd", "wh.738539302");
        params.put("keep_login", 1);
        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("http://git.oschina.net/huanghaibin_dev/HttpNet")
                .build();
        client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    Log.e("result", response.getBody());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
