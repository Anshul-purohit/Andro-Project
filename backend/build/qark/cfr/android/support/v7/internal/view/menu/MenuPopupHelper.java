/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnKeyListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.BaseAdapter
 *  android.widget.FrameLayout
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow
 *  android.widget.PopupWindow$OnDismissListener
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.util.ArrayList
 */
package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.internal.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.ArrayList;

public class MenuPopupHelper
implements AdapterView.OnItemClickListener,
View.OnKeyListener,
ViewTreeObserver.OnGlobalLayoutListener,
PopupWindow.OnDismissListener,
MenuPresenter {
    static final int ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;
    private static final String TAG = "MenuPopupHelper";
    private MenuAdapter mAdapter;
    private View mAnchorView;
    private Context mContext;
    boolean mForceShowIcon;
    private LayoutInflater mInflater;
    private ViewGroup mMeasureParent;
    private MenuBuilder mMenu;
    private boolean mOverflowOnly;
    private ListPopupWindow mPopup;
    private int mPopupMaxWidth;
    private MenuPresenter.Callback mPresenterCallback;
    private ViewTreeObserver mTreeObserver;

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder) {
        this(context, menuBuilder, null, false);
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view) {
        this(context, menuBuilder, view, false);
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean bl) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from((Context)context);
        this.mMenu = menuBuilder;
        this.mOverflowOnly = bl;
        context = context.getResources();
        this.mPopupMaxWidth = Math.max((int)(context.getDisplayMetrics().widthPixels / 2), (int)context.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        this.mAnchorView = view;
        menuBuilder.addMenuPresenter(this);
    }

    private int measureContentWidth(ListAdapter listAdapter) {
        int n = 0;
        View view = null;
        int n2 = 0;
        int n3 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        int n4 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        int n5 = listAdapter.getCount();
        for (int i = 0; i < n5; ++i) {
            int n6 = listAdapter.getItemViewType(i);
            int n7 = n2;
            if (n6 != n2) {
                n7 = n6;
                view = null;
            }
            if (this.mMeasureParent == null) {
                this.mMeasureParent = new FrameLayout(this.mContext);
            }
            view = listAdapter.getView(i, view, this.mMeasureParent);
            view.measure(n3, n4);
            n = Math.max((int)n, (int)view.getMeasuredWidth());
            n2 = n7;
        }
        return n;
    }

    @Override
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
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
        throw new UnsupportedOperationException("MenuPopupHelpers manage their own views");
    }

    @Override
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
    }

    public boolean isShowing() {
        if (this.mPopup != null && this.mPopup.isShowing()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        block3 : {
            block2 : {
                if (menuBuilder != this.mMenu) break block2;
                this.dismiss();
                if (this.mPresenterCallback != null) break block3;
            }
            return;
        }
        this.mPresenterCallback.onCloseMenu(menuBuilder, bl);
    }

    public void onDismiss() {
        this.mPopup = null;
        this.mMenu.close();
        if (this.mTreeObserver != null) {
            if (!this.mTreeObserver.isAlive()) {
                this.mTreeObserver = this.mAnchorView.getViewTreeObserver();
            }
            this.mTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
            this.mTreeObserver = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onGlobalLayout() {
        if (!this.isShowing()) return;
        {
            View view = this.mAnchorView;
            if (view == null || !view.isShown()) {
                this.dismiss();
                return;
            } else {
                if (!this.isShowing()) return;
                {
                    this.mPopup.show();
                    return;
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> object, View view, int n, long l) {
        object = this.mAdapter;
        ((MenuAdapter)((Object)object)).mAdapterMenu.performItemAction(object.getItem(n), 0);
    }

    public boolean onKey(View view, int n, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && n == 82) {
            this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean bl;
        boolean bl2 = bl = false;
        if (!subMenuBuilder.hasVisibleItems()) return bl2;
        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.mContext, subMenuBuilder, this.mAnchorView, false);
        menuPopupHelper.setCallback(this.mPresenterCallback);
        boolean bl3 = false;
        int n = subMenuBuilder.size();
        int n2 = 0;
        do {
            block4 : {
                block3 : {
                    bl2 = bl3;
                    if (n2 >= n) break block3;
                    MenuItem menuItem = subMenuBuilder.getItem(n2);
                    if (!menuItem.isVisible() || menuItem.getIcon() == null) break block4;
                    bl2 = true;
                }
                menuPopupHelper.setForceShowIcon(bl2);
                bl2 = bl;
                if (!menuPopupHelper.tryShow()) return bl2;
                if (this.mPresenterCallback == null) return true;
                this.mPresenterCallback.onOpenSubMenu(subMenuBuilder);
                return true;
            }
            ++n2;
        } while (true);
    }

    public void setAnchorView(View view) {
        this.mAnchorView = view;
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    public void setForceShowIcon(boolean bl) {
        this.mForceShowIcon = bl;
    }

    public void show() {
        if (!this.tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public boolean tryShow() {
        boolean bl = false;
        this.mPopup = new ListPopupWindow(this.mContext, null, R.attr.popupMenuStyle);
        this.mPopup.setOnDismissListener(this);
        this.mPopup.setOnItemClickListener(this);
        this.mAdapter = new MenuAdapter(this.mMenu);
        this.mPopup.setAdapter((ListAdapter)this.mAdapter);
        this.mPopup.setModal(true);
        View view = this.mAnchorView;
        if (view != null) {
            if (this.mTreeObserver == null) {
                bl = true;
            }
            this.mTreeObserver = view.getViewTreeObserver();
            if (bl) {
                this.mTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
            }
            this.mPopup.setAnchorView(view);
            this.mPopup.setContentWidth(Math.min((int)this.measureContentWidth((ListAdapter)this.mAdapter), (int)this.mPopupMaxWidth));
            this.mPopup.setInputMethodMode(2);
            this.mPopup.show();
            this.mPopup.getListView().setOnKeyListener((View.OnKeyListener)this);
            return true;
        }
        return false;
    }

    @Override
    public void updateMenuView(boolean bl) {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private class MenuAdapter
    extends BaseAdapter {
        private MenuBuilder mAdapterMenu;
        private int mExpandedIndex;

        public MenuAdapter(MenuBuilder menuBuilder) {
            this.mExpandedIndex = -1;
            this.mAdapterMenu = menuBuilder;
            this.findExpandedIndex();
        }

        void findExpandedIndex() {
            MenuItemImpl menuItemImpl = MenuPopupHelper.this.mMenu.getExpandedItem();
            if (menuItemImpl != null) {
                ArrayList<MenuItemImpl> arrayList = MenuPopupHelper.this.mMenu.getNonActionItems();
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    if ((MenuItemImpl)arrayList.get(i) != menuItemImpl) continue;
                    this.mExpandedIndex = i;
                    return;
                }
            }
            this.mExpandedIndex = -1;
        }

        /*
         * Enabled aggressive block sorting
         */
        public int getCount() {
            ArrayList<MenuItemImpl> arrayList = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            if (this.mExpandedIndex < 0) {
                return arrayList.size();
            }
            return arrayList.size() - 1;
        }

        /*
         * Enabled aggressive block sorting
         */
        public MenuItemImpl getItem(int n) {
            ArrayList<MenuItemImpl> arrayList = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            int n2 = n;
            if (this.mExpandedIndex >= 0) {
                n2 = n;
                if (n >= this.mExpandedIndex) {
                    n2 = n + 1;
                }
            }
            return (MenuItemImpl)arrayList.get(n2);
        }

        public long getItemId(int n) {
            return n;
        }

        public View getView(int n, View object, ViewGroup viewGroup) {
            View view = object;
            if (object == null) {
                view = MenuPopupHelper.this.mInflater.inflate(MenuPopupHelper.ITEM_LAYOUT, viewGroup, false);
            }
            object = (MenuView.ItemView)view;
            if (MenuPopupHelper.this.mForceShowIcon) {
                ((ListMenuItemView)view).setForceShowIcon(true);
            }
            object.initialize(this.getItem(n), 0);
            return view;
        }

        public void notifyDataSetChanged() {
            this.findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }

}

