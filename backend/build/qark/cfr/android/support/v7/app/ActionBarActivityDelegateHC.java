/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivityDelegateBase;
import android.support.v7.app.ActionBarImplHC;

class ActionBarActivityDelegateHC
extends ActionBarActivityDelegateBase {
    ActionBarActivityDelegateHC(ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }

    @Override
    public ActionBar createSupportActionBar() {
        this.ensureSubDecor();
        return new ActionBarImplHC(this.mActivity, this.mActivity);
    }
}

