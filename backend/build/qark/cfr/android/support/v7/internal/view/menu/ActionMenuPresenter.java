/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.SparseBooleanArray
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.ImageButton
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.ArrayList
 */
package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.BaseMenuPresenter;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuDialogHelper;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import java.util.ArrayList;

public class ActionMenuPresenter
extends BaseMenuPresenter
implements ActionProvider.SubUiVisibilityListener {
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    private ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    private View mOverflowButton;
    private OverflowPopup mOverflowPopup;
    final PopupPresenterCallback mPopupPresenterCallback;
    private OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    public ActionMenuPresenter(Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
        this.mPopupPresenterCallback = new PopupPresenterCallback();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private View findViewForItem(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        int n = viewGroup.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            View view = viewGroup.getChildAt(n2);
            if (view instanceof MenuView.ItemView) {
                View view2 = view;
                if (((MenuView.ItemView)view).getItemData() == menuItem) return view2;
            }
            ++n2;
        }
        return null;
    }

    @Override
    public void bindItemView(MenuItemImpl object, MenuView.ItemView itemView) {
        itemView.initialize((MenuItemImpl)object, 0);
        object = (ActionMenuView)this.mMenuView;
        ((ActionMenuItemView)itemView).setItemInvoker((MenuBuilder.ItemInvoker)object);
    }

    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }

    @Override
    public boolean filterLeftoverView(ViewGroup viewGroup, int n) {
        if (viewGroup.getChildAt(n) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(viewGroup, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean flagActionItems() {
        int n;
        int n2;
        int n3;
        ArrayList<MenuItemImpl> arrayList;
        int n4;
        int n5;
        int n6;
        int n7;
        MenuItemImpl menuItemImpl;
        int n8;
        ViewGroup viewGroup;
        int n9;
        int n10;
        block37 : {
            block38 : {
                arrayList = this.mMenu.getVisibleItems();
                n5 = arrayList.size();
                n2 = this.mMaxItems;
                n8 = this.mActionItemWidthLimit;
                n4 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                viewGroup = (ViewGroup)this.mMenuView;
                n9 = 0;
                n6 = 0;
                n7 = 0;
                n3 = 0;
                for (n10 = 0; n10 < n5; ++n10) {
                    menuItemImpl = (MenuItemImpl)arrayList.get(n10);
                    if (menuItemImpl.requiresActionButton()) {
                        ++n9;
                    } else if (menuItemImpl.requestsActionButton()) {
                        ++n6;
                    } else {
                        n3 = 1;
                    }
                    n = n2;
                    if (this.mExpandedActionViewsExclusive) {
                        n = n2;
                        if (menuItemImpl.isActionViewExpanded()) {
                            n = 0;
                        }
                    }
                    n2 = n;
                }
                n10 = n2;
                if (!this.mReserveOverflow) break block37;
                if (n3 != 0) break block38;
                n10 = n2;
                if (n9 + n6 <= n2) break block37;
            }
            n10 = n2 - 1;
        }
        n10 -= n9;
        menuItemImpl = this.mActionButtonGroups;
        menuItemImpl.clear();
        int n11 = 0;
        n9 = 0;
        if (this.mStrictWidthLimit) {
            n9 = n8 / this.mMinCellSize;
            n2 = this.mMinCellSize;
            n11 = this.mMinCellSize + n8 % n2 / n9;
        }
        n2 = 0;
        n6 = n8;
        n8 = n2;
        n2 = n7;
        while (n8 < n5) {
            int n12;
            Object object;
            MenuItemImpl menuItemImpl2 = (MenuItemImpl)arrayList.get(n8);
            if (menuItemImpl2.requiresActionButton()) {
                object = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                if (this.mScrapActionButtonView == null) {
                    this.mScrapActionButtonView = object;
                }
                if (this.mStrictWidthLimit) {
                    n9 -= ActionMenuView.measureChildForCells((View)object, n11, n9, n4, 0);
                } else {
                    object.measure(n4, n4);
                }
                n7 = object.getMeasuredWidth();
                n3 = n6 - n7;
                n = n2;
                if (n2 == 0) {
                    n = n7;
                }
                if ((n2 = menuItemImpl2.getGroupId()) != 0) {
                    menuItemImpl.put(n2, true);
                }
                menuItemImpl2.setIsActionButton(true);
                n12 = n10;
                n7 = n9;
            } else {
                n7 = n9;
                n = n2;
                n12 = n10;
                n3 = n6;
                if (menuItemImpl2.requestsActionButton()) {
                    n12 = menuItemImpl2.getGroupId();
                    boolean bl = menuItemImpl.get(n12);
                    int n13 = !(n10 <= 0 && !bl || n6 <= 0 || this.mStrictWidthLimit && n9 <= 0) ? 1 : 0;
                    n7 = n9;
                    n = n2;
                    int n14 = n13;
                    n3 = n6;
                    if (n13 != 0) {
                        object = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                        if (this.mScrapActionButtonView == null) {
                            this.mScrapActionButtonView = object;
                        }
                        if (this.mStrictWidthLimit) {
                            n = ActionMenuView.measureChildForCells((View)object, n11, n9, n4, 0);
                            n9 = n3 = n9 - n;
                            if (n == 0) {
                                n13 = 0;
                                n9 = n3;
                            }
                        } else {
                            object.measure(n4, n4);
                        }
                        n7 = object.getMeasuredWidth();
                        n3 = n6 - n7;
                        n = n2;
                        if (n2 == 0) {
                            n = n7;
                        }
                        if (this.mStrictWidthLimit) {
                            n2 = n3 >= 0 ? 1 : 0;
                            n14 = n13 & n2;
                            n7 = n9;
                        } else {
                            n2 = n3 + n > 0 ? 1 : 0;
                            n14 = n13 & n2;
                            n7 = n9;
                        }
                    }
                    if (n14 != 0 && n12 != 0) {
                        menuItemImpl.put(n12, true);
                        n2 = n10;
                    } else {
                        n2 = n10;
                        if (bl) {
                            menuItemImpl.put(n12, false);
                            n9 = 0;
                            do {
                                n2 = n10;
                                if (n9 >= n8) break;
                                object = (MenuItemImpl)arrayList.get(n9);
                                n2 = n10;
                                if (object.getGroupId() == n12) {
                                    n2 = n10;
                                    if (object.isActionButton()) {
                                        n2 = n10 + 1;
                                    }
                                    object.setIsActionButton(false);
                                }
                                ++n9;
                                n10 = n2;
                            } while (true);
                        }
                    }
                    n9 = n2;
                    if (n14 != 0) {
                        n9 = n2 - 1;
                    }
                    menuItemImpl2.setIsActionButton((boolean)n14);
                    n12 = n9;
                }
            }
            ++n8;
            n9 = n7;
            n2 = n;
            n10 = n12;
            n6 = n3;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public View getItemView(MenuItemImpl object, View view, ViewGroup viewGroup) {
        View view2 = object.getActionView();
        if (view2 == null || object.hasCollapsibleActionView()) {
            view2 = view;
            if (!(view instanceof ActionMenuItemView)) {
                view2 = null;
            }
            view2 = super.getItemView((MenuItemImpl)object, view2, viewGroup);
        }
        int n = object.isActionViewExpanded() ? 8 : 0;
        view2.setVisibility(n);
        object = (ActionMenuView)viewGroup;
        view = view2.getLayoutParams();
        if (!object.checkLayoutParams((ViewGroup.LayoutParams)view)) {
            view2.setLayoutParams((ViewGroup.LayoutParams)object.generateLayoutParams((ViewGroup.LayoutParams)view));
        }
        return view2;
    }

    @Override
    public MenuView getMenuView(ViewGroup object) {
        object = super.getMenuView((ViewGroup)object);
        ((ActionMenuView)object).setPresenter(this);
        return object;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            ((View)this.mMenuView).removeCallbacks((Runnable)this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            return true;
        }
        OverflowPopup overflowPopup = this.mOverflowPopup;
        if (overflowPopup != null) {
            overflowPopup.dismiss();
            return true;
        }
        return false;
    }

    public boolean hideSubMenus() {
        if (this.mActionButtonPopup != null) {
            this.mActionButtonPopup.dismiss();
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void initForMenu(Context object, MenuBuilder menuBuilder) {
        super.initForMenu((Context)object, menuBuilder);
        menuBuilder = object.getResources();
        object = ActionBarPolicy.get((Context)object);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = object.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = object.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = object.getMaxActionButtons();
        }
        int n = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                int n2 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                this.mOverflowButton.measure(n2, n2);
            }
            n -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = n;
        this.mMinCellSize = (int)(56.0f * menuBuilder.getDisplayMetrics().density);
        this.mScrapActionButtonView = null;
    }

    public boolean isOverflowMenuShowing() {
        if (this.mOverflowPopup != null && this.mOverflowPopup.isShowing()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, bl);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = this.mContext.getResources().getInteger(R.integer.abc_max_action_buttons);
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        parcelable = (SavedState)parcelable;
        if (parcelable.openSubMenuId > 0 && (parcelable = this.mMenu.findItem(parcelable.openSubMenuId)) != null) {
            this.onSubMenuSelected((SubMenuBuilder)parcelable.getSubMenu());
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        block4 : {
            SubMenuBuilder subMenuBuilder2;
            block5 : {
                block3 : {
                    if (!subMenuBuilder.hasVisibleItems()) break block3;
                    subMenuBuilder2 = subMenuBuilder;
                    while (subMenuBuilder2.getParentMenu() != this.mMenu) {
                        subMenuBuilder2 = (SubMenuBuilder)subMenuBuilder2.getParentMenu();
                    }
                    if (this.findViewForItem(subMenuBuilder2.getItem()) != null) break block4;
                    if (this.mOverflowButton != null) break block5;
                }
                return false;
            }
            subMenuBuilder2 = this.mOverflowButton;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        this.mActionButtonPopup = new ActionButtonSubmenu(subMenuBuilder);
        this.mActionButtonPopup.show(null);
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    @Override
    public void onSubUiVisibilityChanged(boolean bl) {
        if (bl) {
            super.onSubMenuSelected(null);
            return;
        }
        this.mMenu.close(false);
    }

    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mExpandedActionViewsExclusive = bl;
    }

    public void setItemLimit(int n) {
        this.mMaxItems = n;
        this.mMaxItemsSet = true;
    }

    public void setReserveOverflow(boolean bl) {
        this.mReserveOverflow = bl;
        this.mReserveOverflowSet = true;
    }

    public void setWidthLimit(int n, boolean bl) {
        this.mWidthLimit = n;
        this.mStrictWidthLimit = bl;
        this.mWidthLimitSet = true;
    }

    @Override
    public boolean shouldIncludeItem(int n, MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }

    public boolean showOverflowMenu() {
        if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null) {
            this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true));
            ((View)this.mMenuView).post((Runnable)this.mPostedOpenRunnable);
            super.onSubMenuSelected(null);
            return true;
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void updateMenuView(boolean var1_1) {
        super.updateMenuView(var1_1);
        if (this.mMenuView == null) {
            return;
        }
        if (this.mMenu != null) {
            var4_2 = this.mMenu.getActionItems();
            var3_3 = var4_2.size();
            for (var2_4 = 0; var2_4 < var3_3; ++var2_4) {
                var5_5 = ((MenuItemImpl)var4_2.get(var2_4)).getSupportActionProvider();
                if (var5_5 == null) continue;
                var5_5.setSubUiVisibilityListener(this);
            }
        }
        var4_2 = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        var2_4 = var3_3 = 0;
        if (!this.mReserveOverflow) ** GOTO lbl-1000
        var2_4 = var3_3;
        if (var4_2 != null) {
            var2_4 = var4_2.size();
            var2_4 = var2_4 == 1 ? (!((MenuItemImpl)var4_2.get(0)).isActionViewExpanded() ? 1 : 0) : (var2_4 > 0 ? 1 : 0);
        }
        if (var2_4 != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            if ((var4_2 = (ViewGroup)this.mOverflowButton.getParent()) != this.mMenuView) {
                if (var4_2 != null) {
                    var4_2.removeView(this.mOverflowButton);
                }
                var4_2 = (ActionMenuView)this.mMenuView;
                var4_2.addView(this.mOverflowButton, (ViewGroup.LayoutParams)var4_2.generateOverflowButtonLayoutParams());
            }
        } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            ((ViewGroup)this.mMenuView).removeView(this.mOverflowButton);
        }
        ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    private class ActionButtonSubmenu
    extends MenuDialogHelper {
        public ActionButtonSubmenu(SubMenuBuilder subMenuBuilder) {
            super(subMenuBuilder);
            ActionMenuPresenter.this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
        }
    }

    private class OpenOverflowRunnable
    implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup overflowPopup) {
            this.mPopup = overflowPopup;
        }

        public void run() {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
            View view = (View)ActionMenuPresenter.this.mMenuView;
            if (view != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    private class OverflowMenuButton
    extends ImageButton
    implements ActionMenuView.ActionMenuChildView {
        public OverflowMenuButton(Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
        }

        @Override
        public boolean needsDividerAfter() {
            return false;
        }

        @Override
        public boolean needsDividerBefore() {
            return false;
        }

        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }
    }

    private class OverflowPopup
    extends MenuPopupHelper {
        public OverflowPopup(Context context, MenuBuilder menuBuilder, View view, boolean bl) {
            super(context, menuBuilder, view, bl);
            this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override
        public void onDismiss() {
            super.onDismiss();
            ActionMenuPresenter.this.mMenu.close();
            ActionMenuPresenter.this.mOverflowPopup = null;
        }
    }

    private class PopupPresenterCallback
    implements MenuPresenter.Callback {
        private PopupPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            if (menuBuilder instanceof SubMenuBuilder) {
                ((SubMenuBuilder)menuBuilder).getRootMenu().close(false);
            }
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
            return false;
        }
    }

    private static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.openSubMenuId);
        }

    }

}

