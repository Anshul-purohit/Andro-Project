// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.app.Activity;
import android.support.v7.internal.view.ActionModeWrapperJB;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.view.ActionMode;
import android.content.Context;

class ActionBarActivityDelegateJB extends ActionBarActivityDelegateICS
{
    ActionBarActivityDelegateJB(final ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }
    
    @Override
    ActionModeWrapper.CallbackWrapper createActionModeCallbackWrapper(final Context context, final ActionMode.Callback callback) {
        return new ActionModeWrapperJB.CallbackWrapper(context, callback);
    }
    
    @Override
    ActionModeWrapper createActionModeWrapper(final Context context, final android.view.ActionMode actionMode) {
        return new ActionModeWrapperJB(context, actionMode);
    }
    
    @Override
    public ActionBar createSupportActionBar() {
        return new ActionBarImplJB(this.mActivity, this.mActivity);
    }
}
