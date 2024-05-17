/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  java.lang.CharSequence
 *  java.lang.IllegalStateException
 *  java.lang.Object
 */
package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivityDelegate;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ActionBarActivity
extends FragmentActivity
implements ActionBar.Callback,
TaskStackBuilder.SupportParentable,
ActionBarDrawerToggle.DelegateProvider {
    ActionBarActivityDelegate mImpl;

    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mImpl.addContentView(view, layoutParams);
    }

    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return this.mImpl.getDrawerToggleDelegate();
    }

    public MenuInflater getMenuInflater() {
        return this.mImpl.getMenuInflater();
    }

    public ActionBar getSupportActionBar() {
        return this.mImpl.getSupportActionBar();
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        return NavUtils.getParentActivityIntent(this);
    }

    @Override
    public void onBackPressed() {
        if (!this.mImpl.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mImpl.onConfigurationChanged(configuration);
    }

    public final void onContentChanged() {
        this.mImpl.onContentChanged();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        this.mImpl = ActionBarActivityDelegate.createDelegate(this);
        super.onCreate(bundle);
        this.mImpl.onCreate(bundle);
    }

    @Override
    public boolean onCreatePanelMenu(int n, Menu menu2) {
        return this.mImpl.onCreatePanelMenu(n, menu2);
    }

    public View onCreatePanelView(int n) {
        if (n == 0) {
            return this.mImpl.onCreatePanelView(n);
        }
        return super.onCreatePanelView(n);
    }

    public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder taskStackBuilder) {
        taskStackBuilder.addParentStack(this);
    }

    @Override
    public final boolean onMenuItemSelected(int n, MenuItem menuItem) {
        if (this.mImpl.onMenuItemSelected(n, menuItem)) {
            return true;
        }
        ActionBar actionBar = this.getSupportActionBar();
        if (menuItem.getItemId() == 16908332 && actionBar != null && (actionBar.getDisplayOptions() & 4) != 0) {
            return this.onSupportNavigateUp();
        }
        return false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.mImpl.onPostResume();
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu2) {
        return this.mImpl.onPrepareOptionsPanel(view, menu2);
    }

    @Override
    public boolean onPreparePanel(int n, View view, Menu menu2) {
        return this.mImpl.onPreparePanel(n, view, menu2);
    }

    public void onPrepareSupportNavigateUpTaskStack(TaskStackBuilder taskStackBuilder) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mImpl.onStop();
    }

    public void onSupportActionModeFinished(ActionMode actionMode) {
    }

    public void onSupportActionModeStarted(ActionMode actionMode) {
    }

    public void onSupportContentChanged() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onSupportNavigateUp() {
        Object object = this.getSupportParentActivityIntent();
        if (object == null) return false;
        if (this.supportShouldUpRecreateTask((Intent)object)) {
            object = TaskStackBuilder.create((Context)this);
            this.onCreateSupportNavigateUpTaskStack((TaskStackBuilder)object);
            this.onPrepareSupportNavigateUpTaskStack((TaskStackBuilder)object);
            object.startActivities();
            try {
                ActivityCompat.finishAffinity(this);
            }
            catch (IllegalStateException illegalStateException) {
                this.finish();
                return true;
            }
            do {
                return true;
                break;
            } while (true);
        }
        this.supportNavigateUpTo((Intent)object);
        return true;
    }

    protected void onTitleChanged(CharSequence charSequence, int n) {
        super.onTitleChanged(charSequence, n);
        this.mImpl.onTitleChanged(charSequence);
    }

    public void setContentView(int n) {
        this.mImpl.setContentView(n);
    }

    public void setContentView(View view) {
        this.mImpl.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mImpl.setContentView(view, layoutParams);
    }

    public void setSupportProgress(int n) {
        this.mImpl.setSupportProgress(n);
    }

    public void setSupportProgressBarIndeterminate(boolean bl) {
        this.mImpl.setSupportProgressBarIndeterminate(bl);
    }

    public void setSupportProgressBarIndeterminateVisibility(boolean bl) {
        this.mImpl.setSupportProgressBarIndeterminateVisibility(bl);
    }

    public void setSupportProgressBarVisibility(boolean bl) {
        this.mImpl.setSupportProgressBarVisibility(bl);
    }

    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        return this.mImpl.startSupportActionMode(callback);
    }

    void superAddContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.addContentView(view, layoutParams);
    }

    boolean superOnCreatePanelMenu(int n, Menu menu2) {
        return super.onCreatePanelMenu(n, menu2);
    }

    boolean superOnMenuItemSelected(int n, MenuItem menuItem) {
        return super.onMenuItemSelected(n, menuItem);
    }

    boolean superOnPrepareOptionsPanel(View view, Menu menu2) {
        return super.onPrepareOptionsPanel(view, menu2);
    }

    boolean superOnPreparePanel(int n, View view, Menu menu2) {
        return super.onPreparePanel(n, view, menu2);
    }

    void superSetContentView(int n) {
        super.setContentView(n);
    }

    void superSetContentView(View view) {
        super.setContentView(view);
    }

    void superSetContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(view, layoutParams);
    }

    @Override
    public void supportInvalidateOptionsMenu() {
        if (Build.VERSION.SDK_INT >= 14) {
            super.supportInvalidateOptionsMenu();
        }
        this.mImpl.supportInvalidateOptionsMenu();
    }

    public void supportNavigateUpTo(Intent intent) {
        NavUtils.navigateUpTo(this, intent);
    }

    public boolean supportRequestWindowFeature(int n) {
        return this.mImpl.supportRequestWindowFeature(n);
    }

    public boolean supportShouldUpRecreateTask(Intent intent) {
        return NavUtils.shouldUpRecreateTask(this, intent);
    }
}

