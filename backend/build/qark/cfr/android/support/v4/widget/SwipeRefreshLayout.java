/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.Transformation
 *  android.widget.AbsListView
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 */
package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeProgressBar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

public class SwipeRefreshLayout
extends ViewGroup {
    private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    private static final int[] LAYOUT_ATTRS = new int[]{16842766};
    private static final float MAX_SWIPE_DISTANCE_FACTOR = 0.6f;
    private static final float PROGRESS_BAR_HEIGHT = 4.0f;
    private static final int REFRESH_TRIGGER_DISTANCE = 120;
    private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 300L;
    private final AccelerateInterpolator mAccelerateInterpolator;
    private final Animation mAnimateToStartPosition;
    private final Runnable mCancel;
    private float mCurrPercentage = 0.0f;
    private int mCurrentTargetOffsetTop;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private float mDistanceToTriggerSync = -1.0f;
    private MotionEvent mDownEvent;
    private int mFrom;
    private float mFromPercentage = 0.0f;
    private OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private int mOriginalOffsetTop;
    private float mPrevY;
    private SwipeProgressBar mProgressBar;
    private int mProgressBarHeight;
    private boolean mRefreshing = false;
    private final Runnable mReturnToStartPosition;
    private final Animation.AnimationListener mReturnToStartPositionListener;
    private boolean mReturningToStart;
    private final Animation.AnimationListener mShrinkAnimationListener;
    private Animation mShrinkTrigger;
    private View mTarget;
    private int mTouchSlop;

    public SwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAnimateToStartPosition = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                int n = 0;
                if (SwipeRefreshLayout.this.mFrom != SwipeRefreshLayout.this.mOriginalOffsetTop) {
                    n = SwipeRefreshLayout.this.mFrom + (int)((float)(SwipeRefreshLayout.this.mOriginalOffsetTop - SwipeRefreshLayout.this.mFrom) * f);
                }
                int n2 = n - SwipeRefreshLayout.this.mTarget.getTop();
                int n3 = SwipeRefreshLayout.this.mTarget.getTop();
                n = n2;
                if (n2 + n3 < 0) {
                    n = 0 - n3;
                }
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(n);
            }
        };
        this.mShrinkTrigger = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                float f2 = SwipeRefreshLayout.this.mFromPercentage;
                float f3 = SwipeRefreshLayout.this.mFromPercentage;
                SwipeRefreshLayout.this.mProgressBar.setTriggerPercentage(f2 + (0.0f - f3) * f);
            }
        };
        this.mReturnToStartPositionListener = new BaseAnimationListener(){

            @Override
            public void onAnimationEnd(Animation animation) {
                SwipeRefreshLayout.this.mCurrentTargetOffsetTop = 0;
            }
        };
        this.mShrinkAnimationListener = new BaseAnimationListener(){

            @Override
            public void onAnimationEnd(Animation animation) {
                SwipeRefreshLayout.this.mCurrPercentage = 0.0f;
            }
        };
        this.mReturnToStartPosition = new Runnable(){

            public void run() {
                SwipeRefreshLayout.this.mReturningToStart = true;
                SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
            }
        };
        this.mCancel = new Runnable(){

            public void run() {
                SwipeRefreshLayout.this.mReturningToStart = true;
                if (SwipeRefreshLayout.this.mProgressBar != null) {
                    SwipeRefreshLayout.this.mFromPercentage = SwipeRefreshLayout.this.mCurrPercentage;
                    SwipeRefreshLayout.this.mShrinkTrigger.setDuration((long)SwipeRefreshLayout.this.mMediumAnimationDuration);
                    SwipeRefreshLayout.this.mShrinkTrigger.setAnimationListener(SwipeRefreshLayout.this.mShrinkAnimationListener);
                    SwipeRefreshLayout.this.mShrinkTrigger.reset();
                    SwipeRefreshLayout.this.mShrinkTrigger.setInterpolator((Interpolator)SwipeRefreshLayout.this.mDecelerateInterpolator);
                    SwipeRefreshLayout.this.startAnimation(SwipeRefreshLayout.this.mShrinkTrigger);
                }
                SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
            }
        };
        this.mTouchSlop = ViewConfiguration.get((Context)context).getScaledTouchSlop();
        this.mMediumAnimationDuration = this.getResources().getInteger(17694721);
        this.setWillNotDraw(false);
        this.mProgressBar = new SwipeProgressBar((View)this);
        this.mProgressBarHeight = (int)(this.getResources().getDisplayMetrics().density * 4.0f);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        this.mAccelerateInterpolator = new AccelerateInterpolator(1.5f);
        context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
        this.setEnabled(context.getBoolean(0, true));
        context.recycle();
    }

    private void animateOffsetToStartPosition(int n, Animation.AnimationListener animationListener) {
        this.mFrom = n;
        this.mAnimateToStartPosition.reset();
        this.mAnimateToStartPosition.setDuration((long)this.mMediumAnimationDuration);
        this.mAnimateToStartPosition.setAnimationListener(animationListener);
        this.mAnimateToStartPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
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
        if (this.mDistanceToTriggerSync == -1.0f && this.getParent() != null && ((View)this.getParent()).getHeight() > 0) {
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            this.mDistanceToTriggerSync = (int)Math.min((float)((float)((View)this.getParent()).getHeight() * 0.6f), (float)(120.0f * displayMetrics.density));
        }
    }

    private void setTargetOffsetTopAndBottom(int n) {
        this.mTarget.offsetTopAndBottom(n);
        this.mCurrentTargetOffsetTop = this.mTarget.getTop();
    }

    private void setTriggerPercentage(float f) {
        if (f == 0.0f) {
            this.mCurrPercentage = 0.0f;
            return;
        }
        this.mCurrPercentage = f;
        this.mProgressBar.setTriggerPercentage(f);
    }

    private void startRefresh() {
        this.removeCallbacks(this.mCancel);
        this.mReturnToStartPosition.run();
        this.setRefreshing(true);
        this.mListener.onRefresh();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateContentOffsetTop(int n) {
        int n2;
        int n3 = this.mTarget.getTop();
        if ((float)n > this.mDistanceToTriggerSync) {
            n2 = (int)this.mDistanceToTriggerSync;
        } else {
            n2 = n;
            if (n < 0) {
                n2 = 0;
            }
        }
        this.setTargetOffsetTopAndBottom(n2 - n3);
    }

    private void updatePositionTimeout() {
        this.removeCallbacks(this.mCancel);
        this.postDelayed(this.mCancel, 300L);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.mTarget, -1);
        }
        if (this.mTarget instanceof AbsListView) {
            AbsListView absListView = (AbsListView)this.mTarget;
            if (absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop())) return true;
            return false;
        }
        if (this.mTarget.getScrollY() <= 0) return false;
        return true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mProgressBar.draw(canvas);
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

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.ensureTarget();
        boolean bl = false;
        if (this.mReturningToStart && motionEvent.getAction() == 0) {
            this.mReturningToStart = false;
        }
        boolean bl2 = bl;
        if (this.isEnabled()) {
            bl2 = bl;
            if (!this.mReturningToStart) {
                bl2 = bl;
                if (!this.canChildScrollUp()) {
                    bl2 = this.onTouchEvent(motionEvent);
                }
            }
        }
        bl = bl2;
        if (!bl2) {
            bl = super.onInterceptTouchEvent(motionEvent);
        }
        return bl;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n = this.getMeasuredWidth();
        n2 = this.getMeasuredHeight();
        this.mProgressBar.setBounds(0, 0, n, this.mProgressBarHeight);
        if (this.getChildCount() == 0) {
            return;
        }
        View view = this.getChildAt(0);
        n3 = this.getPaddingLeft();
        n4 = this.mCurrentTargetOffsetTop + this.getPaddingTop();
        view.layout(n3, n4, n3 + (n - this.getPaddingLeft() - this.getPaddingRight()), n4 + (n2 - this.getPaddingTop() - this.getPaddingBottom()));
    }

    public void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (this.getChildCount() > 1 && !this.isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        }
        if (this.getChildCount() > 0) {
            this.getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec((int)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight()), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom()), (int)1073741824));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            default: {
                return false;
            }
            case 0: {
                this.mCurrPercentage = 0.0f;
                this.mDownEvent = MotionEvent.obtain((MotionEvent)motionEvent);
                this.mPrevY = this.mDownEvent.getY();
                return false;
            }
            case 2: {
                if (this.mDownEvent == null) return false;
                if (this.mReturningToStart) return false;
                float f = motionEvent.getY();
                float f2 = f - this.mDownEvent.getY();
                if (f2 <= (float)this.mTouchSlop) return false;
                if (f2 > this.mDistanceToTriggerSync) {
                    this.startRefresh();
                    return true;
                }
                this.setTriggerPercentage(this.mAccelerateInterpolator.getInterpolation(f2 / this.mDistanceToTriggerSync));
                float f3 = f2;
                if (this.mPrevY > f) {
                    f3 = f2 - (float)this.mTouchSlop;
                }
                this.updateContentOffsetTop((int)f3);
                if (this.mPrevY > f && this.mTarget.getTop() < this.mTouchSlop) {
                    this.removeCallbacks(this.mCancel);
                } else {
                    this.updatePositionTimeout();
                }
                this.mPrevY = motionEvent.getY();
                return true;
            }
            case 1: 
            case 3: 
        }
        if (this.mDownEvent == null) return false;
        this.mDownEvent.recycle();
        this.mDownEvent = null;
        return false;
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
    }

    public void setColorScheme(int n, int n2, int n3, int n4) {
        this.ensureTarget();
        Resources resources = this.getResources();
        n = resources.getColor(n);
        n2 = resources.getColor(n2);
        n3 = resources.getColor(n3);
        n4 = resources.getColor(n4);
        this.mProgressBar.setColorScheme(n, n2, n3, n4);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mListener = onRefreshListener;
    }

    public void setRefreshing(boolean bl) {
        block3 : {
            block2 : {
                if (this.mRefreshing == bl) break block2;
                this.ensureTarget();
                this.mCurrPercentage = 0.0f;
                this.mRefreshing = bl;
                if (!this.mRefreshing) break block3;
                this.mProgressBar.start();
            }
            return;
        }
        this.mProgressBar.stop();
    }

    private class BaseAnimationListener
    implements Animation.AnimationListener {
        private BaseAnimationListener() {
        }

        public void onAnimationEnd(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public static interface OnRefreshListener {
        public void onRefresh();
    }

}

