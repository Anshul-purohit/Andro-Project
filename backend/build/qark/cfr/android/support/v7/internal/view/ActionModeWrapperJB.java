/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.ActionMode
 */
package android.support.v7.internal.view;

import android.content.Context;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.view.ActionMode;
import android.view.ActionMode;

public class ActionModeWrapperJB
extends ActionModeWrapper {
    public ActionModeWrapperJB(Context context, ActionMode actionMode) {
        super(context, actionMode);
    }

    @Override
    public boolean getTitleOptionalHint() {
        return this.mWrappedObject.getTitleOptionalHint();
    }

    @Override
    public boolean isTitleOptional() {
        return this.mWrappedObject.isTitleOptional();
    }

    @Override
    public void setTitleOptionalHint(boolean bl) {
        this.mWrappedObject.setTitleOptionalHint(bl);
    }

    public static class CallbackWrapper
    extends ActionModeWrapper.CallbackWrapper {
        public CallbackWrapper(Context context, ActionMode.Callback callback) {
            super(context, callback);
        }

        @Override
        protected ActionModeWrapper createActionModeWrapper(Context context, ActionMode actionMode) {
            return new ActionModeWrapperJB(context, actionMode);
        }
    }

}

