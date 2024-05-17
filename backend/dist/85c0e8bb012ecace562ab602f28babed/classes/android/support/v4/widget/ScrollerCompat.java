package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.widget.Scroller;

class ScrollerCompat
{
  Scroller mScroller;
  
  ScrollerCompat(Context paramContext)
  {
    mScroller = new Scroller(paramContext);
  }
  
  public static ScrollerCompat from(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 14) {}
    for (paramContext = new ScrollerCompatImplIcs(paramContext);; paramContext = new ScrollerCompat(paramContext)) {
      return paramContext;
    }
  }
  
  public void abortAnimation()
  {
    mScroller.abortAnimation();
  }
  
  public boolean computeScrollOffset()
  {
    return mScroller.computeScrollOffset();
  }
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    mScroller.fling(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
  }
  
  public float getCurrVelocity()
  {
    return 0.0F;
  }
  
  public int getCurrX()
  {
    return mScroller.getCurrX();
  }
  
  public int getCurrY()
  {
    return mScroller.getCurrY();
  }
  
  public int getDuration()
  {
    return mScroller.getDuration();
  }
  
  public boolean isFinished()
  {
    return mScroller.isFinished();
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mScroller.startScroll(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mScroller.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  static class ScrollerCompatImplIcs
    extends ScrollerCompat
  {
    public ScrollerCompatImplIcs(Context paramContext)
    {
      super();
    }
    
    public float getCurrVelocity()
    {
      return ScrollerCompatIcs.getCurrVelocity(mScroller);
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.widget.ScrollerCompat
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */