/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ActivityNotFoundException
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.ActionProvider
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.LayoutInflater
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.ViewDebug
 *  android.view.ViewDebug$CapturedViewProperty
 *  android.view.ViewGroup
 *  android.widget.LinearLayout
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.lang.UnsupportedOperationException
 */
package android.support.v7.internal.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class MenuItemImpl
implements SupportMenuItem {
    private static final int CHECKABLE = 1;
    private static final int CHECKED = 2;
    private static final int ENABLED = 16;
    private static final int EXCLUSIVE = 4;
    private static final int HIDDEN = 8;
    private static final int IS_ACTION = 32;
    static final int NO_ICON = 0;
    private static final int SHOW_AS_ACTION_MASK = 3;
    private static final String TAG = "MenuItemImpl";
    private static String sDeleteShortcutLabel;
    private static String sEnterShortcutLabel;
    private static String sPrependShortcutLabel;
    private static String sSpaceShortcutLabel;
    private ActionProvider mActionProvider;
    private View mActionView;
    private final int mCategoryOrder;
    private MenuItem.OnMenuItemClickListener mClickListener;
    private int mFlags = 16;
    private final int mGroup;
    private Drawable mIconDrawable;
    private int mIconResId = 0;
    private final int mId;
    private Intent mIntent;
    private boolean mIsActionViewExpanded = false;
    private Runnable mItemCallback;
    private MenuBuilder mMenu;
    private ContextMenu.ContextMenuInfo mMenuInfo;
    private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;
    private final int mOrdering;
    private char mShortcutAlphabeticChar;
    private char mShortcutNumericChar;
    private int mShowAsAction = 0;
    private SubMenuBuilder mSubMenu;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;

    MenuItemImpl(MenuBuilder menuBuilder, int n, int n2, int n3, int n4, CharSequence charSequence, int n5) {
        this.mMenu = menuBuilder;
        this.mId = n2;
        this.mGroup = n;
        this.mCategoryOrder = n3;
        this.mOrdering = n4;
        this.mTitle = charSequence;
        this.mShowAsAction = n5;
    }

    public void actionFormatChanged() {
        this.mMenu.onItemActionRequestChanged(this);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean collapseActionView() {
        block5 : {
            block4 : {
                if ((this.mShowAsAction & 8) == 0) break block4;
                if (this.mActionView == null) {
                    return true;
                }
                if (this.mOnActionExpandListener == null || this.mOnActionExpandListener.onMenuItemActionCollapse(this)) break block5;
            }
            return false;
        }
        return this.mMenu.collapseItemActionView(this);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean expandActionView() {
        if ((this.mShowAsAction & 8) == 0 || this.mActionView == null || this.mOnActionExpandListener != null && !this.mOnActionExpandListener.onMenuItemActionExpand(this)) {
            return false;
        }
        return this.mMenu.expandItemActionView(this);
    }

    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("Implementation should use getSupportActionProvider!");
    }

    @Override
    public View getActionView() {
        if (this.mActionView != null) {
            return this.mActionView;
        }
        if (this.mActionProvider != null) {
            this.mActionView = this.mActionProvider.onCreateActionView(this);
            return this.mActionView;
        }
        return null;
    }

    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    Runnable getCallback() {
        return this.mItemCallback;
    }

    public int getGroupId() {
        return this.mGroup;
    }

    public Drawable getIcon() {
        if (this.mIconDrawable != null) {
            return this.mIconDrawable;
        }
        if (this.mIconResId != 0) {
            Drawable drawable2 = this.mMenu.getResources().getDrawable(this.mIconResId);
            this.mIconResId = 0;
            this.mIconDrawable = drawable2;
            return drawable2;
        }
        return null;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    @ViewDebug.CapturedViewProperty
    public int getItemId() {
        return this.mId;
    }

    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.mMenuInfo;
    }

    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }

    public int getOrder() {
        return this.mCategoryOrder;
    }

    public int getOrdering() {
        return this.mOrdering;
    }

    char getShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    String getShortcutLabel() {
        char c = this.getShortcut();
        if (c == '\u0000') {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(sPrependShortcutLabel);
        switch (c) {
            default: {
                stringBuilder.append(c);
                do {
                    return stringBuilder.toString();
                    break;
                } while (true);
            }
            case '\n': {
                stringBuilder.append(sEnterShortcutLabel);
                return stringBuilder.toString();
            }
            case '\b': {
                stringBuilder.append(sDeleteShortcutLabel);
                return stringBuilder.toString();
            }
            case ' ': 
        }
        stringBuilder.append(sSpaceShortcutLabel);
        return stringBuilder.toString();
    }

    public SubMenu getSubMenu() {
        return this.mSubMenu;
    }

    @Override
    public ActionProvider getSupportActionProvider() {
        return this.mActionProvider;
    }

    @ViewDebug.CapturedViewProperty
    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getTitleCondensed() {
        if (this.mTitleCondensed != null) {
            return this.mTitleCondensed;
        }
        return this.mTitle;
    }

    CharSequence getTitleForItemView(MenuView.ItemView itemView) {
        if (itemView != null && itemView.prefersCondensedTitle()) {
            return this.getTitleCondensed();
        }
        return this.getTitle();
    }

    public boolean hasCollapsibleActionView() {
        if ((this.mShowAsAction & 8) != 0 && this.mActionView != null) {
            return true;
        }
        return false;
    }

    public boolean hasSubMenu() {
        if (this.mSubMenu != null) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean invoke() {
        block8 : {
            block7 : {
                if (this.mClickListener != null && this.mClickListener.onMenuItemClick((MenuItem)this) || this.mMenu.dispatchMenuItemSelected(this.mMenu.getRootMenu(), this)) break block7;
                if (this.mItemCallback != null) {
                    this.mItemCallback.run();
                    return true;
                }
                if (this.mIntent != null) {
                    try {
                        this.mMenu.getContext().startActivity(this.mIntent);
                        return true;
                    }
                    catch (ActivityNotFoundException activityNotFoundException) {
                        Log.e((String)"MenuItemImpl", (String)"Can't find activity to handle intent; ignoring", (Throwable)activityNotFoundException);
                    }
                }
                if (this.mActionProvider == null || !this.mActionProvider.onPerformDefaultAction()) break block8;
            }
            return true;
        }
        return false;
    }

    public boolean isActionButton() {
        if ((this.mFlags & 32) == 32) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return this.mIsActionViewExpanded;
    }

    public boolean isCheckable() {
        if ((this.mFlags & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isChecked() {
        if ((this.mFlags & 2) == 2) {
            return true;
        }
        return false;
    }

    public boolean isEnabled() {
        if ((this.mFlags & 16) != 0) {
            return true;
        }
        return false;
    }

    public boolean isExclusiveCheckable() {
        if ((this.mFlags & 4) != 0) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isVisible() {
        if (this.mActionProvider != null && this.mActionProvider.overridesItemVisibility()) {
            if ((this.mFlags & 8) == 0 && this.mActionProvider.isVisible()) return true;
            return false;
        }
        if ((this.mFlags & 8) != 0) return false;
        return true;
    }

    public boolean requestsActionButton() {
        if ((this.mShowAsAction & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean requiresActionButton() {
        if ((this.mShowAsAction & 2) == 2) {
            return true;
        }
        return false;
    }

    public MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException("Implementation should use setSupportActionProvider!");
    }

    @Override
    public SupportMenuItem setActionView(int n) {
        Context context = this.mMenu.getContext();
        this.setActionView(LayoutInflater.from((Context)context).inflate(n, (ViewGroup)new LinearLayout(context), false));
        return this;
    }

    @Override
    public SupportMenuItem setActionView(View view) {
        this.mActionView = view;
        this.mActionProvider = null;
        if (view != null && view.getId() == -1 && this.mId > 0) {
            view.setId(this.mId);
        }
        this.mMenu.onItemActionRequestChanged(this);
        return this;
    }

    public void setActionViewExpanded(boolean bl) {
        this.mIsActionViewExpanded = bl;
        this.mMenu.onItemsChanged(false);
    }

    public MenuItem setAlphabeticShortcut(char c) {
        if (this.mShortcutAlphabeticChar == c) {
            return this;
        }
        this.mShortcutAlphabeticChar = Character.toLowerCase((char)c);
        this.mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setCallback(Runnable runnable) {
        this.mItemCallback = runnable;
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public MenuItem setCheckable(boolean bl) {
        int n = this.mFlags;
        int n2 = this.mFlags;
        int n3 = bl ? 1 : 0;
        this.mFlags = n3 | n2 & -2;
        if (n != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
        return this;
    }

    public MenuItem setChecked(boolean bl) {
        if ((this.mFlags & 4) != 0) {
            this.mMenu.setExclusiveItemChecked(this);
            return this;
        }
        this.setCheckedInt(bl);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    void setCheckedInt(boolean bl) {
        int n = this.mFlags;
        int n2 = this.mFlags;
        int n3 = bl ? 2 : 0;
        this.mFlags = n3 | n2 & -3;
        if (n != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public MenuItem setEnabled(boolean bl) {
        this.mFlags = bl ? (this.mFlags |= 16) : (this.mFlags &= -17);
        this.mMenu.onItemsChanged(false);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setExclusiveCheckable(boolean bl) {
        int n = this.mFlags;
        int n2 = bl ? 4 : 0;
        this.mFlags = n2 | n & -5;
    }

    public MenuItem setIcon(int n) {
        this.mIconDrawable = null;
        this.mIconResId = n;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setIcon(Drawable drawable2) {
        this.mIconResId = 0;
        this.mIconDrawable = drawable2;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        this.mIntent = intent;
        return this;
    }

    public void setIsActionButton(boolean bl) {
        if (bl) {
            this.mFlags |= 32;
            return;
        }
        this.mFlags &= -33;
    }

    void setMenuInfo(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.mMenuInfo = contextMenuInfo;
    }

    public MenuItem setNumericShortcut(char c) {
        if (this.mShortcutNumericChar == c) {
            return this;
        }
        this.mShortcutNumericChar = c;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException("Implementation should use setSupportOnActionExpandListener!");
    }

    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.mClickListener = onMenuItemClickListener;
        return this;
    }

    public MenuItem setShortcut(char c, char c2) {
        this.mShortcutNumericChar = c;
        this.mShortcutAlphabeticChar = Character.toLowerCase((char)c2);
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override
    public void setShowAsAction(int n) {
        switch (n & 3) {
            default: {
                throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
            }
            case 0: 
            case 1: 
            case 2: 
        }
        this.mShowAsAction = n;
        this.mMenu.onItemActionRequestChanged(this);
    }

    @Override
    public SupportMenuItem setShowAsActionFlags(int n) {
        this.setShowAsAction(n);
        return this;
    }

    void setSubMenu(SubMenuBuilder subMenuBuilder) {
        this.mSubMenu = subMenuBuilder;
        subMenuBuilder.setHeaderTitle(this.getTitle());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public SupportMenuItem setSupportActionProvider(ActionProvider actionProvider) {
        block5 : {
            block4 : {
                if (this.mActionProvider == actionProvider) break block4;
                this.mActionView = null;
                if (this.mActionProvider != null) {
                    this.mActionProvider.setVisibilityListener(null);
                }
                this.mActionProvider = actionProvider;
                this.mMenu.onItemsChanged(true);
                if (actionProvider != null) break block5;
            }
            return this;
        }
        actionProvider.setVisibilityListener(new ActionProvider.VisibilityListener(){

            @Override
            public void onActionProviderVisibilityChanged(boolean bl) {
                MenuItemImpl.this.mMenu.onItemVisibleChanged(MenuItemImpl.this);
            }
        });
        return this;
    }

    @Override
    public SupportMenuItem setSupportOnActionExpandListener(MenuItemCompat.OnActionExpandListener onActionExpandListener) {
        this.mOnActionExpandListener = onActionExpandListener;
        return this;
    }

    public MenuItem setTitle(int n) {
        return this.setTitle(this.mMenu.getContext().getString(n));
    }

    public MenuItem setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.mMenu.onItemsChanged(false);
        if (this.mSubMenu != null) {
            this.mSubMenu.setHeaderTitle(charSequence);
        }
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.mTitleCondensed = charSequence;
        if (charSequence == null) {
            charSequence = this.mTitle;
        }
        this.mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setVisible(boolean bl) {
        if (this.setVisibleInt(bl)) {
            this.mMenu.onItemVisibleChanged(this);
        }
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean setVisibleInt(boolean bl) {
        boolean bl2 = false;
        int n = this.mFlags;
        int n2 = this.mFlags;
        int n3 = bl ? 0 : 8;
        this.mFlags = n3 | n2 & -9;
        bl = bl2;
        if (n == this.mFlags) return bl;
        return true;
    }

    public boolean shouldShowIcon() {
        return this.mMenu.getOptionalIconsVisible();
    }

    boolean shouldShowShortcut() {
        if (this.mMenu.isShortcutsVisible() && this.getShortcut() != '\u0000') {
            return true;
        }
        return false;
    }

    public boolean showsTextAsAction() {
        if ((this.mShowAsAction & 4) == 4) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.mTitle.toString();
    }

}

