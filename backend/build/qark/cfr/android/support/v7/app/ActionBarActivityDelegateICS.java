/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.KeyEvent
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.view.Window$Callback
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivityDelegate;
import android.support.v7.app.ActionBarImplICS;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.support.v7.view.ActionMode;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

class ActionBarActivityDelegateICS
extends ActionBarActivityDelegate {
    Menu mMenu;

    ActionBarActivityDelegateICS(ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mActivity.superAddContentView(view, layoutParams);
    }

    ActionModeWrapper.CallbackWrapper createActionModeCallbackWrapper(Context context, ActionMode.Callback callback) {
        return new ActionModeWrapper.CallbackWrapper(context, callback);
    }

    ActionModeWrapper createActionModeWrapper(Context context, android.view.ActionMode actionMode) {
        return new ActionModeWrapper(context, actionMode);
    }

    @Override
    public ActionBar createSupportActionBar() {
        return new ActionBarImplICS(this.mActivity, this.mActivity);
    }

    Window.Callback createWindowCallbackWrapper(Window.Callback callback) {
        return new WindowCallbackWrapper(callback);
    }

    @Override
    int getHomeAsUpIndicatorAttrId() {
        return 16843531;
    }

    public void onActionModeFinished(android.view.ActionMode actionMode) {
        this.mActivity.onSupportActionModeFinished(this.createActionModeWrapper(this.getActionBarThemedContext(), actionMode));
    }

    public void onActionModeStarted(android.view.ActionMode actionMode) {
        this.mActivity.onSupportActionModeStarted(this.createActionModeWrapper(this.getActionBarThemedContext(), actionMode));
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onContentChanged() {
        this.mActivity.onSupportContentChanged();
    }

    @Override
    public void onCreate(Bundle bundle) {
        if ("splitActionBarWhenNarrow".equals((Object)this.getUiOptionsFromMetadata())) {
            this.mActivity.getWindow().setUiOptions(1, 1);
        }
        super.onCreate(bundle);
        if (this.mHasActionBar) {
            this.mActivity.requestWindowFeature(8);
        }
        if (this.mOverlayActionBar) {
            this.mActivity.requestWindowFeature(9);
        }
        bundle = this.mActivity.getWindow();
        bundle.setCallback(this.createWindowCallbackWrapper(bundle.getCallback()));
    }

    @Override
    public boolean onCreatePanelMenu(int n, Menu menu2) {
        if (n == 0 || n == 8) {
            if (this.mMenu == null) {
                this.mMenu = MenuWrapperFactory.createMenuWrapper(menu2);
            }
            return this.mActivity.superOnCreatePanelMenu(n, this.mMenu);
        }
        return this.mActivity.superOnCreatePanelMenu(n, menu2);
    }

    @Override
    public View onCreatePanelView(int n) {
        return null;
    }

    @Override
    public boolean onMenuItemSelected(int n, MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (n == 0) {
            menuItem2 = MenuWrapperFactory.createMenuItemWrapper(menuItem);
        }
        return this.mActivity.superOnMenuItemSelected(n, menuItem2);
    }

    @Override
    public void onPostResume() {
    }

    @Override
    public boolean onPreparePanel(int n, View view, Menu menu2) {
        if (n == 0 || n == 8) {
            return this.mActivity.superOnPreparePanel(n, view, this.mMenu);
        }
        return this.mActivity.superOnPreparePanel(n, view, menu2);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onTitleChanged(CharSequence charSequence) {
    }

    @Override
    public void setContentView(int n) {
        this.mActivity.superSetContentView(n);
    }

    @Override
    public void setContentView(View view) {
        this.mActivity.superSetContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mActivity.superSetContentView(view, layoutParams);
    }

    @Override
    void setSupportProgress(int n) {
        this.mActivity.setProgress(n);
    }

    @Override
    void setSupportProgressBarIndeterminate(boolean bl) {
        this.mActivity.setProgressBarIndeterminate(bl);
    }

    @Override
    void setSupportProgressBarIndeterminateVisibility(boolean bl) {
        this.mActivity.setProgressBarIndeterminateVisibility(bl);
    }

    @Override
    void setSupportProgressBarVisibility(boolean bl) {
        this.mActivity.setProgressBarVisibility(bl);
    }

    @Override
    public ActionMode startSupportActionMode(ActionMode.Callback object) {
        if (object == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        Context context = this.getActionBarThemedContext();
        ActionModeWrapper.CallbackWrapper callbackWrapper = this.createActionModeCallbackWrapper(context, (ActionMode.Callback)object);
        object = null;
        android.view.ActionMode actionMode = this.mActivity.startActionMode((ActionMode.Callback)callbackWrapper);
        if (actionMode != null) {
            object = this.createActionModeWrapper(context, actionMode);
            callbackWrapper.setLastStartedActionMode((ActionModeWrapper)object);
        }
        return object;
    }

    @Override
    public void supportInvalidateOptionsMenu() {
        this.mMenu = null;
    }

    @Override
    public boolean supportRequestWindowFeature(int n) {
        return this.mActivity.requestWindowFeature(n);
    }

    class WindowCallbackWrapper
    implements Window.Callback {
        final Window.Callback mWrapped;

        public WindowCallbackWrapper(Window.Callback callback) {
            this.mWrapped = callback;
        }

        public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
            return this.mWrapped.dispatchGenericMotionEvent(motionEvent);
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return this.mWrapped.dispatchKeyEvent(keyEvent);
        }

        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return this.mWrapped.dispatchKeyShortcutEvent(keyEvent);
        }

        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            return this.mWrapped.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return this.mWrapped.dispatchTouchEvent(motionEvent);
        }

        public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
            return this.mWrapped.dispatchTrackballEvent(motionEvent);
        }

        public void onActionModeFinished(android.view.ActionMode actionMode) {
            this.mWrapped.onActionModeFinished(actionMode);
            ActionBarActivityDelegateICS.this.onActionModeFinished(actionMode);
        }

        public void onActionModeStarted(android.view.ActionMode actionMode) {
            this.mWrapped.onActionModeStarted(actionMode);
            ActionBarActivityDelegateICS.this.onActionModeStarted(actionMode);
        }

        public void onAttachedToWindow() {
            this.mWrapped.onAttachedToWindow();
        }

        public void onContentChanged() {
            this.mWrapped.onContentChanged();
        }

        public boolean onCreatePanelMenu(int n, Menu menu2) {
            return this.mWrapped.onCreatePanelMenu(n, menu2);
        }

        public View onCreatePanelView(int n) {
            return this.mWrapped.onCreatePanelView(n);
        }

        public void onDetachedFromWindow() {
            this.mWrapped.onDetachedFromWindow();
        }

        public boolean onMenuItemSelected(int n, MenuItem menuItem) {
            return this.mWrapped.onMenuItemSelected(n, menuItem);
        }

        public boolean onMenuOpened(int n, Menu menu2) {
            return this.mWrapped.onMenuOpened(n, menu2);
        }

        public void onPanelClosed(int n, Menu menu2) {
            this.mWrapped.onPanelClosed(n, menu2);
        }

        public boolean onPreparePanel(int n, View view, Menu menu2) {
            return this.mWrapped.onPreparePanel(n, view, menu2);
        }

        public boolean onSearchRequested() {
            return this.mWrapped.onSearchRequested();
        }

        public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
            this.mWrapped.onWindowAttributesChanged(layoutParams);
        }

        public void onWindowFocusChanged(boolean bl) {
            this.mWrapped.onWindowFocusChanged(bl);
        }

        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return this.mWrapped.onWindowStartingActionMode(callback);
        }
    }

}

