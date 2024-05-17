package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
   private static final int DEFAULT_FADE_COLOR = -858993460;
   private static final int DEFAULT_OVERHANG_SIZE = 32;
   static final SlidingPaneLayout.SlidingPanelLayoutImpl IMPL;
   private static final int MIN_FLING_VELOCITY = 400;
   private static final String TAG = "SlidingPaneLayout";
   private boolean mCanSlide;
   private int mCoveredFadeColor;
   private final ViewDragHelper mDragHelper;
   private boolean mFirstLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private boolean mIsUnableToDrag;
   private final int mOverhangSize;
   private SlidingPaneLayout.PanelSlideListener mPanelSlideListener;
   private int mParallaxBy;
   private float mParallaxOffset;
   private final ArrayList mPostedRunnables;
   private boolean mPreservedOpenState;
   private Drawable mShadowDrawable;
   private float mSlideOffset;
   private int mSlideRange;
   private View mSlideableView;
   private int mSliderFadeColor;
   private final Rect mTmpRect;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 17) {
         IMPL = new SlidingPaneLayout.SlidingPanelLayoutImplJBMR1();
      } else if (var0 >= 16) {
         IMPL = new SlidingPaneLayout.SlidingPanelLayoutImplJB();
      } else {
         IMPL = new SlidingPaneLayout.SlidingPanelLayoutImplBase();
      }
   }

   public SlidingPaneLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SlidingPaneLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public SlidingPaneLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSliderFadeColor = -858993460;
      this.mFirstLayout = true;
      this.mTmpRect = new Rect();
      this.mPostedRunnables = new ArrayList();
      float var4 = var1.getResources().getDisplayMetrics().density;
      this.mOverhangSize = (int)(32.0F * var4 + 0.5F);
      ViewConfiguration.get(var1);
      this.setWillNotDraw(false);
      ViewCompat.setAccessibilityDelegate(this, new SlidingPaneLayout.AccessibilityDelegate());
      ViewCompat.setImportantForAccessibility(this, 1);
      this.mDragHelper = ViewDragHelper.create(this, 0.5F, new SlidingPaneLayout.DragHelperCallback());
      this.mDragHelper.setEdgeTrackingEnabled(1);
      this.mDragHelper.setMinVelocity(400.0F * var4);
   }

   private boolean closePane(View var1, int var2) {
      boolean var3 = false;
      if (this.mFirstLayout || this.smoothSlideTo(0.0F, var2)) {
         this.mPreservedOpenState = false;
         var3 = true;
      }

      return var3;
   }

   private void dimChildView(View var1, float var2, int var3) {
      SlidingPaneLayout.LayoutParams var5 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
      if (var2 > 0.0F && var3 != 0) {
         int var4 = (int)((float)((-16777216 & var3) >>> 24) * var2);
         if (var5.dimPaint == null) {
            var5.dimPaint = new Paint();
         }

         var5.dimPaint.setColorFilter(new PorterDuffColorFilter(var4 << 24 | 16777215 & var3, Mode.SRC_OVER));
         if (ViewCompat.getLayerType(var1) != 2) {
            ViewCompat.setLayerType(var1, 2, var5.dimPaint);
         }

         this.invalidateChildRegion(var1);
      } else if (ViewCompat.getLayerType(var1) != 0) {
         if (var5.dimPaint != null) {
            var5.dimPaint.setColorFilter((ColorFilter)null);
         }

         SlidingPaneLayout.DisableLayerRunnable var6 = new SlidingPaneLayout.DisableLayerRunnable(var1);
         this.mPostedRunnables.add(var6);
         ViewCompat.postOnAnimation(this, var6);
         return;
      }

   }

   private void invalidateChildRegion(View var1) {
      IMPL.invalidateChildRegion(this, var1);
   }

   private void onPanelDragged(int var1) {
      if (this.mSlideableView == null) {
         this.mSlideOffset = 0.0F;
      } else {
         SlidingPaneLayout.LayoutParams var2 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         this.mSlideOffset = (float)(var1 - (this.getPaddingLeft() + var2.leftMargin)) / (float)this.mSlideRange;
         if (this.mParallaxBy != 0) {
            this.parallaxOtherViews(this.mSlideOffset);
         }

         if (var2.dimWhenOffset) {
            this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
         }

         this.dispatchOnPanelSlide(this.mSlideableView);
      }
   }

   private boolean openPane(View var1, int var2) {
      if (!this.mFirstLayout && !this.smoothSlideTo(1.0F, var2)) {
         return false;
      } else {
         this.mPreservedOpenState = true;
         return true;
      }
   }

   private void parallaxOtherViews(float var1) {
      SlidingPaneLayout.LayoutParams var6 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
      boolean var2;
      if (var6.dimWhenOffset && var6.leftMargin <= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var4 = this.getChildCount();

      for(int var3 = 0; var3 < var4; ++var3) {
         View var7 = this.getChildAt(var3);
         if (var7 != this.mSlideableView) {
            int var5 = (int)((1.0F - this.mParallaxOffset) * (float)this.mParallaxBy);
            this.mParallaxOffset = var1;
            var7.offsetLeftAndRight(var5 - (int)((1.0F - var1) * (float)this.mParallaxBy));
            if (var2) {
               this.dimChildView(var7, 1.0F - this.mParallaxOffset, this.mCoveredFadeColor);
            }
         }
      }

   }

   private static boolean viewIsOpaque(View var0) {
      if (!ViewCompat.isOpaque(var0)) {
         if (VERSION.SDK_INT >= 18) {
            return false;
         }

         Drawable var1 = var0.getBackground();
         if (var1 == null) {
            return false;
         }

         if (var1.getOpacity() != -1) {
            return false;
         }
      }

      return true;
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5) {
      if (var1 instanceof ViewGroup) {
         ViewGroup var9 = (ViewGroup)var1;
         int var7 = var1.getScrollX();
         int var8 = var1.getScrollY();

         for(int var6 = var9.getChildCount() - 1; var6 >= 0; --var6) {
            View var10 = var9.getChildAt(var6);
            if (var4 + var7 >= var10.getLeft() && var4 + var7 < var10.getRight() && var5 + var8 >= var10.getTop() && var5 + var8 < var10.getBottom() && this.canScroll(var10, true, var3, var4 + var7 - var10.getLeft(), var5 + var8 - var10.getTop())) {
               return true;
            }
         }
      }

      return var2 && ViewCompat.canScrollHorizontally(var1, -var3);
   }

   @Deprecated
   public boolean canSlide() {
      return this.mCanSlide;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof SlidingPaneLayout.LayoutParams && super.checkLayoutParams(var1);
   }

   public boolean closePane() {
      return this.closePane(this.mSlideableView, 0);
   }

   public void computeScroll() {
      if (this.mDragHelper.continueSettling(true)) {
         if (this.mCanSlide) {
            ViewCompat.postInvalidateOnAnimation(this);
            return;
         }

         this.mDragHelper.abort();
      }

   }

   void dispatchOnPanelClosed(View var1) {
      if (this.mPanelSlideListener != null) {
         this.mPanelSlideListener.onPanelClosed(var1);
      }

      this.sendAccessibilityEvent(32);
   }

   void dispatchOnPanelOpened(View var1) {
      if (this.mPanelSlideListener != null) {
         this.mPanelSlideListener.onPanelOpened(var1);
      }

      this.sendAccessibilityEvent(32);
   }

   void dispatchOnPanelSlide(View var1) {
      if (this.mPanelSlideListener != null) {
         this.mPanelSlideListener.onPanelSlide(var1, this.mSlideOffset);
      }

   }

   public void draw(Canvas var1) {
      super.draw(var1);
      View var6;
      if (this.getChildCount() > 1) {
         var6 = this.getChildAt(1);
      } else {
         var6 = null;
      }

      if (var6 != null && this.mShadowDrawable != null) {
         int var2 = this.mShadowDrawable.getIntrinsicWidth();
         int var3 = var6.getLeft();
         int var4 = var6.getTop();
         int var5 = var6.getBottom();
         this.mShadowDrawable.setBounds(var3 - var2, var4, var3, var5);
         this.mShadowDrawable.draw(var1);
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      SlidingPaneLayout.LayoutParams var7 = (SlidingPaneLayout.LayoutParams)var2.getLayoutParams();
      int var5 = var1.save(2);
      if (this.mCanSlide && !var7.slideable && this.mSlideableView != null) {
         var1.getClipBounds(this.mTmpRect);
         this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
         var1.clipRect(this.mTmpRect);
      }

      boolean var6;
      if (VERSION.SDK_INT >= 11) {
         var6 = super.drawChild(var1, var2, var3);
      } else if (var7.dimWhenOffset && this.mSlideOffset > 0.0F) {
         if (!var2.isDrawingCacheEnabled()) {
            var2.setDrawingCacheEnabled(true);
         }

         Bitmap var8 = var2.getDrawingCache();
         if (var8 != null) {
            var1.drawBitmap(var8, (float)var2.getLeft(), (float)var2.getTop(), var7.dimPaint);
            var6 = false;
         } else {
            Log.e("SlidingPaneLayout", "drawChild: child view " + var2 + " returned null drawing cache");
            var6 = super.drawChild(var1, var2, var3);
         }
      } else {
         if (var2.isDrawingCacheEnabled()) {
            var2.setDrawingCacheEnabled(false);
         }

         var6 = super.drawChild(var1, var2, var3);
      }

      var1.restoreToCount(var5);
      return var6;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new SlidingPaneLayout.LayoutParams();
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new SlidingPaneLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MarginLayoutParams ? new SlidingPaneLayout.LayoutParams((MarginLayoutParams)var1) : new SlidingPaneLayout.LayoutParams(var1);
   }

   public int getCoveredFadeColor() {
      return this.mCoveredFadeColor;
   }

   public int getParallaxDistance() {
      return this.mParallaxBy;
   }

   public int getSliderFadeColor() {
      return this.mSliderFadeColor;
   }

   boolean isDimmed(View var1) {
      if (var1 != null) {
         SlidingPaneLayout.LayoutParams var2 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
         if (this.mCanSlide && var2.dimWhenOffset && this.mSlideOffset > 0.0F) {
            return true;
         }
      }

      return false;
   }

   public boolean isOpen() {
      return !this.mCanSlide || this.mSlideOffset == 1.0F;
   }

   public boolean isSlideable() {
      return this.mCanSlide;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mFirstLayout = true;
      int var1 = 0;

      for(int var2 = this.mPostedRunnables.size(); var1 < var2; ++var1) {
         ((SlidingPaneLayout.DisableLayerRunnable)this.mPostedRunnables.get(var1)).run();
      }

      this.mPostedRunnables.clear();
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var6 = MotionEventCompat.getActionMasked(var1);
      if (!this.mCanSlide && var6 == 0 && this.getChildCount() > 1) {
         View var8 = this.getChildAt(1);
         if (var8 != null) {
            boolean var7;
            if (!this.mDragHelper.isViewUnder(var8, (int)var1.getX(), (int)var1.getY())) {
               var7 = true;
            } else {
               var7 = false;
            }

            this.mPreservedOpenState = var7;
         }
      }

      if (this.mCanSlide && (!this.mIsUnableToDrag || var6 == 0)) {
         if (var6 != 3 && var6 != 1) {
            boolean var5 = false;
            boolean var4 = var5;
            float var2;
            float var3;
            switch(var6) {
            case 0:
               this.mIsUnableToDrag = false;
               var2 = var1.getX();
               var3 = var1.getY();
               this.mInitialMotionX = var2;
               this.mInitialMotionY = var3;
               var4 = var5;
               if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)var2, (int)var3)) {
                  var4 = var5;
                  if (this.isDimmed(this.mSlideableView)) {
                     var4 = true;
                  }
               }
            case 1:
               break;
            case 2:
               var3 = var1.getX();
               var2 = var1.getY();
               var3 = Math.abs(var3 - this.mInitialMotionX);
               var2 = Math.abs(var2 - this.mInitialMotionY);
               var4 = var5;
               if (var3 > (float)this.mDragHelper.getTouchSlop()) {
                  var4 = var5;
                  if (var2 > var3) {
                     this.mDragHelper.cancel();
                     this.mIsUnableToDrag = true;
                     return false;
                  }
               }
               break;
            default:
               var4 = var5;
            }

            return this.mDragHelper.shouldInterceptTouchEvent(var1) || var4;
         } else {
            this.mDragHelper.cancel();
            return false;
         }
      } else {
         this.mDragHelper.cancel();
         return super.onInterceptTouchEvent(var1);
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var8 = var4 - var2;
      var2 = this.getPaddingLeft();
      int var9 = this.getPaddingRight();
      int var10 = this.getPaddingTop();
      int var7 = this.getChildCount();
      if (this.mFirstLayout) {
         float var6;
         if (this.mCanSlide && this.mPreservedOpenState) {
            var6 = 1.0F;
         } else {
            var6 = 0.0F;
         }

         this.mSlideOffset = var6;
      }

      byte var16 = 0;
      var3 = var2;
      var2 = var2;

      for(var4 = var16; var4 < var7; ++var4) {
         View var14 = this.getChildAt(var4);
         if (var14.getVisibility() != 8) {
            SlidingPaneLayout.LayoutParams var15 = (SlidingPaneLayout.LayoutParams)var14.getLayoutParams();
            int var11 = var14.getMeasuredWidth();
            var5 = 0;
            if (var15.slideable) {
               int var12 = var15.leftMargin;
               int var13 = var15.rightMargin;
               var12 = Math.min(var2, var8 - var9 - this.mOverhangSize) - var3 - (var12 + var13);
               this.mSlideRange = var12;
               if (var15.leftMargin + var3 + var12 + var11 / 2 > var8 - var9) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var15.dimWhenOffset = var1;
               var12 = (int)((float)var12 * this.mSlideOffset);
               var3 += var15.leftMargin + var12;
               this.mSlideOffset = (float)var12 / (float)this.mSlideRange;
            } else if (this.mCanSlide && this.mParallaxBy != 0) {
               var5 = (int)((1.0F - this.mSlideOffset) * (float)this.mParallaxBy);
               var3 = var2;
            } else {
               var3 = var2;
            }

            var5 = var3 - var5;
            var14.layout(var5, var10, var5 + var11, var10 + var14.getMeasuredHeight());
            var2 += var14.getWidth();
         }
      }

      if (this.mFirstLayout) {
         if (this.mCanSlide) {
            if (this.mParallaxBy != 0) {
               this.parallaxOtherViews(this.mSlideOffset);
            }

            if (((SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
               this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
            }
         } else {
            for(var2 = 0; var2 < var7; ++var2) {
               this.dimChildView(this.getChildAt(var2), 0.0F, this.mSliderFadeColor);
            }
         }

         this.updateObscuredViewsVisibility(this.mSlideableView);
      }

      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      int var8 = MeasureSpec.getMode(var1);
      int var5 = MeasureSpec.getSize(var1);
      int var6 = MeasureSpec.getMode(var2);
      var2 = MeasureSpec.getSize(var2);
      int var7;
      int var9;
      if (var8 != 1073741824) {
         if (!this.isInEditMode()) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
         }

         if (var8 == Integer.MIN_VALUE) {
            var7 = var5;
            var1 = var2;
            var9 = var6;
         } else {
            var9 = var6;
            var1 = var2;
            var7 = var5;
            if (var8 == 0) {
               var7 = 300;
               var9 = var6;
               var1 = var2;
            }
         }
      } else {
         var9 = var6;
         var1 = var2;
         var7 = var5;
         if (var6 == 0) {
            if (!this.isInEditMode()) {
               throw new IllegalStateException("Height must not be UNSPECIFIED");
            }

            var9 = var6;
            var1 = var2;
            var7 = var5;
            if (var6 == 0) {
               var9 = Integer.MIN_VALUE;
               var1 = 300;
               var7 = var5;
            }
         }
      }

      var5 = 0;
      var2 = -1;
      switch(var9) {
      case Integer.MIN_VALUE:
         var2 = var1 - this.getPaddingTop() - this.getPaddingBottom();
         break;
      case 1073741824:
         var2 = var1 - this.getPaddingTop() - this.getPaddingBottom();
         var5 = var2;
      }

      float var3 = 0.0F;
      boolean var14 = false;
      var8 = var7 - this.getPaddingLeft() - this.getPaddingRight();
      int var12 = this.getChildCount();
      if (var12 > 2) {
         Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
      }

      this.mSlideableView = null;

      int var10;
      int var11;
      View var16;
      SlidingPaneLayout.LayoutParams var17;
      for(var10 = 0; var10 < var12; var8 = var11) {
         var16 = this.getChildAt(var10);
         var17 = (SlidingPaneLayout.LayoutParams)var16.getLayoutParams();
         boolean var15;
         if (var16.getVisibility() == 8) {
            var17.dimWhenOffset = false;
            var11 = var8;
            var6 = var5;
            var15 = var14;
         } else {
            label182: {
               float var4 = var3;
               if (var17.weight > 0.0F) {
                  var4 = var3 + var17.weight;
                  var15 = var14;
                  var6 = var5;
                  var3 = var4;
                  var11 = var8;
                  if (var17.width == 0) {
                     break label182;
                  }
               }

               var1 = var17.leftMargin + var17.rightMargin;
               if (var17.width == -2) {
                  var1 = MeasureSpec.makeMeasureSpec(var7 - var1, Integer.MIN_VALUE);
               } else if (var17.width == -1) {
                  var1 = MeasureSpec.makeMeasureSpec(var7 - var1, 1073741824);
               } else {
                  var1 = MeasureSpec.makeMeasureSpec(var17.width, 1073741824);
               }

               if (var17.height == -2) {
                  var6 = MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE);
               } else if (var17.height == -1) {
                  var6 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
               } else {
                  var6 = MeasureSpec.makeMeasureSpec(var17.height, 1073741824);
               }

               var16.measure(var1, var6);
               var6 = var16.getMeasuredWidth();
               var11 = var16.getMeasuredHeight();
               var1 = var5;
               if (var9 == Integer.MIN_VALUE) {
                  var1 = var5;
                  if (var11 > var5) {
                     var1 = Math.min(var11, var2);
                  }
               }

               var5 = var8 - var6;
               if (var5 < 0) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               var17.slideable = var15;
               var14 |= var15;
               var15 = var14;
               var6 = var1;
               var3 = var4;
               var11 = var5;
               if (var17.slideable) {
                  this.mSlideableView = var16;
                  var15 = var14;
                  var6 = var1;
                  var3 = var4;
                  var11 = var5;
               }
            }
         }

         ++var10;
         var14 = var15;
         var5 = var6;
      }

      if (var14 || var3 > 0.0F) {
         var10 = var7 - this.mOverhangSize;

         for(var6 = 0; var6 < var12; ++var6) {
            var16 = this.getChildAt(var6);
            if (var16.getVisibility() != 8) {
               var17 = (SlidingPaneLayout.LayoutParams)var16.getLayoutParams();
               if (var16.getVisibility() != 8) {
                  boolean var18;
                  if (var17.width == 0 && var17.weight > 0.0F) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }

                  if (var18) {
                     var9 = 0;
                  } else {
                     var9 = var16.getMeasuredWidth();
                  }

                  if (var14 && var16 != this.mSlideableView) {
                     if (var17.width < 0 && (var9 > var10 || var17.weight > 0.0F)) {
                        if (var18) {
                           if (var17.height == -2) {
                              var1 = MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE);
                           } else if (var17.height == -1) {
                              var1 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
                           } else {
                              var1 = MeasureSpec.makeMeasureSpec(var17.height, 1073741824);
                           }
                        } else {
                           var1 = MeasureSpec.makeMeasureSpec(var16.getMeasuredHeight(), 1073741824);
                        }

                        var16.measure(MeasureSpec.makeMeasureSpec(var10, 1073741824), var1);
                     }
                  } else if (var17.weight > 0.0F) {
                     if (var17.width == 0) {
                        if (var17.height == -2) {
                           var1 = MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE);
                        } else if (var17.height == -1) {
                           var1 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
                        } else {
                           var1 = MeasureSpec.makeMeasureSpec(var17.height, 1073741824);
                        }
                     } else {
                        var1 = MeasureSpec.makeMeasureSpec(var16.getMeasuredHeight(), 1073741824);
                     }

                     if (var14) {
                        var11 = var7 - (var17.leftMargin + var17.rightMargin);
                        int var13 = MeasureSpec.makeMeasureSpec(var11, 1073741824);
                        if (var9 != var11) {
                           var16.measure(var13, var1);
                        }
                     } else {
                        var11 = Math.max(0, var8);
                        var16.measure(MeasureSpec.makeMeasureSpec(var9 + (int)(var17.weight * (float)var11 / var3), 1073741824), var1);
                     }
                  }
               }
            }
         }
      }

      this.setMeasuredDimension(var7, var5);
      this.mCanSlide = var14;
      if (this.mDragHelper.getViewDragState() != 0 && !var14) {
         this.mDragHelper.abort();
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      SlidingPaneLayout.SavedState var2 = (SlidingPaneLayout.SavedState)var1;
      super.onRestoreInstanceState(var2.getSuperState());
      if (var2.isOpen) {
         this.openPane();
      } else {
         this.closePane();
      }

      this.mPreservedOpenState = var2.isOpen;
   }

   protected Parcelable onSaveInstanceState() {
      SlidingPaneLayout.SavedState var2 = new SlidingPaneLayout.SavedState(super.onSaveInstanceState());
      boolean var1;
      if (this.isSlideable()) {
         var1 = this.isOpen();
      } else {
         var1 = this.mPreservedOpenState;
      }

      var2.isOpen = var1;
      return var2;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3) {
         this.mFirstLayout = true;
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var7;
      if (!this.mCanSlide) {
         var7 = super.onTouchEvent(var1);
      } else {
         this.mDragHelper.processTouchEvent(var1);
         int var6 = var1.getAction();
         boolean var8 = true;
         float var2;
         float var3;
         switch(var6 & 255) {
         case 0:
            var2 = var1.getX();
            var3 = var1.getY();
            this.mInitialMotionX = var2;
            this.mInitialMotionY = var3;
            return true;
         case 1:
            var7 = var8;
            if (this.isDimmed(this.mSlideableView)) {
               var2 = var1.getX();
               var3 = var1.getY();
               float var4 = var2 - this.mInitialMotionX;
               float var5 = var3 - this.mInitialMotionY;
               var6 = this.mDragHelper.getTouchSlop();
               var7 = var8;
               if (var4 * var4 + var5 * var5 < (float)(var6 * var6)) {
                  var7 = var8;
                  if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)var2, (int)var3)) {
                     this.closePane(this.mSlideableView, 0);
                     return true;
                  }
               }
            }
            break;
         default:
            return true;
         }
      }

      return var7;
   }

   public boolean openPane() {
      return this.openPane(this.mSlideableView, 0);
   }

   public void requestChildFocus(View var1, View var2) {
      super.requestChildFocus(var1, var2);
      if (!this.isInTouchMode() && !this.mCanSlide) {
         boolean var3;
         if (var1 == this.mSlideableView) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mPreservedOpenState = var3;
      }

   }

   void setAllChildrenVisible() {
      int var1 = 0;

      for(int var2 = this.getChildCount(); var1 < var2; ++var1) {
         View var3 = this.getChildAt(var1);
         if (var3.getVisibility() == 4) {
            var3.setVisibility(0);
         }
      }

   }

   public void setCoveredFadeColor(int var1) {
      this.mCoveredFadeColor = var1;
   }

   public void setPanelSlideListener(SlidingPaneLayout.PanelSlideListener var1) {
      this.mPanelSlideListener = var1;
   }

   public void setParallaxDistance(int var1) {
      this.mParallaxBy = var1;
      this.requestLayout();
   }

   public void setShadowDrawable(Drawable var1) {
      this.mShadowDrawable = var1;
   }

   public void setShadowResource(int var1) {
      this.setShadowDrawable(this.getResources().getDrawable(var1));
   }

   public void setSliderFadeColor(int var1) {
      this.mSliderFadeColor = var1;
   }

   @Deprecated
   public void smoothSlideClosed() {
      this.closePane();
   }

   @Deprecated
   public void smoothSlideOpen() {
      this.openPane();
   }

   boolean smoothSlideTo(float var1, int var2) {
      if (this.mCanSlide) {
         SlidingPaneLayout.LayoutParams var3 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         var2 = (int)((float)(this.getPaddingLeft() + var3.leftMargin) + (float)this.mSlideRange * var1);
         if (this.mDragHelper.smoothSlideViewTo(this.mSlideableView, var2, this.mSlideableView.getTop())) {
            this.setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
         }
      }

      return false;
   }

   void updateObscuredViewsVisibility(View var1) {
      int var8 = this.getPaddingLeft();
      int var9 = this.getWidth();
      int var10 = this.getPaddingRight();
      int var11 = this.getPaddingTop();
      int var12 = this.getHeight();
      int var13 = this.getPaddingBottom();
      int var2;
      int var3;
      int var4;
      int var5;
      if (var1 != null && viewIsOpaque(var1)) {
         var3 = var1.getLeft();
         var4 = var1.getRight();
         var5 = var1.getTop();
         var2 = var1.getBottom();
      } else {
         var2 = 0;
         var5 = 0;
         var4 = 0;
         var3 = 0;
      }

      int var6 = 0;

      for(int var14 = this.getChildCount(); var6 < var14; ++var6) {
         View var18 = this.getChildAt(var6);
         if (var18 == var1) {
            break;
         }

         int var7 = Math.max(var8, var18.getLeft());
         int var15 = Math.max(var11, var18.getTop());
         int var16 = Math.min(var9 - var10, var18.getRight());
         int var17 = Math.min(var12 - var13, var18.getBottom());
         byte var19;
         if (var7 >= var3 && var15 >= var5 && var16 <= var4 && var17 <= var2) {
            var19 = 4;
         } else {
            var19 = 0;
         }

         var18.setVisibility(var19);
      }

   }

   class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final Rect mTmpRect = new Rect();

      private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat var1, AccessibilityNodeInfoCompat var2) {
         Rect var3 = this.mTmpRect;
         var2.getBoundsInParent(var3);
         var1.setBoundsInParent(var3);
         var2.getBoundsInScreen(var3);
         var1.setBoundsInScreen(var3);
         var1.setVisibleToUser(var2.isVisibleToUser());
         var1.setPackageName(var2.getPackageName());
         var1.setClassName(var2.getClassName());
         var1.setContentDescription(var2.getContentDescription());
         var1.setEnabled(var2.isEnabled());
         var1.setClickable(var2.isClickable());
         var1.setFocusable(var2.isFocusable());
         var1.setFocused(var2.isFocused());
         var1.setAccessibilityFocused(var2.isAccessibilityFocused());
         var1.setSelected(var2.isSelected());
         var1.setLongClickable(var2.isLongClickable());
         var1.addAction(var2.getActions());
         var1.setMovementGranularities(var2.getMovementGranularities());
      }

      public boolean filter(View var1) {
         return SlidingPaneLayout.this.isDimmed(var1);
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(SlidingPaneLayout.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         AccessibilityNodeInfoCompat var5 = AccessibilityNodeInfoCompat.obtain(var2);
         super.onInitializeAccessibilityNodeInfo(var1, var5);
         this.copyNodeInfoNoChildren(var2, var5);
         var5.recycle();
         var2.setClassName(SlidingPaneLayout.class.getName());
         var2.setSource(var1);
         ViewParent var6 = ViewCompat.getParentForAccessibility(var1);
         if (var6 instanceof View) {
            var2.setParent((View)var6);
         }

         int var4 = SlidingPaneLayout.this.getChildCount();

         for(int var3 = 0; var3 < var4; ++var3) {
            var1 = SlidingPaneLayout.this.getChildAt(var3);
            if (!this.filter(var1) && var1.getVisibility() == 0) {
               ViewCompat.setImportantForAccessibility(var1, 1);
               var2.addChild(var1);
            }
         }

      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         return !this.filter(var2) ? super.onRequestSendAccessibilityEvent(var1, var2, var3) : false;
      }
   }

   private class DisableLayerRunnable implements Runnable {
      final View mChildView;

      DisableLayerRunnable(View var2) {
         this.mChildView = var2;
      }

      public void run() {
         if (this.mChildView.getParent() == SlidingPaneLayout.this) {
            ViewCompat.setLayerType(this.mChildView, 0, (Paint)null);
            SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
         }

         SlidingPaneLayout.this.mPostedRunnables.remove(this);
      }
   }

   private class DragHelperCallback extends ViewDragHelper.Callback {
      private DragHelperCallback() {
      }

      // $FF: synthetic method
      DragHelperCallback(Object var2) {
         this();
      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         SlidingPaneLayout.LayoutParams var5 = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
         var3 = SlidingPaneLayout.this.getPaddingLeft() + var5.leftMargin;
         int var4 = SlidingPaneLayout.this.mSlideRange;
         return Math.min(Math.max(var2, var3), var3 + var4);
      }

      public int getViewHorizontalDragRange(View var1) {
         return SlidingPaneLayout.this.mSlideRange;
      }

      public void onEdgeDragStarted(int var1, int var2) {
         SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, var2);
      }

      public void onViewCaptured(View var1, int var2) {
         SlidingPaneLayout.this.setAllChildrenVisible();
      }

      public void onViewDragStateChanged(int var1) {
         if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
            if (SlidingPaneLayout.this.mSlideOffset != 0.0F) {
               SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
               SlidingPaneLayout.this.mPreservedOpenState = true;
               return;
            }

            SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
            SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
            SlidingPaneLayout.this.mPreservedOpenState = false;
         }

      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         SlidingPaneLayout.this.onPanelDragged(var2);
         SlidingPaneLayout.this.invalidate();
      }

      public void onViewReleased(View var1, float var2, float var3) {
         int var4;
         label14: {
            SlidingPaneLayout.LayoutParams var6 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
            int var5 = SlidingPaneLayout.this.getPaddingLeft() + var6.leftMargin;
            if (var2 <= 0.0F) {
               var4 = var5;
               if (var2 != 0.0F) {
                  break label14;
               }

               var4 = var5;
               if (SlidingPaneLayout.this.mSlideOffset <= 0.5F) {
                  break label14;
               }
            }

            var4 = var5 + SlidingPaneLayout.this.mSlideRange;
         }

         SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(var4, var1.getTop());
         SlidingPaneLayout.this.invalidate();
      }

      public boolean tryCaptureView(View var1, int var2) {
         return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)var1.getLayoutParams()).slideable;
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int[] ATTRS = new int[]{16843137};
      Paint dimPaint;
      boolean dimWhenOffset;
      boolean slideable;
      public float weight = 0.0F;

      public LayoutParams() {
         super(-1, -1);
      }

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, ATTRS);
         this.weight = var3.getFloat(0, 0.0F);
         var3.recycle();
      }

      public LayoutParams(SlidingPaneLayout.LayoutParams var1) {
         super(var1);
         this.weight = var1.weight;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }
   }

   public interface PanelSlideListener {
      void onPanelClosed(View var1);

      void onPanelOpened(View var1);

      void onPanelSlide(View var1, float var2);
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public SlidingPaneLayout.SavedState createFromParcel(Parcel var1) {
            return new SlidingPaneLayout.SavedState(var1);
         }

         public SlidingPaneLayout.SavedState[] newArray(int var1) {
            return new SlidingPaneLayout.SavedState[var1];
         }
      };
      boolean isOpen;

      private SavedState(Parcel var1) {
         super(var1);
         boolean var2;
         if (var1.readInt() != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.isOpen = var2;
      }

      // $FF: synthetic method
      SavedState(Parcel var1, Object var2) {
         this(var1);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         byte var3;
         if (this.isOpen) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         var1.writeInt(var3);
      }
   }

   public static class SimplePanelSlideListener implements SlidingPaneLayout.PanelSlideListener {
      public void onPanelClosed(View var1) {
      }

      public void onPanelOpened(View var1) {
      }

      public void onPanelSlide(View var1, float var2) {
      }
   }

   interface SlidingPanelLayoutImpl {
      void invalidateChildRegion(SlidingPaneLayout var1, View var2);
   }

   static class SlidingPanelLayoutImplBase implements SlidingPaneLayout.SlidingPanelLayoutImpl {
      public void invalidateChildRegion(SlidingPaneLayout var1, View var2) {
         ViewCompat.postInvalidateOnAnimation(var1, var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom());
      }
   }

   static class SlidingPanelLayoutImplJB extends SlidingPaneLayout.SlidingPanelLayoutImplBase {
      private Method mGetDisplayList;
      private Field mRecreateDisplayList;

      SlidingPanelLayoutImplJB() {
         try {
            this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[])null);
         } catch (NoSuchMethodException var3) {
            Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", var3);
         }

         try {
            this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
            this.mRecreateDisplayList.setAccessible(true);
         } catch (NoSuchFieldException var2) {
            Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", var2);
         }
      }

      public void invalidateChildRegion(SlidingPaneLayout var1, View var2) {
         if (this.mGetDisplayList != null && this.mRecreateDisplayList != null) {
            try {
               this.mRecreateDisplayList.setBoolean(var2, true);
               this.mGetDisplayList.invoke(var2, (Object[])null);
            } catch (Exception var4) {
               Log.e("SlidingPaneLayout", "Error refreshing display list state", var4);
            }

            super.invalidateChildRegion(var1, var2);
         } else {
            var2.invalidate();
         }
      }
   }

   static class SlidingPanelLayoutImplJBMR1 extends SlidingPaneLayout.SlidingPanelLayoutImplBase {
      public void invalidateChildRegion(SlidingPaneLayout var1, View var2) {
         ViewCompat.setLayerPaint(var2, ((SlidingPaneLayout.LayoutParams)var2.getLayoutParams()).dimPaint);
      }
   }
}
