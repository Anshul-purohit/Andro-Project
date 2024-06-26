// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.os.Build$VERSION;

public class ScaleGestureDetectorCompat
{
    static final ScaleGestureDetectorImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (ScaleGestureDetectorImpl)new ScaleGestureDetectorCompatKitKatImpl();
            return;
        }
        IMPL = (ScaleGestureDetectorImpl)new BaseScaleGestureDetectorImpl();
    }
    
    private ScaleGestureDetectorCompat() {
    }
    
    public static boolean isQuickScaleEnabled(final Object o) {
        return ScaleGestureDetectorCompat.IMPL.isQuickScaleEnabled(o);
    }
    
    public static void setQuickScaleEnabled(final Object o, final boolean b) {
        ScaleGestureDetectorCompat.IMPL.setQuickScaleEnabled(o, b);
    }
    
    private static class BaseScaleGestureDetectorImpl implements ScaleGestureDetectorImpl
    {
        @Override
        public boolean isQuickScaleEnabled(final Object o) {
            return false;
        }
        
        @Override
        public void setQuickScaleEnabled(final Object o, final boolean b) {
        }
    }
    
    private static class ScaleGestureDetectorCompatKitKatImpl implements ScaleGestureDetectorImpl
    {
        @Override
        public boolean isQuickScaleEnabled(final Object o) {
            return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(o);
        }
        
        @Override
        public void setQuickScaleEnabled(final Object o, final boolean b) {
            ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(o, b);
        }
    }
    
    interface ScaleGestureDetectorImpl
    {
        boolean isQuickScaleEnabled(final Object p0);
        
        void setQuickScaleEnabled(final Object p0, final boolean p1);
    }
}
