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
import com.haibin.httpnet.core.ContentTypeFactory;
import com.haibin.httpnet.core.call.InterceptListener;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * HTTP MultiPart RequestBody，请全体包括文本、文件流等
 */
@SuppressWarnings("all")
public class MultiPartContent extends HttpContent {
    private InterceptListener mListener = null;

    public MultiPartContent(RequestParams params, String encode) {
        super(params, encode);
    }

    public MultiPartContent(RequestParams params, String encode, InterceptListener listener) {
        super(params, encode);
        this.mListener = listener;
    }

    public MultiPartContent(RequestParams params) {
        super(params, "UTF-8");
    }

    @Override
    public void doOutput() throws IOException {
        if (mParams.getTextParams() != null && mParams.getTextParams().size() > 0)
            outputText();
        if (mParams.getMultiParams() != null && mParams.getMultiParams().size() > 0)
            outputFileFormData();
        outputEnd();
    }

    @Override
    public void doOutput(InterceptListener listener) throws IOException {
        this.mListener = listener;
        doOutput();
    }

    private void outputText() throws IOException {
        StringBuilder buffer = new StringBuilder();
        Set<RequestParams.Key> set = mParams.getTextParams().keySet();
        IdentityHashMap<RequestParams.Key, String> texts = mParams.getTextParams();
        for (RequestParams.Key keys : set) {
            String key = urlEncode(keys.getName());
            String value = urlEncode(texts.get(key));
            buffer.append(END + DATA_TAG + BOUNDARY + END);
            buffer.append("Content-Disposition: form-data; name=\"" + key + "\"");
            buffer.append(END + END);
            buffer.append(value);
        }
        mOutputStream.write(buffer.toString().getBytes(mEncode));
    }

    private void outputFileFormData() throws IOException {
        Set<RequestParams.Key> set = mParams.getMultiParams().keySet();
        IdentityHashMap<RequestParams.Key, File> fileMap = mParams.getMultiParams();
        int index = 0;
        for (RequestParams.Key keys : set) {
            StringBuffer buffer = new StringBuffer();
            String key = urlEncode(keys.getName());
            File file = fileMap.get(keys);
            String fileName = file.getName();
            buffer.append(END + DATA_TAG + BOUNDARY + END);
            buffer.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"");
            buffer.append(END);
            buffer.append("Content-Type: " + ContentTypeFactory.getInstance().getContentType(fileName));
            buffer.append(END + END);
            mOutputStream.writeBytes(buffer.toString());
            outputFile(file, index);
            ++index;
        }
    }

    private void outputFile(File file, int index) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        long p = 0;
        long length = file.length();
        int bytes;
        onProgress(index, mListener, p, length);
        byte[] bufferOut = new byte[1024 * 10];
        while ((bytes = in.read(bufferOut)) != -1) {
            mOutputStream.write(bufferOut, 0, bytes);
            p += bytes;
            onProgress(index, mListener, p, length);
        }
        in.close();
    }

    private void onProgress(int index, InterceptListener listener, long currentLength, long allLength) {
        if (listener != null)
            listener.onProgress(index, currentLength, allLength);
    }

    private void intoString(StringBuffer buffer) {
        Set<RequestParams.Key> set = mParams.getTextParams().keySet();
        IdentityHashMap<RequestParams.Key, String> texts = mParams.getTextParams();
        for (RequestParams.Key keys : set) {
            String value = texts.get(keys.getName());
            buffer.append(keys.getName() + "=" + value + "&");
        }
    }

    @Override
    public String intoString() {
        StringBuffer buffer = new StringBuffer();
        intoString(buffer);
        return buffer.substring(0, buffer.length() - 1);
    }

    @Override
    public long getContentLength() {
        long length = 0;
        IdentityHashMap<RequestParams.Key, String> text = mParams.getTextParams();
        IdentityHashMap<RequestParams.Key, File> multi = mParams.getMultiParams();
        if (text != null) {
            Set<RequestParams.Key> set = text.keySet();
            for (RequestParams.Key keys : set) {
                length += (END + DATA_TAG + BOUNDARY + END + "Content-Disposition: form-data; name=\"" + keys.getName() + "\"" + END + END).length();
            }
        }
        if (multi != null) {
            Set<RequestParams.Key> set = multi.keySet();
            for (Iterator<RequestParams.Key> iterator = set.iterator(); iterator.hasNext(); ) {
                String key = iterator.next().getName();
                File file = multi.get(key);
                String fileName = file.getName();
                length += ("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""
                        + END
                        + "Content-Type: " + ContentTypeFactory.getInstance().getContentType(fileName)
                        + END + END).length() + file.length();
            }
        }
        return length;
    }
}
