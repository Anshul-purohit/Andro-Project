package android.support.v4.view;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public class ViewCompat {
   public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
   public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
   public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
   private static final long FAKE_FRAME_TIME = 10L;
   static final ViewCompat.ViewCompatImpl IMPL;
   public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
   public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
   public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
   public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
   public static final int LAYER_TYPE_HARDWARE = 2;
   public static final int LAYER_TYPE_NONE = 0;
   public static final int LAYER_TYPE_SOFTWARE = 1;
   public static final int LAYOUT_DIRECTION_INHERIT = 2;
   public static final int LAYOUT_DIRECTION_LOCALE = 3;
   public static final int LAYOUT_DIRECTION_LTR = 0;
   public static final int LAYOUT_DIRECTION_RTL = 1;
   public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
   public static final int MEASURED_SIZE_MASK = 16777215;
   public static final int MEASURED_STATE_MASK = -16777216;
   public static final int MEASURED_STATE_TOO_SMALL = 16777216;
   public static final int OVER_SCROLL_ALWAYS = 0;
   public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
   public static final int OVER_SCROLL_NEVER = 2;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 19) {
         IMPL = new ViewCompat.KitKatViewCompatImpl();
      } else if (var0 >= 17) {
         IMPL = new ViewCompat.JbMr1ViewCompatImpl();
      } else if (var0 >= 16) {
         IMPL = new ViewCompat.JBViewCompatImpl();
      } else if (var0 >= 14) {
         IMPL = new ViewCompat.ICSViewCompatImpl();
      } else if (var0 >= 11) {
         IMPL = new ViewCompat.HCViewCompatImpl();
      } else if (var0 >= 9) {
         IMPL = new ViewCompat.GBViewCompatImpl();
      } else {
         IMPL = new ViewCompat.BaseViewCompatImpl();
      }
   }

   public static boolean canScrollHorizontally(View var0, int var1) {
      return IMPL.canScrollHorizontally(var0, var1);
   }

   public static boolean canScrollVertically(View var0, int var1) {
      return IMPL.canScrollVertically(var0, var1);
   }

   public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var0) {
      return IMPL.getAccessibilityNodeProvider(var0);
   }

   public static float getAlpha(View var0) {
      return IMPL.getAlpha(var0);
   }

   public static int getImportantForAccessibility(View var0) {
      return IMPL.getImportantForAccessibility(var0);
   }

   public static int getLabelFor(View var0) {
      return IMPL.getLabelFor(var0);
   }

   public static int getLayerType(View var0) {
      return IMPL.getLayerType(var0);
   }

   public static int getLayoutDirection(View var0) {
      return IMPL.getLayoutDirection(var0);
   }

   public static int getMeasuredHeightAndState(View var0) {
      return IMPL.getMeasuredHeightAndState(var0);
   }

   public static int getMeasuredState(View var0) {
      return IMPL.getMeasuredState(var0);
   }

   public static int getMeasuredWidthAndState(View var0) {
      return IMPL.getMeasuredWidthAndState(var0);
   }

   public static int getOverScrollMode(View var0) {
      return IMPL.getOverScrollMode(var0);
   }

   public static ViewParent getParentForAccessibility(View var0) {
      return IMPL.getParentForAccessibility(var0);
   }

   public static boolean hasTransientState(View var0) {
      return IMPL.hasTransientState(var0);
   }

   public static boolean isOpaque(View var0) {
      return IMPL.isOpaque(var0);
   }

   public static void onInitializeAccessibilityEvent(View var0, AccessibilityEvent var1) {
      IMPL.onInitializeAccessibilityEvent(var0, var1);
   }

   public static void onInitializeAccessibilityNodeInfo(View var0, AccessibilityNodeInfoCompat var1) {
      IMPL.onInitializeAccessibilityNodeInfo(var0, var1);
   }

   public static void onPopulateAccessibilityEvent(View var0, AccessibilityEvent var1) {
      IMPL.onPopulateAccessibilityEvent(var0, var1);
   }

   public static boolean performAccessibilityAction(View var0, int var1, Bundle var2) {
      return IMPL.performAccessibilityAction(var0, var1, var2);
   }

   public static void postInvalidateOnAnimation(View var0) {
      IMPL.postInvalidateOnAnimation(var0);
   }

   public static void postInvalidateOnAnimation(View var0, int var1, int var2, int var3, int var4) {
      IMPL.postInvalidateOnAnimation(var0, var1, var2, var3, var4);
   }

   public static void postOnAnimation(View var0, Runnable var1) {
      IMPL.postOnAnimation(var0, var1);
   }

   public static void postOnAnimationDelayed(View var0, Runnable var1, long var2) {
      IMPL.postOnAnimationDelayed(var0, var1, var2);
   }

   public static int resolveSizeAndState(int var0, int var1, int var2) {
      return IMPL.resolveSizeAndState(var0, var1, var2);
   }

   public static void setAccessibilityDelegate(View var0, AccessibilityDelegateCompat var1) {
      IMPL.setAccessibilityDelegate(var0, var1);
   }

   public static void setHasTransientState(View var0, boolean var1) {
      IMPL.setHasTransientState(var0, var1);
   }

   public static void setImportantForAccessibility(View var0, int var1) {
      IMPL.setImportantForAccessibility(var0, var1);
   }

   public static void setLabelFor(View var0, int var1) {
      IMPL.setLabelFor(var0, var1);
   }

   public static void setLayerPaint(View var0, Paint var1) {
      IMPL.setLayerPaint(var0, var1);
   }

   public static void setLayerType(View var0, int var1, Paint var2) {
      IMPL.setLayerType(var0, var1, var2);
   }

   public static void setLayoutDirection(View var0, int var1) {
      IMPL.setLayoutDirection(var0, var1);
   }

   public static void setOverScrollMode(View var0, int var1) {
      IMPL.setOverScrollMode(var0, var1);
   }

   public int getAccessibilityLiveRegion(View var1) {
      return IMPL.getAccessibilityLiveRegion(var1);
   }

   public void setAccessibilityLiveRegion(View var1, int var2) {
      IMPL.setAccessibilityLiveRegion(var1, var2);
   }

   static class BaseViewCompatImpl implements ViewCompat.ViewCompatImpl {
      public boolean canScrollHorizontally(View var1, int var2) {
         return false;
      }

      public boolean canScrollVertically(View var1, int var2) {
         return false;
      }

      public int getAccessibilityLiveRegion(View var1) {
         return 0;
      }

      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
         return null;
      }

      public float getAlpha(View var1) {
         return 1.0F;
      }

      long getFrameTime() {
         return 10L;
      }

      public int getImportantForAccessibility(View var1) {
         return 0;
      }

      public int getLabelFor(View var1) {
         return 0;
      }

      public int getLayerType(View var1) {
         return 0;
      }

      public int getLayoutDirection(View var1) {
         return 0;
      }

      public int getMeasuredHeightAndState(View var1) {
         return var1.getMeasuredHeight();
      }

      public int getMeasuredState(View var1) {
         return 0;
      }

      public int getMeasuredWidthAndState(View var1) {
         return var1.getMeasuredWidth();
      }

      public int getOverScrollMode(View var1) {
         return 2;
      }

      public ViewParent getParentForAccessibility(View var1) {
         return var1.getParent();
      }

      public boolean hasTransientState(View var1) {
         return false;
      }

      public boolean isOpaque(View var1) {
         boolean var3 = false;
         Drawable var4 = var1.getBackground();
         boolean var2 = var3;
         if (var4 != null) {
            var2 = var3;
            if (var4.getOpacity() == -1) {
               var2 = true;
            }
         }

         return var2;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         return false;
      }

      public void postInvalidateOnAnimation(View var1) {
         var1.postInvalidateDelayed(this.getFrameTime());
      }

      public void postInvalidateOnAnimation(View var1, int var2, int var3, int var4, int var5) {
         var1.postInvalidateDelayed(this.getFrameTime(), var2, var3, var4, var5);
      }

      public void postOnAnimation(View var1, Runnable var2) {
         var1.postDelayed(var2, this.getFrameTime());
      }

      public void postOnAnimationDelayed(View var1, Runnable var2, long var3) {
         var1.postDelayed(var2, this.getFrameTime() + var3);
      }

      public int resolveSizeAndState(int var1, int var2, int var3) {
         return View.resolveSize(var1, var2);
      }

      public void setAccessibilityDelegate(View var1, AccessibilityDelegateCompat var2) {
      }

      public void setAccessibilityLiveRegion(View var1, int var2) {
      }

      public void setHasTransientState(View var1, boolean var2) {
      }

      public void setImportantForAccessibility(View var1, int var2) {
      }

      public void setLabelFor(View var1, int var2) {
      }

      public void setLayerPaint(View var1, Paint var2) {
      }

      public void setLayerType(View var1, int var2, Paint var3) {
      }

      public void setLayoutDirection(View var1, int var2) {
      }

      public void setOverScrollMode(View var1, int var2) {
      }
   }

   static class EclairMr1ViewCompatImpl extends ViewCompat.BaseViewCompatImpl {
      public boolean isOpaque(View var1) {
         return ViewCompatEclairMr1.isOpaque(var1);
      }
   }

   static class GBViewCompatImpl extends ViewCompat.EclairMr1ViewCompatImpl {
      public int getOverScrollMode(View var1) {
         return ViewCompatGingerbread.getOverScrollMode(var1);
      }

      public void setOverScrollMode(View var1, int var2) {
         ViewCompatGingerbread.setOverScrollMode(var1, var2);
      }
   }

   static class HCViewCompatImpl extends ViewCompat.GBViewCompatImpl {
      public float getAlpha(View var1) {
         return ViewCompatHC.getAlpha(var1);
      }

      long getFrameTime() {
         return ViewCompatHC.getFrameTime();
      }

      public int getLayerType(View var1) {
         return ViewCompatHC.getLayerType(var1);
      }

      public int getMeasuredHeightAndState(View var1) {
         return ViewCompatHC.getMeasuredHeightAndState(var1);
      }

      public int getMeasuredState(View var1) {
         return ViewCompatHC.getMeasuredState(var1);
      }

      public int getMeasuredWidthAndState(View var1) {
         return ViewCompatHC.getMeasuredWidthAndState(var1);
      }

      public int resolveSizeAndState(int var1, int var2, int var3) {
         return ViewCompatHC.resolveSizeAndState(var1, var2, var3);
      }

      public void setLayerPaint(View var1, Paint var2) {
         this.setLayerType(var1, this.getLayerType(var1), var2);
         var1.invalidate();
      }

      public void setLayerType(View var1, int var2, Paint var3) {
         ViewCompatHC.setLayerType(var1, var2, var3);
      }
   }

   static class ICSViewCompatImpl extends ViewCompat.HCViewCompatImpl {
      public boolean canScrollHorizontally(View var1, int var2) {
         return ViewCompatICS.canScrollHorizontally(var1, var2);
      }

      public boolean canScrollVertically(View var1, int var2) {
         return ViewCompatICS.canScrollVertically(var1, var2);
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         ViewCompatICS.onInitializeAccessibilityEvent(var1, var2);
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         ViewCompatICS.onInitializeAccessibilityNodeInfo(var1, var2.getInfo());
      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         ViewCompatICS.onPopulateAccessibilityEvent(var1, var2);
      }

      public void setAccessibilityDelegate(View var1, AccessibilityDelegateCompat var2) {
         ViewCompatICS.setAccessibilityDelegate(var1, var2.getBridge());
      }
   }

   static class JBViewCompatImpl extends ViewCompat.ICSViewCompatImpl {
      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
         Object var2 = ViewCompatJB.getAccessibilityNodeProvider(var1);
         return var2 != null ? new AccessibilityNodeProviderCompat(var2) : null;
      }

      public int getImportantForAccessibility(View var1) {
         return ViewCompatJB.getImportantForAccessibility(var1);
      }

      public ViewParent getParentForAccessibility(View var1) {
         return ViewCompatJB.getParentForAccessibility(var1);
      }

      public boolean hasTransientState(View var1) {
         return ViewCompatJB.hasTransientState(var1);
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         return ViewCompatJB.performAccessibilityAction(var1, var2, var3);
      }

      public void postInvalidateOnAnimation(View var1) {
         ViewCompatJB.postInvalidateOnAnimation(var1);
      }

      public void postInvalidateOnAnimation(View var1, int var2, int var3, int var4, int var5) {
         ViewCompatJB.postInvalidateOnAnimation(var1, var2, var3, var4, var5);
      }

      public void postOnAnimation(View var1, Runnable var2) {
         ViewCompatJB.postOnAnimation(var1, var2);
      }

      public void postOnAnimationDelayed(View var1, Runnable var2, long var3) {
         ViewCompatJB.postOnAnimationDelayed(var1, var2, var3);
      }

      public void setHasTransientState(View var1, boolean var2) {
         ViewCompatJB.setHasTransientState(var1, var2);
      }

      public void setImportantForAccessibility(View var1, int var2) {
         ViewCompatJB.setImportantForAccessibility(var1, var2);
      }
   }

   static class JbMr1ViewCompatImpl extends ViewCompat.JBViewCompatImpl {
      public int getLabelFor(View var1) {
         return ViewCompatJellybeanMr1.getLabelFor(var1);
      }

      public int getLayoutDirection(View var1) {
         return ViewCompatJellybeanMr1.getLayoutDirection(var1);
      }

      public void setLabelFor(View var1, int var2) {
         ViewCompatJellybeanMr1.setLabelFor(var1, var2);
      }

      public void setLayerPaint(View var1, Paint var2) {
         ViewCompatJellybeanMr1.setLayerPaint(var1, var2);
      }

      public void setLayoutDirection(View var1, int var2) {
         ViewCompatJellybeanMr1.setLayoutDirection(var1, var2);
      }
   }

   static class KitKatViewCompatImpl extends ViewCompat.JbMr1ViewCompatImpl {
      public int getAccessibilityLiveRegion(View var1) {
         return ViewCompatKitKat.getAccessibilityLiveRegion(var1);
      }

      public void setAccessibilityLiveRegion(View var1, int var2) {
         ViewCompatKitKat.setAccessibilityLiveRegion(var1, var2);
      }
   }

   interface ViewCompatImpl {
      boolean canScrollHorizontally(View var1, int var2);

      boolean canScrollVertically(View var1, int var2);

      int getAccessibilityLiveRegion(View var1);

      AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1);

      float getAlpha(View var1);

      int getImportantForAccessibility(View var1);

      int getLabelFor(View var1);

      int getLayerType(View var1);

      int getLayoutDirection(View var1);

      int getMeasuredHeightAndState(View var1);

      int getMeasuredState(View var1);

      int getMeasuredWidthAndState(View var1);

      int getOverScrollMode(View var1);

      ViewParent getParentForAccessibility(View var1);

      boolean hasTransientState(View var1);

      boolean isOpaque(View var1);

      void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2);

      void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2);

      void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2);

      boolean performAccessibilityAction(View var1, int var2, Bundle var3);

      void postInvalidateOnAnimation(View var1);

      void postInvalidateOnAnimation(View var1, int var2, int var3, int var4, int var5);

      void postOnAnimation(View var1, Runnable var2);

      void postOnAnimationDelayed(View var1, Runnable var2, long var3);

      int resolveSizeAndState(int var1, int var2, int var3);

      void setAccessibilityDelegate(View var1, AccessibilityDelegateCompat var2);

      void setAccessibilityLiveRegion(View var1, int var2);

      void setHasTransientState(View var1, boolean var2);

      void setImportantForAccessibility(View var1, int var2);

      void setLabelFor(View var1, int var2);

      void setLayerPaint(View var1, Paint var2);

      void setLayerType(View var1, int var2, Paint var3);

      void setLayoutDirection(View var1, int var2);

      void setOverScrollMode(View var1, int var2);
   }
}
