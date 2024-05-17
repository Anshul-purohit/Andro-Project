/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  java.lang.Object
 *  java.lang.ThreadLocal
 *  java.net.Socket
 *  java.net.SocketException
 */
package android.support.v4.net;

import android.os.Build;
import android.support.v4.net.TrafficStatsCompatIcs;
import java.net.Socket;
import java.net.SocketException;

public class TrafficStatsCompat {
    private static final TrafficStatsCompatImpl IMPL = Build.VERSION.SDK_INT >= 14 ? new IcsTrafficStatsCompatImpl() : new BaseTrafficStatsCompatImpl();

    public static void clearThreadStatsTag() {
        IMPL.clearThreadStatsTag();
    }

    public static int getThreadStatsTag() {
        return IMPL.getThreadStatsTag();
    }

    public static void incrementOperationCount(int n) {
        IMPL.incrementOperationCount(n);
    }

    public static void incrementOperationCount(int n, int n2) {
        IMPL.incrementOperationCount(n, n2);
    }

    public static void setThreadStatsTag(int n) {
        IMPL.setThreadStatsTag(n);
    }

    public static void tagSocket(Socket socket) throws SocketException {
        IMPL.tagSocket(socket);
    }

    public static void untagSocket(Socket socket) throws SocketException {
        IMPL.untagSocket(socket);
    }

    static class BaseTrafficStatsCompatImpl
    implements TrafficStatsCompatImpl {
        private ThreadLocal<SocketTags> mThreadSocketTags;

        BaseTrafficStatsCompatImpl() {
            this.mThreadSocketTags = new ThreadLocal<SocketTags>(){

                protected SocketTags initialValue() {
                    return new SocketTags();
                }
            };
        }

        @Override
        public void clearThreadStatsTag() {
            ((SocketTags)this.mThreadSocketTags.get()).statsTag = -1;
        }

        @Override
        public int getThreadStatsTag() {
            return ((SocketTags)this.mThreadSocketTags.get()).statsTag;
        }

        @Override
        public void incrementOperationCount(int n) {
        }

        @Override
        public void incrementOperationCount(int n, int n2) {
        }

        @Override
        public void setThreadStatsTag(int n) {
            ((SocketTags)this.mThreadSocketTags.get()).statsTag = n;
        }

        @Override
        public void tagSocket(Socket socket) {
        }

        @Override
        public void untagSocket(Socket socket) {
        }

        private static class SocketTags {
            public int statsTag = -1;

            private SocketTags() {
            }
        }

    }

    static class IcsTrafficStatsCompatImpl
    implements TrafficStatsCompatImpl {
        IcsTrafficStatsCompatImpl() {
        }

        @Override
        public void clearThreadStatsTag() {
            TrafficStatsCompatIcs.clearThreadStatsTag();
        }

        @Override
        public int getThreadStatsTag() {
            return TrafficStatsCompatIcs.getThreadStatsTag();
        }

        @Override
        public void incrementOperationCount(int n) {
            TrafficStatsCompatIcs.incrementOperationCount(n);
        }

        @Override
        public void incrementOperationCount(int n, int n2) {
            TrafficStatsCompatIcs.incrementOperationCount(n, n2);
        }

        @Override
        public void setThreadStatsTag(int n) {
            TrafficStatsCompatIcs.setThreadStatsTag(n);
        }

        @Override
        public void tagSocket(Socket socket) throws SocketException {
            TrafficStatsCompatIcs.tagSocket(socket);
        }

        @Override
        public void untagSocket(Socket socket) throws SocketException {
            TrafficStatsCompatIcs.untagSocket(socket);
        }
    }

    static interface TrafficStatsCompatImpl {
        public void clearThreadStatsTag();

        public int getThreadStatsTag();

        public void incrementOperationCount(int var1);

        public void incrementOperationCount(int var1, int var2);

        public void setThreadStatsTag(int var1);

        public void tagSocket(Socket var1) throws SocketException;

        public void untagSocket(Socket var1) throws SocketException;
    }

}

