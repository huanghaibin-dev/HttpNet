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

import com.haibin.httpnet.core.ProgressListener;
import com.haibin.httpnet.builder.RequestParams;
import com.haibin.httpnet.core.ContentTypeFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * HTTP MultiPart RequestBody，请全体包括文本、文件流等
 */
public class MultiPartContent extends HttpContent {
    public MultiPartContent(RequestParams params, String encode) {
        super(params, encode);
    }

    @Override
    public void doOutput() throws IOException {
        outputText();
        outputFileFormData();
        outputEnd();
    }

    private void outputText() throws IOException {
        StringBuffer buffer = new StringBuffer();
        Set<String> set = mParams.getTextParams().keySet();
        Map<String, String> texts = mParams.getTextParams();
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = texts.get(key);
            buffer.append(END + DATA_TAG + BOUNDARY + END);
            buffer.append("Content-Disposition: form-data; name=\"" + key + "\"");
            buffer.append(END + END);
            buffer.append(value);
        }
        mOutputStream.write(buffer.toString().getBytes(mEncode));
    }

    private void outputFileFormData() throws IOException {
        Set<String> set = mParams.getMultiParams().keySet();
        Map<String, File> fileMap = mParams.getMultiParams();
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
            StringBuffer buffer = new StringBuffer();
            String key = iterator.next();
            File file = fileMap.get(key);
            String fileName = file.getName();
            buffer.append(END + DATA_TAG + BOUNDARY + END);
            buffer.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"");
            buffer.append(END);
            buffer.append("Content-Type: " + ContentTypeFactory.getInstance().getContentType(fileName));
            buffer.append(END + END);
            mOutputStream.writeBytes(buffer.toString());
            outputFile(file);
        }
    }

    private void outputFile(File file) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        long p = 0;
        long length = file.length();
        ProgressListener listener = null;
        int bytes = 0;
        onProgress(listener, p, length);
        byte[] bufferOut = new byte[1024 * 10];
        while ((bytes = in.read(bufferOut)) != -1) {
            mOutputStream.write(bufferOut, 0, bytes);
            p += bytes;
            onProgress(listener, p, length);
        }
        in.close();
    }

    private void onProgress(ProgressListener listener, long currentLength, long allLength) {
        if (listener != null)
            listener.onProgress(currentLength, allLength);
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
        StringBuffer buffer = new StringBuffer();
        intoString(buffer);
        return buffer.substring(0, buffer.length() - 1);
    }

    @Override
    public long getContentLength() {
        long length = 0;
        Map<String, String> text = mParams.getTextParams();
        Map<String, File> multi = mParams.getMultiParams();
        if (text != null) {
            Set<String> set = text.keySet();
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                length += (END + DATA_TAG + BOUNDARY + END + "Content-Disposition: form-data; name=\"" + iterator.next() + "\"" + END + END).length();
            }
        }
        if (multi != null) {
            Set<String> set = multi.keySet();
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
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
