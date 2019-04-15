package com.nss.simplexrest.push.notifications.apns;

/**
 * The main class to interact with the APNS Service.
 *
 * Provides an interface to create the {@link ApnsServiceBuilder} and
 * {@code ApnsNotification} payload.
 *
 */
public final class APNS {

    private APNS() { throw new AssertionError("Uninstantiable class"); }

    /**
     * Returns a new Payload builder
     */
    public static PayloadBuilder newPayload() {
        return new PayloadBuilder();
    }

    /**
     * Returns a new APNS Service for sending iPhone notifications
     */
    public static ApnsServiceBuilder newService() {
        return new ApnsServiceBuilder();
    }
}
