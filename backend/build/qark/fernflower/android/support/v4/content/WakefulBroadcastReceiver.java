package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
   private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
   private static final SparseArray mActiveWakeLocks = new SparseArray();
   private static int mNextId = 1;

   public static boolean completeWakefulIntent(Intent var0) {
      int var1 = var0.getIntExtra("android.support.content.wakelockid", 0);
      if (var1 == 0) {
         return false;
      } else {
         SparseArray var23 = mActiveWakeLocks;
         synchronized(var23){}

         Throwable var10000;
         boolean var10001;
         label182: {
            WakeLock var2;
            try {
               var2 = (WakeLock)mActiveWakeLocks.get(var1);
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label182;
            }

            if (var2 != null) {
               label176:
               try {
                  var2.release();
                  mActiveWakeLocks.remove(var1);
                  return true;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label176;
               }
            } else {
               label178:
               try {
                  Log.w("WakefulBroadcastReceiver", "No active wake lock id #" + var1);
                  return true;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label178;
               }
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static ComponentName startWakefulService(Context var0, Intent var1) {
      SparseArray var3 = mActiveWakeLocks;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label260: {
         int var2;
         try {
            var2 = mNextId++;
            if (mNextId <= 0) {
               mNextId = 1;
            }
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label260;
         }

         ComponentName var36;
         try {
            var1.putExtra("android.support.content.wakelockid", var2);
            var36 = var0.startService(var1);
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label260;
         }

         if (var36 == null) {
            label247:
            try {
               return null;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label247;
            }
         } else {
            label249:
            try {
               WakeLock var35 = ((PowerManager)var0.getSystemService("power")).newWakeLock(1, "wake:" + var36.flattenToShortString());
               var35.setReferenceCounted(false);
               var35.acquire(60000L);
               mActiveWakeLocks.put(var2, var35);
               return var36;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label249;
            }
         }
      }

      while(true) {
         Throwable var34 = var10000;

         try {
            throw var34;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            continue;
         }
      }
   }
}
