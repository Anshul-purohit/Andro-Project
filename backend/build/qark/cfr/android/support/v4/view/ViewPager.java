/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.FocusFinder
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.SoundEffectConstants
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.animation.Interpolator
 *  android.widget.Scroller
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Comparator
 */
package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager
extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId = -1;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    static {
        LAYOUT_ATTRS = new int[]{16842931};
        COMPARATOR = new Comparator<ItemInfo>(){

            public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * (f -= 1.0f) + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }

    public ViewPager(Context context) {
        super(context);
        this.mEndScrollRunnable = new Runnable(){

            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEndScrollRunnable = new ;
        this.mScrollState = 0;
        this.initViewPager();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void calculatePageOffsets(ItemInfo itemInfo, int n, ItemInfo itemInfo2) {
        int n2;
        float f;
        float f2;
        int n3;
        float f3;
        int n4;
        int n5;
        block12 : {
            block15 : {
                block14 : {
                    block13 : {
                        n5 = this.mAdapter.getCount();
                        n3 = this.getClientWidth();
                        f = n3 > 0 ? (float)this.mPageMargin / (float)n3 : 0.0f;
                        if (itemInfo2 == null) break block12;
                        n3 = itemInfo2.position;
                        if (n3 >= itemInfo.position) break block13;
                        n4 = 0;
                        f3 = itemInfo2.offset + itemInfo2.widthFactor + f;
                        ++n3;
                        break block14;
                    }
                    if (n3 <= itemInfo.position) break block12;
                    n4 = this.mItems.size() - 1;
                    f3 = itemInfo2.offset;
                    --n3;
                    break block15;
                }
                while (n3 <= itemInfo.position && n4 < this.mItems.size()) {
                    itemInfo2 = (ItemInfo)this.mItems.get(n4);
                    do {
                        f2 = f3;
                        n2 = n3;
                        if (n3 <= itemInfo2.position) break;
                        f2 = f3;
                        n2 = n3;
                        if (n4 >= this.mItems.size() - 1) break;
                        itemInfo2 = (ItemInfo)this.mItems.get(++n4);
                    } while (true);
                    while (n2 < itemInfo2.position) {
                        f2 += this.mAdapter.getPageWidth(n2) + f;
                        ++n2;
                    }
                    itemInfo2.offset = f2;
                    f3 = f2 + (itemInfo2.widthFactor + f);
                    n3 = n2 + 1;
                }
                break block12;
            }
            while (n3 >= itemInfo.position && n4 >= 0) {
                itemInfo2 = (ItemInfo)this.mItems.get(n4);
                do {
                    f2 = f3;
                    n2 = n3;
                    if (n3 >= itemInfo2.position) break;
                    f2 = f3;
                    n2 = n3;
                    if (n4 <= 0) break;
                    itemInfo2 = (ItemInfo)this.mItems.get(--n4);
                } while (true);
                while (n2 > itemInfo2.position) {
                    f2 -= this.mAdapter.getPageWidth(n2) + f;
                    --n2;
                }
                itemInfo2.offset = f3 = f2 - (itemInfo2.widthFactor + f);
                n3 = n2 - 1;
            }
        }
        n2 = this.mItems.size();
        f2 = itemInfo.offset;
        n3 = itemInfo.position - 1;
        f3 = itemInfo.position == 0 ? itemInfo.offset : -3.4028235E38f;
        this.mFirstOffset = f3;
        f3 = itemInfo.position == n5 - 1 ? itemInfo.offset + itemInfo.widthFactor - 1.0f : Float.MAX_VALUE;
        this.mLastOffset = f3;
        f3 = f2;
        for (n4 = n - 1; n4 >= 0; --n4, --n3) {
            itemInfo2 = (ItemInfo)this.mItems.get(n4);
            while (n3 > itemInfo2.position) {
                f3 -= this.mAdapter.getPageWidth(n3) + f;
                --n3;
            }
            itemInfo2.offset = f3 -= itemInfo2.widthFactor + f;
            if (itemInfo2.position != 0) continue;
            this.mFirstOffset = f3;
        }
        f3 = itemInfo.offset + itemInfo.widthFactor + f;
        n3 = itemInfo.position + 1;
        n4 = n + 1;
        n = n3;
        n3 = n4;
        do {
            if (n3 >= n2) {
                this.mNeedCalculatePageOffsets = false;
                return;
            }
            itemInfo = (ItemInfo)this.mItems.get(n3);
            while (n < itemInfo.position) {
                f3 += this.mAdapter.getPageWidth(n) + f;
                ++n;
            }
            if (itemInfo.position == n5 - 1) {
                this.mLastOffset = itemInfo.widthFactor + f3 - 1.0f;
            }
            itemInfo.offset = f3;
            f3 += itemInfo.widthFactor + f;
            ++n3;
            ++n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void completeScroll(boolean bl) {
        int n;
        int n2;
        int n3 = this.mScrollState == 2 ? 1 : 0;
        if (n3 != 0) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            n2 = this.getScrollX();
            n = this.getScrollY();
            int n4 = this.mScroller.getCurrX();
            int n5 = this.mScroller.getCurrY();
            if (n2 != n4 || n != n5) {
                this.scrollTo(n4, n5);
            }
        }
        this.mPopulatePending = false;
        n = 0;
        n2 = n3;
        for (n3 = n; n3 < this.mItems.size(); ++n3) {
            ItemInfo itemInfo = (ItemInfo)this.mItems.get(n3);
            if (!itemInfo.scrolling) continue;
            n2 = 1;
            itemInfo.scrolling = false;
        }
        if (n2 != 0) {
            if (!bl) {
                this.mEndScrollRunnable.run();
                return;
            }
            ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int determineTargetPage(int n, float f, int n2, int n3) {
        if (Math.abs((int)n3) > this.mFlingDistance && Math.abs((int)n2) > this.mMinimumVelocity) {
            if (n2 <= 0) {
                ++n;
            }
        } else {
            float f2 = n >= this.mCurItem ? 0.4f : 0.6f;
            n = (int)((float)n + f + f2);
        }
        n2 = n;
        if (this.mItems.size() <= 0) return n2;
        ItemInfo itemInfo = (ItemInfo)this.mItems.get(0);
        ItemInfo itemInfo2 = (ItemInfo)this.mItems.get(this.mItems.size() - 1);
        return Math.max((int)itemInfo.position, (int)Math.min((int)n, (int)itemInfo2.position));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void enableLayers(boolean bl) {
        int n = this.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            int n3 = bl ? 2 : 0;
            ViewCompat.setLayerType(this.getChildAt(n2), n3, null);
            ++n2;
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        if (view == null) {
            rect2.set(0, 0, 0, 0);
            return rect2;
        } else {
            rect2.left = view.getLeft();
            rect2.right = view.getRight();
            rect2.top = view.getTop();
            rect2.bottom = view.getBottom();
            for (rect = view.getParent(); rect instanceof ViewGroup && rect != this; rect2.left += rect.getLeft(), rect2.right += rect.getRight(), rect2.top += rect.getTop(), rect2.bottom += rect.getBottom(), rect = rect.getParent()) {
                rect = (ViewGroup)rect;
            }
        }
        return rect2;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    /*
     * Enabled aggressive block sorting
     */
    private ItemInfo infoForCurrentScrollPosition() {
        float f = 0.0f;
        int n = this.getClientWidth();
        float f2 = n > 0 ? (float)this.getScrollX() / (float)n : 0.0f;
        if (n > 0) {
            f = (float)this.mPageMargin / (float)n;
        }
        int n2 = -1;
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean bl = true;
        ItemInfo itemInfo = null;
        n = 0;
        do {
            ItemInfo itemInfo2 = itemInfo;
            if (n >= this.mItems.size()) return itemInfo2;
            itemInfo2 = (ItemInfo)this.mItems.get(n);
            int n3 = n;
            ItemInfo itemInfo3 = itemInfo2;
            if (!bl) {
                n3 = n;
                itemInfo3 = itemInfo2;
                if (itemInfo2.position != n2 + 1) {
                    itemInfo3 = this.mTempItem;
                    itemInfo3.offset = f3 + f4 + f;
                    itemInfo3.position = n2 + 1;
                    itemInfo3.widthFactor = this.mAdapter.getPageWidth(itemInfo3.position);
                    n3 = n - 1;
                }
            }
            f3 = itemInfo3.offset;
            f4 = itemInfo3.widthFactor;
            if (!bl) {
                itemInfo2 = itemInfo;
                if (f2 < f3) return itemInfo2;
            }
            if (f2 < f4 + f3 + f) return itemInfo3;
            if (n3 == this.mItems.size() - 1) {
                return itemInfo3;
            }
            bl = false;
            n2 = itemInfo3.position;
            f4 = itemInfo3.widthFactor;
            n = n3 + 1;
            itemInfo = itemInfo3;
        } while (true);
    }

    private boolean isGutterDrag(float f, float f2) {
        if (f < (float)this.mGutterSize && f2 > 0.0f || f > (float)(this.getWidth() - this.mGutterSize) && f2 < 0.0f) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int n = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, n) == this.mActivePointerId) {
            n = n == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(motionEvent, n);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, n);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private boolean pageScrolled(int n) {
        boolean bl = false;
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0f, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
        } else {
            ItemInfo itemInfo = this.infoForCurrentScrollPosition();
            int n2 = this.getClientWidth();
            int n3 = this.mPageMargin;
            float f = (float)this.mPageMargin / (float)n2;
            int n4 = itemInfo.position;
            f = ((float)n / (float)n2 - itemInfo.offset) / (itemInfo.widthFactor + f);
            n = (int)((float)(n2 + n3) * f);
            this.mCalledSuper = false;
            this.onPageScrolled(n4, f, n);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean performDrag(float f) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        float f2 = this.mLastMotionX;
        this.mLastMotionX = f;
        float f3 = (float)this.getScrollX() + (f2 - f);
        int n = this.getClientWidth();
        f = (float)n * this.mFirstOffset;
        f2 = (float)n * this.mLastOffset;
        boolean bl4 = true;
        boolean bl5 = true;
        ItemInfo itemInfo = (ItemInfo)this.mItems.get(0);
        ItemInfo itemInfo2 = (ItemInfo)this.mItems.get(this.mItems.size() - 1);
        if (itemInfo.position != 0) {
            bl4 = false;
            f = itemInfo.offset * (float)n;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            bl5 = false;
            f2 = itemInfo2.offset * (float)n;
        }
        if (f3 < f) {
            if (bl4) {
                bl3 = this.mLeftEdge.onPull(Math.abs((float)(f - f3)) / (float)n);
            }
        } else {
            bl3 = bl;
            f = f3;
            if (f3 > f2) {
                bl3 = bl2;
                if (bl5) {
                    bl3 = this.mRightEdge.onPull(Math.abs((float)(f3 - f2)) / (float)n);
                }
                f = f2;
            }
        }
        this.mLastMotionX += f - (float)((int)f);
        this.scrollTo((int)f, this.getScrollY());
        this.pageScrolled((int)f);
        return bl3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recomputeScrollPosition(int n, int n2, int n3, int n4) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            int n5 = this.getPaddingLeft();
            int n6 = this.getPaddingRight();
            int n7 = this.getPaddingLeft();
            int n8 = this.getPaddingRight();
            float f = (float)this.getScrollX() / (float)(n2 - n7 - n8 + n4);
            n2 = (int)((float)(n - n5 - n6 + n3) * f);
            this.scrollTo(n2, this.getScrollY());
            if (!this.mScroller.isFinished()) {
                n3 = this.mScroller.getDuration();
                n4 = this.mScroller.timePassed();
                ItemInfo itemInfo = this.infoForPosition(this.mCurItem);
                this.mScroller.startScroll(n2, 0, (int)(itemInfo.offset * (float)n), 0, n3 - n4);
            }
            return;
        }
        ItemInfo itemInfo = this.infoForPosition(this.mCurItem);
        float f = itemInfo != null ? Math.min((float)itemInfo.offset, (float)this.mLastOffset) : 0.0f;
        if ((n = (int)((float)(n - this.getPaddingLeft() - this.getPaddingRight()) * f)) == this.getScrollX()) return;
        this.completeScroll(false);
        this.scrollTo(n, this.getScrollY());
    }

    private void removeNonDecorViews() {
        int n = 0;
        while (n < this.getChildCount()) {
            int n2 = n;
            if (!((LayoutParams)this.getChildAt((int)n).getLayoutParams()).isDecor) {
                this.removeViewAt(n);
                n2 = n - 1;
            }
            n = n2 + 1;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean bl) {
        ViewParent viewParent = this.getParent();
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(bl);
        }
    }

    private void scrollToItem(int n, boolean bl, int n2, boolean bl2) {
        ItemInfo itemInfo = this.infoForPosition(n);
        int n3 = 0;
        if (itemInfo != null) {
            n3 = (int)((float)this.getClientWidth() * Math.max((float)this.mFirstOffset, (float)Math.min((float)itemInfo.offset, (float)this.mLastOffset)));
        }
        if (bl) {
            this.smoothScrollTo(n3, 0, n2);
            if (bl2 && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(n);
            }
            if (bl2 && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(n);
            }
            return;
        }
        if (bl2 && this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(n);
        }
        if (bl2 && this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(n);
        }
        this.completeScroll(false);
        this.scrollTo(n3, 0);
        this.pageScrolled(n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setScrollState(int n) {
        block5 : {
            block4 : {
                if (this.mScrollState == n) break block4;
                this.mScrollState = n;
                if (this.mPageTransformer != null) {
                    boolean bl = n != 0;
                    this.enableLayers(bl);
                }
                if (this.mOnPageChangeListener != null) break block5;
            }
            return;
        }
        this.mOnPageChangeListener.onPageScrollStateChanged(n);
    }

    private void setScrollingCacheEnabled(boolean bl) {
        if (this.mScrollingCacheEnabled != bl) {
            this.mScrollingCacheEnabled = bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view = this.getChildAt(i);
                this.mDrawingOrderedChildren.add((Object)view);
            }
            Collections.sort(this.mDrawingOrderedChildren, (Comparator)sPositionComparator);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        int n3 = arrayList.size();
        int n4 = this.getDescendantFocusability();
        if (n4 != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                ItemInfo itemInfo;
                View view = this.getChildAt(i);
                if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem) continue;
                view.addFocusables(arrayList, n, n2);
            }
        }
        if (n4 == 262144 && n3 != arrayList.size() || !this.isFocusable() || (n2 & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode() || arrayList == null) {
            return;
        }
        arrayList.add((Object)this);
    }

    ItemInfo addNewItem(int n, int n2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = n;
        itemInfo.object = this.mAdapter.instantiateItem(this, n);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(n);
        if (n2 < 0 || n2 >= this.mItems.size()) {
            this.mItems.add((Object)itemInfo);
            return itemInfo;
        }
        this.mItems.add(n2, (Object)itemInfo);
        return itemInfo;
    }

    public void addTouchables(ArrayList<View> arrayList) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ItemInfo itemInfo;
            View view = this.getChildAt(i);
            if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem) continue;
            view.addTouchables(arrayList);
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        ViewGroup.LayoutParams layoutParams2 = layoutParams;
        if (!this.checkLayoutParams(layoutParams)) {
            layoutParams2 = this.generateLayoutParams(layoutParams);
        }
        layoutParams = (LayoutParams)layoutParams2;
        layoutParams.isDecor |= view instanceof Decor;
        if (this.mInLayout) {
            if (layoutParams != null && layoutParams.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            layoutParams.needsMeasure = true;
            this.addViewInLayout(view, n, layoutParams2);
            return;
        }
        super.addView(view, n, layoutParams2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean arrowScroll(int var1_1) {
        block16 : {
            block15 : {
                block14 : {
                    var6_2 = this.findFocus();
                    if (var6_2 != this) break block14;
                    var5_3 = null;
                    break block15;
                }
                var5_3 = var6_2;
                if (var6_2 != null) break block16;
            }
lbl9: // 3 sources:
            do {
                var4_6 = false;
                var6_2 = FocusFinder.getInstance().findNextFocus((ViewGroup)this, var5_3, var1_1);
                if (var6_2 != null && var6_2 != var5_3) {
                    if (var1_1 == 17) {
                        var2_4 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)var6_2).left;
                        var3_5 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)var5_3).left;
                        var4_6 = var5_3 != null && var2_4 >= var3_5 ? this.pageLeft() : var6_2.requestFocus();
                    } else if (var1_1 == 66) {
                        var2_4 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)var6_2).left;
                        var3_5 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)var5_3).left;
                        var4_6 = var5_3 != null && var2_4 <= var3_5 ? this.pageRight() : var6_2.requestFocus();
                    }
                } else if (var1_1 == 17 || var1_1 == 1) {
                    var4_6 = this.pageLeft();
                } else if (var1_1 == 66 || var1_1 == 2) {
                    var4_6 = this.pageRight();
                }
                if (var4_6 == false) return var4_6;
                this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection((int)var1_1));
                return var4_6;
                break;
            } while (true);
        }
        var3_5 = 0;
        var5_3 = var6_2.getParent();
        do {
            block18 : {
                block17 : {
                    var2_4 = var3_5;
                    if (!(var5_3 instanceof ViewGroup)) break block17;
                    if (var5_3 != this) break block18;
                    var2_4 = 1;
                }
                var5_3 = var6_2;
                if (var2_4 != 0) ** GOTO lbl9
                var7_7 = new StringBuilder();
                var7_7.append(var6_2.getClass().getSimpleName());
                var5_3 = var6_2.getParent();
                while (var5_3 instanceof ViewGroup) {
                    var7_7.append(" => ").append(var5_3.getClass().getSimpleName());
                    var5_3 = var5_3.getParent();
                }
                break;
            }
            var5_3 = var5_3.getParent();
        } while (true);
        Log.e((String)"ViewPager", (String)("arrowScroll tried to find focus based on non-child current focused view " + var7_7.toString()));
        var5_3 = null;
        ** while (true)
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        this.setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)0, (float)0.0f, (float)0.0f, (int)0);
        this.mVelocityTracker.addMovement(motionEvent);
        motionEvent.recycle();
        this.mFakeDragBeginTime = l;
        return true;
    }

    protected boolean canScroll(View view, boolean bl, int n, int n2, int n3) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            int n4 = view.getScrollX();
            int n5 = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                View view2 = viewGroup.getChildAt(i);
                if (n2 + n4 < view2.getLeft() || n2 + n4 >= view2.getRight() || n3 + n5 < view2.getTop() || n3 + n5 >= view2.getBottom() || !this.canScroll(view2, true, n, n2 + n4 - view2.getLeft(), n3 + n5 - view2.getTop())) continue;
                return true;
            }
        }
        if (bl && ViewCompat.canScrollHorizontally(view, - n)) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean canScrollHorizontally(int n) {
        boolean bl = true;
        boolean bl2 = true;
        if (this.mAdapter == null) {
            return false;
        }
        int n2 = this.getClientWidth();
        int n3 = this.getScrollX();
        if (n < 0) {
            if (n3 <= (int)((float)n2 * this.mFirstOffset)) return false;
            return bl2;
        }
        if (n <= 0) return false;
        if (n3 >= (int)((float)n2 * this.mLastOffset)) return false;
        return bl;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams)) {
            return true;
        }
        return false;
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int n = this.getScrollX();
            int n2 = this.getScrollY();
            int n3 = this.mScroller.getCurrX();
            int n4 = this.mScroller.getCurrY();
            if (n != n3 || n2 != n4) {
                this.scrollTo(n3, n4);
                if (!this.pageScrolled(n3)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, n4);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        this.completeScroll(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    void dataSetChanged() {
        Object object;
        int n;
        this.mExpectedAdapterCount = n = this.mAdapter.getCount();
        int n2 = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < n ? 1 : 0;
        int n3 = this.mCurItem;
        int n4 = 0;
        int n5 = 0;
        while (n5 < this.mItems.size()) {
            int n6;
            int n7;
            int n8;
            object = (ItemInfo)this.mItems.get(n5);
            int n9 = this.mAdapter.getItemPosition(object.object);
            if (n9 == -1) {
                n7 = n3;
                n6 = n4;
                n8 = n5;
            } else if (n9 == -2) {
                this.mItems.remove(n5);
                n9 = n5 - 1;
                n5 = n4;
                if (n4 == 0) {
                    this.mAdapter.startUpdate(this);
                    n5 = 1;
                }
                this.mAdapter.destroyItem(this, object.position, object.object);
                n2 = 1;
                n8 = n9;
                n6 = n5;
                n7 = n3;
                if (this.mCurItem == object.position) {
                    n7 = Math.max((int)0, (int)Math.min((int)this.mCurItem, (int)(n - 1)));
                    n2 = 1;
                    n8 = n9;
                    n6 = n5;
                }
            } else {
                n8 = n5;
                n6 = n4;
                n7 = n3;
                if (object.position != n9) {
                    if (object.position == this.mCurItem) {
                        n3 = n9;
                    }
                    object.position = n9;
                    n2 = 1;
                    n8 = n5;
                    n6 = n4;
                    n7 = n3;
                }
            }
            n5 = n8 + 1;
            n4 = n6;
            n3 = n7;
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (n2 != 0) {
            n4 = this.getChildCount();
            for (n2 = 0; n2 < n4; ++n2) {
                object = (LayoutParams)this.getChildAt(n2).getLayoutParams();
                if (object.isDecor) continue;
                object.widthFactor = 0.0f;
            }
            this.setCurrentItemInternal(n3, false, true);
            this.requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent)) {
            return true;
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            ItemInfo itemInfo;
            View view = this.getChildAt(i);
            if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem || !view.dispatchPopulateAccessibilityEvent(accessibilityEvent)) continue;
            return true;
        }
        return false;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float)Math.sin((double)((float)((double)(f - 0.5f) * 0.4712389167638204)));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int n = 0;
        int n2 = 0;
        int n3 = ViewCompat.getOverScrollMode((View)this);
        if (n3 == 0 || n3 == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1) {
            if (!this.mLeftEdge.isFinished()) {
                n = canvas.save();
                n2 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                n3 = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(- n2 + this.getPaddingTop()), this.mFirstOffset * (float)n3);
                this.mLeftEdge.setSize(n2, n3);
                n2 = false | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(n);
            }
            n = n2;
            if (!this.mRightEdge.isFinished()) {
                n3 = canvas.save();
                n = this.getWidth();
                int n4 = this.getHeight();
                int n5 = this.getPaddingTop();
                int n6 = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(- this.getPaddingTop()), (- this.mLastOffset + 1.0f) * (float)n);
                this.mRightEdge.setSize(n4 - n5 - n6, n);
                n = n2 | this.mRightEdge.draw(canvas);
                canvas.restoreToCount(n3);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (n != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable2 = this.mMarginDrawable;
        if (drawable2 != null && drawable2.isStateful()) {
            drawable2.setState(this.getDrawableState());
        }
    }

    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        Object object = this.mVelocityTracker;
        object.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
        int n = (int)VelocityTrackerCompat.getXVelocity((VelocityTracker)object, this.mActivePointerId);
        this.mPopulatePending = true;
        int n2 = this.getClientWidth();
        int n3 = this.getScrollX();
        object = this.infoForCurrentScrollPosition();
        this.setCurrentItemInternal(this.determineTargetPage(object.position, ((float)n3 / (float)n2 - object.offset) / object.widthFactor, n, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, n);
        this.endDrag();
        this.mFakeDragging = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) return false;
        {
            switch (keyEvent.getKeyCode()) {
                default: {
                    return false;
                }
                case 21: {
                    return this.arrowScroll(17);
                }
                case 22: {
                    return this.arrowScroll(66);
                }
                case 61: {
                    if (Build.VERSION.SDK_INT < 11) return false;
                    if (KeyEventCompat.hasNoModifiers(keyEvent)) {
                        return this.arrowScroll(2);
                    }
                    if (!KeyEventCompat.hasModifiers(keyEvent, 1)) return false;
                    return this.arrowScroll(1);
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void fakeDragBy(float f) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        this.mLastMotionX += f;
        float f2 = (float)this.getScrollX() - f;
        int n = this.getClientWidth();
        f = (float)n * this.mFirstOffset;
        float f3 = (float)n * this.mLastOffset;
        ItemInfo itemInfo = (ItemInfo)this.mItems.get(0);
        ItemInfo itemInfo2 = (ItemInfo)this.mItems.get(this.mItems.size() - 1);
        if (itemInfo.position != 0) {
            f = itemInfo.offset * (float)n;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            f3 = itemInfo2.offset * (float)n;
        }
        if (f2 >= f) {
            f = f2;
            if (f2 > f3) {
                f = f3;
            }
        }
        this.mLastMotionX += f - (float)((int)f);
        this.scrollTo((int)f, this.getScrollY());
        this.pageScrolled((int)f);
        long l = SystemClock.uptimeMillis();
        itemInfo = MotionEvent.obtain((long)this.mFakeDragBeginTime, (long)l, (int)2, (float)this.mLastMotionX, (float)0.0f, (int)0);
        this.mVelocityTracker.addMovement((MotionEvent)itemInfo);
        itemInfo.recycle();
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return this.generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected int getChildDrawingOrder(int n, int n2) {
        if (this.mDrawingOrder == 2) {
            n = n - 1 - n2;
            do {
                return ((LayoutParams)((View)this.mDrawingOrderedChildren.get((int)n)).getLayoutParams()).childIndex;
                break;
            } while (true);
        }
        n = n2;
        return ((LayoutParams)((View)this.mDrawingOrderedChildren.get((int)n)).getLayoutParams()).childIndex;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    ItemInfo infoForAnyChild(View view) {
        ViewParent viewParent;
        while ((viewParent = view.getParent()) != this) {
            if (viewParent == null || !(viewParent instanceof View)) {
                return null;
            }
            view = (View)viewParent;
        }
        return this.infoForChild(view);
    }

    ItemInfo infoForChild(View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo itemInfo = (ItemInfo)this.mItems.get(i);
            if (!this.mAdapter.isViewFromObject(view, itemInfo.object)) continue;
            return itemInfo;
        }
        return null;
    }

    ItemInfo infoForPosition(int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo itemInfo = (ItemInfo)this.mItems.get(i);
            if (itemInfo.position != n) continue;
            return itemInfo;
        }
        return null;
    }

    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
        this.mMinimumVelocity = (int)(400.0f * f);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        this.mFlingDistance = (int)(25.0f * f);
        this.mCloseEnough = (int)(2.0f * f);
        this.mDefaultGutterSize = (int)(16.0f * f);
        ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int n = this.getScrollX();
            int n2 = this.getWidth();
            float f = (float)this.mPageMargin / (float)n2;
            int n3 = 0;
            Object object = (ArrayList<ItemInfo>)this.mItems.get(0);
            float f2 = object.offset;
            int n4 = this.mItems.size();
            int n5 = ((ItemInfo)this.mItems.get((int)(n4 - 1))).position;
            for (int i = object.position; i < n5; ++i) {
                float f3;
                while (i > object.position && n3 < n4) {
                    object = this.mItems;
                    object = (ItemInfo)object.get(++n3);
                }
                if (i == object.position) {
                    f3 = (object.offset + object.widthFactor) * (float)n2;
                    f2 = object.offset + object.widthFactor + f;
                } else {
                    float f4 = this.mAdapter.getPageWidth(i);
                    f3 = (f2 + f4) * (float)n2;
                    f2 += f4 + f;
                }
                if ((float)this.mPageMargin + f3 > (float)n) {
                    this.mMarginDrawable.setBounds((int)f3, this.mTopPageBounds, (int)((float)this.mPageMargin + f3 + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (f3 > (float)(n + n2)) break;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onInterceptTouchEvent(MotionEvent var1_1) {
        block17 : {
            var7_2 = var1_1.getAction() & 255;
            if (var7_2 == 3 || var7_2 == 1) {
                this.mIsBeingDragged = false;
                this.mIsUnableToDrag = false;
                this.mActivePointerId = -1;
                if (this.mVelocityTracker == null) return false;
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                return false;
            }
            if (var7_2 != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }
                if (this.mIsUnableToDrag) {
                    return false;
                }
            }
            switch (var7_2) {
                case 2: {
                    var7_2 = this.mActivePointerId;
                    if (var7_2 != -1) {
                        var7_2 = MotionEventCompat.findPointerIndex(var1_1, var7_2);
                        var3_3 = MotionEventCompat.getX(var1_1, var7_2);
                        var2_4 = var3_3 - this.mLastMotionX;
                        var5_6 = Math.abs((float)var2_4);
                        var4_7 = MotionEventCompat.getY(var1_1, var7_2);
                        var6_8 = Math.abs((float)(var4_7 - this.mInitialMotionY));
                        if (var2_4 != 0.0f && !this.isGutterDrag(this.mLastMotionX, var2_4) && this.canScroll((View)this, false, (int)var2_4, (int)var3_3, (int)var4_7)) {
                            this.mLastMotionX = var3_3;
                            this.mLastMotionY = var4_7;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                        if (var5_6 > (float)this.mTouchSlop && 0.5f * var5_6 > var6_8) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.setScrollState(1);
                            var2_4 = var2_4 > 0.0f ? this.mInitialMotionX + (float)this.mTouchSlop : this.mInitialMotionX - (float)this.mTouchSlop;
                            this.mLastMotionX = var2_4;
                            this.mLastMotionY = var4_7;
                            this.setScrollingCacheEnabled(true);
                        } else if (var6_8 > (float)this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && this.performDrag(var3_3)) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                            ** break;
                        }
                    }
                    ** GOTO lbl63
                }
                case 0: {
                    this.mInitialMotionX = var2_5 = var1_1.getX();
                    this.mLastMotionX = var2_5;
                    this.mInitialMotionY = var2_5 = var1_1.getY();
                    this.mLastMotionY = var2_5;
                    this.mActivePointerId = MotionEventCompat.getPointerId(var1_1, 0);
                    this.mIsUnableToDrag = false;
                    this.mScroller.computeScrollOffset();
                    if (this.mScrollState == 2 && Math.abs((int)(this.mScroller.getFinalX() - this.mScroller.getCurrX())) > this.mCloseEnough) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        this.mIsBeingDragged = true;
                        this.requestParentDisallowInterceptTouchEvent(true);
                        this.setScrollState(1);
                        ** break;
                    }
                    this.completeScroll(false);
                    this.mIsBeingDragged = false;
                }
lbl63: // 5 sources:
                default: {
                    break block17;
                }
                case 6: 
            }
            this.onSecondaryPointerUp(var1_1);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(var1_1);
        return this.mIsBeingDragged;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams;
        int n5;
        View view;
        int n6 = this.getChildCount();
        int n7 = n3 - n;
        int n8 = n4 - n2;
        n2 = this.getPaddingLeft();
        n = this.getPaddingTop();
        int n9 = this.getPaddingRight();
        n4 = this.getPaddingBottom();
        int n10 = this.getScrollX();
        int n11 = 0;
        for (int i = 0; i < n6; ++i) {
            view = this.getChildAt(i);
            int n12 = n11;
            int n13 = n4;
            n5 = n2;
            int n14 = n9;
            n3 = n;
            if (view.getVisibility() != 8) {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n12 = n11;
                n13 = n4;
                n5 = n2;
                n14 = n9;
                n3 = n;
                if (layoutParams.isDecor) {
                    n3 = layoutParams.gravity;
                    n14 = layoutParams.gravity;
                    switch (n3 & 7) {
                        default: {
                            n3 = n2;
                            n5 = n2;
                            break;
                        }
                        case 3: {
                            n3 = n2;
                            n5 = n2 + view.getMeasuredWidth();
                            break;
                        }
                        case 1: {
                            n3 = Math.max((int)((n7 - view.getMeasuredWidth()) / 2), (int)n2);
                            n5 = n2;
                            break;
                        }
                        case 5: {
                            n3 = n7 - n9 - view.getMeasuredWidth();
                            n9 += view.getMeasuredWidth();
                            n5 = n2;
                        }
                    }
                    switch (n14 & 112) {
                        default: {
                            n2 = n;
                            break;
                        }
                        case 48: {
                            n2 = n;
                            n += view.getMeasuredHeight();
                            break;
                        }
                        case 16: {
                            n2 = Math.max((int)((n8 - view.getMeasuredHeight()) / 2), (int)n);
                            break;
                        }
                        case 80: {
                            n2 = n8 - n4 - view.getMeasuredHeight();
                            n4 += view.getMeasuredHeight();
                        }
                    }
                    view.layout(n3, n2, view.getMeasuredWidth() + (n3 += n10), view.getMeasuredHeight() + n2);
                    n12 = n11 + 1;
                    n3 = n;
                    n14 = n9;
                    n13 = n4;
                }
            }
            n11 = n12;
            n4 = n13;
            n2 = n5;
            n9 = n14;
            n = n3;
        }
        n9 = n7 - n2 - n9;
        for (n3 = 0; n3 < n6; ++n3) {
            ItemInfo itemInfo;
            view = this.getChildAt(n3);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.isDecor || (itemInfo = this.infoForChild(view)) == null) continue;
            n5 = n2 + (int)((float)n9 * itemInfo.offset);
            if (layoutParams.needsMeasure) {
                layoutParams.needsMeasure = false;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n9 * layoutParams.widthFactor)), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(n8 - n - n4), (int)1073741824));
            }
            view.layout(n5, n, view.getMeasuredWidth() + n5, view.getMeasuredHeight() + n);
        }
        this.mTopPageBounds = n;
        this.mBottomPageBounds = n8 - n4;
        this.mDecorChildCount = n11;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3;
        View view;
        LayoutParams layoutParams;
        this.setMeasuredDimension(ViewPager.getDefaultSize((int)0, (int)n), ViewPager.getDefaultSize((int)0, (int)n2));
        n = this.getMeasuredWidth();
        this.mGutterSize = Math.min((int)(n / 10), (int)this.mDefaultGutterSize);
        n = n - this.getPaddingLeft() - this.getPaddingRight();
        n2 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int n4 = this.getChildCount();
        for (int i = 0; i < n4; ++i) {
            view = this.getChildAt(i);
            n3 = n2;
            int n5 = n;
            if (view.getVisibility() != 8) {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n3 = n2;
                n5 = n;
                if (layoutParams != null) {
                    n3 = n2;
                    n5 = n;
                    if (layoutParams.isDecor) {
                        int n6;
                        n5 = layoutParams.gravity & 7;
                        int n7 = layoutParams.gravity & 112;
                        int n8 = Integer.MIN_VALUE;
                        n3 = Integer.MIN_VALUE;
                        n7 = n7 == 48 || n7 == 80 ? 1 : 0;
                        boolean bl = n5 == 3 || n5 == 5;
                        if (n7 != 0) {
                            n5 = 1073741824;
                        } else {
                            n5 = n8;
                            if (bl) {
                                n3 = 1073741824;
                                n5 = n8;
                            }
                        }
                        int n9 = n;
                        n8 = n2;
                        int n10 = n9;
                        if (layoutParams.width != -2) {
                            n5 = n6 = 1073741824;
                            n10 = n9;
                            if (layoutParams.width != -1) {
                                n10 = layoutParams.width;
                                n5 = n6;
                            }
                        }
                        n9 = n8;
                        if (layoutParams.height != -2) {
                            n3 = n6 = 1073741824;
                            n9 = n8;
                            if (layoutParams.height != -1) {
                                n9 = layoutParams.height;
                                n3 = n6;
                            }
                        }
                        view.measure(View.MeasureSpec.makeMeasureSpec((int)n10, (int)n5), View.MeasureSpec.makeMeasureSpec((int)n9, (int)n3));
                        if (n7 != 0) {
                            n3 = n2 - view.getMeasuredHeight();
                            n5 = n;
                        } else {
                            n3 = n2;
                            n5 = n;
                            if (bl) {
                                n5 = n - view.getMeasuredWidth();
                                n3 = n2;
                            }
                        }
                    }
                }
            }
            n2 = n3;
            n = n5;
        }
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)n2, (int)1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        n3 = this.getChildCount();
        n2 = 0;
        while (n2 < n3) {
            view = this.getChildAt(n2);
            if (!(view.getVisibility() == 8 || (layoutParams = (LayoutParams)view.getLayoutParams()) != null && layoutParams.isDecor)) {
                view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n * layoutParams.widthFactor)), (int)1073741824), this.mChildHeightMeasureSpec);
            }
            ++n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onPageScrolled(int n, float f, int n2) {
        int n3;
        View view;
        if (this.mDecorChildCount > 0) {
            int n4 = this.getScrollX();
            n3 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            int n6 = this.getWidth();
            int n7 = this.getChildCount();
            for (int i = 0; i < n7; ++i) {
                int n8;
                int n9;
                view = this.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (!layoutParams.isDecor) {
                    n8 = n5;
                    n9 = n3;
                } else {
                    switch (layoutParams.gravity & 7) {
                        default: {
                            n8 = n3;
                            break;
                        }
                        case 3: {
                            n8 = n3;
                            n3 += view.getWidth();
                            break;
                        }
                        case 1: {
                            n8 = Math.max((int)((n6 - view.getMeasuredWidth()) / 2), (int)n3);
                            break;
                        }
                        case 5: {
                            n8 = n6 - n5 - view.getMeasuredWidth();
                            n5 += view.getMeasuredWidth();
                        }
                    }
                    int n10 = n8 + n4 - view.getLeft();
                    n9 = n3;
                    n8 = n5;
                    if (n10 != 0) {
                        view.offsetLeftAndRight(n10);
                        n9 = n3;
                        n8 = n5;
                    }
                }
                n3 = n9;
                n5 = n8;
            }
        }
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(n, f, n2);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(n, f, n2);
        }
        if (this.mPageTransformer != null) {
            n2 = this.getScrollX();
            n3 = this.getChildCount();
            for (n = 0; n < n3; ++n) {
                view = this.getChildAt(n);
                if (((LayoutParams)view.getLayoutParams()).isDecor) continue;
                f = (float)(view.getLeft() - n2) / (float)this.getClientWidth();
                this.mPageTransformer.transformPage(view, f);
            }
        }
        this.mCalledSuper = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected boolean onRequestFocusInDescendants(int n, Rect rect) {
        int n2;
        int n3;
        int n4 = this.getChildCount();
        if ((n & 2) != 0) {
            n2 = 0;
            n3 = 1;
        } else {
            n2 = n4 - 1;
            n3 = -1;
            n4 = -1;
        }
        while (n2 != n4) {
            ItemInfo itemInfo;
            View view = this.getChildAt(n2);
            if (view.getVisibility() == 0 && (itemInfo = this.infoForChild(view)) != null && itemInfo.position == this.mCurItem && view.requestFocus(n, rect)) {
                return true;
            }
            n2 += n3;
        }
        return false;
    }

    public void onRestoreInstanceState(Parcelable object) {
        if (!(object instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        if (this.mAdapter != null) {
            this.mAdapter.restoreState(object.adapterState, object.loader);
            this.setCurrentItemInternal(object.position, false, true);
            return;
        }
        this.mRestoredCurItem = object.position;
        this.mRestoredAdapterState = object.adapterState;
        this.mRestoredClassLoader = object.loader;
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        if (this.mAdapter != null) {
            savedState.adapterState = this.mAdapter.saveState();
        }
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.recomputeScrollPosition(n, n3, this.mPageMargin, this.mPageMargin);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        if (this.mFakeDragging) {
            return true;
        }
        if (var1_1.getAction() == 0 && var1_1.getEdgeFlags() != 0) {
            return false;
        }
        if (this.mAdapter == null) return false;
        if (this.mAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(var1_1);
        var8_2 = var1_1.getAction();
        var6_4 = var7_3 = 0;
        switch (var8_2 & 255) {
            default: {
                var6_4 = var7_3;
                break;
            }
            case 0: {
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                this.populate();
                this.mInitialMotionX = var2_5 = var1_1.getX();
                this.mLastMotionX = var2_5;
                this.mInitialMotionY = var2_5 = var1_1.getY();
                this.mLastMotionY = var2_5;
                this.mActivePointerId = MotionEventCompat.getPointerId(var1_1, 0);
                var6_4 = var7_3;
                break;
            }
            case 2: {
                if (!this.mIsBeingDragged) {
                    var6_4 = MotionEventCompat.findPointerIndex(var1_1, this.mActivePointerId);
                    var2_6 = MotionEventCompat.getX(var1_1, var6_4);
                    var4_7 = Math.abs((float)(var2_6 - this.mLastMotionX));
                    var3_8 = MotionEventCompat.getY(var1_1, var6_4);
                    var5_9 = Math.abs((float)(var3_8 - this.mLastMotionY));
                    if (var4_7 > (float)this.mTouchSlop && var4_7 > var5_9) {
                        this.mIsBeingDragged = true;
                        this.requestParentDisallowInterceptTouchEvent(true);
                        var2_6 = var2_6 - this.mInitialMotionX > 0.0f ? this.mInitialMotionX + (float)this.mTouchSlop : this.mInitialMotionX - (float)this.mTouchSlop;
                        this.mLastMotionX = var2_6;
                        this.mLastMotionY = var3_8;
                        this.setScrollState(1);
                        this.setScrollingCacheEnabled(true);
                        var9_10 = this.getParent();
                        if (var9_10 != null) {
                            var9_10.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                var6_4 = var7_3;
                if (this.mIsBeingDragged) {
                    var6_4 = false | this.performDrag(MotionEventCompat.getX(var1_1, MotionEventCompat.findPointerIndex(var1_1, this.mActivePointerId)));
                    break;
                }
                ** GOTO lbl81
            }
            case 1: {
                var6_4 = var7_3;
                if (this.mIsBeingDragged) {
                    var9_11 = this.mVelocityTracker;
                    var9_11.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                    var6_4 = (int)VelocityTrackerCompat.getXVelocity((VelocityTracker)var9_11, this.mActivePointerId);
                    this.mPopulatePending = true;
                    var7_3 = this.getClientWidth();
                    var8_2 = this.getScrollX();
                    var9_11 = this.infoForCurrentScrollPosition();
                    this.setCurrentItemInternal(this.determineTargetPage(var9_11.position, ((float)var8_2 / (float)var7_3 - var9_11.offset) / var9_11.widthFactor, var6_4, (int)(MotionEventCompat.getX(var1_1, MotionEventCompat.findPointerIndex(var1_1, this.mActivePointerId)) - this.mInitialMotionX)), true, true, var6_4);
                    this.mActivePointerId = -1;
                    this.endDrag();
                    var6_4 = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                    break;
                }
                ** GOTO lbl81
            }
            case 3: {
                var6_4 = var7_3;
                if (this.mIsBeingDragged) {
                    this.scrollToItem(this.mCurItem, true, 0, false);
                    this.mActivePointerId = -1;
                    this.endDrag();
                    var6_4 = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                    break;
                }
                ** GOTO lbl81
            }
            case 5: {
                var6_4 = MotionEventCompat.getActionIndex(var1_1);
                this.mLastMotionX = MotionEventCompat.getX(var1_1, var6_4);
                this.mActivePointerId = MotionEventCompat.getPointerId(var1_1, var6_4);
                var6_4 = var7_3;
            }
lbl81: // 5 sources:
            case 4: {
                break;
            }
            case 6: {
                this.onSecondaryPointerUp(var1_1);
                this.mLastMotionX = MotionEventCompat.getX(var1_1, MotionEventCompat.findPointerIndex(var1_1, this.mActivePointerId));
                var6_4 = var7_3;
            }
        }
        if (var6_4 == 0) return true;
        ViewCompat.postInvalidateOnAnimation((View)this);
        return true;
    }

    boolean pageLeft() {
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        }
        return false;
    }

    boolean pageRight() {
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }

    void populate() {
        this.populate(this.mCurItem);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void populate(int n) {
        Object object;
        Object object2;
        ItemInfo itemInfo;
        int n2;
        int n3;
        block34 : {
            int n4;
            block35 : {
                float f;
                Object object3;
                object = null;
                n2 = 2;
                if (this.mCurItem != n) {
                    n3 = this.mCurItem < n ? 66 : 17;
                    object = this.infoForPosition(this.mCurItem);
                    this.mCurItem = n;
                    n2 = n3;
                }
                if (this.mAdapter == null) {
                    this.sortChildDrawingOrder();
                    return;
                }
                if (this.mPopulatePending) {
                    this.sortChildDrawingOrder();
                    return;
                }
                if (this.getWindowToken() == null) return;
                this.mAdapter.startUpdate(this);
                n = this.mOffscreenPageLimit;
                int n5 = Math.max((int)0, (int)(this.mCurItem - n));
                int n6 = this.mAdapter.getCount();
                int n7 = Math.min((int)(n6 - 1), (int)(this.mCurItem + n));
                if (n6 != this.mExpectedAdapterCount) {
                    String string2;
                    try {
                        string2 = this.getResources().getResourceName(this.getId());
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        string2 = Integer.toHexString((int)this.getId());
                        throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + n6 + " Pager id: " + string2 + " Pager class: " + (Object)this.getClass() + " Problematic adapter: " + (Object)this.mAdapter.getClass());
                    }
                    throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + n6 + " Pager id: " + string2 + " Pager class: " + (Object)this.getClass() + " Problematic adapter: " + (Object)this.mAdapter.getClass());
                }
                itemInfo = null;
                n = 0;
                do {
                    block37 : {
                        block36 : {
                            object2 = itemInfo;
                            if (n >= this.mItems.size()) break block36;
                            object3 = (ItemInfo)this.mItems.get(n);
                            if (object3.position < this.mCurItem) break block37;
                            object2 = itemInfo;
                            if (object3.position == this.mCurItem) {
                                object2 = object3;
                            }
                        }
                        itemInfo = object2;
                        if (object2 == null) {
                            itemInfo = object2;
                            if (n6 > 0) {
                                itemInfo = this.addNewItem(this.mCurItem, n);
                            }
                        }
                        if (itemInfo != null) {
                            break;
                        }
                        break block34;
                    }
                    ++n;
                } while (true);
                float f2 = 0.0f;
                int n8 = n - 1;
                object2 = n8 >= 0 ? (ItemInfo)this.mItems.get(n8) : null;
                int n9 = this.getClientWidth();
                float f3 = n9 <= 0 ? 0.0f : 2.0f - itemInfo.widthFactor + (float)this.getPaddingLeft() / (float)n9;
                int n10 = this.mCurItem - 1;
                object3 = object2;
                n4 = n;
                do {
                    block41 : {
                        block39 : {
                            block40 : {
                                block38 : {
                                    if (n10 < 0) break block38;
                                    if (f2 < f3 || n10 >= n5) break block39;
                                    if (object3 != null) break block40;
                                }
                                f2 = itemInfo.widthFactor;
                                n10 = n4 + 1;
                                if (f2 < 2.0f) {
                                    object2 = n10 < this.mItems.size() ? (ItemInfo)this.mItems.get(n10) : null;
                                    f3 = n9 <= 0 ? 0.0f : (float)this.getPaddingRight() / (float)n9 + 2.0f;
                                    object3 = object2;
                                    break;
                                }
                                break block35;
                            }
                            n = n4;
                            f = f2;
                            object2 = object3;
                            n3 = n8;
                            if (n10 == object3.position) {
                                n = n4;
                                f = f2;
                                object2 = object3;
                                n3 = n8;
                                if (!object3.scrolling) {
                                    this.mItems.remove(n8);
                                    this.mAdapter.destroyItem(this, n10, object3.object);
                                    n3 = n8 - 1;
                                    n = n4 - 1;
                                    if (n3 >= 0) {
                                        object2 = (ItemInfo)this.mItems.get(n3);
                                        f = f2;
                                    } else {
                                        object2 = null;
                                        f = f2;
                                    }
                                }
                            }
                            break block41;
                        }
                        if (object3 != null && n10 == object3.position) {
                            f = f2 + object3.widthFactor;
                            n3 = n8 - 1;
                            object2 = n3 >= 0 ? (ItemInfo)this.mItems.get(n3) : null;
                            n = n4;
                        } else {
                            f = f2 + this.addNewItem((int)n10, (int)(n8 + 1)).widthFactor;
                            n = n4 + 1;
                            object2 = n8 >= 0 ? (ItemInfo)this.mItems.get(n8) : null;
                            n3 = n8;
                        }
                    }
                    --n10;
                    n4 = n;
                    f2 = f;
                    object3 = object2;
                    n8 = n3;
                } while (true);
                for (n3 = this.mCurItem + 1; n3 < n6; ++n3) {
                    if (f2 >= f3 && n3 > n7) {
                        if (object3 == null) break;
                        f = f2;
                        object2 = object3;
                        n = n10;
                        if (n3 == object3.position) {
                            f = f2;
                            object2 = object3;
                            n = n10;
                            if (!object3.scrolling) {
                                this.mItems.remove(n10);
                                this.mAdapter.destroyItem(this, n3, object3.object);
                                if (n10 < this.mItems.size()) {
                                    object2 = (ItemInfo)this.mItems.get(n10);
                                    n = n10;
                                    f = f2;
                                } else {
                                    object2 = null;
                                    f = f2;
                                    n = n10;
                                }
                            }
                        }
                    } else if (object3 != null && n3 == object3.position) {
                        f = f2 + object3.widthFactor;
                        n = n10 + 1;
                        object2 = n < this.mItems.size() ? (ItemInfo)this.mItems.get(n) : null;
                    } else {
                        object2 = this.addNewItem(n3, n10);
                        n = n10 + 1;
                        f = f2 + object2.widthFactor;
                        object2 = n < this.mItems.size() ? (ItemInfo)this.mItems.get(n) : null;
                    }
                    f2 = f;
                    object3 = object2;
                    n10 = n;
                }
            }
            this.calculatePageOffsets(itemInfo, n4, (ItemInfo)object);
        }
        object = this.mAdapter;
        n = this.mCurItem;
        object2 = itemInfo != null ? itemInfo.object : null;
        object.setPrimaryItem(this, n, object2);
        this.mAdapter.finishUpdate(this);
        n3 = this.getChildCount();
        for (n = 0; n < n3; ++n) {
            object = this.getChildAt(n);
            object2 = (LayoutParams)object.getLayoutParams();
            object2.childIndex = n;
            if (object2.isDecor || object2.widthFactor != 0.0f || (object = this.infoForChild((View)object)) == null) continue;
            object2.widthFactor = object.widthFactor;
            object2.position = object.position;
        }
        this.sortChildDrawingOrder();
        if (!this.hasFocus()) return;
        object2 = this.findFocus();
        object2 = object2 != null ? this.infoForAnyChild((View)object2) : null;
        if (object2 != null) {
            if (object2.position == this.mCurItem) return;
        }
        n = 0;
        while (n < this.getChildCount()) {
            object2 = this.getChildAt(n);
            object = this.infoForChild((View)object2);
            if (object != null && object.position == this.mCurItem) {
                if (object2.requestFocus(n2)) return;
            }
            ++n;
        }
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
            return;
        }
        super.removeView(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setAdapter(PagerAdapter pagerAdapter) {
        Object object;
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); ++i) {
                object = (ItemInfo)this.mItems.get(i);
                this.mAdapter.destroyItem(this, object.position, object.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }
        object = this.mAdapter;
        this.mAdapter = pagerAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean bl = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!bl) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }
        if (this.mAdapterChangeListener != null && object != pagerAdapter) {
            this.mAdapterChangeListener.onAdapterChanged((PagerAdapter)object, pagerAdapter);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    void setChildrenDrawingOrderEnabledCompat(boolean bl) {
        if (Build.VERSION.SDK_INT < 7) return;
        if (this.mSetChildrenDrawingOrderEnabled == null) {
            try {
                this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.e((String)"ViewPager", (String)"Can't find setChildrenDrawingOrderEnabled", (Throwable)noSuchMethodException);
            }
        }
        try {
            this.mSetChildrenDrawingOrderEnabled.invoke((Object)this, new Object[]{bl});
            return;
        }
        catch (Exception exception) {
            Log.e((String)"ViewPager", (String)"Error changing children drawing order", (Throwable)exception);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setCurrentItem(int n) {
        this.mPopulatePending = false;
        boolean bl = !this.mFirstLayout;
        this.setCurrentItemInternal(n, bl, false);
    }

    public void setCurrentItem(int n, boolean bl) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, bl, false);
    }

    void setCurrentItemInternal(int n, boolean bl, boolean bl2) {
        this.setCurrentItemInternal(n, bl, bl2, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    void setCurrentItemInternal(int n, boolean bl, boolean bl2, int n2) {
        int n3;
        boolean bl3 = true;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if (!bl2 && this.mCurItem == n && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if (n < 0) {
            n3 = 0;
        } else {
            n3 = n;
            if (n >= this.mAdapter.getCount()) {
                n3 = this.mAdapter.getCount() - 1;
            }
        }
        if (n3 > this.mCurItem + (n = this.mOffscreenPageLimit) || n3 < this.mCurItem - n) {
            for (n = 0; n < this.mItems.size(); ++n) {
                ((ItemInfo)this.mItems.get((int)n)).scrolling = true;
            }
        }
        bl2 = this.mCurItem != n3 ? bl3 : false;
        if (!this.mFirstLayout) {
            this.populate(n3);
            this.scrollToItem(n3, bl, n2, bl2);
            return;
        }
        this.mCurItem = n3;
        if (bl2 && this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(n3);
        }
        if (bl2 && this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(n3);
        }
        this.requestLayout();
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener;
        return onPageChangeListener2;
    }

    public void setOffscreenPageLimit(int n) {
        int n2 = n;
        if (n < 1) {
            Log.w((String)"ViewPager", (String)("Requested offscreen page limit " + n + " too small; defaulting to " + 1));
            n2 = 1;
        }
        if (n2 != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = n2;
            this.populate();
        }
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        this.mAdapterChangeListener = onAdapterChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageMargin(int n) {
        int n2 = this.mPageMargin;
        this.mPageMargin = n;
        int n3 = this.getWidth();
        this.recomputeScrollPosition(n3, n3, n, n2);
        this.requestLayout();
    }

    public void setPageMarginDrawable(int n) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(n));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPageMarginDrawable(Drawable drawable2) {
        this.mMarginDrawable = drawable2;
        if (drawable2 != null) {
            this.refreshDrawableState();
        }
        boolean bl = drawable2 == null;
        this.setWillNotDraw(bl);
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPageTransformer(boolean bl, PageTransformer pageTransformer) {
        int n = 1;
        if (Build.VERSION.SDK_INT < 11) return;
        boolean bl2 = pageTransformer != null;
        boolean bl3 = this.mPageTransformer != null;
        boolean bl4 = bl2 != bl3;
        this.mPageTransformer = pageTransformer;
        this.setChildrenDrawingOrderEnabledCompat(bl2);
        if (bl2) {
            if (bl) {
                n = 2;
            }
            this.mDrawingOrder = n;
        } else {
            this.mDrawingOrder = 0;
        }
        if (bl4) {
            this.populate();
        }
    }

    void smoothScrollTo(int n, int n2) {
        this.smoothScrollTo(n, n2, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    void smoothScrollTo(int n, int n2, int n3) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        int n4 = this.getScrollX();
        int n5 = this.getScrollY();
        int n6 = n - n4;
        if (n6 == 0 && (n2 -= n5) == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        n = this.getClientWidth();
        int n7 = n / 2;
        float f = Math.min((float)1.0f, (float)(1.0f * (float)Math.abs((int)n6) / (float)n));
        float f2 = n7;
        float f3 = n7;
        f = this.distanceInfluenceForSnapDuration(f);
        n3 = Math.abs((int)n3);
        if (n3 > 0) {
            n = Math.round((float)(1000.0f * Math.abs((float)((f2 + f3 * f) / (float)n3)))) * 4;
        } else {
            f2 = n;
            f3 = this.mAdapter.getPageWidth(this.mCurItem);
            n = (int)((1.0f + (float)Math.abs((int)n6) / ((float)this.mPageMargin + f2 * f3)) * 100.0f);
        }
        n = Math.min((int)n, (int)600);
        this.mScroller.startScroll(n4, n5, n6, n2, n);
        ViewCompat.postInvalidateOnAnimation((View)this);
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (super.verifyDrawable(drawable2) || drawable2 == this.mMarginDrawable) {
            return true;
        }
        return false;
    }

    static interface Decor {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams
    extends ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
            this.gravity = context.getInteger(0, 48);
            context.recycle();
        }
    }

    class MyAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        private boolean canScroll() {
            if (ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1) {
                return true;
            }
            return false;
        }

        @Override
        public void onInitializeAccessibilityEvent(View object, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent((View)object, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ViewPager.class.getName());
            object = AccessibilityRecordCompat.obtain();
            object.setScrollable(this.canScroll());
            if (accessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                object.setItemCount(ViewPager.this.mAdapter.getCount());
                object.setFromIndex(ViewPager.this.mCurItem);
                object.setToIndex(ViewPager.this.mCurItem);
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
            accessibilityNodeInfoCompat.setScrollable(this.canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
            }
        }

        @Override
        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
            if (super.performAccessibilityAction(view, n, bundle)) {
                return true;
            }
            switch (n) {
                default: {
                    return false;
                }
                case 4096: {
                    if (ViewPager.this.canScrollHorizontally(1)) {
                        ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                        return true;
                    }
                    return false;
                }
                case 8192: 
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                return true;
            }
            return false;
        }
    }

    static interface OnAdapterChangeListener {
        public void onAdapterChanged(PagerAdapter var1, PagerAdapter var2);
    }

    public static interface OnPageChangeListener {
        public void onPageScrollStateChanged(int var1);

        public void onPageScrolled(int var1, float var2, int var3);

        public void onPageSelected(int var1);
    }

    public static interface PageTransformer {
        public void transformPage(View var1, float var2);
    }

    private class PagerObserver
    extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>(){

            @Override
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        });
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel);
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader2);
            this.loader = classLoader2;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString((int)System.identityHashCode((Object)((Object)this))) + " position=" + this.position + "}";
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, n);
        }

    }

    public static class SimpleOnPageChangeListener
    implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int n) {
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
        }

        @Override
        public void onPageSelected(int n) {
        }
    }

    static class ViewPositionComparator
    implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View object, View object2) {
            object = (LayoutParams)object.getLayoutParams();
            object2 = (LayoutParams)object2.getLayoutParams();
            if (object.isDecor != object2.isDecor) {
                if (object.isDecor) {
                    return 1;
                }
                return -1;
            }
            return object.position - object2.position;
        }
    }

}

