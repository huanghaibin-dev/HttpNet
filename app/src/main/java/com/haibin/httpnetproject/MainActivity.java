package com.haibin.httpnetproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Headers;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;

import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");

            X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(getAssets().open("ad.cer"));
            PublicKey pk = Cert.getPublicKey();
            if (pk != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpNetClient client = new HttpNetClient();
        RequestParams params = new RequestParams();
        params.put("username", "huanghaibin_dev@163.com");
        params.put("pwd", "wh.738539302");
        params.put("keep_login", 1);
        Headers.Builder headers = new Headers.Builder()
                .addHeader("X-Requested-With","XMLHttpRequest")
                .addHeader("Host","www.oschina.net");
        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("POST")
                .params(params)
                .timeout(13000)
                .url("https://www.oschina.net/action/api/login_validate")
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
                Log.e("Exception", e.getMessage());
            }
        });
    }
}
