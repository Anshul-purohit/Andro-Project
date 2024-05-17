// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.View;

public class ViewCompatKitKat
{
    public static int getAccessibilityLiveRegion(final View view) {
        return view.getAccessibilityLiveRegion();
    }
    
    public static void setAccessibilityLiveRegion(final View view, final int accessibilityLiveRegion) {
        view.setAccessibilityLiveRegion(accessibilityLiveRegion);
    }
}
