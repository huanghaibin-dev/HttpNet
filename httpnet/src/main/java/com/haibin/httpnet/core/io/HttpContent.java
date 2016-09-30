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
package com.haibin.httpnet.core.io;

import com.haibin.httpnet.builder.RequestParams;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 *                                            ***** FormContent
 * 定义HttpContent 传输数据，使用策略模式 ********* JsonContent
 *                                            ***** MultiPartContent
 *
 */
public abstract class HttpContent {
    public static final String BOUNDARY = "http-net";
    public static final String DATA_TAG = "--";
    public static final String END = "\r\n";
    protected String mEncode;
    protected RequestParams mParams;
    protected DataOutputStream mOutputStream;

    public HttpContent(RequestParams mParams, String encode) {
        this.mEncode = encode == null ? "UTF-8" : encode;
        this.mParams = mParams;
    }

    public void setOutputStream(DataOutputStream outputStream) throws IOException {
        this.mOutputStream = outputStream;
        doOutput();
    }

    public HttpContent(RequestParams params) {
        this.mParams = params;
    }

    public RequestParams getParams() {
        return mParams;
    }

    public void setParams(RequestParams mParams) {
        this.mParams = mParams;
    }

    protected String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, mEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void doOutput() throws IOException;

    public abstract String intoString();

    public void outputEnd() throws IOException {
        mOutputStream.writeBytes(END + DATA_TAG + BOUNDARY + DATA_TAG + END);
        mOutputStream.flush();
        mOutputStream.close();
    }

    public long getContentLength() {
        return 0;
    }
}
