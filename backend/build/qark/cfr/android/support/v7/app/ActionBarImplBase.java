/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.util.TypedValue
 *  android.view.ContextThemeWrapper
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  android.widget.SpinnerAdapter
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.ref.WeakReference
 *  java.util.ArrayList
 */
package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.internal.widget.ScrollingTabContainerView;
import android.support.v7.view.ActionMode;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class ActionBarImplBase
extends ActionBar {
    private static final int CONTEXT_DISPLAY_NORMAL = 0;
    private static final int CONTEXT_DISPLAY_SPLIT = 1;
    private static final int INVALID_POSITION = -1;
    ActionModeImpl mActionMode;
    private ActionBarView mActionView;
    private ActionBarActivity mActivity;
    private ActionBar.Callback mCallback;
    private ActionBarContainer mContainerView;
    private View mContentView;
    private Context mContext;
    private int mContextDisplayMode;
    private ActionBarContextView mContextView;
    private int mCurWindowVisibility = 0;
    ActionMode mDeferredDestroyActionMode;
    ActionMode.Callback mDeferredModeDestroyCallback;
    private Dialog mDialog;
    private boolean mDisplayHomeAsUpSet;
    final Handler mHandler = new Handler();
    private boolean mHasEmbeddedTabs;
    private boolean mHiddenByApp;
    private boolean mHiddenBySystem;
    private boolean mLastMenuVisibility;
    private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList();
    private boolean mNowShowing = true;
    private ActionBarOverlayLayout mOverlayLayout;
    private int mSavedTabPosition = -1;
    private TabImpl mSelectedTab;
    private boolean mShowHideAnimationEnabled;
    private boolean mShowingForMode;
    private ActionBarContainer mSplitView;
    private ScrollingTabContainerView mTabScrollView;
    Runnable mTabSelector;
    private ArrayList<TabImpl> mTabs = new ArrayList();
    private Context mThemedContext;
    private ViewGroup mTopVisibilityView;

    public ActionBarImplBase(ActionBarActivity actionBarActivity, ActionBar.Callback callback) {
        this.mActivity = actionBarActivity;
        this.mContext = actionBarActivity;
        this.mCallback = callback;
        this.init(this.mActivity);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean checkShowingFlags(boolean bl, boolean bl2, boolean bl3) {
        if (bl3 || !bl && !bl2) {
            return true;
        }
        return false;
    }

    private void cleanupTabs() {
        if (this.mSelectedTab != null) {
            this.selectTab(null);
        }
        this.mTabs.clear();
        if (this.mTabScrollView != null) {
            this.mTabScrollView.removeAllTabs();
        }
        this.mSavedTabPosition = -1;
    }

    private void configureTab(ActionBar.Tab tab, int n) {
        if ((tab = (TabImpl)tab).getCallback() == null) {
            throw new IllegalStateException("Action Bar Tab must have a Callback");
        }
        tab.setPosition(n);
        this.mTabs.add(n, (Object)tab);
        int n2 = this.mTabs.size();
        ++n;
        while (n < n2) {
            ((TabImpl)this.mTabs.get(n)).setPosition(n);
            ++n;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void ensureTabsExist() {
        if (this.mTabScrollView != null) {
            return;
        }
        ScrollingTabContainerView scrollingTabContainerView = new ScrollingTabContainerView(this.mContext);
        if (this.mHasEmbeddedTabs) {
            scrollingTabContainerView.setVisibility(0);
            this.mActionView.setEmbeddedTabView(scrollingTabContainerView);
        } else {
            if (this.getNavigationMode() == 2) {
                scrollingTabContainerView.setVisibility(0);
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
            this.mContainerView.setTabContainer(scrollingTabContainerView);
        }
        this.mTabScrollView = scrollingTabContainerView;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void init(ActionBarActivity object) {
        boolean bl = false;
        this.mOverlayLayout = (ActionBarOverlayLayout)object.findViewById(R.id.action_bar_overlay_layout);
        if (this.mOverlayLayout != null) {
            this.mOverlayLayout.setActionBar(this);
        }
        this.mActionView = (ActionBarView)object.findViewById(R.id.action_bar);
        this.mContextView = (ActionBarContextView)object.findViewById(R.id.action_context_bar);
        this.mContainerView = (ActionBarContainer)object.findViewById(R.id.action_bar_container);
        this.mTopVisibilityView = (ViewGroup)object.findViewById(R.id.top_action_bar);
        if (this.mTopVisibilityView == null) {
            this.mTopVisibilityView = this.mContainerView;
        }
        this.mSplitView = (ActionBarContainer)object.findViewById(R.id.split_action_bar);
        if (this.mActionView == null || this.mContextView == null || this.mContainerView == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with a compatible window decor layout");
        }
        this.mActionView.setContextView(this.mContextView);
        int n = this.mActionView.isSplitActionBar() ? 1 : 0;
        this.mContextDisplayMode = n;
        n = (this.mActionView.getDisplayOptions() & 4) != 0 ? 1 : 0;
        if (n != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        if ((object = ActionBarPolicy.get(this.mContext)).enableHomeButtonByDefault() || n != 0) {
            bl = true;
        }
        this.setHomeButtonEnabled(bl);
        this.setHasEmbeddedTabs(object.hasEmbeddedTabs());
        this.setTitle(this.mActivity.getTitle());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setHasEmbeddedTabs(boolean bl) {
        boolean bl2 = true;
        this.mHasEmbeddedTabs = bl;
        if (!this.mHasEmbeddedTabs) {
            this.mActionView.setEmbeddedTabView(null);
            this.mContainerView.setTabContainer(this.mTabScrollView);
        } else {
            this.mContainerView.setTabContainer(null);
            this.mActionView.setEmbeddedTabView(this.mTabScrollView);
        }
        boolean bl3 = this.getNavigationMode() == 2;
        if (this.mTabScrollView != null) {
            if (bl3) {
                this.mTabScrollView.setVisibility(0);
            } else {
                this.mTabScrollView.setVisibility(8);
            }
        }
        ActionBarView actionBarView = this.mActionView;
        bl = !this.mHasEmbeddedTabs && bl3 ? bl2 : false;
        actionBarView.setCollapsable(bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateVisibility(boolean bl) {
        if (ActionBarImplBase.checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (this.mNowShowing) return;
            {
                this.mNowShowing = true;
                this.doShow(bl);
                return;
            }
        } else {
            if (!this.mNowShowing) return;
            {
                this.mNowShowing = false;
                this.doHide(bl);
                return;
            }
        }
    }

    @Override
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.add((Object)onMenuVisibilityListener);
    }

    @Override
    public void addTab(ActionBar.Tab tab) {
        this.addTab(tab, this.mTabs.isEmpty());
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n) {
        this.addTab(tab, n, this.mTabs.isEmpty());
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n, boolean bl) {
        this.ensureTabsExist();
        this.mTabScrollView.addTab(tab, n, bl);
        this.configureTab(tab, n);
        if (bl) {
            this.selectTab(tab);
        }
    }

    @Override
    public void addTab(ActionBar.Tab tab, boolean bl) {
        this.ensureTabsExist();
        this.mTabScrollView.addTab(tab, bl);
        this.configureTab(tab, this.mTabs.size());
        if (bl) {
            this.selectTab(tab);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void animateToMode(boolean bl) {
        int n = 8;
        if (bl) {
            this.showForActionMode();
        } else {
            this.hideForActionMode();
        }
        Object object = this.mActionView;
        int n2 = bl ? 4 : 0;
        object.animateToVisibility(n2);
        object = this.mContextView;
        n2 = bl ? 0 : 8;
        object.animateToVisibility(n2);
        if (this.mTabScrollView != null && !this.mActionView.hasEmbeddedTabs() && this.mActionView.isCollapsed()) {
            object = this.mTabScrollView;
            n2 = bl ? n : 0;
            object.setVisibility(n2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void doHide(boolean bl) {
        Animation animation;
        boolean bl2;
        block6 : {
            block5 : {
                this.mTopVisibilityView.clearAnimation();
                if (this.mTopVisibilityView.getVisibility() == 8) break block5;
                bl2 = this.isShowHideAnimationEnabled() || bl;
                if (bl2) {
                    animation = AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.abc_slide_out_top);
                    this.mTopVisibilityView.startAnimation(animation);
                }
                this.mTopVisibilityView.setVisibility(8);
                if (this.mSplitView != null && this.mSplitView.getVisibility() != 8) break block6;
            }
            return;
        }
        if (bl2) {
            animation = AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.abc_slide_out_bottom);
            this.mSplitView.startAnimation(animation);
        }
        this.mSplitView.setVisibility(8);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void doShow(boolean bl) {
        Animation animation;
        boolean bl2;
        block6 : {
            block5 : {
                this.mTopVisibilityView.clearAnimation();
                if (this.mTopVisibilityView.getVisibility() == 0) break block5;
                bl2 = this.isShowHideAnimationEnabled() || bl;
                if (bl2) {
                    animation = AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.abc_slide_in_top);
                    this.mTopVisibilityView.startAnimation(animation);
                }
                this.mTopVisibilityView.setVisibility(0);
                if (this.mSplitView != null && this.mSplitView.getVisibility() != 0) break block6;
            }
            return;
        }
        if (bl2) {
            animation = AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.abc_slide_in_bottom);
            this.mSplitView.startAnimation(animation);
        }
        this.mSplitView.setVisibility(0);
    }

    @Override
    public View getCustomView() {
        return this.mActionView.getCustomNavigationView();
    }

    @Override
    public int getDisplayOptions() {
        return this.mActionView.getDisplayOptions();
    }

    @Override
    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int getNavigationItemCount() {
        switch (this.mActionView.getNavigationMode()) {
            default: {
                return 0;
            }
            case 2: {
                return this.mTabs.size();
            }
            case 1: {
                SpinnerAdapter spinnerAdapter = this.mActionView.getDropdownAdapter();
                if (spinnerAdapter == null) return 0;
                return spinnerAdapter.getCount();
            }
        }
    }

    @Override
    public int getNavigationMode() {
        return this.mActionView.getNavigationMode();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int getSelectedNavigationIndex() {
        switch (this.mActionView.getNavigationMode()) {
            case 2: {
                if (this.mSelectedTab != null) {
                    return this.mSelectedTab.getPosition();
                }
            }
            default: {
                return -1;
            }
            case 1: 
        }
        return this.mActionView.getDropdownSelectedPosition();
    }

    @Override
    public ActionBar.Tab getSelectedTab() {
        return this.mSelectedTab;
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mActionView.getSubtitle();
    }

    @Override
    public ActionBar.Tab getTabAt(int n) {
        return (ActionBar.Tab)this.mTabs.get(n);
    }

    @Override
    public int getTabCount() {
        return this.mTabs.size();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public Context getThemedContext() {
        block4 : {
            block3 : {
                if (this.mThemedContext != null) break block3;
                TypedValue typedValue = new TypedValue();
                this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                int n = typedValue.resourceId;
                if (n == 0) break block4;
                this.mThemedContext = new ContextThemeWrapper(this.mContext, n);
            }
            do {
                return this.mThemedContext;
                break;
            } while (true);
        }
        this.mThemedContext = this.mContext;
        return this.mThemedContext;
    }

    @Override
    public CharSequence getTitle() {
        return this.mActionView.getTitle();
    }

    public boolean hasNonEmbeddedTabs() {
        if (!this.mHasEmbeddedTabs && this.getNavigationMode() == 2) {
            return true;
        }
        return false;
    }

    @Override
    public void hide() {
        if (!this.mHiddenByApp) {
            this.mHiddenByApp = true;
            this.updateVisibility(false);
        }
    }

    void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            this.updateVisibility(false);
        }
    }

    boolean isShowHideAnimationEnabled() {
        return this.mShowHideAnimationEnabled;
    }

    @Override
    public boolean isShowing() {
        return this.mNowShowing;
    }

    @Override
    public ActionBar.Tab newTab() {
        return new TabImpl();
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    @Override
    public void removeAllTabs() {
        this.cleanupTabs();
    }

    @Override
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.remove((Object)onMenuVisibilityListener);
    }

    @Override
    public void removeTab(ActionBar.Tab tab) {
        this.removeTabAt(tab.getPosition());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void removeTabAt(int n) {
        TabImpl tabImpl;
        block6 : {
            block5 : {
                if (this.mTabScrollView == null) break block5;
                int n2 = this.mSelectedTab != null ? this.mSelectedTab.getPosition() : this.mSavedTabPosition;
                this.mTabScrollView.removeTabAt(n);
                tabImpl = (TabImpl)this.mTabs.remove(n);
                if (tabImpl != null) {
                    tabImpl.setPosition(-1);
                }
                int n3 = this.mTabs.size();
                for (int i = n; i < n3; ++i) {
                    ((TabImpl)this.mTabs.get(i)).setPosition(i);
                }
                if (n2 == n) break block6;
            }
            return;
        }
        tabImpl = this.mTabs.isEmpty() ? null : (TabImpl)this.mTabs.get(Math.max((int)0, (int)(n - 1)));
        this.selectTab(tabImpl);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void selectTab(ActionBar.Tab tab) {
        int n = -1;
        if (this.getNavigationMode() != 2) {
            if (tab != null) {
                n = tab.getPosition();
            }
            this.mSavedTabPosition = n;
            return;
        } else {
            FragmentTransaction fragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
            if (this.mSelectedTab == tab) {
                if (this.mSelectedTab != null) {
                    this.mSelectedTab.getCallback().onTabReselected(this.mSelectedTab, fragmentTransaction);
                    this.mTabScrollView.animateToTab(tab.getPosition());
                }
            } else {
                ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
                if (tab != null) {
                    n = tab.getPosition();
                }
                scrollingTabContainerView.setTabSelected(n);
                if (this.mSelectedTab != null) {
                    this.mSelectedTab.getCallback().onTabUnselected(this.mSelectedTab, fragmentTransaction);
                }
                this.mSelectedTab = (TabImpl)tab;
                if (this.mSelectedTab != null) {
                    this.mSelectedTab.getCallback().onTabSelected(this.mSelectedTab, fragmentTransaction);
                }
            }
            if (fragmentTransaction.isEmpty()) return;
            {
                fragmentTransaction.commit();
                return;
            }
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable2) {
        this.mContainerView.setPrimaryBackground(drawable2);
    }

    @Override
    public void setCustomView(int n) {
        this.setCustomView(LayoutInflater.from((Context)this.getThemedContext()).inflate(n, (ViewGroup)this.mActionView, false));
    }

    @Override
    public void setCustomView(View view) {
        this.mActionView.setCustomNavigationView(view);
    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.mActionView.setCustomNavigationView(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayHomeAsUpEnabled(boolean bl) {
        int n = bl ? 4 : 0;
        this.setDisplayOptions(n, 4);
    }

    @Override
    public void setDisplayOptions(int n) {
        if ((n & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions(n);
    }

    @Override
    public void setDisplayOptions(int n, int n2) {
        int n3 = this.mActionView.getDisplayOptions();
        if ((n2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions(n & n2 | ~ n2 & n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowCustomEnabled(boolean bl) {
        int n = bl ? 16 : 0;
        this.setDisplayOptions(n, 16);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowHomeEnabled(boolean bl) {
        int n = bl ? 2 : 0;
        this.setDisplayOptions(n, 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowTitleEnabled(boolean bl) {
        int n = bl ? 8 : 0;
        this.setDisplayOptions(n, 8);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayUseLogoEnabled(boolean bl) {
        int n = bl ? 1 : 0;
        this.setDisplayOptions(n, 1);
    }

    @Override
    public void setHomeAsUpIndicator(int n) {
        this.mActionView.setHomeAsUpIndicator(n);
    }

    @Override
    public void setHomeAsUpIndicator(Drawable drawable2) {
        this.mActionView.setHomeAsUpIndicator(drawable2);
    }

    @Override
    public void setHomeButtonEnabled(boolean bl) {
        this.mActionView.setHomeButtonEnabled(bl);
    }

    @Override
    public void setIcon(int n) {
        this.mActionView.setIcon(n);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.mActionView.setIcon(drawable2);
    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, ActionBar.OnNavigationListener onNavigationListener) {
        this.mActionView.setDropdownAdapter(spinnerAdapter);
        this.mActionView.setCallback(onNavigationListener);
    }

    @Override
    public void setLogo(int n) {
        this.mActionView.setLogo(n);
    }

    @Override
    public void setLogo(Drawable drawable2) {
        this.mActionView.setLogo(drawable2);
    }

    /*
     * Exception decompiling
     */
    @Override
    public void setNavigationMode(int var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:486)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void setSelectedNavigationItem(int n) {
        switch (this.mActionView.getNavigationMode()) {
            default: {
                throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
            }
            case 2: {
                this.selectTab((ActionBar.Tab)this.mTabs.get(n));
                return;
            }
            case 1: 
        }
        this.mActionView.setDropdownSelectedPosition(n);
    }

    public void setShowHideAnimationEnabled(boolean bl) {
        this.mShowHideAnimationEnabled = bl;
        if (!bl) {
            this.mTopVisibilityView.clearAnimation();
            if (this.mSplitView != null) {
                this.mSplitView.clearAnimation();
            }
        }
    }

    @Override
    public void setSplitBackgroundDrawable(Drawable drawable2) {
        this.mContainerView.setSplitBackground(drawable2);
    }

    @Override
    public void setStackedBackgroundDrawable(Drawable drawable2) {
        this.mContainerView.setStackedBackground(drawable2);
    }

    @Override
    public void setSubtitle(int n) {
        this.setSubtitle(this.mContext.getString(n));
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mActionView.setSubtitle(charSequence);
    }

    @Override
    public void setTitle(int n) {
        this.setTitle(this.mContext.getString(n));
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mActionView.setTitle(charSequence);
    }

    @Override
    public void show() {
        if (this.mHiddenByApp) {
            this.mHiddenByApp = false;
            this.updateVisibility(false);
        }
    }

    void showForActionMode() {
        if (!this.mShowingForMode) {
            this.mShowingForMode = true;
            this.updateVisibility(false);
        }
    }

    public ActionMode startActionMode(ActionMode.Callback object) {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        this.mContextView.killMode();
        object = new ActionModeImpl((ActionMode.Callback)object);
        if (object.dispatchOnCreate()) {
            object.invalidate();
            this.mContextView.initForMode((ActionMode)object);
            this.animateToMode(true);
            if (this.mSplitView != null && this.mContextDisplayMode == 1 && this.mSplitView.getVisibility() != 0) {
                this.mSplitView.setVisibility(0);
            }
            this.mContextView.sendAccessibilityEvent(32);
            this.mActionMode = object;
            return object;
        }
        return null;
    }

    class ActionModeImpl
    extends ActionMode
    implements MenuBuilder.Callback {
        private ActionMode.Callback mCallback;
        private WeakReference<View> mCustomView;
        private MenuBuilder mMenu;

        public ActionModeImpl(ActionMode.Callback callback) {
            this.mCallback = callback;
            this.mMenu = new MenuBuilder(ActionBarImplBase.this.getThemedContext()).setDefaultShowAsAction(1);
            this.mMenu.setCallback(this);
        }

        public boolean dispatchOnCreate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                boolean bl = this.mCallback.onCreateActionMode(this, this.mMenu);
                return bl;
            }
            finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void finish() {
            if (ActionBarImplBase.this.mActionMode != this) {
                return;
            }
            if (!ActionBarImplBase.checkShowingFlags(ActionBarImplBase.this.mHiddenByApp, ActionBarImplBase.this.mHiddenBySystem, false)) {
                ActionBarImplBase.this.mDeferredDestroyActionMode = this;
                ActionBarImplBase.this.mDeferredModeDestroyCallback = this.mCallback;
            } else {
                this.mCallback.onDestroyActionMode(this);
            }
            this.mCallback = null;
            ActionBarImplBase.this.animateToMode(false);
            ActionBarImplBase.this.mContextView.closeMode();
            ActionBarImplBase.this.mActionView.sendAccessibilityEvent(32);
            ActionBarImplBase.this.mActionMode = null;
        }

        @Override
        public View getCustomView() {
            if (this.mCustomView != null) {
                return (View)this.mCustomView.get();
            }
            return null;
        }

        @Override
        public Menu getMenu() {
            return this.mMenu;
        }

        @Override
        public MenuInflater getMenuInflater() {
            return new SupportMenuInflater(ActionBarImplBase.this.getThemedContext());
        }

        @Override
        public CharSequence getSubtitle() {
            return ActionBarImplBase.this.mContextView.getSubtitle();
        }

        @Override
        public CharSequence getTitle() {
            return ActionBarImplBase.this.mContextView.getTitle();
        }

        @Override
        public void invalidate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                this.mCallback.onPrepareActionMode(this, this.mMenu);
                return;
            }
            finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        @Override
        public boolean isTitleOptional() {
            return ActionBarImplBase.this.mContextView.isTitleOptional();
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        public void onCloseSubMenu(SubMenuBuilder subMenuBuilder) {
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (this.mCallback != null) {
                return this.mCallback.onActionItemClicked(this, menuItem);
            }
            return false;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.mCallback == null) {
                return;
            }
            this.invalidate();
            ActionBarImplBase.this.mContextView.showOverflowMenu();
        }

        public void onMenuModeChange(Menu menu2) {
            if (this.mCallback == null) {
                return;
            }
            this.invalidate();
            ActionBarImplBase.this.mContextView.showOverflowMenu();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            boolean bl = true;
            if (this.mCallback == null) {
                return false;
            }
            if (subMenuBuilder.hasVisibleItems()) return bl;
            return true;
        }

        @Override
        public void setCustomView(View view) {
            ActionBarImplBase.this.mContextView.setCustomView(view);
            this.mCustomView = new WeakReference((Object)view);
        }

        @Override
        public void setSubtitle(int n) {
            this.setSubtitle(ActionBarImplBase.this.mContext.getResources().getString(n));
        }

        @Override
        public void setSubtitle(CharSequence charSequence) {
            ActionBarImplBase.this.mContextView.setSubtitle(charSequence);
        }

        @Override
        public void setTitle(int n) {
            this.setTitle(ActionBarImplBase.this.mContext.getResources().getString(n));
        }

        @Override
        public void setTitle(CharSequence charSequence) {
            ActionBarImplBase.this.mContextView.setTitle(charSequence);
        }

        @Override
        public void setTitleOptionalHint(boolean bl) {
            super.setTitleOptionalHint(bl);
            ActionBarImplBase.this.mContextView.setTitleOptional(bl);
        }
    }

    public class TabImpl
    extends ActionBar.Tab {
        private ActionBar.TabListener mCallback;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        private int mPosition;
        private Object mTag;
        private CharSequence mText;

        public TabImpl() {
            this.mPosition = -1;
        }

        public ActionBar.TabListener getCallback() {
            return this.mCallback;
        }

        @Override
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        @Override
        public View getCustomView() {
            return this.mCustomView;
        }

        @Override
        public Drawable getIcon() {
            return this.mIcon;
        }

        @Override
        public int getPosition() {
            return this.mPosition;
        }

        @Override
        public Object getTag() {
            return this.mTag;
        }

        @Override
        public CharSequence getText() {
            return this.mText;
        }

        @Override
        public void select() {
            ActionBarImplBase.this.selectTab(this);
        }

        @Override
        public ActionBar.Tab setContentDescription(int n) {
            return this.setContentDescription(ActionBarImplBase.this.mContext.getResources().getText(n));
        }

        @Override
        public ActionBar.Tab setContentDescription(CharSequence charSequence) {
            this.mContentDesc = charSequence;
            if (this.mPosition >= 0) {
                ActionBarImplBase.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override
        public ActionBar.Tab setCustomView(int n) {
            return this.setCustomView(LayoutInflater.from((Context)ActionBarImplBase.this.getThemedContext()).inflate(n, null));
        }

        @Override
        public ActionBar.Tab setCustomView(View view) {
            this.mCustomView = view;
            if (this.mPosition >= 0) {
                ActionBarImplBase.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override
        public ActionBar.Tab setIcon(int n) {
            return this.setIcon(ActionBarImplBase.this.mContext.getResources().getDrawable(n));
        }

        @Override
        public ActionBar.Tab setIcon(Drawable drawable2) {
            this.mIcon = drawable2;
            if (this.mPosition >= 0) {
                ActionBarImplBase.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        public void setPosition(int n) {
            this.mPosition = n;
        }

        @Override
        public ActionBar.Tab setTabListener(ActionBar.TabListener tabListener) {
            this.mCallback = tabListener;
            return this;
        }

        @Override
        public ActionBar.Tab setTag(Object object) {
            this.mTag = object;
            return this;
        }

        @Override
        public ActionBar.Tab setText(int n) {
            return this.setText(ActionBarImplBase.this.mContext.getResources().getText(n));
        }

        @Override
        public ActionBar.Tab setText(CharSequence charSequence) {
            this.mText = charSequence;
            if (this.mPosition >= 0) {
                ActionBarImplBase.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
    }

}

