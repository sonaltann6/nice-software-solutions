package com.nss.simplexrest.push.notifications.exceptions;

import com.nss.simplexrest.push.notifications.apns.DeliveryError;

public class ApnsDeliveryErrorException extends ApnsException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DeliveryError deliveryError;

    public ApnsDeliveryErrorException(DeliveryError error) {
        this.deliveryError = error;
    }

    public String getMessage() {
        return "Failed to deliver notification with error code " + deliveryError.code();
    }

    public DeliveryError getDeliveryError() {
        return deliveryError;
    }    
}
