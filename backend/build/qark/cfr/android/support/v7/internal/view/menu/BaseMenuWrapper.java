/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  java.lang.Object
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Set
 */
package android.support.v7.internal.view.menu;

import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.internal.view.menu.BaseWrapper;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

abstract class BaseMenuWrapper<T>
extends BaseWrapper<T> {
    private HashMap<MenuItem, SupportMenuItem> mMenuItems;
    private HashMap<SubMenu, SubMenu> mSubMenus;

    BaseMenuWrapper(T t) {
        super(t);
    }

    final SupportMenuItem getMenuItemWrapper(MenuItem menuItem) {
        if (menuItem != null) {
            SupportMenuItem supportMenuItem;
            if (this.mMenuItems == null) {
                this.mMenuItems = new HashMap();
            }
            SupportMenuItem supportMenuItem2 = supportMenuItem = (SupportMenuItem)this.mMenuItems.get((Object)menuItem);
            if (supportMenuItem == null) {
                supportMenuItem2 = MenuWrapperFactory.createSupportMenuItemWrapper(menuItem);
                this.mMenuItems.put((Object)menuItem, (Object)supportMenuItem2);
            }
            return supportMenuItem2;
        }
        return null;
    }

    final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (subMenu != null) {
            SubMenu subMenu2;
            if (this.mSubMenus == null) {
                this.mSubMenus = new HashMap();
            }
            SubMenu subMenu3 = subMenu2 = (SubMenu)this.mSubMenus.get((Object)subMenu);
            if (subMenu2 == null) {
                subMenu3 = MenuWrapperFactory.createSupportSubMenuWrapper(subMenu);
                this.mSubMenus.put((Object)subMenu, (Object)subMenu3);
            }
            return subMenu3;
        }
        return null;
    }

    final void internalClear() {
        if (this.mMenuItems != null) {
            this.mMenuItems.clear();
        }
        if (this.mSubMenus != null) {
            this.mSubMenus.clear();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    final void internalRemoveGroup(int n) {
        if (this.mMenuItems != null) {
            Iterator iterator = this.mMenuItems.keySet().iterator();
            while (iterator.hasNext()) {
                if (n != ((MenuItem)iterator.next()).getGroupId()) continue;
                iterator.remove();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    final void internalRemoveItem(int n) {
        if (this.mMenuItems == null) {
            return;
        }
        Iterator iterator = this.mMenuItems.keySet().iterator();
        do {
            if (!iterator.hasNext()) return;
        } while (n != ((MenuItem)iterator.next()).getItemId());
        iterator.remove();
    }
}

