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
package com.haibin.httpnet.core.connect;

import com.haibin.httpnet.core.io.IO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * using https ssl
 */

public final class SSLManager {

    private static SSLSocketFactory mDefaultSslSocketFactory;
    private SSLSocketFactory mSslSocketFactory;

    private synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
        if (mDefaultSslSocketFactory == null) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                mDefaultSslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new AssertionError(); // The system has no TLS. Just give up.
            }
        }
        return mDefaultSslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.mSslSocketFactory = sslSocketFactory;
    }

    /**
     * 添加证书文件流，可添加多个
     */
    public void setSslSocketFactory(InputStream... cerInputStream) {
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream is : cerInputStream) {
                X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(is);
                keyStore.setCertificateEntry("alias" + index++, cert);
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");//安全数据层

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());//信任证书管理工厂

            trustManagerFactory.init(keyStore);

            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            mSslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IO.close(cerInputStream);
        }
    }

    /**
     * 添加证书文件，可添加多个
     *
     * @param cerPaths cerPaths
     */
    public void setSslSocketFactory(String... cerPaths) {
        FileInputStream[] cers = new FileInputStream[cerPaths.length];
        for (int i = 0; i < cerPaths.length; i++) {
            File file = new File(cerPaths[i]);
            if (file.exists()) {
                try {
                    cers[i] = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        setSslSocketFactory(cers);
    }

    /**
     * 添加证书文本
     *
     * @param cerValues cerValues
     */
    public void setSslSocketFactoryAsString(String... cerValues) {
        ByteArrayInputStream[] cers = new ByteArrayInputStream[cerValues.length];
        for (int i = 0; i < cerValues.length; i++) {
            cers[i] = new ByteArrayInputStream(cerValues[i].getBytes());
        }
        setSslSocketFactory(cers);
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSslSocketFactory == null ? getDefaultSSLSocketFactory() : mSslSocketFactory;
    }
}
