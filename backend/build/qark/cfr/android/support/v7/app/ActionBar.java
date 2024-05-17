/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.SpinnerAdapter
 *  java.lang.CharSequence
 *  java.lang.Object
 */
package android.support.v7.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

public abstract class ActionBar {
    public static final int DISPLAY_HOME_AS_UP = 4;
    public static final int DISPLAY_SHOW_CUSTOM = 16;
    public static final int DISPLAY_SHOW_HOME = 2;
    public static final int DISPLAY_SHOW_TITLE = 8;
    public static final int DISPLAY_USE_LOGO = 1;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_TABS = 2;

    public abstract void addOnMenuVisibilityListener(OnMenuVisibilityListener var1);

    public abstract void addTab(Tab var1);

    public abstract void addTab(Tab var1, int var2);

    public abstract void addTab(Tab var1, int var2, boolean var3);

    public abstract void addTab(Tab var1, boolean var2);

    public abstract View getCustomView();

    public abstract int getDisplayOptions();

    public abstract int getHeight();

    public abstract int getNavigationItemCount();

    public abstract int getNavigationMode();

    public abstract int getSelectedNavigationIndex();

    public abstract Tab getSelectedTab();

    public abstract CharSequence getSubtitle();

    public abstract Tab getTabAt(int var1);

    public abstract int getTabCount();

    public Context getThemedContext() {
        return null;
    }

    public abstract CharSequence getTitle();

    public abstract void hide();

    public abstract boolean isShowing();

    public abstract Tab newTab();

    public abstract void removeAllTabs();

    public abstract void removeOnMenuVisibilityListener(OnMenuVisibilityListener var1);

    public abstract void removeTab(Tab var1);

    public abstract void removeTabAt(int var1);

    public abstract void selectTab(Tab var1);

    public abstract void setBackgroundDrawable(Drawable var1);

    public abstract void setCustomView(int var1);

    public abstract void setCustomView(View var1);

    public abstract void setCustomView(View var1, LayoutParams var2);

    public abstract void setDisplayHomeAsUpEnabled(boolean var1);

    public abstract void setDisplayOptions(int var1);

    public abstract void setDisplayOptions(int var1, int var2);

    public abstract void setDisplayShowCustomEnabled(boolean var1);

    public abstract void setDisplayShowHomeEnabled(boolean var1);

    public abstract void setDisplayShowTitleEnabled(boolean var1);

    public abstract void setDisplayUseLogoEnabled(boolean var1);

    public void setHomeActionContentDescription(int n) {
    }

    public void setHomeActionContentDescription(CharSequence charSequence) {
    }

    public void setHomeAsUpIndicator(int n) {
    }

    public void setHomeAsUpIndicator(Drawable drawable2) {
    }

    public void setHomeButtonEnabled(boolean bl) {
    }

    public abstract void setIcon(int var1);

    public abstract void setIcon(Drawable var1);

    public abstract void setListNavigationCallbacks(SpinnerAdapter var1, OnNavigationListener var2);

    public abstract void setLogo(int var1);

    public abstract void setLogo(Drawable var1);

    public abstract void setNavigationMode(int var1);

    public abstract void setSelectedNavigationItem(int var1);

    public void setSplitBackgroundDrawable(Drawable drawable2) {
    }

    public void setStackedBackgroundDrawable(Drawable drawable2) {
    }

    public abstract void setSubtitle(int var1);

    public abstract void setSubtitle(CharSequence var1);

    public abstract void setTitle(int var1);

    public abstract void setTitle(CharSequence var1);

    public abstract void show();

    static interface Callback {
        public FragmentManager getSupportFragmentManager();
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public int gravity = -1;

        public LayoutParams(int n) {
            this(-2, -1, n);
        }

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.gravity = 19;
        }

        public LayoutParams(int n, int n2, int n3) {
            super(n, n2);
            this.gravity = n3;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBarLayout);
            this.gravity = context.getInt(0, -1);
            context.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public static interface OnMenuVisibilityListener {
        public void onMenuVisibilityChanged(boolean var1);
    }

    public static interface OnNavigationListener {
        public boolean onNavigationItemSelected(int var1, long var2);
    }

    public static abstract class Tab {
        public static final int INVALID_POSITION = -1;

        public abstract CharSequence getContentDescription();

        public abstract View getCustomView();

        public abstract Drawable getIcon();

        public abstract int getPosition();

        public abstract Object getTag();

        public abstract CharSequence getText();

        public abstract void select();

        public abstract Tab setContentDescription(int var1);

        public abstract Tab setContentDescription(CharSequence var1);

        public abstract Tab setCustomView(int var1);

        public abstract Tab setCustomView(View var1);

        public abstract Tab setIcon(int var1);

        public abstract Tab setIcon(Drawable var1);

        public abstract Tab setTabListener(TabListener var1);

        public abstract Tab setTag(Object var1);

        public abstract Tab setText(int var1);

        public abstract Tab setText(CharSequence var1);
    }

    public static interface TabListener {
        public void onTabReselected(Tab var1, FragmentTransaction var2);

        public void onTabSelected(Tab var1, FragmentTransaction var2);

        public void onTabUnselected(Tab var1, FragmentTransaction var2);
    }

}

