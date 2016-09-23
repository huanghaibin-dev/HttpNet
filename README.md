#HttpNet项目结构如下
![输入图片说明](http://git.oschina.net/uploads/images/2016/0919/132807_3e935005_494015.png "在这里输入图片标题")

HttpNet网络请求框架基于HttpUrlConnection，采用Client + Request + Call的请求模型，支持https默认证书，数字安全证书！后续将会实现队列、缓存模块。

如果用于Android开发，请使用[Elegant](http://git.oschina.net/huanghaibin_dev/Elegant)体验新的Android开发高潮，它的网络请求模块基于HttpNet，采用动态代理 + 构建的思想，致敬Retrofit！

##gradle

```java
compile 'com.haibin:httpnet:1.0.3'
```

###默认支持Https认证，如果使用数字证书,在执行请求之前使用下面3种API导入证书即可

```java

ConnectionManager.setSslSocketFactory(getAssets().open("12306.cer"));//证书文件输入流
ConnectionManager.setSslSocketFactory("filepath/12306.cer");//证书路径
ConnectionManager.setSslSocketFactoryAsString("cerValue");//证书文本

Request request = new Request.Builder()
                .encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .proxy("192.168.1.1",80) //支持HTTP代理
                .url("https://kyfw.12306.cn/otn/")
                .build();
```


##GET请求构建:
```java

Request request = new Request.Builder().encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();
```

##POST请求构建:
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

client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {
                String body = response.getBody();
                InputStream is = response.toStream();//如果采用下载
            }

            @Override
            public void onFailure(Exception e) {

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
 