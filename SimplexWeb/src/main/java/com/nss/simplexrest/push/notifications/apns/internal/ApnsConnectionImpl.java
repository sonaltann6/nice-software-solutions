package com.nss.simplexrest.push.notifications.apns.internal;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

import com.nss.simplexrest.push.notifications.apns.ApnsDelegate;
import com.nss.simplexrest.push.notifications.apns.ApnsNotification;
import com.nss.simplexrest.push.notifications.apns.DeliveryError;
import com.nss.simplexrest.push.notifications.apns.EnhancedApnsNotification;
import com.nss.simplexrest.push.notifications.apns.ReconnectPolicy;
import com.nss.simplexrest.push.notifications.apns.StartSendingApnsDelegate;
import com.nss.simplexrest.push.notifications.exceptions.ApnsDeliveryErrorException;
import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

public class ApnsConnectionImpl implements ApnsConnection {

    private final SocketFactory factory;
    private final String host;
    private final int port;
    private final int readTimeout;
    private final int connectTimeout;
    private final Proxy proxy;
    private final String proxyUsername;
    private final String proxyPassword;
    private final ReconnectPolicy reconnectPolicy;
    private final ApnsDelegate delegate;
    private int cacheLength;
    private final boolean errorDetection;
    private final ThreadFactory threadFactory;
    private final boolean autoAdjustCacheLength;
    private final ConcurrentLinkedQueue<ApnsNotification> cachedNotifications, notificationsBuffer;
    private Socket socket;
    private final AtomicInteger threadId = new AtomicInteger(0);

    public ApnsConnectionImpl(SocketFactory factory, String host, int port) {
        this(factory, host, port, new ReconnectPolicies.Never(), ApnsDelegate.EMPTY);
    }

    private ApnsConnectionImpl(SocketFactory factory, String host, int port, ReconnectPolicy reconnectPolicy, ApnsDelegate delegate) {
        this(factory, host, port, null, null, null, reconnectPolicy, delegate);
    }

    private ApnsConnectionImpl(SocketFactory factory, String host, int port, Proxy proxy, String proxyUsername, String proxyPassword,
                               ReconnectPolicy reconnectPolicy, ApnsDelegate delegate) {
        this(factory, host, port, proxy, proxyUsername, proxyPassword, reconnectPolicy, delegate, false, null,
                ApnsConnection.DEFAULT_CACHE_LENGTH, true, 0, 0);
    }

    public ApnsConnectionImpl(SocketFactory factory, String host, int port, Proxy proxy, String proxyUsername, String proxyPassword,
                              ReconnectPolicy reconnectPolicy, ApnsDelegate delegate, boolean errorDetection, ThreadFactory tf, int cacheLength,
                              boolean autoAdjustCacheLength, int readTimeout, int connectTimeout) {
        this.factory = factory;
        this.host = host;
        this.port = port;
        this.reconnectPolicy = reconnectPolicy;
        this.delegate = delegate == null ? ApnsDelegate.EMPTY : delegate;
        this.proxy = proxy;
        this.errorDetection = errorDetection;
        this.threadFactory = tf == null ? defaultThreadFactory() : tf;
        this.cacheLength = cacheLength;
        this.autoAdjustCacheLength = autoAdjustCacheLength;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        cachedNotifications = new ConcurrentLinkedQueue<ApnsNotification>();
        notificationsBuffer = new ConcurrentLinkedQueue<ApnsNotification>();
    }

    private ThreadFactory defaultThreadFactory() {
        return new ThreadFactory() {
            ThreadFactory wrapped = Executors.defaultThreadFactory();
            @Override
            public Thread newThread( Runnable r )
            {
                Thread result = wrapped.newThread(r);
                result.setName("MonitoringThread-"+threadId.incrementAndGet());
                result.setDaemon(true);
                return result;
            }
        };
    }

    public synchronized void close() {
        Utilities.close(socket);
    }

    private void monitorSocket(final Socket socketToMonitor) {
    	System.out.println("Launching Monitoring Thread for socket {}"+ socketToMonitor);

        Thread t = threadFactory.newThread(new Runnable() {
            final static int EXPECTED_SIZE = 6;

            @Override
            public void run() {
            	System.out.println("Started monitoring thread");
                try {
                    InputStream in;
                    try {
                        in = socketToMonitor.getInputStream();
                    } catch (IOException ioe) {
                    	System.out.println("The value of socket is null"+ ioe);
                        in = null;
                    }

                    byte[] bytes = new byte[EXPECTED_SIZE];
                    while (in != null && readPacket(in, bytes)) {
                    	System.out.println("Error-response packet {}"+ Utilities.encodeHex(bytes));
                        // Quickly close socket, so we won't ever try to send push notifications
                        // using the defective socket.
                        Utilities.close(socketToMonitor);

                        int command = bytes[0] & 0xFF;
                        if (command != 8) {
                            throw new IOException("Unexpected command byte " + command);
                        }
                        int statusCode = bytes[1] & 0xFF;
                        DeliveryError e = DeliveryError.ofCode(statusCode);

                        int id = Utilities.parseBytes(bytes[2], bytes[3], bytes[4], bytes[5]);

                        System.out.println("Closed connection cause={}; id={}"+ id);
                        delegate.connectionClosed(e, id);

                        Queue<ApnsNotification> tempCache = new LinkedList<ApnsNotification>();
                        ApnsNotification notification = null;
                        boolean foundNotification = false;

                        while (!cachedNotifications.isEmpty()) {
                            notification = cachedNotifications.poll();
                            System.out.println("Candidate for removal, message id {}"+ notification.getIdentifier());

                            if (notification.getIdentifier() == id) {
                            	System.out.println("Bad message found {}"+ notification.getIdentifier());
                                foundNotification = true;
                                break;
                            }
                            tempCache.add(notification);
                        }

                        if (foundNotification) {
                        	System.out.println("delegate.messageSendFailed, message id {}"+ notification.getIdentifier());
                            delegate.messageSendFailed(notification, new ApnsDeliveryErrorException(e));
                        } else {
                            cachedNotifications.addAll(tempCache);
                            int resendSize = tempCache.size();
                            System.out.println("Received error for message that wasn't in the cache...");
                            if (autoAdjustCacheLength) {
                                cacheLength = cacheLength + (resendSize / 2);
                                delegate.cacheLengthExceeded(cacheLength);
                            }
                            System.out.println("delegate.messageSendFailed, unknown id");
                            delegate.messageSendFailed(null, new ApnsDeliveryErrorException(e));
                        }

                        int resendSize = 0;

                        while (!cachedNotifications.isEmpty()) {

                            resendSize++;
                            final ApnsNotification resendNotification = cachedNotifications.poll();
                            System.out.println("Queuing for resend {}"+ resendNotification.getIdentifier());
                            notificationsBuffer.add(resendNotification);
                        }
                        System.out.println("resending {} notifications"+ resendSize);
                        delegate.notificationsResent(resendSize);
                    }
                    System.out.println("Monitoring input stream closed by EOF");

                } catch (IOException e) {
                    // An exception when reading the error code is non-critical, it will cause another retry
                    // sending the message. Other than providing a more stable network connection to the APNS
                    // server we can't do much about it - so let's not spam the application's error log.
                	System.out.println("Exception while waiting for error code"+ e);
                    delegate.connectionClosed(DeliveryError.UNKNOWN, -1);
                } finally {
                    Utilities.close(socketToMonitor);
                    drainBuffer();
                }
            }

            /**
             * Read a packet like in.readFully(bytes) does - but do not throw an exception and return false if nothing
             * could be read at all.
             * @param in the input stream
             * @param bytes the array to be filled with data
             * @return true if a packet as been read, false if the stream was at EOF right at the beginning.
             * @throws IOException When a problem occurs, especially EOFException when there's an EOF in the middle of the packet.
             */
            private boolean readPacket(final InputStream in, final byte[] bytes) throws IOException {
                final int len = bytes.length;
                int n = 0;
                while (n < len) {
                    try {
                        int count = in.read(bytes, n, len - n);
                        if (count < 0) {
                            throw new EOFException("EOF after reading "+n+" bytes of new packet.");
                        }
                        n += count;
                    } catch (IOException ioe) {
                        if (n == 0)
                            return false;
                        throw new IOException("Error after reading "+n+" bytes of packet", ioe);
                    }
                }
                return true;
            }
        });
        t.start();
    }

    private synchronized Socket getOrCreateSocket(boolean resend) throws NetworkIOException {
        if (reconnectPolicy.shouldReconnect()) {
        	System.out.println("Reconnecting due to reconnectPolicy dictating it");
            Utilities.close(socket);
            socket = null;
        }

        if (socket == null || socket.isClosed()) {
            try {
                if (proxy == null) {
                    socket = factory.createSocket(host, port);
                    System.out.println("Connected new socket {}"+ socket);
                } else if (proxy.type() == Proxy.Type.HTTP) {
                    TlsTunnelBuilder tunnelBuilder = new TlsTunnelBuilder();
                    socket = tunnelBuilder.build((SSLSocketFactory) factory, proxy, proxyUsername, proxyPassword, host, port);
                    System.out.println("Connected new socket through http tunnel {}"+ socket);
                } else {
                    boolean success = false;
                    Socket proxySocket = null;
                    try {
                        proxySocket = new Socket(proxy);
                        proxySocket.connect(new InetSocketAddress(host, port), connectTimeout);
                        socket = ((SSLSocketFactory) factory).createSocket(proxySocket, host, port, false);
                        success = true;
                    } finally {
                        if (!success) {
                            Utilities.close(proxySocket);
                        }
                    }
                    System.out.println("Connected new socket through socks tunnel {}"+ socket);
                }

                socket.setSoTimeout(readTimeout);
                socket.setKeepAlive(true);

                if (errorDetection) {
                    monitorSocket(socket);
                }

                reconnectPolicy.reconnected();
                System.out.println("Made a new connection to APNS");
            } catch (IOException e) {
            	System.out.println("Couldn't connect to APNS server"+ e);
                // indicate to clients whether this is a resend or initial send
                throw new NetworkIOException(e, resend);
            }
        }
        return socket;
    }

    int DELAY_IN_MS = 1000;
    private static final int RETRIES = 3;

    public synchronized void sendMessage(ApnsNotification m) throws NetworkIOException {
        sendMessage(m, false);
        drainBuffer();
    }

    private synchronized void sendMessage(ApnsNotification m, boolean fromBuffer) throws NetworkIOException {
    	System.out.println("sendMessage {} fromBuffer: {}"+ m+ fromBuffer);

        if (delegate instanceof StartSendingApnsDelegate) {
            ((StartSendingApnsDelegate) delegate).startSending(m, fromBuffer);
        }

        int attempts = 0;
        while (true) {
            try {
                attempts++;
                Socket socket = getOrCreateSocket(fromBuffer);
                socket.getOutputStream().write(m.marshall());
                socket.getOutputStream().flush();
                cacheNotification(m);

                delegate.messageSent(m, fromBuffer);

                //logger.debug("Message \"{}\" sent", m);
                attempts = 0;
                break;
            } catch (SSLHandshakeException e) {
                // No use retrying this, it's dead Jim
                throw new NetworkIOException(e);
            } catch (IOException e) {
                Utilities.close(socket);
                if (attempts >= RETRIES) {
                	System.out.println("Couldn't send message after " + RETRIES + " retries." + m+ e);
                    delegate.messageSendFailed(m, e);
                    Utilities.wrapAndThrowAsRuntimeException(e);
                }
                // The first failure might be due to closed connection (which in turn might be caused by
                // a message containing a bad token), so don't delay for the first retry.
                //
                // Additionally we don't want to spam the log file in this case, only after the second retry
                // which uses the delay.

                if (attempts != 1) {
                	System.out.println("Failed to send message " + m + "... trying again after delay"+ e);
                    Utilities.sleep(DELAY_IN_MS);
                }
            }
        }
    }

    private synchronized void drainBuffer() {
    	System.out.println("draining buffer");
        while (!notificationsBuffer.isEmpty()) {
            final ApnsNotification notification = notificationsBuffer.poll();
            try {
                sendMessage(notification, true);
            }
            catch (NetworkIOException ex) {
                // at this point we are retrying the submission of messages but failing to connect to APNS, therefore
                // notify the client of this
                delegate.messageSendFailed(notification, ex);
            }
        }
    }

    private void cacheNotification(ApnsNotification notification) {
        cachedNotifications.add(notification);
        while (cachedNotifications.size() > cacheLength) {
            cachedNotifications.poll();
            System.out.println("Removing notification from cache " + notification);
        }
    }

    public ApnsConnectionImpl copy() {
        return new ApnsConnectionImpl(factory, host, port, proxy, proxyUsername, proxyPassword, reconnectPolicy.copy(), delegate,
                errorDetection, threadFactory, cacheLength, autoAdjustCacheLength, readTimeout, connectTimeout);
    }

    public void testConnection() throws NetworkIOException {
        ApnsConnectionImpl testConnection = null;
        try {
            testConnection =
                    new ApnsConnectionImpl(factory, host, port, proxy, proxyUsername, proxyPassword, reconnectPolicy.copy(), delegate);
            final ApnsNotification notification = new EnhancedApnsNotification(0, 0, new byte[]{0}, new byte[]{0});
            testConnection.sendMessage(notification);
        } finally {
            if (testConnection != null) {
                testConnection.close();
            }
        }
    }

    public void setCacheLength(int cacheLength) {
        this.cacheLength = cacheLength;
    }

    public int getCacheLength() {
        return cacheLength;
    }
}