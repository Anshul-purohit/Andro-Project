/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.text.Html
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package android.support.v4.app;

import android.text.Html;

class ShareCompatJB {
    ShareCompatJB() {
    }

    public static String escapeHtml(CharSequence charSequence) {
        return Html.escapeHtml((CharSequence)charSequence);
    }
}

