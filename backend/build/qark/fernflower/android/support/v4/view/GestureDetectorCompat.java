package android.support.v4.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;

public class GestureDetectorCompat {
   private final GestureDetectorCompat.GestureDetectorCompatImpl mImpl;

   public GestureDetectorCompat(Context var1, OnGestureListener var2) {
      this(var1, var2, (Handler)null);
   }

   public GestureDetectorCompat(Context var1, OnGestureListener var2, Handler var3) {
      if (VERSION.SDK_INT > 17) {
         this.mImpl = new GestureDetectorCompat.GestureDetectorCompatImplJellybeanMr2(var1, var2, var3);
      } else {
         this.mImpl = new GestureDetectorCompat.GestureDetectorCompatImplBase(var1, var2, var3);
      }
   }

   public boolean isLongpressEnabled() {
      return this.mImpl.isLongpressEnabled();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return this.mImpl.onTouchEvent(var1);
   }

   public void setIsLongpressEnabled(boolean var1) {
      this.mImpl.setIsLongpressEnabled(var1);
   }

   public void setOnDoubleTapListener(OnDoubleTapListener var1) {
      this.mImpl.setOnDoubleTapListener(var1);
   }

   interface GestureDetectorCompatImpl {
      boolean isLongpressEnabled();

      boolean onTouchEvent(MotionEvent var1);

      void setIsLongpressEnabled(boolean var1);

      void setOnDoubleTapListener(OnDoubleTapListener var1);
   }

   static class GestureDetectorCompatImplBase implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
      private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
      private static final int LONG_PRESS = 2;
      private static final int SHOW_PRESS = 1;
      private static final int TAP = 3;
      private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
      private boolean mAlwaysInBiggerTapRegion;
      private boolean mAlwaysInTapRegion;
      private MotionEvent mCurrentDownEvent;
      private boolean mDeferConfirmSingleTap;
      private OnDoubleTapListener mDoubleTapListener;
      private int mDoubleTapSlopSquare;
      private float mDownFocusX;
      private float mDownFocusY;
      private final Handler mHandler;
      private boolean mInLongPress;
      private boolean mIsDoubleTapping;
      private boolean mIsLongpressEnabled;
      private float mLastFocusX;
      private float mLastFocusY;
      private final OnGestureListener mListener;
      private int mMaximumFlingVelocity;
      private int mMinimumFlingVelocity;
      private MotionEvent mPreviousUpEvent;
      private boolean mStillDown;
      private int mTouchSlopSquare;
      private VelocityTracker mVelocityTracker;

      public GestureDetectorCompatImplBase(Context var1, OnGestureListener var2, Handler var3) {
         if (var3 != null) {
            this.mHandler = new GestureDetectorCompat.GestureDetectorCompatImplBase.GestureHandler(var3);
         } else {
            this.mHandler = new GestureDetectorCompat.GestureDetectorCompatImplBase.GestureHandler();
         }

         this.mListener = var2;
         if (var2 instanceof OnDoubleTapListener) {
            this.setOnDoubleTapListener((OnDoubleTapListener)var2);
         }

         this.init(var1);
      }

      private void cancel() {
         this.mHandler.removeMessages(1);
         this.mHandler.removeMessages(2);
         this.mHandler.removeMessages(3);
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
         this.mIsDoubleTapping = false;
         this.mStillDown = false;
         this.mAlwaysInTapRegion = false;
         this.mAlwaysInBiggerTapRegion = false;
         this.mDeferConfirmSingleTap = false;
         if (this.mInLongPress) {
            this.mInLongPress = false;
         }

      }

      private void cancelTaps() {
         this.mHandler.removeMessages(1);
         this.mHandler.removeMessages(2);
         this.mHandler.removeMessages(3);
         this.mIsDoubleTapping = false;
         this.mAlwaysInTapRegion = false;
         this.mAlwaysInBiggerTapRegion = false;
         this.mDeferConfirmSingleTap = false;
         if (this.mInLongPress) {
            this.mInLongPress = false;
         }

      }

      private void dispatchLongPress() {
         this.mHandler.removeMessages(3);
         this.mDeferConfirmSingleTap = false;
         this.mInLongPress = true;
         this.mListener.onLongPress(this.mCurrentDownEvent);
      }

      private void init(Context var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("Context must not be null");
         } else if (this.mListener == null) {
            throw new IllegalArgumentException("OnGestureListener must not be null");
         } else {
            this.mIsLongpressEnabled = true;
            ViewConfiguration var4 = ViewConfiguration.get(var1);
            int var2 = var4.getScaledTouchSlop();
            int var3 = var4.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = var4.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = var4.getScaledMaximumFlingVelocity();
            this.mTouchSlopSquare = var2 * var2;
            this.mDoubleTapSlopSquare = var3 * var3;
         }
      }

      private boolean isConsideredDoubleTap(MotionEvent var1, MotionEvent var2, MotionEvent var3) {
         if (this.mAlwaysInBiggerTapRegion && var3.getEventTime() - var2.getEventTime() <= (long)DOUBLE_TAP_TIMEOUT) {
            int var4 = (int)var1.getX() - (int)var3.getX();
            int var5 = (int)var1.getY() - (int)var3.getY();
            if (var4 * var4 + var5 * var5 < this.mDoubleTapSlopSquare) {
               return true;
            }
         }

         return false;
      }

      public boolean isLongpressEnabled() {
         return this.mIsLongpressEnabled;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var10 = var1.getAction();
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         boolean var6;
         if ((var10 & 255) == 6) {
            var6 = true;
         } else {
            var6 = false;
         }

         int var7;
         if (var6) {
            var7 = MotionEventCompat.getActionIndex(var1);
         } else {
            var7 = -1;
         }

         float var3 = 0.0F;
         float var2 = 0.0F;
         int var9 = MotionEventCompat.getPointerCount(var1);

         int var8;
         for(var8 = 0; var8 < var9; ++var8) {
            if (var7 != var8) {
               var3 += MotionEventCompat.getX(var1, var8);
               var2 += MotionEventCompat.getY(var1, var8);
            }
         }

         int var17;
         if (var6) {
            var17 = var9 - 1;
         } else {
            var17 = var9;
         }

         var3 /= (float)var17;
         var2 /= (float)var17;
         boolean var18 = false;
         boolean var13 = false;
         boolean var14 = false;
         boolean var11 = false;
         boolean var12 = var11;
         switch(var10 & 255) {
         case 0:
            var6 = var18;
            if (this.mDoubleTapListener != null) {
               var11 = this.mHandler.hasMessages(3);
               if (var11) {
                  this.mHandler.removeMessages(3);
               }

               if (this.mCurrentDownEvent != null && this.mPreviousUpEvent != null && var11 && this.isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, var1)) {
                  this.mIsDoubleTapping = true;
                  var6 = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(var1);
               } else {
                  this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
                  var6 = var18;
               }
            }

            this.mLastFocusX = var3;
            this.mDownFocusX = var3;
            this.mLastFocusY = var2;
            this.mDownFocusY = var2;
            if (this.mCurrentDownEvent != null) {
               this.mCurrentDownEvent.recycle();
            }

            this.mCurrentDownEvent = MotionEvent.obtain(var1);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled) {
               this.mHandler.removeMessages(2);
               this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT + (long)LONGPRESS_TIMEOUT);
            }

            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT);
            return var6 | this.mListener.onDown(var1);
         case 1:
            this.mStillDown = false;
            MotionEvent var15 = MotionEvent.obtain(var1);
            if (this.mIsDoubleTapping) {
               var11 = false | this.mDoubleTapListener.onDoubleTapEvent(var1);
            } else if (this.mInLongPress) {
               this.mHandler.removeMessages(3);
               this.mInLongPress = false;
               var11 = var14;
            } else if (this.mAlwaysInTapRegion) {
               var12 = this.mListener.onSingleTapUp(var1);
               var11 = var12;
               if (this.mDeferConfirmSingleTap) {
                  var11 = var12;
                  if (this.mDoubleTapListener != null) {
                     this.mDoubleTapListener.onSingleTapConfirmed(var1);
                     var11 = var12;
                  }
               }
            } else {
               label102: {
                  VelocityTracker var16 = this.mVelocityTracker;
                  var17 = MotionEventCompat.getPointerId(var1, 0);
                  var16.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                  var2 = VelocityTrackerCompat.getYVelocity(var16, var17);
                  var3 = VelocityTrackerCompat.getXVelocity(var16, var17);
                  if (Math.abs(var2) <= (float)this.mMinimumFlingVelocity) {
                     var11 = var14;
                     if (Math.abs(var3) <= (float)this.mMinimumFlingVelocity) {
                        break label102;
                     }
                  }

                  var11 = this.mListener.onFling(this.mCurrentDownEvent, var1, var3, var2);
               }
            }

            if (this.mPreviousUpEvent != null) {
               this.mPreviousUpEvent.recycle();
            }

            this.mPreviousUpEvent = var15;
            if (this.mVelocityTracker != null) {
               this.mVelocityTracker.recycle();
               this.mVelocityTracker = null;
            }

            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            return var11;
         case 2:
            var12 = var11;
            if (this.mInLongPress) {
               break;
            }

            float var4 = this.mLastFocusX - var3;
            float var5 = this.mLastFocusY - var2;
            if (this.mIsDoubleTapping) {
               return false | this.mDoubleTapListener.onDoubleTapEvent(var1);
            }

            if (this.mAlwaysInTapRegion) {
               var17 = (int)(var3 - this.mDownFocusX);
               var7 = (int)(var2 - this.mDownFocusY);
               var17 = var17 * var17 + var7 * var7;
               var11 = var13;
               if (var17 > this.mTouchSlopSquare) {
                  var11 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var4, var5);
                  this.mLastFocusX = var3;
                  this.mLastFocusY = var2;
                  this.mAlwaysInTapRegion = false;
                  this.mHandler.removeMessages(3);
                  this.mHandler.removeMessages(1);
                  this.mHandler.removeMessages(2);
               }

               var12 = var11;
               if (var17 > this.mTouchSlopSquare) {
                  this.mAlwaysInBiggerTapRegion = false;
                  return var11;
               }
               break;
            } else {
               if (Math.abs(var4) < 1.0F) {
                  var12 = var11;
                  if (Math.abs(var5) < 1.0F) {
                     break;
                  }
               }

               var11 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var4, var5);
               this.mLastFocusX = var3;
               this.mLastFocusY = var2;
               return var11;
            }
         case 3:
            this.cancel();
            return false;
         case 4:
            break;
         case 5:
            this.mLastFocusX = var3;
            this.mDownFocusX = var3;
            this.mLastFocusY = var2;
            this.mDownFocusY = var2;
            this.cancelTaps();
            return false;
         case 6:
            this.mLastFocusX = var3;
            this.mDownFocusX = var3;
            this.mLastFocusY = var2;
            this.mDownFocusY = var2;
            this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            var7 = MotionEventCompat.getActionIndex(var1);
            var17 = MotionEventCompat.getPointerId(var1, var7);
            var2 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, var17);
            var3 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, var17);
            var17 = 0;

            while(true) {
               var12 = var11;
               if (var17 >= var9) {
                  return var12;
               }

               if (var17 != var7) {
                  var8 = MotionEventCompat.getPointerId(var1, var17);
                  if (var2 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, var8) + var3 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, var8) < 0.0F) {
                     this.mVelocityTracker.clear();
                     return false;
                  }
               }

               ++var17;
            }
         default:
            var12 = var11;
         }

         return var12;
      }

      public void setIsLongpressEnabled(boolean var1) {
         this.mIsLongpressEnabled = var1;
      }

      public void setOnDoubleTapListener(OnDoubleTapListener var1) {
         this.mDoubleTapListener = var1;
      }

      private class GestureHandler extends Handler {
         GestureHandler() {
         }

         GestureHandler(Handler var2) {
            super(var2.getLooper());
         }

         public void handleMessage(Message var1) {
            switch(var1.what) {
            case 1:
               GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
               break;
            case 2:
               GestureDetectorCompatImplBase.this.dispatchLongPress();
               return;
            case 3:
               if (GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
                  if (!GestureDetectorCompatImplBase.this.mStillDown) {
                     GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                     return;
                  }

                  GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                  return;
               }
               break;
            default:
               throw new RuntimeException("Unknown message " + var1);
            }

         }
      }
   }

   static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private final GestureDetector mDetector;

      public GestureDetectorCompatImplJellybeanMr2(Context var1, OnGestureListener var2, Handler var3) {
         this.mDetector = new GestureDetector(var1, var2, var3);
      }

      public boolean isLongpressEnabled() {
         return this.mDetector.isLongpressEnabled();
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return this.mDetector.onTouchEvent(var1);
      }

      public void setIsLongpressEnabled(boolean var1) {
         this.mDetector.setIsLongpressEnabled(var1);
      }

      public void setOnDoubleTapListener(OnDoubleTapListener var1) {
         this.mDetector.setOnDoubleTapListener(var1);
      }
   }
}
