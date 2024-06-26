/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.util.Log
 *  android.util.SparseArray
 *  java.lang.Object
 *  java.lang.String
 */
package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver
extends BroadcastReceiver {
    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
    private static final SparseArray<PowerManager.WakeLock> mActiveWakeLocks = new SparseArray();
    private static int mNextId = 1;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean completeWakefulIntent(Intent sparseArray) {
        int n = sparseArray.getIntExtra("android.support.content.wakelockid", 0);
        if (n == 0) {
            return false;
        }
        sparseArray = mActiveWakeLocks;
        synchronized (sparseArray) {
            PowerManager.WakeLock wakeLock = (PowerManager.WakeLock)mActiveWakeLocks.get(n);
            if (wakeLock != null) {
                wakeLock.release();
                mActiveWakeLocks.remove(n);
                return true;
            }
            Log.w((String)"WakefulBroadcastReceiver", (String)("No active wake lock id #" + n));
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ComponentName startWakefulService(Context context, Intent intent) {
        SparseArray<PowerManager.WakeLock> sparseArray = mActiveWakeLocks;
        synchronized (sparseArray) {
            int n = mNextId++;
            if (mNextId <= 0) {
                mNextId = 1;
            }
            intent.putExtra("android.support.content.wakelockid", n);
            intent = context.startService(intent);
            if (intent == null) {
                return null;
            }
            context = ((PowerManager)context.getSystemService("power")).newWakeLock(1, "wake:" + intent.flattenToShortString());
            context.setReferenceCounted(false);
            context.acquire(60000L);
            mActiveWakeLocks.put(n, (Object)context);
            return intent;
        }
    }
}

