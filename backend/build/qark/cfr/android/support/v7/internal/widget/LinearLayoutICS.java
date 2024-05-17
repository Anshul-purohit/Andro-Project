/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LinearLayoutICS
extends LinearLayout {
    private static final int SHOW_DIVIDER_BEGINNING = 1;
    private static final int SHOW_DIVIDER_END = 4;
    private static final int SHOW_DIVIDER_MIDDLE = 2;
    private static final int SHOW_DIVIDER_NONE = 0;
    private final Drawable mDivider;
    private final int mDividerHeight;
    private final int mDividerPadding;
    private final int mDividerWidth;
    private final int mShowDividers;

    /*
     * Enabled aggressive block sorting
     */
    public LinearLayoutICS(Context context, AttributeSet attributeSet) {
        boolean bl = true;
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutICS);
        this.mDivider = context.getDrawable(0);
        if (this.mDivider != null) {
            this.mDividerWidth = this.mDivider.getIntrinsicWidth();
            this.mDividerHeight = this.mDivider.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        this.mShowDividers = context.getInt(1, 0);
        this.mDividerPadding = context.getDimensionPixelSize(2, 0);
        context.recycle();
        if (this.mDivider != null) {
            bl = false;
        }
        this.setWillNotDraw(bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    void drawSupportDividersHorizontal(Canvas canvas) {
        View view;
        int n;
        int n2 = this.getChildCount();
        for (n = 0; n < n2; ++n) {
            view = this.getChildAt(n);
            if (view == null || view.getVisibility() == 8 || !this.hasSupportDividerBeforeChildAt(n)) continue;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
            this.drawSupportVerticalDivider(canvas, view.getLeft() - layoutParams.leftMargin);
        }
        if (this.hasSupportDividerBeforeChildAt(n2)) {
            view = this.getChildAt(n2 - 1);
            n = view == null ? this.getWidth() - this.getPaddingRight() - this.mDividerWidth : view.getRight();
            this.drawSupportVerticalDivider(canvas, n);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void drawSupportDividersVertical(Canvas canvas) {
        View view;
        int n;
        int n2 = this.getChildCount();
        for (n = 0; n < n2; ++n) {
            view = this.getChildAt(n);
            if (view == null || view.getVisibility() == 8 || !this.hasSupportDividerBeforeChildAt(n)) continue;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
            this.drawSupportHorizontalDivider(canvas, view.getTop() - layoutParams.topMargin);
        }
        if (this.hasSupportDividerBeforeChildAt(n2)) {
            view = this.getChildAt(n2 - 1);
            n = view == null ? this.getHeight() - this.getPaddingBottom() - this.mDividerHeight : view.getBottom();
            this.drawSupportHorizontalDivider(canvas, n);
        }
    }

    void drawSupportHorizontalDivider(Canvas canvas, int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }

    void drawSupportVerticalDivider(Canvas canvas, int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public int getSupportDividerWidth() {
        return this.mDividerWidth;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean hasSupportDividerBeforeChildAt(int n) {
        if (n == 0) {
            if ((this.mShowDividers & 1) == 0) return false;
            return true;
        }
        if (n == this.getChildCount()) {
            if ((this.mShowDividers & 4) != 0) return true;
            return false;
        }
        if ((this.mShowDividers & 2) == 0) return false;
        boolean bl = false;
        --n;
        do {
            boolean bl2 = bl;
            if (n < 0) return bl2;
            if (this.getChildAt(n).getVisibility() != 8) {
                return true;
            }
            --n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void measureChildWithMargins(View view, int n, int n2, int n3, int n4) {
        if (this.mDivider != null) {
            int n5 = this.indexOfChild(view);
            int n6 = this.getChildCount();
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
            if (this.getOrientation() == 1) {
                if (this.hasSupportDividerBeforeChildAt(n5)) {
                    layoutParams.topMargin = this.mDividerHeight;
                } else if (n5 == n6 - 1 && this.hasSupportDividerBeforeChildAt(n6)) {
                    layoutParams.bottomMargin = this.mDividerHeight;
                }
            } else if (this.hasSupportDividerBeforeChildAt(n5)) {
                layoutParams.leftMargin = this.mDividerWidth;
            } else if (n5 == n6 - 1 && this.hasSupportDividerBeforeChildAt(n6)) {
                layoutParams.rightMargin = this.mDividerWidth;
            }
        }
        super.measureChildWithMargins(view, n, n2, n3, n4);
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.getOrientation() == 1) {
            this.drawSupportDividersVertical(canvas);
            return;
        }
        this.drawSupportDividersHorizontal(canvas);
    }
}

