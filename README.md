#HttpNet项目结构如下
![HttpNet项目结构](http://git.oschina.net/uploads/images/2016/0919/132807_3e935005_494015.png "HttpNet项目结构")

HttpNet网络请求框架基于HttpUrlConnection，采用Client + Request + Call的请求模型，支持HTTPS，支持同步和异步方法，支持Rxjava，支持上传和下载监听，同步请求配合RxJava十分方便实现线程切换

如果用于Android开发，请使用[Elegant](http://git.oschina.net/huanghaibin_dev/Elegant)体验新的Android开发高潮，它的网络请求模块基于HttpNet，采用动态代理 + 构建的思想，致敬Retrofit！

##gradle

```java
compile 'com.haibin:httpnet:1.1.5'
```

##RxJava断点下载
```java
final File rangeFile = new File(Environment.getExternalStorageDirectory().getPath() + "/cnblogs.apk");
final long readySize = rangeFile.exists() ? rangeFile.length() : 0;
Headers.Builder headers = new Headers.Builder()
                .addHeader("Range", "bytes=" + readySize + "-");
Request request = new Request.Builder()
          .url("http://f1.market.xiaomi.com/download/
                AppStore/0117653278abecee8762883a940e129e9d242ae7d/com.huanghaibin_dev.cnblogs.apk")
           .headers(headers)
           .build();
callDownload = client.newCall(request);//调用callDownload.cancel();取消请求实现暂停，再次请求即可从断点位置继续下载
Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    Response response = callDownload.execute();
                    InputStream is = response.toStream();
                    RandomAccessFile randomAccessFile = new RandomAccessFile(rangeFile, "rw");
                    randomAccessFile.seek(readySize);
                    int length = response.getContentLength();
                    length += readySize;
                    int p = (int) readySize;
                    int bytes;
                    byte[] buffer = new byte[1024];
                    while ((bytes = is.read(buffer)) != -1) {
                        randomAccessFile.write(buffer, 0, bytes);
                        p += bytes;
                        e.onNext(String.valueOf((p / (float) length) * 100));
                    }
                    response.close();
                } catch (Exception error) {
                    error.printStackTrace();
                    e.onError(error);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        text.setText(value);
                        float f = Float.parseFloat(value);
                        progressBar.setProgress((int) f);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
```

##RxJava同步上传监听：
```java
final Request request = new Request.Builder()
                .url("http://upload.cnblogs.com/ImageUploader/TemporaryAvatarUpload")
                .method("POST")
                .params(new RequestParams()
                        .putFile("qqfile", "/storage/emulated/0/DCIM/Camera/339718150.jpeg"))
                .headers(new Headers.Builder().addHeader("Cookie", "CNZZDATA1259029673=2072545293-1479795067-null%7C1479795067; lhb_smart_1=1; __utma=226521935.1789795872.1480996255.1480996255.1480996255.1; __utmz=226521935.1480996255.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; .CNBlogsCookie=A6783E37E1040979421EC4A57A2FEFBB74B65BB51C7345AC99B64A7065293F59A79C6830C60D71629E8D28A332436E23CD40968EB58AA830CBD0F0733438F9A7627C074DB0462C2576D206D3752E640871E8CB23D1A50B0A9962C158466EE81425B1E516; _gat=1; _ga=GA1.2.1789795872.1480996255"))
                .build();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                client.newCall(request)
                        .intercept(new InterceptListener() {
                            @Override
                            public void onProgress(int index, long currentLength, long totalLength) {
                                Log.e("file", index + " -- " + " -- " + currentLength + " -- " + totalLength);
                                e.onNext(index + " -- " + " -- " + currentLength + " -- " + totalLength);
                            }
                        })
                        .execute();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("is", response);
                        text.setText(response);
                    }

                });
```

##RxJava同步下载监听：
```java

Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                callDownload = client.newCall("http://f3.market.xiaomi.com/download/AppStore/0b3f6b4e06ff14b61065972a96149da822c86ad40/com.eg.android.AlipayGphone.apk");
                Response response = callDownload.execute();
                InputStream is = response.toStream();
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/alipay.apk");
                FileOutputStream os = new FileOutputStream(file);
                int length = response.getContentLength();
                int p = 0;
                int bytes;
                byte[] buffer = new byte[1024];
                while ((bytes = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytes);
                    p += bytes;
                    e.onNext(String.valueOf((p / (float) length) * 100));
                }
                response.close();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        text.setText(s);
                        float f = Float.parseFloat(s);
                        progressBar.setProgress((int) f);
                    }
                });

```

###默认支持Https CA认证证书，如果使用自签名证书,在执行请求之前使用下面3种API导入证书即可

```java

client.setSslSocketFactory(getAssets().open("12306.cer"));//证书文件输入流
client.setSslSocketFactory("filepath/12306.cer");//证书路径
client.setSslSocketFactoryAsString("cerValue");//证书文本

/*注意，添加多个证书只能调用该方法一次，可以使用如下方式添加多个证书,该客户端导入证书之后将不能访问其它没有导入https的链接，
重新创建一个HttpNetClient反问新的https即可*/

InputStream is12306 = getAssets().open("12306.cer");
InputStream isGoogle = getAssets().open("google.cer");
client.setSslSocketFactory(is12306 , isGoogle );

Request request = new Request.Builder()
                .encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .proxy("192.168.1.1",80) //支持HTTP代理
                .url("https://kyfw.12306.cn/otn/")
                .build();
```


##GET异步请求构建:
```java

Request request = new Request.Builder().encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();
```

##POST异步请求构建:
```java

RequestParams params = new RequestParams()
                .put("userName","oscer")
                .putFile("fileName","file")
                .put("pwd","oschina");
        Request request = new Request.Builder()
                .encode("UTF-8")
                .method("POST")
                .params(params)
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();
```

##POST JSON请求构建:
```java

Request request = new Request.Builder()
                .encode("UTF-8")
                .method("POST")
                .content(new JsonContent("json")
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();
       
```

##执行请求:
```java

HttpNetClient client = new HttpNetClient();

client.setProxy("192.168.1.1",80);//您也可以开启该客户端全局代理

client.newCall(request)
                //如果采用上传文件方式，可以在这里开启上传进度监控
                .intercept(new InterceptListener() {
                    @Override
                    public void onProgress(final int index, final long currentLength, final long totalLength) {
                        Log.e("当前进度", "  --  " + ((float) currentLength / totalLength) * 100);
                    }
                })
                .execute(new CallBack() {
                    @Override
                    public void onResponse(Response response) {
                        String body = response.getBody();
                        InputStream is = response.toStream();//如果采用下载，可以在这里监听下载进度
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("onFailure", " onFailure " + e.getMessage());
                    }
                });

```

##Licenses
- Copyright (C) 2013 huanghaibin_dev <huanghaibin_dev@163.com>
 
- Licensed under the Apache License, Version 2.0 (the "License");
- you may not use this file except in compliance with the License.
- You may obtain a copy of the License at
 
-         http://www.apache.org/licenses/LICENSE-2.0
 
- Unless required by applicable law or agreed to in writing, software
- distributed under the License is distributed on an "AS IS" BASIS,
- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
- See the License for the specific language governing permissions and
  limitations under the License.
 
