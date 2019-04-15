package com.nss.simplexrest.push.notifications.apns.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

public class ApnsFeedbackConnection {

    private final SocketFactory factory;
    private final String host;
    private final int port;
    private final Proxy proxy;
    private final int readTimeout;
    private final int connectTimeout;
    private final String proxyUsername;
    private final String proxyPassword;

    public ApnsFeedbackConnection(final SocketFactory factory, final String host, final int port) {
        this(factory, host, port, null, 0, 0, null, null);
    }

    public ApnsFeedbackConnection(final SocketFactory factory, final String host, final int port,
            final Proxy proxy, int readTimeout, int connectTimeout, final String proxyUsername, final String proxyPassword) {
        this.factory = factory;
        this.host = host;
        this.port = port;
        this.proxy = proxy;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    int DELAY_IN_MS = 1000;
    private static final int RETRIES = 3;

    public Map<String, Date> getInactiveDevices() throws NetworkIOException {
        int attempts = 0;
        while (true) {
            try {
                attempts++;
                final Map<String, Date> result = getInactiveDevicesImpl();

                attempts = 0;
                return result;
            } catch (final Exception e) {
            	System.out.println("Failed to retrieve invalid devices"+ e);
                if (attempts >= RETRIES) {
                	System.out.println("Couldn't get feedback connection"+ e);
                    Utilities.wrapAndThrowAsRuntimeException(e);
                }
                Utilities.sleep(DELAY_IN_MS);
            }
        }
    }

    public Map<String, Date> getInactiveDevicesImpl() throws IOException {
        Socket proxySocket = null;
        Socket socket = null;
        try {
            if (proxy == null) {
                socket = factory.createSocket(host, port);
            } else if (proxy.type() == Proxy.Type.HTTP) {
                TlsTunnelBuilder tunnelBuilder = new TlsTunnelBuilder();
                socket = tunnelBuilder.build((SSLSocketFactory) factory, proxy, proxyUsername, proxyPassword, host, port);
            } else {
                proxySocket = new Socket(proxy);
                proxySocket.connect(new InetSocketAddress(host, port), connectTimeout);
                socket = ((SSLSocketFactory) factory).createSocket(proxySocket, host, port, false);
            }
            socket.setSoTimeout(readTimeout);
            socket.setKeepAlive(true);
            final InputStream stream = socket.getInputStream();
            return Utilities.parseFeedbackStream(stream);
        } finally {
            Utilities.close(socket);
            Utilities.close(proxySocket);
        }
    }

}
