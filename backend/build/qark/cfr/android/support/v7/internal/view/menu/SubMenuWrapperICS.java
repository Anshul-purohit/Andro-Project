/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  java.lang.CharSequence
 *  java.lang.Object
 */
package android.support.v7.internal.view.menu;

import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v7.internal.view.menu.MenuWrapperICS;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

class SubMenuWrapperICS
extends MenuWrapperICS
implements SupportSubMenu {
    SubMenuWrapperICS(SubMenu subMenu) {
        super((Menu)subMenu);
    }

    public void clearHeader() {
        ((SubMenu)this.mWrappedObject).clearHeader();
    }

    public MenuItem getItem() {
        return this.getMenuItemWrapper(((SubMenu)this.mWrappedObject).getItem());
    }

    @Override
    public SubMenu getWrappedObject() {
        return (SubMenu)this.mWrappedObject;
    }

    public SubMenu setHeaderIcon(int n) {
        ((SubMenu)this.mWrappedObject).setHeaderIcon(n);
        return this;
    }

    public SubMenu setHeaderIcon(Drawable drawable2) {
        ((SubMenu)this.mWrappedObject).setHeaderIcon(drawable2);
        return this;
    }

    public SubMenu setHeaderTitle(int n) {
        ((SubMenu)this.mWrappedObject).setHeaderTitle(n);
        return this;
    }

    public SubMenu setHeaderTitle(CharSequence charSequence) {
        ((SubMenu)this.mWrappedObject).setHeaderTitle(charSequence);
        return this;
    }

    public SubMenu setHeaderView(View view) {
        ((SubMenu)this.mWrappedObject).setHeaderView(view);
        return this;
    }

    public SubMenu setIcon(int n) {
        ((SubMenu)this.mWrappedObject).setIcon(n);
        return this;
    }

    public SubMenu setIcon(Drawable drawable2) {
        ((SubMenu)this.mWrappedObject).setIcon(drawable2);
        return this;
    }
}

