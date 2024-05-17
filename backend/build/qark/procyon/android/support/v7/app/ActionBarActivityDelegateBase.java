// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarContainer;
import android.view.Window$Callback;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.Menu;
import android.support.v4.internal.view.SupportMenu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.support.v7.internal.view.menu.MenuView;
import android.content.Context;
import android.support.v7.internal.widget.ProgressBarICS;
import android.util.DisplayMetrics;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.support.v7.appcompat.R;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuPresenter;
import android.support.v7.view.ActionMode;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuPresenter;

class ActionBarActivityDelegateBase extends ActionBarActivityDelegate implements MenuPresenter.Callback, MenuBuilder.Callback
{
    private static final int[] ACTION_BAR_DRAWABLE_TOGGLE_ATTRS;
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
    
    static {
        ACTION_BAR_DRAWABLE_TOGGLE_ATTRS = new int[] { R.attr.homeAsUpIndicator };
    }
    
    ActionBarActivityDelegateBase(final ActionBarActivity actionBarActivity) {
        super(actionBarActivity);
    }
    
    private void applyFixedSizeWindow() {
        final TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
        TypedValue typedValue = null;
        final TypedValue typedValue2 = null;
        TypedValue typedValue3 = null;
        final TypedValue typedValue4 = null;
        TypedValue typedValue5 = null;
        final TypedValue typedValue6 = null;
        TypedValue typedValue7 = null;
        final TypedValue typedValue8 = null;
        if (obtainStyledAttributes.hasValue(3)) {
            typedValue = typedValue2;
            if (!false) {
                typedValue = new TypedValue();
            }
            obtainStyledAttributes.getValue(3, typedValue);
        }
        if (obtainStyledAttributes.hasValue(5)) {
            typedValue3 = typedValue4;
            if (!false) {
                typedValue3 = new TypedValue();
            }
            obtainStyledAttributes.getValue(5, typedValue3);
        }
        if (obtainStyledAttributes.hasValue(6)) {
            typedValue5 = typedValue6;
            if (!false) {
                typedValue5 = new TypedValue();
            }
            obtainStyledAttributes.getValue(6, typedValue5);
        }
        if (obtainStyledAttributes.hasValue(4)) {
            typedValue7 = typedValue8;
            if (!false) {
                typedValue7 = new TypedValue();
            }
            obtainStyledAttributes.getValue(4, typedValue7);
        }
        final DisplayMetrics displayMetrics = this.mActivity.getResources().getDisplayMetrics();
        boolean b;
        if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
            b = true;
        }
        else {
            b = false;
        }
        final int n = -1;
        final int n2 = -1;
        if (!b) {
            typedValue3 = typedValue;
        }
        int n3 = n;
        if (typedValue3 != null) {
            n3 = n;
            if (typedValue3.type != 0) {
                if (typedValue3.type == 5) {
                    n3 = (int)typedValue3.getDimension(displayMetrics);
                }
                else {
                    n3 = n;
                    if (typedValue3.type == 6) {
                        n3 = (int)typedValue3.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                    }
                }
            }
        }
        if (!b) {
            typedValue5 = typedValue7;
        }
        int n4 = n2;
        if (typedValue5 != null) {
            n4 = n2;
            if (typedValue5.type != 0) {
                if (typedValue5.type == 5) {
                    n4 = (int)typedValue5.getDimension(displayMetrics);
                }
                else {
                    n4 = n2;
                    if (typedValue5.type == 6) {
                        n4 = (int)typedValue5.getFraction((float)displayMetrics.heightPixels, (float)displayMetrics.heightPixels);
                    }
                }
            }
        }
        if (n3 != -1 || n4 != -1) {
            this.mActivity.getWindow().setLayout(n3, n4);
        }
        obtainStyledAttributes.recycle();
    }
    
    private ProgressBarICS getCircularProgressBar() {
        final ProgressBarICS progressBarICS = (ProgressBarICS)this.mActionBarView.findViewById(R.id.progress_circular);
        if (progressBarICS != null) {
            progressBarICS.setVisibility(4);
        }
        return progressBarICS;
    }
    
    private ProgressBarICS getHorizontalProgressBar() {
        final ProgressBarICS progressBarICS = (ProgressBarICS)this.mActionBarView.findViewById(R.id.progress_horizontal);
        if (progressBarICS != null) {
            progressBarICS.setVisibility(4);
        }
        return progressBarICS;
    }
    
    private MenuView getListMenuView(final Context context, final MenuPresenter.Callback callback) {
        if (this.mMenu == null) {
            return null;
        }
        if (this.mListMenuPresenter == null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.Theme);
            final int resourceId = obtainStyledAttributes.getResourceId(4, R.style.Theme_AppCompat_CompactMenu);
            obtainStyledAttributes.recycle();
            (this.mListMenuPresenter = new ListMenuPresenter(R.layout.abc_list_menu_item_layout, resourceId)).setCallback(callback);
            this.mMenu.addMenuPresenter(this.mListMenuPresenter);
        }
        else {
            this.mListMenuPresenter.updateMenuView(false);
        }
        return this.mListMenuPresenter.getMenuView((ViewGroup)new FrameLayout(context));
    }
    
    private void hideProgressBars(final ProgressBarICS progressBarICS, final ProgressBarICS progressBarICS2) {
        if (this.mFeatureIndeterminateProgress && progressBarICS2.getVisibility() == 0) {
            progressBarICS2.setVisibility(4);
        }
        if (this.mFeatureProgress && progressBarICS.getVisibility() == 0) {
            progressBarICS.setVisibility(4);
        }
    }
    
    private boolean initializePanelMenu() {
        (this.mMenu = new MenuBuilder(this.getActionBarThemedContext())).setCallback((MenuBuilder.Callback)this);
        return true;
    }
    
    private boolean preparePanel() {
        if (this.mPanelIsPrepared) {
            return true;
        }
        if (this.mMenu == null || this.mPanelRefreshContent) {
            if (this.mMenu == null && (!this.initializePanelMenu() || this.mMenu == null)) {
                return false;
            }
            if (this.mActionBarView != null) {
                this.mActionBarView.setMenu(this.mMenu, this);
            }
            this.mMenu.stopDispatchingItemsChanged();
            if (!this.mActivity.superOnCreatePanelMenu(0, (Menu)this.mMenu)) {
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
        if (!this.mActivity.superOnPreparePanel(0, null, (Menu)this.mMenu)) {
            if (this.mActionBarView != null) {
                this.mActionBarView.setMenu(null, this);
            }
            this.mMenu.startDispatchingItemsChanged();
            return false;
        }
        this.mMenu.startDispatchingItemsChanged();
        return this.mPanelIsPrepared = true;
    }
    
    private void reopenMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (this.mActionBarView == null || !this.mActionBarView.isOverflowReserved()) {
            menuBuilder.close();
            return;
        }
        if (!this.mActionBarView.isOverflowMenuShowing() || !b) {
            if (this.mActionBarView.getVisibility() == 0) {
                this.mActionBarView.showOverflowMenu();
            }
            return;
        }
        this.mActionBarView.hideOverflowMenu();
    }
    
    private void showProgressBars(final ProgressBarICS progressBarICS, final ProgressBarICS progressBarICS2) {
        if (this.mFeatureIndeterminateProgress && progressBarICS2.getVisibility() == 4) {
            progressBarICS2.setVisibility(0);
        }
        if (this.mFeatureProgress && progressBarICS.getProgress() < 10000) {
            progressBarICS.setVisibility(0);
        }
    }
    
    private void updateProgressBars(int progress) {
        final ProgressBarICS circularProgressBar = this.getCircularProgressBar();
        final ProgressBarICS horizontalProgressBar = this.getHorizontalProgressBar();
        if (progress == -1) {
            if (this.mFeatureProgress) {
                progress = horizontalProgressBar.getProgress();
                if (horizontalProgressBar.isIndeterminate() || progress < 10000) {
                    progress = 0;
                }
                else {
                    progress = 4;
                }
                horizontalProgressBar.setVisibility(progress);
            }
            if (this.mFeatureIndeterminateProgress) {
                circularProgressBar.setVisibility(0);
            }
        }
        else if (progress == -2) {
            if (this.mFeatureProgress) {
                horizontalProgressBar.setVisibility(8);
            }
            if (this.mFeatureIndeterminateProgress) {
                circularProgressBar.setVisibility(8);
            }
        }
        else {
            if (progress == -3) {
                horizontalProgressBar.setIndeterminate(true);
                return;
            }
            if (progress == -4) {
                horizontalProgressBar.setIndeterminate(false);
                return;
            }
            if (progress >= 0 && progress <= 10000) {
                horizontalProgressBar.setProgress(progress + 0);
                if (progress < 10000) {
                    this.showProgressBars(horizontalProgressBar, circularProgressBar);
                    return;
                }
                this.hideProgressBars(horizontalProgressBar, circularProgressBar);
            }
        }
    }
    
    public void addContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mActivity.findViewById(16908290)).addView(view, viewGroup$LayoutParams);
        this.mActivity.onSupportContentChanged();
    }
    
    public ActionBar createSupportActionBar() {
        this.ensureSubDecor();
        return new ActionBarImplBase(this.mActivity, this.mActivity);
    }
    
    final void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            if (this.mHasActionBar) {
                if (this.mOverlayActionBar) {
                    this.mActivity.superSetContentView(R.layout.abc_action_bar_decor_overlay);
                }
                else {
                    this.mActivity.superSetContentView(R.layout.abc_action_bar_decor);
                }
                (this.mActionBarView = (ActionBarView)this.mActivity.findViewById(R.id.action_bar)).setWindowCallback((Window$Callback)this.mActivity);
                if (this.mFeatureProgress) {
                    this.mActionBarView.initProgress();
                }
                if (this.mFeatureIndeterminateProgress) {
                    this.mActionBarView.initIndeterminateProgress();
                }
                final boolean equals = "splitActionBarWhenNarrow".equals(this.getUiOptionsFromMetadata());
                boolean b;
                if (equals) {
                    b = this.mActivity.getResources().getBoolean(R.bool.abc_split_action_bar_is_narrow);
                }
                else {
                    final TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
                    b = obtainStyledAttributes.getBoolean(2, false);
                    obtainStyledAttributes.recycle();
                }
                final ActionBarContainer actionBarContainer = (ActionBarContainer)this.mActivity.findViewById(R.id.split_action_bar);
                if (actionBarContainer != null) {
                    this.mActionBarView.setSplitView(actionBarContainer);
                    this.mActionBarView.setSplitActionBar(b);
                    this.mActionBarView.setSplitWhenNarrow(equals);
                    final ActionBarContextView actionBarContextView = (ActionBarContextView)this.mActivity.findViewById(R.id.action_context_bar);
                    actionBarContextView.setSplitView(actionBarContainer);
                    actionBarContextView.setSplitActionBar(b);
                    actionBarContextView.setSplitWhenNarrow(equals);
                }
            }
            else {
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
            this.mActivity.getWindow().getDecorView().post((Runnable)new Runnable() {
                @Override
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
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mActivity.closeOptionsMenu();
        this.mActionBarView.dismissPopupMenus();
        this.mClosingActionMenu = false;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            ((ActionBarImplBase)this.getSupportActionBar()).onConfigurationChanged(configuration);
        }
    }
    
    public void onContentChanged() {
    }
    
    public boolean onCreatePanelMenu(final int n, final Menu menu) {
        return n != 0 && this.mActivity.superOnCreatePanelMenu(n, menu);
    }
    
    public View onCreatePanelView(final int n) {
        View view = null;
        if (n == 0) {
            view = view;
            if (this.preparePanel()) {
                view = (View)this.getListMenuView((Context)this.mActivity, this);
            }
        }
        return view;
    }
    
    public boolean onMenuItemSelected(final int n, final MenuItem menuItem) {
        MenuItem menuItemWrapper = menuItem;
        if (n == 0) {
            menuItemWrapper = MenuWrapperFactory.createMenuItemWrapper(menuItem);
        }
        return this.mActivity.superOnMenuItemSelected(n, menuItemWrapper);
    }
    
    @Override
    public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        return this.mActivity.onMenuItemSelected(0, menuItem);
    }
    
    @Override
    public void onMenuModeChange(final MenuBuilder menuBuilder) {
        this.reopenMenu(menuBuilder, true);
    }
    
    @Override
    public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
        return false;
    }
    
    public void onPostResume() {
        final ActionBarImplBase actionBarImplBase = (ActionBarImplBase)this.getSupportActionBar();
        if (actionBarImplBase != null) {
            actionBarImplBase.setShowHideAnimationEnabled(true);
        }
    }
    
    public boolean onPreparePanel(final int n, final View view, final Menu menu) {
        return n != 0 && this.mActivity.superOnPreparePanel(n, view, menu);
    }
    
    public void onStop() {
        final ActionBarImplBase actionBarImplBase = (ActionBarImplBase)this.getSupportActionBar();
        if (actionBarImplBase != null) {
            actionBarImplBase.setShowHideAnimationEnabled(false);
        }
    }
    
    public void onTitleChanged(final CharSequence charSequence) {
        if (this.mActionBarView != null) {
            this.mActionBarView.setWindowTitle(charSequence);
            return;
        }
        this.mTitleToSet = charSequence;
    }
    
    public void setContentView(final int n) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
        viewGroup.removeAllViews();
        this.mActivity.getLayoutInflater().inflate(n, viewGroup);
        this.mActivity.onSupportContentChanged();
    }
    
    public void setContentView(final View view) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mActivity.onSupportContentChanged();
    }
    
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, viewGroup$LayoutParams);
        this.mActivity.onSupportContentChanged();
    }
    
    @Override
    void setSupportProgress(final int n) {
        this.updateProgressBars(n + 0);
    }
    
    @Override
    void setSupportProgressBarIndeterminate(final boolean b) {
        int n;
        if (b) {
            n = -3;
        }
        else {
            n = -4;
        }
        this.updateProgressBars(n);
    }
    
    @Override
    void setSupportProgressBarIndeterminateVisibility(final boolean b) {
        int n;
        if (b) {
            n = -1;
        }
        else {
            n = -2;
        }
        this.updateProgressBars(n);
    }
    
    @Override
    void setSupportProgressBarVisibility(final boolean b) {
        int n;
        if (b) {
            n = -1;
        }
        else {
            n = -2;
        }
        this.updateProgressBars(n);
    }
    
    public ActionMode startSupportActionMode(final ActionMode.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        final ActionModeCallbackWrapper actionModeCallbackWrapper = new ActionModeCallbackWrapper(callback);
        final ActionBarImplBase actionBarImplBase = (ActionBarImplBase)this.getSupportActionBar();
        if (actionBarImplBase != null) {
            this.mActionMode = actionBarImplBase.startActionMode(actionModeCallbackWrapper);
        }
        if (this.mActionMode != null) {
            this.mActivity.onSupportActionModeStarted(this.mActionMode);
        }
        return this.mActionMode;
    }
    
    public void supportInvalidateOptionsMenu() {
        if (this.mMenu != null) {
            final Bundle mPanelFrozenActionViewState = new Bundle();
            this.mMenu.saveActionViewStates(mPanelFrozenActionViewState);
            if (mPanelFrozenActionViewState.size() > 0) {
                this.mPanelFrozenActionViewState = mPanelFrozenActionViewState;
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
    
    public boolean supportRequestWindowFeature(final int n) {
        switch (n) {
            default: {
                return this.mActivity.requestWindowFeature(n);
            }
            case 8: {
                return this.mHasActionBar = true;
            }
            case 9: {
                return this.mOverlayActionBar = true;
            }
            case 2: {
                return this.mFeatureProgress = true;
            }
            case 5: {
                return this.mFeatureIndeterminateProgress = true;
            }
        }
    }
    
    private class ActionModeCallbackWrapper implements ActionMode.Callback
    {
        private ActionMode.Callback mWrapped;
        
        public ActionModeCallbackWrapper(final ActionMode.Callback mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        @Override
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }
        
        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }
        
        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            ActionBarActivityDelegateBase.this.mActivity.onSupportActionModeFinished(actionMode);
            ActionBarActivityDelegateBase.this.mActionMode = null;
        }
        
        @Override
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }
}
