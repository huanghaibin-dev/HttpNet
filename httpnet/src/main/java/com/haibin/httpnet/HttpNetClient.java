/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/huanghaibin_dev
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
package com.haibin.httpnet;

import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Dispatcher;
import com.haibin.httpnet.core.call.Call;
import com.haibin.httpnet.core.call.RealCall;
import com.haibin.httpnet.core.connection.SSLManager;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.net.ssl.SSLSocketFactory;

/**
 * Http客户端
 */
@SuppressWarnings("unused")
public final class HttpNetClient {
    private Dispatcher mDispatcher;

    private Proxy mProxy;//为当前客户端开启全局代理

    private SSLManager mSslManager;

    public HttpNetClient() {
        mDispatcher = new Dispatcher();
        mSslManager = new SSLManager();
    }

    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    public Call newCall(String url) {
        return new RealCall(this, getDefaultRequest(url));
    }

    public Dispatcher dispatcher() {
        return mDispatcher;
    }

    public Proxy getProxy() {
        return mProxy;
    }

    /**
     * 开启全局代理
     */
    public void setProxy(Proxy proxy) {
        this.mProxy = proxy;
    }

    /**
     * 开启全局代理
     */
    public void setProxy(String host, int port) {
        this.mProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }

    /**
     * 导入证书
     */
    public HttpNetClient setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        mSslManager.setSslSocketFactory(sslSocketFactory);
        return this;
    }

    /**
     * 导入证书
     */
    public HttpNetClient setSslSocketFactory(InputStream... cerInputStream) {
        mSslManager.setSslSocketFactory(cerInputStream);
        return this;
    }

    public HttpNetClient setSslSocketFactory(String... cerPaths) {
        mSslManager.setSslSocketFactory(cerPaths);
        return this;
    }

    public HttpNetClient setSslSocketFactoryAsString(String... cerValues) {
        mSslManager.setSslSocketFactoryAsString(cerValues);
        return this;
    }

    public void cancelAll() {
        mDispatcher.shutdown();
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSslManager.getSslSocketFactory();
    }

    private Request getDefaultRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }
}
