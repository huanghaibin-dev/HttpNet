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
package com.haibin.httpnet;

import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Dispatcher;
import com.haibin.httpnet.core.call.Call;
import com.haibin.httpnet.core.call.RealCall;
import com.haibin.httpnet.core.connect.SSLManager;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Http客户端
 */
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

    public Dispatcher dispatcher() {
        return mDispatcher;
    }

    public Proxy getProxy() {
        return mProxy;
    }

    public void setProxy(Proxy proxy) {
        this.mProxy = proxy;
    }

    public void setProxy(String host, int port) {
        this.mProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }

    public HttpNetClient setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        mSslManager.setSslSocketFactory(sslSocketFactory);
        return this;
    }

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

    public SSLSocketFactory getSslSocketFactory() {
        return mSslManager.getSslSocketFactory();
    }
}
