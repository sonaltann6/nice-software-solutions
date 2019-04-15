package com.nss.simplexrest.push.notifications.apns.internal;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import com.nss.simplexrest.push.notifications.apns.ApnsNotification;
import com.nss.simplexrest.push.notifications.apns.ApnsService;
import com.nss.simplexrest.push.notifications.exceptions.NetworkIOException;

public class QueuedApnsService extends AbstractApnsService {
	
    private ApnsService service;
    private BlockingQueue<ApnsNotification> queue;
    private AtomicBoolean started = new AtomicBoolean(false);

    public QueuedApnsService(ApnsService service) {
        this(service, null);
    }

    public QueuedApnsService(ApnsService service, final ThreadFactory tf) {
        super(null);
        this.service = service;
        this.queue = new LinkedBlockingQueue<ApnsNotification>();
        this.threadFactory = tf == null ? Executors.defaultThreadFactory() : tf;
        this.thread = null;
    }

    @Override
    public void push(ApnsNotification msg) {
        if (!started.get()) {
            throw new IllegalStateException("service hasn't be started or was closed");
        }
        queue.add(msg);
    }

    private final ThreadFactory threadFactory;
    private Thread thread;
    private volatile boolean shouldContinue;

    public void start() {
        if (started.getAndSet(true)) {
            // I prefer if we throw a runtime IllegalStateException here,
            // but I want to maintain semantic backward compatibility.
            // So it is returning immediately here
            return;
        }

        service.start();
        shouldContinue = true;
        thread = threadFactory.newThread(new Runnable() {
            public void run() {
                while (shouldContinue) {
                    try {
                        ApnsNotification msg = queue.take();
                        service.push(msg);
                    } catch (InterruptedException e) {
                    	// ignore
                    } catch (NetworkIOException e) {
                    	// ignore: failed connect...
                    } catch (Exception e) {
                    	// weird if we reached here - something wrong is happening, but we shouldn't stop the service anyway!
                    	System.out.println("Unexpected message caught... Shouldn't be here"+e);
                    }
                }
            }
        });
        thread.start();
    }

    public void stop() {
        started.set(false);
        shouldContinue = false;
        thread.interrupt();
        service.stop();
    }

    @Override
    public Map<String, Date> getInactiveDevices() throws NetworkIOException {
        return service.getInactiveDevices();
    }

    public void testConnection() throws NetworkIOException {
        service.testConnection();
    }

}
