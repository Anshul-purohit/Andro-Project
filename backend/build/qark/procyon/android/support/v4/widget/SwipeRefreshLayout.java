// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.content.res.Resources;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.widget.AbsListView;
import android.os.Build$VERSION;
import android.content.res.TypedArray;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation$AnimationListener;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AccelerateInterpolator;
import android.view.ViewGroup;

public class SwipeRefreshLayout extends ViewGroup
{
    private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    private static final int[] LAYOUT_ATTRS;
    private static final float MAX_SWIPE_DISTANCE_FACTOR = 0.6f;
    private static final float PROGRESS_BAR_HEIGHT = 4.0f;
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
    private OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private int mOriginalOffsetTop;
    private float mPrevY;
    private SwipeProgressBar mProgressBar;
    private int mProgressBarHeight;
    private boolean mRefreshing;
    private final Runnable mReturnToStartPosition;
    private final Animation$AnimationListener mReturnToStartPositionListener;
    private boolean mReturningToStart;
    private final Animation$AnimationListener mShrinkAnimationListener;
    private Animation mShrinkTrigger;
    private View mTarget;
    private int mTouchSlop;
    
    static {
        LAYOUT_ATTRS = new int[] { 16842766 };
    }
    
    public SwipeRefreshLayout(final Context context) {
        this(context, null);
    }
    
    public SwipeRefreshLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRefreshing = false;
        this.mDistanceToTriggerSync = -1.0f;
        this.mFromPercentage = 0.0f;
        this.mCurrPercentage = 0.0f;
        this.mAnimateToStartPosition = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                int n2 = 0;
                if (SwipeRefreshLayout.this.mFrom != SwipeRefreshLayout.this.mOriginalOffsetTop) {
                    n2 = SwipeRefreshLayout.this.mFrom + (int)((SwipeRefreshLayout.this.mOriginalOffsetTop - SwipeRefreshLayout.this.mFrom) * n);
                }
                final int n3 = n2 - SwipeRefreshLayout.this.mTarget.getTop();
                final int top = SwipeRefreshLayout.this.mTarget.getTop();
                int n4 = n3;
                if (n3 + top < 0) {
                    n4 = 0 - top;
                }
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(n4);
            }
        };
        this.mShrinkTrigger = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                SwipeRefreshLayout.this.mProgressBar.setTriggerPercentage(SwipeRefreshLayout.this.mFromPercentage + (0.0f - SwipeRefreshLayout.this.mFromPercentage) * n);
            }
        };
        this.mReturnToStartPositionListener = (Animation$AnimationListener)new BaseAnimationListener() {
            @Override
            public void onAnimationEnd(final Animation animation) {
                SwipeRefreshLayout.this.mCurrentTargetOffsetTop = 0;
            }
        };
        this.mShrinkAnimationListener = (Animation$AnimationListener)new BaseAnimationListener() {
            @Override
            public void onAnimationEnd(final Animation animation) {
                SwipeRefreshLayout.this.mCurrPercentage = 0.0f;
            }
        };
        this.mReturnToStartPosition = new Runnable() {
            @Override
            public void run() {
                SwipeRefreshLayout.this.mReturningToStart = true;
                SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
            }
        };
        this.mCancel = new Runnable() {
            @Override
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
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mMediumAnimationDuration = this.getResources().getInteger(17694721);
        this.setWillNotDraw(false);
        this.mProgressBar = new SwipeProgressBar((View)this);
        this.mProgressBarHeight = (int)(this.getResources().getDisplayMetrics().density * 4.0f);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        this.mAccelerateInterpolator = new AccelerateInterpolator(1.5f);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, SwipeRefreshLayout.LAYOUT_ATTRS);
        this.setEnabled(obtainStyledAttributes.getBoolean(0, true));
        obtainStyledAttributes.recycle();
    }
    
    private void animateOffsetToStartPosition(final int mFrom, final Animation$AnimationListener animationListener) {
        this.mFrom = mFrom;
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
            this.mDistanceToTriggerSync = (float)(int)Math.min(((View)this.getParent()).getHeight() * 0.6f, 120.0f * this.getResources().getDisplayMetrics().density);
        }
    }
    
    private void setTargetOffsetTopAndBottom(final int n) {
        this.mTarget.offsetTopAndBottom(n);
        this.mCurrentTargetOffsetTop = this.mTarget.getTop();
    }
    
    private void setTriggerPercentage(final float n) {
        if (n == 0.0f) {
            this.mCurrPercentage = 0.0f;
            return;
        }
        this.mCurrPercentage = n;
        this.mProgressBar.setTriggerPercentage(n);
    }
    
    private void startRefresh() {
        this.removeCallbacks(this.mCancel);
        this.mReturnToStartPosition.run();
        this.setRefreshing(true);
        this.mListener.onRefresh();
    }
    
    private void updateContentOffsetTop(final int n) {
        final int top = this.mTarget.getTop();
        int n2;
        if (n > this.mDistanceToTriggerSync) {
            n2 = (int)this.mDistanceToTriggerSync;
        }
        else if ((n2 = n) < 0) {
            n2 = 0;
        }
        this.setTargetOffsetTopAndBottom(n2 - top);
    }
    
    private void updatePositionTimeout() {
        this.removeCallbacks(this.mCancel);
        this.postDelayed(this.mCancel, 300L);
    }
    
    public boolean canChildScrollUp() {
        if (Build$VERSION.SDK_INT < 14) {
            if (this.mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView)this.mTarget;
                if (absListView.getChildCount() <= 0 || (absListView.getFirstVisiblePosition() <= 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop())) {
                    return false;
                }
            }
            else if (this.mTarget.getScrollY() <= 0) {
                return false;
            }
            return true;
        }
        return ViewCompat.canScrollVertically(this.mTarget, -1);
    }
    
    public void draw(final Canvas canvas) {
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
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        this.ensureTarget();
        final boolean b = false;
        if (this.mReturningToStart && motionEvent.getAction() == 0) {
            this.mReturningToStart = false;
        }
        boolean onTouchEvent = b;
        if (this.isEnabled()) {
            onTouchEvent = b;
            if (!this.mReturningToStart) {
                onTouchEvent = b;
                if (!this.canChildScrollUp()) {
                    onTouchEvent = this.onTouchEvent(motionEvent);
                }
            }
        }
        boolean onInterceptTouchEvent;
        if (!(onInterceptTouchEvent = onTouchEvent)) {
            onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        }
        return onInterceptTouchEvent;
    }
    
    protected void onLayout(final boolean b, int measuredWidth, int measuredHeight, int paddingLeft, int n) {
        measuredWidth = this.getMeasuredWidth();
        measuredHeight = this.getMeasuredHeight();
        this.mProgressBar.setBounds(0, 0, measuredWidth, this.mProgressBarHeight);
        if (this.getChildCount() == 0) {
            return;
        }
        final View child = this.getChildAt(0);
        paddingLeft = this.getPaddingLeft();
        n = this.mCurrentTargetOffsetTop + this.getPaddingTop();
        child.layout(paddingLeft, n, paddingLeft + (measuredWidth - this.getPaddingLeft() - this.getPaddingRight()), n + (measuredHeight - this.getPaddingTop() - this.getPaddingBottom()));
    }
    
    public void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (this.getChildCount() > 1 && !this.isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        }
        if (this.getChildCount() > 0) {
            this.getChildAt(0).measure(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0: {
                this.mCurrPercentage = 0.0f;
                this.mDownEvent = MotionEvent.obtain(motionEvent);
                this.mPrevY = this.mDownEvent.getY();
                return false;
            }
            case 2: {
                if (this.mDownEvent == null || this.mReturningToStart) {
                    break;
                }
                final float y = motionEvent.getY();
                final float n = y - this.mDownEvent.getY();
                if (n <= this.mTouchSlop) {
                    break;
                }
                if (n > this.mDistanceToTriggerSync) {
                    this.startRefresh();
                    return true;
                }
                this.setTriggerPercentage(this.mAccelerateInterpolator.getInterpolation(n / this.mDistanceToTriggerSync));
                float n2 = n;
                if (this.mPrevY > y) {
                    n2 = n - this.mTouchSlop;
                }
                this.updateContentOffsetTop((int)n2);
                if (this.mPrevY > y && this.mTarget.getTop() < this.mTouchSlop) {
                    this.removeCallbacks(this.mCancel);
                }
                else {
                    this.updatePositionTimeout();
                }
                this.mPrevY = motionEvent.getY();
                return true;
            }
            case 1:
            case 3: {
                if (this.mDownEvent != null) {
                    this.mDownEvent.recycle();
                    this.mDownEvent = null;
                    return false;
                }
                break;
            }
        }
        return false;
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
    }
    
    public void setColorScheme(int color, int color2, int color3, int color4) {
        this.ensureTarget();
        final Resources resources = this.getResources();
        color = resources.getColor(color);
        color2 = resources.getColor(color2);
        color3 = resources.getColor(color3);
        color4 = resources.getColor(color4);
        this.mProgressBar.setColorScheme(color, color2, color3, color4);
    }
    
    public void setOnRefreshListener(final OnRefreshListener mListener) {
        this.mListener = mListener;
    }
    
    public void setRefreshing(final boolean mRefreshing) {
        if (this.mRefreshing != mRefreshing) {
            this.ensureTarget();
            this.mCurrPercentage = 0.0f;
            this.mRefreshing = mRefreshing;
            if (!this.mRefreshing) {
                this.mProgressBar.stop();
                return;
            }
            this.mProgressBar.start();
        }
    }
    
    private class BaseAnimationListener implements Animation$AnimationListener
    {
        public void onAnimationEnd(final Animation animation) {
        }
        
        public void onAnimationRepeat(final Animation animation) {
        }
        
        public void onAnimationStart(final Animation animation) {
        }
    }
    
    public interface OnRefreshListener
    {
        void onRefresh();
    }
}
