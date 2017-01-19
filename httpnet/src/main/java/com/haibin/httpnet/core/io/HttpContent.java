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
import com.haibin.httpnet.core.call.InterceptListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * ***** FormContent
 * 定义HttpContent 传输数据，使用策略模式 ********* JsonContent
 * ***** MultiPartContent
 */
@SuppressWarnings("unused")
public abstract class HttpContent {
    public static final String BOUNDARY = "http-net";
    static final String DATA_TAG = "--";
    static final String END = "\r\n";
    String mEncode;
    RequestParams mParams;
    DataOutputStream mOutputStream;

    HttpContent(RequestParams mParams, String encode) {
        this.mEncode = encode == null ? "UTF-8" : encode;
        this.mParams = mParams;
    }

    public void setOutputStream(DataOutputStream outputStream) throws IOException {
        this.mOutputStream = outputStream;
    }

    /**
     * URL编码表单
     */
    String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void doOutput() throws IOException;

    public abstract void doOutput(InterceptListener listener) throws IOException;

    public abstract String intoString();

    void outputEnd() throws IOException {
        mOutputStream.writeBytes(END + DATA_TAG + BOUNDARY + DATA_TAG + END);
        mOutputStream.flush();
        mOutputStream.close();
    }
}
