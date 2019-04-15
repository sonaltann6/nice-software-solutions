package com.nss.simplexrest.push.notifications.apns;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

/**
 * Represents the connection and interface to the Apple APNS servers.
 *
 * The service is created by {@link ApnsServiceBuilder} like:
 *
 * <pre>
 *   ApnsService = APNS.newService()
 *                  .withCert("/path/to/certificate.p12", "MyCertPassword")
 *                  .withSandboxDestination()
 *                  .build()
 * </pre>
 */
public interface ApnsService {

    /**
     * Sends a push notification with the provided {@code payload} to the
     * iPhone of {@code deviceToken}.
     *
     * The payload needs to be a valid JSON object, otherwise it may fail
     * silently.  It is recommended to use {@link PayloadBuilder} to create
     * one.
     *
     * @param deviceToken   the destination iPhone device token
     * @param payload       The payload message
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    ApnsNotification push(String deviceToken, String payload) throws NetworkIOException;

    EnhancedApnsNotification push(String deviceToken, String payload, Date expiry) throws NetworkIOException;

    /**
     * Sends a push notification with the provided {@code payload} to the
     * iPhone of {@code deviceToken}.
     *
     * The payload needs to be a valid JSON object, otherwise it may fail
     * silently.  It is recommended to use {@link PayloadBuilder} to create
     * one.
     *
     * @param deviceToken   the destination iPhone device token
     * @param payload       The payload message
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    ApnsNotification push(byte[] deviceToken, byte[] payload) throws NetworkIOException;

    EnhancedApnsNotification push(byte[] deviceToken, byte[] payload, int expiry) throws NetworkIOException;

    /**
     * Sends a bulk push notification with the provided
     * {@code payload} to iPhone of {@code deviceToken}s set.
     *
     * The payload needs to be a valid JSON object, otherwise it may fail
     * silently.  It is recommended to use {@link PayloadBuilder} to create
     * one.
     *
     * @param deviceTokens   the destination iPhone device tokens
     * @param payload       The payload message
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    Collection<? extends ApnsNotification> push(Collection<String> deviceTokens, String payload) throws NetworkIOException;
    Collection<? extends EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload, Date expiry) throws NetworkIOException;

    /**
     * Sends a bulk push notification with the provided
     * {@code payload} to iPhone of {@code deviceToken}s set.
     *
     * The payload needs to be a valid JSON object, otherwise it may fail
     * silently.  It is recommended to use {@link PayloadBuilder} to create
     * one.
     *
     * @param deviceTokens   the destination iPhone device tokens
     * @param payload       The payload message
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    Collection<? extends ApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload) throws NetworkIOException;
    Collection<? extends EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload, int expiry) throws NetworkIOException;

    /**
     * Sends the provided notification {@code message} to the desired
     * destination.
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    void push(ApnsNotification message) throws NetworkIOException;

    /**
     * Starts the service.
     *
     * The underlying implementation may prepare its connections or
     * data structures to be able to send the messages.
     *
     * This method is a blocking call, even if the service represents
     * a Non-blocking push service.  Once the service is returned, it is ready
     * to accept push requests.
     *
     * @throws NetworkIOException if a network error occurred while
     *      starting the service
     */
    void start();

    /**
     * Stops the service and frees any allocated resources it created for this
     * service.
     *
     * The underlying implementation should close all connections it created,
     * and possibly stop any threads as well.
     */
    void stop();

    /**
     * Returns the list of devices that reported failed-delivery
     * attempts to the Apple Feedback services.
     *
     * The result is map, mapping the device tokens as Hex Strings
     * mapped to the timestamp when APNs determined that the
     * application no longer exists on the device.
     * @throws NetworkIOException if a network error occurred
     *      while retrieving invalid device connection
     */
    Map<String, Date> getInactiveDevices() throws NetworkIOException;

    /**
     * Test that the service is setup properly and the Apple servers
     * are reachable.
     *
     * @throws NetworkIOException   if the Apple servers aren't reachable
     *      or the service cannot send notifications for now
     */
    void testConnection() throws NetworkIOException;
    
}