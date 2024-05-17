/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.SparseArray
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.KeyCharacterMap
 *  android.view.KeyCharacterMap$KeyData
 *  android.view.KeyEvent
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.ref.WeakReference
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 *  java.util.concurrent.CopyOnWriteArrayList
 */
package android.support.v7.internal.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder
implements SupportMenu {
    private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
    private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
    private static final String PRESENTER_KEY = "android:menu:presenters";
    private static final String TAG = "MenuBuilder";
    private static final int[] sCategoryToOrder = new int[]{1, 4, 5, 3, 2, 0};
    private ArrayList<MenuItemImpl> mActionItems;
    private Callback mCallback;
    private final Context mContext;
    private ContextMenu.ContextMenuInfo mCurrentMenuInfo;
    private int mDefaultShowAsAction = 0;
    private MenuItemImpl mExpandedItem;
    Drawable mHeaderIcon;
    CharSequence mHeaderTitle;
    View mHeaderView;
    private boolean mIsActionItemsStale;
    private boolean mIsClosing = false;
    private boolean mIsVisibleItemsStale;
    private ArrayList<MenuItemImpl> mItems;
    private boolean mItemsChangedWhileDispatchPrevented = false;
    private ArrayList<MenuItemImpl> mNonActionItems;
    private boolean mOptionalIconsVisible = false;
    private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters = new CopyOnWriteArrayList();
    private boolean mPreventDispatchingItemsChanged = false;
    private boolean mQwertyMode;
    private final Resources mResources;
    private boolean mShortcutsVisible;
    private ArrayList<MenuItemImpl> mTempShortcutItemList = new ArrayList();
    private ArrayList<MenuItemImpl> mVisibleItems;

    public MenuBuilder(Context context) {
        this.mContext = context;
        this.mResources = context.getResources();
        this.mItems = new ArrayList();
        this.mVisibleItems = new ArrayList();
        this.mIsVisibleItemsStale = true;
        this.mActionItems = new ArrayList();
        this.mNonActionItems = new ArrayList();
        this.mIsActionItemsStale = true;
        this.setShortcutsVisibleInner(true);
    }

    private MenuItem addInternal(int n, int n2, int n3, CharSequence object) {
        int n4 = MenuBuilder.getOrdering(n3);
        object = new MenuItemImpl(this, n, n2, n3, n4, (CharSequence)object, this.mDefaultShowAsAction);
        if (this.mCurrentMenuInfo != null) {
            object.setMenuInfo(this.mCurrentMenuInfo);
        }
        this.mItems.add(MenuBuilder.findInsertIndex(this.mItems, n4), object);
        this.onItemsChanged(true);
        return object;
    }

    private void dispatchPresenterUpdate(boolean bl) {
        if (this.mPresenters.isEmpty()) {
            return;
        }
        this.stopDispatchingItemsChanged();
        for (WeakReference weakReference : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove((Object)weakReference);
                continue;
            }
            menuPresenter.updateMenuView(bl);
        }
        this.startDispatchingItemsChanged();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void dispatchRestoreInstanceState(Bundle bundle) {
        if ((bundle = bundle.getSparseParcelableArray("android:menu:presenters")) != null && !this.mPresenters.isEmpty()) {
            for (WeakReference weakReference : this.mPresenters) {
                MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove((Object)weakReference);
                    continue;
                }
                int n = menuPresenter.getId();
                if (n <= 0 || (weakReference = (Parcelable)bundle.get(n)) == null) continue;
                menuPresenter.onRestoreInstanceState((Parcelable)weakReference);
            }
        }
    }

    private void dispatchSaveInstanceState(Bundle bundle) {
        if (this.mPresenters.isEmpty()) {
            return;
        }
        SparseArray sparseArray = new SparseArray();
        for (WeakReference weakReference : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove((Object)weakReference);
                continue;
            }
            int n = menuPresenter.getId();
            if (n <= 0 || (weakReference = menuPresenter.onSaveInstanceState()) == null) continue;
            sparseArray.put(n, (Object)weakReference);
        }
        bundle.putSparseParcelableArray("android:menu:presenters", sparseArray);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean dispatchSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (this.mPresenters.isEmpty()) {
            return false;
        }
        boolean bl = false;
        Iterator iterator = this.mPresenters.iterator();
        do {
            boolean bl2 = bl;
            if (!iterator.hasNext()) return bl2;
            WeakReference weakReference = (WeakReference)iterator.next();
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove((Object)weakReference);
                continue;
            }
            if (bl) continue;
            bl = menuPresenter.onSubMenuSelected(subMenuBuilder);
        } while (true);
    }

    private static int findInsertIndex(ArrayList<MenuItemImpl> arrayList, int n) {
        for (int i = arrayList.size() - 1; i >= 0; --i) {
            if (((MenuItemImpl)arrayList.get(i)).getOrdering() > n) continue;
            return i + 1;
        }
        return 0;
    }

    private static int getOrdering(int n) {
        int n2 = (-65536 & n) >> 16;
        if (n2 < 0 || n2 >= sCategoryToOrder.length) {
            throw new IllegalArgumentException("order does not contain a valid category.");
        }
        return sCategoryToOrder[n2] << 16 | 65535 & n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void removeItemAtInt(int n, boolean bl) {
        block3 : {
            block2 : {
                if (n < 0 || n >= this.mItems.size()) break block2;
                this.mItems.remove(n);
                if (bl) break block3;
            }
            return;
        }
        this.onItemsChanged(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setHeaderInternal(int n, CharSequence charSequence, int n2, Drawable drawable2, View view) {
        Resources resources = this.getResources();
        if (view != null) {
            this.mHeaderView = view;
            this.mHeaderTitle = null;
            this.mHeaderIcon = null;
        } else {
            if (n > 0) {
                this.mHeaderTitle = resources.getText(n);
            } else if (charSequence != null) {
                this.mHeaderTitle = charSequence;
            }
            if (n2 > 0) {
                this.mHeaderIcon = resources.getDrawable(n2);
            } else if (drawable2 != null) {
                this.mHeaderIcon = drawable2;
            }
            this.mHeaderView = null;
        }
        this.onItemsChanged(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setShortcutsVisibleInner(boolean bl) {
        boolean bl2 = true;
        bl = bl && this.mResources.getConfiguration().keyboard != 1 && this.mResources.getBoolean(R.bool.abc_config_showMenuShortcutsWhenKeyboardPresent) ? bl2 : false;
        this.mShortcutsVisible = bl;
    }

    public MenuItem add(int n) {
        return this.addInternal(0, 0, 0, this.mResources.getString(n));
    }

    public MenuItem add(int n, int n2, int n3, int n4) {
        return this.addInternal(n, n2, n3, this.mResources.getString(n4));
    }

    public MenuItem add(int n, int n2, int n3, CharSequence charSequence) {
        return this.addInternal(n, n2, n3, charSequence);
    }

    public MenuItem add(CharSequence charSequence) {
        return this.addInternal(0, 0, 0, charSequence);
    }

    /*
     * Enabled aggressive block sorting
     */
    public int addIntentOptions(int n, int n2, int n3, ComponentName componentName, Intent[] arrintent, Intent intent, int n4, MenuItem[] arrmenuItem) {
        PackageManager packageManager = this.mContext.getPackageManager();
        List list = packageManager.queryIntentActivityOptions(componentName, arrintent, intent, 0);
        int n5 = list != null ? list.size() : 0;
        if ((n4 & 1) == 0) {
            this.removeGroup(n);
        }
        n4 = 0;
        while (n4 < n5) {
            ResolveInfo resolveInfo = (ResolveInfo)list.get(n4);
            componentName = resolveInfo.specificIndex < 0 ? intent : arrintent[resolveInfo.specificIndex];
            componentName = new Intent((Intent)componentName);
            componentName.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
            componentName = this.add(n, n2, n3, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent((Intent)componentName);
            if (arrmenuItem != null && resolveInfo.specificIndex >= 0) {
                arrmenuItem[resolveInfo.specificIndex] = componentName;
            }
            ++n4;
        }
        return n5;
    }

    public void addMenuPresenter(MenuPresenter menuPresenter) {
        this.mPresenters.add((Object)new WeakReference((Object)menuPresenter));
        menuPresenter.initForMenu(this.mContext, this);
        this.mIsActionItemsStale = true;
    }

    public SubMenu addSubMenu(int n) {
        return this.addSubMenu(0, 0, 0, this.mResources.getString(n));
    }

    public SubMenu addSubMenu(int n, int n2, int n3, int n4) {
        return this.addSubMenu(n, n2, n3, this.mResources.getString(n4));
    }

    public SubMenu addSubMenu(int n, int n2, int n3, CharSequence object) {
        object = (MenuItemImpl)this.addInternal(n, n2, n3, (CharSequence)object);
        SubMenuBuilder subMenuBuilder = new SubMenuBuilder(this.mContext, this, (MenuItemImpl)object);
        object.setSubMenu(subMenuBuilder);
        return subMenuBuilder;
    }

    public SubMenu addSubMenu(CharSequence charSequence) {
        return this.addSubMenu(0, 0, 0, charSequence);
    }

    public void changeMenuMode() {
        if (this.mCallback != null) {
            this.mCallback.onMenuModeChange(this);
        }
    }

    public void clear() {
        if (this.mExpandedItem != null) {
            this.collapseItemActionView(this.mExpandedItem);
        }
        this.mItems.clear();
        this.onItemsChanged(true);
    }

    public void clearAll() {
        this.mPreventDispatchingItemsChanged = true;
        this.clear();
        this.clearHeader();
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.onItemsChanged(true);
    }

    public void clearHeader() {
        this.mHeaderIcon = null;
        this.mHeaderTitle = null;
        this.mHeaderView = null;
        this.onItemsChanged(false);
    }

    public void close() {
        this.close(true);
    }

    final void close(boolean bl) {
        if (this.mIsClosing) {
            return;
        }
        this.mIsClosing = true;
        for (WeakReference weakReference : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove((Object)weakReference);
                continue;
            }
            menuPresenter.onCloseMenu(this, bl);
        }
        this.mIsClosing = false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean collapseItemActionView(MenuItemImpl menuItemImpl) {
        boolean bl;
        if (this.mPresenters.isEmpty()) return false;
        if (this.mExpandedItem != menuItemImpl) {
            return false;
        }
        boolean bl2 = false;
        this.stopDispatchingItemsChanged();
        Iterator iterator = this.mPresenters.iterator();
        do {
            bl = bl2;
            if (!iterator.hasNext()) break;
            WeakReference weakReference = (WeakReference)iterator.next();
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove((Object)weakReference);
                continue;
            }
            bl2 = bl = menuPresenter.collapseItemActionView(this, menuItemImpl);
            if (bl) break;
        } while (true);
        this.startDispatchingItemsChanged();
        bl2 = bl;
        if (!bl) return bl2;
        this.mExpandedItem = null;
        return bl;
    }

    boolean dispatchMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        if (this.mCallback != null && this.mCallback.onMenuItemSelected(menuBuilder, menuItem)) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean expandItemActionView(MenuItemImpl menuItemImpl) {
        boolean bl;
        if (this.mPresenters.isEmpty()) {
            return false;
        }
        boolean bl2 = false;
        this.stopDispatchingItemsChanged();
        Iterator iterator = this.mPresenters.iterator();
        do {
            bl = bl2;
            if (!iterator.hasNext()) break;
            WeakReference weakReference = (WeakReference)iterator.next();
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove((Object)weakReference);
                continue;
            }
            bl2 = bl = menuPresenter.expandItemActionView(this, menuItemImpl);
            if (bl) break;
        } while (true);
        this.startDispatchingItemsChanged();
        bl2 = bl;
        if (!bl) return bl2;
        this.mExpandedItem = menuItemImpl;
        return bl;
    }

    public int findGroupIndex(int n) {
        return this.findGroupIndex(n, 0);
    }

    public int findGroupIndex(int n, int n2) {
        int n3 = this.size();
        int n4 = n2;
        if (n2 < 0) {
            n4 = 0;
        }
        while (n4 < n3) {
            if (((MenuItemImpl)this.mItems.get(n4)).getGroupId() == n) {
                return n4;
            }
            ++n4;
        }
        return -1;
    }

    public MenuItem findItem(int n) {
        int n2 = this.size();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(i);
            if (menuItemImpl.getItemId() == n) {
                return menuItemImpl;
            }
            if (!menuItemImpl.hasSubMenu() || (menuItemImpl = menuItemImpl.getSubMenu().findItem(n)) == null) continue;
            return menuItemImpl;
        }
        return null;
    }

    public int findItemIndex(int n) {
        int n2 = this.size();
        for (int i = 0; i < n2; ++i) {
            if (((MenuItemImpl)this.mItems.get(i)).getItemId() != n) continue;
            return i;
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    MenuItemImpl findItemWithShortcutForKey(int n, KeyEvent object) {
        ArrayList<MenuItemImpl> arrayList = this.mTempShortcutItemList;
        arrayList.clear();
        this.findItemsWithShortcutForKey((List<MenuItemImpl>)arrayList, n, (KeyEvent)object);
        if (arrayList.isEmpty()) {
            return null;
        }
        int n2 = object.getMetaState();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        object.getKeyData(keyData);
        int n3 = arrayList.size();
        if (n3 == 1) {
            return (MenuItemImpl)arrayList.get(0);
        }
        boolean bl = this.isQwertyMode();
        int n4 = 0;
        while (n4 < n3) {
            object = (MenuItemImpl)arrayList.get(n4);
            char c = bl ? object.getAlphabeticShortcut() : object.getNumericShortcut();
            if (c == keyData.meta[0] && (n2 & 2) == 0 || c == keyData.meta[2] && (n2 & 2) != 0 || bl && c == '\b' && n == 67) {
                return object;
            }
            ++n4;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    void findItemsWithShortcutForKey(List<MenuItemImpl> list, int n, KeyEvent keyEvent) {
        boolean bl = this.isQwertyMode();
        int n2 = keyEvent.getMetaState();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        if (!keyEvent.getKeyData(keyData) && n != 67) {
            return;
        }
        int n3 = this.mItems.size();
        int n4 = 0;
        while (n4 < n3) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(n4);
            if (menuItemImpl.hasSubMenu()) {
                ((MenuBuilder)menuItemImpl.getSubMenu()).findItemsWithShortcutForKey(list, n, keyEvent);
            }
            char c = bl ? menuItemImpl.getAlphabeticShortcut() : menuItemImpl.getNumericShortcut();
            if ((n2 & 5) == 0 && c != '\u0000' && (c == keyData.meta[0] || c == keyData.meta[2] || bl && c == '\b' && n == 67) && menuItemImpl.isEnabled()) {
                list.add((Object)menuItemImpl);
            }
            ++n4;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void flagActionItems() {
        if (!this.mIsActionItemsStale) {
            return;
        }
        int n = 0;
        for (Object object : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)object.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(object);
                continue;
            }
            n |= menuPresenter.flagActionItems();
        }
        if (n == 0) {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            this.mNonActionItems.addAll(this.getVisibleItems());
        } else {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            ArrayList<MenuItemImpl> arrayList = this.getVisibleItems();
            int n2 = arrayList.size();
            for (n = 0; n < n2; ++n) {
                Object object;
                object = (MenuItemImpl)arrayList.get(n);
                if (object.isActionButton()) {
                    this.mActionItems.add(object);
                    continue;
                }
                this.mNonActionItems.add(object);
            }
        }
        this.mIsActionItemsStale = false;
    }

    ArrayList<MenuItemImpl> getActionItems() {
        this.flagActionItems();
        return this.mActionItems;
    }

    protected String getActionViewStatesKey() {
        return "android:menu:actionviewstates";
    }

    public Context getContext() {
        return this.mContext;
    }

    public MenuItemImpl getExpandedItem() {
        return this.mExpandedItem;
    }

    public Drawable getHeaderIcon() {
        return this.mHeaderIcon;
    }

    public CharSequence getHeaderTitle() {
        return this.mHeaderTitle;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public MenuItem getItem(int n) {
        return (MenuItem)this.mItems.get(n);
    }

    ArrayList<MenuItemImpl> getNonActionItems() {
        this.flagActionItems();
        return this.mNonActionItems;
    }

    boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }

    Resources getResources() {
        return this.mResources;
    }

    public MenuBuilder getRootMenu() {
        return this;
    }

    ArrayList<MenuItemImpl> getVisibleItems() {
        if (!this.mIsVisibleItemsStale) {
            return this.mVisibleItems;
        }
        this.mVisibleItems.clear();
        int n = this.mItems.size();
        for (int i = 0; i < n; ++i) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(i);
            if (!menuItemImpl.isVisible()) continue;
            this.mVisibleItems.add((Object)menuItemImpl);
        }
        this.mIsVisibleItemsStale = false;
        this.mIsActionItemsStale = true;
        return this.mVisibleItems;
    }

    public boolean hasVisibleItems() {
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            if (!((MenuItemImpl)this.mItems.get(i)).isVisible()) continue;
            return true;
        }
        return false;
    }

    boolean isQwertyMode() {
        return this.mQwertyMode;
    }

    public boolean isShortcutKey(int n, KeyEvent keyEvent) {
        if (this.findItemWithShortcutForKey(n, keyEvent) != null) {
            return true;
        }
        return false;
    }

    public boolean isShortcutsVisible() {
        return this.mShortcutsVisible;
    }

    void onItemActionRequestChanged(MenuItemImpl menuItemImpl) {
        this.mIsActionItemsStale = true;
        this.onItemsChanged(true);
    }

    void onItemVisibleChanged(MenuItemImpl menuItemImpl) {
        this.mIsVisibleItemsStale = true;
        this.onItemsChanged(true);
    }

    void onItemsChanged(boolean bl) {
        if (!this.mPreventDispatchingItemsChanged) {
            if (bl) {
                this.mIsVisibleItemsStale = true;
                this.mIsActionItemsStale = true;
            }
            this.dispatchPresenterUpdate(bl);
            return;
        }
        this.mItemsChangedWhileDispatchPrevented = true;
    }

    public boolean performIdentifierAction(int n, int n2) {
        return this.performItemAction(this.findItem(n), n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean performItemAction(MenuItem object, int n) {
        boolean bl;
        Object object2 = (MenuItemImpl)object;
        if (object2 == null) return false;
        if (!object2.isEnabled()) {
            return false;
        }
        boolean bl2 = object2.invoke();
        object = object2.getSupportActionProvider();
        boolean bl3 = object != null && object.hasSubMenu();
        if (object2.hasCollapsibleActionView()) {
            bl = bl2 |= object2.expandActionView();
            if (!bl2) return bl;
            this.close(true);
            return bl2;
        }
        if (object2.hasSubMenu() || bl3) {
            this.close(false);
            if (!object2.hasSubMenu()) {
                object2.setSubMenu(new SubMenuBuilder(this.getContext(), this, (MenuItemImpl)object2));
            }
            object2 = (SubMenuBuilder)object2.getSubMenu();
            if (bl3) {
                object.onPrepareSubMenu((SubMenu)object2);
            }
            bl = bl2 |= this.dispatchSubMenuSelected((SubMenuBuilder)object2);
            if (bl2) return bl;
            this.close(true);
            return bl2;
        }
        bl = bl2;
        if ((n & 1) != 0) return bl;
        this.close(true);
        return bl2;
    }

    public boolean performShortcut(int n, KeyEvent object, int n2) {
        object = this.findItemWithShortcutForKey(n, (KeyEvent)object);
        boolean bl = false;
        if (object != null) {
            bl = this.performItemAction((MenuItem)object, n2);
        }
        if ((n2 & 2) != 0) {
            this.close(true);
        }
        return bl;
    }

    public void removeGroup(int n) {
        int n2 = this.findGroupIndex(n);
        if (n2 >= 0) {
            int n3 = this.mItems.size();
            for (int i = 0; i < n3 - n2 && ((MenuItemImpl)this.mItems.get(n2)).getGroupId() == n; ++i) {
                this.removeItemAtInt(n2, false);
            }
            this.onItemsChanged(true);
        }
    }

    public void removeItem(int n) {
        this.removeItemAtInt(this.findItemIndex(n), true);
    }

    public void removeItemAt(int n) {
        this.removeItemAtInt(n, true);
    }

    public void removeMenuPresenter(MenuPresenter menuPresenter) {
        for (WeakReference weakReference : this.mPresenters) {
            MenuPresenter menuPresenter2 = (MenuPresenter)weakReference.get();
            if (menuPresenter2 != null && menuPresenter2 != menuPresenter) continue;
            this.mPresenters.remove((Object)weakReference);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void restoreActionViewStates(Bundle bundle) {
        block6 : {
            block5 : {
                int n;
                if (bundle == null) break block5;
                SparseArray sparseArray = bundle.getSparseParcelableArray(this.getActionViewStatesKey());
                int n2 = this.size();
                for (n = 0; n < n2; ++n) {
                    MenuItem menuItem = this.getItem(n);
                    View view = MenuItemCompat.getActionView(menuItem);
                    if (view != null && view.getId() != -1) {
                        view.restoreHierarchyState(sparseArray);
                    }
                    if (!menuItem.hasSubMenu()) continue;
                    ((SubMenuBuilder)menuItem.getSubMenu()).restoreActionViewStates(bundle);
                }
                n = bundle.getInt("android:menu:expandedactionview");
                if (n > 0 && (bundle = this.findItem(n)) != null) break block6;
            }
            return;
        }
        MenuItemCompat.expandActionView((MenuItem)bundle);
    }

    public void restorePresenterStates(Bundle bundle) {
        this.dispatchRestoreInstanceState(bundle);
    }

    public void saveActionViewStates(Bundle bundle) {
        SparseArray sparseArray = null;
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            MenuItem menuItem = this.getItem(i);
            View view = MenuItemCompat.getActionView(menuItem);
            SparseArray sparseArray2 = sparseArray;
            if (view != null) {
                sparseArray2 = sparseArray;
                if (view.getId() != -1) {
                    SparseArray sparseArray3 = sparseArray;
                    if (sparseArray == null) {
                        sparseArray3 = new SparseArray();
                    }
                    view.saveHierarchyState(sparseArray3);
                    sparseArray2 = sparseArray3;
                    if (MenuItemCompat.isActionViewExpanded(menuItem)) {
                        bundle.putInt("android:menu:expandedactionview", menuItem.getItemId());
                        sparseArray2 = sparseArray3;
                    }
                }
            }
            if (menuItem.hasSubMenu()) {
                ((SubMenuBuilder)menuItem.getSubMenu()).saveActionViewStates(bundle);
            }
            sparseArray = sparseArray2;
        }
        if (sparseArray != null) {
            bundle.putSparseParcelableArray(this.getActionViewStatesKey(), sparseArray);
        }
    }

    public void savePresenterStates(Bundle bundle) {
        this.dispatchSaveInstanceState(bundle);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setCurrentMenuInfo(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.mCurrentMenuInfo = contextMenuInfo;
    }

    public MenuBuilder setDefaultShowAsAction(int n) {
        this.mDefaultShowAsAction = n;
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    void setExclusiveItemChecked(MenuItem menuItem) {
        int n = menuItem.getGroupId();
        int n2 = this.mItems.size();
        int n3 = 0;
        while (n3 < n2) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(n3);
            if (menuItemImpl.getGroupId() == n && menuItemImpl.isExclusiveCheckable() && menuItemImpl.isCheckable()) {
                boolean bl = menuItemImpl == menuItem;
                menuItemImpl.setCheckedInt(bl);
            }
            ++n3;
        }
    }

    public void setGroupCheckable(int n, boolean bl, boolean bl2) {
        int n2 = this.mItems.size();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(i);
            if (menuItemImpl.getGroupId() != n) continue;
            menuItemImpl.setExclusiveCheckable(bl2);
            menuItemImpl.setCheckable(bl);
        }
    }

    public void setGroupEnabled(int n, boolean bl) {
        int n2 = this.mItems.size();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(i);
            if (menuItemImpl.getGroupId() != n) continue;
            menuItemImpl.setEnabled(bl);
        }
    }

    public void setGroupVisible(int n, boolean bl) {
        int n2 = this.mItems.size();
        boolean bl2 = false;
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = (MenuItemImpl)this.mItems.get(i);
            boolean bl3 = bl2;
            if (menuItemImpl.getGroupId() == n) {
                bl3 = bl2;
                if (menuItemImpl.setVisibleInt(bl)) {
                    bl3 = true;
                }
            }
            bl2 = bl3;
        }
        if (bl2) {
            this.onItemsChanged(true);
        }
    }

    protected MenuBuilder setHeaderIconInt(int n) {
        this.setHeaderInternal(0, null, n, null, null);
        return this;
    }

    protected MenuBuilder setHeaderIconInt(Drawable drawable2) {
        this.setHeaderInternal(0, null, 0, drawable2, null);
        return this;
    }

    protected MenuBuilder setHeaderTitleInt(int n) {
        this.setHeaderInternal(n, null, 0, null, null);
        return this;
    }

    protected MenuBuilder setHeaderTitleInt(CharSequence charSequence) {
        this.setHeaderInternal(0, charSequence, 0, null, null);
        return this;
    }

    protected MenuBuilder setHeaderViewInt(View view) {
        this.setHeaderInternal(0, null, 0, null, view);
        return this;
    }

    void setOptionalIconsVisible(boolean bl) {
        this.mOptionalIconsVisible = bl;
    }

    public void setQwertyMode(boolean bl) {
        this.mQwertyMode = bl;
        this.onItemsChanged(false);
    }

    public void setShortcutsVisible(boolean bl) {
        if (this.mShortcutsVisible == bl) {
            return;
        }
        this.setShortcutsVisibleInner(bl);
        this.onItemsChanged(false);
    }

    public int size() {
        return this.mItems.size();
    }

    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            this.onItemsChanged(true);
        }
    }

    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
        }
    }

    public static interface Callback {
        public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2);

        public void onMenuModeChange(MenuBuilder var1);
    }

    public static interface ItemInvoker {
        public boolean invokeItem(MenuItemImpl var1);
    }

}

