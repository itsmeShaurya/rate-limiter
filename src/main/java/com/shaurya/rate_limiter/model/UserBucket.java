package com.shaurya.rate_limiter.model;

public class UserBucket {
    private int requestCount;
    private long windowTime;

    public UserBucket() {
        this.requestCount = requestCount;
        this.windowTime = windowTime;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public long getWindowTime() {
        return windowTime;
    }

    public void setWindowTime(long windowTime) {
        this.windowTime = windowTime;
    }
}
