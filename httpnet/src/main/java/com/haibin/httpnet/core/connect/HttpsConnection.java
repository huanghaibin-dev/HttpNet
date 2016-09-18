package com.haibin.httpnet.core.connect;

import java.io.IOException;
import java.net.ProtocolException;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 */

public class HttpsConnection extends Connection {
    private HttpsURLConnection mConnection;

    @Override
    protected int getResponseCode() throws IOException {
        return 0;
    }

    @Override
    protected void initMethod(String method) throws ProtocolException {

    }

    @Override
    protected void convertConnect() {
        mConnection = (HttpsURLConnection) mUrlConnection;
    }
}
