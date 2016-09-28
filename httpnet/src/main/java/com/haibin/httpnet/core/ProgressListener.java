package com.haibin.httpnet.core;

/**
 *
 */

public interface ProgressListener {
    void onProgress(long currentLength, long allLength);
}
