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
package com.haibin.httpnet.core.call;

import com.haibin.httpnet.core.Response;

import java.io.IOException;

/**
 * 执行Request
 */
public interface Call {

    /**
     * 添加拦截上传监听
     *
     * @param listener 请求监听
     * @return call
     */
    Call intercept(InterceptListener listener);

    /**
     * 异步执行一个请求
     *
     * @param callBack 回调
     */
    void execute(Callback callBack);

    /**
     * 同步执行一个请求，必须在子线程执行
     *
     * @return 请求结果
     * @throws IOException 异常
     */
    Response execute() throws IOException;

    /**
     * 取消一个http请求
     */
    void cancel();
}
