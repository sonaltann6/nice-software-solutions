package com.nss.simplexrest.push.notifications.exceptions;

import java.io.IOException;

public class RuntimeIOException extends ApnsException {
    private static final long serialVersionUID = 8665285084049041306L;

    public RuntimeIOException()                      { super(); }
    public RuntimeIOException(String message)        { super(message); }
    public RuntimeIOException(IOException cause)       { super(cause); }
    public RuntimeIOException(String m, IOException c) { super(m, c); }

}
