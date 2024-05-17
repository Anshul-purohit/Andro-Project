/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Configuration
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Log
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package android.support.v7.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivityDelegateBase;
import android.support.v7.app.ActionBarActivityDelegateHC;
import android.support.v7.app.ActionBarActivityDelegateICS;
import android.support.v7.app.ActionBarActivityDelegateJB;
import android.support.v7.app.ActionBarActivityDelegateJBMR2;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

abstract class ActionBarActivityDelegate {
    static final String METADATA_UI_OPTIONS = "android.support.UI_OPTIONS";
    private static final String TAG = "ActionBarActivityDelegate";
    static final String UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW = "splitActionBarWhenNarrow";
    private ActionBar mActionBar;
    final ActionBarActivity mActivity;
    private boolean mEnableDefaultActionBarUp;
    boolean mHasActionBar;
    private MenuInflater mMenuInflater;
    boolean mOverlayActionBar;

    ActionBarActivityDelegate(ActionBarActivity actionBarActivity) {
        this.mActivity = actionBarActivity;
    }

    static ActionBarActivityDelegate createDelegate(ActionBarActivity actionBarActivity) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ActionBarActivityDelegateJBMR2(actionBarActivity);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActionBarActivityDelegateJB(actionBarActivity);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return new ActionBarActivityDelegateICS(actionBarActivity);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            return new ActionBarActivityDelegateHC(actionBarActivity);
        }
        return new ActionBarActivityDelegateBase(actionBarActivity);
    }

    abstract void addContentView(View var1, ViewGroup.LayoutParams var2);

    abstract ActionBar createSupportActionBar();

    protected final Context getActionBarThemedContext() {
        ActionBarActivity actionBarActivity = this.mActivity;
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBarActivity = actionBar.getThemedContext();
        }
        return actionBarActivity;
    }

    final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }

    abstract int getHomeAsUpIndicatorAttrId();

    MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(this.getActionBarThemedContext());
        }
        return this.mMenuInflater;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    final ActionBar getSupportActionBar() {
        if (this.mHasActionBar || this.mOverlayActionBar) {
            if (this.mActionBar == null) {
                this.mActionBar = this.createSupportActionBar();
                if (this.mEnableDefaultActionBarUp) {
                    this.mActionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
            do {
                return this.mActionBar;
                break;
            } while (true);
        }
        this.mActionBar = null;
        return this.mActionBar;
    }

    protected final String getUiOptionsFromMetadata() {
        String string2;
        try {
            ActivityInfo activityInfo = this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), 128);
            string2 = null;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e((String)"ActionBarActivityDelegate", (String)("getUiOptionsFromMetadata: Activity '" + this.mActivity.getClass().getSimpleName() + "' not in manifest"));
            return null;
        }
        if (activityInfo.metaData != null) {
            string2 = activityInfo.metaData.getString("android.support.UI_OPTIONS");
        }
        return string2;
    }

    abstract boolean onBackPressed();

    abstract void onConfigurationChanged(Configuration var1);

    abstract void onContentChanged();

    void onCreate(Bundle bundle) {
        block5 : {
            block4 : {
                bundle = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
                if (!bundle.hasValue(0)) {
                    bundle.recycle();
                    throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
                }
                this.mHasActionBar = bundle.getBoolean(0, false);
                this.mOverlayActionBar = bundle.getBoolean(1, false);
                bundle.recycle();
                if (NavUtils.getParentActivityName(this.mActivity) == null) break block4;
                if (this.mActionBar != null) break block5;
                this.mEnableDefaultActionBarUp = true;
            }
            return;
        }
        this.mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    abstract boolean onCreatePanelMenu(int var1, Menu var2);

    abstract View onCreatePanelView(int var1);

    abstract boolean onMenuItemSelected(int var1, MenuItem var2);

    abstract void onPostResume();

    boolean onPrepareOptionsPanel(View view, Menu menu2) {
        if (Build.VERSION.SDK_INT < 16) {
            return this.mActivity.onPrepareOptionsMenu(menu2);
        }
        return this.mActivity.superOnPrepareOptionsPanel(view, menu2);
    }

    abstract boolean onPreparePanel(int var1, View var2, Menu var3);

    abstract void onStop();

    abstract void onTitleChanged(CharSequence var1);

    abstract void setContentView(int var1);

    abstract void setContentView(View var1);

    abstract void setContentView(View var1, ViewGroup.LayoutParams var2);

    abstract void setSupportProgress(int var1);

    abstract void setSupportProgressBarIndeterminate(boolean var1);

    abstract void setSupportProgressBarIndeterminateVisibility(boolean var1);

    abstract void setSupportProgressBarVisibility(boolean var1);

    abstract ActionMode startSupportActionMode(ActionMode.Callback var1);

    abstract void supportInvalidateOptionsMenu();

    abstract boolean supportRequestWindowFeature(int var1);

    private class ActionBarDrawableToggleImpl
    implements ActionBarDrawerToggle.Delegate {
        private ActionBarDrawableToggleImpl() {
        }

        @Override
        public Drawable getThemeUpIndicator() {
            TypedArray typedArray = ActionBarActivityDelegate.this.mActivity.obtainStyledAttributes(new int[]{ActionBarActivityDelegate.this.getHomeAsUpIndicatorAttrId()});
            Drawable drawable2 = typedArray.getDrawable(0);
            typedArray.recycle();
            return drawable2;
        }

        @Override
        public void setActionBarDescription(int n) {
            ActionBar actionBar = ActionBarActivityDelegate.this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(n);
            }
        }

        @Override
        public void setActionBarUpIndicator(Drawable drawable2, int n) {
            ActionBar actionBar = ActionBarActivityDelegate.this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(drawable2);
                actionBar.setHomeActionContentDescription(n);
            }
        }
    }

}

