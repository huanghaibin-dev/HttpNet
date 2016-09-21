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
package com.haibin.httpnet.core.call;

import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.connect.Connection;
import com.haibin.httpnet.core.connect.HttpConnection;
import com.haibin.httpnet.core.connect.HttpsConnection;

/**
 *
 */

public class AsyncCall implements Runnable {
    private CallBack mCallBack;
    private Request mRequest;
    private Connection mConnection;

    public AsyncCall(Request request, CallBack callBack) {
        this.mCallBack = callBack;
        this.mRequest = request;
        mConnection = request.url().startsWith("https") ? new HttpsConnection() : new HttpConnection();
    }

    @Override
    public void run() {
        mConnection.connect(mRequest, mCallBack);
    }

    public CallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request mRequest) {
        this.mRequest = mRequest;
    }

    public Connection getConnection() {
        return mConnection;
    }

    public void setConnection(Connection mConnection) {
        this.mConnection = mConnection;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof AsyncCall) {
            return mRequest.url().equalsIgnoreCase(((AsyncCall) o).getRequest().url());
        }
        return super.equals(o);
    }
}