/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.ActionProvider
 *  android.view.CollapsibleActionView
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 *  android.widget.FrameLayout
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Method
 */
package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.internal.view.menu.BaseMenuWrapper;
import android.support.v7.internal.view.menu.BaseWrapper;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.reflect.Method;

public class MenuItemWrapperICS
extends BaseMenuWrapper<MenuItem>
implements SupportMenuItem {
    static final String LOG_TAG = "MenuItemWrapper";
    private final boolean mEmulateProviderVisibilityOverride;
    private boolean mLastRequestVisible;
    private Method mSetExclusiveCheckableMethod;

    MenuItemWrapperICS(MenuItem menuItem) {
        this(menuItem, true);
    }

    MenuItemWrapperICS(MenuItem menuItem, boolean bl) {
        super(menuItem);
        this.mLastRequestVisible = menuItem.isVisible();
        this.mEmulateProviderVisibilityOverride = bl;
    }

    final boolean checkActionProviderOverrideVisibility() {
        boolean bl;
        boolean bl2 = bl = false;
        if (this.mLastRequestVisible) {
            ActionProvider actionProvider = this.getSupportActionProvider();
            bl2 = bl;
            if (actionProvider != null) {
                bl2 = bl;
                if (actionProvider.overridesItemVisibility()) {
                    bl2 = bl;
                    if (!actionProvider.isVisible()) {
                        this.wrappedSetVisible(false);
                        bl2 = true;
                    }
                }
            }
        }
        return bl2;
    }

    @Override
    public boolean collapseActionView() {
        return ((MenuItem)this.mWrappedObject).collapseActionView();
    }

    ActionProviderWrapper createActionProviderWrapper(ActionProvider actionProvider) {
        return new ActionProviderWrapper(actionProvider);
    }

    @Override
    public boolean expandActionView() {
        return ((MenuItem)this.mWrappedObject).expandActionView();
    }

    public android.view.ActionProvider getActionProvider() {
        return ((MenuItem)this.mWrappedObject).getActionProvider();
    }

    @Override
    public View getActionView() {
        View view;
        View view2 = view = ((MenuItem)this.mWrappedObject).getActionView();
        if (view instanceof CollapsibleActionViewWrapper) {
            view2 = ((CollapsibleActionViewWrapper)view).getWrappedView();
        }
        return view2;
    }

    public char getAlphabeticShortcut() {
        return ((MenuItem)this.mWrappedObject).getAlphabeticShortcut();
    }

    public int getGroupId() {
        return ((MenuItem)this.mWrappedObject).getGroupId();
    }

    public Drawable getIcon() {
        return ((MenuItem)this.mWrappedObject).getIcon();
    }

    public Intent getIntent() {
        return ((MenuItem)this.mWrappedObject).getIntent();
    }

    public int getItemId() {
        return ((MenuItem)this.mWrappedObject).getItemId();
    }

    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return ((MenuItem)this.mWrappedObject).getMenuInfo();
    }

    public char getNumericShortcut() {
        return ((MenuItem)this.mWrappedObject).getNumericShortcut();
    }

    public int getOrder() {
        return ((MenuItem)this.mWrappedObject).getOrder();
    }

    public SubMenu getSubMenu() {
        return this.getSubMenuWrapper(((MenuItem)this.mWrappedObject).getSubMenu());
    }

    @Override
    public ActionProvider getSupportActionProvider() {
        ActionProviderWrapper actionProviderWrapper = (ActionProviderWrapper)((MenuItem)this.mWrappedObject).getActionProvider();
        if (actionProviderWrapper != null) {
            return actionProviderWrapper.mInner;
        }
        return null;
    }

    public CharSequence getTitle() {
        return ((MenuItem)this.mWrappedObject).getTitle();
    }

    public CharSequence getTitleCondensed() {
        return ((MenuItem)this.mWrappedObject).getTitleCondensed();
    }

    public boolean hasSubMenu() {
        return ((MenuItem)this.mWrappedObject).hasSubMenu();
    }

    @Override
    public boolean isActionViewExpanded() {
        return ((MenuItem)this.mWrappedObject).isActionViewExpanded();
    }

    public boolean isCheckable() {
        return ((MenuItem)this.mWrappedObject).isCheckable();
    }

    public boolean isChecked() {
        return ((MenuItem)this.mWrappedObject).isChecked();
    }

    public boolean isEnabled() {
        return ((MenuItem)this.mWrappedObject).isEnabled();
    }

    public boolean isVisible() {
        return ((MenuItem)this.mWrappedObject).isVisible();
    }

    public MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        ((MenuItem)this.mWrappedObject).setActionProvider(actionProvider);
        if (actionProvider != null && this.mEmulateProviderVisibilityOverride) {
            this.checkActionProviderOverrideVisibility();
        }
        return this;
    }

    @Override
    public MenuItem setActionView(int n) {
        ((MenuItem)this.mWrappedObject).setActionView(n);
        View view = ((MenuItem)this.mWrappedObject).getActionView();
        if (view instanceof android.support.v7.view.CollapsibleActionView) {
            ((MenuItem)this.mWrappedObject).setActionView((View)new CollapsibleActionViewWrapper(view));
        }
        return this;
    }

    @Override
    public MenuItem setActionView(View view) {
        Object object = view;
        if (view instanceof android.support.v7.view.CollapsibleActionView) {
            object = new CollapsibleActionViewWrapper(view);
        }
        ((MenuItem)this.mWrappedObject).setActionView((View)object);
        return this;
    }

    public MenuItem setAlphabeticShortcut(char c) {
        ((MenuItem)this.mWrappedObject).setAlphabeticShortcut(c);
        return this;
    }

    public MenuItem setCheckable(boolean bl) {
        ((MenuItem)this.mWrappedObject).setCheckable(bl);
        return this;
    }

    public MenuItem setChecked(boolean bl) {
        ((MenuItem)this.mWrappedObject).setChecked(bl);
        return this;
    }

    public MenuItem setEnabled(boolean bl) {
        ((MenuItem)this.mWrappedObject).setEnabled(bl);
        return this;
    }

    public void setExclusiveCheckable(boolean bl) {
        try {
            if (this.mSetExclusiveCheckableMethod == null) {
                this.mSetExclusiveCheckableMethod = ((MenuItem)this.mWrappedObject).getClass().getDeclaredMethod("setExclusiveCheckable", new Class[]{Boolean.TYPE});
            }
            this.mSetExclusiveCheckableMethod.invoke(this.mWrappedObject, new Object[]{bl});
            return;
        }
        catch (Exception exception) {
            Log.w((String)"MenuItemWrapper", (String)"Error while calling setExclusiveCheckable", (Throwable)exception);
            return;
        }
    }

    public MenuItem setIcon(int n) {
        ((MenuItem)this.mWrappedObject).setIcon(n);
        return this;
    }

    public MenuItem setIcon(Drawable drawable2) {
        ((MenuItem)this.mWrappedObject).setIcon(drawable2);
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        ((MenuItem)this.mWrappedObject).setIntent(intent);
        return this;
    }

    public MenuItem setNumericShortcut(char c) {
        ((MenuItem)this.mWrappedObject).setNumericShortcut(c);
        return this;
    }

    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        ((MenuItem)this.mWrappedObject).setOnActionExpandListener(onActionExpandListener);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        MenuItem menuItem = (MenuItem)this.mWrappedObject;
        onMenuItemClickListener = onMenuItemClickListener != null ? new OnMenuItemClickListenerWrapper(onMenuItemClickListener) : null;
        menuItem.setOnMenuItemClickListener(onMenuItemClickListener);
        return this;
    }

    public MenuItem setShortcut(char c, char c2) {
        ((MenuItem)this.mWrappedObject).setShortcut(c, c2);
        return this;
    }

    @Override
    public void setShowAsAction(int n) {
        ((MenuItem)this.mWrappedObject).setShowAsAction(n);
    }

    @Override
    public MenuItem setShowAsActionFlags(int n) {
        ((MenuItem)this.mWrappedObject).setShowAsActionFlags(n);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public SupportMenuItem setSupportActionProvider(ActionProvider object) {
        MenuItem menuItem = (MenuItem)this.mWrappedObject;
        object = object != null ? this.createActionProviderWrapper((ActionProvider)object) : null;
        menuItem.setActionProvider((android.view.ActionProvider)object);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public SupportMenuItem setSupportOnActionExpandListener(MenuItemCompat.OnActionExpandListener object) {
        MenuItem menuItem = (MenuItem)this.mWrappedObject;
        object = object != null ? new OnActionExpandListenerWrapper((MenuItemCompat.OnActionExpandListener)object) : null;
        menuItem.setOnActionExpandListener((MenuItem.OnActionExpandListener)object);
        return null;
    }

    public MenuItem setTitle(int n) {
        ((MenuItem)this.mWrappedObject).setTitle(n);
        return this;
    }

    public MenuItem setTitle(CharSequence charSequence) {
        ((MenuItem)this.mWrappedObject).setTitle(charSequence);
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence charSequence) {
        ((MenuItem)this.mWrappedObject).setTitleCondensed(charSequence);
        return this;
    }

    public MenuItem setVisible(boolean bl) {
        if (this.mEmulateProviderVisibilityOverride) {
            this.mLastRequestVisible = bl;
            if (this.checkActionProviderOverrideVisibility()) {
                return this;
            }
        }
        return this.wrappedSetVisible(bl);
    }

    final MenuItem wrappedSetVisible(boolean bl) {
        return ((MenuItem)this.mWrappedObject).setVisible(bl);
    }

    class ActionProviderWrapper
    extends android.view.ActionProvider {
        final ActionProvider mInner;

        public ActionProviderWrapper(ActionProvider actionProvider) {
            super(actionProvider.getContext());
            this.mInner = actionProvider;
            if (MenuItemWrapperICS.this.mEmulateProviderVisibilityOverride) {
                this.mInner.setVisibilityListener(new ActionProvider.VisibilityListener(MenuItemWrapperICS.this){
                    final /* synthetic */ MenuItemWrapperICS val$this$0;
                    {
                        this.val$this$0 = menuItemWrapperICS;
                    }

                    @Override
                    public void onActionProviderVisibilityChanged(boolean bl) {
                        if (ActionProviderWrapper.this.mInner.overridesItemVisibility() && MenuItemWrapperICS.this.mLastRequestVisible) {
                            MenuItemWrapperICS.this.wrappedSetVisible(bl);
                        }
                    }
                });
            }
        }

        public boolean hasSubMenu() {
            return this.mInner.hasSubMenu();
        }

        public View onCreateActionView() {
            if (MenuItemWrapperICS.this.mEmulateProviderVisibilityOverride) {
                MenuItemWrapperICS.this.checkActionProviderOverrideVisibility();
            }
            return this.mInner.onCreateActionView();
        }

        public boolean onPerformDefaultAction() {
            return this.mInner.onPerformDefaultAction();
        }

        public void onPrepareSubMenu(SubMenu subMenu) {
            this.mInner.onPrepareSubMenu(MenuItemWrapperICS.this.getSubMenuWrapper(subMenu));
        }

    }

    static class CollapsibleActionViewWrapper
    extends FrameLayout
    implements CollapsibleActionView {
        final android.support.v7.view.CollapsibleActionView mWrappedView;

        CollapsibleActionViewWrapper(View view) {
            super(view.getContext());
            this.mWrappedView = (android.support.v7.view.CollapsibleActionView)view;
            this.addView(view);
        }

        View getWrappedView() {
            return (View)this.mWrappedView;
        }

        public void onActionViewCollapsed() {
            this.mWrappedView.onActionViewCollapsed();
        }

        public void onActionViewExpanded() {
            this.mWrappedView.onActionViewExpanded();
        }
    }

    private class OnActionExpandListenerWrapper
    extends BaseWrapper<MenuItemCompat.OnActionExpandListener>
    implements MenuItem.OnActionExpandListener {
        OnActionExpandListenerWrapper(MenuItemCompat.OnActionExpandListener onActionExpandListener) {
            super(onActionExpandListener);
        }

        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            return ((MenuItemCompat.OnActionExpandListener)this.mWrappedObject).onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }

        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            return ((MenuItemCompat.OnActionExpandListener)this.mWrappedObject).onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }

    private class OnMenuItemClickListenerWrapper
    extends BaseWrapper<MenuItem.OnMenuItemClickListener>
    implements MenuItem.OnMenuItemClickListener {
        OnMenuItemClickListenerWrapper(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
            super(onMenuItemClickListener);
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            return ((MenuItem.OnMenuItemClickListener)this.mWrappedObject).onMenuItemClick((MenuItem)MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }

}

