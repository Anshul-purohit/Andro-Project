// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

class ActionBarActivityDelegateHC extends ActionBarActivityDelegateBase
{
    ActionBarActivityDelegateHC(final ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }
    
    @Override
    public ActionBar createSupportActionBar() {
        this.ensureSubDecor();
        return new ActionBarImplHC(this.mActivity, this.mActivity);
    }
}
