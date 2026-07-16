package com.shaurya.rate_limiter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterProperties {

    private int maxRequests;
    private long windowSizeSeconds;
    private String algorithm;

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public long getWindowSizeSeconds() {
        return windowSizeSeconds;
    }

    public void setWindowSizeSeconds(long windowSizeSeonds) {
        this.windowSizeSeconds = windowSizeSeonds;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
