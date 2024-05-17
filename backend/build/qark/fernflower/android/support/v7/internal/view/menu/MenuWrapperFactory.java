package android.support.v7.internal.view.menu;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public final class MenuWrapperFactory {
   private MenuWrapperFactory() {
   }

   public static MenuItem createMenuItemWrapper(MenuItem var0) {
      Object var1;
      if (VERSION.SDK_INT >= 16) {
         var1 = new MenuItemWrapperJB(var0);
      } else {
         var1 = var0;
         if (VERSION.SDK_INT >= 14) {
            return new MenuItemWrapperICS(var0);
         }
      }

      return (MenuItem)var1;
   }

   public static Menu createMenuWrapper(Menu var0) {
      Object var1 = var0;
      if (VERSION.SDK_INT >= 14) {
         var1 = new MenuWrapperICS(var0);
      }

      return (Menu)var1;
   }

   public static SupportMenuItem createSupportMenuItemWrapper(MenuItem var0) {
      if (VERSION.SDK_INT >= 16) {
         return new MenuItemWrapperJB(var0);
      } else if (VERSION.SDK_INT >= 14) {
         return new MenuItemWrapperICS(var0);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public static SupportMenu createSupportMenuWrapper(Menu var0) {
      if (VERSION.SDK_INT >= 14) {
         return new MenuWrapperICS(var0);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public static SupportSubMenu createSupportSubMenuWrapper(SubMenu var0) {
      if (VERSION.SDK_INT >= 14) {
         return new SubMenuWrapperICS(var0);
      } else {
         throw new UnsupportedOperationException();
      }
   }
}
