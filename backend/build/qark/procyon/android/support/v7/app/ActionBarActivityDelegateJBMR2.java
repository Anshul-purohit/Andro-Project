// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.app.Activity;

class ActionBarActivityDelegateJBMR2 extends ActionBarActivityDelegateJB
{
    ActionBarActivityDelegateJBMR2(final ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }
    
    @Override
    public ActionBar createSupportActionBar() {
        return new ActionBarImplJBMR2(this.mActivity, this.mActivity);
    }
}
