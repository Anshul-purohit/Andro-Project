package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
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
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager
  extends ViewGroup
{
  private static final int CLOSE_ENOUGH = 2;
  private static final Comparator<ItemInfo> COMPARATOR = new Comparator()
  {
    public int compare(ViewPager.ItemInfo paramAnonymousItemInfo1, ViewPager.ItemInfo paramAnonymousItemInfo2)
    {
      return position - position;
    }
  };
  private static final boolean DEBUG = false;
  private static final int DEFAULT_GUTTER_SIZE = 16;
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  private static final int INVALID_POINTER = -1;
  private static final int[] LAYOUT_ATTRS = { 16842931 };
  private static final int MAX_SETTLE_DURATION = 600;
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  private static final String TAG = "ViewPager";
  private static final boolean USE_CACHE = false;
  private static final Interpolator sInterpolator = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      paramAnonymousFloat -= 1.0F;
      return paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat + 1.0F;
    }
  };
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
  private long mFakeDragBeginTime;
  private boolean mFakeDragging;
  private boolean mFirstLayout = true;
  private float mFirstOffset = -3.4028235E38F;
  private int mFlingDistance;
  private int mGutterSize;
  private boolean mIgnoreGutter;
  private boolean mInLayout;
  private float mInitialMotionX;
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
  private boolean mPopulatePending;
  private Parcelable mRestoredAdapterState = null;
  private ClassLoader mRestoredClassLoader = null;
  private int mRestoredCurItem = -1;
  private EdgeEffectCompat mRightEdge;
  private int mScrollState = 0;
  private Scroller mScroller;
  private boolean mScrollingCacheEnabled;
  private final ItemInfo mTempItem = new ItemInfo();
  private final Rect mTempRect = new Rect();
  private int mTopPageBounds;
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;
  
  public ViewPager(Context paramContext)
  {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = mAdapter.getCount();
    int j = getWidth();
    float f1;
    if (j > 0)
    {
      f1 = mPageMargin / j;
      if (paramItemInfo2 == null) {
        break label391;
      }
      j = position;
      if (j < position)
      {
        k = 0;
        f2 = offset + widthFactor + f1;
        j++;
      }
    }
    else
    {
      for (;;)
      {
        if ((j > position) || (k >= mItems.size())) {
          break label391;
        }
        for (paramItemInfo2 = (ItemInfo)mItems.get(k);; paramItemInfo2 = (ItemInfo)mItems.get(k))
        {
          f3 = f2;
          m = j;
          if (j <= position) {
            break;
          }
          f3 = f2;
          m = j;
          if (k >= mItems.size() - 1) {
            break;
          }
          k++;
        }
        f1 = 0.0F;
        break;
        while (m < position)
        {
          f3 += mAdapter.getPageWidth(m) + f1;
          m++;
        }
        offset = f3;
        f2 = f3 + (widthFactor + f1);
        j = m + 1;
      }
    }
    if (j > position)
    {
      k = mItems.size() - 1;
      f2 = offset;
      j--;
      while ((j >= position) && (k >= 0))
      {
        for (paramItemInfo2 = (ItemInfo)mItems.get(k);; paramItemInfo2 = (ItemInfo)mItems.get(k))
        {
          f3 = f2;
          m = j;
          if (j >= position) {
            break;
          }
          f3 = f2;
          m = j;
          if (k <= 0) {
            break;
          }
          k--;
        }
        while (m > position)
        {
          f3 -= mAdapter.getPageWidth(m) + f1;
          m--;
        }
        f2 = f3 - (widthFactor + f1);
        offset = f2;
        j = m - 1;
      }
    }
    label391:
    int m = mItems.size();
    float f3 = offset;
    j = position - 1;
    if (position == 0)
    {
      f2 = offset;
      mFirstOffset = f2;
      if (position != i - 1) {
        break label529;
      }
      f2 = offset + widthFactor - 1.0F;
      label457:
      mLastOffset = f2;
      k = paramInt - 1;
      f2 = f3;
    }
    for (;;)
    {
      if (k < 0) {
        break label576;
      }
      paramItemInfo2 = (ItemInfo)mItems.get(k);
      for (;;)
      {
        if (j > position)
        {
          f2 -= mAdapter.getPageWidth(j) + f1;
          j--;
          continue;
          f2 = -3.4028235E38F;
          break;
          label529:
          f2 = Float.MAX_VALUE;
          break label457;
        }
      }
      f2 -= widthFactor + f1;
      offset = f2;
      if (position == 0) {
        mFirstOffset = f2;
      }
      k--;
      j--;
    }
    label576:
    float f2 = offset + widthFactor + f1;
    int k = position + 1;
    j = paramInt + 1;
    for (paramInt = k; j < m; paramInt++)
    {
      paramItemInfo1 = (ItemInfo)mItems.get(j);
      while (paramInt < position)
      {
        f2 += mAdapter.getPageWidth(paramInt) + f1;
        paramInt++;
      }
      if (position == i - 1) {
        mLastOffset = (widthFactor + f2 - 1.0F);
      }
      offset = f2;
      f2 += widthFactor + f1;
      j++;
    }
    mNeedCalculatePageOffsets = false;
  }
  
  private void completeScroll()
  {
    if (mScrollState == 2) {}
    int j;
    for (int i = 1;; i = 0)
    {
      if (i != 0)
      {
        setScrollingCacheEnabled(false);
        mScroller.abortAnimation();
        j = getScrollX();
        k = getScrollY();
        int m = mScroller.getCurrX();
        int n = mScroller.getCurrY();
        if ((j != m) || (k != n)) {
          scrollTo(m, n);
        }
        setScrollState(0);
      }
      mPopulatePending = false;
      int k = 0;
      j = i;
      for (i = k; i < mItems.size(); i++)
      {
        ItemInfo localItemInfo = (ItemInfo)mItems.get(i);
        if (scrolling)
        {
          j = 1;
          scrolling = false;
        }
      }
    }
    if (j != 0) {
      populate();
    }
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    if ((Math.abs(paramInt3) > mFlingDistance) && (Math.abs(paramInt2) > mMinimumVelocity)) {
      if (paramInt2 <= 0) {}
    }
    for (;;)
    {
      paramInt2 = paramInt1;
      if (mItems.size() > 0)
      {
        ItemInfo localItemInfo1 = (ItemInfo)mItems.get(0);
        ItemInfo localItemInfo2 = (ItemInfo)mItems.get(mItems.size() - 1);
        paramInt2 = Math.max(position, Math.min(paramInt1, position));
      }
      return paramInt2;
      paramInt1++;
      continue;
      paramInt1 = (int)(paramInt1 + paramFloat + 0.5F);
    }
  }
  
  private void endDrag()
  {
    mIsBeingDragged = false;
    mIsUnableToDrag = false;
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView)
  {
    Rect localRect = paramRect;
    if (paramRect == null) {
      localRect = new Rect();
    }
    if (paramView == null) {
      localRect.set(0, 0, 0, 0);
    }
    for (;;)
    {
      return localRect;
      left = paramView.getLeft();
      right = paramView.getRight();
      top = paramView.getTop();
      bottom = paramView.getBottom();
      for (paramRect = paramView.getParent(); ((paramRect instanceof ViewGroup)) && (paramRect != this); paramRect = paramRect.getParent())
      {
        paramRect = (ViewGroup)paramRect;
        left += paramRect.getLeft();
        right += paramRect.getRight();
        top += paramRect.getTop();
        bottom += paramRect.getBottom();
      }
    }
  }
  
  private ItemInfo infoForCurrentScrollPosition()
  {
    float f1 = 0.0F;
    int i = getWidth();
    float f2;
    int j;
    float f3;
    float f4;
    int k;
    Object localObject1;
    if (i > 0)
    {
      f2 = getScrollX() / i;
      if (i > 0) {
        f1 = mPageMargin / i;
      }
      j = -1;
      f3 = 0.0F;
      f4 = 0.0F;
      k = 1;
      localObject1 = null;
      i = 0;
    }
    for (;;)
    {
      Object localObject2 = localObject1;
      int m;
      Object localObject3;
      if (i < mItems.size())
      {
        localObject2 = (ItemInfo)mItems.get(i);
        m = i;
        localObject3 = localObject2;
        if (k == 0)
        {
          m = i;
          localObject3 = localObject2;
          if (position != j + 1)
          {
            localObject3 = mTempItem;
            offset = (f3 + f4 + f1);
            position = (j + 1);
            widthFactor = mAdapter.getPageWidth(position);
            m = i - 1;
          }
        }
        f3 = offset;
        f4 = widthFactor;
        if (k == 0)
        {
          localObject2 = localObject1;
          if (f2 < f3) {}
        }
        else
        {
          if ((f2 >= f4 + f3 + f1) && (m != mItems.size() - 1)) {
            break label226;
          }
          localObject2 = localObject3;
        }
      }
      return (ItemInfo)localObject2;
      f2 = 0.0F;
      break;
      label226:
      k = 0;
      j = position;
      f4 = widthFactor;
      i = m + 1;
      localObject1 = localObject3;
    }
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2)
  {
    if (((paramFloat1 < mGutterSize) && (paramFloat2 > 0.0F)) || ((paramFloat1 > getWidth() - mGutterSize) && (paramFloat2 < 0.0F))) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (MotionEventCompat.getPointerId(paramMotionEvent, i) == mActivePointerId) {
      if (i != 0) {
        break label56;
      }
    }
    label56:
    for (i = 1;; i = 0)
    {
      mLastMotionX = MotionEventCompat.getX(paramMotionEvent, i);
      mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
      if (mVelocityTracker != null) {
        mVelocityTracker.clear();
      }
      return;
    }
  }
  
  private boolean pageScrolled(int paramInt)
  {
    boolean bool = false;
    if (mItems.size() == 0)
    {
      mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (!mCalledSuper) {
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
      }
    }
    else
    {
      ItemInfo localItemInfo = infoForCurrentScrollPosition();
      int i = getWidth();
      int j = mPageMargin;
      float f = mPageMargin / i;
      int k = position;
      f = (paramInt / i - offset) / (widthFactor + f);
      paramInt = (int)((i + j) * f);
      mCalledSuper = false;
      onPageScrolled(k, f, paramInt);
      if (!mCalledSuper) {
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
      }
      bool = true;
    }
    return bool;
  }
  
  private boolean performDrag(float paramFloat)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    float f1 = mLastMotionX;
    mLastMotionX = paramFloat;
    float f2 = getScrollX() + (f1 - paramFloat);
    int i = getWidth();
    paramFloat = i * mFirstOffset;
    f1 = i * mLastOffset;
    int j = 1;
    int k = 1;
    ItemInfo localItemInfo1 = (ItemInfo)mItems.get(0);
    ItemInfo localItemInfo2 = (ItemInfo)mItems.get(mItems.size() - 1);
    if (position != 0)
    {
      j = 0;
      paramFloat = offset * i;
    }
    if (position != mAdapter.getCount() - 1)
    {
      k = 0;
      f1 = offset * i;
    }
    if (f2 < paramFloat) {
      if (j != 0) {
        bool3 = mLeftEdge.onPull(Math.abs(paramFloat - f2) / i);
      }
    }
    for (;;)
    {
      mLastMotionX += paramFloat - (int)paramFloat;
      scrollTo((int)paramFloat, getScrollY());
      pageScrolled((int)paramFloat);
      return bool3;
      bool3 = bool1;
      paramFloat = f2;
      if (f2 > f1)
      {
        bool3 = bool2;
        if (k != 0) {
          bool3 = mRightEdge.onPull(Math.abs(f2 - f1) / i);
        }
        paramFloat = f1;
      }
    }
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt2 > 0) && (!mItems.isEmpty()))
    {
      f = getScrollX() / (paramInt2 + paramInt4);
      paramInt4 = (int)((paramInt1 + paramInt3) * f);
      scrollTo(paramInt4, getScrollY());
      if (!mScroller.isFinished())
      {
        paramInt2 = mScroller.getDuration();
        paramInt3 = mScroller.timePassed();
        localItemInfo = infoForPosition(mCurItem);
        mScroller.startScroll(paramInt4, 0, (int)(offset * paramInt1), 0, paramInt2 - paramInt3);
      }
      return;
    }
    ItemInfo localItemInfo = infoForPosition(mCurItem);
    if (localItemInfo != null) {}
    for (float f = Math.min(offset, mLastOffset);; f = 0.0F)
    {
      paramInt1 = (int)(paramInt1 * f);
      if (paramInt1 == getScrollX()) {
        break;
      }
      completeScroll();
      scrollTo(paramInt1, getScrollY());
      break;
    }
  }
  
  private void removeNonDecorViews()
  {
    int j;
    for (int i = 0; i < getChildCount(); i = j + 1)
    {
      j = i;
      if (!getChildAtgetLayoutParamsisDecor)
      {
        removeViewAt(i);
        j = i - 1;
      }
    }
  }
  
  private void setScrollState(int paramInt)
  {
    if (mScrollState == paramInt) {}
    for (;;)
    {
      return;
      mScrollState = paramInt;
      if (mOnPageChangeListener != null) {
        mOnPageChangeListener.onPageScrollStateChanged(paramInt);
      }
    }
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if (mScrollingCacheEnabled != paramBoolean) {
      mScrollingCacheEnabled = paramBoolean;
    }
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216) {
      for (int k = 0; k < getChildCount(); k++)
      {
        View localView = getChildAt(k);
        if (localView.getVisibility() == 0)
        {
          ItemInfo localItemInfo = infoForChild(localView);
          if ((localItemInfo != null) && (position == mCurItem)) {
            localView.addFocusables(paramArrayList, paramInt1, paramInt2);
          }
        }
      }
    }
    if (((j == 262144) && (i != paramArrayList.size())) || (!isFocusable())) {}
    for (;;)
    {
      return;
      if ((((paramInt2 & 0x1) != 1) || (!isInTouchMode()) || (isFocusableInTouchMode())) && (paramArrayList != null)) {
        paramArrayList.add(this);
      }
    }
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2)
  {
    ItemInfo localItemInfo = new ItemInfo();
    position = paramInt1;
    object = mAdapter.instantiateItem(this, paramInt1);
    widthFactor = mAdapter.getPageWidth(paramInt1);
    if ((paramInt2 < 0) || (paramInt2 >= mItems.size())) {
      mItems.add(localItemInfo);
    }
    for (;;)
    {
      return localItemInfo;
      mItems.add(paramInt2, localItemInfo);
    }
  }
  
  public void addTouchables(ArrayList<View> paramArrayList)
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (position == mCurItem)) {
          localView.addTouchables(paramArrayList);
        }
      }
    }
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    ViewGroup.LayoutParams localLayoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams)) {
      localLayoutParams = generateLayoutParams(paramLayoutParams);
    }
    paramLayoutParams = (LayoutParams)localLayoutParams;
    isDecor |= paramView instanceof Decor;
    if (mInLayout)
    {
      if ((paramLayoutParams != null) && (isDecor)) {
        throw new IllegalStateException("Cannot add pager decor view during layout");
      }
      needsMeasure = true;
      addViewInLayout(paramView, paramInt, localLayoutParams);
    }
    for (;;)
    {
      return;
      super.addView(paramView, paramInt, localLayoutParams);
    }
  }
  
  public boolean arrowScroll(int paramInt)
  {
    View localView1 = findFocus();
    View localView2 = localView1;
    if (localView1 == this) {
      localView2 = null;
    }
    boolean bool = false;
    localView1 = FocusFinder.getInstance().findNextFocus(this, localView2, paramInt);
    int i;
    int j;
    if ((localView1 != null) && (localView1 != localView2)) {
      if (paramInt == 17)
      {
        i = getChildRectInPagerCoordinatesmTempRect, localView1).left;
        j = getChildRectInPagerCoordinatesmTempRect, localView2).left;
        if ((localView2 != null) && (i >= j)) {
          bool = pageLeft();
        }
      }
    }
    for (;;)
    {
      if (bool) {
        playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
      }
      return bool;
      bool = localView1.requestFocus();
      continue;
      if (paramInt == 66)
      {
        i = getChildRectInPagerCoordinatesmTempRect, localView1).left;
        j = getChildRectInPagerCoordinatesmTempRect, localView2).left;
        if ((localView2 != null) && (i <= j))
        {
          bool = pageRight();
        }
        else
        {
          bool = localView1.requestFocus();
          continue;
          if ((paramInt == 17) || (paramInt == 1)) {
            bool = pageLeft();
          } else if ((paramInt == 66) || (paramInt == 2)) {
            bool = pageRight();
          }
        }
      }
    }
  }
  
  public boolean beginFakeDrag()
  {
    boolean bool = false;
    if (mIsBeingDragged) {
      return bool;
    }
    mFakeDragging = true;
    setScrollState(1);
    mLastMotionX = 0.0F;
    mInitialMotionX = 0.0F;
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    for (;;)
    {
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
      mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      mFakeDragBeginTime = l;
      bool = true;
      break;
      mVelocityTracker.clear();
    }
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    int k;
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      k = localViewGroup.getChildCount() - 1;
      if (k >= 0)
      {
        View localView = localViewGroup.getChildAt(k);
        if ((paramInt2 + i >= localView.getLeft()) && (paramInt2 + i < localView.getRight()) && (paramInt3 + j >= localView.getTop()) && (paramInt3 + j < localView.getBottom()) && (canScroll(localView, true, paramInt1, paramInt2 + i - localView.getLeft(), paramInt3 + j - localView.getTop()))) {
          paramBoolean = true;
        }
      }
    }
    for (;;)
    {
      return paramBoolean;
      k--;
      break;
      if ((paramBoolean) && (ViewCompat.canScrollHorizontally(paramView, -paramInt1))) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
    }
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams))) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public void computeScroll()
  {
    if ((!mScroller.isFinished()) && (mScroller.computeScrollOffset()))
    {
      int i = getScrollX();
      int j = getScrollY();
      int k = mScroller.getCurrX();
      int m = mScroller.getCurrY();
      if ((i != k) || (j != m))
      {
        scrollTo(k, m);
        if (!pageScrolled(k))
        {
          mScroller.abortAnimation();
          scrollTo(0, m);
        }
      }
      ViewCompat.postInvalidateOnAnimation(this);
    }
    for (;;)
    {
      return;
      completeScroll();
    }
  }
  
  void dataSetChanged()
  {
    int i;
    int j;
    int k;
    int m;
    label47:
    Object localObject;
    int n;
    int i1;
    int i2;
    int i3;
    if ((mItems.size() < mOffscreenPageLimit * 2 + 1) && (mItems.size() < mAdapter.getCount()))
    {
      i = 1;
      j = mCurItem;
      k = 0;
      m = 0;
      if (m >= mItems.size()) {
        break label299;
      }
      localObject = (ItemInfo)mItems.get(m);
      n = mAdapter.getItemPosition(object);
      if (n != -1) {
        break label123;
      }
      i1 = j;
      i2 = k;
      i3 = m;
    }
    for (;;)
    {
      m = i3 + 1;
      k = i2;
      j = i1;
      break label47;
      i = 0;
      break;
      label123:
      if (n == -2)
      {
        mItems.remove(m);
        n = m - 1;
        m = k;
        if (k == 0)
        {
          mAdapter.startUpdate(this);
          m = 1;
        }
        mAdapter.destroyItem(this, position, object);
        i = 1;
        i3 = n;
        i2 = m;
        i1 = j;
        if (mCurItem == position)
        {
          i1 = Math.max(0, Math.min(mCurItem, mAdapter.getCount() - 1));
          i = 1;
          i3 = n;
          i2 = m;
        }
      }
      else
      {
        i3 = m;
        i2 = k;
        i1 = j;
        if (position != n)
        {
          if (position == mCurItem) {
            j = n;
          }
          position = n;
          i = 1;
          i3 = m;
          i2 = k;
          i1 = j;
        }
      }
    }
    label299:
    if (k != 0) {
      mAdapter.finishUpdate(this);
    }
    Collections.sort(mItems, COMPARATOR);
    if (i != 0)
    {
      k = getChildCount();
      for (i = 0; i < k; i++)
      {
        localObject = (LayoutParams)getChildAt(i).getLayoutParams();
        if (!isDecor) {
          widthFactor = 0.0F;
        }
      }
      setCurrentItemInternal(j, false, true);
      requestLayout();
    }
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if ((super.dispatchKeyEvent(paramKeyEvent)) || (executeKeyEvent(paramKeyEvent))) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    int i = getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo == null) || (position != mCurItem) || (!localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))) {}
      }
    }
    for (boolean bool = true;; bool = false)
    {
      return bool;
      j++;
      break;
    }
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    int i = 0;
    int j = 0;
    int m = ViewCompat.getOverScrollMode(this);
    boolean bool;
    if ((m == 0) || ((m == 1) && (mAdapter != null) && (mAdapter.getCount() > 1)))
    {
      int k;
      if (!mLeftEdge.isFinished())
      {
        i = paramCanvas.save();
        j = getHeight() - getPaddingTop() - getPaddingBottom();
        m = getWidth();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-j + getPaddingTop(), mFirstOffset * m);
        mLeftEdge.setSize(j, m);
        k = false | mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(i);
      }
      i = k;
      if (!mRightEdge.isFinished())
      {
        m = paramCanvas.save();
        int n = getWidth();
        int i1 = getHeight();
        int i2 = getPaddingTop();
        i = getPaddingBottom();
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), -(mLastOffset + 1.0F) * n);
        mRightEdge.setSize(i1 - i2 - i, n);
        bool = k | mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(m);
      }
    }
    for (;;)
    {
      if (bool) {
        ViewCompat.postInvalidateOnAnimation(this);
      }
      return;
      mLeftEdge.finish();
      mRightEdge.finish();
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mMarginDrawable;
    if ((localDrawable != null) && (localDrawable.isStateful())) {
      localDrawable.setState(getDrawableState());
    }
  }
  
  public void endFakeDrag()
  {
    if (!mFakeDragging) {
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }
    Object localObject = mVelocityTracker;
    ((VelocityTracker)localObject).computeCurrentVelocity(1000, mMaximumVelocity);
    int i = (int)VelocityTrackerCompat.getXVelocity((VelocityTracker)localObject, mActivePointerId);
    mPopulatePending = true;
    int j = getWidth();
    int k = getScrollX();
    localObject = infoForCurrentScrollPosition();
    setCurrentItemInternal(determineTargetPage(position, (k / j - offset) / widthFactor, i, (int)(mLastMotionX - mInitialMotionX)), true, true, i);
    endDrag();
    mFakeDragging = false;
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramKeyEvent.getAction() == 0) {
      switch (paramKeyEvent.getKeyCode())
      {
      default: 
        bool2 = bool1;
      }
    }
    for (;;)
    {
      return bool2;
      bool2 = arrowScroll(17);
      continue;
      bool2 = arrowScroll(66);
      continue;
      bool2 = bool1;
      if (Build.VERSION.SDK_INT >= 11) {
        if (KeyEventCompat.hasNoModifiers(paramKeyEvent))
        {
          bool2 = arrowScroll(2);
        }
        else
        {
          bool2 = bool1;
          if (KeyEventCompat.hasModifiers(paramKeyEvent, 1)) {
            bool2 = arrowScroll(1);
          }
        }
      }
    }
  }
  
  public void fakeDragBy(float paramFloat)
  {
    if (!mFakeDragging) {
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }
    mLastMotionX += paramFloat;
    float f1 = getScrollX() - paramFloat;
    int i = getWidth();
    paramFloat = i * mFirstOffset;
    float f2 = i * mLastOffset;
    Object localObject = (ItemInfo)mItems.get(0);
    ItemInfo localItemInfo = (ItemInfo)mItems.get(mItems.size() - 1);
    if (position != 0) {
      paramFloat = offset * i;
    }
    if (position != mAdapter.getCount() - 1) {
      f2 = offset * i;
    }
    if (f1 < paramFloat) {}
    for (;;)
    {
      mLastMotionX += paramFloat - (int)paramFloat;
      scrollTo((int)paramFloat, getScrollY());
      pageScrolled((int)paramFloat);
      long l = SystemClock.uptimeMillis();
      localObject = MotionEvent.obtain(mFakeDragBeginTime, l, 2, mLastMotionX, 0.0F, 0);
      mVelocityTracker.addMovement((MotionEvent)localObject);
      ((MotionEvent)localObject).recycle();
      return;
      paramFloat = f1;
      if (f1 > f2) {
        paramFloat = f2;
      }
    }
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter()
  {
    return mAdapter;
  }
  
  public int getCurrentItem()
  {
    return mCurItem;
  }
  
  public int getOffscreenPageLimit()
  {
    return mOffscreenPageLimit;
  }
  
  public int getPageMargin()
  {
    return mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView)
  {
    ViewParent localViewParent = paramView.getParent();
    if (localViewParent != this) {
      if ((localViewParent != null) && ((localViewParent instanceof View))) {}
    }
    for (paramView = null;; paramView = infoForChild(paramView))
    {
      return paramView;
      paramView = (View)localViewParent;
      break;
    }
  }
  
  ItemInfo infoForChild(View paramView)
  {
    int i = 0;
    ItemInfo localItemInfo;
    if (i < mItems.size())
    {
      localItemInfo = (ItemInfo)mItems.get(i);
      if (!mAdapter.isViewFromObject(paramView, object)) {}
    }
    for (paramView = localItemInfo;; paramView = null)
    {
      return paramView;
      i++;
      break;
    }
  }
  
  ItemInfo infoForPosition(int paramInt)
  {
    int i = 0;
    ItemInfo localItemInfo;
    if (i < mItems.size())
    {
      localItemInfo = (ItemInfo)mItems.get(i);
      if (position != paramInt) {}
    }
    for (;;)
    {
      return localItemInfo;
      i++;
      break;
      localItemInfo = null;
    }
  }
  
  void initViewPager()
  {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context localContext = getContext();
    mScroller = new Scroller(localContext, sInterpolator);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(localViewConfiguration);
    mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    mLeftEdge = new EdgeEffectCompat(localContext);
    mRightEdge = new EdgeEffectCompat(localContext);
    float f = getResourcesgetDisplayMetricsdensity;
    mFlingDistance = ((int)(25.0F * f));
    mCloseEnough = ((int)(2.0F * f));
    mDefaultGutterSize = ((int)(16.0F * f));
    ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility(this) == 0) {
      ViewCompat.setImportantForAccessibility(this, 1);
    }
  }
  
  public boolean isFakeDragging()
  {
    return mFakeDragging;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mFirstLayout = true;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i;
    int j;
    float f1;
    int k;
    Object localObject;
    float f2;
    int m;
    int n;
    int i1;
    if ((mPageMargin > 0) && (mMarginDrawable != null) && (mItems.size() > 0) && (mAdapter != null))
    {
      i = getScrollX();
      j = getWidth();
      f1 = mPageMargin / j;
      k = 0;
      localObject = (ItemInfo)mItems.get(0);
      f2 = offset;
      m = mItems.size();
      n = position;
      i1 = mItems.get(m - 1)).position;
    }
    for (;;)
    {
      float f3;
      if (n < i1)
      {
        while ((n > position) && (k < m))
        {
          localObject = mItems;
          k++;
          localObject = (ItemInfo)((ArrayList)localObject).get(k);
        }
        if (n != position) {
          break label268;
        }
        f3 = (offset + widthFactor) * j;
      }
      label268:
      float f4;
      for (f2 = offset + widthFactor + f1;; f2 += f4 + f1)
      {
        if (mPageMargin + f3 > i)
        {
          mMarginDrawable.setBounds((int)f3, mTopPageBounds, (int)(mPageMargin + f3 + 0.5F), mBottomPageBounds);
          mMarginDrawable.draw(paramCanvas);
        }
        if (f3 <= i + j) {
          break;
        }
        return;
        f4 = mAdapter.getPageWidth(n);
        f3 = (f2 + f4) * j;
      }
      n++;
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction() & 0xFF;
    boolean bool;
    if ((i == 3) || (i == 1))
    {
      mIsBeingDragged = false;
      mIsUnableToDrag = false;
      mActivePointerId = -1;
      if (mVelocityTracker != null)
      {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
      }
      bool = false;
    }
    for (;;)
    {
      return bool;
      if (i == 0) {
        break;
      }
      if (mIsBeingDragged)
      {
        bool = true;
      }
      else
      {
        if (!mIsUnableToDrag) {
          break;
        }
        bool = false;
      }
    }
    switch (i)
    {
    }
    for (;;)
    {
      if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
      }
      mVelocityTracker.addMovement(paramMotionEvent);
      bool = mIsBeingDragged;
      break;
      i = mActivePointerId;
      if (i != -1)
      {
        i = MotionEventCompat.findPointerIndex(paramMotionEvent, i);
        float f1 = MotionEventCompat.getX(paramMotionEvent, i);
        float f2 = f1 - mLastMotionX;
        float f3 = Math.abs(f2);
        float f4 = MotionEventCompat.getY(paramMotionEvent, i);
        float f5 = Math.abs(f4 - mLastMotionY);
        if ((f2 != 0.0F) && (!isGutterDrag(mLastMotionX, f2)) && (canScroll(this, false, (int)f2, (int)f1, (int)f4)))
        {
          mLastMotionX = f1;
          mInitialMotionX = f1;
          mLastMotionY = f4;
          mIsUnableToDrag = true;
          bool = false;
          break;
        }
        if ((f3 > mTouchSlop) && (f3 > f5))
        {
          mIsBeingDragged = true;
          setScrollState(1);
          if (f2 > 0.0F)
          {
            f5 = mInitialMotionX + mTouchSlop;
            label322:
            mLastMotionX = f5;
            setScrollingCacheEnabled(true);
          }
        }
        while ((mIsBeingDragged) && (performDrag(f1)))
        {
          ViewCompat.postInvalidateOnAnimation(this);
          break;
          f5 = mInitialMotionX - mTouchSlop;
          break label322;
          if (f5 > mTouchSlop) {
            mIsUnableToDrag = true;
          }
        }
        f5 = paramMotionEvent.getX();
        mInitialMotionX = f5;
        mLastMotionX = f5;
        mLastMotionY = paramMotionEvent.getY();
        mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
        mIsUnableToDrag = false;
        mScroller.computeScrollOffset();
        if ((mScrollState == 2) && (Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough))
        {
          mScroller.abortAnimation();
          mPopulatePending = false;
          populate();
          mIsBeingDragged = true;
          setScrollState(1);
        }
        else
        {
          completeScroll();
          mIsBeingDragged = false;
          continue;
          onSecondaryPointerUp(paramMotionEvent);
        }
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mInLayout = true;
    populate();
    mInLayout = false;
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    paramInt2 = getPaddingLeft();
    paramInt1 = getPaddingTop();
    int m = getPaddingRight();
    paramInt4 = getPaddingBottom();
    int n = getScrollX();
    int i1 = 0;
    int i2 = 0;
    View localView;
    int i5;
    Object localObject;
    if (i2 < i)
    {
      localView = getChildAt(i2);
      int i3 = i1;
      int i4 = paramInt4;
      i5 = paramInt2;
      int i6 = m;
      paramInt3 = paramInt1;
      if (localView.getVisibility() != 8)
      {
        localObject = (LayoutParams)localView.getLayoutParams();
        i3 = i1;
        i4 = paramInt4;
        i5 = paramInt2;
        i6 = m;
        paramInt3 = paramInt1;
        if (isDecor)
        {
          paramInt3 = gravity;
          i6 = gravity;
          switch (paramInt3 & 0x7)
          {
          case 2: 
          case 4: 
          default: 
            paramInt3 = paramInt2;
            i5 = paramInt2;
            label206:
            switch (i6 & 0x70)
            {
            default: 
              paramInt2 = paramInt1;
            }
            break;
          }
        }
      }
      for (;;)
      {
        paramInt3 += n;
        localView.layout(paramInt3, paramInt2, localView.getMeasuredWidth() + paramInt3, localView.getMeasuredHeight() + paramInt2);
        i3 = i1 + 1;
        paramInt3 = paramInt1;
        i6 = m;
        i4 = paramInt4;
        i2++;
        i1 = i3;
        paramInt4 = i4;
        paramInt2 = i5;
        m = i6;
        paramInt1 = paramInt3;
        break;
        paramInt3 = paramInt2;
        i5 = paramInt2 + localView.getMeasuredWidth();
        break label206;
        paramInt3 = Math.max((j - localView.getMeasuredWidth()) / 2, paramInt2);
        i5 = paramInt2;
        break label206;
        paramInt3 = j - m - localView.getMeasuredWidth();
        m += localView.getMeasuredWidth();
        i5 = paramInt2;
        break label206;
        paramInt2 = paramInt1;
        paramInt1 += localView.getMeasuredHeight();
        continue;
        paramInt2 = Math.max((k - localView.getMeasuredHeight()) / 2, paramInt1);
        continue;
        paramInt2 = k - paramInt4 - localView.getMeasuredHeight();
        paramInt4 += localView.getMeasuredHeight();
      }
    }
    for (paramInt3 = 0; paramInt3 < i; paramInt3++)
    {
      localView = getChildAt(paramInt3);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (!isDecor)
        {
          localObject = infoForChild(localView);
          if (localObject != null)
          {
            i5 = paramInt2 + (int)(j * offset);
            if (needsMeasure)
            {
              needsMeasure = false;
              localView.measure(View.MeasureSpec.makeMeasureSpec((int)((j - paramInt2 - m) * widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(k - paramInt1 - paramInt4, 1073741824));
            }
            localView.layout(i5, paramInt1, localView.getMeasuredWidth() + i5, localView.getMeasuredHeight() + paramInt1);
          }
        }
      }
    }
    mTopPageBounds = paramInt1;
    mBottomPageBounds = (k - paramInt4);
    mDecorChildCount = i1;
    mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    paramInt1 = getMeasuredWidth();
    mGutterSize = Math.min(paramInt1 / 10, mDefaultGutterSize);
    paramInt1 = paramInt1 - getPaddingLeft() - getPaddingRight();
    paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int i = getChildCount();
    int j = 0;
    View localView;
    LayoutParams localLayoutParams;
    if (j < i)
    {
      localView = getChildAt(j);
      k = paramInt2;
      int m = paramInt1;
      int n;
      int i1;
      label181:
      int i2;
      if (localView.getVisibility() != 8)
      {
        localLayoutParams = (LayoutParams)localView.getLayoutParams();
        k = paramInt2;
        m = paramInt1;
        if (localLayoutParams != null)
        {
          k = paramInt2;
          m = paramInt1;
          if (isDecor)
          {
            m = gravity & 0x7;
            n = gravity & 0x70;
            i1 = Integer.MIN_VALUE;
            k = Integer.MIN_VALUE;
            if ((n != 48) && (n != 80)) {
              break label358;
            }
            n = 1;
            if ((m != 3) && (m != 5)) {
              break label364;
            }
            i2 = 1;
            label196:
            if (n == 0) {
              break label370;
            }
            m = 1073741824;
            label206:
            int i3 = paramInt1;
            i1 = paramInt2;
            int i4 = i3;
            int i5;
            if (width != -2)
            {
              i5 = 1073741824;
              m = i5;
              i4 = i3;
              if (width != -1)
              {
                i4 = width;
                m = i5;
              }
            }
            i3 = k;
            k = i1;
            if (height != -2)
            {
              i5 = 1073741824;
              i3 = i5;
              k = i1;
              if (height != -1)
              {
                k = height;
                i3 = i5;
              }
            }
            localView.measure(View.MeasureSpec.makeMeasureSpec(i4, m), View.MeasureSpec.makeMeasureSpec(k, i3));
            if (n == 0) {
              break label391;
            }
            k = paramInt2 - localView.getMeasuredHeight();
            m = paramInt1;
          }
        }
      }
      for (;;)
      {
        j++;
        paramInt2 = k;
        paramInt1 = m;
        break;
        label358:
        n = 0;
        break label181;
        label364:
        i2 = 0;
        break label196;
        label370:
        m = i1;
        if (i2 == 0) {
          break label206;
        }
        k = 1073741824;
        m = i1;
        break label206;
        label391:
        k = paramInt2;
        m = paramInt1;
        if (i2 != 0)
        {
          m = paramInt1 - localView.getMeasuredWidth();
          k = paramInt2;
        }
      }
    }
    mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    mInLayout = true;
    populate();
    mInLayout = false;
    int k = getChildCount();
    for (paramInt2 = 0; paramInt2 < k; paramInt2++)
    {
      localView = getChildAt(paramInt2);
      if (localView.getVisibility() != 8)
      {
        localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if ((localLayoutParams == null) || (!isDecor)) {
          localView.measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * widthFactor), 1073741824), mChildHeightMeasureSpec);
        }
      }
    }
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (mDecorChildCount > 0)
    {
      int i = getScrollX();
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = getWidth();
      int n = getChildCount();
      int i1 = 0;
      while (i1 < n)
      {
        View localView = getChildAt(i1);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int i2;
        int i3;
        if (!isDecor)
        {
          i2 = k;
          i3 = j;
          i1++;
          j = i3;
          k = i2;
        }
        else
        {
          switch (gravity & 0x7)
          {
          case 2: 
          case 4: 
          default: 
            i2 = j;
          }
          for (;;)
          {
            int i4 = i2 + i - localView.getLeft();
            i3 = j;
            i2 = k;
            if (i4 == 0) {
              break;
            }
            localView.offsetLeftAndRight(i4);
            i3 = j;
            i2 = k;
            break;
            i2 = j;
            j += localView.getWidth();
            continue;
            i2 = Math.max((m - localView.getMeasuredWidth()) / 2, j);
            continue;
            i2 = m - k - localView.getMeasuredWidth();
            k += localView.getMeasuredWidth();
          }
        }
      }
    }
    if (mOnPageChangeListener != null) {
      mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    }
    if (mInternalPageChangeListener != null) {
      mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    }
    mCalledSuper = true;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i = getChildCount();
    int j;
    int k;
    if ((paramInt & 0x2) != 0)
    {
      j = 0;
      k = 1;
      if (j == i) {
        break label103;
      }
      View localView = getChildAt(j);
      if (localView.getVisibility() != 0) {
        break label93;
      }
      ItemInfo localItemInfo = infoForChild(localView);
      if ((localItemInfo == null) || (position != mCurItem) || (!localView.requestFocus(paramInt, paramRect))) {
        break label93;
      }
    }
    label93:
    label103:
    for (boolean bool = true;; bool = false)
    {
      return bool;
      j = i - 1;
      k = -1;
      i = -1;
      break;
      j += k;
      break;
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
    }
    for (;;)
    {
      return;
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      if (mAdapter != null)
      {
        mAdapter.restoreState(adapterState, loader);
        setCurrentItemInternal(position, false, true);
      }
      else
      {
        mRestoredCurItem = position;
        mRestoredAdapterState = adapterState;
        mRestoredClassLoader = loader;
      }
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    position = mCurItem;
    if (mAdapter != null) {
      adapterState = mAdapter.saveState();
    }
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      recomputeScrollPosition(paramInt1, paramInt3, mPageMargin, mPageMargin);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1;
    if (mFakeDragging) {
      bool1 = true;
    }
    for (;;)
    {
      return bool1;
      if ((paramMotionEvent.getAction() == 0) && (paramMotionEvent.getEdgeFlags() != 0))
      {
        bool1 = false;
      }
      else
      {
        if ((mAdapter != null) && (mAdapter.getCount() != 0)) {
          break;
        }
        bool1 = false;
      }
    }
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    int j = 0;
    int k = j;
    switch (i & 0xFF)
    {
    default: 
      k = j;
    }
    for (;;)
    {
      if (k != 0) {
        ViewCompat.postInvalidateOnAnimation(this);
      }
      bool1 = true;
      break;
      mScroller.abortAnimation();
      mPopulatePending = false;
      populate();
      mIsBeingDragged = true;
      setScrollState(1);
      float f1 = paramMotionEvent.getX();
      mInitialMotionX = f1;
      mLastMotionX = f1;
      mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
      k = j;
      continue;
      if (!mIsBeingDragged)
      {
        k = MotionEventCompat.findPointerIndex(paramMotionEvent, mActivePointerId);
        f1 = MotionEventCompat.getX(paramMotionEvent, k);
        float f2 = Math.abs(f1 - mLastMotionX);
        float f3 = Math.abs(MotionEventCompat.getY(paramMotionEvent, k) - mLastMotionY);
        if ((f2 > mTouchSlop) && (f2 > f3))
        {
          mIsBeingDragged = true;
          if (f1 - mInitialMotionX <= 0.0F) {
            break label361;
          }
        }
      }
      label361:
      for (f1 = mInitialMotionX + mTouchSlop;; f1 = mInitialMotionX - mTouchSlop)
      {
        mLastMotionX = f1;
        setScrollState(1);
        setScrollingCacheEnabled(true);
        k = j;
        if (!mIsBeingDragged) {
          break;
        }
        bool2 = false | performDrag(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, mActivePointerId)));
        break;
      }
      boolean bool2 = j;
      if (mIsBeingDragged)
      {
        Object localObject = mVelocityTracker;
        ((VelocityTracker)localObject).computeCurrentVelocity(1000, mMaximumVelocity);
        j = (int)VelocityTrackerCompat.getXVelocity((VelocityTracker)localObject, mActivePointerId);
        mPopulatePending = true;
        i = getWidth();
        int m = getScrollX();
        localObject = infoForCurrentScrollPosition();
        setCurrentItemInternal(determineTargetPage(position, (m / i - offset) / widthFactor, j, (int)(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, mActivePointerId)) - mInitialMotionX)), true, true, j);
        mActivePointerId = -1;
        endDrag();
        boolean bool3 = mLeftEdge.onRelease() | mRightEdge.onRelease();
        continue;
        bool3 = j;
        if (mIsBeingDragged)
        {
          setCurrentItemInternal(mCurItem, true, true);
          mActivePointerId = -1;
          endDrag();
          bool3 = mLeftEdge.onRelease() | mRightEdge.onRelease();
          continue;
          int n = MotionEventCompat.getActionIndex(paramMotionEvent);
          mLastMotionX = MotionEventCompat.getX(paramMotionEvent, n);
          mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, n);
          n = j;
          continue;
          onSecondaryPointerUp(paramMotionEvent);
          mLastMotionX = MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, mActivePointerId));
          n = j;
        }
      }
    }
  }
  
  boolean pageLeft()
  {
    boolean bool = true;
    if (mCurItem > 0) {
      setCurrentItem(mCurItem - 1, true);
    }
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
  
  boolean pageRight()
  {
    boolean bool = true;
    if ((mAdapter != null) && (mCurItem < mAdapter.getCount() - 1)) {
      setCurrentItem(mCurItem + 1, true);
    }
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
  
  void populate()
  {
    populate(mCurItem);
  }
  
  void populate(int paramInt)
  {
    Object localObject1 = null;
    if (mCurItem != paramInt)
    {
      localObject1 = infoForPosition(mCurItem);
      mCurItem = paramInt;
    }
    if (mAdapter == null) {
      break label31;
    }
    label31:
    label220:
    label314:
    label471:
    label483:
    label611:
    label621:
    label738:
    label744:
    label859:
    label872:
    label993:
    label1094:
    label1100:
    for (;;)
    {
      return;
      if ((!mPopulatePending) && (getWindowToken() != null))
      {
        mAdapter.startUpdate(this);
        paramInt = mOffscreenPageLimit;
        int i = Math.max(0, mCurItem - paramInt);
        int j = mAdapter.getCount();
        int k = Math.min(j - 1, mCurItem + paramInt);
        Object localObject2 = null;
        paramInt = 0;
        Object localObject3 = localObject2;
        Object localObject4;
        if (paramInt < mItems.size())
        {
          localObject4 = (ItemInfo)mItems.get(paramInt);
          if (position < mCurItem) {
            break label471;
          }
          localObject3 = localObject2;
          if (position == mCurItem) {
            localObject3 = localObject4;
          }
        }
        localObject2 = localObject3;
        if (localObject3 == null)
        {
          localObject2 = localObject3;
          if (j > 0) {
            localObject2 = addNewItem(mCurItem, paramInt);
          }
        }
        float f1;
        int m;
        int n;
        int i1;
        int i2;
        if (localObject2 != null)
        {
          f1 = 0.0F;
          m = paramInt - 1;
          if (m >= 0)
          {
            localObject3 = (ItemInfo)mItems.get(m);
            float f2 = widthFactor;
            n = mCurItem - 1;
            localObject4 = localObject3;
            i1 = paramInt;
            if (n >= 0)
            {
              if ((f1 < 2.0F - f2) || (n >= i)) {
                break label621;
              }
              if (localObject4 != null) {
                break label483;
              }
            }
            f1 = widthFactor;
            n = i1 + 1;
            if (f1 < 2.0F)
            {
              if (n >= mItems.size()) {
                break label738;
              }
              localObject3 = (ItemInfo)mItems.get(n);
              i2 = mCurItem + 1;
              localObject4 = localObject3;
              if (i2 < j)
              {
                if ((f1 < 2.0F) || (i2 <= k)) {
                  break label872;
                }
                if (localObject4 != null) {
                  break label744;
                }
              }
            }
            calculatePageOffsets((ItemInfo)localObject2, i1, (ItemInfo)localObject1);
          }
        }
        else
        {
          localObject1 = mAdapter;
          paramInt = mCurItem;
          if (localObject2 == null) {
            break label993;
          }
        }
        for (localObject3 = object;; localObject3 = null)
        {
          ((PagerAdapter)localObject1).setPrimaryItem(this, paramInt, localObject3);
          mAdapter.finishUpdate(this);
          i2 = getChildCount();
          for (paramInt = 0; paramInt < i2; paramInt++)
          {
            localObject1 = getChildAt(paramInt);
            localObject3 = (LayoutParams)((View)localObject1).getLayoutParams();
            if ((!isDecor) && (widthFactor == 0.0F))
            {
              localObject1 = infoForChild((View)localObject1);
              if (localObject1 != null) {
                widthFactor = widthFactor;
              }
            }
          }
          paramInt++;
          break;
          localObject3 = null;
          break label220;
          paramInt = i1;
          float f3 = f1;
          localObject3 = localObject4;
          i2 = m;
          if (n == position)
          {
            paramInt = i1;
            f3 = f1;
            localObject3 = localObject4;
            i2 = m;
            if (!scrolling)
            {
              mItems.remove(m);
              mAdapter.destroyItem(this, n, object);
              i2 = m - 1;
              paramInt = i1 - 1;
              if (i2 < 0) {
                break label611;
              }
              localObject3 = (ItemInfo)mItems.get(i2);
            }
          }
          for (f3 = f1;; f3 = f1)
          {
            n--;
            i1 = paramInt;
            f1 = f3;
            localObject4 = localObject3;
            m = i2;
            break;
            localObject3 = null;
          }
          if ((localObject4 != null) && (n == position))
          {
            f3 = f1 + widthFactor;
            i2 = m - 1;
            if (i2 >= 0) {}
            for (localObject3 = (ItemInfo)mItems.get(i2);; localObject3 = null)
            {
              paramInt = i1;
              break;
            }
          }
          f3 = f1 + addNewItem1widthFactor;
          paramInt = i1 + 1;
          if (m >= 0) {}
          for (localObject3 = (ItemInfo)mItems.get(m);; localObject3 = null)
          {
            i2 = m;
            break;
          }
          localObject3 = null;
          break label314;
          f3 = f1;
          localObject3 = localObject4;
          paramInt = n;
          if (i2 == position)
          {
            f3 = f1;
            localObject3 = localObject4;
            paramInt = n;
            if (!scrolling)
            {
              mItems.remove(n);
              mAdapter.destroyItem(this, i2, object);
              if (n >= mItems.size()) {
                break label859;
              }
              localObject3 = (ItemInfo)mItems.get(n);
              paramInt = n;
              f3 = f1;
            }
          }
          for (;;)
          {
            i2++;
            f1 = f3;
            localObject4 = localObject3;
            n = paramInt;
            break;
            localObject3 = null;
            f3 = f1;
            paramInt = n;
          }
          if ((localObject4 != null) && (i2 == position))
          {
            f3 = f1 + widthFactor;
            paramInt = n + 1;
            if (paramInt < mItems.size()) {}
            for (localObject3 = (ItemInfo)mItems.get(paramInt);; localObject3 = null) {
              break;
            }
          }
          localObject3 = addNewItem(i2, n);
          paramInt = n + 1;
          f3 = f1 + widthFactor;
          if (paramInt < mItems.size()) {}
          for (localObject3 = (ItemInfo)mItems.get(paramInt);; localObject3 = null) {
            break;
          }
        }
        if (!hasFocus()) {
          break;
        }
        localObject3 = findFocus();
        if (localObject3 != null) {}
        for (localObject3 = infoForAnyChild((View)localObject3);; localObject3 = null)
        {
          if ((localObject3 != null) && (position == mCurItem)) {
            break label1100;
          }
          for (paramInt = 0;; paramInt++)
          {
            if (paramInt >= getChildCount()) {
              break label1094;
            }
            localObject3 = getChildAt(paramInt);
            localObject1 = infoForChild((View)localObject3);
            if ((localObject1 != null) && (position == mCurItem) && (((View)localObject3).requestFocus(2))) {
              break;
            }
          }
          break label31;
        }
      }
    }
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    if (mAdapter != null)
    {
      mAdapter.unregisterDataSetObserver(mObserver);
      mAdapter.startUpdate(this);
      for (int i = 0; i < mItems.size(); i++)
      {
        localObject = (ItemInfo)mItems.get(i);
        mAdapter.destroyItem(this, position, object);
      }
      mAdapter.finishUpdate(this);
      mItems.clear();
      removeNonDecorViews();
      mCurItem = 0;
      scrollTo(0, 0);
    }
    Object localObject = mAdapter;
    mAdapter = paramPagerAdapter;
    if (mAdapter != null)
    {
      if (mObserver == null) {
        mObserver = new PagerObserver(null);
      }
      mAdapter.registerDataSetObserver(mObserver);
      mPopulatePending = false;
      mFirstLayout = true;
      if (mRestoredCurItem < 0) {
        break label232;
      }
      mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
      setCurrentItemInternal(mRestoredCurItem, false, true);
      mRestoredCurItem = -1;
      mRestoredAdapterState = null;
      mRestoredClassLoader = null;
    }
    for (;;)
    {
      if ((mAdapterChangeListener != null) && (localObject != paramPagerAdapter)) {
        mAdapterChangeListener.onAdapterChanged((PagerAdapter)localObject, paramPagerAdapter);
      }
      return;
      label232:
      populate();
    }
  }
  
  public void setCurrentItem(int paramInt)
  {
    mPopulatePending = false;
    if (!mFirstLayout) {}
    for (boolean bool = true;; bool = false)
    {
      setCurrentItemInternal(paramInt, bool, false);
      return;
    }
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    int i = 1;
    if ((mAdapter == null) || (mAdapter.getCount() <= 0)) {
      setScrollingCacheEnabled(false);
    }
    for (;;)
    {
      return;
      if ((!paramBoolean2) && (mCurItem == paramInt1) && (mItems.size() != 0))
      {
        setScrollingCacheEnabled(false);
      }
      else
      {
        int j;
        if (paramInt1 < 0) {
          j = 0;
        }
        for (;;)
        {
          paramInt1 = mOffscreenPageLimit;
          if ((j <= mCurItem + paramInt1) && (j >= mCurItem - paramInt1)) {
            break;
          }
          for (paramInt1 = 0; paramInt1 < mItems.size(); paramInt1++) {
            mItems.get(paramInt1)).scrolling = true;
          }
          j = paramInt1;
          if (paramInt1 >= mAdapter.getCount()) {
            j = mAdapter.getCount() - 1;
          }
        }
        if (mCurItem != j) {}
        for (paramInt1 = i;; paramInt1 = 0)
        {
          populate(j);
          ItemInfo localItemInfo = infoForPosition(j);
          i = 0;
          if (localItemInfo != null) {
            i = (int)(getWidth() * Math.max(mFirstOffset, Math.min(offset, mLastOffset)));
          }
          if (!paramBoolean1) {
            break label279;
          }
          smoothScrollTo(i, 0, paramInt2);
          if ((paramInt1 != 0) && (mOnPageChangeListener != null)) {
            mOnPageChangeListener.onPageSelected(j);
          }
          if ((paramInt1 == 0) || (mInternalPageChangeListener == null)) {
            break;
          }
          mInternalPageChangeListener.onPageSelected(j);
          break;
        }
        label279:
        if ((paramInt1 != 0) && (mOnPageChangeListener != null)) {
          mOnPageChangeListener.onPageSelected(j);
        }
        if ((paramInt1 != 0) && (mInternalPageChangeListener != null)) {
          mInternalPageChangeListener.onPageSelected(j);
        }
        completeScroll();
        scrollTo(i, 0);
      }
    }
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    OnPageChangeListener localOnPageChangeListener = mInternalPageChangeListener;
    mInternalPageChangeListener = paramOnPageChangeListener;
    return localOnPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 1)
    {
      Log.w("ViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + 1);
      i = 1;
    }
    if (i != mOffscreenPageLimit)
    {
      mOffscreenPageLimit = i;
      populate();
    }
  }
  
  void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    mAdapterChangeListener = paramOnAdapterChangeListener;
  }
  
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt)
  {
    int i = mPageMargin;
    mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt)
  {
    setPageMarginDrawable(getContext().getResources().getDrawable(paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable)
  {
    mMarginDrawable = paramDrawable;
    if (paramDrawable != null) {
      refreshDrawableState();
    }
    if (paramDrawable == null) {}
    for (boolean bool = true;; bool = false)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
    }
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2)
  {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
    }
    int i;
    int j;
    int k;
    for (;;)
    {
      return;
      i = getScrollX();
      j = getScrollY();
      k = paramInt1 - i;
      paramInt2 -= j;
      if ((k != 0) || (paramInt2 != 0)) {
        break;
      }
      completeScroll();
      populate();
      setScrollState(0);
    }
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getWidth();
    int m = paramInt1 / 2;
    float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / paramInt1);
    float f2 = m;
    float f3 = m;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {}
    for (paramInt1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt3)) * 4;; paramInt1 = (int)((1.0F + Math.abs(k) / (mPageMargin + f2 * f3)) * 100.0F))
    {
      paramInt1 = Math.min(paramInt1, 600);
      mScroller.startScroll(i, j, k, paramInt2, paramInt1);
      ViewCompat.postInvalidateOnAnimation(this);
      break;
      f2 = paramInt1;
      f3 = mAdapter.getPageWidth(mCurItem);
    }
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    if ((super.verifyDrawable(paramDrawable)) || (paramDrawable == mMarginDrawable)) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  static abstract interface Decor {}
  
  static class ItemInfo
  {
    Object object;
    float offset;
    int position;
    boolean scrolling;
    float widthFactor;
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    public int gravity;
    public boolean isDecor;
    public boolean needsMeasure;
    public float widthFactor = 0.0F;
    
    public LayoutParams()
    {
      super(-1);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, ViewPager.LAYOUT_ATTRS);
      gravity = paramContext.getInteger(0, 48);
      paramContext.recycle();
    }
  }
  
  class MyAccessibilityDelegate
    extends AccessibilityDelegateCompat
  {
    MyAccessibilityDelegate() {}
    
    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      paramAccessibilityEvent.setClassName(ViewPager.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      boolean bool = true;
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
      paramAccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      if ((mAdapter != null) && (mAdapter.getCount() > 1)) {}
      for (;;)
      {
        paramAccessibilityNodeInfoCompat.setScrollable(bool);
        if ((mAdapter != null) && (mCurItem >= 0) && (mCurItem < mAdapter.getCount() - 1)) {
          paramAccessibilityNodeInfoCompat.addAction(4096);
        }
        if ((mAdapter != null) && (mCurItem > 0) && (mCurItem < mAdapter.getCount())) {
          paramAccessibilityNodeInfoCompat.addAction(8192);
        }
        return;
        bool = false;
      }
    }
    
    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      boolean bool = true;
      if (super.performAccessibilityAction(paramView, paramInt, paramBundle)) {}
      for (;;)
      {
        return bool;
        switch (paramInt)
        {
        default: 
          bool = false;
          break;
        case 4096: 
          if ((mAdapter != null) && (mCurItem >= 0) && (mCurItem < mAdapter.getCount() - 1)) {
            setCurrentItem(mCurItem + 1);
          } else {
            bool = false;
          }
          break;
        case 8192: 
          if ((mAdapter != null) && (mCurItem > 0) && (mCurItem < mAdapter.getCount())) {
            setCurrentItem(mCurItem - 1);
          } else {
            bool = false;
          }
          break;
        }
      }
    }
  }
  
  static abstract interface OnAdapterChangeListener
  {
    public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2);
  }
  
  public static abstract interface OnPageChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);
    
    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);
    
    public abstract void onPageSelected(int paramInt);
  }
  
  private class PagerObserver
    extends DataSetObserver
  {
    private PagerObserver() {}
    
    public void onChanged()
    {
      dataSetChanged();
    }
    
    public void onInvalidated()
    {
      dataSetChanged();
    }
  }
  
  public static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
    {
      public ViewPager.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new ViewPager.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public ViewPager.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ViewPager.SavedState[paramAnonymousInt];
      }
    });
    Parcelable adapterState;
    ClassLoader loader;
    int position;
    
    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super();
      ClassLoader localClassLoader = paramClassLoader;
      if (paramClassLoader == null) {
        localClassLoader = getClass().getClassLoader();
      }
      position = paramParcel.readInt();
      adapterState = paramParcel.readParcelable(localClassLoader);
      loader = localClassLoader;
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + position + "}";
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(position);
      paramParcel.writeParcelable(adapterState, paramInt);
    }
  }
  
  public static class SimpleOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    public void onPageScrollStateChanged(int paramInt) {}
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
    
    public void onPageSelected(int paramInt) {}
  }
}

/* Location:
 * Qualified Name:     android.support.v4.view.ViewPager
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */