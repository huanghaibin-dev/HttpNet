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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * the request response
 */

public class Response {
    private Map<String, List<String>> mHeaders;
    private int mCode;
    private String mBody;
    private InputStream mInputStream;
    private String mEncode;
    private int mContentLength;

    public Response(int code, InputStream is, Map<String, List<String>> headers, String encode, int contentLength) {
        this.mCode = code;
        this.mInputStream = is;
        this.mHeaders = headers;
        this.mEncode = encode;
        this.mContentLength = contentLength;
    }

    public int getCode() {
        return mCode;
    }

    public String getBody() {
        if (mBody == null) {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                int b;
                while ((b = mInputStream.read()) != -1) {
                    os.write(b);
                }
                mBody = new String(os.toByteArray(), mEncode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mBody;
    }

    public InputStream toStream() {
        return mInputStream;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }

    public int getContentLength() {
        return mContentLength;
    }
}
