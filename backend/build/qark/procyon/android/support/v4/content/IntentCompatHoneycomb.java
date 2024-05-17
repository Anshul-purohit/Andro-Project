// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.content.Intent;
import android.content.ComponentName;

class IntentCompatHoneycomb
{
    public static Intent makeMainActivity(final ComponentName componentName) {
        return Intent.makeMainActivity(componentName);
    }
    
    public static Intent makeRestartActivityTask(final ComponentName componentName) {
        return Intent.makeRestartActivityTask(componentName);
    }
}
