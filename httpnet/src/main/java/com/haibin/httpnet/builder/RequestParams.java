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

import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 *
 */
@SuppressWarnings("unused")
public final class RequestParams {
    private IdentityHashMap<Key, String> textParams;
    private IdentityHashMap<Key, File> multiParams;

    public RequestParams() {
        textParams = new IdentityHashMap<>();
    }

    public RequestParams put(String name, String value) {
        textParams.put(new Key(name), value);
        return this;
    }

    public RequestParams put(String name, int value) {
        return put(name, String.valueOf(value));
    }

    public RequestParams put(String name, long value) {
        return put(name, String.valueOf(value));
    }

    public RequestParams put(String name, double value) {
        return put(name, String.valueOf(value));
    }

    public RequestParams put(String name, float value) {
        return put(name, String.valueOf(value));
    }

    public RequestParams put(String name, byte value) {
        return put(name, String.valueOf(value));
    }

    public RequestParams put(String name, boolean value) {
        return put(name, String.valueOf(value));
    }

    public RequestParams putFile(String name, File file) {
        if (multiParams == null) multiParams = new IdentityHashMap<>();
        if (!file.exists())
            return this;//throw new IllegalArgumentException("request param file not find exception");
        multiParams.put(new Key(name), file);
        return this;
    }

    public RequestParams putFile(String name, String fileName) {
        return putFile(name, new File(fileName));
    }

    public IdentityHashMap<Key, String> getTextParams() {
        return textParams;
    }

    public IdentityHashMap<Key, File> getMultiParams() {
        return multiParams;
    }

    public class Key {
        private String name;

        public Key(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
