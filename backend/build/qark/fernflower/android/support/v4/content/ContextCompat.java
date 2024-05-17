package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import java.io.File;

public class ContextCompat {
   private static final String DIR_ANDROID = "Android";
   private static final String DIR_CACHE = "cache";
   private static final String DIR_DATA = "data";
   private static final String DIR_FILES = "files";
   private static final String DIR_OBB = "obb";

   private static File buildPath(File var0, String... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var4 = var1[var2];
         if (var0 == null) {
            var0 = new File(var4);
         } else if (var4 != null) {
            var0 = new File(var0, var4);
         }
      }

      return var0;
   }

   public static File[] getExternalCacheDirs(Context var0) {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 19) {
         return ContextCompatKitKat.getExternalCacheDirs(var0);
      } else {
         File var2;
         if (var1 >= 8) {
            var2 = ContextCompatFroyo.getExternalCacheDir(var0);
         } else {
            var2 = buildPath(Environment.getExternalStorageDirectory(), "Android", "data", var0.getPackageName(), "cache");
         }

         return new File[]{var2};
      }
   }

   public static File[] getExternalFilesDirs(Context var0, String var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 >= 19) {
         return ContextCompatKitKat.getExternalFilesDirs(var0, var1);
      } else {
         File var3;
         if (var2 >= 8) {
            var3 = ContextCompatFroyo.getExternalFilesDir(var0, var1);
         } else {
            var3 = buildPath(Environment.getExternalStorageDirectory(), "Android", "data", var0.getPackageName(), "files", var1);
         }

         return new File[]{var3};
      }
   }

   public static File[] getObbDirs(Context var0) {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 19) {
         return ContextCompatKitKat.getObbDirs(var0);
      } else {
         File var2;
         if (var1 >= 11) {
            var2 = ContextCompatHoneycomb.getObbDir(var0);
         } else {
            var2 = buildPath(Environment.getExternalStorageDirectory(), "Android", "obb", var0.getPackageName());
         }

         return new File[]{var2};
      }
   }

   public static boolean startActivities(Context var0, Intent[] var1) {
      return startActivities(var0, var1, (Bundle)null);
   }

   public static boolean startActivities(Context var0, Intent[] var1, Bundle var2) {
      int var3 = VERSION.SDK_INT;
      if (var3 >= 16) {
         ContextCompatJellybean.startActivities(var0, var1, var2);
         return true;
      } else if (var3 >= 11) {
         ContextCompatHoneycomb.startActivities(var0, var1);
         return true;
      } else {
         return false;
      }
   }
}
