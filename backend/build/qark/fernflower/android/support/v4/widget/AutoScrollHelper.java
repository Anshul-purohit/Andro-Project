package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public abstract class AutoScrollHelper implements OnTouchListener {
   private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
   private static final int DEFAULT_EDGE_TYPE = 1;
   private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
   private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
   private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
   private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
   private static final int DEFAULT_RAMP_UP_DURATION = 500;
   private static final float DEFAULT_RELATIVE_EDGE = 0.2F;
   private static final float DEFAULT_RELATIVE_VELOCITY = 1.0F;
   public static final int EDGE_TYPE_INSIDE = 0;
   public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
   public static final int EDGE_TYPE_OUTSIDE = 2;
   private static final int HORIZONTAL = 0;
   public static final float NO_MAX = Float.MAX_VALUE;
   public static final float NO_MIN = 0.0F;
   public static final float RELATIVE_UNSPECIFIED = 0.0F;
   private static final int VERTICAL = 1;
   private int mActivationDelay;
   private boolean mAlreadyDelayed;
   private boolean mAnimating;
   private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
   private int mEdgeType;
   private boolean mEnabled;
   private boolean mExclusive;
   private float[] mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
   private float[] mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
   private float[] mMinimumVelocity = new float[]{0.0F, 0.0F};
   private boolean mNeedsCancel;
   private boolean mNeedsReset;
   private float[] mRelativeEdges = new float[]{0.0F, 0.0F};
   private float[] mRelativeVelocity = new float[]{0.0F, 0.0F};
   private Runnable mRunnable;
   private final AutoScrollHelper.ClampedScroller mScroller = new AutoScrollHelper.ClampedScroller();
   private final View mTarget;

   public AutoScrollHelper(View var1) {
      this.mTarget = var1;
      DisplayMetrics var4 = Resources.getSystem().getDisplayMetrics();
      int var2 = (int)(1575.0F * var4.density + 0.5F);
      int var3 = (int)(315.0F * var4.density + 0.5F);
      this.setMaximumVelocity((float)var2, (float)var2);
      this.setMinimumVelocity((float)var3, (float)var3);
      this.setEdgeType(1);
      this.setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
      this.setRelativeEdges(0.2F, 0.2F);
      this.setRelativeVelocity(1.0F, 1.0F);
      this.setActivationDelay(DEFAULT_ACTIVATION_DELAY);
      this.setRampUpDuration(500);
      this.setRampDownDuration(500);
   }

   private void cancelTargetTouch() {
      long var1 = SystemClock.uptimeMillis();
      MotionEvent var3 = MotionEvent.obtain(var1, var1, 3, 0.0F, 0.0F, 0);
      this.mTarget.onTouchEvent(var3);
      var3.recycle();
   }

   private float computeTargetVelocity(int var1, float var2, float var3, float var4) {
      var2 = this.getEdgeValue(this.mRelativeEdges[var1], var3, this.mMaximumEdges[var1], var2);
      if (var2 == 0.0F) {
         return 0.0F;
      } else {
         float var6 = this.mRelativeVelocity[var1];
         var3 = this.mMinimumVelocity[var1];
         float var5 = this.mMaximumVelocity[var1];
         var4 = var6 * var4;
         return var2 > 0.0F ? constrain(var2 * var4, var3, var5) : -constrain(-var2 * var4, var3, var5);
      }
   }

   private static float constrain(float var0, float var1, float var2) {
      if (var0 > var2) {
         return var2;
      } else {
         return var0 < var1 ? var1 : var0;
      }
   }

   private static int constrain(int var0, int var1, int var2) {
      if (var0 > var2) {
         return var2;
      } else {
         return var0 < var1 ? var1 : var0;
      }
   }

   private float constrainEdgeValue(float var1, float var2) {
      if (var2 != 0.0F) {
         switch(this.mEdgeType) {
         case 0:
         case 1:
            if (var1 < var2) {
               if (var1 >= 0.0F) {
                  return 1.0F - var1 / var2;
               }

               if (this.mAnimating && this.mEdgeType == 1) {
                  return 1.0F;
               }
            }
            break;
         case 2:
            if (var1 < 0.0F) {
               return var1 / -var2;
            }
            break;
         default:
            return 0.0F;
         }
      }

      return 0.0F;
   }

   private float getEdgeValue(float var1, float var2, float var3, float var4) {
      float var5 = 0.0F;
      var1 = constrain(var1 * var2, 0.0F, var3);
      var3 = this.constrainEdgeValue(var4, var1);
      var2 = this.constrainEdgeValue(var2 - var4, var1) - var3;
      if (var2 < 0.0F) {
         var1 = -this.mEdgeInterpolator.getInterpolation(-var2);
      } else {
         var1 = var5;
         if (var2 <= 0.0F) {
            return var1;
         }

         var1 = this.mEdgeInterpolator.getInterpolation(var2);
      }

      var1 = constrain(var1, -1.0F, 1.0F);
      return var1;
   }

   private void requestStop() {
      if (this.mNeedsReset) {
         this.mAnimating = false;
      } else {
         this.mScroller.requestStop();
      }
   }

   private boolean shouldAnimate() {
      AutoScrollHelper.ClampedScroller var3 = this.mScroller;
      int var1 = var3.getVerticalDirection();
      int var2 = var3.getHorizontalDirection();
      return var1 != 0 && this.canTargetScrollVertically(var1) || var2 != 0 && this.canTargetScrollHorizontally(var2);
   }

   private void startAnimating() {
      if (this.mRunnable == null) {
         this.mRunnable = new AutoScrollHelper.ScrollAnimationRunnable();
      }

      this.mAnimating = true;
      this.mNeedsReset = true;
      if (!this.mAlreadyDelayed && this.mActivationDelay > 0) {
         ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, (long)this.mActivationDelay);
      } else {
         this.mRunnable.run();
      }

      this.mAlreadyDelayed = true;
   }

   public abstract boolean canTargetScrollHorizontally(int var1);

   public abstract boolean canTargetScrollVertically(int var1);

   public boolean isEnabled() {
      return this.mEnabled;
   }

   public boolean isExclusive() {
      return this.mExclusive;
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      boolean var5 = true;
      if (!this.mEnabled) {
         return false;
      } else {
         switch(MotionEventCompat.getActionMasked(var2)) {
         case 0:
            this.mNeedsCancel = true;
            this.mAlreadyDelayed = false;
         case 2:
            float var3 = this.computeTargetVelocity(0, var2.getX(), (float)var1.getWidth(), (float)this.mTarget.getWidth());
            float var4 = this.computeTargetVelocity(1, var2.getY(), (float)var1.getHeight(), (float)this.mTarget.getHeight());
            this.mScroller.setTargetVelocity(var3, var4);
            if (!this.mAnimating && this.shouldAnimate()) {
               this.startAnimating();
            }
            break;
         case 1:
         case 3:
            this.requestStop();
         }

         if (!this.mExclusive || !this.mAnimating) {
            var5 = false;
         }

         return var5;
      }
   }

   public abstract void scrollTargetBy(int var1, int var2);

   public AutoScrollHelper setActivationDelay(int var1) {
      this.mActivationDelay = var1;
      return this;
   }

   public AutoScrollHelper setEdgeType(int var1) {
      this.mEdgeType = var1;
      return this;
   }

   public AutoScrollHelper setEnabled(boolean var1) {
      if (this.mEnabled && !var1) {
         this.requestStop();
      }

      this.mEnabled = var1;
      return this;
   }

   public AutoScrollHelper setExclusive(boolean var1) {
      this.mExclusive = var1;
      return this;
   }

   public AutoScrollHelper setMaximumEdges(float var1, float var2) {
      this.mMaximumEdges[0] = var1;
      this.mMaximumEdges[1] = var2;
      return this;
   }

   public AutoScrollHelper setMaximumVelocity(float var1, float var2) {
      this.mMaximumVelocity[0] = var1 / 1000.0F;
      this.mMaximumVelocity[1] = var2 / 1000.0F;
      return this;
   }

   public AutoScrollHelper setMinimumVelocity(float var1, float var2) {
      this.mMinimumVelocity[0] = var1 / 1000.0F;
      this.mMinimumVelocity[1] = var2 / 1000.0F;
      return this;
   }

   public AutoScrollHelper setRampDownDuration(int var1) {
      this.mScroller.setRampDownDuration(var1);
      return this;
   }

   public AutoScrollHelper setRampUpDuration(int var1) {
      this.mScroller.setRampUpDuration(var1);
      return this;
   }

   public AutoScrollHelper setRelativeEdges(float var1, float var2) {
      this.mRelativeEdges[0] = var1;
      this.mRelativeEdges[1] = var2;
      return this;
   }

   public AutoScrollHelper setRelativeVelocity(float var1, float var2) {
      this.mRelativeVelocity[0] = var1 / 1000.0F;
      this.mRelativeVelocity[1] = var2 / 1000.0F;
      return this;
   }

   private static class ClampedScroller {
      private long mDeltaTime = 0L;
      private int mDeltaX = 0;
      private int mDeltaY = 0;
      private int mEffectiveRampDown;
      private int mRampDownDuration;
      private int mRampUpDuration;
      private long mStartTime = Long.MIN_VALUE;
      private long mStopTime = -1L;
      private float mStopValue;
      private float mTargetVelocityX;
      private float mTargetVelocityY;

      public ClampedScroller() {
      }

      private float getValueAt(long var1) {
         if (var1 < this.mStartTime) {
            return 0.0F;
         } else if (this.mStopTime >= 0L && var1 >= this.mStopTime) {
            long var5 = this.mStopTime;
            float var3 = this.mStopValue;
            float var4 = this.mStopValue;
            return AutoScrollHelper.constrain((float)(var1 - var5) / (float)this.mEffectiveRampDown, 0.0F, 1.0F) * var4 + (1.0F - var3);
         } else {
            return AutoScrollHelper.constrain((float)(var1 - this.mStartTime) / (float)this.mRampUpDuration, 0.0F, 1.0F) * 0.5F;
         }
      }

      private float interpolateValue(float var1) {
         return -4.0F * var1 * var1 + 4.0F * var1;
      }

      public void computeScrollDelta() {
         if (this.mDeltaTime == 0L) {
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
         } else {
            long var2 = AnimationUtils.currentAnimationTimeMillis();
            float var1 = this.interpolateValue(this.getValueAt(var2));
            long var4 = var2 - this.mDeltaTime;
            this.mDeltaTime = var2;
            this.mDeltaX = (int)((float)var4 * var1 * this.mTargetVelocityX);
            this.mDeltaY = (int)((float)var4 * var1 * this.mTargetVelocityY);
         }
      }

      public int getDeltaX() {
         return this.mDeltaX;
      }

      public int getDeltaY() {
         return this.mDeltaY;
      }

      public int getHorizontalDirection() {
         return (int)(this.mTargetVelocityX / Math.abs(this.mTargetVelocityX));
      }

      public int getVerticalDirection() {
         return (int)(this.mTargetVelocityY / Math.abs(this.mTargetVelocityY));
      }

      public boolean isFinished() {
         return this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + (long)this.mEffectiveRampDown;
      }

      public void requestStop() {
         long var1 = AnimationUtils.currentAnimationTimeMillis();
         this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(var1 - this.mStartTime), 0, this.mRampDownDuration);
         this.mStopValue = this.getValueAt(var1);
         this.mStopTime = var1;
      }

      public void setRampDownDuration(int var1) {
         this.mRampDownDuration = var1;
      }

      public void setRampUpDuration(int var1) {
         this.mRampUpDuration = var1;
      }

      public void setTargetVelocity(float var1, float var2) {
         this.mTargetVelocityX = var1;
         this.mTargetVelocityY = var2;
      }

      public void start() {
         this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
         this.mStopTime = -1L;
         this.mDeltaTime = this.mStartTime;
         this.mStopValue = 0.5F;
         this.mDeltaX = 0;
         this.mDeltaY = 0;
      }
   }

   private class ScrollAnimationRunnable implements Runnable {
      private ScrollAnimationRunnable() {
      }

      // $FF: synthetic method
      ScrollAnimationRunnable(Object var2) {
         this();
      }

      public void run() {
         if (AutoScrollHelper.this.mAnimating) {
            if (AutoScrollHelper.this.mNeedsReset) {
               AutoScrollHelper.this.mNeedsReset = false;
               AutoScrollHelper.this.mScroller.start();
            }

            AutoScrollHelper.ClampedScroller var3 = AutoScrollHelper.this.mScroller;
            if (!var3.isFinished() && AutoScrollHelper.this.shouldAnimate()) {
               if (AutoScrollHelper.this.mNeedsCancel) {
                  AutoScrollHelper.this.mNeedsCancel = false;
                  AutoScrollHelper.this.cancelTargetTouch();
               }

               var3.computeScrollDelta();
               int var1 = var3.getDeltaX();
               int var2 = var3.getDeltaY();
               AutoScrollHelper.this.scrollTargetBy(var1, var2);
               ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
            } else {
               AutoScrollHelper.this.mAnimating = false;
            }
         }
      }
   }
}
