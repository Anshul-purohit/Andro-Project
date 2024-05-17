package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.View;
import android.view.KeyEvent.Callback;

public class KeyEventCompat {
   static final KeyEventCompat.KeyEventVersionImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 11) {
         IMPL = new KeyEventCompat.HoneycombKeyEventVersionImpl();
      } else {
         IMPL = new KeyEventCompat.BaseKeyEventVersionImpl();
      }
   }

   public static boolean dispatch(KeyEvent var0, Callback var1, Object var2, Object var3) {
      return IMPL.dispatch(var0, var1, var2, var3);
   }

   public static Object getKeyDispatcherState(View var0) {
      return IMPL.getKeyDispatcherState(var0);
   }

   public static boolean hasModifiers(KeyEvent var0, int var1) {
      return IMPL.metaStateHasModifiers(var0.getMetaState(), var1);
   }

   public static boolean hasNoModifiers(KeyEvent var0) {
      return IMPL.metaStateHasNoModifiers(var0.getMetaState());
   }

   public static boolean isTracking(KeyEvent var0) {
      return IMPL.isTracking(var0);
   }

   public static boolean metaStateHasModifiers(int var0, int var1) {
      return IMPL.metaStateHasModifiers(var0, var1);
   }

   public static boolean metaStateHasNoModifiers(int var0) {
      return IMPL.metaStateHasNoModifiers(var0);
   }

   public static int normalizeMetaState(int var0) {
      return IMPL.normalizeMetaState(var0);
   }

   public static void startTracking(KeyEvent var0) {
      IMPL.startTracking(var0);
   }

   static class BaseKeyEventVersionImpl implements KeyEventCompat.KeyEventVersionImpl {
      private static final int META_ALL_MASK = 247;
      private static final int META_MODIFIER_MASK = 247;

      private static int metaStateFilterDirectionalModifiers(int var0, int var1, int var2, int var3, int var4) {
         boolean var6 = true;
         boolean var5;
         if ((var1 & var2) != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var3 |= var4;
         boolean var7;
         if ((var1 & var3) != 0) {
            var7 = var6;
         } else {
            var7 = false;
         }

         if (var5) {
            if (var7) {
               throw new IllegalArgumentException("bad arguments");
            }

            var3 = var0 & ~var3;
         } else {
            var3 = var0;
            if (var7) {
               return var0 & ~var2;
            }
         }

         return var3;
      }

      public boolean dispatch(KeyEvent var1, Callback var2, Object var3, Object var4) {
         return var1.dispatch(var2);
      }

      public Object getKeyDispatcherState(View var1) {
         return null;
      }

      public boolean isTracking(KeyEvent var1) {
         return false;
      }

      public boolean metaStateHasModifiers(int var1, int var2) {
         return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(this.normalizeMetaState(var1) & 247, var2, 1, 64, 128), var2, 2, 16, 32) == var2;
      }

      public boolean metaStateHasNoModifiers(int var1) {
         return (this.normalizeMetaState(var1) & 247) == 0;
      }

      public int normalizeMetaState(int var1) {
         int var2 = var1;
         if ((var1 & 192) != 0) {
            var2 = var1 | 1;
         }

         var1 = var2;
         if ((var2 & 48) != 0) {
            var1 = var2 | 2;
         }

         return var1 & 247;
      }

      public void startTracking(KeyEvent var1) {
      }
   }

   static class EclairKeyEventVersionImpl extends KeyEventCompat.BaseKeyEventVersionImpl {
      public boolean dispatch(KeyEvent var1, Callback var2, Object var3, Object var4) {
         return KeyEventCompatEclair.dispatch(var1, var2, var3, var4);
      }

      public Object getKeyDispatcherState(View var1) {
         return KeyEventCompatEclair.getKeyDispatcherState(var1);
      }

      public boolean isTracking(KeyEvent var1) {
         return KeyEventCompatEclair.isTracking(var1);
      }

      public void startTracking(KeyEvent var1) {
         KeyEventCompatEclair.startTracking(var1);
      }
   }

   static class HoneycombKeyEventVersionImpl extends KeyEventCompat.EclairKeyEventVersionImpl {
      public boolean metaStateHasModifiers(int var1, int var2) {
         return KeyEventCompatHoneycomb.metaStateHasModifiers(var1, var2);
      }

      public boolean metaStateHasNoModifiers(int var1) {
         return KeyEventCompatHoneycomb.metaStateHasNoModifiers(var1);
      }

      public int normalizeMetaState(int var1) {
         return KeyEventCompatHoneycomb.normalizeMetaState(var1);
      }
   }

   interface KeyEventVersionImpl {
      boolean dispatch(KeyEvent var1, Callback var2, Object var3, Object var4);

      Object getKeyDispatcherState(View var1);

      boolean isTracking(KeyEvent var1);

      boolean metaStateHasModifiers(int var1, int var2);

      boolean metaStateHasNoModifiers(int var1);

      int normalizeMetaState(int var1);

      void startTracking(KeyEvent var1);
   }
}
