/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.view.ActionMode
 */
package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivityDelegateICS;
import android.support.v7.app.ActionBarImplJB;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.internal.view.ActionModeWrapperJB;
import android.support.v7.view.ActionMode;
import android.view.ActionMode;

class ActionBarActivityDelegateJB
extends ActionBarActivityDelegateICS {
    ActionBarActivityDelegateJB(ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }

    @Override
    ActionModeWrapper.CallbackWrapper createActionModeCallbackWrapper(Context context, ActionMode.Callback callback) {
        return new ActionModeWrapperJB.CallbackWrapper(context, callback);
    }

    @Override
    ActionModeWrapper createActionModeWrapper(Context context, ActionMode actionMode) {
        return new ActionModeWrapperJB(context, actionMode);
    }

    @Override
    public ActionBar createSupportActionBar() {
        return new ActionBarImplJB(this.mActivity, this.mActivity);
    }
}

