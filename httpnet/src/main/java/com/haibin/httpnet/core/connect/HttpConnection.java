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

import com.haibin.httpnet.HttpNetClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * http
 */
@Deprecated
public class HttpConnection extends Connection {
    private HttpURLConnection mConnection;

    public HttpConnection(HttpNetClient client) {
        super(client);
    }

    @Override
    protected void convertConnect() {
        mConnection = (HttpURLConnection) mUrlConnection;
        mConnection.setRequestProperty("Cache-Control", "no-cache");
        mConnection.setUseCaches(false);
        mConnection.setAllowUserInteraction(false);
        mConnection.setChunkedStreamingMode(1024);
    }

    @Override
    protected void initMethod(String method) throws ProtocolException {
        mConnection.setRequestMethod(method);
    }

    @Override
    protected int getResponseCode() throws IOException {
        return mConnection.getResponseCode();
    }

    @Override
    public void disConnect() {
        if (mConnection != null)
            mConnection.disconnect();
    }
}
