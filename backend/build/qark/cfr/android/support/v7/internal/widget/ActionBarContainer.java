/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  java.lang.Math
 *  java.lang.Object
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.internal.widget.ScrollingTabContainerView;
import android.support.v7.view.ActionMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ActionBarContainer
extends FrameLayout {
    private ActionBarView mActionBarView;
    private Drawable mBackground;
    private boolean mIsSplit;
    private boolean mIsStacked;
    private boolean mIsTransitioning;
    private Drawable mSplitBackground;
    private Drawable mStackedBackground;
    private View mTabContainer;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        boolean bl = true;
        super(context, attributeSet);
        this.setBackgroundDrawable(null);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBar);
        this.mBackground = context.getDrawable(10);
        this.mStackedBackground = context.getDrawable(11);
        if (this.getId() == R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = context.getDrawable(12);
        }
        context.recycle();
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                bl = false;
            }
        } else if (this.mBackground != null || this.mStackedBackground != null) {
            bl = false;
        }
        this.setWillNotDraw(bl);
    }

    private void drawBackgroundDrawable(Drawable drawable2, Canvas canvas) {
        Rect rect = drawable2.getBounds();
        if (drawable2 instanceof ColorDrawable && !rect.isEmpty() && Build.VERSION.SDK_INT < 11) {
            canvas.save();
            canvas.clipRect(rect);
            drawable2.draw(canvas);
            canvas.restore();
            return;
        }
        drawable2.draw(canvas);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackground != null && this.mBackground.isStateful()) {
            this.mBackground.setState(this.getDrawableState());
        }
        if (this.mStackedBackground != null && this.mStackedBackground.isStateful()) {
            this.mStackedBackground.setState(this.getDrawableState());
        }
        if (this.mSplitBackground != null && this.mSplitBackground.isStateful()) {
            this.mSplitBackground.setState(this.getDrawableState());
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onDraw(Canvas canvas) {
        if (this.getWidth() == 0 || this.getHeight() == 0) return;
        if (this.mIsSplit) {
            if (this.mSplitBackground == null) return;
            {
                this.drawBackgroundDrawable(this.mSplitBackground, canvas);
                return;
            }
        }
        if (this.mBackground != null) {
            this.drawBackgroundDrawable(this.mBackground, canvas);
        }
        if (this.mStackedBackground == null || !this.mIsStacked) {
            return;
        }
        this.drawBackgroundDrawable(this.mStackedBackground, canvas);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = (ActionBarView)this.findViewById(R.id.action_bar);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        n2 = this.mTabContainer != null && this.mTabContainer.getVisibility() != 8 ? 1 : 0;
        if (this.mTabContainer != null && this.mTabContainer.getVisibility() != 8) {
            n4 = this.getMeasuredHeight();
            int n5 = this.mTabContainer.getMeasuredHeight();
            if ((this.mActionBarView.getDisplayOptions() & 2) != 0) {
                this.mTabContainer.layout(n, n4 - n5, n3, n4);
            } else {
                int n6 = this.getChildCount();
                for (n4 = 0; n4 < n6; ++n4) {
                    View view = this.getChildAt(n4);
                    if (view == this.mTabContainer || this.mActionBarView.isCollapsed()) continue;
                    view.offsetTopAndBottom(n5);
                }
                this.mTabContainer.layout(n, 0, n3, n5);
            }
        }
        n3 = 0;
        n = 0;
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                n = 1;
            }
        } else {
            n = n3;
            if (this.mBackground != null) {
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                n = 1;
            }
            bl = n2 != 0 && this.mStackedBackground != null;
            this.mIsStacked = bl;
            if (bl) {
                this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
                n = 1;
            }
        }
        if (n != 0) {
            this.invalidate();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onMeasure(int n, int n2) {
        block3 : {
            block2 : {
                super.onMeasure(n, n2);
                if (this.mActionBarView == null) break block2;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mActionBarView.getLayoutParams();
                n = this.mActionBarView.isCollapsed() ? 0 : this.mActionBarView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                if (this.mTabContainer == null) return;
                if (this.mTabContainer.getVisibility() != 8 && View.MeasureSpec.getMode((int)n2) == Integer.MIN_VALUE) break block3;
            }
            return;
        }
        n2 = View.MeasureSpec.getSize((int)n2);
        this.setMeasuredDimension(this.getMeasuredWidth(), Math.min((int)(this.mTabContainer.getMeasuredHeight() + n), (int)n2));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPrimaryBackground(Drawable drawable2) {
        boolean bl = true;
        if (this.mBackground != null) {
            this.mBackground.setCallback(null);
            this.unscheduleDrawable(this.mBackground);
        }
        this.mBackground = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            if (this.mActionBarView != null) {
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                bl = false;
            }
        } else if (this.mBackground != null || this.mStackedBackground != null) {
            bl = false;
        }
        this.setWillNotDraw(bl);
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSplitBackground(Drawable drawable2) {
        boolean bl = true;
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setCallback(null);
            this.unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            if (this.mIsSplit && this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                bl = false;
            }
        } else if (this.mBackground != null || this.mStackedBackground != null) {
            bl = false;
        }
        this.setWillNotDraw(bl);
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setStackedBackground(Drawable drawable2) {
        boolean bl = true;
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setCallback(null);
            this.unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            if (this.mIsStacked && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                bl = false;
            }
        } else if (this.mBackground != null || this.mStackedBackground != null) {
            bl = false;
        }
        this.setWillNotDraw(bl);
        this.invalidate();
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        if (this.mTabContainer != null) {
            this.removeView(this.mTabContainer);
        }
        this.mTabContainer = scrollingTabContainerView;
        if (scrollingTabContainerView != null) {
            this.addView((View)scrollingTabContainerView);
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setTransitioning(boolean bl) {
        this.mIsTransitioning = bl;
        int n = bl ? 393216 : 262144;
        this.setDescendantFocusability(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setVisibility(int n) {
        super.setVisibility(n);
        boolean bl = n == 0;
        if (this.mBackground != null) {
            this.mBackground.setVisible(bl, false);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setVisible(bl, false);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setVisible(bl, false);
        }
    }

    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (drawable2 == this.mBackground && !this.mIsSplit || drawable2 == this.mStackedBackground && this.mIsStacked || drawable2 == this.mSplitBackground && this.mIsSplit || super.verifyDrawable(drawable2)) {
            return true;
        }
        return false;
    }
}

