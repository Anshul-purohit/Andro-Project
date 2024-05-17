/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ActionBarOverlayLayout
extends FrameLayout {
    static final int[] mActionBarSizeAttr = new int[]{R.attr.actionBarSize};
    private ActionBar mActionBar;
    private View mActionBarBottom;
    private int mActionBarHeight;
    private View mActionBarTop;
    private ActionBarView mActionView;
    private ActionBarContainer mContainerView;
    private View mContent;
    private final Rect mZeroRect = new Rect(0, 0, 0, 0);

    public ActionBarOverlayLayout(Context context) {
        super(context);
        this.init(context);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(context);
    }

    private boolean applyInsets(View view, Rect rect, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        boolean bl5 = false;
        view = (FrameLayout.LayoutParams)view.getLayoutParams();
        boolean bl6 = bl5;
        if (bl) {
            bl6 = bl5;
            if (view.leftMargin != rect.left) {
                bl6 = true;
                view.leftMargin = rect.left;
            }
        }
        bl = bl6;
        if (bl2) {
            bl = bl6;
            if (view.topMargin != rect.top) {
                bl = true;
                view.topMargin = rect.top;
            }
        }
        bl2 = bl;
        if (bl4) {
            bl2 = bl;
            if (view.rightMargin != rect.right) {
                bl2 = true;
                view.rightMargin = rect.right;
            }
        }
        bl = bl2;
        if (bl3) {
            bl = bl2;
            if (view.bottomMargin != rect.bottom) {
                bl = true;
                view.bottomMargin = rect.bottom;
            }
        }
        return bl;
    }

    private void init(Context context) {
        context = this.getContext().getTheme().obtainStyledAttributes(mActionBarSizeAttr);
        this.mActionBarHeight = context.getDimensionPixelSize(0, 0);
        context.recycle();
    }

    void pullChildren() {
        if (this.mContent == null) {
            this.mContent = this.findViewById(R.id.action_bar_activity_content);
            if (this.mContent == null) {
                this.mContent = this.findViewById(16908290);
            }
            this.mActionBarTop = this.findViewById(R.id.top_action_bar);
            this.mContainerView = (ActionBarContainer)this.findViewById(R.id.action_bar_container);
            this.mActionView = (ActionBarView)this.findViewById(R.id.action_bar);
            this.mActionBarBottom = this.findViewById(R.id.split_action_bar);
        }
    }

    public void setActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
    }
}

