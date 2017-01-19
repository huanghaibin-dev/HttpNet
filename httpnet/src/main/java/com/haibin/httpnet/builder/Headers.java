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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the http/https header
 */
@SuppressWarnings("unused")
public class Headers {
    private Map<String, List<String>> mHeaders;

    private Headers(Builder builder) {
        this.mHeaders = builder.mHeaders;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, List<String>> mHeaders) {
        this.mHeaders = mHeaders;
    }

    public static final class Builder {
        private Map<String, List<String>> mHeaders;

        public Builder() {
            mHeaders = new HashMap<>();
        }

        public Builder addHeader(String name, String value) {
            checkNotNull(name, value);
            if (mHeaders.containsKey(name)) {
                if (mHeaders.get(name) == null) mHeaders.put(value, new ArrayList<String>());
                mHeaders.get(name).add(value);
            } else {
                List<String> h = new ArrayList<>();
                h.add(value);
                mHeaders.put(name, h);
            }
            return this;
        }

        public Builder setHeader(String name, String value) {
            if (mHeaders.containsKey(name)) {
                mHeaders.remove(name);
            }
            List<String> h = new ArrayList<>();
            h.add(value);
            mHeaders.put(name, h);
            return this;
        }

        private void checkNotNull(String name, String value) {
            if (name == null) throw new NullPointerException("name can not be null");
            if (value == null) throw new NullPointerException("value can not be null");
        }

        public Headers build() {
            return new Headers(this);
        }
    }
}
