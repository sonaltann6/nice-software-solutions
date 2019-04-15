package com.nss.simplexrest.push.notifications.apns;

/**
 * Errors in delivery that may get reported by Apple APN servers
 */
public enum DeliveryError {
    /**
     * Connection closed without any error.
     *
     * This may occur if the APN service faces an invalid simple
     * APNS notification while running in enhanced mode
     */
    NO_ERROR(0),
    PROCESSING_ERROR(1),
    MISSING_DEVICE_TOKEN(2),
    MISSING_TOPIC(3),
    MISSING_PAYLOAD(4),
    INVALID_TOKEN_SIZE(5),
    INVALID_TOPIC_SIZE(6),
    INVALID_PAYLOAD_SIZE(7),
    INVALID_TOKEN(8),

    NONE(255),
    UNKNOWN(254);

    private final byte code;
    DeliveryError(int code) {
        this.code = (byte)code;
    }

    /** The status code as specified by Apple */
    public int code() {
        return code;
    }

    /**
     * Returns the appropriate {@code DeliveryError} enum
     * corresponding to the Apple provided status code
     *
     * @param code  status code provided by Apple
     * @return  the appropriate DeliveryError
     */
    public static DeliveryError ofCode(int code) {
        for (DeliveryError e : DeliveryError.values()) {
            if (e.code == code)
                return e;
        }

        return UNKNOWN;
    }
}
