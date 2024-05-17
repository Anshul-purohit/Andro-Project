/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.widget.ListView
 */
package android.support.v4.widget;

import android.support.v4.widget.AutoScrollHelper;
import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper
extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(ListView listView) {
        super((View)listView);
        this.mTarget = listView;
    }

    @Override
    public boolean canTargetScrollHorizontally(int n) {
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean canTargetScrollVertically(int n) {
        ListView listView = this.mTarget;
        int n2 = listView.getCount();
        int n3 = listView.getChildCount();
        int n4 = listView.getFirstVisiblePosition();
        if (n > 0 ? n4 + n3 >= n2 && listView.getChildAt(n3 - 1).getBottom() <= listView.getHeight() : n >= 0 || n4 <= 0 && listView.getChildAt(0).getTop() >= 0) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void scrollTargetBy(int n, int n2) {
        View view;
        ListView listView = this.mTarget;
        n = listView.getFirstVisiblePosition();
        if (n == -1 || (view = listView.getChildAt(0)) == null) {
            return;
        }
        listView.setSelectionFromTop(n, view.getTop() - n2);
    }
}

