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
package com.haibin.httpnet.core.call;

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Response;
import com.haibin.httpnet.core.connection.Connection;
import com.haibin.httpnet.core.connection.HttpConnection;
import com.haibin.httpnet.core.connection.HttpsConnection;

import java.io.IOException;

/**
 * 请求的封装执行
 */

public class AsyncCall implements Runnable {
    private Callback mCallBack;
    private Request mRequest;
    private Connection mConnection;

    AsyncCall(HttpNetClient client, Request request, Callback callBack, InterceptListener listener) {
        this.mCallBack = callBack;
        this.mRequest = request;
        mConnection = request.url().startsWith("https") ?
                new HttpsConnection(client, request, listener) :
                new HttpConnection(client, request, listener);
    }

    @Override
    public void run() {
        mConnection.connect(mCallBack);
    }

    public Response execute() throws IOException {
        return mConnection.connect();
    }

    public Request getRequest() {
        return mRequest;
    }

    Connection getConnection() {
        return mConnection;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof AsyncCall) {
            return mRequest.url().equalsIgnoreCase(((AsyncCall) o).getRequest().url());
        }
        return super.equals(o);
    }
}