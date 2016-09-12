#HttpNet项目结构如下
![输入图片说明](http://git.oschina.net/uploads/images/2016/0912/101823_10ccdcb3_494015.png "在这里输入图片标题")

HttpNet网络请求框架基于HttpUrlConnection，采用Client + Request + Call的请求模型，后续将会实现
Https、队列、缓存模块

##gradle

```java
compile 'com.haibin:httpnet:1.0.0'
```

##using
```java
GET请求：
HttpNetClient client = new HttpNetClient();
        Request request = new Request.Builder().encode("UTF-8")
                .method("GET")
                .timeout(13000)
                .url("http://www.oschina.net")
                .build();
        
POST请求：
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