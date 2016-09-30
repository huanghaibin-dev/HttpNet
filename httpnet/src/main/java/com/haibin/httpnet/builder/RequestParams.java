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
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class RequestParams {
    private Map<String, String> textParams;
    private Map<String, File> multiParams;

    public RequestParams() {
        textParams = new HashMap<>();
    }

    public RequestParams put(String name, String value) {
        textParams.put(name, value);
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
        if (multiParams == null) multiParams = new HashMap<>();
        if (!file.exists())
            return this;//throw new IllegalArgumentException("request param file not find exception");
        multiParams.put(name, file);
        return this;
    }

    public RequestParams putFile(String name, String fileName) {
        return putFile(name, new File(fileName));
    }

    public Map<String, String> getTextParams() {
        return textParams;
    }

    public Map<String, File> getMultiParams() {
        return multiParams;
    }
}
