package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;

public class SwipeRefreshLayout extends ViewGroup {
   private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5F;
   private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0F;
   private static final int[] LAYOUT_ATTRS = new int[]{16842766};
   private static final float MAX_SWIPE_DISTANCE_FACTOR = 0.6F;
   private static final float PROGRESS_BAR_HEIGHT = 4.0F;
   private static final int REFRESH_TRIGGER_DISTANCE = 120;
   private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 300L;
   private final AccelerateInterpolator mAccelerateInterpolator;
   private final Animation mAnimateToStartPosition;
   private final Runnable mCancel;
   private float mCurrPercentage;
   private int mCurrentTargetOffsetTop;
   private final DecelerateInterpolator mDecelerateInterpolator;
   private float mDistanceToTriggerSync;
   private MotionEvent mDownEvent;
   private int mFrom;
   private float mFromPercentage;
   private SwipeRefreshLayout.OnRefreshListener mListener;
   private int mMediumAnimationDuration;
   private int mOriginalOffsetTop;
   private float mPrevY;
   private SwipeProgressBar mProgressBar;
   private int mProgressBarHeight;
   private boolean mRefreshing;
   private final Runnable mReturnToStartPosition;
   private final AnimationListener mReturnToStartPositionListener;
   private boolean mReturningToStart;
   private final AnimationListener mShrinkAnimationListener;
   private Animation mShrinkTrigger;
   private View mTarget;
   private int mTouchSlop;

   public SwipeRefreshLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SwipeRefreshLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mRefreshing = false;
      this.mDistanceToTriggerSync = -1.0F;
      this.mFromPercentage = 0.0F;
      this.mCurrPercentage = 0.0F;
      this.mAnimateToStartPosition = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            int var3 = 0;
            if (SwipeRefreshLayout.this.mFrom != SwipeRefreshLayout.this.mOriginalOffsetTop) {
               var3 = SwipeRefreshLayout.this.mFrom + (int)((float)(SwipeRefreshLayout.this.mOriginalOffsetTop - SwipeRefreshLayout.this.mFrom) * var1);
            }

            int var4 = var3 - SwipeRefreshLayout.this.mTarget.getTop();
            int var5 = SwipeRefreshLayout.this.mTarget.getTop();
            var3 = var4;
            if (var4 + var5 < 0) {
               var3 = 0 - var5;
            }

            SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(var3);
         }
      };
      this.mShrinkTrigger = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            float var3 = SwipeRefreshLayout.this.mFromPercentage;
            float var4 = SwipeRefreshLayout.this.mFromPercentage;
            SwipeRefreshLayout.this.mProgressBar.setTriggerPercentage(var3 + (0.0F - var4) * var1);
         }
      };
      this.mReturnToStartPositionListener = new SwipeRefreshLayout.BaseAnimationListener() {
         public void onAnimationEnd(Animation var1) {
            SwipeRefreshLayout.this.mCurrentTargetOffsetTop = 0;
         }
      };
      this.mShrinkAnimationListener = new SwipeRefreshLayout.BaseAnimationListener() {
         public void onAnimationEnd(Animation var1) {
            SwipeRefreshLayout.this.mCurrPercentage = 0.0F;
         }
      };
      this.mReturnToStartPosition = new Runnable() {
         public void run() {
            SwipeRefreshLayout.this.mReturningToStart = true;
            SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
         }
      };
      this.mCancel = new Runnable() {
         public void run() {
            SwipeRefreshLayout.this.mReturningToStart = true;
            if (SwipeRefreshLayout.this.mProgressBar != null) {
               SwipeRefreshLayout.this.mFromPercentage = SwipeRefreshLayout.this.mCurrPercentage;
               SwipeRefreshLayout.this.mShrinkTrigger.setDuration((long)SwipeRefreshLayout.this.mMediumAnimationDuration);
               SwipeRefreshLayout.this.mShrinkTrigger.setAnimationListener(SwipeRefreshLayout.this.mShrinkAnimationListener);
               SwipeRefreshLayout.this.mShrinkTrigger.reset();
               SwipeRefreshLayout.this.mShrinkTrigger.setInterpolator(SwipeRefreshLayout.this.mDecelerateInterpolator);
               SwipeRefreshLayout.this.startAnimation(SwipeRefreshLayout.this.mShrinkTrigger);
            }

            SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
         }
      };
      this.mTouchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
      this.mMediumAnimationDuration = this.getResources().getInteger(17694721);
      this.setWillNotDraw(false);
      this.mProgressBar = new SwipeProgressBar(this);
      this.mProgressBarHeight = (int)(this.getResources().getDisplayMetrics().density * 4.0F);
      this.mDecelerateInterpolator = new DecelerateInterpolator(2.0F);
      this.mAccelerateInterpolator = new AccelerateInterpolator(1.5F);
      TypedArray var3 = var1.obtainStyledAttributes(var2, LAYOUT_ATTRS);
      this.setEnabled(var3.getBoolean(0, true));
      var3.recycle();
   }

   private void animateOffsetToStartPosition(int var1, AnimationListener var2) {
      this.mFrom = var1;
      this.mAnimateToStartPosition.reset();
      this.mAnimateToStartPosition.setDuration((long)this.mMediumAnimationDuration);
      this.mAnimateToStartPosition.setAnimationListener(var2);
      this.mAnimateToStartPosition.setInterpolator(this.mDecelerateInterpolator);
      this.mTarget.startAnimation(this.mAnimateToStartPosition);
   }

   private void ensureTarget() {
      if (this.mTarget == null) {
         if (this.getChildCount() > 1 && !this.isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
         }

         this.mTarget = this.getChildAt(0);
         this.mOriginalOffsetTop = this.mTarget.getTop() + this.getPaddingTop();
      }

      if (this.mDistanceToTriggerSync == -1.0F && this.getParent() != null && ((View)this.getParent()).getHeight() > 0) {
         DisplayMetrics var1 = this.getResources().getDisplayMetrics();
         this.mDistanceToTriggerSync = (float)((int)Math.min((float)((View)this.getParent()).getHeight() * 0.6F, 120.0F * var1.density));
      }

   }

   private void setTargetOffsetTopAndBottom(int var1) {
      this.mTarget.offsetTopAndBottom(var1);
      this.mCurrentTargetOffsetTop = this.mTarget.getTop();
   }

   private void setTriggerPercentage(float var1) {
      if (var1 == 0.0F) {
         this.mCurrPercentage = 0.0F;
      } else {
         this.mCurrPercentage = var1;
         this.mProgressBar.setTriggerPercentage(var1);
      }
   }

   private void startRefresh() {
      this.removeCallbacks(this.mCancel);
      this.mReturnToStartPosition.run();
      this.setRefreshing(true);
      this.mListener.onRefresh();
   }

   private void updateContentOffsetTop(int var1) {
      int var3 = this.mTarget.getTop();
      int var2;
      if ((float)var1 > this.mDistanceToTriggerSync) {
         var2 = (int)this.mDistanceToTriggerSync;
      } else {
         var2 = var1;
         if (var1 < 0) {
            var2 = 0;
         }
      }

      this.setTargetOffsetTopAndBottom(var2 - var3);
   }

   private void updatePositionTimeout() {
      this.removeCallbacks(this.mCancel);
      this.postDelayed(this.mCancel, 300L);
   }

   public boolean canChildScrollUp() {
      if (VERSION.SDK_INT >= 14) {
         return ViewCompat.canScrollVertically(this.mTarget, -1);
      } else {
         if (this.mTarget instanceof AbsListView) {
            AbsListView var1 = (AbsListView)this.mTarget;
            if (var1.getChildCount() <= 0 || var1.getFirstVisiblePosition() <= 0 && var1.getChildAt(0).getTop() >= var1.getPaddingTop()) {
               return false;
            }
         } else if (this.mTarget.getScrollY() <= 0) {
            return false;
         }

         return true;
      }
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      this.mProgressBar.draw(var1);
   }

   public boolean isRefreshing() {
      return this.mRefreshing;
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.removeCallbacks(this.mCancel);
      this.removeCallbacks(this.mReturnToStartPosition);
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks(this.mReturnToStartPosition);
      this.removeCallbacks(this.mCancel);
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      this.ensureTarget();
      boolean var3 = false;
      if (this.mReturningToStart && var1.getAction() == 0) {
         this.mReturningToStart = false;
      }

      boolean var2 = var3;
      if (this.isEnabled()) {
         var2 = var3;
         if (!this.mReturningToStart) {
            var2 = var3;
            if (!this.canChildScrollUp()) {
               var2 = this.onTouchEvent(var1);
            }
         }
      }

      var3 = var2;
      if (!var2) {
         var3 = super.onInterceptTouchEvent(var1);
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var2 = this.getMeasuredWidth();
      var3 = this.getMeasuredHeight();
      this.mProgressBar.setBounds(0, 0, var2, this.mProgressBarHeight);
      if (this.getChildCount() != 0) {
         View var6 = this.getChildAt(0);
         var4 = this.getPaddingLeft();
         var5 = this.mCurrentTargetOffsetTop + this.getPaddingTop();
         var6.layout(var4, var5, var4 + (var2 - this.getPaddingLeft() - this.getPaddingRight()), var5 + (var3 - this.getPaddingTop() - this.getPaddingBottom()));
      }
   }

   public void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.getChildCount() > 1 && !this.isInEditMode()) {
         throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
      } else {
         if (this.getChildCount() > 0) {
            this.getChildAt(0).measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
         }

      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      switch(var1.getAction()) {
      case 0:
         this.mCurrPercentage = 0.0F;
         this.mDownEvent = MotionEvent.obtain(var1);
         this.mPrevY = this.mDownEvent.getY();
         return false;
      case 1:
      case 3:
         if (this.mDownEvent != null) {
            this.mDownEvent.recycle();
            this.mDownEvent = null;
            return false;
         }
         break;
      case 2:
         if (this.mDownEvent != null && !this.mReturningToStart) {
            float var4 = var1.getY();
            float var3 = var4 - this.mDownEvent.getY();
            if (var3 > (float)this.mTouchSlop) {
               if (var3 > this.mDistanceToTriggerSync) {
                  this.startRefresh();
                  return true;
               }

               this.setTriggerPercentage(this.mAccelerateInterpolator.getInterpolation(var3 / this.mDistanceToTriggerSync));
               float var2 = var3;
               if (this.mPrevY > var4) {
                  var2 = var3 - (float)this.mTouchSlop;
               }

               this.updateContentOffsetTop((int)var2);
               if (this.mPrevY > var4 && this.mTarget.getTop() < this.mTouchSlop) {
                  this.removeCallbacks(this.mCancel);
               } else {
                  this.updatePositionTimeout();
               }

               this.mPrevY = var1.getY();
               return true;
            }
         }
      }

      return false;
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
   }

   public void setColorScheme(int var1, int var2, int var3, int var4) {
      this.ensureTarget();
      Resources var5 = this.getResources();
      var1 = var5.getColor(var1);
      var2 = var5.getColor(var2);
      var3 = var5.getColor(var3);
      var4 = var5.getColor(var4);
      this.mProgressBar.setColorScheme(var1, var2, var3, var4);
   }

   public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener var1) {
      this.mListener = var1;
   }

   public void setRefreshing(boolean var1) {
      if (this.mRefreshing != var1) {
         this.ensureTarget();
         this.mCurrPercentage = 0.0F;
         this.mRefreshing = var1;
         if (!this.mRefreshing) {
            this.mProgressBar.stop();
            return;
         }

         this.mProgressBar.start();
      }

   }

   private class BaseAnimationListener implements AnimationListener {
      private BaseAnimationListener() {
      }

      // $FF: synthetic method
      BaseAnimationListener(Object var2) {
         this();
      }

      public void onAnimationEnd(Animation var1) {
      }

      public void onAnimationRepeat(Animation var1) {
      }

      public void onAnimationStart(Animation var1) {
      }
   }

   public interface OnRefreshListener {
      void onRefresh();
   }
}
