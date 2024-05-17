/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  java.io.File
 *  java.lang.Object
 *  java.lang.String
 */
package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompatFroyo;
import android.support.v4.content.ContextCompatHoneycomb;
import android.support.v4.content.ContextCompatJellybean;
import android.support.v4.content.ContextCompatKitKat;
import java.io.File;

public class ContextCompat {
    private static final String DIR_ANDROID = "Android";
    private static final String DIR_CACHE = "cache";
    private static final String DIR_DATA = "data";
    private static final String DIR_FILES = "files";
    private static final String DIR_OBB = "obb";

    /*
     * Enabled aggressive block sorting
     */
    private static /* varargs */ File buildPath(File file, String ... arrstring) {
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String string2 = arrstring[n2];
            if (file == null) {
                file = new File(string2);
            } else if (string2 != null) {
                file = new File(file, string2);
            }
            ++n2;
        }
        return file;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static File[] getExternalCacheDirs(Context context) {
        int n = Build.VERSION.SDK_INT;
        if (n >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context);
        }
        if (n >= 8) {
            context = ContextCompatFroyo.getExternalCacheDir(context);
            do {
                return new File[]{context};
                break;
            } while (true);
        }
        context = ContextCompat.buildPath(Environment.getExternalStorageDirectory(), "Android", "data", context.getPackageName(), "cache");
        return new File[]{context};
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static File[] getExternalFilesDirs(Context context, String string2) {
        int n = Build.VERSION.SDK_INT;
        if (n >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context, string2);
        }
        if (n >= 8) {
            context = ContextCompatFroyo.getExternalFilesDir(context, string2);
            do {
                return new File[]{context};
                break;
            } while (true);
        }
        context = ContextCompat.buildPath(Environment.getExternalStorageDirectory(), "Android", "data", context.getPackageName(), "files", string2);
        return new File[]{context};
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static File[] getObbDirs(Context context) {
        int n = Build.VERSION.SDK_INT;
        if (n >= 19) {
            return ContextCompatKitKat.getObbDirs(context);
        }
        if (n >= 11) {
            context = ContextCompatHoneycomb.getObbDir(context);
            do {
                return new File[]{context};
                break;
            } while (true);
        }
        context = ContextCompat.buildPath(Environment.getExternalStorageDirectory(), "Android", "obb", context.getPackageName());
        return new File[]{context};
    }

    public static boolean startActivities(Context context, Intent[] arrintent) {
        return ContextCompat.startActivities(context, arrintent, null);
    }

    public static boolean startActivities(Context context, Intent[] arrintent, Bundle bundle) {
        int n = Build.VERSION.SDK_INT;
        if (n >= 16) {
            ContextCompatJellybean.startActivities(context, arrintent, bundle);
            return true;
        }
        if (n >= 11) {
            ContextCompatHoneycomb.startActivities(context, arrintent);
            return true;
        }
        return false;
    }
}

