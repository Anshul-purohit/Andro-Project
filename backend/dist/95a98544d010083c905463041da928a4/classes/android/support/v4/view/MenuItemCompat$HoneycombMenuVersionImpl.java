package android.support.v4.view;

import android.view.MenuItem;
import android.view.View;

class MenuItemCompat$HoneycombMenuVersionImpl
  implements MenuItemCompat.MenuVersionImpl
{
  public MenuItem setActionView(MenuItem paramMenuItem, View paramView)
  {
    return MenuItemCompatHoneycomb.setActionView(paramMenuItem, paramView);
  }
  
  public boolean setShowAsAction(MenuItem paramMenuItem, int paramInt)
  {
    MenuItemCompatHoneycomb.setShowAsAction(paramMenuItem, paramInt);
    return true;
  }
}

/* Location:
 * Qualified Name:     android.support.v4.view.MenuItemCompat.HoneycombMenuVersionImpl
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */