// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.graphics.drawable.Drawable;
import android.app.Activity;

public class ActionBarImplJBMR2 extends ActionBarImplJB
{
    public ActionBarImplJBMR2(final Activity activity, final Callback callback) {
        super(activity, callback);
    }
    
    @Override
    public void setHomeActionContentDescription(final int homeActionContentDescription) {
        this.mActionBar.setHomeActionContentDescription(homeActionContentDescription);
    }
    
    @Override
    public void setHomeActionContentDescription(final CharSequence homeActionContentDescription) {
        this.mActionBar.setHomeActionContentDescription(homeActionContentDescription);
    }
    
    @Override
    public void setHomeAsUpIndicator(final int homeAsUpIndicator) {
        this.mActionBar.setHomeAsUpIndicator(homeAsUpIndicator);
    }
    
    @Override
    public void setHomeAsUpIndicator(final Drawable homeAsUpIndicator) {
        this.mActionBar.setHomeAsUpIndicator(homeAsUpIndicator);
    }
}
