package com.nss.simplexrest.push.notifications.exceptions;

public class InvalidSSLConfig extends ApnsException {
    private static final long serialVersionUID = -7283168775864517167L;

    public InvalidSSLConfig()                      { super(); }
    public InvalidSSLConfig(String message)        { super(message); }
    public InvalidSSLConfig(Throwable cause)       { super(cause); }
    public InvalidSSLConfig(String m, Throwable c) { super(m, c); }

}
