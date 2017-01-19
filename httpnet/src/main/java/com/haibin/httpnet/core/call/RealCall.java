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

import com.haibin.httpnet.HttpNetClient;
import com.haibin.httpnet.builder.Request;
import com.haibin.httpnet.core.Response;

import java.io.IOException;

/**
 * 请求的真正代理实现
 */
public class RealCall implements Call {
    private HttpNetClient mClient;
    private Request mRequest;
    private AsyncCall mAsyncCall;
    private InterceptListener mListener;

    public RealCall(HttpNetClient client, Request request) {
        this.mClient = client;
        this.mRequest = request;
    }

    @Override
    public Call intercept(InterceptListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public Response execute() throws IOException {
        if (mAsyncCall == null)
            mAsyncCall = new AsyncCall(mClient, mRequest, null, mListener);
        return mAsyncCall.execute();
    }

    @Override
    public void execute(Callback callBack) {
        if (mAsyncCall == null)
            mAsyncCall = new AsyncCall(mClient, mRequest, callBack, mListener);
        mClient.dispatcher().execute(mAsyncCall);
    }

    @Override
    public void cancel() {
        mAsyncCall.getConnection().disconnect();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
