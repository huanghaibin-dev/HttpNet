package com.haibin.httpnet.core.connect;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 验证
 */

public class HostVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return "localhost".equals(hostname);
    }
}
