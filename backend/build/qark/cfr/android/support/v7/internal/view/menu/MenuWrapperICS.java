/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.view.KeyEvent
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  java.lang.CharSequence
 *  java.lang.Object
 */
package android.support.v7.internal.view.menu;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.internal.view.menu.BaseMenuWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

class MenuWrapperICS
extends BaseMenuWrapper<Menu>
implements SupportMenu {
    MenuWrapperICS(Menu menu2) {
        super(menu2);
    }

    public MenuItem add(int n) {
        return this.getMenuItemWrapper(((Menu)this.mWrappedObject).add(n));
    }

    public MenuItem add(int n, int n2, int n3, int n4) {
        return this.getMenuItemWrapper(((Menu)this.mWrappedObject).add(n, n2, n3, n4));
    }

    public MenuItem add(int n, int n2, int n3, CharSequence charSequence) {
        return this.getMenuItemWrapper(((Menu)this.mWrappedObject).add(n, n2, n3, charSequence));
    }

    public MenuItem add(CharSequence charSequence) {
        return this.getMenuItemWrapper(((Menu)this.mWrappedObject).add(charSequence));
    }

    public int addIntentOptions(int n, int n2, int n3, ComponentName componentName, Intent[] arrintent, Intent intent, int n4, MenuItem[] arrmenuItem) {
        MenuItem[] arrmenuItem2 = null;
        if (arrmenuItem != null) {
            arrmenuItem2 = new MenuItem[arrmenuItem.length];
        }
        n2 = ((Menu)this.mWrappedObject).addIntentOptions(n, n2, n3, componentName, arrintent, intent, n4, arrmenuItem2);
        if (arrmenuItem2 != null) {
            n3 = arrmenuItem2.length;
            for (n = 0; n < n3; ++n) {
                arrmenuItem[n] = this.getMenuItemWrapper(arrmenuItem2[n]);
            }
        }
        return n2;
    }

    public SubMenu addSubMenu(int n) {
        return this.getSubMenuWrapper(((Menu)this.mWrappedObject).addSubMenu(n));
    }

    public SubMenu addSubMenu(int n, int n2, int n3, int n4) {
        return this.getSubMenuWrapper(((Menu)this.mWrappedObject).addSubMenu(n, n2, n3, n4));
    }

    public SubMenu addSubMenu(int n, int n2, int n3, CharSequence charSequence) {
        return this.getSubMenuWrapper(((Menu)this.mWrappedObject).addSubMenu(n, n2, n3, charSequence));
    }

    public SubMenu addSubMenu(CharSequence charSequence) {
        return this.getSubMenuWrapper(((Menu)this.mWrappedObject).addSubMenu(charSequence));
    }

    public void clear() {
        this.internalClear();
        ((Menu)this.mWrappedObject).clear();
    }

    public void close() {
        ((Menu)this.mWrappedObject).close();
    }

    public MenuItem findItem(int n) {
        return this.getMenuItemWrapper(((Menu)this.mWrappedObject).findItem(n));
    }

    public MenuItem getItem(int n) {
        return this.getMenuItemWrapper(((Menu)this.mWrappedObject).getItem(n));
    }

    public boolean hasVisibleItems() {
        return ((Menu)this.mWrappedObject).hasVisibleItems();
    }

    public boolean isShortcutKey(int n, KeyEvent keyEvent) {
        return ((Menu)this.mWrappedObject).isShortcutKey(n, keyEvent);
    }

    public boolean performIdentifierAction(int n, int n2) {
        return ((Menu)this.mWrappedObject).performIdentifierAction(n, n2);
    }

    public boolean performShortcut(int n, KeyEvent keyEvent, int n2) {
        return ((Menu)this.mWrappedObject).performShortcut(n, keyEvent, n2);
    }

    public void removeGroup(int n) {
        this.internalRemoveGroup(n);
        ((Menu)this.mWrappedObject).removeGroup(n);
    }

    public void removeItem(int n) {
        this.internalRemoveItem(n);
        ((Menu)this.mWrappedObject).removeItem(n);
    }

    public void setGroupCheckable(int n, boolean bl, boolean bl2) {
        ((Menu)this.mWrappedObject).setGroupCheckable(n, bl, bl2);
    }

    public void setGroupEnabled(int n, boolean bl) {
        ((Menu)this.mWrappedObject).setGroupEnabled(n, bl);
    }

    public void setGroupVisible(int n, boolean bl) {
        ((Menu)this.mWrappedObject).setGroupVisible(n, bl);
    }

    public void setQwertyMode(boolean bl) {
        ((Menu)this.mWrappedObject).setQwertyMode(bl);
    }

    public int size() {
        return ((Menu)this.mWrappedObject).size();
    }
}

