// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.content.Intent;
import java.io.File;
import android.content.Context;

class ContextCompatHoneycomb
{
    public static File getObbDir(final Context context) {
        return context.getObbDir();
    }
    
    static void startActivities(final Context context, final Intent[] array) {
        context.startActivities(array);
    }
}
