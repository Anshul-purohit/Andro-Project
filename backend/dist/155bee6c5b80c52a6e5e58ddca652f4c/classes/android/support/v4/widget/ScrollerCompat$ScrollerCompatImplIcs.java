package android.support.v4.widget;

import android.content.Context;

class ScrollerCompat$ScrollerCompatImplIcs
  extends ScrollerCompat
{
  public ScrollerCompat$ScrollerCompatImplIcs(Context paramContext)
  {
    super(paramContext);
  }
  
  public float getCurrVelocity()
  {
    return ScrollerCompatIcs.getCurrVelocity(mScroller);
  }
}

/* Location:
 * Qualified Name:     android.support.v4.widget.ScrollerCompat.ScrollerCompatImplIcs
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */