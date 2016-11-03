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


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haibin.httpnet.core.io.IO;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 报文数据中的Content-Type格式，支持所有文件
 */
public class ContentTypeFactory {
    private Map<String, String> mTypeMap;
    private static ContentTypeFactory mInstance = new ContentTypeFactory();

    private ContentTypeFactory() {
        InputStream is = getClass().getResourceAsStream("/assets/content_type.json");
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            byte data[] = bos.toByteArray();
            String json = new String(data);
            mTypeMap = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IO.close(bos, is);
        }
    }

    public String getContentType(String fileName) {
        String end = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")).toLowerCase() : ".*";
        return mTypeMap.containsKey(end) ? mTypeMap.get(end) : mTypeMap.get(".*");
    }

    public static ContentTypeFactory getInstance() {
        return mInstance;
    }
}
