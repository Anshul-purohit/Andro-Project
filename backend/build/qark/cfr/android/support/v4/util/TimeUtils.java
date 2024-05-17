/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  java.io.PrintWriter
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;

    static {
        sFormatSync = new Object();
        sFormatStr = new char[24];
    }

    private static int accumField(int n, int n2, boolean bl, int n3) {
        if (n > 99 || bl && n3 >= 3) {
            return n2 + 3;
        }
        if (n > 9 || bl && n3 >= 2) {
            return n2 + 2;
        }
        if (bl || n > 0) {
            return n2 + 1;
        }
        return 0;
    }

    public static void formatDuration(long l, long l2, PrintWriter printWriter) {
        if (l == 0L) {
            printWriter.print("--");
            return;
        }
        TimeUtils.formatDuration(l - l2, printWriter, 0);
    }

    public static void formatDuration(long l, PrintWriter printWriter) {
        TimeUtils.formatDuration(l, printWriter, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void formatDuration(long l, PrintWriter printWriter, int n) {
        Object object = sFormatSync;
        synchronized (object) {
            n = TimeUtils.formatDurationLocked(l, n);
            printWriter.print(new String(sFormatStr, 0, n));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void formatDuration(long l, StringBuilder stringBuilder) {
        Object object = sFormatSync;
        synchronized (object) {
            int n = TimeUtils.formatDurationLocked(l, 0);
            stringBuilder.append(sFormatStr, 0, n);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int formatDurationLocked(long l, int n) {
        boolean bl;
        int n2;
        if (sFormatStr.length < n) {
            sFormatStr = new char[n];
        }
        char[] arrc = sFormatStr;
        if (l == 0L) {
            do {
                if (n - 1 >= 0) {
                    arrc[0] = 48;
                    return 1;
                }
                arrc[0] = 32;
            } while (true);
        }
        if (l > 0L) {
            n2 = 43;
        } else {
            n2 = 45;
            l = - l;
        }
        int n3 = (int)(l % 1000L);
        int n4 = (int)Math.floor((double)(l / 1000L));
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = n4;
        if (n4 > 86400) {
            n5 = n4 / 86400;
            n8 = n4 - 86400 * n5;
        }
        n4 = n8;
        if (n8 > 3600) {
            n6 = n8 / 3600;
            n4 = n8 - n6 * 3600;
        }
        int n9 = n4;
        if (n4 > 60) {
            n7 = n4 / 60;
            n9 = n4 - n7 * 60;
        }
        int n10 = 0;
        int n11 = 0;
        if (n != 0) {
            n8 = TimeUtils.accumField(n5, 1, false, 0);
            bl = n8 > 0;
            bl = (n8 += TimeUtils.accumField(n6, 1, bl, 2)) > 0;
            bl = (n8 += TimeUtils.accumField(n7, 1, bl, 2)) > 0;
            n4 = n8 + TimeUtils.accumField(n9, 1, bl, 2);
            n8 = n4 > 0 ? 3 : 0;
            n4 += TimeUtils.accumField(n3, 2, true, n8) + 1;
            n8 = n11;
            do {
                n10 = n8++;
                if (n4 >= n) break;
                arrc[n8] = 32;
                ++n4;
            } while (true);
        }
        arrc[n10] = n2;
        n4 = n10 + 1;
        n = n != 0 ? 1 : 0;
        bl = (n5 = TimeUtils.printField(arrc, n5, 'd', n4, false, 0)) != n4;
        n8 = n != 0 ? 2 : 0;
        bl = (n5 = TimeUtils.printField(arrc, n6, 'h', n5, bl, n8)) != n4;
        n8 = n != 0 ? 2 : 0;
        bl = (n5 = TimeUtils.printField(arrc, n7, 'm', n5, bl, n8)) != n4;
        n8 = n != 0 ? 2 : 0;
        n8 = TimeUtils.printField(arrc, n9, 's', n5, bl, n8);
        n = n != 0 && n8 != n4 ? 3 : 0;
        n = TimeUtils.printField(arrc, n3, 'm', n8, true, n);
        arrc[n] = 115;
        return n + 1;
    }

    private static int printField(char[] arrc, int n, char c, int n2, boolean bl, int n3) {
        int n4;
        block5 : {
            block9 : {
                int n5;
                block8 : {
                    block7 : {
                        block6 : {
                            block4 : {
                                if (bl) break block4;
                                n4 = n2;
                                if (n <= 0) break block5;
                            }
                            if (bl && n3 >= 3) break block6;
                            n4 = n;
                            n5 = n2;
                            if (n <= 99) break block7;
                        }
                        n4 = n / 100;
                        arrc[n2] = (char)(n4 + 48);
                        n5 = n2 + 1;
                        n4 = n - n4 * 100;
                    }
                    if (bl && n3 >= 2 || n4 > 9) break block8;
                    n3 = n4;
                    n = n5;
                    if (n2 == n5) break block9;
                }
                n2 = n4 / 10;
                arrc[n5] = (char)(n2 + 48);
                n = n5 + 1;
                n3 = n4 - n2 * 10;
            }
            arrc[n] = (char)(n3 + 48);
            arrc[++n] = c;
            n4 = n + 1;
        }
        return n4;
    }
}

