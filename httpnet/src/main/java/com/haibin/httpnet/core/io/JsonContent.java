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

import java.io.IOException;

/**
 * Json POST请求
 */
public class JsonContent extends HttpContent {
    private String mJson;

    public JsonContent(String json) {
        super(null, "UTF-8");
        this.mJson = json;
    }

    public JsonContent(String json, String encode) {
        super(null, encode);
        this.mJson = json;
    }

    public String getJson() {
        return mJson;
    }

    public void setJson(String mJson) {
        this.mJson = mJson;
    }


    @Override
    public void doOutput() throws IOException {
        mOutputStream.write(mJson.getBytes(mEncode));
        //outputEnd();
    }

    @Override
    public String intoString() {
        return mJson;
    }

    @Override
    public long getContentLength() {
        return mJson.length();
    }
}
