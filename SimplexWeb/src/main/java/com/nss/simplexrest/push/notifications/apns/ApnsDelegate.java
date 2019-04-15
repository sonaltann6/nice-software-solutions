package com.nss.simplexrest.push.notifications.apns;

/**
 * A delegate that gets notified of the status of notification delivery to the
 * Apple Server.
 *
 * The delegate doesn't get notified when the notification actually arrives at
 * the phone.
 */
public interface ApnsDelegate {

    /**
     * Called when message was successfully sent to the Apple servers
     *
     * @param message the notification that was sent
     * @param resent whether the notification was resent after an error
     */
    public void messageSent(ApnsNotification message, boolean resent);

    /**
     * Called when the delivery of the message failed for any reason
     *
     * If message is null, then your notification has been rejected by Apple but
     * it has been removed from the cache so it is not possible to identify
     * which notification caused the error. In this case subsequent
     * notifications may be lost. If this happens you should consider increasing
     * your cacheLength value to prevent data loss.
     *
     * @param message the notification that was attempted to be sent
     * @param e the cause and description of the failure
     */
    public void messageSendFailed(ApnsNotification message, Throwable e);

    /**
     * The connection was closed and/or an error packet was received while
     * monitoring was turned on.
     *
     * @param e the delivery error
     * @param messageIdentifier  id of the message that failed
     */
    public void connectionClosed(DeliveryError e, int messageIdentifier);

    /**
     * The resend cache needed a bigger size (while resending messages)
     *
     * @param newCacheLength new size of the resend cache.
     */
    public void cacheLengthExceeded(int newCacheLength);

    /**
     * A number of notifications has been queued for resending due to a error-response
     * packet being received.
     *
     * @param resendCount the number of messages being queued for resend
     */
    public void notificationsResent(int resendCount);
    
    /**
     * A no operation delegate that does nothing!
     */
    public final static ApnsDelegate EMPTY = new ApnsDelegateAdapter();
}
