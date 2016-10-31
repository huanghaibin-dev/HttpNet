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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
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

    private void intoString(StringBuffer buffer) {
        Set<String> set = mParams.getTextParams().keySet();
        Map<String, String> texts = mParams.getTextParams();
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = texts.get(key);
            buffer.append(key + "=" + value + "&");
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

    @Override
    public long getContentLength() {
        return intoString().length();
    }
}
