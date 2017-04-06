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
package com.haibin.httpnet.core.connection;

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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * using https ssl 证书管理工具
 */
@SuppressWarnings("all")
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
                throw new AssertionError();
            }
        }
        return mDefaultSslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.mSslSocketFactory = sslSocketFactory;
    }

    /**
     * 采用该方式自行构建 KeyStore
     *
     * @param is   keystore 文件流
     * @param pwd  keystrore 密码
     * @param type 证书类型，java默认的JKS或android的BKS
     * @return KeyStore
     * @throws Exception 类型错误
     */
    public static KeyStore getKeyStore(InputStream is, String pwd, String type) throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(is, pwd.toCharArray());
        is.close();
        return ks;
    }

    /**
     * 如果使用SSL双向验证，即需要客户端证书和客户端私钥，则采用以下方式
     *
     * @param keyStoreStream   客户端证书流
     * @param keyPwd           客户端证书密码
     * @param trustStoreStream 信任TrustStore.keystrore 流
     * @param truesPwd         信任TrustStore.keystrore 密码
     * @return SSLContext
     * @throws Exception 异常
     */
    public static SSLContext getSSLContext(InputStream keyStoreStream, String keyPwd, InputStream trustStoreStream, String truesPwd) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = getKeyStore(keyStoreStream, keyPwd, "");
        keyManagerFactory.init(keyStore, keyPwd.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = getKeyStore(trustStoreStream, truesPwd, "");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TSL");
        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(keyStoreStream);
        keyStore.setCertificateEntry("ca", cert);
        sslContext.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    }

    public static SSLContext getSSLContext(InputStream keyStoreStream, InputStream trustStoreStream, String truesPwd,InputStream t) throws Exception {
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(keyStoreStream);
            keyStore.setCertificateEntry("ca", cert);
            IO.close(keyStoreStream);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, truesPwd.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());//信任证书管理工厂
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(trustStoreStream, truesPwd.toCharArray());

            X509Certificate trues = (X509Certificate) certificatefactory.generateCertificate(t);
            trustStore.setCertificateEntry("ca", cert);

            trustManagerFactory.init(trustStore);
            IO.close(trustStoreStream,t);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IO.close(keyStoreStream, trustStoreStream);
        }
        return null;
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

            SSLContext sslContext = SSLContext.getInstance("TLSv1","AndroidOpenSSL");//安全数据层

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
        ByteArrayInputStream[] cer = new ByteArrayInputStream[cerValues.length];
        for (int i = 0; i < cerValues.length; i++) {
            cer[i] = new ByteArrayInputStream(cerValues[i].getBytes());
        }
        setSslSocketFactory(cer);
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSslSocketFactory == null ? getDefaultSSLSocketFactory() : mSslSocketFactory;
    }
}
