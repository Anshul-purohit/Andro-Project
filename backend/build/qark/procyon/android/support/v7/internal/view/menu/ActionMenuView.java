// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.internal.view.menu;

import android.view.ViewDebug$ExportedProperty;
import android.os.Build$VERSION;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.widget.LinearLayout$LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.View;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.internal.widget.LinearLayoutICS;

public class ActionMenuView extends LinearLayoutICS implements ItemInvoker, MenuView
{
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
    
    public ActionMenuView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuView(final Context context, final AttributeSet set) {
        super(context, set);
        this.setBaselineAligned(false);
        final float density = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * density);
        this.mGeneratedItemPadding = (int)(4.0f * density);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        this.mMaxItemHeight = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        obtainStyledAttributes.recycle();
    }
    
    static int measureChildForCells(final View view, final int n, int n2, int cellsUsed, int n3) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(cellsUsed) - n3, View$MeasureSpec.getMode(cellsUsed));
        ActionMenuItemView actionMenuItemView;
        if (view instanceof ActionMenuItemView) {
            actionMenuItemView = (ActionMenuItemView)view;
        }
        else {
            actionMenuItemView = null;
        }
        if (actionMenuItemView != null && actionMenuItemView.hasText()) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        final int n4 = cellsUsed = 0;
        Label_0131: {
            if (n2 > 0) {
                if (n3 != 0) {
                    cellsUsed = n4;
                    if (n2 < 2) {
                        break Label_0131;
                    }
                }
                view.measure(View$MeasureSpec.makeMeasureSpec(n * n2, Integer.MIN_VALUE), measureSpec);
                final int measuredWidth = view.getMeasuredWidth();
                cellsUsed = (n2 = measuredWidth / n);
                if (measuredWidth % n != 0) {
                    n2 = cellsUsed + 1;
                }
                cellsUsed = n2;
                if (n3 != 0 && (cellsUsed = n2) < 2) {
                    cellsUsed = 2;
                }
            }
        }
        layoutParams.expandable = (!layoutParams.isOverflowButton && n3 != 0);
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(cellsUsed * n, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int paddingLeft) {
        final int mode = View$MeasureSpec.getMode(paddingLeft);
        i = View$MeasureSpec.getSize(i);
        final int size = View$MeasureSpec.getSize(paddingLeft);
        paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int n = this.getPaddingTop() + this.getPaddingBottom();
        int n2;
        if (mode == 1073741824) {
            n2 = View$MeasureSpec.makeMeasureSpec(size - n, 1073741824);
        }
        else {
            n2 = View$MeasureSpec.makeMeasureSpec(Math.min(this.mMaxItemHeight, size - n), Integer.MIN_VALUE);
        }
        final int n3 = i - (paddingLeft + paddingRight);
        paddingLeft = n3 / this.mMinCellSize;
        i = this.mMinCellSize;
        if (paddingLeft == 0) {
            this.setMeasuredDimension(n3, 0);
            return;
        }
        final int n4 = this.mMinCellSize + n3 % i / paddingLeft;
        int a = 0;
        int a2 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        long n8 = 0L;
        final int childCount = this.getChildCount();
        long n9;
        int n10;
        for (int j = 0; j < childCount; ++j, n7 = n10, n8 = n9) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() == 8) {
                n9 = n8;
                n10 = n7;
            }
            else {
                final boolean b = child instanceof ActionMenuItemView;
                final int n11 = n6 + 1;
                if (b) {
                    child.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = (b && ((ActionMenuItemView)child).hasText());
                if (layoutParams.isOverflowButton) {
                    i = 1;
                }
                else {
                    i = paddingLeft;
                }
                final int measureChildForCells = measureChildForCells(child, n4, i, n2, n);
                final int max = Math.max(a2, measureChildForCells);
                i = n5;
                if (layoutParams.expandable) {
                    i = n5 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    n7 = 1;
                }
                final int n12 = paddingLeft - measureChildForCells;
                final int max2 = Math.max(a, child.getMeasuredHeight());
                paddingLeft = n12;
                n5 = i;
                n10 = n7;
                a2 = max;
                a = max2;
                n9 = n8;
                n6 = n11;
                if (measureChildForCells == 1) {
                    n9 = (n8 | (long)(1 << j));
                    paddingLeft = n12;
                    n5 = i;
                    n10 = n7;
                    a2 = max;
                    a = max2;
                    n6 = n11;
                }
            }
        }
        final boolean b2 = n7 != 0 && n6 == 2;
        i = 0;
        long k;
        while (true) {
            k = n8;
            if (n5 <= 0) {
                break;
            }
            k = n8;
            if (paddingLeft <= 0) {
                break;
            }
            int n13 = Integer.MAX_VALUE;
            long n14 = 0L;
            int n15 = 0;
            int n16;
            long n17;
            int cellsUsed;
            for (int l = 0; l < childCount; ++l, n13 = cellsUsed, n14 = n17, n15 = n16) {
                final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(l).getLayoutParams();
                if (!layoutParams2.expandable) {
                    n16 = n15;
                    n17 = n14;
                    cellsUsed = n13;
                }
                else if (layoutParams2.cellsUsed < n13) {
                    cellsUsed = layoutParams2.cellsUsed;
                    n17 = 1 << l;
                    n16 = 1;
                }
                else {
                    cellsUsed = n13;
                    n17 = n14;
                    n16 = n15;
                    if (layoutParams2.cellsUsed == n13) {
                        n17 = (n14 | (long)(1 << l));
                        n16 = n15 + 1;
                        cellsUsed = n13;
                    }
                }
            }
            n8 |= n14;
            if (n15 > paddingLeft) {
                k = n8;
                break;
            }
            View child2;
            LayoutParams layoutParams3;
            int n18;
            long n19;
            for (i = 0; i < childCount; ++i, paddingLeft = n18, n8 = n19) {
                child2 = this.getChildAt(i);
                layoutParams3 = (LayoutParams)child2.getLayoutParams();
                if (((long)(1 << i) & n14) == 0x0L) {
                    n18 = paddingLeft;
                    n19 = n8;
                    if (layoutParams3.cellsUsed == n13 + 1) {
                        n19 = (n8 | (long)(1 << i));
                        n18 = paddingLeft;
                    }
                }
                else {
                    if (b2 && layoutParams3.preventEdgeOffset && paddingLeft == 1) {
                        child2.setPadding(this.mGeneratedItemPadding + n4, 0, this.mGeneratedItemPadding, 0);
                    }
                    ++layoutParams3.cellsUsed;
                    layoutParams3.expanded = true;
                    n18 = paddingLeft - 1;
                    n19 = n8;
                }
            }
            i = 1;
        }
        boolean b3;
        if (n7 == 0 && n6 == 1) {
            b3 = true;
        }
        else {
            b3 = false;
        }
        int n20 = paddingLeft;
        int n21 = i;
        Label_1201: {
            if (paddingLeft > 0) {
                n20 = paddingLeft;
                n21 = i;
                if (k != 0L) {
                    if (paddingLeft >= n6 - 1 && !b3) {
                        n20 = paddingLeft;
                        n21 = i;
                        if (a2 <= 1) {
                            break Label_1201;
                        }
                    }
                    float n23;
                    final float n22 = n23 = (float)Long.bitCount(k);
                    if (!b3) {
                        float n24 = n22;
                        if ((0x1L & k) != 0x0L) {
                            n24 = n22;
                            if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                                n24 = n22 - 0.5f;
                            }
                        }
                        n23 = n24;
                        if (((long)(1 << childCount - 1) & k) != 0x0L) {
                            n23 = n24;
                            if (!((LayoutParams)this.getChildAt(childCount - 1).getLayoutParams()).preventEdgeOffset) {
                                n23 = n24 - 0.5f;
                            }
                        }
                    }
                    int n25;
                    if (n23 > 0.0f) {
                        n25 = (int)(paddingLeft * n4 / n23);
                    }
                    else {
                        n25 = 0;
                    }
                    for (int n26 = 0; n26 < childCount; ++n26, i = paddingLeft) {
                        if (((long)(1 << n26) & k) == 0x0L) {
                            paddingLeft = i;
                        }
                        else {
                            final View child3 = this.getChildAt(n26);
                            final LayoutParams layoutParams4 = (LayoutParams)child3.getLayoutParams();
                            if (child3 instanceof ActionMenuItemView) {
                                layoutParams4.extraPixels = n25;
                                layoutParams4.expanded = true;
                                if (n26 == 0 && !layoutParams4.preventEdgeOffset) {
                                    layoutParams4.leftMargin = -n25 / 2;
                                }
                                paddingLeft = 1;
                            }
                            else if (layoutParams4.isOverflowButton) {
                                layoutParams4.extraPixels = n25;
                                layoutParams4.expanded = true;
                                layoutParams4.rightMargin = -n25 / 2;
                                paddingLeft = 1;
                            }
                            else {
                                if (n26 != 0) {
                                    layoutParams4.leftMargin = n25 / 2;
                                }
                                paddingLeft = i;
                                if (n26 != childCount - 1) {
                                    layoutParams4.rightMargin = n25 / 2;
                                    paddingLeft = i;
                                }
                            }
                        }
                    }
                    n20 = 0;
                    n21 = i;
                }
            }
        }
        if (n21 != 0) {
            View child4;
            LayoutParams layoutParams5;
            for (i = 0; i < childCount; ++i) {
                child4 = this.getChildAt(i);
                layoutParams5 = (LayoutParams)child4.getLayoutParams();
                if (layoutParams5.expanded) {
                    child4.measure(View$MeasureSpec.makeMeasureSpec(layoutParams5.cellsUsed * n4 + layoutParams5.extraPixels, 1073741824), n2);
                }
            }
        }
        i = size;
        if (mode != 1073741824) {
            i = a;
        }
        this.setMeasuredDimension(n3, i);
        this.mMeasuredExtraWidth = n20 * n4;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams != null && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return false;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            final LayoutParams layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            if (layoutParams.gravity <= 0) {
                layoutParams.gravity = 16;
            }
            return layoutParams;
        }
        return this.generateDefaultLayoutParams();
    }
    
    public LayoutParams generateOverflowButtonLayoutParams() {
        final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }
    
    @Override
    public int getWindowAnimations() {
        return 0;
    }
    
    @Override
    protected boolean hasSupportDividerBeforeChildAt(final int n) {
        final View child = this.getChildAt(n - 1);
        final View child2 = this.getChildAt(n);
        boolean b2;
        final boolean b = b2 = false;
        if (n < this.getChildCount()) {
            b2 = b;
            if (child instanceof ActionMenuChildView) {
                b2 = (false | ((ActionMenuChildView)child).needsDividerAfter());
            }
        }
        boolean b3 = b2;
        if (n > 0) {
            b3 = b2;
            if (child2 instanceof ActionMenuChildView) {
                b3 = (b2 | ((ActionMenuChildView)child2).needsDividerBefore());
            }
        }
        return b3;
    }
    
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    public boolean isExpandedFormat() {
        return this.mFormatItems;
    }
    
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (Build$VERSION.SDK_INT >= 8) {
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
    
    protected void onLayout(final boolean b, int i, int n, int measuredWidth, int n2) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, n, measuredWidth, n2);
        }
        else {
            final int childCount = this.getChildCount();
            final int n3 = (n + n2) / 2;
            final int supportDividerWidth = this.getSupportDividerWidth();
            int n4 = 0;
            n2 = 0;
            n = measuredWidth - i - this.getPaddingRight() - this.getPaddingLeft();
            boolean b2 = false;
            for (int j = 0; j < childCount; ++j) {
                final View child = this.getChildAt(j);
                if (child.getVisibility() != 8) {
                    final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                    if (layoutParams.isOverflowButton) {
                        int measuredWidth2;
                        final int n5 = measuredWidth2 = child.getMeasuredWidth();
                        if (this.hasSupportDividerBeforeChildAt(j)) {
                            measuredWidth2 = n5 + supportDividerWidth;
                        }
                        final int measuredHeight = child.getMeasuredHeight();
                        final int n6 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                        final int n7 = n3 - measuredHeight / 2;
                        child.layout(n6 - measuredWidth2, n7, n6, n7 + measuredHeight);
                        n -= measuredWidth2;
                        b2 = true;
                    }
                    else {
                        final int n8 = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                        final int n9 = n4 + n8;
                        final int n10 = n - n8;
                        n = n9;
                        if (this.hasSupportDividerBeforeChildAt(j)) {
                            n = n9 + supportDividerWidth;
                        }
                        ++n2;
                        n4 = n;
                        n = n10;
                    }
                }
            }
            if (childCount == 1 && !b2) {
                final View child2 = this.getChildAt(0);
                n = child2.getMeasuredWidth();
                n2 = child2.getMeasuredHeight();
                i = (measuredWidth - i) / 2 - n / 2;
                measuredWidth = n3 - n2 / 2;
                child2.layout(i, measuredWidth, i + n, measuredWidth + n2);
                return;
            }
            if (b2) {
                i = 0;
            }
            else {
                i = 1;
            }
            i = n2 - i;
            if (i > 0) {
                i = n / i;
            }
            else {
                i = 0;
            }
            n2 = Math.max(0, i);
            n = this.getPaddingLeft();
            View child3;
            LayoutParams layoutParams2;
            int measuredHeight2;
            int n11;
            for (i = 0; i < childCount; ++i, n = measuredWidth) {
                child3 = this.getChildAt(i);
                layoutParams2 = (LayoutParams)child3.getLayoutParams();
                measuredWidth = n;
                if (child3.getVisibility() != 8) {
                    if (layoutParams2.isOverflowButton) {
                        measuredWidth = n;
                    }
                    else {
                        n += layoutParams2.leftMargin;
                        measuredWidth = child3.getMeasuredWidth();
                        measuredHeight2 = child3.getMeasuredHeight();
                        n11 = n3 - measuredHeight2 / 2;
                        child3.layout(n, n11, n + measuredWidth, n11 + measuredHeight2);
                        measuredWidth = n + (layoutParams2.rightMargin + measuredWidth + n2);
                    }
                }
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final boolean mFormatItems = this.mFormatItems;
        this.mFormatItems = (View$MeasureSpec.getMode(n) == 1073741824);
        if (mFormatItems != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        final int mode = View$MeasureSpec.getMode(n);
        if (this.mFormatItems && this.mMenu != null && mode != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = mode;
            this.mMenu.onItemsChanged(true);
        }
        if (this.mFormatItems) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
        }
        super.onMeasure(n, n2);
    }
    
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
    }
    
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }
    
    public interface ActionMenuChildView
    {
        boolean needsDividerAfter();
        
        boolean needsDividerBefore();
    }
    
    public static class LayoutParams extends LinearLayout$LayoutParams
    {
        @ViewDebug$ExportedProperty
        public int cellsUsed;
        @ViewDebug$ExportedProperty
        public boolean expandable;
        public boolean expanded;
        @ViewDebug$ExportedProperty
        public int extraPixels;
        @ViewDebug$ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug$ExportedProperty
        public boolean preventEdgeOffset;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }
        
        public LayoutParams(final int n, final int n2, final boolean isOverflowButton) {
            super(n, n2);
            this.isOverflowButton = isOverflowButton;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((LinearLayout$LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
    }
}
