package com.nss.simplexrest.push.notifications.exceptions;

import java.io.IOException;

public class NetworkIOException extends ApnsException {
    private static final long serialVersionUID = 3353516625486306533L;

    private boolean resend;

    public NetworkIOException()                      { super(); }
    public NetworkIOException(String message)        { super(message); }
    public NetworkIOException(IOException cause)       { super(cause); }
    public NetworkIOException(String m, IOException c) { super(m, c); }
    public NetworkIOException(IOException cause, boolean resend) {
        super(cause);
        this.resend = resend;
    }

    /**
     * Identifies whether an exception was thrown during a resend of a
     * message or not.  In this case a resend refers to whether the
     * message is being resent from the buffer of messages internal.
     * This would occur if we sent 5 messages quickly to APNS:
     * 1,2,3,4,5 and the 3 message was rejected.  We would
     * then need to resend 4 and 5.  If a network exception was
     * triggered when doing this, then the resend flag will be
     * {@code true}.
     * @return {@code true} for an exception trigger during a resend, otherwise {@code false}.
     */
    public boolean isResend() {
        return resend;
    }

}
