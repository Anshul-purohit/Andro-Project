/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  java.lang.Object
 *  java.lang.UnsupportedOperationException
 */
package android.support.v7.internal.view.menu;

import android.os.Build;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v7.internal.view.menu.MenuItemWrapperICS;
import android.support.v7.internal.view.menu.MenuItemWrapperJB;
import android.support.v7.internal.view.menu.MenuWrapperICS;
import android.support.v7.internal.view.menu.SubMenuWrapperICS;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public final class MenuWrapperFactory {
    private MenuWrapperFactory() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static MenuItem createMenuItemWrapper(MenuItem menuItem) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new MenuItemWrapperJB(menuItem);
        }
        MenuItem menuItem2 = menuItem;
        if (Build.VERSION.SDK_INT < 14) return menuItem2;
        return new MenuItemWrapperICS(menuItem);
    }

    public static Menu createMenuWrapper(Menu menu2) {
        Menu menu3 = menu2;
        if (Build.VERSION.SDK_INT >= 14) {
            menu3 = new MenuWrapperICS(menu2);
        }
        return menu3;
    }

    public static SupportMenuItem createSupportMenuItemWrapper(MenuItem menuItem) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new MenuItemWrapperJB(menuItem);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return new MenuItemWrapperICS(menuItem);
        }
        throw new UnsupportedOperationException();
    }

    public static SupportMenu createSupportMenuWrapper(Menu menu2) {
        if (Build.VERSION.SDK_INT >= 14) {
            return new MenuWrapperICS(menu2);
        }
        throw new UnsupportedOperationException();
    }

    public static SupportSubMenu createSupportSubMenuWrapper(SubMenu subMenu) {
        if (Build.VERSION.SDK_INT >= 14) {
            return new SubMenuWrapperICS(subMenu);
        }
        throw new UnsupportedOperationException();
    }
}

