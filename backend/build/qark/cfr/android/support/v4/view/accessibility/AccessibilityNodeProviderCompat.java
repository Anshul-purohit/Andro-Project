/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package android.support.v4.view.accessibility;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
    private static final AccessibilityNodeProviderImpl IMPL = Build.VERSION.SDK_INT >= 19 ? new AccessibilityNodeProviderKitKatImpl() : (Build.VERSION.SDK_INT >= 16 ? new AccessibilityNodeProviderJellyBeanImpl() : new AccessibilityNodeProviderStubImpl());
    private final Object mProvider;

    public AccessibilityNodeProviderCompat() {
        this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
    }

    public AccessibilityNodeProviderCompat(Object object) {
        this.mProvider = object;
    }

    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int n) {
        return null;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String string2, int n) {
        return null;
    }

    public AccessibilityNodeInfoCompat findFocus(int n) {
        return null;
    }

    public Object getProvider() {
        return this.mProvider;
    }

    public boolean performAction(int n, int n2, Bundle bundle) {
        return false;
    }

    static interface AccessibilityNodeProviderImpl {
        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat var1);
    }

    static class AccessibilityNodeProviderJellyBeanImpl
    extends AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderJellyBeanImpl() {
        }

        @Override
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge(){

                @Override
                public Object createAccessibilityNodeInfo(int n) {
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = accessibilityNodeProviderCompat.createAccessibilityNodeInfo(n);
                    if (accessibilityNodeInfoCompat == null) {
                        return null;
                    }
                    return accessibilityNodeInfoCompat.getInfo();
                }

                @Override
                public List<Object> findAccessibilityNodeInfosByText(String list, int n) {
                    list = accessibilityNodeProviderCompat.findAccessibilityNodeInfosByText((String)list, n);
                    ArrayList arrayList = new ArrayList();
                    int n2 = list.size();
                    for (n = 0; n < n2; ++n) {
                        arrayList.add(((AccessibilityNodeInfoCompat)list.get(n)).getInfo());
                    }
                    return arrayList;
                }

                @Override
                public boolean performAction(int n, int n2, Bundle bundle) {
                    return accessibilityNodeProviderCompat.performAction(n, n2, bundle);
                }
            });
        }

    }

    static class AccessibilityNodeProviderKitKatImpl
    extends AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderKitKatImpl() {
        }

        @Override
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge(){

                @Override
                public Object createAccessibilityNodeInfo(int n) {
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = accessibilityNodeProviderCompat.createAccessibilityNodeInfo(n);
                    if (accessibilityNodeInfoCompat == null) {
                        return null;
                    }
                    return accessibilityNodeInfoCompat.getInfo();
                }

                @Override
                public List<Object> findAccessibilityNodeInfosByText(String list, int n) {
                    list = accessibilityNodeProviderCompat.findAccessibilityNodeInfosByText((String)list, n);
                    ArrayList arrayList = new ArrayList();
                    int n2 = list.size();
                    for (n = 0; n < n2; ++n) {
                        arrayList.add(((AccessibilityNodeInfoCompat)list.get(n)).getInfo());
                    }
                    return arrayList;
                }

                @Override
                public Object findFocus(int n) {
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = accessibilityNodeProviderCompat.findFocus(n);
                    if (accessibilityNodeInfoCompat == null) {
                        return null;
                    }
                    return accessibilityNodeInfoCompat.getInfo();
                }

                @Override
                public boolean performAction(int n, int n2, Bundle bundle) {
                    return accessibilityNodeProviderCompat.performAction(n, n2, bundle);
                }
            });
        }

    }

    static class AccessibilityNodeProviderStubImpl
    implements AccessibilityNodeProviderImpl {
        AccessibilityNodeProviderStubImpl() {
        }

        @Override
        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return null;
        }
    }

}

