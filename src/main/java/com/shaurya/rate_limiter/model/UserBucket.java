package com.shaurya.rate_limiter.model;

import java.util.concurrent.atomic.AtomicInteger;

public class UserBucket {
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private volatile long windowStartTime = System.currentTimeMillis();

    public AtomicInteger getRequestCount() {
        return requestCount;
    }

    public long getWindowStartTime() {
        return windowStartTime;
    }

    public void setWindowStartTime(long windowStartTime) {
        this.windowStartTime = windowStartTime;
    }
}
