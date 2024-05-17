package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class GestureDetectorCompat
{
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener)
  {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
  {
    if (Build.VERSION.SDK_INT >= 17) {}
    for (mImpl = new GestureDetectorCompatImplJellybeanMr1(paramContext, paramOnGestureListener, paramHandler);; mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler)) {
      return;
    }
  }
  
  public boolean isLongpressEnabled()
  {
    return mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean)
  {
    mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
  {
    mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static abstract interface GestureDetectorCompatImpl
  {
    public abstract boolean isLongpressEnabled();
    
    public abstract boolean onTouchEvent(MotionEvent paramMotionEvent);
    
    public abstract void setIsLongpressEnabled(boolean paramBoolean);
    
    public abstract void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    private static final int LONGPRESS_TIMEOUT = ;
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private boolean mAlwaysInBiggerTapRegion;
    private boolean mAlwaysInTapRegion;
    private MotionEvent mCurrentDownEvent;
    private GestureDetector.OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mInLongPress;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    private final GestureDetector.OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;
    
    public GestureDetectorCompatImplBase(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      if (paramHandler != null) {}
      for (mHandler = new GestureHandler(paramHandler);; mHandler = new GestureHandler())
      {
        mListener = paramOnGestureListener;
        if ((paramOnGestureListener instanceof GestureDetector.OnDoubleTapListener)) {
          setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)paramOnGestureListener);
        }
        init(paramContext);
        return;
      }
    }
    
    private void cancel()
    {
      mHandler.removeMessages(1);
      mHandler.removeMessages(2);
      mHandler.removeMessages(3);
      mVelocityTracker.recycle();
      mVelocityTracker = null;
      mIsDoubleTapping = false;
      mStillDown = false;
      mAlwaysInTapRegion = false;
      mAlwaysInBiggerTapRegion = false;
      if (mInLongPress) {
        mInLongPress = false;
      }
    }
    
    private void cancelTaps()
    {
      mHandler.removeMessages(1);
      mHandler.removeMessages(2);
      mHandler.removeMessages(3);
      mIsDoubleTapping = false;
      mAlwaysInTapRegion = false;
      mAlwaysInBiggerTapRegion = false;
      if (mInLongPress) {
        mInLongPress = false;
      }
    }
    
    private void dispatchLongPress()
    {
      mHandler.removeMessages(3);
      mInLongPress = true;
      mListener.onLongPress(mCurrentDownEvent);
    }
    
    private void init(Context paramContext)
    {
      if (paramContext == null) {
        throw new IllegalArgumentException("Context must not be null");
      }
      if (mListener == null) {
        throw new IllegalArgumentException("OnGestureListener must not be null");
      }
      mIsLongpressEnabled = true;
      paramContext = ViewConfiguration.get(paramContext);
      int i = paramContext.getScaledTouchSlop();
      int j = paramContext.getScaledDoubleTapSlop();
      mMinimumFlingVelocity = paramContext.getScaledMinimumFlingVelocity();
      mMaximumFlingVelocity = paramContext.getScaledMaximumFlingVelocity();
      mTouchSlopSquare = (i * i);
      mDoubleTapSlopSquare = (j * j);
    }
    
    private boolean isConsideredDoubleTap(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, MotionEvent paramMotionEvent3)
    {
      boolean bool1 = false;
      boolean bool2;
      if (!mAlwaysInBiggerTapRegion) {
        bool2 = bool1;
      }
      for (;;)
      {
        return bool2;
        bool2 = bool1;
        if (paramMotionEvent3.getEventTime() - paramMotionEvent2.getEventTime() <= DOUBLE_TAP_TIMEOUT)
        {
          int i = (int)paramMotionEvent1.getX() - (int)paramMotionEvent3.getX();
          int j = (int)paramMotionEvent1.getY() - (int)paramMotionEvent3.getY();
          bool2 = bool1;
          if (i * i + j * j < mDoubleTapSlopSquare) {
            bool2 = true;
          }
        }
      }
    }
    
    public boolean isLongpressEnabled()
    {
      return mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getAction();
      if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
      }
      mVelocityTracker.addMovement(paramMotionEvent);
      int j;
      int m;
      label49:
      float f1;
      float f2;
      int n;
      int i1;
      if ((i & 0xFF) == 6)
      {
        j = 1;
        if (j == 0) {
          break label89;
        }
        m = MotionEventCompat.getActionIndex(paramMotionEvent);
        f1 = 0.0F;
        f2 = 0.0F;
        n = MotionEventCompat.getPointerCount(paramMotionEvent);
        i1 = 0;
        label64:
        if (i1 >= n) {
          break label120;
        }
        if (m != i1) {
          break label95;
        }
      }
      for (;;)
      {
        i1++;
        break label64;
        j = 0;
        break;
        label89:
        m = -1;
        break label49;
        label95:
        f1 += MotionEventCompat.getX(paramMotionEvent, i1);
        f2 += MotionEventCompat.getY(paramMotionEvent, i1);
      }
      label120:
      boolean bool2;
      boolean bool3;
      boolean bool4;
      boolean bool5;
      if (j != 0)
      {
        j = n - 1;
        f1 /= j;
        f2 /= j;
        m = 0;
        bool2 = false;
        bool3 = false;
        bool4 = false;
        bool5 = bool4;
        switch (i & 0xFF)
        {
        default: 
          bool5 = bool4;
        }
      }
      for (;;)
      {
        return bool5;
        j = n;
        break;
        mLastFocusX = f1;
        mDownFocusX = f1;
        mLastFocusY = f2;
        mDownFocusY = f2;
        cancelTaps();
        bool5 = bool4;
        continue;
        mLastFocusX = f1;
        mDownFocusX = f1;
        mLastFocusY = f2;
        mDownFocusY = f2;
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        m = MotionEventCompat.getActionIndex(paramMotionEvent);
        j = MotionEventCompat.getPointerId(paramMotionEvent, m);
        f1 = VelocityTrackerCompat.getXVelocity(mVelocityTracker, j);
        f2 = VelocityTrackerCompat.getYVelocity(mVelocityTracker, j);
        j = 0;
        bool5 = bool4;
        if (j < n)
        {
          if (j == m) {}
          do
          {
            j++;
            break;
            i1 = MotionEventCompat.getPointerId(paramMotionEvent, j);
          } while (f1 * VelocityTrackerCompat.getXVelocity(mVelocityTracker, i1) + f2 * VelocityTrackerCompat.getYVelocity(mVelocityTracker, i1) >= 0.0F);
          mVelocityTracker.clear();
          bool5 = bool4;
          continue;
          j = m;
          if (mDoubleTapListener != null)
          {
            bool5 = mHandler.hasMessages(3);
            if (bool5) {
              mHandler.removeMessages(3);
            }
            if ((mCurrentDownEvent == null) || (mPreviousUpEvent == null) || (!bool5) || (!isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, paramMotionEvent))) {
              break label648;
            }
            mIsDoubleTapping = true;
          }
          for (boolean bool1 = false | mDoubleTapListener.onDoubleTap(mCurrentDownEvent) | mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);; bool1 = m)
          {
            mLastFocusX = f1;
            mDownFocusX = f1;
            mLastFocusY = f2;
            mDownFocusY = f2;
            if (mCurrentDownEvent != null) {
              mCurrentDownEvent.recycle();
            }
            mCurrentDownEvent = MotionEvent.obtain(paramMotionEvent);
            mAlwaysInTapRegion = true;
            mAlwaysInBiggerTapRegion = true;
            mStillDown = true;
            mInLongPress = false;
            if (mIsLongpressEnabled)
            {
              mHandler.removeMessages(2);
              mHandler.sendEmptyMessageAtTime(2, mCurrentDownEvent.getDownTime() + TAP_TIMEOUT + LONGPRESS_TIMEOUT);
            }
            mHandler.sendEmptyMessageAtTime(1, mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
            bool5 = bool1 | mListener.onDown(paramMotionEvent);
            break;
            label648:
            mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
          }
          bool5 = bool4;
          if (!mInLongPress)
          {
            float f3 = mLastFocusX - f1;
            float f4 = mLastFocusY - f2;
            if (mIsDoubleTapping)
            {
              bool5 = false | mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
            }
            else
            {
              int k;
              if (mAlwaysInTapRegion)
              {
                k = (int)(f1 - mDownFocusX);
                m = (int)(f2 - mDownFocusY);
                k = k * k + m * m;
                bool4 = bool2;
                if (k > mTouchSlopSquare)
                {
                  bool4 = mListener.onScroll(mCurrentDownEvent, paramMotionEvent, f3, f4);
                  mLastFocusX = f1;
                  mLastFocusY = f2;
                  mAlwaysInTapRegion = false;
                  mHandler.removeMessages(3);
                  mHandler.removeMessages(1);
                  mHandler.removeMessages(2);
                }
                bool5 = bool4;
                if (k > mTouchSlopSquare)
                {
                  mAlwaysInBiggerTapRegion = false;
                  bool5 = bool4;
                }
              }
              else if (Math.abs(f3) < 1.0F)
              {
                bool5 = bool4;
                if (Math.abs(f4) < 1.0F) {}
              }
              else
              {
                bool5 = mListener.onScroll(mCurrentDownEvent, paramMotionEvent, f3, f4);
                mLastFocusX = f1;
                mLastFocusY = f2;
                continue;
                mStillDown = false;
                MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
                if (mIsDoubleTapping) {
                  bool5 = false | mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
                }
                for (;;)
                {
                  if (mPreviousUpEvent != null) {
                    mPreviousUpEvent.recycle();
                  }
                  mPreviousUpEvent = localMotionEvent;
                  if (mVelocityTracker != null)
                  {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                  }
                  mIsDoubleTapping = false;
                  mHandler.removeMessages(1);
                  mHandler.removeMessages(2);
                  break;
                  if (mInLongPress)
                  {
                    mHandler.removeMessages(3);
                    mInLongPress = false;
                    bool5 = bool3;
                  }
                  else if (mAlwaysInTapRegion)
                  {
                    bool5 = mListener.onSingleTapUp(paramMotionEvent);
                  }
                  else
                  {
                    VelocityTracker localVelocityTracker = mVelocityTracker;
                    k = MotionEventCompat.getPointerId(paramMotionEvent, 0);
                    localVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    f1 = VelocityTrackerCompat.getYVelocity(localVelocityTracker, k);
                    f2 = VelocityTrackerCompat.getXVelocity(localVelocityTracker, k);
                    if (Math.abs(f1) <= mMinimumFlingVelocity)
                    {
                      bool5 = bool3;
                      if (Math.abs(f2) <= mMinimumFlingVelocity) {}
                    }
                    else
                    {
                      bool5 = mListener.onFling(mCurrentDownEvent, paramMotionEvent, f2, f1);
                    }
                  }
                }
                cancel();
                bool5 = bool4;
              }
            }
          }
        }
      }
    }
    
    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      mIsLongpressEnabled = paramBoolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      mDoubleTapListener = paramOnDoubleTapListener;
    }
    
    private class GestureHandler
      extends Handler
    {
      GestureHandler() {}
      
      GestureHandler(Handler paramHandler)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        switch (what)
        {
        default: 
          throw new RuntimeException("Unknown message " + paramMessage);
        case 1: 
          mListener.onShowPress(mCurrentDownEvent);
        }
        for (;;)
        {
          return;
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
          continue;
          if ((mDoubleTapListener != null) && (!mStillDown)) {
            mDoubleTapListener.onSingleTapConfirmed(mCurrentDownEvent);
          }
        }
      }
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr1
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private final GestureDetector mDetector;
    
    public GestureDetectorCompatImplJellybeanMr1(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      mDetector = new GestureDetector(paramContext, paramOnGestureListener, paramHandler);
    }
    
    public boolean isLongpressEnabled()
    {
      return mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      return mDetector.onTouchEvent(paramMotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      mDetector.setIsLongpressEnabled(paramBoolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      mDetector.setOnDoubleTapListener(paramOnDoubleTapListener);
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.view.GestureDetectorCompat
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */