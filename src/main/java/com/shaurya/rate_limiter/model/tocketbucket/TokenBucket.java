package com.shaurya.rate_limiter.model.tocketbucket;

public class TokenBucket {
    private double availableTokens;
    private long lastRefillTime;

    public TokenBucket() {
    }

    public TokenBucket(double availableTokens, long lastRefillTime) {
        this.availableTokens = availableTokens;
        this.lastRefillTime = lastRefillTime;
    }

    public double getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(double availableTokens) {
        this.availableTokens = availableTokens;
    }

    public long getLastRefillTime() {
        return lastRefillTime;
    }

    public void setLastRefillTime(long lastRefillTime) {
        this.lastRefillTime = lastRefillTime;
    }
}
