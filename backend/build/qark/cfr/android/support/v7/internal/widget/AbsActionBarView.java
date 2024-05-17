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
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.widget.ActionBarContainer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

abstract class AbsActionBarView
extends ViewGroup {
    private static final int FADE_DURATION = 200;
    protected ActionMenuPresenter mActionMenuPresenter;
    protected int mContentHeight;
    protected ActionMenuView mMenuView;
    protected boolean mSplitActionBar;
    protected ActionBarContainer mSplitView;
    protected boolean mSplitWhenNarrow;

    AbsActionBarView(Context context) {
        super(context);
    }

    AbsActionBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    AbsActionBarView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void animateToVisibility(int n) {
        this.clearAnimation();
        if (n != this.getVisibility()) {
            Context context = this.getContext();
            int n2 = n == 0 ? R.anim.abc_fade_in : R.anim.abc_fade_out;
            context = AnimationUtils.loadAnimation((Context)context, (int)n2);
            this.startAnimation((Animation)context);
            this.setVisibility(n);
            if (this.mSplitView != null && this.mMenuView != null) {
                this.mMenuView.startAnimation((Animation)context);
                this.mMenuView.setVisibility(n);
            }
        }
    }

    public void dismissPopupMenus() {
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
    }

    public int getAnimatedVisibility() {
        return this.getVisibility();
    }

    public int getContentHeight() {
        return this.mContentHeight;
    }

    public boolean hideOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    public boolean isOverflowReserved() {
        if (this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowReserved()) {
            return true;
        }
        return false;
    }

    protected int measureChildView(View view, int n, int n2, int n3) {
        view.measure(View.MeasureSpec.makeMeasureSpec((int)n, (int)Integer.MIN_VALUE), n2);
        return Math.max((int)0, (int)(n - view.getMeasuredWidth() - n3));
    }

    protected void onConfigurationChanged(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 8) {
            super.onConfigurationChanged(configuration);
        }
        TypedArray typedArray = this.getContext().obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        this.setContentHeight(typedArray.getLayoutDimension(1, 0));
        typedArray.recycle();
        if (this.mSplitWhenNarrow) {
            this.setSplitActionBar(this.getContext().getResources().getBoolean(R.bool.abc_split_action_bar_is_narrow));
        }
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.onConfigurationChanged(configuration);
        }
    }

    protected int positionChild(View view, int n, int n2, int n3) {
        int n4 = view.getMeasuredWidth();
        int n5 = view.getMeasuredHeight();
        view.layout(n, n2, n + n4, (n2 += (n3 - n5) / 2) + n5);
        return n4;
    }

    protected int positionChildInverse(View view, int n, int n2, int n3) {
        int n4 = view.getMeasuredWidth();
        int n5 = view.getMeasuredHeight();
        view.layout(n - n4, n2, n, (n2 += (n3 - n5) / 2) + n5);
        return n4;
    }

    public void postShowOverflowMenu() {
        this.post(new Runnable(){

            public void run() {
                AbsActionBarView.this.showOverflowMenu();
            }
        });
    }

    public void setContentHeight(int n) {
        this.mContentHeight = n;
        this.requestLayout();
    }

    public void setSplitActionBar(boolean bl) {
        this.mSplitActionBar = bl;
    }

    public void setSplitView(ActionBarContainer actionBarContainer) {
        this.mSplitView = actionBarContainer;
    }

    public void setSplitWhenNarrow(boolean bl) {
        this.mSplitWhenNarrow = bl;
    }

    public void setVisibility(int n) {
        if (n != this.getVisibility()) {
            super.setVisibility(n);
        }
    }

    public boolean showOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }

}

