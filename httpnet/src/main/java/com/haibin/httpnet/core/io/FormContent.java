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
package com.haibin.httpnet.core.io;

import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.call.InterceptListener;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Form表单的形式
 */
public class FormContent extends HttpContent {
    public FormContent(RequestParams params, String encode) {
        super(params, encode);
    }

    @Override
    public void doOutput() throws IOException {
        if (mParams != null && mParams.getTextParams() != null && mParams.getTextParams().size() > 0) {
            StringBuffer buffer = new StringBuffer();
            intoString(buffer);
            mOutputStream.write(buffer.substring(0, buffer.length() - 1).getBytes(mEncode));
        }
    }

    @Override
    public void doOutput(InterceptListener listener) throws IOException {
        doOutput();
    }

    private void intoString(StringBuffer buffer) {
        Set<RequestParams.Key> set = mParams.getTextParams().keySet();
        IdentityHashMap<RequestParams.Key, String> texts = mParams.getTextParams();
        for (RequestParams.Key keys : set) {
            String key = urlEncode(keys.getName());
            String value = urlEncode(texts.get(keys));
            buffer.append(key).append("=").append(value).append("&");
        }
    }

    @Override
    public String intoString() {
        if (mParams.getTextParams() == null || mParams.getTextParams().size() == 0)
            return "";
        StringBuffer buffer = new StringBuffer();
        intoString(buffer);
        return buffer.substring(0, buffer.length() - 1);
    }
}
