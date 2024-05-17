/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Bundle
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.view.Window$Callback
 *  android.widget.FrameLayout
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 */
package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivityDelegate;
import android.support.v7.app.ActionBarImplBase;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.ListMenuPresenter;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.internal.widget.ProgressBarICS;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

class ActionBarActivityDelegateBase
extends ActionBarActivityDelegate
implements MenuPresenter.Callback,
MenuBuilder.Callback {
    private static final int[] ACTION_BAR_DRAWABLE_TOGGLE_ATTRS = new int[]{R.attr.homeAsUpIndicator};
    private static final String TAG = "ActionBarActivityDelegateBase";
    private ActionBarView mActionBarView;
    private ActionMode mActionMode;
    private boolean mClosingActionMenu;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private ListMenuPresenter mListMenuPresenter;
    private MenuBuilder mMenu;
    private Bundle mPanelFrozenActionViewState;
    private boolean mPanelIsPrepared;
    private boolean mPanelRefreshContent;
    private boolean mSubDecorInstalled;
    private CharSequence mTitleToSet;

    ActionBarActivityDelegateBase(ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applyFixedSizeWindow() {
        TypedArray typedArray = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
        TypedValue typedValue = null;
        TypedValue typedValue2 = null;
        TypedValue typedValue3 = null;
        TypedValue typedValue4 = null;
        TypedValue typedValue5 = null;
        TypedValue typedValue6 = null;
        DisplayMetrics displayMetrics = null;
        DisplayMetrics displayMetrics2 = null;
        if (typedArray.hasValue(3)) {
            typedValue = typedValue2;
            if (!false) {
                typedValue = new TypedValue();
            }
            typedArray.getValue(3, typedValue);
        }
        if (typedArray.hasValue(5)) {
            typedValue3 = typedValue4;
            if (!false) {
                typedValue3 = new TypedValue();
            }
            typedArray.getValue(5, typedValue3);
        }
        if (typedArray.hasValue(6)) {
            typedValue5 = typedValue6;
            if (!false) {
                typedValue5 = new TypedValue();
            }
            typedArray.getValue(6, typedValue5);
        }
        if (typedArray.hasValue(4)) {
            displayMetrics = displayMetrics2;
            if (!false) {
                displayMetrics = new TypedValue();
            }
            typedArray.getValue(4, (TypedValue)displayMetrics);
        }
        displayMetrics2 = this.mActivity.getResources().getDisplayMetrics();
        int n = displayMetrics2.widthPixels < displayMetrics2.heightPixels ? 1 : 0;
        int n2 = -1;
        int n3 = -1;
        if (n == 0) {
            typedValue3 = typedValue;
        }
        int n4 = n2;
        if (typedValue3 != null) {
            n4 = n2;
            if (typedValue3.type != 0) {
                if (typedValue3.type == 5) {
                    n4 = (int)typedValue3.getDimension(displayMetrics2);
                } else {
                    n4 = n2;
                    if (typedValue3.type == 6) {
                        n4 = (int)typedValue3.getFraction((float)displayMetrics2.widthPixels, (float)displayMetrics2.widthPixels);
                    }
                }
            }
        }
        if (n == 0) {
            typedValue5 = displayMetrics;
        }
        n = n3;
        if (typedValue5 != null) {
            n = n3;
            if (typedValue5.type != 0) {
                if (typedValue5.type == 5) {
                    n = (int)typedValue5.getDimension(displayMetrics2);
                } else {
                    n = n3;
                    if (typedValue5.type == 6) {
                        n = (int)typedValue5.getFraction((float)displayMetrics2.heightPixels, (float)displayMetrics2.heightPixels);
                    }
                }
            }
        }
        if (n4 != -1 || n != -1) {
            this.mActivity.getWindow().setLayout(n4, n);
        }
        typedArray.recycle();
    }

    private ProgressBarICS getCircularProgressBar() {
        ProgressBarICS progressBarICS = (ProgressBarICS)this.mActionBarView.findViewById(R.id.progress_circular);
        if (progressBarICS != null) {
            progressBarICS.setVisibility(4);
        }
        return progressBarICS;
    }

    private ProgressBarICS getHorizontalProgressBar() {
        ProgressBarICS progressBarICS = (ProgressBarICS)this.mActionBarView.findViewById(R.id.progress_horizontal);
        if (progressBarICS != null) {
            progressBarICS.setVisibility(4);
        }
        return progressBarICS;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private MenuView getListMenuView(Context context, MenuPresenter.Callback callback) {
        if (this.mMenu == null) {
            return null;
        }
        if (this.mListMenuPresenter == null) {
            TypedArray typedArray = context.obtainStyledAttributes(R.styleable.Theme);
            int n = typedArray.getResourceId(4, R.style.Theme_AppCompat_CompactMenu);
            typedArray.recycle();
            this.mListMenuPresenter = new ListMenuPresenter(R.layout.abc_list_menu_item_layout, n);
            this.mListMenuPresenter.setCallback(callback);
            this.mMenu.addMenuPresenter(this.mListMenuPresenter);
            do {
                return this.mListMenuPresenter.getMenuView((ViewGroup)new FrameLayout(context));
                break;
            } while (true);
        }
        this.mListMenuPresenter.updateMenuView(false);
        return this.mListMenuPresenter.getMenuView((ViewGroup)new FrameLayout(context));
    }

    private void hideProgressBars(ProgressBarICS progressBarICS, ProgressBarICS progressBarICS2) {
        if (this.mFeatureIndeterminateProgress && progressBarICS2.getVisibility() == 0) {
            progressBarICS2.setVisibility(4);
        }
        if (this.mFeatureProgress && progressBarICS.getVisibility() == 0) {
            progressBarICS.setVisibility(4);
        }
    }

    private boolean initializePanelMenu() {
        this.mMenu = new MenuBuilder(this.getActionBarThemedContext());
        this.mMenu.setCallback(this);
        return true;
    }

    private boolean preparePanel() {
        if (this.mPanelIsPrepared) {
            return true;
        }
        if (this.mMenu == null || this.mPanelRefreshContent) {
            if (!(this.mMenu != null || this.initializePanelMenu() && this.mMenu != null)) {
                return false;
            }
            if (this.mActionBarView != null) {
                this.mActionBarView.setMenu(this.mMenu, this);
            }
            this.mMenu.stopDispatchingItemsChanged();
            if (!this.mActivity.superOnCreatePanelMenu(0, this.mMenu)) {
                this.mMenu = null;
                if (this.mActionBarView != null) {
                    this.mActionBarView.setMenu(null, this);
                }
                return false;
            }
            this.mPanelRefreshContent = false;
        }
        this.mMenu.stopDispatchingItemsChanged();
        if (this.mPanelFrozenActionViewState != null) {
            this.mMenu.restoreActionViewStates(this.mPanelFrozenActionViewState);
            this.mPanelFrozenActionViewState = null;
        }
        if (!this.mActivity.superOnPreparePanel(0, null, this.mMenu)) {
            if (this.mActionBarView != null) {
                this.mActionBarView.setMenu(null, this);
            }
            this.mMenu.startDispatchingItemsChanged();
            return false;
        }
        this.mMenu.startDispatchingItemsChanged();
        this.mPanelIsPrepared = true;
        return true;
    }

    private void reopenMenu(MenuBuilder menuBuilder, boolean bl) {
        if (this.mActionBarView != null && this.mActionBarView.isOverflowReserved()) {
            if (!this.mActionBarView.isOverflowMenuShowing() || !bl) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mActionBarView.showOverflowMenu();
                }
                return;
            }
            this.mActionBarView.hideOverflowMenu();
            return;
        }
        menuBuilder.close();
    }

    private void showProgressBars(ProgressBarICS progressBarICS, ProgressBarICS progressBarICS2) {
        if (this.mFeatureIndeterminateProgress && progressBarICS2.getVisibility() == 4) {
            progressBarICS2.setVisibility(0);
        }
        if (this.mFeatureProgress && progressBarICS.getProgress() < 10000) {
            progressBarICS.setVisibility(0);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateProgressBars(int n) {
        ProgressBarICS progressBarICS = this.getCircularProgressBar();
        ProgressBarICS progressBarICS2 = this.getHorizontalProgressBar();
        if (n == -1) {
            if (this.mFeatureProgress) {
                n = progressBarICS2.getProgress();
                n = progressBarICS2.isIndeterminate() || n < 10000 ? 0 : 4;
                progressBarICS2.setVisibility(n);
            }
            if (!this.mFeatureIndeterminateProgress) return;
            {
                progressBarICS.setVisibility(0);
            }
            return;
        }
        if (n == -2) {
            if (this.mFeatureProgress) {
                progressBarICS2.setVisibility(8);
            }
            if (!this.mFeatureIndeterminateProgress) return;
            {
                progressBarICS.setVisibility(8);
                return;
            }
        }
        if (n == -3) {
            progressBarICS2.setIndeterminate(true);
            return;
        }
        if (n == -4) {
            progressBarICS2.setIndeterminate(false);
            return;
        }
        if (n < 0 || n > 10000) return;
        {
            progressBarICS2.setProgress(n + 0);
            if (n < 10000) {
                this.showProgressBars(progressBarICS2, progressBarICS);
                return;
            }
        }
        this.hideProgressBars(progressBarICS2, progressBarICS);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mActivity.findViewById(16908290)).addView(view, layoutParams);
        this.mActivity.onSupportContentChanged();
    }

    @Override
    public ActionBar createSupportActionBar() {
        this.ensureSubDecor();
        return new ActionBarImplBase(this.mActivity, this.mActivity);
    }

    /*
     * Enabled aggressive block sorting
     */
    final void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            if (this.mHasActionBar) {
                boolean bl;
                boolean bl2;
                Object object;
                if (this.mOverlayActionBar) {
                    this.mActivity.superSetContentView(R.layout.abc_action_bar_decor_overlay);
                } else {
                    this.mActivity.superSetContentView(R.layout.abc_action_bar_decor);
                }
                this.mActionBarView = (ActionBarView)this.mActivity.findViewById(R.id.action_bar);
                this.mActionBarView.setWindowCallback((Window.Callback)this.mActivity);
                if (this.mFeatureProgress) {
                    this.mActionBarView.initProgress();
                }
                if (this.mFeatureIndeterminateProgress) {
                    this.mActionBarView.initIndeterminateProgress();
                }
                if (bl = "splitActionBarWhenNarrow".equals((Object)this.getUiOptionsFromMetadata())) {
                    bl2 = this.mActivity.getResources().getBoolean(R.bool.abc_split_action_bar_is_narrow);
                } else {
                    object = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
                    bl2 = object.getBoolean(2, false);
                    object.recycle();
                }
                if ((object = (ActionBarContainer)this.mActivity.findViewById(R.id.split_action_bar)) != null) {
                    this.mActionBarView.setSplitView((ActionBarContainer)((Object)object));
                    this.mActionBarView.setSplitActionBar(bl2);
                    this.mActionBarView.setSplitWhenNarrow(bl);
                    ActionBarContextView actionBarContextView = (ActionBarContextView)this.mActivity.findViewById(R.id.action_context_bar);
                    actionBarContextView.setSplitView((ActionBarContainer)((Object)object));
                    actionBarContextView.setSplitActionBar(bl2);
                    actionBarContextView.setSplitWhenNarrow(bl);
                }
            } else {
                this.mActivity.superSetContentView(R.layout.abc_simple_decor);
            }
            this.mActivity.findViewById(16908290).setId(-1);
            this.mActivity.findViewById(R.id.action_bar_activity_content).setId(16908290);
            if (this.mTitleToSet != null) {
                this.mActionBarView.setWindowTitle(this.mTitleToSet);
                this.mTitleToSet = null;
            }
            this.applyFixedSizeWindow();
            this.mSubDecorInstalled = true;
            this.mActivity.getWindow().getDecorView().post(new Runnable(){

                public void run() {
                    ActionBarActivityDelegateBase.this.supportInvalidateOptionsMenu();
                }
            });
        }
    }

    @Override
    int getHomeAsUpIndicatorAttrId() {
        return R.attr.homeAsUpIndicator;
    }

    @Override
    public boolean onBackPressed() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
            return true;
        }
        if (this.mActionBarView != null && this.mActionBarView.hasExpandedActionView()) {
            this.mActionBarView.collapseActionView();
            return true;
        }
        return false;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mActivity.closeOptionsMenu();
        this.mActionBarView.dismissPopupMenus();
        this.mClosingActionMenu = false;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            ((ActionBarImplBase)this.getSupportActionBar()).onConfigurationChanged(configuration);
        }
    }

    @Override
    public void onContentChanged() {
    }

    @Override
    public boolean onCreatePanelMenu(int n, Menu menu2) {
        if (n != 0) {
            return this.mActivity.superOnCreatePanelMenu(n, menu2);
        }
        return false;
    }

    @Override
    public View onCreatePanelView(int n) {
        View view;
        View view2 = view = null;
        if (n == 0) {
            view2 = view;
            if (this.preparePanel()) {
                view2 = (View)this.getListMenuView((Context)this.mActivity, this);
            }
        }
        return view2;
    }

    @Override
    public boolean onMenuItemSelected(int n, MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (n == 0) {
            menuItem2 = MenuWrapperFactory.createMenuItemWrapper(menuItem);
        }
        return this.mActivity.superOnMenuItemSelected(n, menuItem2);
    }

    @Override
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.mActivity.onMenuItemSelected(0, menuItem);
    }

    @Override
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        this.reopenMenu(menuBuilder, true);
    }

    @Override
    public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
        return false;
    }

    @Override
    public void onPostResume() {
        ActionBarImplBase actionBarImplBase = (ActionBarImplBase)this.getSupportActionBar();
        if (actionBarImplBase != null) {
            actionBarImplBase.setShowHideAnimationEnabled(true);
        }
    }

    @Override
    public boolean onPreparePanel(int n, View view, Menu menu2) {
        if (n != 0) {
            return this.mActivity.superOnPreparePanel(n, view, menu2);
        }
        return false;
    }

    @Override
    public void onStop() {
        ActionBarImplBase actionBarImplBase = (ActionBarImplBase)this.getSupportActionBar();
        if (actionBarImplBase != null) {
            actionBarImplBase.setShowHideAnimationEnabled(false);
        }
    }

    @Override
    public void onTitleChanged(CharSequence charSequence) {
        if (this.mActionBarView != null) {
            this.mActionBarView.setWindowTitle(charSequence);
            return;
        }
        this.mTitleToSet = charSequence;
    }

    @Override
    public void setContentView(int n) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
        viewGroup.removeAllViews();
        this.mActivity.getLayoutInflater().inflate(n, viewGroup);
        this.mActivity.onSupportContentChanged();
    }

    @Override
    public void setContentView(View view) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mActivity.onSupportContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.mActivity.onSupportContentChanged();
    }

    @Override
    void setSupportProgress(int n) {
        this.updateProgressBars(n + 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void setSupportProgressBarIndeterminate(boolean bl) {
        int n = bl ? -3 : -4;
        this.updateProgressBars(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void setSupportProgressBarIndeterminateVisibility(boolean bl) {
        int n = bl ? -1 : -2;
        this.updateProgressBars(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void setSupportProgressBarVisibility(boolean bl) {
        int n = bl ? -1 : -2;
        this.updateProgressBars(n);
    }

    @Override
    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        callback = new ActionModeCallbackWrapper(callback);
        ActionBarImplBase actionBarImplBase = (ActionBarImplBase)this.getSupportActionBar();
        if (actionBarImplBase != null) {
            this.mActionMode = actionBarImplBase.startActionMode(callback);
        }
        if (this.mActionMode != null) {
            this.mActivity.onSupportActionModeStarted(this.mActionMode);
        }
        return this.mActionMode;
    }

    @Override
    public void supportInvalidateOptionsMenu() {
        if (this.mMenu != null) {
            Bundle bundle = new Bundle();
            this.mMenu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                this.mPanelFrozenActionViewState = bundle;
            }
            this.mMenu.stopDispatchingItemsChanged();
            this.mMenu.clear();
        }
        this.mPanelRefreshContent = true;
        if (this.mActionBarView != null) {
            this.mPanelIsPrepared = false;
            this.preparePanel();
        }
    }

    @Override
    public boolean supportRequestWindowFeature(int n) {
        switch (n) {
            default: {
                return this.mActivity.requestWindowFeature(n);
            }
            case 8: {
                this.mHasActionBar = true;
                return true;
            }
            case 9: {
                this.mOverlayActionBar = true;
                return true;
            }
            case 2: {
                this.mFeatureProgress = true;
                return true;
            }
            case 5: 
        }
        this.mFeatureIndeterminateProgress = true;
        return true;
    }

    private class ActionModeCallbackWrapper
    implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapper(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu2) {
            return this.mWrapped.onCreateActionMode(actionMode, menu2);
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            ActionBarActivityDelegateBase.this.mActivity.onSupportActionModeFinished(actionMode);
            ActionBarActivityDelegateBase.this.mActionMode = null;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu2) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu2);
        }
    }

}

