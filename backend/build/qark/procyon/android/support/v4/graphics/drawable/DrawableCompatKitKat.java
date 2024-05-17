// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;

class DrawableCompatKitKat
{
    public static boolean isAutoMirrored(final Drawable drawable) {
        return drawable.isAutoMirrored();
    }
    
    public static void setAutoMirrored(final Drawable drawable, final boolean autoMirrored) {
        drawable.setAutoMirrored(autoMirrored);
    }
}
