package com.shaurya.rate_limiter.model.slidingwindow;

import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowBucket {

    private final Deque<Long> requestTimeStamps = new ArrayDeque<>();

    public Deque<Long> getRequestTimeStamps() {
        return requestTimeStamps;
    }
}
