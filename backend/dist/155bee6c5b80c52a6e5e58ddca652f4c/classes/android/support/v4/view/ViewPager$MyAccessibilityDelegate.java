package android.support.v4.view;

import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

class ViewPager$MyAccessibilityDelegate
  extends AccessibilityDelegateCompat
{
  ViewPager$MyAccessibilityDelegate(ViewPager paramViewPager) {}
  
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
    if ((ViewPager.access$100(this$0) != null) && (ViewPager.access$100(this$0).getCount() > 1)) {}
    for (;;)
    {
      paramAccessibilityNodeInfoCompat.setScrollable(bool);
      if ((ViewPager.access$100(this$0) != null) && (ViewPager.access$200(this$0) >= 0) && (ViewPager.access$200(this$0) < ViewPager.access$100(this$0).getCount() - 1)) {
        paramAccessibilityNodeInfoCompat.addAction(4096);
      }
      if ((ViewPager.access$100(this$0) != null) && (ViewPager.access$200(this$0) > 0) && (ViewPager.access$200(this$0) < ViewPager.access$100(this$0).getCount())) {
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
        if ((ViewPager.access$100(this$0) != null) && (ViewPager.access$200(this$0) >= 0) && (ViewPager.access$200(this$0) < ViewPager.access$100(this$0).getCount() - 1)) {
          this$0.setCurrentItem(ViewPager.access$200(this$0) + 1);
        } else {
          bool = false;
        }
        break;
      case 8192: 
        if ((ViewPager.access$100(this$0) != null) && (ViewPager.access$200(this$0) > 0) && (ViewPager.access$200(this$0) < ViewPager.access$100(this$0).getCount())) {
          this$0.setCurrentItem(ViewPager.access$200(this$0) - 1);
        } else {
          bool = false;
        }
        break;
      }
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.view.ViewPager.MyAccessibilityDelegate
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */