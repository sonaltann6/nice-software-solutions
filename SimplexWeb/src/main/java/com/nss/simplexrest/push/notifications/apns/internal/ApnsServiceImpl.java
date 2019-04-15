package com.nss.simplexrest.push.notifications.apns.internal;

import com.nss.simplexrest.push.notifications.apns.ApnsNotification;
import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

public class ApnsServiceImpl extends AbstractApnsService {
    private ApnsConnection connection;

    public ApnsServiceImpl(ApnsConnection connection, ApnsFeedbackConnection feedback) {
        super(feedback);
        this.connection = connection;
    }

    @Override
    public void push(ApnsNotification msg) throws NetworkIOException {
        connection.sendMessage(msg);
    }

    public void start() {
    }

    public void stop() {
        Utilities.close(connection);
    }

    public void testConnection() {
        connection.testConnection();
    }
}