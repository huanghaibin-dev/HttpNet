/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haibin.httpnet.core.connection;


import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;
import com.haibin.httpnet.core.call.InterceptListener;
import com.haibin.httpnet.core.io.HttpContent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * https连接
 */
@SuppressWarnings("unused")
public class HttpsConnection extends Connection {
    private HttpsURLConnection mHttpsUrlConnection;
    private InterceptListener mListener;

    public HttpsConnection(HttpNetClient client) {
        super(client);
    }

    public HttpsConnection(HttpNetClient client, InterceptListener listener) {
        super(client);
        this.mListener = listener;
    }

    @Override
    void connect(URLConnection connection, String method) throws IOException {
        mHttpsUrlConnection = (HttpsURLConnection) connection;
        mHttpsUrlConnection.setSSLSocketFactory(mClient.getSslSocketFactory());
        mHttpsUrlConnection.setRequestMethod(method);
        mHttpsUrlConnection.setUseCaches(true);
        mHttpsUrlConnection.setConnectTimeout(mRequest.timeout());
        mHttpsUrlConnection.setRequestProperty("Accept-Language", "zh-CN");
        mHttpsUrlConnection.setRequestProperty("Charset", mRequest.encode());
        mHttpsUrlConnection.setRequestProperty("Connection", "Keep-Alive");
    }

    @Override
    void post() throws IOException {
        mHttpsUrlConnection.setDoOutput(true);
        mHttpsUrlConnection.setRequestProperty("Content-Type", getContentType(mRequest.params()));
        mOutputStream = new DataOutputStream(mHttpsUrlConnection.getOutputStream());
        HttpContent body = mRequest.content();
        if (body != null) {
            body.setOutputStream(mOutputStream);
            body.doOutput(mListener);
        }
    }

    @Override
    void get() throws IOException {

    }

    @Override
    void put() throws IOException {
        post();
    }

    @Override
    void delete() throws IOException {

    }

    @Override
    void patch() throws IOException {

    }

    @Override
    void onResponse(CallBack callBack) throws IOException {
        callBack.onResponse(
                new Response(mHttpsUrlConnection.getResponseCode(),
                        mInputStream,
                        mHttpsUrlConnection.getHeaderFields(),
                        mRequest.encode(), mHttpsUrlConnection.getContentLength()));
    }

    @Override
    public void disconnect() {
        if (mHttpsUrlConnection != null)
            mHttpsUrlConnection.disconnect();
    }

    @Override
    void finish() {

    }
}
