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

import com.haibin.httpnet.core.call.AsyncCall;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 请求分发器，控制并发量
 */

public final class Dispatcher {
    private static final int MAX_REQUEST_TASK = 64;
    private static final int PRE_REQUEST_TASK = 5;
    //private LruCache<String,AsyncCall> mRequestCache = new LruCache<>(1024);
    //private LinkedHashMap<String, AsyncCall> mRequestCache = new LinkedHashMap<>(1024 * 1024);
    private ExecutorService mExecutorService;

    public Dispatcher() {
        this.mExecutorService = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public void enqueue(AsyncCall call) {
        this.mExecutorService.execute(call);
    }
}
