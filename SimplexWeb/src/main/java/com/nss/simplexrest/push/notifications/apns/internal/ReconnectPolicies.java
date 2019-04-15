package com.nss.simplexrest.push.notifications.apns.internal;

import com.nss.simplexrest.push.notifications.apns.ReconnectPolicy;

public final class ReconnectPolicies {

    public static class Never implements ReconnectPolicy {

        public boolean shouldReconnect() { return false; }
        public void reconnected() { }
        public Never copy() { return this; }
    }

    public static class Always implements ReconnectPolicy {
        public boolean shouldReconnect() { return true; }
        public void reconnected() { }
        public Always copy() { return this; }
    }

    public static class EveryHalfHour implements ReconnectPolicy {
        private static final long PERIOD = 30 * 60 * 1000;

        private long lastRunning = System.currentTimeMillis();

        public boolean shouldReconnect() {
            return System.currentTimeMillis() - lastRunning > PERIOD;
        }

        public void reconnected() {
            lastRunning = System.currentTimeMillis();
        }

        public EveryHalfHour copy() {
            return new EveryHalfHour();
        }
    }
}
