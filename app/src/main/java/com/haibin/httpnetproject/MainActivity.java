package com.haibin.httpnetproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Headers;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.Call;
import com.haibin.httpnet.core.call.CallBack;
import com.haibin.httpnet.core.io.JsonContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView text;
    private Handler handler = new Handler();
    private Call callExe;

    private Call callDownload;

    HttpNetClient client = new HttpNetClient();

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        text = (TextView) findViewById(R.id.text);
        AppContext.getRefWatcher().watch(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute:
                httpPostJson();
                break;
            case R.id.btn_cancel:
                if (callExe != null) {
                    callExe.cancel();
                }
                if (callDownload != null) {
                    callDownload.cancel();
                }
                break;
            case R.id.btn_download:
                httpDownload();
                break;
        }
    }

    public void httpPostJson() {

        Headers.Builder header = new Headers.Builder()
                .addHeader("Cookie", "_ga=GA1.2.2128538109.1473746167; pgv_pvi=9544373248; SERVERID=d0849c852e6ab8cf0cebe3fa386ea513|1477908232|1477908230")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Content-Type", "application/json; charset=UTF-8");

        Request request = new Request.Builder()
                .url("https://passport.cnblogs.com/user/signin")
                .content(new JsonContent("{\"input1\":\"kV/jhKdvBcnLjutqHSIG2KbTchONKXgGX3EgSKKo6z1Xb0Jk6x04+4xeu1VPONYJTazlUTuxpsyNSOAv08uOlxBDcO988rMDO/6b/B5Ozjw9WJqK73h3FnY4kbz0qCT9jCyC0X1mSNoRLh88yE1XtiQoJSLvAWfX/PZ3zYTmAT4=\",\"input2\":\"DTQ2iCphxVXZ/gYT6SMeJ0w/NaH/U+zWb9CRX8CbriAr3GLGp+StPSZw+cWK01DktC/b7XlZasQvji9NCOWYcz2Z3ppSnXmSJbKqgwHHOqI2Pezte15A/qx5Qfr3KlwdbE8Pr6v2uZSLOo/lLrurxxoQnRsyTjeQz1z8sgfqhms=\",\"remember\":false}"))
                .method("POST")
                .headers(header)
                .build();
        client.newCall(request)
                .execute(new CallBack() {
                    @Override
                    public void onResponse(Response response) {
                        Log.e("onResponse", response.getBody());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("onFailure", e.getMessage());
                    }
                });
    }

    public void httpGet() {
        client.newCall("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1306/27/c4/22626360_1372304637240_800x800.jpg")
                .execute(new CallBack() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            InputStream is = response.toStream();

                            final Bitmap bitmap = BitmapFactory.decodeStream(is);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("response", e.getMessage());
                    }
                });
    }

    public void httpDownload() {
        callDownload = client.newCall("http://f3.market.xiaomi.com/download/AppStore/0b3f6b4e06ff14b61065972a96149da822c86ad40/com.eg.android.AlipayGphone.apk");
        callDownload.execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                try {
                    InputStream is = response.toStream();
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/alipay.apk");
                    FileOutputStream os = new FileOutputStream(file);
                    int length = response.getContentLength();
                    int p = 0;
                    int bytes = 0;
                    byte[] buffer = new byte[1024];
                    while ((bytes = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytes);
                        p += bytes;
                        Log.e("progress", "进度==    " + (p / (float) length) * 100 + "%");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Exception", e.getMessage());
            }
        });
    }
}
