/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.Adapter
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.SpinnerAdapter
 *  java.lang.CharSequence
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package android.support.v7.internal.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.AbsSpinnerICS;
import android.support.v7.internal.widget.AdapterViewICS;
import android.support.v7.internal.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

class SpinnerICS
extends AbsSpinnerICS
implements DialogInterface.OnClickListener {
    private static final int MAX_ITEMS_MEASURED = 15;
    static final int MODE_DIALOG = 0;
    static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "Spinner";
    int mDropDownWidth;
    private int mGravity;
    private SpinnerPopup mPopup;
    private DropDownAdapter mTempAdapter;
    private Rect mTempRect = new Rect();

    SpinnerICS(Context context) {
        this(context, null);
    }

    SpinnerICS(Context context, int n) {
        this(context, null, R.attr.spinnerStyle, n);
    }

    SpinnerICS(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.spinnerStyle);
    }

    SpinnerICS(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, -1);
    }

    /*
     * Enabled aggressive block sorting
     */
    SpinnerICS(Context object, AttributeSet attributeSet, int n, int n2) {
        super((Context)object, attributeSet, n);
        TypedArray typedArray = object.obtainStyledAttributes(attributeSet, R.styleable.Spinner, n, 0);
        int n3 = n2;
        if (n2 == -1) {
            n3 = typedArray.getInt(7, 0);
        }
        switch (n3) {
            case 0: {
                this.mPopup = new DialogPopup();
            }
            default: {
                break;
            }
            case 1: {
                object = new DropdownPopup((Context)object, attributeSet, n);
                this.mDropDownWidth = typedArray.getLayoutDimension(3, -2);
                object.setBackgroundDrawable(typedArray.getDrawable(2));
                n = typedArray.getDimensionPixelOffset(5, 0);
                if (n != 0) {
                    object.setVerticalOffset(n);
                }
                if ((n = typedArray.getDimensionPixelOffset(4, 0)) != 0) {
                    object.setHorizontalOffset(n);
                }
                this.mPopup = object;
            }
        }
        this.mGravity = typedArray.getInt(0, 17);
        this.mPopup.setPromptText(typedArray.getString(6));
        typedArray.recycle();
        if (this.mTempAdapter != null) {
            this.mPopup.setAdapter(this.mTempAdapter);
            this.mTempAdapter = null;
        }
    }

    private View makeAndAddView(int n) {
        View view;
        if (!this.mDataChanged && (view = this.mRecycler.get(n)) != null) {
            this.setUpChild(view);
            return view;
        }
        view = this.mAdapter.getView(n, null, (ViewGroup)this);
        this.setUpChild(view);
        return view;
    }

    private void setUpChild(View view) {
        ViewGroup.LayoutParams layoutParams;
        ViewGroup.LayoutParams layoutParams2 = layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams2 = this.generateDefaultLayoutParams();
        }
        this.addViewInLayout(view, 0, layoutParams2);
        view.setSelected(this.hasFocus());
        int n = ViewGroup.getChildMeasureSpec((int)this.mHeightMeasureSpec, (int)(this.mSpinnerPadding.top + this.mSpinnerPadding.bottom), (int)layoutParams2.height);
        view.measure(ViewGroup.getChildMeasureSpec((int)this.mWidthMeasureSpec, (int)(this.mSpinnerPadding.left + this.mSpinnerPadding.right), (int)layoutParams2.width), n);
        n = this.mSpinnerPadding.top + (this.getMeasuredHeight() - this.mSpinnerPadding.bottom - this.mSpinnerPadding.top - view.getMeasuredHeight()) / 2;
        int n2 = view.getMeasuredHeight();
        view.layout(0, n, 0 + view.getMeasuredWidth(), n + n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public int getBaseline() {
        View view;
        int n = -1;
        View view2 = null;
        if (this.getChildCount() > 0) {
            view = this.getChildAt(0);
        } else {
            view = view2;
            if (this.mAdapter != null) {
                view = view2;
                if (this.mAdapter.getCount() > 0) {
                    view = this.makeAndAddView(0);
                    this.mRecycler.put(0, view);
                    this.removeAllViewsInLayout();
                }
            }
        }
        int n2 = n;
        if (view == null) return n2;
        int n3 = view.getBaseline();
        n2 = n;
        if (n3 < 0) return n2;
        return view.getTop() + n3;
    }

    public CharSequence getPrompt() {
        return this.mPopup.getHintText();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void layout(int n, boolean bl) {
        int n2 = this.mSpinnerPadding.left;
        int n3 = this.getRight() - this.getLeft() - this.mSpinnerPadding.left - this.mSpinnerPadding.right;
        if (this.mDataChanged) {
            this.handleDataChanged();
        }
        if (this.mItemCount == 0) {
            this.resetList();
            return;
        }
        if (this.mNextSelectedPosition >= 0) {
            this.setSelectedPositionInt(this.mNextSelectedPosition);
        }
        this.recycleAllViews();
        this.removeAllViewsInLayout();
        this.mFirstPosition = this.mSelectedPosition;
        View view = this.makeAndAddView(this.mSelectedPosition);
        int n4 = view.getMeasuredWidth();
        n = n2;
        switch (this.mGravity & 7) {
            case 1: {
                n = n3 / 2 + n2 - n4 / 2;
            }
            default: {
                break;
            }
            case 5: {
                n = n2 + n3 - n4;
            }
        }
        view.offsetLeftAndRight(n);
        this.mRecycler.clear();
        this.invalidate();
        this.checkSelectionChanged();
        this.mDataChanged = false;
        this.mNeedSync = false;
        this.setNextSelectedPositionInt(this.mSelectedPosition);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    int measureContentWidth(SpinnerAdapter spinnerAdapter, Drawable drawable2) {
        if (spinnerAdapter == null) {
            return 0;
        }
        int n = 0;
        View view = null;
        int n2 = 0;
        int n3 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        int n4 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        int n5 = Math.max((int)0, (int)this.getSelectedItemPosition());
        int n6 = Math.min((int)spinnerAdapter.getCount(), (int)(n5 + 15));
        for (n5 = Math.max((int)0, (int)(n5 - (15 - (n6 - n5)))); n5 < n6; ++n5) {
            int n7 = spinnerAdapter.getItemViewType(n5);
            int n8 = n2;
            if (n7 != n2) {
                n8 = n7;
                view = null;
            }
            if ((view = spinnerAdapter.getView(n5, view, (ViewGroup)this)).getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            view.measure(n3, n4);
            n = Math.max((int)n, (int)view.getMeasuredWidth());
            n2 = n8;
        }
        n5 = n;
        if (drawable2 == null) return n5;
        drawable2.getPadding(this.mTempRect);
        return n + (this.mTempRect.left + this.mTempRect.right);
    }

    public void onClick(DialogInterface dialogInterface, int n) {
        this.setSelection(n);
        dialogInterface.dismiss();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPopup != null && this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.mInLayout = true;
        this.layout(0, false);
        this.mInLayout = false;
    }

    @Override
    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (this.mPopup != null && View.MeasureSpec.getMode((int)n) == Integer.MIN_VALUE) {
            this.setMeasuredDimension(Math.min((int)Math.max((int)this.getMeasuredWidth(), (int)this.measureContentWidth(this.getAdapter(), this.getBackground())), (int)View.MeasureSpec.getSize((int)n)), this.getMeasuredHeight());
        }
    }

    public boolean performClick() {
        boolean bl;
        boolean bl2 = bl = super.performClick();
        if (!bl) {
            bl2 = bl = true;
            if (!this.mPopup.isShowing()) {
                this.mPopup.show();
                bl2 = bl;
            }
        }
        return bl2;
    }

    @Override
    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            this.mPopup.setAdapter(new DropDownAdapter(spinnerAdapter));
            return;
        }
        this.mTempAdapter = new DropDownAdapter(spinnerAdapter);
    }

    public void setGravity(int n) {
        if (this.mGravity != n) {
            int n2 = n;
            if ((n & 7) == 0) {
                n2 = n | 3;
            }
            this.mGravity = n2;
            this.requestLayout();
        }
    }

    @Override
    public void setOnItemClickListener(AdapterViewICS.OnItemClickListener onItemClickListener) {
        throw new RuntimeException("setOnItemClickListener cannot be used with a spinner.");
    }

    void setOnItemClickListenerInt(AdapterViewICS.OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }

    public void setPrompt(CharSequence charSequence) {
        this.mPopup.setPromptText(charSequence);
    }

    public void setPromptId(int n) {
        this.setPrompt(this.getContext().getText(n));
    }

    private class DialogPopup
    implements SpinnerPopup,
    DialogInterface.OnClickListener {
        private ListAdapter mListAdapter;
        private AlertDialog mPopup;
        private CharSequence mPrompt;

        private DialogPopup() {
        }

        @Override
        public void dismiss() {
            this.mPopup.dismiss();
            this.mPopup = null;
        }

        @Override
        public CharSequence getHintText() {
            return this.mPrompt;
        }

        @Override
        public boolean isShowing() {
            if (this.mPopup != null) {
                return this.mPopup.isShowing();
            }
            return false;
        }

        public void onClick(DialogInterface dialogInterface, int n) {
            SpinnerICS.this.setSelection(n);
            if (SpinnerICS.this.mOnItemClickListener != null) {
                SpinnerICS.this.performItemClick(null, n, this.mListAdapter.getItemId(n));
            }
            this.dismiss();
        }

        @Override
        public void setAdapter(ListAdapter listAdapter) {
            this.mListAdapter = listAdapter;
        }

        @Override
        public void setPromptText(CharSequence charSequence) {
            this.mPrompt = charSequence;
        }

        @Override
        public void show() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SpinnerICS.this.getContext());
            if (this.mPrompt != null) {
                builder.setTitle(this.mPrompt);
            }
            this.mPopup = builder.setSingleChoiceItems(this.mListAdapter, SpinnerICS.this.getSelectedItemPosition(), (DialogInterface.OnClickListener)this).show();
        }
    }

    private static class DropDownAdapter
    implements ListAdapter,
    SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public DropDownAdapter(SpinnerAdapter spinnerAdapter) {
            this.mAdapter = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter)spinnerAdapter;
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.areAllItemsEnabled();
            }
            return true;
        }

        public int getCount() {
            if (this.mAdapter == null) {
                return 0;
            }
            return this.mAdapter.getCount();
        }

        public View getDropDownView(int n, View view, ViewGroup viewGroup) {
            if (this.mAdapter == null) {
                return null;
            }
            return this.mAdapter.getDropDownView(n, view, viewGroup);
        }

        public Object getItem(int n) {
            if (this.mAdapter == null) {
                return null;
            }
            return this.mAdapter.getItem(n);
        }

        public long getItemId(int n) {
            if (this.mAdapter == null) {
                return -1L;
            }
            return this.mAdapter.getItemId(n);
        }

        public int getItemViewType(int n) {
            return 0;
        }

        public View getView(int n, View view, ViewGroup viewGroup) {
            return this.getDropDownView(n, view, viewGroup);
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean hasStableIds() {
            if (this.mAdapter != null && this.mAdapter.hasStableIds()) {
                return true;
            }
            return false;
        }

        public boolean isEmpty() {
            if (this.getCount() == 0) {
                return true;
            }
            return false;
        }

        public boolean isEnabled(int n) {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.isEnabled(n);
            }
            return true;
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }

    private class DropdownPopup
    extends ListPopupWindow
    implements SpinnerPopup {
        private ListAdapter mAdapter;
        private CharSequence mHintText;

        public DropdownPopup(Context context, AttributeSet attributeSet, int n) {
            super(context, attributeSet, n);
            this.setAnchorView((View)SpinnerICS.this);
            this.setModal(true);
            this.setPromptPosition(0);
            this.setOnItemClickListener(new AdapterViewICS.OnItemClickListenerWrapper(new AdapterViewICS.OnItemClickListener(SpinnerICS.this){
                final /* synthetic */ SpinnerICS val$this$0;
                {
                    this.val$this$0 = spinnerICS;
                }

                public void onItemClick(AdapterViewICS adapterViewICS, View view, int n, long l) {
                    SpinnerICS.this.setSelection(n);
                    if (SpinnerICS.this.mOnItemClickListener != null) {
                        SpinnerICS.this.performItemClick(view, n, DropdownPopup.this.mAdapter.getItemId(n));
                    }
                    DropdownPopup.this.dismiss();
                }
            }));
        }

        @Override
        public CharSequence getHintText() {
            return this.mHintText;
        }

        @Override
        public void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        @Override
        public void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void show() {
            int n;
            int n2 = SpinnerICS.this.getPaddingLeft();
            if (SpinnerICS.this.mDropDownWidth == -2) {
                n = SpinnerICS.this.getWidth();
                int n3 = SpinnerICS.this.getPaddingRight();
                this.setContentWidth(Math.max((int)SpinnerICS.this.measureContentWidth((SpinnerAdapter)this.mAdapter, this.getBackground()), (int)(n - n2 - n3)));
            } else if (SpinnerICS.this.mDropDownWidth == -1) {
                this.setContentWidth(SpinnerICS.this.getWidth() - n2 - SpinnerICS.this.getPaddingRight());
            } else {
                this.setContentWidth(SpinnerICS.this.mDropDownWidth);
            }
            Drawable drawable2 = this.getBackground();
            n = 0;
            if (drawable2 != null) {
                drawable2.getPadding(SpinnerICS.this.mTempRect);
                n = - SpinnerICS.access$200((SpinnerICS)SpinnerICS.this).left;
            }
            this.setHorizontalOffset(n + n2);
            this.setInputMethodMode(2);
            super.show();
            this.getListView().setChoiceMode(1);
            this.setSelection(SpinnerICS.this.getSelectedItemPosition());
        }

    }

    private static interface SpinnerPopup {
        public void dismiss();

        public CharSequence getHintText();

        public boolean isShowing();

        public void setAdapter(ListAdapter var1);

        public void setPromptText(CharSequence var1);

        public void show();
    }

}

