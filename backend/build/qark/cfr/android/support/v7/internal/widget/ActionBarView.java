/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.Window$Callback
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.SpinnerAdapter
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 */
package android.support.v7.internal.widget;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.ActionMenuItem;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.internal.widget.AbsActionBarView;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.AdapterViewICS;
import android.support.v7.internal.widget.ProgressBarICS;
import android.support.v7.internal.widget.ScrollingTabContainerView;
import android.support.v7.internal.widget.SpinnerICS;
import android.support.v7.view.CollapsibleActionView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.util.List;

public class ActionBarView
extends AbsActionBarView {
    private static final int DEFAULT_CUSTOM_GRAVITY = 19;
    public static final int DISPLAY_DEFAULT = 0;
    private static final int DISPLAY_RELAYOUT_MASK = 31;
    private static final String TAG = "ActionBarView";
    private ActionBar.OnNavigationListener mCallback;
    private Context mContext;
    private ActionBarContextView mContextView;
    private View mCustomNavView;
    private int mDisplayOptions = -1;
    View mExpandedActionView;
    private final View.OnClickListener mExpandedActionViewUpListener;
    private HomeView mExpandedHomeLayout;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private HomeView mHomeLayout;
    private Drawable mIcon;
    private boolean mIncludeTabs;
    private int mIndeterminateProgressStyle;
    private ProgressBarICS mIndeterminateProgressView;
    private boolean mIsCollapsable;
    private boolean mIsCollapsed;
    private int mItemPadding;
    private LinearLayout mListNavLayout;
    private Drawable mLogo;
    private ActionMenuItem mLogoNavItem;
    private final AdapterViewICS.OnItemSelectedListener mNavItemSelectedListener;
    private int mNavigationMode;
    private MenuBuilder mOptionsMenu;
    private int mProgressBarPadding;
    private int mProgressStyle;
    private ProgressBarICS mProgressView;
    private SpinnerICS mSpinner;
    private SpinnerAdapter mSpinnerAdapter;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private ScrollingTabContainerView mTabScrollView;
    private Runnable mTabSelector;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private int mTitleStyleRes;
    private View mTitleUpView;
    private TextView mTitleView;
    private final View.OnClickListener mUpClickListener;
    private boolean mUserTitle;
    Window.Callback mWindowCallback;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ActionBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNavItemSelectedListener = new AdapterViewICS.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterViewICS<?> adapterViewICS, View view, int n, long l) {
                if (ActionBarView.this.mCallback != null) {
                    ActionBarView.this.mCallback.onNavigationItemSelected(n, l);
                }
            }

            @Override
            public void onNothingSelected(AdapterViewICS<?> adapterViewICS) {
            }
        };
        this.mExpandedActionViewUpListener = new View.OnClickListener(){

            public void onClick(View object) {
                object = ActionBarView.access$100((ActionBarView)ActionBarView.this).mCurrentExpandedItem;
                if (object != null) {
                    object.collapseActionView();
                }
            }
        };
        this.mUpClickListener = new View.OnClickListener(){

            public void onClick(View view) {
                ActionBarView.this.mWindowCallback.onMenuItemSelected(0, (MenuItem)ActionBarView.this.mLogoNavItem);
            }
        };
        this.mContext = context;
        this.setBackgroundResource(0);
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        PackageManager packageManager = context.getPackageManager();
        this.mNavigationMode = attributeSet.getInt(2, 0);
        this.mTitle = attributeSet.getText(0);
        this.mSubtitle = attributeSet.getText(4);
        this.mLogo = attributeSet.getDrawable(8);
        if (this.mLogo == null && Build.VERSION.SDK_INT >= 9) {
            if (context instanceof Activity) {
                try {
                    this.mLogo = packageManager.getActivityLogo(((Activity)context).getComponentName());
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    Log.e((String)"ActionBarView", (String)"Activity component name not found!", (Throwable)nameNotFoundException);
                }
            }
            if (this.mLogo == null) {
                this.mLogo = applicationInfo.loadLogo(packageManager);
            }
        }
        this.mIcon = attributeSet.getDrawable(7);
        if (this.mIcon == null) {
            if (context instanceof Activity) {
                try {
                    this.mIcon = packageManager.getActivityIcon(((Activity)context).getComponentName());
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    Log.e((String)"ActionBarView", (String)"Activity component name not found!", (Throwable)nameNotFoundException);
                }
            }
            if (this.mIcon == null) {
                this.mIcon = applicationInfo.loadIcon(packageManager);
            }
        }
        applicationInfo = LayoutInflater.from((Context)context);
        int n = attributeSet.getResourceId(14, R.layout.abc_action_bar_home);
        this.mHomeLayout = (HomeView)applicationInfo.inflate(n, (ViewGroup)this, false);
        this.mExpandedHomeLayout = (HomeView)applicationInfo.inflate(n, (ViewGroup)this, false);
        this.mExpandedHomeLayout.setUp(true);
        this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
        this.mExpandedHomeLayout.setContentDescription(this.getResources().getText(R.string.abc_action_bar_up_description));
        this.mTitleStyleRes = attributeSet.getResourceId(5, 0);
        this.mSubtitleStyleRes = attributeSet.getResourceId(6, 0);
        this.mProgressStyle = attributeSet.getResourceId(15, 0);
        this.mIndeterminateProgressStyle = attributeSet.getResourceId(16, 0);
        this.mProgressBarPadding = attributeSet.getDimensionPixelOffset(17, 0);
        this.mItemPadding = attributeSet.getDimensionPixelOffset(18, 0);
        this.setDisplayOptions(attributeSet.getInt(3, 0));
        n = attributeSet.getResourceId(13, 0);
        if (n != 0) {
            this.mCustomNavView = applicationInfo.inflate(n, (ViewGroup)this, false);
            this.mNavigationMode = 0;
            this.setDisplayOptions(this.mDisplayOptions | 16);
        }
        this.mContentHeight = attributeSet.getLayoutDimension(1, 0);
        attributeSet.recycle();
        this.mLogoNavItem = new ActionMenuItem(context, 0, 16908332, 0, 0, this.mTitle);
        this.mHomeLayout.setOnClickListener(this.mUpClickListener);
        this.mHomeLayout.setClickable(true);
        this.mHomeLayout.setFocusable(true);
    }

    static /* synthetic */ ExpandedActionViewMenuPresenter access$100(ActionBarView actionBarView) {
        return actionBarView.mExpandedMenuPresenter;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void configPresenters(MenuBuilder menuBuilder) {
        if (menuBuilder != null) {
            menuBuilder.addMenuPresenter(this.mActionMenuPresenter);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter);
        } else {
            this.mActionMenuPresenter.initForMenu(this.mContext, null);
            this.mExpandedMenuPresenter.initForMenu(this.mContext, null);
        }
        this.mActionMenuPresenter.updateMenuView(true);
        this.mExpandedMenuPresenter.updateMenuView(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void initTitle() {
        boolean bl = true;
        if (this.mTitleLayout == null) {
            this.mTitleLayout = (LinearLayout)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.abc_action_bar_title_item, (ViewGroup)this, false);
            this.mTitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_title);
            this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
            this.mTitleUpView = this.mTitleLayout.findViewById(R.id.up);
            this.mTitleLayout.setOnClickListener(this.mUpClickListener);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(this.mContext, this.mTitleStyleRes);
            }
            if (this.mTitle != null) {
                this.mTitleView.setText(this.mTitle);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(this.mContext, this.mSubtitleStyleRes);
            }
            if (this.mSubtitle != null) {
                this.mSubtitleView.setText(this.mSubtitle);
                this.mSubtitleView.setVisibility(0);
            }
            boolean bl2 = (this.mDisplayOptions & 4) != 0;
            boolean bl3 = (this.mDisplayOptions & 2) != 0;
            View view = this.mTitleUpView;
            int n = !bl3 ? (bl2 ? 0 : 4) : 8;
            view.setVisibility(n);
            view = this.mTitleLayout;
            if (!bl2 || bl3) {
                bl = false;
            }
            view.setEnabled(bl);
        }
        this.addView((View)this.mTitleLayout);
        if (this.mExpandedActionView != null || TextUtils.isEmpty((CharSequence)this.mTitle) && TextUtils.isEmpty((CharSequence)this.mSubtitle)) {
            this.mTitleLayout.setVisibility(8);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setTitleImpl(CharSequence charSequence) {
        int n = 0;
        this.mTitle = charSequence;
        if (this.mTitleView != null) {
            this.mTitleView.setText(charSequence);
            int n2 = this.mExpandedActionView == null && (this.mDisplayOptions & 8) != 0 && (!TextUtils.isEmpty((CharSequence)this.mTitle) || !TextUtils.isEmpty((CharSequence)this.mSubtitle)) ? 1 : 0;
            LinearLayout linearLayout = this.mTitleLayout;
            n2 = n2 != 0 ? n : 8;
            linearLayout.setVisibility(n2);
        }
        if (this.mLogoNavItem != null) {
            this.mLogoNavItem.setTitle(charSequence);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void collapseActionView() {
        if (this.mExpandedMenuPresenter == null) {
            return;
        }
        MenuItemImpl menuItemImpl = this.mExpandedMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ActionBar.LayoutParams(19);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ActionBar.LayoutParams(this.getContext(), attributeSet);
    }

    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        ViewGroup.LayoutParams layoutParams2 = layoutParams;
        if (layoutParams == null) {
            layoutParams2 = this.generateDefaultLayoutParams();
        }
        return layoutParams2;
    }

    public View getCustomNavigationView() {
        return this.mCustomNavView;
    }

    public int getDisplayOptions() {
        return this.mDisplayOptions;
    }

    public SpinnerAdapter getDropdownAdapter() {
        return this.mSpinnerAdapter;
    }

    public int getDropdownSelectedPosition() {
        return this.mSpinner.getSelectedItemPosition();
    }

    public int getNavigationMode() {
        return this.mNavigationMode;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public boolean hasEmbeddedTabs() {
        return this.mIncludeTabs;
    }

    public boolean hasExpandedActionView() {
        if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
            return true;
        }
        return false;
    }

    public void initIndeterminateProgress() {
        this.mIndeterminateProgressView = new ProgressBarICS(this.mContext, null, 0, this.mIndeterminateProgressStyle);
        this.mIndeterminateProgressView.setId(R.id.progress_circular);
        this.mIndeterminateProgressView.setVisibility(8);
        this.addView((View)this.mIndeterminateProgressView);
    }

    public void initProgress() {
        this.mProgressView = new ProgressBarICS(this.mContext, null, 0, this.mProgressStyle);
        this.mProgressView.setId(R.id.progress_horizontal);
        this.mProgressView.setMax(10000);
        this.mProgressView.setVisibility(8);
        this.addView((View)this.mProgressView);
    }

    public boolean isCollapsed() {
        return this.mIsCollapsed;
    }

    public boolean isSplitActionBar() {
        return this.mSplitActionBar;
    }

    @Override
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTitleView = null;
        this.mSubtitleView = null;
        this.mTitleUpView = null;
        if (this.mTitleLayout != null && this.mTitleLayout.getParent() == this) {
            this.removeView((View)this.mTitleLayout);
        }
        this.mTitleLayout = null;
        if ((this.mDisplayOptions & 8) != 0) {
            this.initTitle();
        }
        if (this.mTabScrollView != null && this.mIncludeTabs) {
            configuration = this.mTabScrollView.getLayoutParams();
            if (configuration != null) {
                configuration.width = -2;
                configuration.height = -1;
            }
            this.mTabScrollView.setAllowCollapse(true);
        }
        if (this.mProgressView != null) {
            this.removeView((View)this.mProgressView);
            this.initProgress();
        }
        if (this.mIndeterminateProgressView != null) {
            this.removeView((View)this.mIndeterminateProgressView);
            this.initIndeterminateProgress();
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mTabSelector);
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    protected void onFinishInflate() {
        ViewParent viewParent;
        super.onFinishInflate();
        this.addView((View)this.mHomeLayout);
        if (this.mCustomNavView != null && (this.mDisplayOptions & 16) != 0 && (viewParent = this.mCustomNavView.getParent()) != this) {
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup)viewParent).removeView(this.mCustomNavView);
            }
            this.addView(this.mCustomNavView);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void onLayout(boolean var1_1, int var2_2, int var3_3, int var4_4, int var5_5) {
        block40 : {
            var7_6 = this.getPaddingLeft();
            var8_7 = this.getPaddingTop();
            var9_8 = var5_5 - var3_3 - this.getPaddingTop() - this.getPaddingBottom();
            if (var9_8 <= 0) {
                return;
            }
            var11_9 = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
            var6_10 = var7_6;
            if (var11_9.getVisibility() != 8) {
                var3_3 = var11_9.getLeftOffset();
                var6_10 = var7_6 + (this.positionChild((View)var11_9, var7_6 + var3_3, var8_7, var9_8) + var3_3);
            }
            var3_3 = var6_10;
            if (this.mExpandedActionView != null) break block40;
            var7_6 = this.mTitleLayout != null && this.mTitleLayout.getVisibility() != 8 && (this.mDisplayOptions & 8) != 0 ? 1 : 0;
            var5_5 = var6_10;
            if (var7_6 != 0) {
                var5_5 = var6_10 + this.positionChild((View)this.mTitleLayout, var6_10, var8_7, var9_8);
            }
            var3_3 = var5_5;
            switch (this.mNavigationMode) {
                default: {
                    var3_3 = var5_5;
                    break;
                }
                case 1: {
                    var3_3 = var5_5;
                    if (this.mListNavLayout != null) {
                        var3_3 = var5_5;
                        if (var7_6 != 0) {
                            var3_3 = var5_5 + this.mItemPadding;
                        }
                        var3_3 += this.positionChild((View)this.mListNavLayout, var3_3, var8_7, var9_8) + this.mItemPadding;
                        break;
                    }
                    ** GOTO lbl38
                }
                case 2: {
                    var3_3 = var5_5;
                    if (this.mTabScrollView != null) {
                        var3_3 = var5_5;
                        if (var7_6 != 0) {
                            var3_3 = var5_5 + this.mItemPadding;
                        }
                        var3_3 += this.positionChild((View)this.mTabScrollView, var3_3, var8_7, var9_8) + this.mItemPadding;
                    }
                }
lbl38: // 5 sources:
                case 0: 
            }
        }
        var4_4 = var2_2 = var4_4 - var2_2 - this.getPaddingRight();
        if (this.mMenuView != null) {
            var4_4 = var2_2;
            if (this.mMenuView.getParent() == this) {
                this.positionChildInverse((View)this.mMenuView, var2_2, var8_7, var9_8);
                var4_4 = var2_2 - this.mMenuView.getMeasuredWidth();
            }
        }
        var2_2 = var4_4;
        if (this.mIndeterminateProgressView != null) {
            var2_2 = var4_4;
            if (this.mIndeterminateProgressView.getVisibility() != 8) {
                this.positionChildInverse(this.mIndeterminateProgressView, var4_4, var8_7, var9_8);
                var2_2 = var4_4 - this.mIndeterminateProgressView.getMeasuredWidth();
            }
        }
        var12_11 = null;
        if (this.mExpandedActionView != null) {
            var11_9 = this.mExpandedActionView;
        } else {
            var11_9 = var12_11;
            if ((this.mDisplayOptions & 16) != 0) {
                var11_9 = var12_11;
                if (this.mCustomNavView != null) {
                    var11_9 = this.mCustomNavView;
                }
            }
        }
        if (var11_9 != null) {
            var12_11 = var11_9.getLayoutParams();
            var12_11 = var12_11 instanceof ActionBar.LayoutParams != false ? (ActionBar.LayoutParams)var12_11 : null;
            var5_5 = var12_11 != null ? var12_11.gravity : 19;
            var10_12 = var11_9.getMeasuredWidth();
            var6_10 = 0;
            var7_6 = 0;
            var8_7 = var2_2;
            var4_4 = var3_3;
            if (var12_11 != null) {
                var4_4 = var3_3 + var12_11.leftMargin;
                var8_7 = var2_2 - var12_11.rightMargin;
                var6_10 = var12_11.topMargin;
                var7_6 = var12_11.bottomMargin;
            }
            if ((var3_3 = var5_5 & 7) == 1) {
                var2_2 = (this.getWidth() - var10_12) / 2;
                if (var2_2 < var4_4) {
                    var3_3 = 3;
                } else if (var2_2 + var10_12 > var8_7) {
                    var3_3 = 5;
                }
            } else if (var5_5 == -1) {
                var3_3 = 3;
            }
            var2_2 = var9_8 = 0;
            switch (var3_3) {
                default: {
                    var2_2 = var9_8;
                    break;
                }
                case 1: {
                    var2_2 = (this.getWidth() - var10_12) / 2;
                    break;
                }
                case 3: {
                    var2_2 = var4_4;
                }
                case 2: 
                case 4: {
                    break;
                }
                case 5: {
                    var2_2 = var8_7 - var10_12;
                }
            }
            var3_3 = var5_5 & 112;
            if (var5_5 == -1) {
                var3_3 = 16;
            }
            var4_4 = 0;
            switch (var3_3) {
                default: {
                    var3_3 = var4_4;
                    break;
                }
                case 16: {
                    var3_3 = this.getPaddingTop();
                    var3_3 = (this.getHeight() - this.getPaddingBottom() - var3_3 - var11_9.getMeasuredHeight()) / 2;
                    break;
                }
                case 48: {
                    var3_3 = this.getPaddingTop() + var6_10;
                    break;
                }
                case 80: {
                    var3_3 = this.getHeight() - this.getPaddingBottom() - var11_9.getMeasuredHeight() - var7_6;
                }
            }
            var4_4 = var11_9.getMeasuredWidth();
            var11_9.layout(var2_2, var3_3, var2_2 + var4_4, var11_9.getMeasuredHeight() + var3_3);
        }
        if (this.mProgressView == null) return;
        this.mProgressView.bringToFront();
        var2_2 = this.mProgressView.getMeasuredHeight() / 2;
        this.mProgressView.layout(this.mProgressBarPadding, - var2_2, this.mProgressBarPadding + this.mProgressView.getMeasuredWidth(), var2_2);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3;
        int n4;
        Object object;
        Object object2;
        int n5;
        int n6;
        block32 : {
            n6 = this.getChildCount();
            if (!this.mIsCollapsable) break block32;
            n3 = 0;
            for (n5 = 0; n5 < n6; ++n5) {
                block33 : {
                    block34 : {
                        object2 = this.getChildAt(n5);
                        n4 = n3;
                        if (object2.getVisibility() == 8) break block33;
                        if (object2 != this.mMenuView) break block34;
                        n4 = n3;
                        if (this.mMenuView.getChildCount() == 0) break block33;
                    }
                    n4 = n3 + 1;
                }
                n3 = n4;
            }
            this.setMeasuredDimension(0, 0);
            this.mIsCollapsed = true;
            return;
        }
        this.mIsCollapsed = false;
        if (View.MeasureSpec.getMode((int)n) != 1073741824) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"MATCH_PARENT\" (or fill_parent)");
        }
        if (View.MeasureSpec.getMode((int)n2) != Integer.MIN_VALUE) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
        }
        int n7 = View.MeasureSpec.getSize((int)n);
        int n8 = this.mContentHeight > 0 ? this.mContentHeight : View.MeasureSpec.getSize((int)n2);
        int n9 = this.getPaddingTop() + this.getPaddingBottom();
        n = this.getPaddingLeft();
        n2 = this.getPaddingRight();
        int n10 = n8 - n9;
        int n11 = View.MeasureSpec.makeMeasureSpec((int)n10, (int)Integer.MIN_VALUE);
        n4 = n7 - n - n2;
        n3 = n5 = n4 / 2;
        object2 = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
        n2 = n4;
        if (object2.getVisibility() != 8) {
            object = object2.getLayoutParams();
            n = object.width < 0 ? View.MeasureSpec.makeMeasureSpec((int)n4, (int)Integer.MIN_VALUE) : View.MeasureSpec.makeMeasureSpec((int)object.width, (int)1073741824);
            object2.measure(n, View.MeasureSpec.makeMeasureSpec((int)n10, (int)1073741824));
            n = object2.getMeasuredWidth() + object2.getLeftOffset();
            n2 = Math.max((int)0, (int)(n4 - n));
            n5 = Math.max((int)0, (int)(n2 - n));
        }
        n4 = n2;
        n = n3;
        if (this.mMenuView != null) {
            n4 = n2;
            n = n3;
            if (this.mMenuView.getParent() == this) {
                n4 = this.measureChildView((View)this.mMenuView, n2, n11, 0);
                n = Math.max((int)0, (int)(n3 - this.mMenuView.getMeasuredWidth()));
            }
        }
        n3 = n4;
        int n12 = n;
        if (this.mIndeterminateProgressView != null) {
            n3 = n4;
            n12 = n;
            if (this.mIndeterminateProgressView.getVisibility() != 8) {
                n3 = this.measureChildView(this.mIndeterminateProgressView, n4, n11, 0);
                n12 = Math.max((int)0, (int)(n - this.mIndeterminateProgressView.getMeasuredWidth()));
            }
        }
        n4 = this.mTitleLayout != null && this.mTitleLayout.getVisibility() != 8 && (this.mDisplayOptions & 8) != 0 ? 1 : 0;
        n = n3;
        n2 = n5;
        if (this.mExpandedActionView == null) {
            switch (this.mNavigationMode) {
                default: {
                    n2 = n5;
                    n = n3;
                    break;
                }
                case 1: {
                    n = n3;
                    n2 = n5;
                    if (this.mListNavLayout == null) break;
                    n = n4 != 0 ? this.mItemPadding * 2 : this.mItemPadding;
                    n2 = Math.max((int)0, (int)(n3 - n));
                    n5 = Math.max((int)0, (int)(n5 - n));
                    this.mListNavLayout.measure(View.MeasureSpec.makeMeasureSpec((int)n2, (int)Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec((int)n10, (int)1073741824));
                    n3 = this.mListNavLayout.getMeasuredWidth();
                    n = Math.max((int)0, (int)(n2 - n3));
                    n2 = Math.max((int)0, (int)(n5 - n3));
                    break;
                }
                case 2: {
                    n = n3;
                    n2 = n5;
                    if (this.mTabScrollView == null) break;
                    n = n4 != 0 ? this.mItemPadding * 2 : this.mItemPadding;
                    n2 = Math.max((int)0, (int)(n3 - n));
                    n5 = Math.max((int)0, (int)(n5 - n));
                    this.mTabScrollView.measure(View.MeasureSpec.makeMeasureSpec((int)n2, (int)Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec((int)n10, (int)1073741824));
                    n3 = this.mTabScrollView.getMeasuredWidth();
                    n = Math.max((int)0, (int)(n2 - n3));
                    n2 = Math.max((int)0, (int)(n5 - n3));
                }
            }
        }
        object = null;
        if (this.mExpandedActionView != null) {
            object2 = this.mExpandedActionView;
        } else {
            object2 = object;
            if ((this.mDisplayOptions & 16) != 0) {
                object2 = object;
                if (this.mCustomNavView != null) {
                    object2 = this.mCustomNavView;
                }
            }
        }
        n5 = n;
        if (object2 != null) {
            ViewGroup.LayoutParams layoutParams = this.generateLayoutParams(object2.getLayoutParams());
            object = layoutParams instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams)layoutParams : null;
            n3 = 0;
            n11 = 0;
            if (object != null) {
                n3 = object.leftMargin + object.rightMargin;
                n11 = object.topMargin + object.bottomMargin;
            }
            n5 = this.mContentHeight <= 0 ? Integer.MIN_VALUE : (layoutParams.height != -2 ? 1073741824 : Integer.MIN_VALUE);
            int n13 = n10;
            if (layoutParams.height >= 0) {
                n13 = Math.min((int)layoutParams.height, (int)n10);
            }
            int n14 = Math.max((int)0, (int)(n13 - n11));
            n11 = layoutParams.width != -2 ? 1073741824 : Integer.MIN_VALUE;
            n13 = layoutParams.width >= 0 ? Math.min((int)layoutParams.width, (int)n) : n;
            int n15 = Math.max((int)0, (int)(n13 - n3));
            n13 = object != null ? object.gravity : 19;
            n10 = n15;
            if ((n13 & 7) == 1) {
                n10 = n15;
                if (layoutParams.width == -1) {
                    n10 = Math.min((int)n2, (int)n12) * 2;
                }
            }
            object2.measure(View.MeasureSpec.makeMeasureSpec((int)n10, (int)n11), View.MeasureSpec.makeMeasureSpec((int)n14, (int)n5));
            n5 = n - (object2.getMeasuredWidth() + n3);
        }
        if (this.mExpandedActionView == null && n4 != 0) {
            this.measureChildView((View)this.mTitleLayout, n5, View.MeasureSpec.makeMeasureSpec((int)this.mContentHeight, (int)1073741824), 0);
            Math.max((int)0, (int)(n2 - this.mTitleLayout.getMeasuredWidth()));
        }
        if (this.mContentHeight <= 0) {
            n2 = 0;
            for (n = 0; n < n6; ++n) {
                n3 = this.getChildAt(n).getMeasuredHeight() + n9;
                n5 = n2;
                if (n3 > n2) {
                    n5 = n3;
                }
                n2 = n5;
            }
            this.setMeasuredDimension(n7, n2);
        } else {
            this.setMeasuredDimension(n7, n8);
        }
        if (this.mContextView != null) {
            this.mContextView.setContentHeight(this.getMeasuredHeight());
        }
        if (this.mProgressView == null || this.mProgressView.getVisibility() == 8) return;
        {
            this.mProgressView.measure(View.MeasureSpec.makeMeasureSpec((int)(n7 - this.mProgressBarPadding * 2), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredHeight(), (int)Integer.MIN_VALUE));
            return;
        }
    }

    public void onRestoreInstanceState(Parcelable object) {
        SupportMenuItem supportMenuItem;
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        if (object.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && this.mOptionsMenu != null && (supportMenuItem = (SupportMenuItem)this.mOptionsMenu.findItem(object.expandedMenuItemId)) != null) {
            supportMenuItem.expandActionView();
        }
        if (object.isOverflowOpen) {
            this.postShowOverflowMenu();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = this.isOverflowMenuShowing();
        return savedState;
    }

    public void setCallback(ActionBar.OnNavigationListener onNavigationListener) {
        this.mCallback = onNavigationListener;
    }

    public void setCollapsable(boolean bl) {
        this.mIsCollapsable = bl;
    }

    public void setContextView(ActionBarContextView actionBarContextView) {
        this.mContextView = actionBarContextView;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setCustomNavigationView(View view) {
        boolean bl = (this.mDisplayOptions & 16) != 0;
        if (this.mCustomNavView != null && bl) {
            this.removeView(this.mCustomNavView);
        }
        this.mCustomNavView = view;
        if (this.mCustomNavView != null && bl) {
            this.addView(this.mCustomNavView);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDisplayOptions(int n) {
        int n2 = 8;
        int n3 = -1;
        boolean bl = true;
        if (this.mDisplayOptions != -1) {
            n3 = n ^ this.mDisplayOptions;
        }
        this.mDisplayOptions = n;
        if ((n3 & 31) != 0) {
            Drawable drawable2;
            boolean bl2;
            boolean bl3 = (n & 2) != 0;
            int n4 = bl3 && this.mExpandedActionView == null ? 0 : 8;
            this.mHomeLayout.setVisibility(n4);
            if ((n3 & 4) != 0) {
                bl2 = (n & 4) != 0;
                this.mHomeLayout.setUp(bl2);
                if (bl2) {
                    this.setHomeButtonEnabled(true);
                }
            }
            if ((n3 & 1) != 0) {
                n4 = this.mLogo != null && (n & 1) != 0 ? 1 : 0;
                HomeView homeView = this.mHomeLayout;
                drawable2 = n4 != 0 ? this.mLogo : this.mIcon;
                homeView.setIcon(drawable2);
            }
            if ((n3 & 8) != 0) {
                if ((n & 8) != 0) {
                    this.initTitle();
                } else {
                    this.removeView((View)this.mTitleLayout);
                }
            }
            if (this.mTitleLayout != null && (n3 & 6) != 0) {
                boolean bl4 = (this.mDisplayOptions & 4) != 0;
                drawable2 = this.mTitleUpView;
                n4 = n2;
                if (!bl3) {
                    n4 = bl4 ? 0 : 4;
                }
                drawable2.setVisibility(n4);
                drawable2 = this.mTitleLayout;
                bl2 = !bl3 && bl4 ? bl : false;
                drawable2.setEnabled(bl2);
            }
            if ((n3 & 16) != 0 && this.mCustomNavView != null) {
                if ((n & 16) != 0) {
                    this.addView(this.mCustomNavView);
                } else {
                    this.removeView(this.mCustomNavView);
                }
            }
            this.requestLayout();
        } else {
            this.invalidate();
        }
        if (!this.mHomeLayout.isEnabled()) {
            this.mHomeLayout.setContentDescription(null);
            return;
        }
        if ((n & 4) != 0) {
            this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_up_description));
            return;
        }
        this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_home_description));
    }

    public void setDropdownAdapter(SpinnerAdapter spinnerAdapter) {
        this.mSpinnerAdapter = spinnerAdapter;
        if (this.mSpinner != null) {
            this.mSpinner.setAdapter(spinnerAdapter);
        }
    }

    public void setDropdownSelectedPosition(int n) {
        this.mSpinner.setSelection(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setEmbeddedTabView(ScrollingTabContainerView scrollingTabContainerView) {
        if (this.mTabScrollView != null) {
            this.removeView((View)this.mTabScrollView);
        }
        this.mTabScrollView = scrollingTabContainerView;
        boolean bl = scrollingTabContainerView != null;
        this.mIncludeTabs = bl;
        if (this.mIncludeTabs && this.mNavigationMode == 2) {
            this.addView((View)this.mTabScrollView);
            ViewGroup.LayoutParams layoutParams = this.mTabScrollView.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -1;
            scrollingTabContainerView.setAllowCollapse(true);
        }
    }

    public void setHomeAsUpIndicator(int n) {
        this.mHomeLayout.setUpIndicator(n);
    }

    public void setHomeAsUpIndicator(Drawable drawable2) {
        this.mHomeLayout.setUpIndicator(drawable2);
    }

    public void setHomeButtonEnabled(boolean bl) {
        this.mHomeLayout.setEnabled(bl);
        this.mHomeLayout.setFocusable(bl);
        if (!bl) {
            this.mHomeLayout.setContentDescription(null);
            return;
        }
        if ((this.mDisplayOptions & 4) != 0) {
            this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_up_description));
            return;
        }
        this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_home_description));
    }

    public void setIcon(int n) {
        this.setIcon(this.mContext.getResources().getDrawable(n));
    }

    public void setIcon(Drawable drawable2) {
        this.mIcon = drawable2;
        if (drawable2 != null && ((this.mDisplayOptions & 1) == 0 || this.mLogo == null)) {
            this.mHomeLayout.setIcon(drawable2);
        }
        if (this.mExpandedActionView != null) {
            this.mExpandedHomeLayout.setIcon(this.mIcon.getConstantState().newDrawable(this.getResources()));
        }
    }

    public void setLogo(int n) {
        this.setLogo(this.mContext.getResources().getDrawable(n));
    }

    public void setLogo(Drawable drawable2) {
        this.mLogo = drawable2;
        if (drawable2 != null && (this.mDisplayOptions & 1) != 0) {
            this.mHomeLayout.setIcon(drawable2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setMenu(SupportMenu object, MenuPresenter.Callback callback) {
        MenuBuilder menuBuilder;
        if (object == this.mOptionsMenu) {
            return;
        }
        if (this.mOptionsMenu != null) {
            this.mOptionsMenu.removeMenuPresenter(this.mActionMenuPresenter);
            this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
        }
        this.mOptionsMenu = menuBuilder = (MenuBuilder)object;
        if (this.mMenuView != null && (object = (ViewGroup)this.mMenuView.getParent()) != null) {
            object.removeView((View)this.mMenuView);
        }
        if (this.mActionMenuPresenter == null) {
            this.mActionMenuPresenter = new ActionMenuPresenter(this.mContext);
            this.mActionMenuPresenter.setCallback(callback);
            this.mActionMenuPresenter.setId(R.id.action_menu_presenter);
            this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
        }
        callback = new ViewGroup.LayoutParams(-2, -1);
        if (!this.mSplitActionBar) {
            this.mActionMenuPresenter.setExpandedActionViewsExclusive(this.getResources().getBoolean(R.bool.abc_action_bar_expanded_action_views_exclusive));
            this.configPresenters(menuBuilder);
            object = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
            object.initialize(menuBuilder);
            menuBuilder = (ViewGroup)object.getParent();
            if (menuBuilder != null && menuBuilder != this) {
                menuBuilder.removeView((View)object);
            }
            this.addView((View)object, (ViewGroup.LayoutParams)callback);
        } else {
            this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
            this.mActionMenuPresenter.setWidthLimit(this.getContext().getResources().getDisplayMetrics().widthPixels, true);
            this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
            callback.width = -1;
            this.configPresenters(menuBuilder);
            object = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
            if (this.mSplitView != null) {
                menuBuilder = (ViewGroup)object.getParent();
                if (menuBuilder != null && menuBuilder != this.mSplitView) {
                    menuBuilder.removeView((View)object);
                }
                object.setVisibility(this.getAnimatedVisibility());
                this.mSplitView.addView((View)object, (ViewGroup.LayoutParams)callback);
            } else {
                object.setLayoutParams((ViewGroup.LayoutParams)callback);
            }
        }
        this.mMenuView = object;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setNavigationMode(int n) {
        int n2 = this.mNavigationMode;
        if (n != n2) {
            switch (n2) {
                case 1: {
                    if (this.mListNavLayout != null) {
                        this.removeView((View)this.mListNavLayout);
                    }
                }
                default: {
                    break;
                }
                case 2: {
                    if (this.mTabScrollView == null || !this.mIncludeTabs) break;
                    this.removeView((View)this.mTabScrollView);
                }
            }
            switch (n) {
                case 1: {
                    if (this.mSpinner == null) {
                        this.mSpinner = new SpinnerICS(this.mContext, null, R.attr.actionDropDownStyle);
                        this.mListNavLayout = (LinearLayout)LayoutInflater.from((Context)this.mContext).inflate(R.layout.abc_action_bar_view_list_nav_layout, null);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
                        layoutParams.gravity = 17;
                        this.mListNavLayout.addView((View)this.mSpinner, (ViewGroup.LayoutParams)layoutParams);
                    }
                    if (this.mSpinner.getAdapter() != this.mSpinnerAdapter) {
                        this.mSpinner.setAdapter(this.mSpinnerAdapter);
                    }
                    this.mSpinner.setOnItemSelectedListener(this.mNavItemSelectedListener);
                    this.addView((View)this.mListNavLayout);
                }
                default: {
                    break;
                }
                case 2: {
                    if (this.mTabScrollView == null || !this.mIncludeTabs) break;
                    this.addView((View)this.mTabScrollView);
                }
            }
            this.mNavigationMode = n;
            this.requestLayout();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setSplitActionBar(boolean bl) {
        if (this.mSplitActionBar != bl) {
            Object object;
            if (this.mMenuView != null) {
                object = (ViewGroup)this.mMenuView.getParent();
                if (object != null) {
                    object.removeView((View)this.mMenuView);
                }
                if (bl) {
                    if (this.mSplitView != null) {
                        this.mSplitView.addView((View)this.mMenuView);
                    }
                    this.mMenuView.getLayoutParams().width = -1;
                } else {
                    this.addView((View)this.mMenuView);
                    this.mMenuView.getLayoutParams().width = -2;
                }
                this.mMenuView.requestLayout();
            }
            if (this.mSplitView != null) {
                object = this.mSplitView;
                int n = bl ? 0 : 8;
                object.setVisibility(n);
            }
            if (this.mActionMenuPresenter != null) {
                if (!bl) {
                    this.mActionMenuPresenter.setExpandedActionViewsExclusive(this.getResources().getBoolean(R.bool.abc_action_bar_expanded_action_views_exclusive));
                } else {
                    this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
                    this.mActionMenuPresenter.setWidthLimit(this.getContext().getResources().getDisplayMetrics().widthPixels, true);
                    this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
                }
            }
            super.setSplitActionBar(bl);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSubtitle(CharSequence charSequence) {
        int n = 0;
        this.mSubtitle = charSequence;
        if (this.mSubtitleView != null) {
            this.mSubtitleView.setText(charSequence);
            TextView textView = this.mSubtitleView;
            int n2 = charSequence != null ? 0 : 8;
            textView.setVisibility(n2);
            n2 = this.mExpandedActionView == null && (this.mDisplayOptions & 8) != 0 && (!TextUtils.isEmpty((CharSequence)this.mTitle) || !TextUtils.isEmpty((CharSequence)this.mSubtitle)) ? 1 : 0;
            charSequence = this.mTitleLayout;
            n2 = n2 != 0 ? n : 8;
            charSequence.setVisibility(n2);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mUserTitle = true;
        this.setTitleImpl(charSequence);
    }

    public void setWindowCallback(Window.Callback callback) {
        this.mWindowCallback = callback;
    }

    public void setWindowTitle(CharSequence charSequence) {
        if (!this.mUserTitle) {
            this.setTitleImpl(charSequence);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    private class ExpandedActionViewMenuPresenter
    implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        private ExpandedActionViewMenuPresenter() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewCollapsed();
            }
            ActionBarView.this.removeView(ActionBarView.this.mExpandedActionView);
            ActionBarView.this.removeView((View)ActionBarView.this.mExpandedHomeLayout);
            ActionBarView.this.mExpandedActionView = null;
            if ((ActionBarView.this.mDisplayOptions & 2) != 0) {
                ActionBarView.this.mHomeLayout.setVisibility(0);
            }
            if ((ActionBarView.this.mDisplayOptions & 8) != 0) {
                if (ActionBarView.this.mTitleLayout == null) {
                    ActionBarView.this.initTitle();
                } else {
                    ActionBarView.this.mTitleLayout.setVisibility(0);
                }
            }
            if (ActionBarView.this.mTabScrollView != null && ActionBarView.this.mNavigationMode == 2) {
                ActionBarView.this.mTabScrollView.setVisibility(0);
            }
            if (ActionBarView.this.mSpinner != null && ActionBarView.this.mNavigationMode == 1) {
                ActionBarView.this.mSpinner.setVisibility(0);
            }
            if (ActionBarView.this.mCustomNavView != null && (ActionBarView.this.mDisplayOptions & 16) != 0) {
                ActionBarView.this.mCustomNavView.setVisibility(0);
            }
            ActionBarView.this.mExpandedHomeLayout.setIcon(null);
            this.mCurrentExpandedItem = null;
            ActionBarView.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }

        @Override
        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            ActionBarView.this.mExpandedActionView = menuItemImpl.getActionView();
            ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.mIcon.getConstantState().newDrawable(ActionBarView.this.getResources()));
            this.mCurrentExpandedItem = menuItemImpl;
            if (ActionBarView.this.mExpandedActionView.getParent() != ActionBarView.this) {
                ActionBarView.this.addView(ActionBarView.this.mExpandedActionView);
            }
            if (ActionBarView.this.mExpandedHomeLayout.getParent() != ActionBarView.this) {
                ActionBarView.this.addView((View)ActionBarView.this.mExpandedHomeLayout);
            }
            ActionBarView.this.mHomeLayout.setVisibility(8);
            if (ActionBarView.this.mTitleLayout != null) {
                ActionBarView.this.mTitleLayout.setVisibility(8);
            }
            if (ActionBarView.this.mTabScrollView != null) {
                ActionBarView.this.mTabScrollView.setVisibility(8);
            }
            if (ActionBarView.this.mSpinner != null) {
                ActionBarView.this.mSpinner.setVisibility(8);
            }
            if (ActionBarView.this.mCustomNavView != null) {
                ActionBarView.this.mCustomNavView.setVisibility(8);
            }
            ActionBarView.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        @Override
        public boolean flagActionItems() {
            return false;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public MenuView getMenuView(ViewGroup viewGroup) {
            return null;
        }

        @Override
        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            if (this.mMenu != null && this.mCurrentExpandedItem != null) {
                this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
            }
            this.mMenu = menuBuilder;
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        @Override
        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override
        public Parcelable onSaveInstanceState() {
            return null;
        }

        @Override
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        @Override
        public void setCallback(MenuPresenter.Callback callback) {
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void updateMenuView(boolean var1_1) {
            if (this.mCurrentExpandedItem == null) return;
            var3_3 = var4_2 = false;
            if (this.mMenu == null) ** GOTO lbl13
            var5_4 = this.mMenu.size();
            var2_5 = 0;
            do {
                var3_3 = var4_2;
                if (var2_5 < var5_4) {
                    if ((SupportMenuItem)this.mMenu.getItem(var2_5) == this.mCurrentExpandedItem) {
                        return;
                    }
                } else {
                    if (var3_3 != false) return;
lbl13: // 2 sources:
                    this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                    return;
                }
                ++var2_5;
            } while (true);
        }
    }

    private static class HomeView
    extends FrameLayout {
        private Drawable mDefaultUpIndicator;
        private ImageView mIconView;
        private int mUpIndicatorRes;
        private ImageView mUpView;
        private int mUpWidth;

        public HomeView(Context context) {
            this(context, null);
        }

        public HomeView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            CharSequence charSequence = this.getContentDescription();
            if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                accessibilityEvent.getText().add((Object)charSequence);
            }
            return true;
        }

        public int getLeftOffset() {
            if (this.mUpView.getVisibility() == 8) {
                return this.mUpWidth;
            }
            return 0;
        }

        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            if (this.mUpIndicatorRes != 0) {
                this.setUpIndicator(this.mUpIndicatorRes);
            }
        }

        protected void onFinishInflate() {
            this.mUpView = (ImageView)this.findViewById(R.id.up);
            this.mIconView = (ImageView)this.findViewById(R.id.home);
            this.mDefaultUpIndicator = this.mUpView.getDrawable();
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            FrameLayout.LayoutParams layoutParams;
            int n5;
            int n6 = (n4 - n2) / 2;
            n2 = 0;
            n4 = n;
            if (this.mUpView.getVisibility() != 8) {
                layoutParams = (FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
                n2 = this.mUpView.getMeasuredHeight();
                n4 = this.mUpView.getMeasuredWidth();
                n5 = n6 - n2 / 2;
                this.mUpView.layout(0, n5, n4, n5 + n2);
                n2 = layoutParams.leftMargin + n4 + layoutParams.rightMargin;
                n4 = n + n2;
            }
            layoutParams = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
            n = this.mIconView.getMeasuredHeight();
            n5 = this.mIconView.getMeasuredWidth();
            n3 = (n3 - n4) / 2;
            n3 = Math.max((int)layoutParams.topMargin, (int)(n6 - n / 2));
            this.mIconView.layout(n2, n3, (n2 += Math.max((int)layoutParams.leftMargin, (int)(n3 - n5 / 2))) + n5, n3 + n);
        }

        /*
         * Enabled aggressive block sorting
         */
        protected void onMeasure(int n, int n2) {
            this.measureChildWithMargins((View)this.mUpView, n, 0, n2, 0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
            this.mUpWidth = layoutParams.leftMargin + this.mUpView.getMeasuredWidth() + layoutParams.rightMargin;
            int n3 = this.mUpView.getVisibility() == 8 ? 0 : this.mUpWidth;
            int n4 = layoutParams.topMargin;
            int n5 = this.mUpView.getMeasuredHeight();
            int n6 = layoutParams.bottomMargin;
            this.measureChildWithMargins((View)this.mIconView, n, n3, n2, 0);
            layoutParams = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
            n3 += layoutParams.leftMargin + this.mIconView.getMeasuredWidth() + layoutParams.rightMargin;
            n4 = Math.max((int)(n4 + n5 + n6), (int)(layoutParams.topMargin + this.mIconView.getMeasuredHeight() + layoutParams.bottomMargin));
            n6 = View.MeasureSpec.getMode((int)n);
            n5 = View.MeasureSpec.getMode((int)n2);
            n = View.MeasureSpec.getSize((int)n);
            n2 = View.MeasureSpec.getSize((int)n2);
            switch (n6) {
                default: {
                    n = n3;
                    break;
                }
                case Integer.MIN_VALUE: {
                    n = Math.min((int)n3, (int)n);
                    break;
                }
                case 1073741824: 
            }
            switch (n5) {
                default: {
                    n2 = n4;
                    break;
                }
                case Integer.MIN_VALUE: {
                    n2 = Math.min((int)n4, (int)n2);
                    break;
                }
                case 1073741824: 
            }
            this.setMeasuredDimension(n, n2);
        }

        public void setIcon(Drawable drawable2) {
            this.mIconView.setImageDrawable(drawable2);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setUp(boolean bl) {
            ImageView imageView = this.mUpView;
            int n = bl ? 0 : 8;
            imageView.setVisibility(n);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setUpIndicator(int n) {
            this.mUpIndicatorRes = n;
            ImageView imageView = this.mUpView;
            Drawable drawable2 = n != 0 ? this.getResources().getDrawable(n) : this.mDefaultUpIndicator;
            imageView.setImageDrawable(drawable2);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setUpIndicator(Drawable drawable2) {
            ImageView imageView = this.mUpView;
            if (drawable2 == null) {
                drawable2 = this.mDefaultUpIndicator;
            }
            imageView.setImageDrawable(drawable2);
            this.mUpIndicatorRes = 0;
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
        int expandedMenuItemId;
        boolean isOverflowOpen;

        /*
         * Enabled aggressive block sorting
         */
        private SavedState(Parcel parcel) {
            super(parcel);
            this.expandedMenuItemId = parcel.readInt();
            boolean bl = parcel.readInt() != 0;
            this.isOverflowOpen = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.expandedMenuItemId);
            n = this.isOverflowOpen ? 1 : 0;
            parcel.writeInt(n);
        }

    }

}

