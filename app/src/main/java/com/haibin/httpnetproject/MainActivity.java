package com.haibin.httpnetproject;

import android.app.Activity;
import android.content.Intent;
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
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.Call;
import com.haibin.httpnet.core.call.CallBack;

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
        //LeakSingle.getInstance(this.getApplication()).setRetainedTextView(text);
        AppContext.getRefWatcher().watch(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute:
                httpPost();
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

    public void httpPost() {

        Headers.Builder header = new Headers.Builder()
                .addHeader("Cookie", ".CNBlogsCookie=FF856CE1EB117C096AEEDAD923D81F64D1495E1CC50A7B54940F6996A510F87D7326E98101CE99AC2A1ACAF225064F151F77EFD6F3E9A59D78CFAC8013901E2FF858BEB0A12EA63AA9FDBA5AE23C0E9A3FE51D9C16273CC8EA6D1967DACF2D03D6114065; _gat=1; _ga=GA1.2.2128538109.1473746167");
        RequestParams params = new RequestParams()
                .putFile("qqfile", "/storage/emulated/0/DCIM/Camera/IMG_20160909_084119.jpg");
        Request request = new Request.Builder()
                .url("http://upload.cnblogs.com/ImageUploader/TemporaryAvatarUpload")
                .headers(header)
                .params(params)
                .method("POST")
                .build();

        callExe = client.newCall(request);
        callExe.execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    Log.e("response", response.getBody());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("response", e.getMessage());
            }
        });
    }

    public void httpDownload() {
        Request request = new Request.Builder()
                .url("http://f3.market.xiaomi.com/download/AppStore/0b3f6b4e06ff14b61065972a96149da822c86ad40/com.eg.android.AlipayGphone.apk")
                .method("GET")
                .build();
        callDownload = client.newCall(request);
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
