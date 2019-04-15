package com.nss.simplexrest.push.notifications.apns;

/**
 * Represents an APNS notification to be sent to Apple service.
 */
public interface ApnsNotification {

    /**
     * Returns the binary representation of the device token.
     */
    public byte[] getDeviceToken();

    /**
     * Returns the binary representation of the payload.
     *
     */
    public byte[] getPayload();

    /**
     * Returns the identifier of the current message.  The
     * identifier is an application generated identifier.
     *
     * @return the notification identifier
     */
    public int getIdentifier();

    /**
     * Returns the expiry date of the notification, a fixed UNIX
     * epoch date expressed in seconds
     *
     * @return the expiry date of the notification
     */
    public int getExpiry();

    /**
     * Returns the binary representation of the message as expected by the
     * APNS server.
     *
     * The returned array can be used to sent directly to the APNS server
     * (on the wire/socket) without any modification.
     */
    public byte[] marshall();
}
