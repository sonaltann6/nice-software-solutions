package com.nss.simplexrest.push.notifications.apns.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.nss.simplexrest.push.notifications.apns.ApnsNotification;
import com.nss.simplexrest.push.notifications.apns.ApnsService;
import com.nss.simplexrest.push.notifications.apns.EnhancedApnsNotification;
import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

abstract class AbstractApnsService implements ApnsService {
    private ApnsFeedbackConnection feedback;
    private AtomicInteger c = new AtomicInteger();

    public AbstractApnsService(ApnsFeedbackConnection feedback) {
        this.feedback = feedback;
    }

    public EnhancedApnsNotification push(String deviceToken, String payload) throws NetworkIOException {
        EnhancedApnsNotification notification =
            new EnhancedApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, deviceToken, payload);
        push(notification);
        return notification;
    }

    public EnhancedApnsNotification push(String deviceToken, String payload, Date expiry) throws NetworkIOException {
        EnhancedApnsNotification notification =
            new EnhancedApnsNotification(c.incrementAndGet(), (int)(expiry.getTime() / 1000), deviceToken, payload);
        push(notification);
        return notification;
    }

    public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload) throws NetworkIOException {
        EnhancedApnsNotification notification =
            new EnhancedApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, deviceToken, payload);
        push(notification);
        return notification;
    }

    public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload, int expiry) throws NetworkIOException {
        EnhancedApnsNotification notification =
            new EnhancedApnsNotification(c.incrementAndGet(), expiry, deviceToken, payload);
        push(notification);
        return notification;
    }

    public Collection<EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload) throws NetworkIOException {
        byte[] messageBytes = Utilities.toUTF8Bytes(payload);
        ArrayList<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (String deviceToken : deviceTokens) {
            byte[] dtBytes = Utilities.decodeHex(deviceToken);
            EnhancedApnsNotification notification =
                new EnhancedApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, dtBytes, messageBytes);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    public Collection<EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload, Date expiry) throws NetworkIOException {
        byte[] messageBytes = Utilities.toUTF8Bytes(payload);
        ArrayList<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (String deviceToken : deviceTokens) {
            byte[] dtBytes = Utilities.decodeHex(deviceToken);
            EnhancedApnsNotification notification =
                new EnhancedApnsNotification(c.incrementAndGet(), (int)(expiry.getTime() / 1000), dtBytes, messageBytes);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    public Collection<EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload) throws NetworkIOException {
    	ArrayList<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (byte[] deviceToken : deviceTokens) {
            EnhancedApnsNotification notification =
                new EnhancedApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, deviceToken, payload);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    public Collection<EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload, int expiry) throws NetworkIOException {
    	ArrayList<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (byte[] deviceToken : deviceTokens) {
            EnhancedApnsNotification notification =
                new EnhancedApnsNotification(c.incrementAndGet(), expiry, deviceToken, payload);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    public abstract void push(ApnsNotification message) throws NetworkIOException;

    public Map<String, Date> getInactiveDevices() throws NetworkIOException {
        return feedback.getInactiveDevices();
    }
}
