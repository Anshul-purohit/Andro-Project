// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityNodeInfoCompatJellybeanMr2
{
    public static String getViewIdResourceName(final Object o) {
        return ((AccessibilityNodeInfo)o).getViewIdResourceName();
    }
    
    public static void setViewIdResourceName(final Object o, final String viewIdResourceName) {
        ((AccessibilityNodeInfo)o).setViewIdResourceName(viewIdResourceName);
    }
}
