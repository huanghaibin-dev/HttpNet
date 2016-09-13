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
package com.haibin.httpnet.builder;

import com.haibin.httpnet.core.io.FormContent;
import com.haibin.httpnet.core.io.HttpContent;
import com.haibin.httpnet.core.io.MultiPartContent;

/**
 * to build a request
 * 构建一个请求
 */
public final class Request {
    private String mUrl;
    private String mMethod;
    private RequestParams mParams;
    private Headers mHeaders;
    private String mEncode;
    private int mTimeout;
    private HttpContent mHttpContent;

    public Request(Builder builder) {
        this.mUrl = builder.mUrl;
        this.mHeaders = builder.mHeaders.build();
        this.mMethod = builder.mMethod;
        this.mParams = builder.mParams;
        this.mHttpContent = builder.mHttpContent;
        this.mEncode = builder.mEncode;
        this.mTimeout = builder.mTimeout;
    }

    public String url() {
        return mUrl;
    }

    public String method() {
        return mMethod;
    }

    public RequestParams params() {
        return mParams;
    }

    public Headers headers() {
        return mHeaders;
    }

    public HttpContent content() {
        return this.mHttpContent;
    }

    public String encode() {
        return mEncode;
    }

    public int timeout() {
        return mTimeout;
    }

    public static final class Builder {
        private String mUrl;
        private String mMethod;
        private String mEncode;
        private int mTimeout;
        private RequestParams mParams;
        private Headers.Builder mHeaders;
        private HttpContent mHttpContent;

        public Builder() {
            this.mMethod = "GET";
            this.mEncode = "UTF-8";
            this.mTimeout = 13000;
            this.mHeaders = new Headers.Builder();
        }

        public Builder url(String url) {
            if (url == null) throw new NullPointerException("url can not be null");
            this.mUrl = url;
            return this;
        }

        public Builder encode(String encode) {
            if (encode == null) throw new NullPointerException("encode can not be null");
            this.mEncode = encode;
            return this;
        }

        public Builder timeout(int timeout) {
            this.mTimeout = timeout;
            if (mTimeout <= 0) mTimeout = 13000;
            return this;
        }

        public Builder method(String method) {
            if (method == null) throw new NullPointerException("method can not be null");
            this.mMethod = method;
            return this;
        }

        public Builder params(RequestParams params) {
            if (params == null) throw new NullPointerException("params can not be null");
            this.mParams = params;
            return this;
        }

        public Builder headers(Headers.Builder headers) {
            this.mHeaders = headers;
            return this;
        }

        public Builder content(HttpContent content) {
            if (content == null) throw new NullPointerException("content can not be null");
            this.mHttpContent = content;
            return this;
        }

        public Request build() {
            if (mHttpContent == null && mParams != null) {
                if (mParams.getMultiParams() != null) {
                    mHttpContent = new MultiPartContent(mParams, mEncode);
                } else {
                    mHttpContent = new FormContent(mParams, mEncode);
                }
            }else {
                mParams = null;
            }
            return new Request(this);
        }
    }
}
