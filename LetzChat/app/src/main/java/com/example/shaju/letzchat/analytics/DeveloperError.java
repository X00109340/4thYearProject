package com.example.shaju.letzchat.analytics;



public class DeveloperError extends Error {

    public DeveloperError(String detailMessage) {
        super(detailMessage);
    }

    public DeveloperError(String messageTemplate, Object... args) {
        super(String.format(messageTemplate, args));
    }

    public DeveloperError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}

