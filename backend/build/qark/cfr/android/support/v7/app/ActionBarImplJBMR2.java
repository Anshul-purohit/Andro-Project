/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.graphics.drawable.Drawable
 *  java.lang.CharSequence
 */
package android.support.v7.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarImplJB;

public class ActionBarImplJBMR2
extends ActionBarImplJB {
    public ActionBarImplJBMR2(Activity activity, ActionBar.Callback callback) {
        super(activity, callback);
    }

    @Override
    public void setHomeActionContentDescription(int n) {
        this.mActionBar.setHomeActionContentDescription(n);
    }

    @Override
    public void setHomeActionContentDescription(CharSequence charSequence) {
        this.mActionBar.setHomeActionContentDescription(charSequence);
    }

    @Override
    public void setHomeAsUpIndicator(int n) {
        this.mActionBar.setHomeAsUpIndicator(n);
    }

    @Override
    public void setHomeAsUpIndicator(Drawable drawable2) {
        this.mActionBar.setHomeAsUpIndicator(drawable2);
    }
}

