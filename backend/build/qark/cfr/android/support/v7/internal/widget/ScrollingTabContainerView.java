/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.AbsListView
 *  android.widget.AbsListView$LayoutParams
 *  android.widget.BaseAdapter
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.SpinnerAdapter
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.widget.AdapterViewICS;
import android.support.v7.internal.widget.CompatTextView;
import android.support.v7.internal.widget.SpinnerICS;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ScrollingTabContainerView
extends HorizontalScrollView
implements AdapterViewICS.OnItemClickListener {
    private static final String TAG = "ScrollingTabContainerView";
    private boolean mAllowCollapse;
    private int mContentHeight;
    private final LayoutInflater mInflater;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    private LinearLayout mTabLayout;
    Runnable mTabSelector;
    private SpinnerICS mTabSpinner;

    public ScrollingTabContainerView(Context object) {
        super((Context)object);
        this.mInflater = LayoutInflater.from((Context)object);
        this.setHorizontalScrollBarEnabled(false);
        object = ActionBarPolicy.get((Context)object);
        this.setContentHeight(object.getTabContainerHeight());
        this.mStackedTabMaxWidth = object.getStackedTabMaxWidth();
        this.mTabLayout = (LinearLayout)this.mInflater.inflate(R.layout.abc_action_bar_tabbar, (ViewGroup)this, false);
        this.addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
    }

    private SpinnerICS createSpinner() {
        SpinnerICS spinnerICS = new SpinnerICS(this.getContext(), null, R.attr.actionDropDownStyle);
        spinnerICS.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -1));
        spinnerICS.setOnItemClickListenerInt(this);
        return spinnerICS;
    }

    private TabView createTabView(ActionBar.Tab tab, boolean bl) {
        TabView tabView = (TabView)this.mInflater.inflate(R.layout.abc_action_bar_tab, (ViewGroup)this.mTabLayout, false);
        tabView.attach(this, tab, bl);
        if (bl) {
            tabView.setBackgroundDrawable(null);
            tabView.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, this.mContentHeight));
            return tabView;
        }
        tabView.setFocusable(true);
        if (this.mTabClickListener == null) {
            this.mTabClickListener = new TabClickListener();
        }
        tabView.setOnClickListener((View.OnClickListener)this.mTabClickListener);
        return tabView;
    }

    private boolean isCollapsed() {
        if (this.mTabSpinner != null && this.mTabSpinner.getParent() == this) {
            return true;
        }
        return false;
    }

    private void performCollapse() {
        if (this.isCollapsed()) {
            return;
        }
        if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
        }
        this.removeView((View)this.mTabLayout);
        this.addView((View)this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
        if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter((SpinnerAdapter)new TabAdapter());
        }
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
            this.mTabSelector = null;
        }
        this.mTabSpinner.setSelection(this.mSelectedTabIndex);
    }

    private boolean performExpand() {
        if (!this.isCollapsed()) {
            return false;
        }
        this.removeView((View)this.mTabSpinner);
        this.addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
        this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void addTab(ActionBar.Tab object, int n, boolean bl) {
        object = this.createTabView((ActionBar.Tab)object, false);
        this.mTabLayout.addView((View)object, n, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(0, -1, 1.0f));
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (bl) {
            object.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void addTab(ActionBar.Tab object, boolean bl) {
        object = this.createTabView((ActionBar.Tab)object, false);
        this.mTabLayout.addView((View)object, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(0, -1, 1.0f));
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (bl) {
            object.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void animateToTab(int n) {
        final View view = this.mTabLayout.getChildAt(n);
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }
        this.mTabSelector = new Runnable(){

            public void run() {
                int n = view.getLeft();
                int n2 = (ScrollingTabContainerView.this.getWidth() - view.getWidth()) / 2;
                ScrollingTabContainerView.this.smoothScrollTo(n - n2, 0);
                ScrollingTabContainerView.this.mTabSelector = null;
            }
        };
        this.post(this.mTabSelector);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null) {
            this.post(this.mTabSelector);
        }
    }

    protected void onConfigurationChanged(Configuration object) {
        object = ActionBarPolicy.get(this.getContext());
        this.setContentHeight(object.getTabContainerHeight());
        this.mStackedTabMaxWidth = object.getStackedTabMaxWidth();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }
    }

    @Override
    public void onItemClick(AdapterViewICS<?> adapterViewICS, View view, int n, long l) {
        ((TabView)view).getTab().select();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onMeasure(int n, int n2) {
        n2 = View.MeasureSpec.getMode((int)n);
        boolean bl = n2 == 1073741824;
        this.setFillViewport(bl);
        int n3 = this.mTabLayout.getChildCount();
        if (n3 > 1 && (n2 == 1073741824 || n2 == Integer.MIN_VALUE)) {
            this.mMaxTabWidth = n3 > 2 ? (int)((float)View.MeasureSpec.getSize((int)n) * 0.4f) : View.MeasureSpec.getSize((int)n) / 2;
            this.mMaxTabWidth = Math.min((int)this.mMaxTabWidth, (int)this.mStackedTabMaxWidth);
        } else {
            this.mMaxTabWidth = -1;
        }
        n3 = View.MeasureSpec.makeMeasureSpec((int)this.mContentHeight, (int)1073741824);
        n2 = !bl && this.mAllowCollapse ? 1 : 0;
        if (n2 != 0) {
            this.mTabLayout.measure(0, n3);
            if (this.mTabLayout.getMeasuredWidth() > View.MeasureSpec.getSize((int)n)) {
                this.performCollapse();
            } else {
                this.performExpand();
            }
        } else {
            this.performExpand();
        }
        n2 = this.getMeasuredWidth();
        super.onMeasure(n, n3);
        n = this.getMeasuredWidth();
        if (bl && n2 != n) {
            this.setTabSelected(this.mSelectedTabIndex);
        }
    }

    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void removeTabAt(int n) {
        this.mTabLayout.removeViewAt(n);
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void setAllowCollapse(boolean bl) {
        this.mAllowCollapse = bl;
    }

    public void setContentHeight(int n) {
        this.mContentHeight = n;
        this.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setTabSelected(int n) {
        this.mSelectedTabIndex = n;
        int n2 = this.mTabLayout.getChildCount();
        for (int i = 0; i < n2; ++i) {
            View view = this.mTabLayout.getChildAt(i);
            boolean bl = i == n;
            view.setSelected(bl);
            if (!bl) continue;
            this.animateToTab(n);
        }
        if (this.mTabSpinner != null && n >= 0) {
            this.mTabSpinner.setSelection(n);
        }
    }

    public void updateTab(int n) {
        ((TabView)this.mTabLayout.getChildAt(n)).update();
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    private class TabAdapter
    extends BaseAdapter {
        private TabAdapter() {
        }

        public int getCount() {
            return ScrollingTabContainerView.this.mTabLayout.getChildCount();
        }

        public Object getItem(int n) {
            return ((TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(n)).getTab();
        }

        public long getItemId(int n) {
            return n;
        }

        public View getView(int n, View view, ViewGroup viewGroup) {
            if (view == null) {
                return ScrollingTabContainerView.this.createTabView((ActionBar.Tab)this.getItem(n), true);
            }
            ((TabView)view).bindTab((ActionBar.Tab)this.getItem(n));
            return view;
        }
    }

    private class TabClickListener
    implements View.OnClickListener {
        private TabClickListener() {
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onClick(View view) {
            ((TabView)view).getTab().select();
            int n = ScrollingTabContainerView.this.mTabLayout.getChildCount();
            int n2 = 0;
            while (n2 < n) {
                View view2 = ScrollingTabContainerView.this.mTabLayout.getChildAt(n2);
                boolean bl = view2 == view;
                view2.setSelected(bl);
                ++n2;
            }
        }
    }

    public static class TabView
    extends LinearLayout {
        private View mCustomView;
        private ImageView mIconView;
        private ScrollingTabContainerView mParent;
        private ActionBar.Tab mTab;
        private TextView mTextView;

        public TabView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        void attach(ScrollingTabContainerView scrollingTabContainerView, ActionBar.Tab tab, boolean bl) {
            this.mParent = scrollingTabContainerView;
            this.mTab = tab;
            if (bl) {
                this.setGravity(19);
            }
            this.update();
        }

        public void bindTab(ActionBar.Tab tab) {
            this.mTab = tab;
            this.update();
        }

        public ActionBar.Tab getTab() {
            return this.mTab;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            n = this.mParent != null ? this.mParent.mMaxTabWidth : 0;
            if (n <= 0) return;
            if (this.getMeasuredWidth() > n) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824), n2);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void update() {
            ActionBar.Tab tab = this.mTab;
            View view = tab.getCustomView();
            if (view != null) {
                tab = view.getParent();
                if (tab != this) {
                    if (tab != null) {
                        ((ViewGroup)tab).removeView(view);
                    }
                    this.addView(view);
                }
                this.mCustomView = view;
                if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                }
                if (this.mIconView == null) return;
                {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                    return;
                }
            } else {
                ImageView imageView;
                if (this.mCustomView != null) {
                    this.removeView(this.mCustomView);
                    this.mCustomView = null;
                }
                Object object = tab.getIcon();
                view = tab.getText();
                if (object != null) {
                    if (this.mIconView == null) {
                        imageView = new ImageView(this.getContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.gravity = 16;
                        imageView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                        this.addView((View)imageView, 0);
                        this.mIconView = imageView;
                    }
                    this.mIconView.setImageDrawable((Drawable)object);
                    this.mIconView.setVisibility(0);
                } else if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                if (view != null) {
                    if (this.mTextView == null) {
                        object = new CompatTextView(this.getContext(), null, R.attr.actionBarTabTextStyle);
                        object.setEllipsize(TextUtils.TruncateAt.END);
                        imageView = new LinearLayout.LayoutParams(-2, -2);
                        imageView.gravity = 16;
                        object.setLayoutParams((ViewGroup.LayoutParams)imageView);
                        this.addView((View)object);
                        this.mTextView = object;
                    }
                    this.mTextView.setText((CharSequence)view);
                    this.mTextView.setVisibility(0);
                } else if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                    this.mTextView.setText(null);
                }
                if (this.mIconView == null) return;
                {
                    this.mIconView.setContentDescription(tab.getContentDescription());
                    return;
                }
            }
        }
    }

}

