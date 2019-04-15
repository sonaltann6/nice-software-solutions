package com.nss.simplexrest.push.notifications.apns.internal;

import java.io.Closeable;

import com.nss.simplexrest.push.notifications.apns.ApnsNotification;
import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

public interface ApnsConnection extends Closeable {

    //Default number of notifications to keep for error purposes
    public static final int DEFAULT_CACHE_LENGTH = 100;

    void sendMessage(ApnsNotification m) throws NetworkIOException;

    void testConnection() throws NetworkIOException;

    ApnsConnection copy();
    
    void setCacheLength(int cacheLength);
    
    int getCacheLength();
}
