/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.io.Writer
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package android.support.v4.util;

import android.util.Log;
import java.io.Writer;

public class LogWriter
extends Writer {
    private StringBuilder mBuilder = new StringBuilder(128);
    private final String mTag;

    public LogWriter(String string2) {
        this.mTag = string2;
    }

    private void flushBuilder() {
        if (this.mBuilder.length() > 0) {
            Log.d((String)this.mTag, (String)this.mBuilder.toString());
            this.mBuilder.delete(0, this.mBuilder.length());
        }
    }

    public void close() {
        this.flushBuilder();
    }

    public void flush() {
        this.flushBuilder();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void write(char[] arrc, int n, int n2) {
        int n3 = 0;
        while (n3 < n2) {
            char c = arrc[n + n3];
            if (c == '\n') {
                this.flushBuilder();
            } else {
                this.mBuilder.append(c);
            }
            ++n3;
        }
    }
}

