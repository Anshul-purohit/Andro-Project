/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 */
package android.support.v4.util;

public class DebugUtils {
    public static void buildShortClassTag(Object object, StringBuilder stringBuilder) {
        String string2;
        block6 : {
            String string3;
            block5 : {
                if (object == null) {
                    stringBuilder.append("null");
                    return;
                }
                string3 = object.getClass().getSimpleName();
                if (string3 == null) break block5;
                string2 = string3;
                if (string3.length() > 0) break block6;
            }
            string3 = object.getClass().getName();
            int n = string3.lastIndexOf(46);
            string2 = string3;
            if (n > 0) {
                string2 = string3.substring(n + 1);
            }
        }
        stringBuilder.append(string2);
        stringBuilder.append('{');
        stringBuilder.append(Integer.toHexString((int)System.identityHashCode((Object)object)));
    }
}

