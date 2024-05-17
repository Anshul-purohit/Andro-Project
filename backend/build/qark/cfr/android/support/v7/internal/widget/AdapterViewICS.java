/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.DataSetObserver
 *  android.os.Parcelable
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewDebug
 *  android.view.ViewDebug$CapturedViewProperty
 *  android.view.ViewDebug$ExportedProperty
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.Adapter
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  java.lang.ClassCastException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Adapter;
import android.widget.AdapterView;

abstract class AdapterViewICS<T extends Adapter>
extends ViewGroup {
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = Long.MIN_VALUE;
    static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
    static final int ITEM_VIEW_TYPE_IGNORE = -1;
    static final int SYNC_FIRST_POSITION = 1;
    static final int SYNC_MAX_DURATION_MILLIS = 100;
    static final int SYNC_SELECTED_POSITION = 0;
    boolean mBlockLayoutRequests = false;
    boolean mDataChanged;
    private boolean mDesiredFocusableInTouchModeState;
    private boolean mDesiredFocusableState;
    private View mEmptyView;
    @ViewDebug.ExportedProperty(category="scrolling")
    int mFirstPosition = 0;
    boolean mInLayout = false;
    @ViewDebug.ExportedProperty(category="list")
    int mItemCount;
    private int mLayoutHeight;
    boolean mNeedSync = false;
    @ViewDebug.ExportedProperty(category="list")
    int mNextSelectedPosition = -1;
    long mNextSelectedRowId = Long.MIN_VALUE;
    int mOldItemCount;
    int mOldSelectedPosition = -1;
    long mOldSelectedRowId = Long.MIN_VALUE;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnItemSelectedListener mOnItemSelectedListener;
    @ViewDebug.ExportedProperty(category="list")
    int mSelectedPosition = -1;
    long mSelectedRowId = Long.MIN_VALUE;
    private AdapterViewICS<T> mSelectionNotifier;
    int mSpecificTop;
    long mSyncHeight;
    int mSyncMode;
    int mSyncPosition;
    long mSyncRowId = Long.MIN_VALUE;

    AdapterViewICS(Context context) {
        super(context);
    }

    AdapterViewICS(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    AdapterViewICS(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private void fireOnSelected() {
        if (this.mOnItemSelectedListener == null) {
            return;
        }
        int n = this.getSelectedItemPosition();
        if (n >= 0) {
            View view = this.getSelectedView();
            this.mOnItemSelectedListener.onItemSelected(this, view, n, this.getAdapter().getItemId(n));
            return;
        }
        this.mOnItemSelectedListener.onNothingSelected(this);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateEmptyStatus(boolean bl) {
        if (this.isInFilterMode()) {
            bl = false;
        }
        if (bl) {
            if (this.mEmptyView != null) {
                this.mEmptyView.setVisibility(0);
                this.setVisibility(8);
            } else {
                this.setVisibility(0);
            }
            if (this.mDataChanged) {
                this.onLayout(false, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            }
            return;
        }
        if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(8);
        }
        this.setVisibility(0);
    }

    public void addView(View view) {
        throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
    }

    public void addView(View view, int n) {
        throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
    }

    protected boolean canAnimate() {
        if (super.canAnimate() && this.mItemCount > 0) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    void checkFocus() {
        block2 : {
            boolean bl;
            block4 : {
                block3 : {
                    boolean bl2 = false;
                    T t = this.getAdapter();
                    boolean bl3 = t == null || t.getCount() == 0;
                    bl3 = !bl3 || this.isInFilterMode();
                    bl = bl3 && this.mDesiredFocusableInTouchModeState;
                    super.setFocusableInTouchMode(bl);
                    bl = bl3 && this.mDesiredFocusableState;
                    super.setFocusable(bl);
                    if (this.mEmptyView == null) break block2;
                    if (t == null) break block3;
                    bl = bl2;
                    if (!t.isEmpty()) break block4;
                }
                bl = true;
            }
            this.updateEmptyStatus(bl);
        }
    }

    void checkSelectionChanged() {
        if (this.mSelectedPosition != this.mOldSelectedPosition || this.mSelectedRowId != this.mOldSelectedRowId) {
            this.selectionChanged();
            this.mOldSelectedPosition = this.mSelectedPosition;
            this.mOldSelectedRowId = this.mSelectedRowId;
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        View view = this.getSelectedView();
        if (view != null && view.getVisibility() == 0 && view.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
            return true;
        }
        return false;
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly(sparseArray);
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly(sparseArray);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    int findSyncPosition() {
        var7_1 = this.mItemCount;
        if (var7_1 == 0) {
            return -1;
        }
        var8_3 = this.mSyncRowId;
        var1_4 = this.mSyncPosition;
        if (var8_3 == Long.MIN_VALUE) {
            return -1;
        }
        var1_4 = Math.min((int)(var7_1 - 1), (int)Math.max((int)0, (int)var1_4));
        var10_5 = SystemClock.uptimeMillis();
        var3_6 = var1_4;
        var4_7 = var1_4;
        var2_8 = false;
        var12_9 = this.getAdapter();
        if (var12_9 != null) ** GOTO lbl25
        return -1;
lbl-1000: // 1 sources:
        {
            block5 : {
                if (var6_10 || var2_8 && var5_2 == 0) break block5;
                if (var5_2 != 0 || !var2_8 && !var6_10) {
                    var1_4 = --var3_6;
                    var2_8 = true;
                }
                ** GOTO lbl25
            }
            do {
                var1_4 = ++var4_7;
                var2_8 = false;
lbl25: // 3 sources:
                if (SystemClock.uptimeMillis() > var10_5 + 100L) return -1;
                var5_2 = var1_4;
                if (var12_9.getItemId(var1_4) == var8_3) return var5_2;
                var5_2 = var4_7 == var7_1 - 1 ? 1 : 0;
                var6_10 = var3_6 == 0;
            } while (var5_2 == 0);
            ** while (!var6_10)
        }
lbl32: // 1 sources:
        return -1;
    }

    public abstract T getAdapter();

    @ViewDebug.CapturedViewProperty
    public int getCount() {
        return this.mItemCount;
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    public int getFirstVisiblePosition() {
        return this.mFirstPosition;
    }

    public Object getItemAtPosition(int n) {
        T t = this.getAdapter();
        if (t == null || n < 0) {
            return null;
        }
        return t.getItem(n);
    }

    public long getItemIdAtPosition(int n) {
        T t = this.getAdapter();
        if (t == null || n < 0) {
            return Long.MIN_VALUE;
        }
        return t.getItemId(n);
    }

    public int getLastVisiblePosition() {
        return this.mFirstPosition + this.getChildCount() - 1;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public final OnItemSelectedListener getOnItemSelectedListener() {
        return this.mOnItemSelectedListener;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getPositionForView(View view) {
        do {
            try {
                View view2 = (View)view.getParent();
                boolean bl = view2.equals((Object)((Object)this));
                if (bl) break;
                view = view2;
            }
            catch (ClassCastException classCastException) {
                return -1;
            }
        } while (true);
        int n = this.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            if (this.getChildAt(n2).equals((Object)view)) {
                return this.mFirstPosition + n2;
            }
            ++n2;
        }
        return -1;
    }

    public Object getSelectedItem() {
        T t = this.getAdapter();
        int n = this.getSelectedItemPosition();
        if (t != null && t.getCount() > 0 && n >= 0) {
            return t.getItem(n);
        }
        return null;
    }

    @ViewDebug.CapturedViewProperty
    public long getSelectedItemId() {
        return this.mNextSelectedRowId;
    }

    @ViewDebug.CapturedViewProperty
    public int getSelectedItemPosition() {
        return this.mNextSelectedPosition;
    }

    public abstract View getSelectedView();

    void handleDataChanged() {
        int n = this.mItemCount;
        int n2 = 0;
        int n3 = 0;
        if (n > 0) {
            int n4 = n3;
            if (this.mNeedSync) {
                this.mNeedSync = false;
                n2 = this.findSyncPosition();
                n4 = n3;
                if (n2 >= 0) {
                    n4 = n3;
                    if (this.lookForSelectablePosition(n2, true) == n2) {
                        this.setNextSelectedPositionInt(n2);
                        n4 = 1;
                    }
                }
            }
            n2 = n4;
            if (n4 == 0) {
                n2 = n3 = this.getSelectedItemPosition();
                if (n3 >= n) {
                    n2 = n - 1;
                }
                n3 = n2;
                if (n2 < 0) {
                    n3 = 0;
                }
                n = n2 = this.lookForSelectablePosition(n3, true);
                if (n2 < 0) {
                    n = this.lookForSelectablePosition(n3, false);
                }
                n2 = n4;
                if (n >= 0) {
                    this.setNextSelectedPositionInt(n);
                    this.checkSelectionChanged();
                    n2 = 1;
                }
            }
        }
        if (n2 == 0) {
            this.mSelectedPosition = -1;
            this.mSelectedRowId = Long.MIN_VALUE;
            this.mNextSelectedPosition = -1;
            this.mNextSelectedRowId = Long.MIN_VALUE;
            this.mNeedSync = false;
            this.checkSelectionChanged();
        }
    }

    boolean isInFilterMode() {
        return false;
    }

    int lookForSelectablePosition(int n, boolean bl) {
        return n;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mSelectionNotifier);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        this.mLayoutHeight = this.getHeight();
    }

    public boolean performItemClick(View view, int n, long l) {
        boolean bl = false;
        if (this.mOnItemClickListener != null) {
            this.playSoundEffect(0);
            if (view != null) {
                view.sendAccessibilityEvent(1);
            }
            this.mOnItemClickListener.onItemClick(this, view, n, l);
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    void rememberSyncState() {
        block6 : {
            block5 : {
                if (this.getChildCount() <= 0) break block5;
                this.mNeedSync = true;
                this.mSyncHeight = this.mLayoutHeight;
                if (this.mSelectedPosition < 0) break block6;
                View view = this.getChildAt(this.mSelectedPosition - this.mFirstPosition);
                this.mSyncRowId = this.mNextSelectedRowId;
                this.mSyncPosition = this.mNextSelectedPosition;
                if (view != null) {
                    this.mSpecificTop = view.getTop();
                }
                this.mSyncMode = 0;
            }
            return;
        }
        View view = this.getChildAt(0);
        T t = this.getAdapter();
        this.mSyncRowId = this.mFirstPosition >= 0 && this.mFirstPosition < t.getCount() ? t.getItemId(this.mFirstPosition) : -1L;
        this.mSyncPosition = this.mFirstPosition;
        if (view != null) {
            this.mSpecificTop = view.getTop();
        }
        this.mSyncMode = 1;
    }

    public void removeAllViews() {
        throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
    }

    public void removeView(View view) {
        throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
    }

    public void removeViewAt(int n) {
        throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
    }

    /*
     * Enabled aggressive block sorting
     */
    void selectionChanged() {
        if (this.mOnItemSelectedListener != null) {
            if (this.mInLayout || this.mBlockLayoutRequests) {
                if (this.mSelectionNotifier == null) {
                    this.mSelectionNotifier = new SelectionNotifier();
                }
                this.post(this.mSelectionNotifier);
            } else {
                this.fireOnSelected();
            }
        }
        if (this.mSelectedPosition != -1 && this.isShown() && !this.isInTouchMode()) {
            this.sendAccessibilityEvent(4);
        }
    }

    public abstract void setAdapter(T var1);

    /*
     * Enabled aggressive block sorting
     */
    public void setEmptyView(View object) {
        this.mEmptyView = object;
        object = this.getAdapter();
        boolean bl = object == null || object.isEmpty();
        this.updateEmptyStatus(bl);
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setFocusable(boolean var1_1) {
        block6 : {
            var3_2 = true;
            var4_3 = this.getAdapter();
            if (var4_3 == null || var4_3.getCount() == 0) {
                var2_4 = true;
lbl5: // 2 sources:
                do {
                    this.mDesiredFocusableState = var1_1;
                    if (!var1_1) {
                        this.mDesiredFocusableInTouchModeState = false;
                    }
                    if (!var1_1) break block6;
                    var1_1 = var3_2;
                    if (var2_4) {
                        if (!this.isInFilterMode()) break block6;
                        var1_1 = var3_2;
                    }
lbl14: // 4 sources:
                    do {
                        super.setFocusable(var1_1);
                        return;
                        break;
                    } while (true);
                    break;
                } while (true);
            }
            var2_4 = false;
            ** while (true)
        }
        var1_1 = false;
        ** while (true)
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setFocusableInTouchMode(boolean var1_1) {
        block6 : {
            var3_2 = true;
            var4_3 = this.getAdapter();
            if (var4_3 == null || var4_3.getCount() == 0) {
                var2_4 = true;
lbl5: // 2 sources:
                do {
                    this.mDesiredFocusableInTouchModeState = var1_1;
                    if (var1_1) {
                        this.mDesiredFocusableState = true;
                    }
                    if (!var1_1) break block6;
                    var1_1 = var3_2;
                    if (var2_4) {
                        if (!this.isInFilterMode()) break block6;
                        var1_1 = var3_2;
                    }
lbl14: // 4 sources:
                    do {
                        super.setFocusableInTouchMode(var1_1);
                        return;
                        break;
                    } while (true);
                    break;
                } while (true);
            }
            var2_4 = false;
            ** while (true)
        }
        var1_1 = false;
        ** while (true)
    }

    void setNextSelectedPositionInt(int n) {
        this.mNextSelectedPosition = n;
        this.mNextSelectedRowId = this.getItemIdAtPosition(n);
        if (this.mNeedSync && this.mSyncMode == 0 && n >= 0) {
            this.mSyncPosition = n;
            this.mSyncRowId = this.mNextSelectedRowId;
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        if (!this.isLongClickable()) {
            this.setLongClickable(true);
        }
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    void setSelectedPositionInt(int n) {
        this.mSelectedPosition = n;
        this.mSelectedRowId = this.getItemIdAtPosition(n);
    }

    public abstract void setSelection(int var1);

    public static class AdapterContextMenuInfo
    implements ContextMenu.ContextMenuInfo {
        public long id;
        public int position;
        public View targetView;

        public AdapterContextMenuInfo(View view, int n, long l) {
            this.targetView = view;
            this.position = n;
            this.id = l;
        }
    }

    class AdapterDataSetObserver
    extends DataSetObserver {
        private Parcelable mInstanceState;

        AdapterDataSetObserver() {
            this.mInstanceState = null;
        }

        public void clearSavedState() {
            this.mInstanceState = null;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onChanged() {
            AdapterViewICS.this.mDataChanged = true;
            AdapterViewICS.this.mOldItemCount = AdapterViewICS.this.mItemCount;
            AdapterViewICS.this.mItemCount = AdapterViewICS.this.getAdapter().getCount();
            if (AdapterViewICS.this.getAdapter().hasStableIds() && this.mInstanceState != null && AdapterViewICS.this.mOldItemCount == 0 && AdapterViewICS.this.mItemCount > 0) {
                AdapterViewICS.this.onRestoreInstanceState(this.mInstanceState);
                this.mInstanceState = null;
            } else {
                AdapterViewICS.this.rememberSyncState();
            }
            AdapterViewICS.this.checkFocus();
            AdapterViewICS.this.requestLayout();
        }

        public void onInvalidated() {
            AdapterViewICS.this.mDataChanged = true;
            if (AdapterViewICS.this.getAdapter().hasStableIds()) {
                this.mInstanceState = AdapterViewICS.this.onSaveInstanceState();
            }
            AdapterViewICS.this.mOldItemCount = AdapterViewICS.this.mItemCount;
            AdapterViewICS.this.mItemCount = 0;
            AdapterViewICS.this.mSelectedPosition = -1;
            AdapterViewICS.this.mSelectedRowId = Long.MIN_VALUE;
            AdapterViewICS.this.mNextSelectedPosition = -1;
            AdapterViewICS.this.mNextSelectedRowId = Long.MIN_VALUE;
            AdapterViewICS.this.mNeedSync = false;
            AdapterViewICS.this.checkFocus();
            AdapterViewICS.this.requestLayout();
        }
    }

    public static interface OnItemClickListener {
        public void onItemClick(AdapterViewICS<?> var1, View var2, int var3, long var4);
    }

    class OnItemClickListenerWrapper
    implements AdapterView.OnItemClickListener {
        private final OnItemClickListener mWrappedListener;

        public OnItemClickListenerWrapper(OnItemClickListener onItemClickListener) {
            this.mWrappedListener = onItemClickListener;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
            this.mWrappedListener.onItemClick(AdapterViewICS.this, view, n, l);
        }
    }

    public static interface OnItemLongClickListener {
        public boolean onItemLongClick(AdapterViewICS<?> var1, View var2, int var3, long var4);
    }

    public static interface OnItemSelectedListener {
        public void onItemSelected(AdapterViewICS<?> var1, View var2, int var3, long var4);

        public void onNothingSelected(AdapterViewICS<?> var1);
    }

    private class SelectionNotifier
    implements Runnable {
        private SelectionNotifier() {
        }

        public void run() {
            if (AdapterViewICS.this.mDataChanged) {
                if (AdapterViewICS.this.getAdapter() != null) {
                    AdapterViewICS.this.post((Runnable)this);
                }
                return;
            }
            AdapterViewICS.this.fireOnSelected();
        }
    }

}

