// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build$VERSION;

public class ViewGroupCompat
{
    static final ViewGroupCompatImpl IMPL;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 18) {
            IMPL = (ViewGroupCompatImpl)new ViewGroupCompatJellybeanMR2Impl();
            return;
        }
        if (sdk_INT >= 14) {
            IMPL = (ViewGroupCompatImpl)new ViewGroupCompatIcsImpl();
            return;
        }
        if (sdk_INT >= 11) {
            IMPL = (ViewGroupCompatImpl)new ViewGroupCompatHCImpl();
            return;
        }
        IMPL = (ViewGroupCompatImpl)new ViewGroupCompatStubImpl();
    }
    
    private ViewGroupCompat() {
    }
    
    public static int getLayoutMode(final ViewGroup viewGroup) {
        return ViewGroupCompat.IMPL.getLayoutMode(viewGroup);
    }
    
    public static boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return ViewGroupCompat.IMPL.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }
    
    public static void setLayoutMode(final ViewGroup viewGroup, final int n) {
        ViewGroupCompat.IMPL.setLayoutMode(viewGroup, n);
    }
    
    public static void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean b) {
        ViewGroupCompat.IMPL.setMotionEventSplittingEnabled(viewGroup, b);
    }
    
    static class ViewGroupCompatHCImpl extends ViewGroupCompatStubImpl
    {
        @Override
        public void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean b) {
            ViewGroupCompatHC.setMotionEventSplittingEnabled(viewGroup, b);
        }
    }
    
    static class ViewGroupCompatIcsImpl extends ViewGroupCompatHCImpl
    {
        @Override
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return ViewGroupCompatIcs.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }
    
    interface ViewGroupCompatImpl
    {
        int getLayoutMode(final ViewGroup p0);
        
        boolean onRequestSendAccessibilityEvent(final ViewGroup p0, final View p1, final AccessibilityEvent p2);
        
        void setLayoutMode(final ViewGroup p0, final int p1);
        
        void setMotionEventSplittingEnabled(final ViewGroup p0, final boolean p1);
    }
    
    static class ViewGroupCompatJellybeanMR2Impl extends ViewGroupCompatIcsImpl
    {
        @Override
        public int getLayoutMode(final ViewGroup viewGroup) {
            return ViewGroupCompatJellybeanMR2.getLayoutMode(viewGroup);
        }
        
        @Override
        public void setLayoutMode(final ViewGroup viewGroup, final int n) {
            ViewGroupCompatJellybeanMR2.setLayoutMode(viewGroup, n);
        }
    }
    
    static class ViewGroupCompatStubImpl implements ViewGroupCompatImpl
    {
        @Override
        public int getLayoutMode(final ViewGroup viewGroup) {
            return 0;
        }
        
        @Override
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return true;
        }
        
        @Override
        public void setLayoutMode(final ViewGroup viewGroup, final int n) {
        }
        
        @Override
        public void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean b) {
        }
    }
}
