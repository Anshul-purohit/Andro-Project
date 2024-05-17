/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.database.DataSetObserver
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.os.IBinder
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.AbsListView
 *  android.widget.AbsListView$OnScrollListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow
 *  android.widget.PopupWindow$OnDismissListener
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.Locale
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.Locale;

public class ListPopupWindow {
    private static final boolean DEBUG = false;
    private static final int EXPAND_LIST_TIMEOUT = 250;
    public static final int FILL_PARENT = -1;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    private static final String TAG = "ListPopupWindow";
    public static final int WRAP_CONTENT = -2;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible = false;
    private View mDropDownAnchorView;
    private int mDropDownHeight = -2;
    private int mDropDownHorizontalOffset;
    private DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth = -2;
    private boolean mForceIgnoreOutsideTouch = false;
    private Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;
    private int mLayoutDirection;
    int mListItemExpandMaximum = Integer.MAX_VALUE;
    private boolean mModal;
    private DataSetObserver mObserver;
    private PopupWindow mPopup;
    private int mPromptPosition = 0;
    private View mPromptView;
    private final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;

    public ListPopupWindow(Context context) {
        this(context, null, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int n) {
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mHandler = new Handler();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mPopup = new PopupWindow(context, attributeSet, n);
        this.mPopup.setInputMethodMode(1);
        context = this.mContext.getResources().getConfiguration().locale;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int buildDropDown() {
        boolean bl;
        Object object;
        int n = 0;
        int n2 = 0;
        if (this.mDropDownList == null) {
            Context context = this.mContext;
            this.mShowDropDownRunnable = new Runnable(){

                public void run() {
                    View view = ListPopupWindow.this.getAnchorView();
                    if (view != null && view.getWindowToken() != null) {
                        ListPopupWindow.this.show();
                    }
                }
            };
            bl = !this.mModal;
            this.mDropDownList = new DropDownListView(context, bl);
            if (this.mDropDownListHighlight != null) {
                this.mDropDownList.setSelector(this.mDropDownListHighlight);
            }
            this.mDropDownList.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                public void onItemSelected(AdapterView<?> object, View view, int n, long l) {
                    if (n != -1 && (object = ListPopupWindow.this.mDropDownList) != null) {
                        ((DropDownListView)((Object)object)).mListSelectionHidden = false;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.mDropDownList.setOnScrollListener((AbsListView.OnScrollListener)this.mScrollListener);
            if (this.mItemSelectedListener != null) {
                this.mDropDownList.setOnItemSelectedListener(this.mItemSelectedListener);
            }
            DropDownListView dropDownListView = this.mDropDownList;
            View view = this.mPromptView;
            object = dropDownListView;
            if (view != null) {
                object = new LinearLayout(context);
                object.setOrientation(1);
                context = new LinearLayout.LayoutParams(-1, 0, 1.0f);
                switch (this.mPromptPosition) {
                    default: {
                        Log.e((String)"ListPopupWindow", (String)("Invalid hint position " + this.mPromptPosition));
                        break;
                    }
                    case 1: {
                        object.addView((View)dropDownListView, (ViewGroup.LayoutParams)context);
                        object.addView(view);
                        break;
                    }
                    case 0: {
                        object.addView(view);
                        object.addView((View)dropDownListView, (ViewGroup.LayoutParams)context);
                        break;
                    }
                }
                view.measure(View.MeasureSpec.makeMeasureSpec((int)this.mDropDownWidth, (int)Integer.MIN_VALUE), 0);
                dropDownListView = (LinearLayout.LayoutParams)view.getLayoutParams();
                n2 = view.getMeasuredHeight() + dropDownListView.topMargin + dropDownListView.bottomMargin;
            }
            this.mPopup.setContentView((View)object);
        } else {
            object = (ViewGroup)this.mPopup.getContentView();
            object = this.mPromptView;
            n2 = n;
            if (object != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)object.getLayoutParams();
                n2 = object.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
        }
        int n3 = 0;
        object = this.mPopup.getBackground();
        if (object != null) {
            object.getPadding(this.mTempRect);
            n3 = n = this.mTempRect.top + this.mTempRect.bottom;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = - this.mTempRect.top;
                n3 = n;
            }
        } else {
            this.mTempRect.setEmpty();
        }
        bl = this.mPopup.getInputMethodMode() == 2;
        int n4 = this.getMaxAvailableHeight(this.getAnchorView(), this.mDropDownVerticalOffset, bl);
        if (this.mDropDownAlwaysVisible || this.mDropDownHeight == -1) {
            return n4 + n3;
        }
        switch (this.mDropDownWidth) {
            default: {
                n = View.MeasureSpec.makeMeasureSpec((int)this.mDropDownWidth, (int)1073741824);
                break;
            }
            case -2: {
                n = View.MeasureSpec.makeMeasureSpec((int)(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right)), (int)Integer.MIN_VALUE);
                break;
            }
            case -1: {
                n = View.MeasureSpec.makeMeasureSpec((int)(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right)), (int)1073741824);
            }
        }
        n4 = this.mDropDownList.measureHeightOfChildrenCompat(n, 0, -1, n4 - n2, -1);
        n = n2;
        if (n4 > 0) {
            n = n2 + n3;
        }
        return n4 + n;
    }

    private void removePromptView() {
        ViewParent viewParent;
        if (this.mPromptView != null && (viewParent = this.mPromptView.getParent()) instanceof ViewGroup) {
            ((ViewGroup)viewParent).removeView(this.mPromptView);
        }
    }

    public void clearListSelection() {
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.mListSelectionHidden = true;
            dropDownListView.requestLayout();
        }
    }

    public void dismiss() {
        this.mPopup.dismiss();
        this.removePromptView();
        this.mPopup.setContentView(null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks((Runnable)this.mResizePopupRunnable);
    }

    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public int getHeight() {
        return this.mDropDownHeight;
    }

    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }

    public ListView getListView() {
        return this.mDropDownList;
    }

    public int getMaxAvailableHeight(View view, int n, boolean bl) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        int n2 = rect.bottom;
        if (bl) {
            n2 = view.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        n = n2 = Math.max((int)(n2 - (arrn[1] + view.getHeight()) - n), (int)(arrn[1] - rect.top + n));
        if (this.mPopup.getBackground() != null) {
            this.mPopup.getBackground().getPadding(this.mTempRect);
            n = n2 - (this.mTempRect.top + this.mTempRect.bottom);
        }
        return n;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    public Object getSelectedItem() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }

    public long getSelectedItemId() {
        if (!this.isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }

    public int getSelectedItemPosition() {
        if (!this.isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }

    public View getSelectedView() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }

    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }

    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public boolean isInputMethodNotNeeded() {
        if (this.mPopup.getInputMethodMode() == 2) {
            return true;
        }
        return false;
    }

    public boolean isModal() {
        return this.mModal;
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (!this.isShowing()) return false;
        if (n == 62) return false;
        if (this.mDropDownList.getSelectedItemPosition() < 0) {
            if (n == 66) return false;
            if (n == 23) return false;
        }
        int n2 = this.mDropDownList.getSelectedItemPosition();
        boolean bl = !this.mPopup.isAboveAnchor();
        ListAdapter listAdapter = this.mAdapter;
        int n3 = Integer.MAX_VALUE;
        int n4 = Integer.MIN_VALUE;
        if (listAdapter != null) {
            boolean bl2 = listAdapter.areAllItemsEnabled();
            n3 = bl2 ? 0 : this.mDropDownList.lookForSelectablePosition(0, true);
            n4 = bl2 ? listAdapter.getCount() - 1 : this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
        }
        if (bl && n == 19 && n2 <= n3 || !bl && n == 20 && n2 >= n4) {
            this.clearListSelection();
            this.mPopup.setInputMethodMode(1);
            this.show();
            return true;
        }
        this.mDropDownList.mListSelectionHidden = false;
        if (this.mDropDownList.onKeyDown(n, keyEvent)) {
            this.mPopup.setInputMethodMode(2);
            this.mDropDownList.requestFocusFromTouch();
            this.show();
            switch (n) {
                case 19: 
                case 20: 
                case 23: 
                case 66: {
                    return true;
                }
            }
            return false;
        }
        if (bl && n == 20) {
            if (n2 != n4) return false;
            return true;
        }
        if (bl) return false;
        if (n != 19) return false;
        if (n2 != n3) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (!this.isShowing() || this.mDropDownList.getSelectedItemPosition() < 0) return false;
        boolean bl = this.mDropDownList.onKeyUp(n, keyEvent);
        if (!bl) return bl;
        switch (n) {
            default: {
                return bl;
            }
            case 23: 
            case 66: 
        }
        this.dismiss();
        return bl;
    }

    public boolean performItemClick(int n) {
        if (this.isShowing()) {
            if (this.mItemClickListener != null) {
                DropDownListView dropDownListView = this.mDropDownList;
                View view = dropDownListView.getChildAt(n - dropDownListView.getFirstVisiblePosition());
                ListAdapter listAdapter = dropDownListView.getAdapter();
                this.mItemClickListener.onItemClick((AdapterView)dropDownListView, view, n, listAdapter.getItemId(n));
            }
            return true;
        }
        return false;
    }

    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setAdapter(ListAdapter listAdapter) {
        if (this.mObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
        }
        this.mAdapter = listAdapter;
        if (this.mAdapter != null) {
            listAdapter.registerDataSetObserver(this.mObserver);
        }
        if (this.mDropDownList != null) {
            this.mDropDownList.setAdapter(this.mAdapter);
        }
    }

    public void setAnchorView(View view) {
        this.mDropDownAnchorView = view;
    }

    public void setAnimationStyle(int n) {
        this.mPopup.setAnimationStyle(n);
    }

    public void setBackgroundDrawable(Drawable drawable2) {
        this.mPopup.setBackgroundDrawable(drawable2);
    }

    public void setContentWidth(int n) {
        Drawable drawable2 = this.mPopup.getBackground();
        if (drawable2 != null) {
            drawable2.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + n;
            return;
        }
        this.setWidth(n);
    }

    public void setDropDownAlwaysVisible(boolean bl) {
        this.mDropDownAlwaysVisible = bl;
    }

    public void setForceIgnoreOutsideTouch(boolean bl) {
        this.mForceIgnoreOutsideTouch = bl;
    }

    public void setHeight(int n) {
        this.mDropDownHeight = n;
    }

    public void setHorizontalOffset(int n) {
        this.mDropDownHorizontalOffset = n;
    }

    public void setInputMethodMode(int n) {
        this.mPopup.setInputMethodMode(n);
    }

    void setListItemExpandMax(int n) {
        this.mListItemExpandMaximum = n;
    }

    public void setListSelector(Drawable drawable2) {
        this.mDropDownListHighlight = drawable2;
    }

    public void setModal(boolean bl) {
        this.mModal = true;
        this.mPopup.setFocusable(bl);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mPopup.setOnDismissListener(onDismissListener);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mItemSelectedListener = onItemSelectedListener;
    }

    public void setPromptPosition(int n) {
        this.mPromptPosition = n;
    }

    public void setPromptView(View view) {
        boolean bl = this.isShowing();
        if (bl) {
            this.removePromptView();
        }
        this.mPromptView = view;
        if (bl) {
            this.show();
        }
    }

    public void setSelection(int n) {
        DropDownListView dropDownListView = this.mDropDownList;
        if (this.isShowing() && dropDownListView != null) {
            dropDownListView.mListSelectionHidden = false;
            dropDownListView.setSelection(n);
            if (dropDownListView.getChoiceMode() != 0) {
                dropDownListView.setItemChecked(n, true);
            }
        }
    }

    public void setSoftInputMode(int n) {
        this.mPopup.setSoftInputMode(n);
    }

    public void setVerticalOffset(int n) {
        this.mDropDownVerticalOffset = n;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setWidth(int n) {
        this.mDropDownWidth = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void show() {
        boolean bl = true;
        boolean bl2 = false;
        int n = -1;
        int n2 = this.buildDropDown();
        int n3 = 0;
        int n4 = 0;
        boolean bl3 = this.isInputMethodNotNeeded();
        if (this.mPopup.isShowing()) {
            PopupWindow popupWindow;
            n3 = this.mDropDownWidth == -1 ? -1 : (this.mDropDownWidth == -2 ? this.getAnchorView().getWidth() : this.mDropDownWidth);
            if (this.mDropDownHeight == -1) {
                if (!bl3) {
                    n2 = -1;
                }
                if (bl3) {
                    popupWindow = this.mPopup;
                    if (this.mDropDownWidth != -1) {
                        n = 0;
                    }
                    popupWindow.setWindowLayoutMode(n, 0);
                } else {
                    popupWindow = this.mPopup;
                    n = this.mDropDownWidth == -1 ? -1 : 0;
                    popupWindow.setWindowLayoutMode(n, -1);
                }
            } else if (this.mDropDownHeight != -2) {
                n2 = this.mDropDownHeight;
            }
            popupWindow = this.mPopup;
            bl = bl2;
            if (!this.mForceIgnoreOutsideTouch) {
                bl = bl2;
                if (!this.mDropDownAlwaysVisible) {
                    bl = true;
                }
            }
            popupWindow.setOutsideTouchable(bl);
            this.mPopup.update(this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, n3, n2);
            return;
        } else {
            if (this.mDropDownWidth == -1) {
                n3 = -1;
            } else if (this.mDropDownWidth == -2) {
                this.mPopup.setWidth(this.getAnchorView().getWidth());
            } else {
                this.mPopup.setWidth(this.mDropDownWidth);
            }
            if (this.mDropDownHeight == -1) {
                n2 = -1;
            } else if (this.mDropDownHeight == -2) {
                this.mPopup.setHeight(n2);
                n2 = n4;
            } else {
                this.mPopup.setHeight(this.mDropDownHeight);
                n2 = n4;
            }
            this.mPopup.setWindowLayoutMode(n3, n2);
            PopupWindow popupWindow = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
                bl = false;
            }
            popupWindow.setOutsideTouchable(bl);
            this.mPopup.setTouchInterceptor((View.OnTouchListener)this.mTouchInterceptor);
            this.mPopup.showAsDropDown(this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset);
            this.mDropDownList.setSelection(-1);
            if (!this.mModal || this.mDropDownList.isInTouchMode()) {
                this.clearListSelection();
            }
            if (this.mModal) return;
            {
                this.mHandler.post((Runnable)this.mHideSelector);
                return;
            }
        }
    }

    private static class DropDownListView
    extends ListView {
        public static final int INVALID_POSITION = -1;
        static final int NO_POSITION = -1;
        private static final String TAG = "ListPopupWindow.DropDownListView";
        private boolean mHijackFocus;
        private boolean mListSelectionHidden;

        public DropDownListView(Context context, boolean bl) {
            super(context, null, R.attr.dropDownListViewStyle);
            this.mHijackFocus = bl;
            this.setCacheColorHint(0);
        }

        /*
         * Enabled aggressive block sorting
         */
        private int lookForSelectablePosition(int n, boolean bl) {
            ListAdapter listAdapter = this.getAdapter();
            if (listAdapter == null || this.isInTouchMode()) return -1;
            int n2 = listAdapter.getCount();
            if (!this.getAdapter().areAllItemsEnabled()) {
                int n3;
                if (bl) {
                    n = Math.max((int)0, (int)n);
                    do {
                        n3 = n;
                        if (n < n2) {
                            n3 = n;
                            if (!listAdapter.isEnabled(n)) {
                                ++n;
                                continue;
                            }
                        }
                        break;
                    } while (true);
                } else {
                    n = Math.min((int)n, (int)(n2 - 1));
                    do {
                        n3 = n;
                        if (n < 0) break;
                        n3 = n;
                        if (listAdapter.isEnabled(n)) break;
                        --n;
                    } while (true);
                }
                if (n3 < 0 || n3 >= n2) return -1;
                return n3;
            }
            if (n >= 0 && n < n2) return n;
            return -1;
        }

        public boolean hasFocus() {
            if (this.mHijackFocus || super.hasFocus()) {
                return true;
            }
            return false;
        }

        public boolean hasWindowFocus() {
            if (this.mHijackFocus || super.hasWindowFocus()) {
                return true;
            }
            return false;
        }

        public boolean isFocused() {
            if (this.mHijackFocus || super.isFocused()) {
                return true;
            }
            return false;
        }

        public boolean isInTouchMode() {
            if (this.mHijackFocus && this.mListSelectionHidden || super.isInTouchMode()) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        final int measureHeightOfChildrenCompat(int n, int n2, int n3, int n4, int n5) {
            n2 = this.getListPaddingTop();
            n3 = this.getListPaddingBottom();
            this.getListPaddingLeft();
            this.getListPaddingRight();
            int n6 = this.getDividerHeight();
            Drawable drawable2 = this.getDivider();
            ListAdapter listAdapter = this.getAdapter();
            if (listAdapter == null) {
                n2 += n3;
                return n2;
            }
            n3 = n2 + n3;
            if (n6 <= 0 || drawable2 == null) {
                n6 = 0;
            }
            n2 = 0;
            drawable2 = null;
            int n7 = 0;
            int n8 = listAdapter.getCount();
            int n9 = 0;
            while (n9 < n8) {
                ViewGroup.LayoutParams layoutParams;
                int n10 = listAdapter.getItemViewType(n9);
                int n11 = n7;
                if (n10 != n7) {
                    drawable2 = null;
                    n11 = n10;
                }
                n7 = (layoutParams = (drawable2 = listAdapter.getView(n9, (View)drawable2, (ViewGroup)this)).getLayoutParams()) != null && layoutParams.height > 0 ? View.MeasureSpec.makeMeasureSpec((int)layoutParams.height, (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                drawable2.measure(n, n7);
                n7 = n3;
                if (n9 > 0) {
                    n7 = n3 + n6;
                }
                if ((n3 = n7 + drawable2.getMeasuredHeight()) >= n4) {
                    if (n5 < 0) return n4;
                    if (n9 <= n5) return n4;
                    if (n2 <= 0) return n4;
                    if (n3 != n4) return n2;
                    return n4;
                }
                n7 = n2;
                if (n5 >= 0) {
                    n7 = n2;
                    if (n9 >= n5) {
                        n7 = n3;
                    }
                }
                ++n9;
                n2 = n7;
                n7 = n11;
            }
            return n3;
        }
    }

    private class ListSelectorHider
    implements Runnable {
        private ListSelectorHider() {
        }

        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    private class PopupDataSetObserver
    extends DataSetObserver {
        private PopupDataSetObserver() {
        }

        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    private class PopupScrollListener
    implements AbsListView.OnScrollListener {
        private PopupScrollListener() {
        }

        public void onScroll(AbsListView absListView, int n, int n2, int n3) {
        }

        public void onScrollStateChanged(AbsListView absListView, int n) {
            if (n == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
                ListPopupWindow.this.mHandler.removeCallbacks((Runnable)ListPopupWindow.this.mResizePopupRunnable);
                ListPopupWindow.this.mResizePopupRunnable.run();
            }
        }
    }

    private class PopupTouchInterceptor
    implements View.OnTouchListener {
        private PopupTouchInterceptor() {
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int n = motionEvent.getAction();
            int n2 = (int)motionEvent.getX();
            int n3 = (int)motionEvent.getY();
            if (n == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && n2 >= 0 && n2 < ListPopupWindow.this.mPopup.getWidth() && n3 >= 0 && n3 < ListPopupWindow.this.mPopup.getHeight()) {
                ListPopupWindow.this.mHandler.postDelayed((Runnable)ListPopupWindow.this.mResizePopupRunnable, 250L);
                return false;
            }
            if (n != 1) return false;
            ListPopupWindow.this.mHandler.removeCallbacks((Runnable)ListPopupWindow.this.mResizePopupRunnable);
            return false;
        }
    }

    private class ResizePopupRunnable
    implements Runnable {
        private ResizePopupRunnable() {
        }

        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
                ListPopupWindow.this.mPopup.setInputMethodMode(2);
                ListPopupWindow.this.show();
            }
        }
    }

}

