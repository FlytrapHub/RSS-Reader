package com.flytrap.rssreader.global.exception;

public class ApplicationException extends RuntimeException {

    private final String defaultCode;

    public ApplicationException(String defaultCode, String message) {
        super(message);
        this.defaultCode = defaultCode;
    }

    public String getDefaultCode() {
        return defaultCode;
    }

}
