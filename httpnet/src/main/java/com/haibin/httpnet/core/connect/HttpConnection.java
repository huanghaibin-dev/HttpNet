package com.haibin.httpnet.core.connect;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 *
 */

public class HttpConnection extends Connection {
    private HttpURLConnection mConnection;

    @Override
    protected void convertConnect() {
        mConnection = (HttpURLConnection) mUrlConnection;
    }

    @Override
    protected void initMethod(String method) throws ProtocolException {
        mConnection.setRequestMethod(method);
    }

    @Override
    protected int getResponseCode() throws IOException {
        return mConnection.getResponseCode();
    }

    @Override
    protected void finish() {
        super.finish();
        mConnection.disconnect();
    }
}
