package com.haibin.httpnetproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Headers;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;
import com.haibin.httpnet.core.connect.SSLManager;
import com.haibin.httpnet.core.io.IO;
import com.haibin.httpnet.core.io.JsonContent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    TextView text;
    Handler handler = new Handler();

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
        httpNet();
    }

    public void httpNet() {
        HttpNetClient client = new HttpNetClient();
        try {
            InputStream is1 = getAssets().open("12306.cer");
            InputStream is2 = getAssets().open("google.cer");
            client.setSslSocketFactory(is1, is2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url("https://kyfw.12306.cn/otn")
                .build();
        client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    Log.e("response",response.getBody());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        HttpNetClient client1 = new HttpNetClient();
        Request request1 = new Request.Builder()
                .url("https://www.baidu.com")
                .build();
        client1.newCall(request1).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    Log.e("response",response.getBody());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("response",e.getMessage());
            }
        });
    }

    public void okHttp() {
        final HttpNetClient client = new HttpNetClient();
        //AppContext.getRefWatcher().watch(client);

        final Headers.Builder builder = new Headers.Builder()
//                .addHeader("Content-Type","application/json; charset=utf-8")
//                .addHeader("X-Requested-With","XMLHttpRequest")
                .addHeader("Cookie", "");

        RequestParams params = new RequestParams()
                .put("remember_me", true)
                .put("phone_num", "15018084713")
                .put("password", "wh.738539302");

        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("POST")
                .timeout(13000)
                //.headers(builder)
                .params(params)
                .url("https://www.zhihu.com/login/phone_num")
                .build();
        client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                if (response != null) {
                    Log.e("response", response.getBody());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public static void setSslSocketFactory(OkHttpClient client, InputStream... cerInputStream) {
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream is : cerInputStream) {
                X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(is);
                keyStore.setCertificateEntry("alias" + index++, cert);
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");//安全数据层

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());//信任证书管理工厂

            trustManagerFactory.init(keyStore);

            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IO.close(cerInputStream);
        }
    }
}
