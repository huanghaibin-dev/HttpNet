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
package com.haibin.httpnet.core;

import com.haibin.httpnet.builder.Headers;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.call.CallBack;
import com.haibin.httpnet.core.io.HttpContent;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * http output or input stream operator
 */
@SuppressWarnings("All")
public class Connection {
    private Request mRequest;
    private DataOutputStream mOutputStream;
    private DataInputStream mInputStream;
    private HttpURLConnection mConnection;

    public void doRequest(Request request, CallBack callBack) {
        this.mRequest = request;
        boolean isPOST = "POST".equals(mRequest.method().toUpperCase());
        if (isPOST)
            post(callBack);
        else
            get(callBack);
    }

    private void post(CallBack callBack) {
        try {
            URL url = new URL(mRequest.url());
            mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setRequestMethod(mRequest.method());
            initConnection();
            mConnection.setDoOutput(true);
            String content_type;
            RequestParams params = mRequest.params();
            content_type = params != null ?
                    (params.getMultiParams() != null ? "multipart/form-data; boundary=\"" + HttpContent.BOUNDARY + "\"" : "application/x-www-form-urlencoded")
                    : "application/json; charset=utf-8";
            mConnection.setRequestProperty("Content-Type", content_type);

            mOutputStream = new DataOutputStream(mConnection.getOutputStream());
            mRequest.content().setOutputStream(mOutputStream);
            Response response = new Response();
            int code = mConnection.getResponseCode();
            response.setCode(code);
            response.setHeaders(mConnection.getHeaderFields());
            mInputStream = new DataInputStream(mConnection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b;
            while ((b = mInputStream.read()) != -1) {
                out.write(b);
            }
            mConnection.disconnect();
            String result = new String(out.toByteArray(), mRequest.encode());
            response.setBody(result);
            callBack.onResponse(response);
            out.close();
        } catch (MalformedURLException e) {
            callBack.onFailure(e);
        } catch (IOException e) {
            callBack.onFailure(e);
        } finally {
            finishClose(mOutputStream, mInputStream);
            mConnection = null;
            mInputStream = null;
            mOutputStream = null;
        }
    }

    private void initConnection() {
        mConnection.setUseCaches(true);
        mConnection.setChunkedStreamingMode(1024 * 10);
        mConnection.setConnectTimeout(mRequest.timeout());
        mConnection.setRequestProperty("Accept-Language", "zh-CN");
        mConnection.setRequestProperty("Charset", mRequest.encode());
        mConnection.setRequestProperty("Connection", "Keep-Alive");
        initHeaders();
    }

    private void get(CallBack callBack) {
        try {
            if (mConnection == null) {
                String httpUrl = mRequest.url();
                if (mRequest.params() != null && mRequest.params().getTextParams() != null) {
                    String paramsStr = mRequest.content().intoString();
                    httpUrl = httpUrl + (httpUrl.endsWith("?") ? paramsStr : "?" + paramsStr);
                }
                URL url = new URL(httpUrl);
                mConnection = (HttpURLConnection) url.openConnection();
            } else {
                mConnection.connect();
            }
            mConnection.setRequestMethod("GET");
            initConnection();
            Response response = new Response();
            int code = mConnection.getResponseCode();
            response.setCode(code);
            response.setHeaders(mConnection.getHeaderFields());
            InputStream in = mConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            mConnection.disconnect();
            in.close();
            String result = new String(out.toByteArray(), mRequest.encode());
            response.setBody(result);
            callBack.onResponse(response);
            out.close();
        } catch (MalformedURLException e) {
            callBack.onFailure(e);
        } catch (IOException e) {
            callBack.onFailure(e);
        } finally {
            finishClose(mOutputStream, mInputStream);
        }
    }

    private void finishClose(Closeable... closeables) {
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

    private void initHeaders() {
        Headers headers = mRequest.headers();
        if (headers != null) {
            Map<String, List<String>> maps = headers.getHeaders();
            if (maps != null) {
                Set<String> sets = maps.keySet();
                for (Iterator<String> iterator = sets.iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    for (String value : maps.get(key)) {
                        mConnection.addRequestProperty(key, value);
                    }
                }
            }
        }
    }

    public void disConnect() {
        if (mConnection != null)
            mConnection.disconnect();
    }
}
