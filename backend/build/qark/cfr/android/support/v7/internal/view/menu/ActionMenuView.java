/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewDebug
 *  android.view.ViewDebug$ExportedProperty
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

public class ActionMenuView
extends LinearLayoutICS
implements MenuBuilder.ItemInvoker,
MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private int mMaxItemHeight;
    private int mMeasuredExtraWidth;
    private MenuBuilder mMenu;
    private int mMinCellSize;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setBaselineAligned(false);
        float f = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * f);
        this.mGeneratedItemPadding = (int)(4.0f * f);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        this.mMaxItemHeight = context.getDimensionPixelSize(1, 0);
        context.recycle();
    }

    /*
     * Enabled aggressive block sorting
     */
    static int measureChildForCells(View view, int n, int n2, int n3, int n4) {
        int n5;
        block6 : {
            int n6;
            block7 : {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                n5 = View.MeasureSpec.makeMeasureSpec((int)(View.MeasureSpec.getSize((int)n3) - n4), (int)View.MeasureSpec.getMode((int)n3));
                ActionMenuItemView actionMenuItemView = view instanceof ActionMenuItemView ? (ActionMenuItemView)view : null;
                n4 = actionMenuItemView != null && actionMenuItemView.hasText() ? 1 : 0;
                n3 = n6 = 0;
                if (n2 <= 0) break block6;
                if (n4 == 0) break block7;
                n3 = n6;
                if (n2 < 2) break block6;
            }
            view.measure(View.MeasureSpec.makeMeasureSpec((int)(n * n2), (int)Integer.MIN_VALUE), n5);
            n6 = view.getMeasuredWidth();
            n2 = n3 = n6 / n;
            if (n6 % n != 0) {
                n2 = n3 + 1;
            }
            n3 = n2;
            if (n4 != 0) {
                n3 = n2;
                if (n2 < 2) {
                    n3 = 2;
                }
            }
        }
        boolean bl = !layoutParams.isOverflowButton && n4 != 0;
        layoutParams.expandable = bl;
        layoutParams.cellsUsed = n3;
        view.measure(View.MeasureSpec.makeMeasureSpec((int)(n3 * n), (int)1073741824), n5);
        return n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onMeasureExactFormat(int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        block39 : {
            int n9;
            int n10;
            Object object;
            long l;
            LayoutParams layoutParams;
            int n11;
            int n12;
            int n13;
            int n14;
            n5 = View.MeasureSpec.getMode((int)n2);
            n = View.MeasureSpec.getSize((int)n);
            n8 = View.MeasureSpec.getSize((int)n2);
            n2 = this.getPaddingLeft();
            n3 = this.getPaddingRight();
            int n15 = this.getPaddingTop() + this.getPaddingBottom();
            int n16 = n5 == 1073741824 ? View.MeasureSpec.makeMeasureSpec((int)(n8 - n15), (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)Math.min((int)this.mMaxItemHeight, (int)(n8 - n15)), (int)Integer.MIN_VALUE);
            n7 = n - (n2 + n3);
            n2 = n7 / this.mMinCellSize;
            n = this.mMinCellSize;
            if (n2 == 0) {
                this.setMeasuredDimension(n7, 0);
                return;
            }
            n6 = this.mMinCellSize + n7 % n / n2;
            n3 = 0;
            int n17 = 0;
            int n18 = 0;
            int n19 = 0;
            n4 = 0;
            long l2 = 0L;
            int n20 = this.getChildCount();
            for (n14 = 0; n14 < n20; ++n14) {
                object = this.getChildAt(n14);
                if (object.getVisibility() == 8) {
                    l = l2;
                    n13 = n4;
                } else {
                    boolean bl = object instanceof ActionMenuItemView;
                    n11 = n19 + 1;
                    if (bl) {
                        object.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                    }
                    layoutParams = (LayoutParams)object.getLayoutParams();
                    layoutParams.expanded = false;
                    layoutParams.extraPixels = 0;
                    layoutParams.cellsUsed = 0;
                    layoutParams.expandable = false;
                    layoutParams.leftMargin = 0;
                    layoutParams.rightMargin = 0;
                    bl = bl && ((ActionMenuItemView)object).hasText();
                    layoutParams.preventEdgeOffset = bl;
                    n = layoutParams.isOverflowButton ? 1 : n2;
                    int n21 = ActionMenuView.measureChildForCells((View)object, n6, n, n16, n15);
                    n9 = Math.max((int)n17, (int)n21);
                    n = n18;
                    if (layoutParams.expandable) {
                        n = n18 + 1;
                    }
                    if (layoutParams.isOverflowButton) {
                        n4 = 1;
                    }
                    n10 = n2 - n21;
                    n12 = Math.max((int)n3, (int)object.getMeasuredHeight());
                    n2 = n10;
                    n18 = n;
                    n13 = n4;
                    n17 = n9;
                    n3 = n12;
                    l = l2;
                    n19 = n11;
                    if (n21 == 1) {
                        l = l2 | (long)(1 << n14);
                        n2 = n10;
                        n18 = n;
                        n13 = n4;
                        n17 = n9;
                        n3 = n12;
                        n19 = n11;
                    }
                }
                n4 = n13;
                l2 = l;
            }
            n13 = n4 != 0 && n19 == 2 ? 1 : 0;
            n = 0;
            do {
                long l3;
                block41 : {
                    block42 : {
                        float f;
                        block43 : {
                            block40 : {
                                l = l2;
                                if (n18 <= 0) break block40;
                                l = l2;
                                if (n2 <= 0) break block40;
                                n11 = Integer.MAX_VALUE;
                                l3 = 0L;
                                n12 = 0;
                                for (n9 = 0; n9 < n20; ++n9) {
                                    object = (LayoutParams)this.getChildAt(n9).getLayoutParams();
                                    if (!object.expandable) {
                                        n14 = n12;
                                        l = l3;
                                        n10 = n11;
                                    } else if (object.cellsUsed < n11) {
                                        n10 = object.cellsUsed;
                                        l = 1 << n9;
                                        n14 = 1;
                                    } else {
                                        n10 = n11;
                                        l = l3;
                                        n14 = n12;
                                        if (object.cellsUsed == n11) {
                                            l = l3 | (long)(1 << n9);
                                            n14 = n12 + 1;
                                            n10 = n11;
                                        }
                                    }
                                    n11 = n10;
                                    l3 = l;
                                    n12 = n14;
                                }
                                l2 |= l3;
                                if (n12 <= n2) break block41;
                                l = l2;
                            }
                            n14 = n4 == 0 && n19 == 1 ? 1 : 0;
                            n4 = n2;
                            n18 = n;
                            if (n2 <= 0) break block42;
                            n4 = n2;
                            n18 = n;
                            if (l == 0L) break block42;
                            if (n2 < n19 - 1 || n14 != 0) break block43;
                            n4 = n2;
                            n18 = n;
                            if (n17 <= true) break block42;
                        }
                        float f2 = f = (float)Long.bitCount((long)l);
                        if (n14 == 0) {
                            float f3 = f;
                            if ((1L & l) != 0L) {
                                f3 = f;
                                if (!((LayoutParams)this.getChildAt((int)0).getLayoutParams()).preventEdgeOffset) {
                                    f3 = f - 0.5f;
                                }
                            }
                            f2 = f3;
                            if (((long)(1 << n20 - 1) & l) != 0L) {
                                f2 = f3;
                                if (!((LayoutParams)this.getChildAt((int)(n20 - 1)).getLayoutParams()).preventEdgeOffset) {
                                    f2 = f3 - 0.5f;
                                }
                            }
                        }
                        n4 = f2 > 0.0f ? (int)((float)(n2 * n6) / f2) : 0;
                        for (n18 = 0; n18 < n20; ++n18) {
                            if (((long)(1 << n18) & l) == 0L) {
                                n2 = n;
                            } else {
                                object = this.getChildAt(n18);
                                layoutParams = (LayoutParams)object.getLayoutParams();
                                if (object instanceof ActionMenuItemView) {
                                    layoutParams.extraPixels = n4;
                                    layoutParams.expanded = true;
                                    if (n18 == 0 && !layoutParams.preventEdgeOffset) {
                                        layoutParams.leftMargin = (- n4) / 2;
                                    }
                                    n2 = 1;
                                } else if (layoutParams.isOverflowButton) {
                                    layoutParams.extraPixels = n4;
                                    layoutParams.expanded = true;
                                    layoutParams.rightMargin = (- n4) / 2;
                                    n2 = 1;
                                } else {
                                    if (n18 != 0) {
                                        layoutParams.leftMargin = n4 / 2;
                                    }
                                    n2 = n;
                                    if (n18 != n20 - 1) {
                                        layoutParams.rightMargin = n4 / 2;
                                        n2 = n;
                                    }
                                }
                            }
                            n = n2;
                        }
                        n4 = 0;
                        n18 = n;
                    }
                    if (n18 != 0) {
                        break;
                    }
                    break block39;
                }
                for (n = 0; n < n20; ++n) {
                    object = this.getChildAt(n);
                    layoutParams = (LayoutParams)object.getLayoutParams();
                    if (((long)(1 << n) & l3) == 0L) {
                        n14 = n2;
                        l = l2;
                        if (layoutParams.cellsUsed == n11 + 1) {
                            l = l2 | (long)(1 << n);
                            n14 = n2;
                        }
                    } else {
                        if (n13 != 0 && layoutParams.preventEdgeOffset && n2 == 1) {
                            object.setPadding(this.mGeneratedItemPadding + n6, 0, this.mGeneratedItemPadding, 0);
                        }
                        ++layoutParams.cellsUsed;
                        layoutParams.expanded = true;
                        n14 = n2 - 1;
                        l = l2;
                    }
                    n2 = n14;
                    l2 = l;
                }
                n = 1;
            } while (true);
            for (n = 0; n < n20; ++n) {
                object = this.getChildAt(n);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (!layoutParams.expanded) continue;
                object.measure(View.MeasureSpec.makeMeasureSpec((int)(layoutParams.cellsUsed * n6 + layoutParams.extraPixels), (int)1073741824), n16);
            }
        }
        n = n8;
        if (n5 != 1073741824) {
            n = n3;
        }
        this.setMeasuredDimension(n7, n);
        this.mMeasuredExtraWidth = n4 * n6;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams != null && layoutParams instanceof LayoutParams) {
            return true;
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        if (object instanceof LayoutParams) {
            object = new LayoutParams((LayoutParams)((Object)object));
            if (object.gravity <= 0) {
                object.gravity = 16;
            }
            return object;
        }
        return this.generateDefaultLayoutParams();
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams layoutParams = this.generateDefaultLayoutParams();
        layoutParams.isOverflowButton = true;
        return layoutParams;
    }

    @Override
    public int getWindowAnimations() {
        return 0;
    }

    @Override
    protected boolean hasSupportDividerBeforeChildAt(int n) {
        boolean bl;
        View view = this.getChildAt(n - 1);
        View view2 = this.getChildAt(n);
        boolean bl2 = bl = false;
        if (n < this.getChildCount()) {
            bl2 = bl;
            if (view instanceof ActionMenuChildView) {
                bl2 = false | ((ActionMenuChildView)view).needsDividerAfter();
            }
        }
        bl = bl2;
        if (n > 0) {
            bl = bl2;
            if (view2 instanceof ActionMenuChildView) {
                bl = bl2 | ((ActionMenuChildView)view2).needsDividerBefore();
            }
        }
        return bl;
    }

    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    @Override
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    public boolean isExpandedFormat() {
        return this.mFormatItems;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 8) {
            super.onConfigurationChanged(configuration);
        }
        this.mPresenter.updateMenuView(false);
        if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowing()) {
            this.mPresenter.hideOverflowMenu();
            this.mPresenter.showOverflowMenu();
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.dismissPopupMenus();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams;
        View view;
        int n5;
        if (!this.mFormatItems) {
            super.onLayout(bl, n, n2, n3, n4);
            return;
        }
        int n6 = this.getChildCount();
        int n7 = (n2 + n4) / 2;
        int n8 = this.getSupportDividerWidth();
        int n9 = 0;
        n4 = 0;
        n2 = n3 - n - this.getPaddingRight() - this.getPaddingLeft();
        int n10 = 0;
        for (n5 = 0; n5 < n6; ++n5) {
            int n11;
            view = this.getChildAt(n5);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.isOverflowButton) {
                n10 = n11 = view.getMeasuredWidth();
                if (this.hasSupportDividerBeforeChildAt(n5)) {
                    n10 = n11 + n8;
                }
                n11 = view.getMeasuredHeight();
                int n12 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                int n13 = n7 - n11 / 2;
                view.layout(n12 - n10, n13, n12, n13 + n11);
                n2 -= n10;
                n10 = 1;
                continue;
            }
            n11 = view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            n11 = n2 - n11;
            n2 = n9 += n11;
            if (this.hasSupportDividerBeforeChildAt(n5)) {
                n2 = n9 + n8;
            }
            ++n4;
            n9 = n2;
            n2 = n11;
        }
        if (n6 == 1 && n10 == 0) {
            view = this.getChildAt(0);
            n2 = view.getMeasuredWidth();
            n4 = view.getMeasuredHeight();
            n = (n3 - n) / 2 - n2 / 2;
            n3 = n7 - n4 / 2;
            view.layout(n, n3, n + n2, n3 + n4);
            return;
        }
        n = n10 != 0 ? 0 : 1;
        n = (n = n4 - n) > 0 ? n2 / n : 0;
        n4 = Math.max((int)0, (int)n);
        n2 = this.getPaddingLeft();
        n = 0;
        while (n < n6) {
            view = this.getChildAt(n);
            layoutParams = (LayoutParams)view.getLayoutParams();
            n3 = n2;
            if (view.getVisibility() != 8) {
                if (layoutParams.isOverflowButton) {
                    n3 = n2;
                } else {
                    n3 = view.getMeasuredWidth();
                    n5 = view.getMeasuredHeight();
                    n9 = n7 - n5 / 2;
                    view.layout(n2, n9, (n2 += layoutParams.leftMargin) + n3, n9 + n5);
                    n3 = n2 + (layoutParams.rightMargin + n3 + n4);
                }
            }
            ++n;
            n2 = n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        boolean bl = this.mFormatItems;
        boolean bl2 = View.MeasureSpec.getMode((int)n) == 1073741824;
        this.mFormatItems = bl2;
        if (bl != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int n3 = View.MeasureSpec.getMode((int)n);
        if (this.mFormatItems && this.mMenu != null && n3 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = n3;
            this.mMenu.onItemsChanged(true);
        }
        if (this.mFormatItems) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        int n4 = this.getChildCount();
        n3 = 0;
        do {
            if (n3 >= n4) {
                super.onMeasure(n, n2);
                return;
            }
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(n3).getLayoutParams();
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
            ++n3;
        } while (true);
    }

    public void setOverflowReserved(boolean bl) {
        this.mReserveOverflow = bl;
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
    }

    public static interface ActionMenuChildView {
        public boolean needsDividerAfter();

        public boolean needsDividerBefore();
    }

    public static class LayoutParams
    extends LinearLayout.LayoutParams {
        @ViewDebug.ExportedProperty
        public int cellsUsed;
        @ViewDebug.ExportedProperty
        public boolean expandable;
        public boolean expanded;
        @ViewDebug.ExportedProperty
        public int extraPixels;
        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug.ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }

        public LayoutParams(int n, int n2, boolean bl) {
            super(n, n2);
            this.isOverflowButton = bl;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((LinearLayout.LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
    }

}

