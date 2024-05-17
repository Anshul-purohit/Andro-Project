/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.ActionBar$LayoutParams
 *  android.app.ActionBar$OnMenuVisibilityListener
 *  android.app.ActionBar$OnNavigationListener
 *  android.app.ActionBar$Tab
 *  android.app.ActionBar$TabListener
 *  android.app.Activity
 *  android.app.FragmentTransaction
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.ImageView
 *  android.widget.SpinnerAdapter
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.ref.WeakReference
 *  java.util.ArrayList
 */
package android.support.v7.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class ActionBarImplICS
extends ActionBar {
    final android.app.ActionBar mActionBar;
    FragmentTransaction mActiveTransaction;
    final Activity mActivity;
    private ArrayList<WeakReference<OnMenuVisibilityListenerWrapper>> mAddedMenuVisWrappers = new ArrayList();
    final ActionBar.Callback mCallback;
    private ImageView mHomeActionView;

    public ActionBarImplICS(Activity activity, ActionBar.Callback callback) {
        this(activity, callback, true);
    }

    ActionBarImplICS(Activity activity, ActionBar.Callback callback, boolean bl) {
        this.mActivity = activity;
        this.mCallback = callback;
        this.mActionBar = activity.getActionBar();
        if (bl && (this.getDisplayOptions() & 4) != 0) {
            this.setHomeButtonEnabled(true);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private OnMenuVisibilityListenerWrapper findAndRemoveMenuVisWrapper(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        int n = 0;
        while (n < this.mAddedMenuVisWrappers.size()) {
            int n2;
            OnMenuVisibilityListenerWrapper onMenuVisibilityListenerWrapper = (OnMenuVisibilityListenerWrapper)((WeakReference)this.mAddedMenuVisWrappers.get(n)).get();
            if (onMenuVisibilityListenerWrapper == null) {
                this.mAddedMenuVisWrappers.remove(n);
                n2 = n - 1;
            } else {
                n2 = n;
                if (onMenuVisibilityListenerWrapper.mWrappedListener == onMenuVisibilityListener) {
                    this.mAddedMenuVisWrappers.remove(n);
                    return onMenuVisibilityListenerWrapper;
                }
            }
            n = n2 + 1;
        }
        return null;
    }

    @Override
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener object) {
        if (object != null) {
            object = new OnMenuVisibilityListenerWrapper((ActionBar.OnMenuVisibilityListener)object);
            this.mAddedMenuVisWrappers.add((Object)new WeakReference(object));
            this.mActionBar.addOnMenuVisibilityListener((ActionBar.OnMenuVisibilityListener)object);
        }
    }

    @Override
    public void addTab(ActionBar.Tab tab) {
        this.mActionBar.addTab(((TabWrapper)tab).mWrappedTab);
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n) {
        this.mActionBar.addTab(((TabWrapper)tab).mWrappedTab, n);
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n, boolean bl) {
        this.mActionBar.addTab(((TabWrapper)tab).mWrappedTab, n, bl);
    }

    @Override
    public void addTab(ActionBar.Tab tab, boolean bl) {
        this.mActionBar.addTab(((TabWrapper)tab).mWrappedTab, bl);
    }

    void commitActiveTransaction() {
        if (this.mActiveTransaction != null && !this.mActiveTransaction.isEmpty()) {
            this.mActiveTransaction.commit();
        }
        this.mActiveTransaction = null;
    }

    FragmentTransaction getActiveTransaction() {
        if (this.mActiveTransaction == null) {
            this.mActiveTransaction = this.mCallback.getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
        }
        return this.mActiveTransaction;
    }

    @Override
    public View getCustomView() {
        return this.mActionBar.getCustomView();
    }

    @Override
    public int getDisplayOptions() {
        return this.mActionBar.getDisplayOptions();
    }

    @Override
    public int getHeight() {
        return this.mActionBar.getHeight();
    }

    /*
     * Enabled aggressive block sorting
     */
    ImageView getHomeActionView() {
        if (this.mHomeActionView == null) {
            ViewGroup viewGroup;
            View view = this.mActivity.findViewById(16908332);
            if (view == null || (viewGroup = (ViewGroup)view.getParent()).getChildCount() != 2) {
                return null;
            }
            view = viewGroup.getChildAt(0);
            viewGroup = viewGroup.getChildAt(1);
            if (view.getId() == 16908332) {
                view = viewGroup;
            }
            if (view instanceof ImageView) {
                this.mHomeActionView = (ImageView)view;
            }
        }
        return this.mHomeActionView;
    }

    @Override
    public int getNavigationItemCount() {
        return this.mActionBar.getNavigationItemCount();
    }

    @Override
    public int getNavigationMode() {
        return this.mActionBar.getNavigationMode();
    }

    @Override
    public int getSelectedNavigationIndex() {
        return this.mActionBar.getSelectedNavigationIndex();
    }

    @Override
    public ActionBar.Tab getSelectedTab() {
        return (ActionBar.Tab)this.mActionBar.getSelectedTab().getTag();
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mActionBar.getSubtitle();
    }

    @Override
    public ActionBar.Tab getTabAt(int n) {
        return (ActionBar.Tab)this.mActionBar.getTabAt(n).getTag();
    }

    @Override
    public int getTabCount() {
        return this.mActionBar.getTabCount();
    }

    Drawable getThemeDefaultUpIndicator() {
        TypedArray typedArray = this.mActivity.obtainStyledAttributes(new int[]{16843531});
        Drawable drawable2 = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawable2;
    }

    @Override
    public Context getThemedContext() {
        return this.mActionBar.getThemedContext();
    }

    @Override
    public CharSequence getTitle() {
        return this.mActionBar.getTitle();
    }

    @Override
    public void hide() {
        this.mActionBar.hide();
    }

    @Override
    public boolean isShowing() {
        return this.mActionBar.isShowing();
    }

    @Override
    public ActionBar.Tab newTab() {
        ActionBar.Tab tab = this.mActionBar.newTab();
        TabWrapper tabWrapper = new TabWrapper(tab);
        tab.setTag((Object)tabWrapper);
        return tabWrapper;
    }

    @Override
    public void removeAllTabs() {
        this.mActionBar.removeAllTabs();
    }

    @Override
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener object) {
        object = this.findAndRemoveMenuVisWrapper((ActionBar.OnMenuVisibilityListener)object);
        this.mActionBar.removeOnMenuVisibilityListener((ActionBar.OnMenuVisibilityListener)object);
    }

    @Override
    public void removeTab(ActionBar.Tab tab) {
        this.mActionBar.removeTab(((TabWrapper)tab).mWrappedTab);
    }

    @Override
    public void removeTabAt(int n) {
        this.mActionBar.removeTabAt(n);
    }

    @Override
    public void selectTab(ActionBar.Tab tab) {
        this.mActionBar.selectTab(((TabWrapper)tab).mWrappedTab);
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable2) {
        this.mActionBar.setBackgroundDrawable(drawable2);
    }

    @Override
    public void setCustomView(int n) {
        this.mActionBar.setCustomView(n);
    }

    @Override
    public void setCustomView(View view) {
        this.mActionBar.setCustomView(view);
    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        ActionBar.LayoutParams layoutParams2 = new ActionBar.LayoutParams((ViewGroup.LayoutParams)layoutParams);
        layoutParams2.gravity = layoutParams.gravity;
        this.mActionBar.setCustomView(view, layoutParams2);
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean bl) {
        this.mActionBar.setDisplayHomeAsUpEnabled(bl);
    }

    @Override
    public void setDisplayOptions(int n) {
        this.mActionBar.setDisplayOptions(n);
    }

    @Override
    public void setDisplayOptions(int n, int n2) {
        this.mActionBar.setDisplayOptions(n, n2);
    }

    @Override
    public void setDisplayShowCustomEnabled(boolean bl) {
        this.mActionBar.setDisplayShowCustomEnabled(bl);
    }

    @Override
    public void setDisplayShowHomeEnabled(boolean bl) {
        this.mActionBar.setDisplayShowHomeEnabled(bl);
    }

    @Override
    public void setDisplayShowTitleEnabled(boolean bl) {
        this.mActionBar.setDisplayShowTitleEnabled(bl);
    }

    @Override
    public void setDisplayUseLogoEnabled(boolean bl) {
        this.mActionBar.setDisplayUseLogoEnabled(bl);
    }

    @Override
    public void setHomeAsUpIndicator(int n) {
        ImageView imageView;
        block3 : {
            block2 : {
                imageView = this.getHomeActionView();
                if (imageView == null) break block2;
                if (n == 0) break block3;
                imageView.setImageResource(n);
            }
            return;
        }
        imageView.setImageDrawable(this.getThemeDefaultUpIndicator());
    }

    @Override
    public void setHomeAsUpIndicator(Drawable drawable2) {
        ImageView imageView = this.getHomeActionView();
        if (imageView != null) {
            Drawable drawable3 = drawable2;
            if (drawable2 == null) {
                drawable3 = this.getThemeDefaultUpIndicator();
            }
            imageView.setImageDrawable(drawable3);
        }
    }

    @Override
    public void setHomeButtonEnabled(boolean bl) {
        this.mActionBar.setHomeButtonEnabled(bl);
    }

    @Override
    public void setIcon(int n) {
        this.mActionBar.setIcon(n);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.mActionBar.setIcon(drawable2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, ActionBar.OnNavigationListener object) {
        android.app.ActionBar actionBar = this.mActionBar;
        object = object != null ? new OnNavigationListenerWrapper((ActionBar.OnNavigationListener)object) : null;
        actionBar.setListNavigationCallbacks(spinnerAdapter, (ActionBar.OnNavigationListener)object);
    }

    @Override
    public void setLogo(int n) {
        this.mActionBar.setLogo(n);
    }

    @Override
    public void setLogo(Drawable drawable2) {
        this.mActionBar.setLogo(drawable2);
    }

    @Override
    public void setNavigationMode(int n) {
        this.mActionBar.setNavigationMode(n);
    }

    @Override
    public void setSelectedNavigationItem(int n) {
        this.mActionBar.setSelectedNavigationItem(n);
    }

    @Override
    public void setSplitBackgroundDrawable(Drawable drawable2) {
        this.mActionBar.setSplitBackgroundDrawable(drawable2);
    }

    @Override
    public void setStackedBackgroundDrawable(Drawable drawable2) {
        this.mActionBar.setStackedBackgroundDrawable(drawable2);
    }

    @Override
    public void setSubtitle(int n) {
        this.mActionBar.setSubtitle(n);
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mActionBar.setSubtitle(charSequence);
    }

    @Override
    public void setTitle(int n) {
        this.mActionBar.setTitle(n);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mActionBar.setTitle(charSequence);
    }

    @Override
    public void show() {
        this.mActionBar.show();
    }

    static class OnMenuVisibilityListenerWrapper
    implements ActionBar.OnMenuVisibilityListener {
        final ActionBar.OnMenuVisibilityListener mWrappedListener;

        public OnMenuVisibilityListenerWrapper(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
            this.mWrappedListener = onMenuVisibilityListener;
        }

        public void onMenuVisibilityChanged(boolean bl) {
            this.mWrappedListener.onMenuVisibilityChanged(bl);
        }
    }

    static class OnNavigationListenerWrapper
    implements ActionBar.OnNavigationListener {
        private final ActionBar.OnNavigationListener mWrappedListener;

        public OnNavigationListenerWrapper(ActionBar.OnNavigationListener onNavigationListener) {
            this.mWrappedListener = onNavigationListener;
        }

        public boolean onNavigationItemSelected(int n, long l) {
            return this.mWrappedListener.onNavigationItemSelected(n, l);
        }
    }

    class TabWrapper
    extends ActionBar.Tab
    implements ActionBar.TabListener {
        private CharSequence mContentDescription;
        private ActionBar.TabListener mTabListener;
        private Object mTag;
        final ActionBar.Tab mWrappedTab;

        public TabWrapper(ActionBar.Tab tab) {
            this.mWrappedTab = tab;
        }

        @Override
        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }

        @Override
        public View getCustomView() {
            return this.mWrappedTab.getCustomView();
        }

        @Override
        public Drawable getIcon() {
            return this.mWrappedTab.getIcon();
        }

        @Override
        public int getPosition() {
            return this.mWrappedTab.getPosition();
        }

        @Override
        public Object getTag() {
            return this.mTag;
        }

        @Override
        public CharSequence getText() {
            return this.mWrappedTab.getText();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onTabReselected(ActionBar.Tab object, android.app.FragmentTransaction fragmentTransaction) {
            ActionBar.TabListener tabListener = this.mTabListener;
            object = fragmentTransaction != null ? ActionBarImplICS.this.getActiveTransaction() : null;
            tabListener.onTabReselected(this, (FragmentTransaction)object);
            ActionBarImplICS.this.commitActiveTransaction();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onTabSelected(ActionBar.Tab object, android.app.FragmentTransaction fragmentTransaction) {
            ActionBar.TabListener tabListener = this.mTabListener;
            object = fragmentTransaction != null ? ActionBarImplICS.this.getActiveTransaction() : null;
            tabListener.onTabSelected(this, (FragmentTransaction)object);
            ActionBarImplICS.this.commitActiveTransaction();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onTabUnselected(ActionBar.Tab object, android.app.FragmentTransaction fragmentTransaction) {
            ActionBar.TabListener tabListener = this.mTabListener;
            object = fragmentTransaction != null ? ActionBarImplICS.this.getActiveTransaction() : null;
            tabListener.onTabUnselected(this, (FragmentTransaction)object);
        }

        @Override
        public void select() {
            this.mWrappedTab.select();
        }

        @Override
        public ActionBar.Tab setContentDescription(int n) {
            this.mContentDescription = ActionBarImplICS.this.mActivity.getText(n);
            return this;
        }

        @Override
        public ActionBar.Tab setContentDescription(CharSequence charSequence) {
            this.mContentDescription = charSequence;
            return this;
        }

        @Override
        public ActionBar.Tab setCustomView(int n) {
            this.mWrappedTab.setCustomView(n);
            return this;
        }

        @Override
        public ActionBar.Tab setCustomView(View view) {
            this.mWrappedTab.setCustomView(view);
            return this;
        }

        @Override
        public ActionBar.Tab setIcon(int n) {
            this.mWrappedTab.setIcon(n);
            return this;
        }

        @Override
        public ActionBar.Tab setIcon(Drawable drawable2) {
            this.mWrappedTab.setIcon(drawable2);
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ActionBar.Tab setTabListener(ActionBar.TabListener object) {
            this.mTabListener = object;
            ActionBar.Tab tab = this.mWrappedTab;
            object = object != null ? this : null;
            tab.setTabListener((ActionBar.TabListener)object);
            return this;
        }

        @Override
        public ActionBar.Tab setTag(Object object) {
            this.mTag = object;
            return this;
        }

        @Override
        public ActionBar.Tab setText(int n) {
            this.mWrappedTab.setText(n);
            return this;
        }

        @Override
        public ActionBar.Tab setText(CharSequence charSequence) {
            this.mWrappedTab.setText(charSequence);
            return this;
        }
    }

}

