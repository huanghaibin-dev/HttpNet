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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.Buffer;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * using https ssl
 */

public final class ConnectionManager {
    /**
     * add cer
     */
    public static void setSslSocketFactory(InputStream... cerInputStream) {
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            for (InputStream is : cerInputStream) {
                X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(is);
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                keyStore.setCertificateEntry("secretKeyAlias", cert);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory =
                        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * add cer
     *
     * @param cerPath cerPath
     */
    public static void setSslSocketFactory(String cerPath) {
        File file = new File(cerPath);
        if (file.exists()) {
            try {
                setSslSocketFactory(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setSslSocketFactoryAsString(String cerValue) {
        setSslSocketFactory(new ByteArrayInputStream(cerValue.getBytes()));
    }
}
