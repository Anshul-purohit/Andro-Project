/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.List
 */
package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class DrawerLayout
extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int[] LAYOUT_ATTRS = new int[]{16842931};
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private int mDrawerState;
    private boolean mFirstLayout = true;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mMinDrawerMargin;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor = -1728053248;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        float f = this.getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int)(64.0f * f + 0.5f);
        f = 400.0f * f;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        this.mLeftDragger = ViewDragHelper.create(this, 1.0f, this.mLeftCallback);
        this.mLeftDragger.setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(f);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = ViewDragHelper.create(this, 1.0f, this.mRightCallback);
        this.mRightDragger.setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(f);
        this.mRightCallback.setDragger(this.mRightDragger);
        this.setFocusableInTouchMode(true);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
    }

    private View findVisibleDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (!this.isDrawerView(view) || !this.isDrawerVisible(view)) continue;
            return view;
        }
        return null;
    }

    static String gravityToString(int n) {
        if ((n & 3) == 3) {
            return "LEFT";
        }
        if ((n & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString((int)n);
    }

    private static boolean hasOpaqueBackground(View view) {
        boolean bl = false;
        view = view.getBackground();
        boolean bl2 = bl;
        if (view != null) {
            bl2 = bl;
            if (view.getOpacity() == -1) {
                bl2 = true;
            }
        }
        return bl2;
    }

    private boolean hasPeekingDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            if (!((LayoutParams)this.getChildAt((int)i).getLayoutParams()).isPeeking) continue;
            return true;
        }
        return false;
    }

    private boolean hasVisibleDrawer() {
        if (this.findVisibleDrawer() != null) {
            return true;
        }
        return false;
    }

    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                this.getChildAt(i).dispatchTouchEvent(motionEvent);
            }
            motionEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    boolean checkDrawerViewAbsoluteGravity(View view, int n) {
        if ((this.getDrawerViewAbsoluteGravity(view) & n) == n) {
            return true;
        }
        return false;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams)) {
            return true;
        }
        return false;
    }

    public void closeDrawer(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + DrawerLayout.gravityToString(n));
        }
        this.closeDrawer(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void closeDrawer(View object) {
        if (!this.isDrawerView((View)object)) {
            throw new IllegalArgumentException("View " + object + " is not a sliding drawer");
        }
        if (this.mFirstLayout) {
            object = (LayoutParams)object.getLayoutParams();
            object.onScreen = 0.0f;
            object.knownOpen = false;
        } else if (this.checkDrawerViewAbsoluteGravity((View)object, 3)) {
            this.mLeftDragger.smoothSlideViewTo((View)object, - object.getWidth(), object.getTop());
        } else {
            this.mRightDragger.smoothSlideViewTo((View)object, this.getWidth(), object.getTop());
        }
        this.invalidate();
    }

    public void closeDrawers() {
        this.closeDrawers(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    void closeDrawers(boolean bl) {
        int n = 0;
        int n2 = this.getChildCount();
        for (int i = 0; i < n2; ++i) {
            View view = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n3 = n;
            if (this.isDrawerView(view)) {
                if (bl && !layoutParams.isPeeking) {
                    n3 = n;
                } else {
                    n3 = view.getWidth();
                    n = this.checkDrawerViewAbsoluteGravity(view, 3) ? (n |= this.mLeftDragger.smoothSlideViewTo(view, - n3, view.getTop())) : (n |= this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop()));
                    layoutParams.isPeeking = false;
                    n3 = n;
                }
            }
            n = n3;
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (n != 0) {
            this.invalidate();
        }
    }

    public void computeScroll() {
        int n = this.getChildCount();
        float f = 0.0f;
        for (int i = 0; i < n; ++i) {
            f = Math.max((float)f, (float)((LayoutParams)this.getChildAt((int)i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f;
        if (this.mLeftDragger.continueSettling(true) | this.mRightDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    void dispatchOnDrawerClosed(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.knownOpen) {
            layoutParams.knownOpen = false;
            if (this.mListener != null) {
                this.mListener.onDrawerClosed(view);
            }
            if (this.hasWindowFocus() && (view = this.getRootView()) != null) {
                view.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerOpened(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.knownOpen) {
            layoutParams.knownOpen = true;
            if (this.mListener != null) {
                this.mListener.onDrawerOpened(view);
            }
            this.sendAccessibilityEvent(32);
        }
    }

    void dispatchOnDrawerSlide(View view, float f) {
        if (this.mListener != null) {
            this.mListener.onDrawerSlide(view, f);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected boolean drawChild(Canvas canvas, View view, long l) {
        int n = this.getHeight();
        boolean bl = this.isContentView(view);
        int n2 = 0;
        int n3 = 0;
        int n4 = this.getWidth();
        int n5 = canvas.save();
        int n6 = n4;
        if (bl) {
            int n7 = this.getChildCount();
            n2 = n3;
            for (n6 = 0; n6 < n7; ++n6) {
                View view2 = this.getChildAt(n6);
                n3 = n2;
                int n8 = n4;
                if (view2 != view) {
                    n3 = n2;
                    n8 = n4;
                    if (view2.getVisibility() == 0) {
                        n3 = n2;
                        n8 = n4;
                        if (DrawerLayout.hasOpaqueBackground(view2)) {
                            n3 = n2;
                            n8 = n4;
                            if (this.isDrawerView(view2)) {
                                int n9;
                                if (view2.getHeight() < n) {
                                    n8 = n4;
                                    n3 = n2;
                                } else if (this.checkDrawerViewAbsoluteGravity(view2, 3)) {
                                    n9 = view2.getRight();
                                    n3 = n2;
                                    n8 = n4;
                                    if (n9 > n2) {
                                        n3 = n9;
                                        n8 = n4;
                                    }
                                } else {
                                    n9 = view2.getLeft();
                                    n3 = n2;
                                    n8 = n4;
                                    if (n9 < n4) {
                                        n8 = n9;
                                        n3 = n2;
                                    }
                                }
                            }
                        }
                    }
                }
                n2 = n3;
                n4 = n8;
            }
            canvas.clipRect(n2, 0, n4, this.getHeight());
            n6 = n4;
        }
        boolean bl2 = super.drawChild(canvas, view, l);
        canvas.restoreToCount(n5);
        if (this.mScrimOpacity > 0.0f && bl) {
            n4 = (int)((float)((this.mScrimColor & -16777216) >>> 24) * this.mScrimOpacity);
            n3 = this.mScrimColor;
            this.mScrimPaint.setColor(n4 << 24 | n3 & 16777215);
            canvas.drawRect((float)n2, 0.0f, (float)n6, (float)this.getHeight(), this.mScrimPaint);
            return bl2;
        } else {
            if (this.mShadowLeft != null && this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n2 = this.mShadowLeft.getIntrinsicWidth();
                n4 = view.getRight();
                n6 = this.mLeftDragger.getEdgeSize();
                float f = Math.max((float)0.0f, (float)Math.min((float)((float)n4 / (float)n6), (float)1.0f));
                this.mShadowLeft.setBounds(n4, view.getTop(), n4 + n2, view.getBottom());
                this.mShadowLeft.setAlpha((int)(255.0f * f));
                this.mShadowLeft.draw(canvas);
                return bl2;
            }
            if (this.mShadowRight == null || !this.checkDrawerViewAbsoluteGravity(view, 5)) return bl2;
            {
                n2 = this.mShadowRight.getIntrinsicWidth();
                n4 = view.getLeft();
                n6 = this.getWidth();
                n3 = this.mRightDragger.getEdgeSize();
                float f = Math.max((float)0.0f, (float)Math.min((float)((float)(n6 - n4) / (float)n3), (float)1.0f));
                this.mShadowRight.setBounds(n4 - n2, view.getTop(), n4, view.getBottom());
                this.mShadowRight.setAlpha((int)(255.0f * f));
                this.mShadowRight.draw(canvas);
                return bl2;
            }
        }
    }

    View findDrawerWithGravity(int n) {
        int n2 = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this));
        int n3 = this.getChildCount();
        for (n = 0; n < n3; ++n) {
            View view = this.getChildAt(n);
            if ((this.getDrawerViewAbsoluteGravity(view) & 7) != (n2 & 7)) continue;
            return view;
        }
        return null;
    }

    View findOpenDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (!((LayoutParams)view.getLayoutParams()).knownOpen) continue;
            return view;
        }
        return null;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public int getDrawerLockMode(int n) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            return this.mLockModeLeft;
        }
        if (n == 5) {
            return this.mLockModeRight;
        }
        return 0;
    }

    public int getDrawerLockMode(View view) {
        int n = this.getDrawerViewAbsoluteGravity(view);
        if (n == 3) {
            return this.mLockModeLeft;
        }
        if (n == 5) {
            return this.mLockModeRight;
        }
        return 0;
    }

    public CharSequence getDrawerTitle(int n) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            return this.mTitleLeft;
        }
        if (n == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    int getDrawerViewAbsoluteGravity(View view) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
    }

    float getDrawerViewOffset(View view) {
        return ((LayoutParams)view.getLayoutParams()).onScreen;
    }

    boolean isContentView(View view) {
        if (((LayoutParams)view.getLayoutParams()).gravity == 0) {
            return true;
        }
        return false;
    }

    public boolean isDrawerOpen(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view != null) {
            return this.isDrawerOpen(view);
        }
        return false;
    }

    public boolean isDrawerOpen(View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + (Object)view + " is not a drawer");
        }
        return ((LayoutParams)view.getLayoutParams()).knownOpen;
    }

    boolean isDrawerView(View view) {
        if ((GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view)) & 7) != 0) {
            return true;
        }
        return false;
    }

    public boolean isDrawerVisible(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view != null) {
            return this.isDrawerVisible(view);
        }
        return false;
    }

    public boolean isDrawerVisible(View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + (Object)view + " is not a drawer");
        }
        if (((LayoutParams)view.getLayoutParams()).onScreen > 0.0f) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    void moveDrawerToOffset(View view, float f) {
        float f2 = this.getDrawerViewOffset(view);
        int n = view.getWidth();
        int n2 = (int)((float)n * f2);
        n = (int)((float)n * f) - n2;
        if (!this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n = - n;
        }
        view.offsetLeftAndRight(n);
        this.setDrawerViewOffset(view, f);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean bl = false;
        int n = MotionEventCompat.getActionMasked(motionEvent);
        boolean bl2 = this.mLeftDragger.shouldInterceptTouchEvent(motionEvent);
        boolean bl3 = this.mRightDragger.shouldInterceptTouchEvent(motionEvent);
        int n2 = 0;
        int n3 = 0;
        switch (n) {
            default: {
                n = n3;
                break;
            }
            case 0: {
                float f = motionEvent.getX();
                float f2 = motionEvent.getY();
                this.mInitialMotionX = f;
                this.mInitialMotionY = f2;
                n = n2;
                if (this.mScrimOpacity > 0.0f) {
                    n = n2;
                    if (this.isContentView(this.mLeftDragger.findTopChildUnder((int)f, (int)f2))) {
                        n = 1;
                    }
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            }
            case 2: {
                n = n3;
                if (!this.mLeftDragger.checkTouchSlop(3)) break;
                this.mLeftCallback.removeCallbacks();
                this.mRightCallback.removeCallbacks();
                n = n3;
                break;
            }
            case 1: 
            case 3: {
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                n = n3;
            }
        }
        if (bl2 | bl3) return true;
        if (n != 0) return true;
        if (this.hasPeekingDrawer()) return true;
        if (!this.mChildrenCanceledTouch) return bl;
        return true;
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4 && this.hasVisibleDrawer()) {
            KeyEventCompat.startTracking(keyEvent);
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (n == 4) {
            keyEvent = this.findVisibleDrawer();
            if (keyEvent != null && this.getDrawerLockMode((View)keyEvent) == 0) {
                this.closeDrawers();
            }
            if (keyEvent != null) {
                return true;
            }
            return false;
        }
        return super.onKeyUp(n, keyEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        this.mInLayout = true;
        int n5 = n3 - n;
        int n6 = this.getChildCount();
        n3 = 0;
        do {
            if (n3 >= n6) {
                this.mInLayout = false;
                this.mFirstLayout = false;
                return;
            }
            View view = this.getChildAt(n3);
            if (view.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (this.isContentView(view)) {
                    view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
                } else {
                    float f;
                    int n7;
                    int n8 = view.getMeasuredWidth();
                    int n9 = view.getMeasuredHeight();
                    if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                        n7 = - n8 + (int)((float)n8 * layoutParams.onScreen);
                        f = (float)(n8 + n7) / (float)n8;
                    } else {
                        n7 = n5 - (int)((float)n8 * layoutParams.onScreen);
                        f = (float)(n5 - n7) / (float)n8;
                    }
                    boolean bl2 = f != layoutParams.onScreen;
                    switch (layoutParams.gravity & 112) {
                        default: {
                            view.layout(n7, layoutParams.topMargin, n7 + n8, layoutParams.topMargin + n9);
                            break;
                        }
                        case 80: {
                            n = n4 - n2;
                            view.layout(n7, n - layoutParams.bottomMargin - view.getMeasuredHeight(), n7 + n8, n - layoutParams.bottomMargin);
                            break;
                        }
                        case 16: {
                            int n10 = n4 - n2;
                            int n11 = (n10 - n9) / 2;
                            if (n11 < layoutParams.topMargin) {
                                n = layoutParams.topMargin;
                            } else {
                                n = n11;
                                if (n11 + n9 > n10 - layoutParams.bottomMargin) {
                                    n = n10 - layoutParams.bottomMargin - n9;
                                }
                            }
                            view.layout(n7, n, n7 + n8, n + n9);
                        }
                    }
                    if (bl2) {
                        this.setDrawerViewOffset(view, f);
                    }
                    n = layoutParams.onScreen > 0.0f ? 0 : 4;
                    if (view.getVisibility() != n) {
                        view.setVisibility(n);
                    }
                }
            }
            ++n3;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        block15 : {
            int n8;
            block14 : {
                n8 = View.MeasureSpec.getMode((int)n);
                n4 = View.MeasureSpec.getMode((int)n2);
                n5 = View.MeasureSpec.getSize((int)n);
                n6 = View.MeasureSpec.getSize((int)n2);
                if (n8 != 1073741824) break block14;
                n7 = n6;
                n3 = n5;
                if (n4 == 1073741824) break block15;
            }
            if (!this.isInEditMode()) {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
            if (n8 != Integer.MIN_VALUE && n8 == 0) {
                n5 = 300;
            }
            if (n4 == Integer.MIN_VALUE) {
                n3 = n5;
                n7 = n6;
            } else {
                n7 = n6;
                n3 = n5;
                if (n4 == 0) {
                    n7 = 300;
                    n3 = n5;
                }
            }
        }
        this.setMeasuredDimension(n3, n7);
        n6 = this.getChildCount();
        n5 = 0;
        while (n5 < n6) {
            View view = this.getChildAt(n5);
            if (view.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (this.isContentView(view)) {
                    view.measure(View.MeasureSpec.makeMeasureSpec((int)(n3 - layoutParams.leftMargin - layoutParams.rightMargin), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(n7 - layoutParams.topMargin - layoutParams.bottomMargin), (int)1073741824));
                } else {
                    if (!this.isDrawerView(view)) {
                        throw new IllegalStateException("Child " + (Object)view + " at index " + n5 + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY");
                    }
                    n4 = this.getDrawerViewAbsoluteGravity(view) & 7;
                    if ((0 & n4) != 0) {
                        throw new IllegalStateException("Child drawer has absolute gravity " + DrawerLayout.gravityToString(n4) + " but this " + "DrawerLayout" + " already has a " + "drawer view along that edge");
                    }
                    view.measure(DrawerLayout.getChildMeasureSpec((int)n, (int)(this.mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin), (int)layoutParams.width), DrawerLayout.getChildMeasureSpec((int)n2, (int)(layoutParams.topMargin + layoutParams.bottomMargin), (int)layoutParams.height));
                }
            }
            ++n5;
        }
    }

    protected void onRestoreInstanceState(Parcelable object) {
        View view;
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        if (object.openDrawerGravity != 0 && (view = this.findDrawerWithGravity(object.openDrawerGravity)) != null) {
            this.openDrawer(view);
        }
        this.setDrawerLockMode(object.lockModeLeft, 3);
        this.setDrawerLockMode(object.lockModeRight, 5);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            Object object = this.getChildAt(i);
            if (!this.isDrawerView((View)object)) continue;
            object = (LayoutParams)object.getLayoutParams();
            if (!object.knownOpen) continue;
            savedState.openDrawerGravity = object.gravity;
            break;
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mLeftDragger.processTouchEvent(motionEvent);
        this.mRightDragger.processTouchEvent(motionEvent);
        switch (motionEvent.getAction() & 255) {
            default: {
                return true;
            }
            case 0: {
                float f = motionEvent.getX();
                float f2 = motionEvent.getY();
                this.mInitialMotionX = f;
                this.mInitialMotionY = f2;
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                return true;
            }
            case 1: {
                float f = motionEvent.getX();
                float f3 = motionEvent.getY();
                boolean bl = true;
                motionEvent = this.mLeftDragger.findTopChildUnder((int)f, (int)f3);
                boolean bl2 = bl;
                if (motionEvent != null) {
                    bl2 = bl;
                    if (this.isContentView((View)motionEvent)) {
                        int n = this.mLeftDragger.getTouchSlop();
                        bl2 = bl;
                        if (f * (f -= this.mInitialMotionX) + f3 * (f3 -= this.mInitialMotionY) < (float)(n * n)) {
                            motionEvent = this.findOpenDrawer();
                            bl2 = bl;
                            if (motionEvent != null) {
                                bl2 = this.getDrawerLockMode((View)motionEvent) == 2;
                            }
                        }
                    }
                }
                this.closeDrawers(bl2);
                this.mDisallowInterceptRequested = false;
                return true;
            }
            case 3: 
        }
        this.closeDrawers(true);
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        return true;
    }

    public void openDrawer(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + DrawerLayout.gravityToString(n));
        }
        this.openDrawer(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void openDrawer(View object) {
        if (!this.isDrawerView((View)object)) {
            throw new IllegalArgumentException("View " + object + " is not a sliding drawer");
        }
        if (this.mFirstLayout) {
            object = (LayoutParams)object.getLayoutParams();
            object.onScreen = 1.0f;
            object.knownOpen = true;
        } else if (this.checkDrawerViewAbsoluteGravity((View)object, 3)) {
            this.mLeftDragger.smoothSlideViewTo((View)object, 0, object.getTop());
        } else {
            this.mRightDragger.smoothSlideViewTo((View)object, this.getWidth() - object.getWidth(), object.getTop());
        }
        this.invalidate();
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        super.requestDisallowInterceptTouchEvent(bl);
        this.mDisallowInterceptRequested = bl;
        if (bl) {
            this.closeDrawers(true);
        }
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.mListener = drawerListener;
    }

    public void setDrawerLockMode(int n) {
        this.setDrawerLockMode(n, 3);
        this.setDrawerLockMode(n, 5);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDrawerLockMode(int n, int n2) {
        ViewDragHelper viewDragHelper;
        if ((n2 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this))) == 3) {
            this.mLockModeLeft = n;
        } else if (n2 == 5) {
            this.mLockModeRight = n;
        }
        if (n != 0) {
            viewDragHelper = n2 == 3 ? this.mLeftDragger : this.mRightDragger;
            viewDragHelper.cancel();
        }
        switch (n) {
            case 2: {
                viewDragHelper = this.findDrawerWithGravity(n2);
                if (viewDragHelper == null) return;
                {
                    this.openDrawer((View)viewDragHelper);
                    return;
                }
            }
            default: {
                return;
            }
            case 1: {
                viewDragHelper = this.findDrawerWithGravity(n2);
                if (viewDragHelper == null) return;
                this.closeDrawer((View)viewDragHelper);
                return;
            }
        }
    }

    public void setDrawerLockMode(int n, View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + (Object)view + " is not a " + "drawer with appropriate layout_gravity");
        }
        this.setDrawerLockMode(n, ((LayoutParams)view.getLayoutParams()).gravity);
    }

    public void setDrawerShadow(int n, int n2) {
        this.setDrawerShadow(this.getResources().getDrawable(n), n2);
    }

    public void setDrawerShadow(Drawable drawable2, int n) {
        if (((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) & 3) == 3) {
            this.mShadowLeft = drawable2;
            this.invalidate();
        }
        if ((n & 5) == 5) {
            this.mShadowRight = drawable2;
            this.invalidate();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDrawerTitle(int n, CharSequence charSequence) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            this.mTitleLeft = charSequence;
            return;
        } else {
            if (n != 5) return;
            {
                this.mTitleRight = charSequence;
                return;
            }
        }
    }

    void setDrawerViewOffset(View view, float f) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (f == layoutParams.onScreen) {
            return;
        }
        layoutParams.onScreen = f;
        this.dispatchOnDrawerSlide(view, f);
    }

    public void setScrimColor(int n) {
        this.mScrimColor = n;
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    void updateDrawerState(int n, int n2, View view) {
        n = this.mLeftDragger.getViewDragState();
        int n3 = this.mRightDragger.getViewDragState();
        n = n == 1 || n3 == 1 ? 1 : (n == 2 || n3 == 2 ? 2 : 0);
        if (view != null && n2 == 0) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                this.dispatchOnDrawerClosed(view);
            } else if (layoutParams.onScreen == 1.0f) {
                this.dispatchOnDrawerOpened(view);
            }
        }
        if (n != this.mDrawerState) {
            this.mDrawerState = n;
            if (this.mListener != null) {
                this.mListener.onDrawerStateChanged(n);
            }
        }
    }

    class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;

        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }

        /*
         * Enabled aggressive block sorting
         */
        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ViewGroup viewGroup) {
            int n = viewGroup.getChildCount();
            int n2 = 0;
            while (n2 < n) {
                View view = viewGroup.getChildAt(n2);
                if (!this.filter(view)) {
                    switch (ViewCompat.getImportantForAccessibility(view)) {
                        case 4: {
                            break;
                        }
                        default: {
                            break;
                        }
                        case 0: {
                            ViewCompat.setImportantForAccessibility(view, 1);
                        }
                        case 1: {
                            accessibilityNodeInfoCompat.addChild(view);
                            break;
                        }
                        case 2: {
                            if (!(view instanceof ViewGroup)) break;
                            this.addChildrenForAccessibility(accessibilityNodeInfoCompat, (ViewGroup)view);
                        }
                    }
                }
                ++n2;
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() == 32) {
                view = accessibilityEvent.getText();
                accessibilityEvent = DrawerLayout.this.findVisibleDrawer();
                if (accessibilityEvent != null) {
                    int n = DrawerLayout.this.getDrawerViewAbsoluteGravity((View)accessibilityEvent);
                    accessibilityEvent = DrawerLayout.this.getDrawerTitle(n);
                    if (accessibilityEvent != null) {
                        view.add((Object)accessibilityEvent);
                    }
                }
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        public boolean filter(View view) {
            View view2 = DrawerLayout.this.findOpenDrawer();
            if (view2 != null && view2 != view) {
                return true;
            }
            return false;
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)DrawerLayout.class.getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat2);
            accessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
            accessibilityNodeInfoCompat.setSource(view);
            ViewParent viewParent = ViewCompat.getParentForAccessibility(view);
            if (viewParent instanceof View) {
                accessibilityNodeInfoCompat.setParent((View)viewParent);
            }
            this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, accessibilityNodeInfoCompat2);
            accessibilityNodeInfoCompat2.recycle();
            this.addChildrenForAccessibility(accessibilityNodeInfoCompat, (ViewGroup)view);
        }

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (!this.filter(view)) {
                return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return false;
        }
    }

    public static interface DrawerListener {
        public void onDrawerClosed(View var1);

        public void onDrawerOpened(View var1);

        public void onDrawerSlide(View var1, float var2);

        public void onDrawerStateChanged(int var1);
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public int gravity = 0;
        boolean isPeeking;
        boolean knownOpen;
        float onScreen;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(int n, int n2, int n3) {
            this(n, n2);
            this.gravity = n3;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
            this.gravity = context.getInt(0, 0);
            context.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    protected static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int lockModeLeft = 0;
        int lockModeRight = 0;
        int openDrawerGravity = 0;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.openDrawerGravity = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.openDrawerGravity);
        }

    }

    public static abstract class SimpleDrawerListener
    implements DrawerListener {
        @Override
        public void onDrawerClosed(View view) {
        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerSlide(View view, float f) {
        }

        @Override
        public void onDrawerStateChanged(int n) {
        }
    }

    private class ViewDragCallback
    extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable;

        public ViewDragCallback(int n) {
            this.mPeekRunnable = new Runnable(){

                public void run() {
                    ViewDragCallback.this.peekDrawer();
                }
            };
            this.mAbsGravity = n;
        }

        private void closeOtherDrawer() {
            View view;
            int n = 3;
            if (this.mAbsGravity == 3) {
                n = 5;
            }
            if ((view = DrawerLayout.this.findDrawerWithGravity(n)) != null) {
                DrawerLayout.this.closeDrawer(view);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void peekDrawer() {
            View view;
            int n = 0;
            int n2 = this.mDragger.getEdgeSize();
            boolean bl = this.mAbsGravity == 3;
            if (bl) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
                if (view != null) {
                    n = - view.getWidth();
                }
                n += n2;
            } else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
                n = DrawerLayout.this.getWidth() - n2;
            }
            if (view != null && (bl && view.getLeft() < n || !bl && view.getLeft() > n) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                this.mDragger.smoothSlideViewTo(view, n, view.getTop());
                layoutParams.isPeeking = true;
                DrawerLayout.this.invalidate();
                this.closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }

        @Override
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                return Math.max((int)(- view.getWidth()), (int)Math.min((int)n, (int)0));
            }
            n2 = DrawerLayout.this.getWidth();
            return Math.max((int)(n2 - view.getWidth()), (int)Math.min((int)n, (int)n2));
        }

        @Override
        public int clampViewPositionVertical(View view, int n, int n2) {
            return view.getTop();
        }

        @Override
        public int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onEdgeDragStarted(int n, int n2) {
            View view = (n & 1) == 1 ? DrawerLayout.this.findDrawerWithGravity(3) : DrawerLayout.this.findDrawerWithGravity(5);
            if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                this.mDragger.captureChildView(view, n2);
            }
        }

        @Override
        public boolean onEdgeLock(int n) {
            return false;
        }

        @Override
        public void onEdgeTouched(int n, int n2) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
        }

        @Override
        public void onViewCaptured(View view, int n) {
            ((LayoutParams)view.getLayoutParams()).isPeeking = false;
            this.closeOtherDrawer();
        }

        @Override
        public void onViewDragStateChanged(int n) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, n, this.mDragger.getCapturedView());
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
            n2 = view.getWidth();
            float f = DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3) ? (float)(n2 + n) / (float)n2 : (float)(DrawerLayout.this.getWidth() - n) / (float)n2;
            DrawerLayout.this.setDrawerViewOffset(view, f);
            n = f == 0.0f ? 4 : 0;
            view.setVisibility(n);
            DrawerLayout.this.invalidate();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onViewReleased(View view, float f, float f2) {
            int n;
            f2 = DrawerLayout.this.getDrawerViewOffset(view);
            int n2 = view.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n = f > 0.0f || f == 0.0f && f2 > 0.5f ? 0 : - n2;
            } else {
                n = DrawerLayout.this.getWidth();
                if (f < 0.0f || f == 0.0f && f2 > 0.5f) {
                    n -= n2;
                }
            }
            this.mDragger.settleCapturedViewAt(n, view.getTop());
            DrawerLayout.this.invalidate();
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        public void setDragger(ViewDragHelper viewDragHelper) {
            this.mDragger = viewDragHelper;
        }

        @Override
        public boolean tryCaptureView(View view, int n) {
            if (DrawerLayout.this.isDrawerView(view) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                return true;
            }
            return false;
        }

    }

}

