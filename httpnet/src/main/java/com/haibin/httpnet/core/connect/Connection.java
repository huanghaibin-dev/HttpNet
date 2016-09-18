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
package com.haibin.httpnet.core.connect;

import com.haibin.httpnet.builder.Headers;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.call.CallBack;
import com.haibin.httpnet.core.io.HttpContent;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 链接对象
 */
public abstract class Connection {
    protected Request mRequest;
    protected DataOutputStream mOutputStream;
    protected DataInputStream mInputStream;
    private ByteArrayOutputStream mBos;
    protected URLConnection mUrlConnection;
    protected Proxy mProxy;

    public void connect(Request request, CallBack callBack) {
        this.mRequest = request;
        try {
            String httpUrl = mRequest.url();
            String method = mRequest.method();
            if ("GET".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
                if (mRequest.params() != null && mRequest.params().getTextParams() != null) {
                    String paramsStr = mRequest.content().intoString();
                    httpUrl = httpUrl + (httpUrl.endsWith("?") ? paramsStr : "?" + paramsStr);
                }
            }
            URL url = new URL(httpUrl);
            if (mProxy == null)
                mUrlConnection = url.openConnection();
            else
                mUrlConnection = url.openConnection(mProxy);
            initConnection();
            initHeaders();

            initMethod(method);
            boolean isPOST = "POST".equalsIgnoreCase(method);
            if (isPOST)
                post(callBack);
            else if ("GET".equals(method))
                get(callBack);
            else if ("PUT".equals(method))
                put(callBack);
            else if ("DELETE".equals(method))
                delete(callBack);
            onResponse(callBack);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finish();
        }
    }

    public void withProxy(Proxy proxy) {
        this.mProxy = proxy;
    }

    protected abstract int getResponseCode() throws IOException;

    protected abstract void initMethod(String method) throws ProtocolException;

    protected abstract void convertConnect();

    protected void get(CallBack callBack) throws IOException {

    }

    protected void post(CallBack callBack) throws IOException {
        mUrlConnection.setDoOutput(true);
        mUrlConnection.setRequestProperty("Content-Type", getContentType());
        mOutputStream = new DataOutputStream(mUrlConnection.getOutputStream());
        mRequest.content().setOutputStream(mOutputStream);
    }

    protected void put(CallBack callBack) throws IOException {

    }

    protected void delete(CallBack callBack) throws IOException {

    }


    protected void onResponse(CallBack callBack) throws IOException {
        Response response = new Response();
        response.setCode(getResponseCode());
        response.setHeaders(mUrlConnection.getHeaderFields());
        mInputStream = new DataInputStream(mUrlConnection.getInputStream());
        mBos = new ByteArrayOutputStream();
        int b;
        while ((b = mInputStream.read()) != -1) {
            mBos.write(b);
        }
        response.setBody(new String(mBos.toByteArray(), mRequest.encode()));
        callBack.onResponse(response);
    }


    /**
     * 初始化基础链接
     */
    protected void initConnection() {
        convertConnect();
        mUrlConnection.setUseCaches(true);
        mUrlConnection.setConnectTimeout(mRequest.timeout());
        mUrlConnection.setRequestProperty("Accept-Language", "zh-CN");
        mUrlConnection.setRequestProperty("Charset", mRequest.encode());
        mUrlConnection.setRequestProperty("Connection", "Keep-Alive");
    }

    /**
     * 初始化头部
     */
    protected void initHeaders() {
        Headers headers = mRequest.headers();
        if (headers != null) {
            Map<String, List<String>> maps = headers.getHeaders();
            if (maps != null) {
                Set<String> sets = maps.keySet();
                for (Iterator<String> iterator = sets.iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    for (String value : maps.get(key)) {
                        mUrlConnection.addRequestProperty(key, value);
                    }
                }
            }
        }
    }

    /**
     * 关闭链接和流
     *
     * @param closeables closeables
     */
    protected void finishClose(Closeable... closeables) {
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取Content-Type
     *
     * @return "multipart/form-data || application/json; charset=utf-8 || application/x-www-form-urlencoded
     */
    protected String getContentType() {
        RequestParams params = mRequest.params();
        return params != null ?
                (params.getMultiParams() != null ? "multipart/form-data; boundary=\"" + HttpContent.BOUNDARY + "\"" : "application/x-www-form-urlencoded")
                : "application/json; charset=utf-8";
    }

    /**
     *
     */
    protected void finish() {
        finishClose(mOutputStream, mInputStream, mBos);
    }
}
