#HttpNet项目结构如下
![输入图片说明](http://git.oschina.net/uploads/images/2016/0912/101823_10ccdcb3_494015.png "在这里输入图片标题")

HttpNet网络请求框架基于HttpUrlConnection，采用Client + Request + Call的请求模型，后续将会实现
Https、队列、缓存模块
如果用于Android开发，请使用[Elegant](http://git.oschina.net/huanghaibin_dev/Elegant)体验新的Android开发高潮，它的网络请求模块基于HttpNet，采用动态代理 + 构建的思想，致敬Retrofit！

##gradle

```java
compile 'com.haibin:httpnet:1.0.1'
```

##using
```java
GET：
HttpNetClient client = new HttpNetClient();
        Request request = new Request.Builder().encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();
        
POST：
RequestParams params = new RequestParams()
                .put("userName","oscer")
                .put("pwd","oschina");
        Request request1 = new Request.Builder()
                .encode("UTF-8")
                .method("POST")
                .params(params)
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();

client.newCall(request).execute(new CallBack() {
            @Override
            public void onResponse(Response response) {

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
 
