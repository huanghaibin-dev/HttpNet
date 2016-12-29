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
import android.widget.Toast;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Headers;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.Call;
import com.haibin.httpnet.core.call.Callback;
import com.haibin.httpnet.core.call.InterceptListener;
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
                //login();
                //httpPostJson();
                executeTest();
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

    public void executeTest() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url("http://upload.cnblogs.com/ImageUploader/TemporaryAvatarUpload")
                            .method("POST")
                            .params(new RequestParams()
                                    .putFile("qqfile", "/storage/emulated/0/DCIM/Camera/339718150.jpeg"))
                            .headers(new Headers.Builder().addHeader("Cookie", "CNZZDATA1259029673=2072545293-1479795067-null%7C1479795067; lhb_smart_1=1; __utma=226521935.1789795872.1480996255.1480996255.1480996255.1; __utmz=226521935.1480996255.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; .CNBlogsCookie=A6783E37E1040979421EC4A57A2FEFBB74B65BB51C7345AC99B64A7065293F59A79C6830C60D71629E8D28A332436E23CD40968EB58AA830CBD0F0733438F9A7627C074DB0462C2576D206D3752E640871E8CB23D1A50B0A9962C158466EE81425B1E516; _gat=1; _ga=GA1.2.1789795872.1480996255"))
                            .build();

                    final Response response = client.newCall(request).execute();
                    if (response != null) {
                        Log.e("is",response.getBody());
//                        String body =  response.getBody();
//                        if(body != null){
//
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
                .execute(new Callback() {
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

    public void upload() {
        Request request = new Request.Builder()
                .url("http://upload.cnblogs.com/ImageUploader/TemporaryAvatarUpload")
                .method("POST")
                .params(new RequestParams()
                        .putFile("qqfile", "/storage/emulated/0/DCIM/Camera/IMG_20160909_080844.jpg"))
                .headers(new Headers.Builder().addHeader("Cookie", "pgv_pvi=9544373248; .CNBlogsCookie=CA5152A644BF0710FB4CFFE2D1634FEE921CB1201E01962ACCEAEE2417BC6AE649E30F5C6DD63FC40ED6B064E4709B1656F8273AE2050DE1FAC47CE884FDFE6D430BAA80271DF15ADAD159FCDF0F37C7AC3B987FFA9ED210939E0650C08D42F84C0FC029; _ga=GA1.2.2128538109.1473746167; _gat=1"))
                .build();
        client.newCall(request)
                .intercept(new InterceptListener() {
                    @Override
                    public void onProgress(final int index, final long currentLength, final long totalLength) {
                        Log.e("当前进度", "  --  " + ((float) currentLength / totalLength) * 100);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("第 " + (index + 1) + " 个文件上传进度" + ((float) currentLength / totalLength) * 100);
                            }
                        });
                    }
                })
                .execute(new Callback() {
                    @Override
                    public void onResponse(Response response) {
                        Log.e("res", response.getBody());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "上传完成", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("onFailure", " onFailure " + e.getMessage());
                    }
                });
    }

    public void httpGet() {
        client.newCall("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1306/27/c4/22626360_1372304637240_800x800.jpg")
                .execute(new Callback() {
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
        callDownload.execute(new Callback() {
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
