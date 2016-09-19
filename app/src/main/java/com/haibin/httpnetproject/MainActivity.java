package com.haibin.httpnetproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView)findViewById(R.id.iv);
        HttpNetClient client = new HttpNetClient();
        RequestParams params = new RequestParams();
        params.put("username", "huanghaibin_dev@163.com");
        params.put("pwd", "wh.738539302");
        params.put("keep_login", 1);
        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1308/02/c0/24056523_1375430477611.jpg")
                .build();
        client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    final Bitmap bitmap = BitmapFactory.decodeStream(response.toStream());
                    if(bitmap != null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
