// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.internal.view;

import android.view.ActionMode;
import android.content.Context;

public class ActionModeWrapperJB extends ActionModeWrapper
{
    public ActionModeWrapperJB(final Context context, final android.view.ActionMode actionMode) {
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
    public void setTitleOptionalHint(final boolean titleOptionalHint) {
        this.mWrappedObject.setTitleOptionalHint(titleOptionalHint);
    }
    
    public static class CallbackWrapper extends ActionModeWrapper.CallbackWrapper
    {
        public CallbackWrapper(final Context context, final Callback callback) {
            super(context, callback);
        }
        
        @Override
        protected ActionModeWrapper createActionModeWrapper(final Context context, final android.view.ActionMode actionMode) {
            return new ActionModeWrapperJB(context, actionMode);
        }
    }
}
