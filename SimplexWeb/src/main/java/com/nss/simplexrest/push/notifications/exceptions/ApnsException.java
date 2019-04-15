package com.nss.simplexrest.push.notifications.exceptions;

/**
 * Base class for all the exceptions thrown in Apns Library
 */
public abstract class ApnsException extends RuntimeException {
    private static final long serialVersionUID = -4756693306121825229L;

    public ApnsException()                      { super(); }
    public ApnsException(String message)        { super(message); }
    public ApnsException(Throwable cause)       { super(cause); }
    public ApnsException(String m, Throwable c) { super(m, c); }

}