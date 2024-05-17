/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.DataSetObserver
 *  android.graphics.Rect
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.Adapter
 *  android.widget.SpinnerAdapter
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.internal.widget.AdapterViewICS;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SpinnerAdapter;

abstract class AbsSpinnerICS
extends AdapterViewICS<SpinnerAdapter> {
    SpinnerAdapter mAdapter;
    boolean mBlockLayoutRequests;
    private DataSetObserver mDataSetObserver;
    int mHeightMeasureSpec;
    final RecycleBin mRecycler;
    int mSelectionBottomPadding = 0;
    int mSelectionLeftPadding = 0;
    int mSelectionRightPadding = 0;
    int mSelectionTopPadding = 0;
    final Rect mSpinnerPadding = new Rect();
    private Rect mTouchFrame;
    int mWidthMeasureSpec;

    AbsSpinnerICS(Context context) {
        super(context);
        this.mRecycler = new RecycleBin();
        this.initAbsSpinner();
    }

    AbsSpinnerICS(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    AbsSpinnerICS(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mRecycler = new RecycleBin();
        this.initAbsSpinner();
    }

    private void initAbsSpinner() {
        this.setFocusable(true);
        this.setWillNotDraw(false);
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-1, -2);
    }

    @Override
    public SpinnerAdapter getAdapter() {
        return this.mAdapter;
    }

    int getChildHeight(View view) {
        return view.getMeasuredHeight();
    }

    int getChildWidth(View view) {
        return view.getMeasuredWidth();
    }

    @Override
    public int getCount() {
        return this.mItemCount;
    }

    @Override
    public View getSelectedView() {
        if (this.mItemCount > 0 && this.mSelectedPosition >= 0) {
            return this.getChildAt(this.mSelectedPosition - this.mFirstPosition);
        }
        return null;
    }

    abstract void layout(int var1, boolean var2);

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        int n4 = this.getPaddingLeft();
        int n5 = this.getPaddingTop();
        int n6 = this.getPaddingRight();
        int n7 = this.getPaddingBottom();
        Rect rect = this.mSpinnerPadding;
        if (n4 <= this.mSelectionLeftPadding) {
            n4 = this.mSelectionLeftPadding;
        }
        rect.left = n4;
        rect = this.mSpinnerPadding;
        n4 = n5 > this.mSelectionTopPadding ? n5 : this.mSelectionTopPadding;
        rect.top = n4;
        rect = this.mSpinnerPadding;
        n4 = n6 > this.mSelectionRightPadding ? n6 : this.mSelectionRightPadding;
        rect.right = n4;
        rect = this.mSpinnerPadding;
        n4 = n7 > this.mSelectionBottomPadding ? n7 : this.mSelectionBottomPadding;
        rect.bottom = n4;
        if (this.mDataChanged) {
            this.handleDataChanged();
        }
        n5 = 0;
        int n8 = 0;
        int n9 = 1;
        int n10 = this.getSelectedItemPosition();
        n6 = n9;
        n7 = n5;
        n4 = n8;
        if (n10 >= 0) {
            n6 = n9;
            n7 = n5;
            n4 = n8;
            if (this.mAdapter != null) {
                n6 = n9;
                n7 = n5;
                n4 = n8;
                if (n10 < this.mAdapter.getCount()) {
                    View view = this.mRecycler.get(n10);
                    rect = view;
                    if (view == null) {
                        rect = this.mAdapter.getView(n10, null, (ViewGroup)this);
                    }
                    if (rect != null) {
                        this.mRecycler.put(n10, (View)rect);
                    }
                    n6 = n9;
                    n7 = n5;
                    n4 = n8;
                    if (rect != null) {
                        if (rect.getLayoutParams() == null) {
                            this.mBlockLayoutRequests = true;
                            rect.setLayoutParams(this.generateDefaultLayoutParams());
                            this.mBlockLayoutRequests = false;
                        }
                        this.measureChild((View)rect, n, n2);
                        n7 = this.getChildHeight((View)rect) + this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
                        n4 = this.getChildWidth((View)rect) + this.mSpinnerPadding.left + this.mSpinnerPadding.right;
                        n6 = 0;
                    }
                }
            }
        }
        n5 = n7;
        n7 = n4;
        if (n6 != 0) {
            n5 = n6 = this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
            n7 = n4;
            if (n3 == 0) {
                n7 = this.mSpinnerPadding.left + this.mSpinnerPadding.right;
                n5 = n6;
            }
        }
        n4 = Math.max((int)n5, (int)this.getSuggestedMinimumHeight());
        n7 = Math.max((int)n7, (int)this.getSuggestedMinimumWidth());
        n4 = AbsSpinnerICS.resolveSize((int)n4, (int)n2);
        this.setMeasuredDimension(AbsSpinnerICS.resolveSize((int)n7, (int)n), n4);
        this.mHeightMeasureSpec = n2;
        this.mWidthMeasureSpec = n;
    }

    public void onRestoreInstanceState(Parcelable object) {
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        if (object.selectedId >= 0L) {
            this.mDataChanged = true;
            this.mNeedSync = true;
            this.mSyncRowId = object.selectedId;
            this.mSyncPosition = object.position;
            this.mSyncMode = 0;
            this.requestLayout();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.selectedId = this.getSelectedItemId();
        if (savedState.selectedId >= 0L) {
            savedState.position = this.getSelectedItemPosition();
            return savedState;
        }
        savedState.position = -1;
        return savedState;
    }

    public int pointToPosition(int n, int n2) {
        Rect rect;
        Rect rect2 = rect = this.mTouchFrame;
        if (rect == null) {
            rect2 = this.mTouchFrame = new Rect();
        }
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            rect = this.getChildAt(i);
            if (rect.getVisibility() != 0) continue;
            rect.getHitRect(rect2);
            if (!rect2.contains(n, n2)) continue;
            return this.mFirstPosition + i;
        }
        return -1;
    }

    void recycleAllViews() {
        int n = this.getChildCount();
        RecycleBin recycleBin = this.mRecycler;
        int n2 = this.mFirstPosition;
        for (int i = 0; i < n; ++i) {
            recycleBin.put(n2 + i, this.getChildAt(i));
        }
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    void resetList() {
        this.mDataChanged = false;
        this.mNeedSync = false;
        this.removeAllViewsInLayout();
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.setSelectedPositionInt(-1);
        this.setNextSelectedPositionInt(-1);
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        int n = -1;
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.resetList();
        }
        this.mAdapter = spinnerAdapter;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        if (this.mAdapter != null) {
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            this.checkFocus();
            this.mDataSetObserver = new AdapterViewICS.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            if (this.mItemCount > 0) {
                n = 0;
            }
            this.setSelectedPositionInt(n);
            this.setNextSelectedPositionInt(n);
            if (this.mItemCount == 0) {
                this.checkSelectionChanged();
            }
        } else {
            this.checkFocus();
            this.resetList();
            this.checkSelectionChanged();
        }
        this.requestLayout();
    }

    @Override
    public void setSelection(int n) {
        this.setNextSelectedPositionInt(n);
        this.requestLayout();
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSelection(int n, boolean bl) {
        bl = bl && this.mFirstPosition <= n && n <= this.mFirstPosition + this.getChildCount() - 1;
        this.setSelectionInt(n, bl);
    }

    void setSelectionInt(int n, boolean bl) {
        if (n != this.mOldSelectedPosition) {
            this.mBlockLayoutRequests = true;
            int n2 = this.mSelectedPosition;
            this.setNextSelectedPositionInt(n);
            this.layout(n - n2, bl);
            this.mBlockLayoutRequests = false;
        }
    }

    class RecycleBin {
        private final SparseArray<View> mScrapHeap;

        RecycleBin() {
            this.mScrapHeap = new SparseArray();
        }

        void clear() {
            SparseArray<View> sparseArray = this.mScrapHeap;
            int n = sparseArray.size();
            for (int i = 0; i < n; ++i) {
                View view = (View)sparseArray.valueAt(i);
                if (view == null) continue;
                AbsSpinnerICS.this.removeDetachedView(view, true);
            }
            sparseArray.clear();
        }

        View get(int n) {
            View view = (View)this.mScrapHeap.get(n);
            if (view != null) {
                this.mScrapHeap.delete(n);
            }
            return view;
        }

        public void put(int n, View view) {
            this.mScrapHeap.put(n, (Object)view);
        }
    }

    static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int position;
        long selectedId;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.selectedId = parcel.readLong();
            this.position = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "AbsSpinner.SavedState{" + Integer.toHexString((int)System.identityHashCode((Object)((Object)this))) + " selectedId=" + this.selectedId + " position=" + this.position + "}";
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeLong(this.selectedId);
            parcel.writeInt(this.position);
        }

    }

}

