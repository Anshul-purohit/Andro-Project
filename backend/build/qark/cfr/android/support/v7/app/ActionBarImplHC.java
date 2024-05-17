/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  java.lang.Object
 */
package android.support.v7.app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarImplBase;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.NativeActionModeAwareLayout;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

class ActionBarImplHC
extends ActionBarImplBase
implements NativeActionModeAwareLayout.OnActionModeForChildListener {
    private ActionMode mCurActionMode;
    final NativeActionModeAwareLayout mNativeActionModeAwareLayout;

    public ActionBarImplHC(ActionBarActivity actionBarActivity, ActionBar.Callback callback) {
        super(actionBarActivity, callback);
        this.mNativeActionModeAwareLayout = (NativeActionModeAwareLayout)actionBarActivity.findViewById(R.id.action_bar_root);
        if (this.mNativeActionModeAwareLayout != null) {
            this.mNativeActionModeAwareLayout.setActionModeForChildListener(this);
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (this.mCurActionMode != null) {
            this.mCurActionMode.finish();
        }
    }

    @Override
    boolean isShowHideAnimationEnabled() {
        if (this.mCurActionMode == null && super.isShowHideAnimationEnabled()) {
            return true;
        }
        return false;
    }

    @Override
    public ActionMode.Callback onActionModeForChild(ActionMode.Callback callback) {
        return new CallbackWrapper(callback);
    }

    @Override
    public void show() {
        super.show();
        if (this.mCurActionMode != null) {
            this.mCurActionMode.finish();
        }
    }

    private class CallbackWrapper
    implements ActionMode.Callback {
        private final ActionMode.Callback mWrappedCallback;

        CallbackWrapper(ActionMode.Callback callback) {
            this.mWrappedCallback = callback;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrappedCallback.onActionItemClicked(actionMode, menuItem);
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu2) {
            boolean bl = this.mWrappedCallback.onCreateActionMode(actionMode, menu2);
            if (bl) {
                ActionBarImplHC.this.mCurActionMode = actionMode;
                ActionBarImplHC.this.showForActionMode();
            }
            return bl;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrappedCallback.onDestroyActionMode(actionMode);
            ActionBarImplHC.this.hideForActionMode();
            ActionBarImplHC.this.mCurActionMode = null;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu2) {
            return this.mWrappedCallback.onPrepareActionMode(actionMode, menu2);
        }
    }

}

