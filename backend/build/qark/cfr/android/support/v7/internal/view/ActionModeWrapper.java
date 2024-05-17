/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  java.lang.CharSequence
 *  java.lang.Object
 */
package android.support.v7.internal.view;

import android.content.Context;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.support.v7.view.ActionMode;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class ActionModeWrapper
extends android.support.v7.view.ActionMode {
    final MenuInflater mInflater;
    final ActionMode mWrappedObject;

    public ActionModeWrapper(Context context, ActionMode actionMode) {
        this.mWrappedObject = actionMode;
        this.mInflater = new SupportMenuInflater(context);
    }

    @Override
    public void finish() {
        this.mWrappedObject.finish();
    }

    @Override
    public View getCustomView() {
        return this.mWrappedObject.getCustomView();
    }

    @Override
    public Menu getMenu() {
        return MenuWrapperFactory.createMenuWrapper(this.mWrappedObject.getMenu());
    }

    @Override
    public MenuInflater getMenuInflater() {
        return this.mInflater;
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mWrappedObject.getSubtitle();
    }

    @Override
    public Object getTag() {
        return this.mWrappedObject.getTag();
    }

    @Override
    public CharSequence getTitle() {
        return this.mWrappedObject.getTitle();
    }

    @Override
    public void invalidate() {
        this.mWrappedObject.invalidate();
    }

    @Override
    public void setCustomView(View view) {
        this.mWrappedObject.setCustomView(view);
    }

    @Override
    public void setSubtitle(int n) {
        this.mWrappedObject.setSubtitle(n);
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mWrappedObject.setSubtitle(charSequence);
    }

    @Override
    public void setTag(Object object) {
        this.mWrappedObject.setTag(object);
    }

    @Override
    public void setTitle(int n) {
        this.mWrappedObject.setTitle(n);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mWrappedObject.setTitle(charSequence);
    }

    public static class CallbackWrapper
    implements ActionMode.Callback {
        final Context mContext;
        private ActionModeWrapper mLastStartedActionMode;
        final ActionMode.Callback mWrappedCallback;

        public CallbackWrapper(Context context, ActionMode.Callback callback) {
            this.mContext = context;
            this.mWrappedCallback = callback;
        }

        private android.support.v7.view.ActionMode getActionModeWrapper(ActionMode actionMode) {
            if (this.mLastStartedActionMode != null && this.mLastStartedActionMode.mWrappedObject == actionMode) {
                return this.mLastStartedActionMode;
            }
            return this.createActionModeWrapper(this.mContext, actionMode);
        }

        protected ActionModeWrapper createActionModeWrapper(Context context, ActionMode actionMode) {
            return new ActionModeWrapper(context, actionMode);
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrappedCallback.onActionItemClicked(this.getActionModeWrapper(actionMode), MenuWrapperFactory.createMenuItemWrapper(menuItem));
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu2) {
            return this.mWrappedCallback.onCreateActionMode(this.getActionModeWrapper(actionMode), MenuWrapperFactory.createMenuWrapper(menu2));
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrappedCallback.onDestroyActionMode(this.getActionModeWrapper(actionMode));
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu2) {
            return this.mWrappedCallback.onPrepareActionMode(this.getActionModeWrapper(actionMode), MenuWrapperFactory.createMenuWrapper(menu2));
        }

        public void setLastStartedActionMode(ActionModeWrapper actionModeWrapper) {
            this.mLastStartedActionMode = actionModeWrapper;
        }
    }

}

