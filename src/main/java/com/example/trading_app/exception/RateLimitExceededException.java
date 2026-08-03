package com.example.trading_app.exception;

public class RateLimitExceededException extends RuntimeException {
    private int remainingAttempts;
    private long timeRemainingSeconds;

    public RateLimitExceededException(String message, int remainingAttempts, long timeRemainingSeconds) {
        super(message);
        this.remainingAttempts = remainingAttempts;
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public long getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public String getTimeRemainingFormatted() {
        long minutes = timeRemainingSeconds / 60;
        long seconds = timeRemainingSeconds % 60;
        return String.format("%d minutes %d seconds", minutes, seconds);
    }
}

