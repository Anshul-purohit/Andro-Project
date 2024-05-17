/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.View
 *  android.widget.LinearLayout
 *  java.lang.Object
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import android.widget.LinearLayout;

public class NativeActionModeAwareLayout
extends LinearLayout {
    private OnActionModeForChildListener mActionModeForChildListener;

    public NativeActionModeAwareLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setActionModeForChildListener(OnActionModeForChildListener onActionModeForChildListener) {
        this.mActionModeForChildListener = onActionModeForChildListener;
    }

    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        ActionMode.Callback callback2 = callback;
        if (this.mActionModeForChildListener != null) {
            callback2 = this.mActionModeForChildListener.onActionModeForChild(callback);
        }
        return super.startActionModeForChild(view, callback2);
    }

    public static interface OnActionModeForChildListener {
        public ActionMode.Callback onActionModeForChild(ActionMode.Callback var1);
    }

}

