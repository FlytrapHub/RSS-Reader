package com.flytrap.rssreader.domain.folder;

public enum SharedStatus {
    SHARED, PRIVATE;

    public static SharedStatus from(boolean isShared) {
        return isShared ? SHARED : PRIVATE;
    }
}
