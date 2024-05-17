package android.support.v7.internal.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R$bool;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyCharacterMap.KeyData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder implements SupportMenu {
   private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
   private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
   private static final String PRESENTER_KEY = "android:menu:presenters";
   private static final String TAG = "MenuBuilder";
   private static final int[] sCategoryToOrder = new int[]{1, 4, 5, 3, 2, 0};
   private ArrayList mActionItems;
   private MenuBuilder.Callback mCallback;
   private final Context mContext;
   private ContextMenuInfo mCurrentMenuInfo;
   private int mDefaultShowAsAction = 0;
   private MenuItemImpl mExpandedItem;
   Drawable mHeaderIcon;
   CharSequence mHeaderTitle;
   View mHeaderView;
   private boolean mIsActionItemsStale;
   private boolean mIsClosing = false;
   private boolean mIsVisibleItemsStale;
   private ArrayList mItems;
   private boolean mItemsChangedWhileDispatchPrevented = false;
   private ArrayList mNonActionItems;
   private boolean mOptionalIconsVisible = false;
   private CopyOnWriteArrayList mPresenters = new CopyOnWriteArrayList();
   private boolean mPreventDispatchingItemsChanged = false;
   private boolean mQwertyMode;
   private final Resources mResources;
   private boolean mShortcutsVisible;
   private ArrayList mTempShortcutItemList = new ArrayList();
   private ArrayList mVisibleItems;

   public MenuBuilder(Context var1) {
      this.mContext = var1;
      this.mResources = var1.getResources();
      this.mItems = new ArrayList();
      this.mVisibleItems = new ArrayList();
      this.mIsVisibleItemsStale = true;
      this.mActionItems = new ArrayList();
      this.mNonActionItems = new ArrayList();
      this.mIsActionItemsStale = true;
      this.setShortcutsVisibleInner(true);
   }

   private MenuItem addInternal(int var1, int var2, int var3, CharSequence var4) {
      int var5 = getOrdering(var3);
      MenuItemImpl var6 = new MenuItemImpl(this, var1, var2, var3, var5, var4, this.mDefaultShowAsAction);
      if (this.mCurrentMenuInfo != null) {
         var6.setMenuInfo(this.mCurrentMenuInfo);
      }

      this.mItems.add(findInsertIndex(this.mItems, var5), var6);
      this.onItemsChanged(true);
      return var6;
   }

   private void dispatchPresenterUpdate(boolean var1) {
      if (!this.mPresenters.isEmpty()) {
         this.stopDispatchingItemsChanged();
         Iterator var2 = this.mPresenters.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            MenuPresenter var4 = (MenuPresenter)var3.get();
            if (var4 == null) {
               this.mPresenters.remove(var3);
            } else {
               var4.updateMenuView(var1);
            }
         }

         this.startDispatchingItemsChanged();
      }
   }

   private void dispatchRestoreInstanceState(Bundle var1) {
      SparseArray var7 = var1.getSparseParcelableArray("android:menu:presenters");
      if (var7 != null && !this.mPresenters.isEmpty()) {
         Iterator var3 = this.mPresenters.iterator();

         while(var3.hasNext()) {
            WeakReference var5 = (WeakReference)var3.next();
            MenuPresenter var4 = (MenuPresenter)var5.get();
            if (var4 == null) {
               this.mPresenters.remove(var5);
            } else {
               int var2 = var4.getId();
               if (var2 > 0) {
                  Parcelable var6 = (Parcelable)var7.get(var2);
                  if (var6 != null) {
                     var4.onRestoreInstanceState(var6);
                  }
               }
            }
         }
      }

   }

   private void dispatchSaveInstanceState(Bundle var1) {
      if (!this.mPresenters.isEmpty()) {
         SparseArray var3 = new SparseArray();
         Iterator var4 = this.mPresenters.iterator();

         while(var4.hasNext()) {
            WeakReference var5 = (WeakReference)var4.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if (var6 == null) {
               this.mPresenters.remove(var5);
            } else {
               int var2 = var6.getId();
               if (var2 > 0) {
                  Parcelable var7 = var6.onSaveInstanceState();
                  if (var7 != null) {
                     var3.put(var2, var7);
                  }
               }
            }
         }

         var1.putSparseParcelableArray("android:menu:presenters", var3);
      }
   }

   private boolean dispatchSubMenuSelected(SubMenuBuilder var1) {
      boolean var3;
      if (this.mPresenters.isEmpty()) {
         var3 = false;
      } else {
         boolean var2 = false;
         Iterator var4 = this.mPresenters.iterator();

         while(true) {
            var3 = var2;
            if (!var4.hasNext()) {
               break;
            }

            WeakReference var5 = (WeakReference)var4.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if (var6 == null) {
               this.mPresenters.remove(var5);
            } else if (!var2) {
               var2 = var6.onSubMenuSelected(var1);
            }
         }
      }

      return var3;
   }

   private static int findInsertIndex(ArrayList var0, int var1) {
      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         if (((MenuItemImpl)var0.get(var2)).getOrdering() <= var1) {
            return var2 + 1;
         }
      }

      return 0;
   }

   private static int getOrdering(int var0) {
      int var1 = (-65536 & var0) >> 16;
      if (var1 >= 0 && var1 < sCategoryToOrder.length) {
         return sCategoryToOrder[var1] << 16 | '\uffff' & var0;
      } else {
         throw new IllegalArgumentException("order does not contain a valid category.");
      }
   }

   private void removeItemAtInt(int var1, boolean var2) {
      if (var1 >= 0 && var1 < this.mItems.size()) {
         this.mItems.remove(var1);
         if (var2) {
            this.onItemsChanged(true);
            return;
         }
      }

   }

   private void setHeaderInternal(int var1, CharSequence var2, int var3, Drawable var4, View var5) {
      Resources var6 = this.getResources();
      if (var5 != null) {
         this.mHeaderView = var5;
         this.mHeaderTitle = null;
         this.mHeaderIcon = null;
      } else {
         if (var1 > 0) {
            this.mHeaderTitle = var6.getText(var1);
         } else if (var2 != null) {
            this.mHeaderTitle = var2;
         }

         if (var3 > 0) {
            this.mHeaderIcon = var6.getDrawable(var3);
         } else if (var4 != null) {
            this.mHeaderIcon = var4;
         }

         this.mHeaderView = null;
      }

      this.onItemsChanged(false);
   }

   private void setShortcutsVisibleInner(boolean var1) {
      boolean var2 = true;
      if (var1 && this.mResources.getConfiguration().keyboard != 1 && this.mResources.getBoolean(R$bool.abc_config_showMenuShortcutsWhenKeyboardPresent)) {
         var1 = var2;
      } else {
         var1 = false;
      }

      this.mShortcutsVisible = var1;
   }

   public MenuItem add(int var1) {
      return this.addInternal(0, 0, 0, this.mResources.getString(var1));
   }

   public MenuItem add(int var1, int var2, int var3, int var4) {
      return this.addInternal(var1, var2, var3, this.mResources.getString(var4));
   }

   public MenuItem add(int var1, int var2, int var3, CharSequence var4) {
      return this.addInternal(var1, var2, var3, var4);
   }

   public MenuItem add(CharSequence var1) {
      return this.addInternal(0, 0, 0, var1);
   }

   public int addIntentOptions(int var1, int var2, int var3, ComponentName var4, Intent[] var5, Intent var6, int var7, MenuItem[] var8) {
      PackageManager var10 = this.mContext.getPackageManager();
      List var11 = var10.queryIntentActivityOptions(var4, var5, var6, 0);
      int var9;
      if (var11 != null) {
         var9 = var11.size();
      } else {
         var9 = 0;
      }

      if ((var7 & 1) == 0) {
         this.removeGroup(var1);
      }

      for(var7 = 0; var7 < var9; ++var7) {
         ResolveInfo var12 = (ResolveInfo)var11.get(var7);
         Intent var13;
         if (var12.specificIndex < 0) {
            var13 = var6;
         } else {
            var13 = var5[var12.specificIndex];
         }

         var13 = new Intent(var13);
         var13.setComponent(new ComponentName(var12.activityInfo.applicationInfo.packageName, var12.activityInfo.name));
         MenuItem var14 = this.add(var1, var2, var3, var12.loadLabel(var10)).setIcon(var12.loadIcon(var10)).setIntent(var13);
         if (var8 != null && var12.specificIndex >= 0) {
            var8[var12.specificIndex] = var14;
         }
      }

      return var9;
   }

   public void addMenuPresenter(MenuPresenter var1) {
      this.mPresenters.add(new WeakReference(var1));
      var1.initForMenu(this.mContext, this);
      this.mIsActionItemsStale = true;
   }

   public SubMenu addSubMenu(int var1) {
      return this.addSubMenu(0, 0, 0, this.mResources.getString(var1));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, int var4) {
      return this.addSubMenu(var1, var2, var3, this.mResources.getString(var4));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      MenuItemImpl var6 = (MenuItemImpl)this.addInternal(var1, var2, var3, var4);
      SubMenuBuilder var5 = new SubMenuBuilder(this.mContext, this, var6);
      var6.setSubMenu(var5);
      return var5;
   }

   public SubMenu addSubMenu(CharSequence var1) {
      return this.addSubMenu(0, 0, 0, var1);
   }

   public void changeMenuMode() {
      if (this.mCallback != null) {
         this.mCallback.onMenuModeChange(this);
      }

   }

   public void clear() {
      if (this.mExpandedItem != null) {
         this.collapseItemActionView(this.mExpandedItem);
      }

      this.mItems.clear();
      this.onItemsChanged(true);
   }

   public void clearAll() {
      this.mPreventDispatchingItemsChanged = true;
      this.clear();
      this.clearHeader();
      this.mPreventDispatchingItemsChanged = false;
      this.mItemsChangedWhileDispatchPrevented = false;
      this.onItemsChanged(true);
   }

   public void clearHeader() {
      this.mHeaderIcon = null;
      this.mHeaderTitle = null;
      this.mHeaderView = null;
      this.onItemsChanged(false);
   }

   public void close() {
      this.close(true);
   }

   final void close(boolean var1) {
      if (!this.mIsClosing) {
         this.mIsClosing = true;
         Iterator var2 = this.mPresenters.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            MenuPresenter var4 = (MenuPresenter)var3.get();
            if (var4 == null) {
               this.mPresenters.remove(var3);
            } else {
               var4.onCloseMenu(this, var1);
            }
         }

         this.mIsClosing = false;
      }
   }

   public boolean collapseItemActionView(MenuItemImpl var1) {
      boolean var2;
      if (!this.mPresenters.isEmpty() && this.mExpandedItem == var1) {
         var2 = false;
         this.stopDispatchingItemsChanged();
         Iterator var4 = this.mPresenters.iterator();

         boolean var3;
         while(true) {
            var3 = var2;
            if (!var4.hasNext()) {
               break;
            }

            WeakReference var5 = (WeakReference)var4.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if (var6 == null) {
               this.mPresenters.remove(var5);
            } else {
               var3 = var6.collapseItemActionView(this, var1);
               var2 = var3;
               if (var3) {
                  break;
               }
            }
         }

         this.startDispatchingItemsChanged();
         var2 = var3;
         if (var3) {
            this.mExpandedItem = null;
            return var3;
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean dispatchMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      return this.mCallback != null && this.mCallback.onMenuItemSelected(var1, var2);
   }

   public boolean expandItemActionView(MenuItemImpl var1) {
      boolean var2;
      if (this.mPresenters.isEmpty()) {
         var2 = false;
      } else {
         var2 = false;
         this.stopDispatchingItemsChanged();
         Iterator var4 = this.mPresenters.iterator();

         boolean var3;
         while(true) {
            var3 = var2;
            if (!var4.hasNext()) {
               break;
            }

            WeakReference var5 = (WeakReference)var4.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if (var6 == null) {
               this.mPresenters.remove(var5);
            } else {
               var3 = var6.expandItemActionView(this, var1);
               var2 = var3;
               if (var3) {
                  break;
               }
            }
         }

         this.startDispatchingItemsChanged();
         var2 = var3;
         if (var3) {
            this.mExpandedItem = var1;
            return var3;
         }
      }

      return var2;
   }

   public int findGroupIndex(int var1) {
      return this.findGroupIndex(var1, 0);
   }

   public int findGroupIndex(int var1, int var2) {
      int var4 = this.size();
      int var3 = var2;
      if (var2 < 0) {
         var3 = 0;
      }

      while(var3 < var4) {
         if (((MenuItemImpl)this.mItems.get(var3)).getGroupId() == var1) {
            return var3;
         }

         ++var3;
      }

      return -1;
   }

   public MenuItem findItem(int var1) {
      int var3 = this.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         MenuItemImpl var4 = (MenuItemImpl)this.mItems.get(var2);
         if (var4.getItemId() == var1) {
            return var4;
         }

         if (var4.hasSubMenu()) {
            MenuItem var5 = var4.getSubMenu().findItem(var1);
            if (var5 != null) {
               return var5;
            }
         }
      }

      return null;
   }

   public int findItemIndex(int var1) {
      int var3 = this.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         if (((MenuItemImpl)this.mItems.get(var2)).getItemId() == var1) {
            return var2;
         }
      }

      return -1;
   }

   MenuItemImpl findItemWithShortcutForKey(int var1, KeyEvent var2) {
      ArrayList var8 = this.mTempShortcutItemList;
      var8.clear();
      this.findItemsWithShortcutForKey(var8, var1, var2);
      if (!var8.isEmpty()) {
         int var5 = var2.getMetaState();
         KeyData var9 = new KeyData();
         var2.getKeyData(var9);
         int var6 = var8.size();
         if (var6 == 1) {
            return (MenuItemImpl)var8.get(0);
         }

         boolean var7 = this.isQwertyMode();

         for(int var3 = 0; var3 < var6; ++var3) {
            MenuItemImpl var10 = (MenuItemImpl)var8.get(var3);
            char var4;
            if (var7) {
               var4 = var10.getAlphabeticShortcut();
            } else {
               var4 = var10.getNumericShortcut();
            }

            if (var4 == var9.meta[0] && (var5 & 2) == 0 || var4 == var9.meta[2] && (var5 & 2) != 0 || var7 && var4 == '\b' && var1 == 67) {
               return var10;
            }
         }
      }

      return null;
   }

   void findItemsWithShortcutForKey(List var1, int var2, KeyEvent var3) {
      boolean var8 = this.isQwertyMode();
      int var6 = var3.getMetaState();
      KeyData var9 = new KeyData();
      if (var3.getKeyData(var9) || var2 == 67) {
         int var7 = this.mItems.size();

         for(int var4 = 0; var4 < var7; ++var4) {
            MenuItemImpl var10 = (MenuItemImpl)this.mItems.get(var4);
            if (var10.hasSubMenu()) {
               ((MenuBuilder)var10.getSubMenu()).findItemsWithShortcutForKey(var1, var2, var3);
            }

            char var5;
            if (var8) {
               var5 = var10.getAlphabeticShortcut();
            } else {
               var5 = var10.getNumericShortcut();
            }

            if ((var6 & 5) == 0 && var5 != 0 && (var5 == var9.meta[0] || var5 == var9.meta[2] || var8 && var5 == '\b' && var2 == 67) && var10.isEnabled()) {
               var1.add(var10);
            }
         }
      }

   }

   public void flagActionItems() {
      if (this.mIsActionItemsStale) {
         boolean var1 = false;
         Iterator var3 = this.mPresenters.iterator();

         while(var3.hasNext()) {
            WeakReference var4 = (WeakReference)var3.next();
            MenuPresenter var5 = (MenuPresenter)var4.get();
            if (var5 == null) {
               this.mPresenters.remove(var4);
            } else {
               var1 |= var5.flagActionItems();
            }
         }

         if (var1) {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            ArrayList var7 = this.getVisibleItems();
            int var2 = var7.size();

            for(int var6 = 0; var6 < var2; ++var6) {
               MenuItemImpl var8 = (MenuItemImpl)var7.get(var6);
               if (var8.isActionButton()) {
                  this.mActionItems.add(var8);
               } else {
                  this.mNonActionItems.add(var8);
               }
            }
         } else {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            this.mNonActionItems.addAll(this.getVisibleItems());
         }

         this.mIsActionItemsStale = false;
      }
   }

   ArrayList getActionItems() {
      this.flagActionItems();
      return this.mActionItems;
   }

   protected String getActionViewStatesKey() {
      return "android:menu:actionviewstates";
   }

   public Context getContext() {
      return this.mContext;
   }

   public MenuItemImpl getExpandedItem() {
      return this.mExpandedItem;
   }

   public Drawable getHeaderIcon() {
      return this.mHeaderIcon;
   }

   public CharSequence getHeaderTitle() {
      return this.mHeaderTitle;
   }

   public View getHeaderView() {
      return this.mHeaderView;
   }

   public MenuItem getItem(int var1) {
      return (MenuItem)this.mItems.get(var1);
   }

   ArrayList getNonActionItems() {
      this.flagActionItems();
      return this.mNonActionItems;
   }

   boolean getOptionalIconsVisible() {
      return this.mOptionalIconsVisible;
   }

   Resources getResources() {
      return this.mResources;
   }

   public MenuBuilder getRootMenu() {
      return this;
   }

   ArrayList getVisibleItems() {
      if (!this.mIsVisibleItemsStale) {
         return this.mVisibleItems;
      } else {
         this.mVisibleItems.clear();
         int var2 = this.mItems.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            MenuItemImpl var3 = (MenuItemImpl)this.mItems.get(var1);
            if (var3.isVisible()) {
               this.mVisibleItems.add(var3);
            }
         }

         this.mIsVisibleItemsStale = false;
         this.mIsActionItemsStale = true;
         return this.mVisibleItems;
      }
   }

   public boolean hasVisibleItems() {
      int var2 = this.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         if (((MenuItemImpl)this.mItems.get(var1)).isVisible()) {
            return true;
         }
      }

      return false;
   }

   boolean isQwertyMode() {
      return this.mQwertyMode;
   }

   public boolean isShortcutKey(int var1, KeyEvent var2) {
      return this.findItemWithShortcutForKey(var1, var2) != null;
   }

   public boolean isShortcutsVisible() {
      return this.mShortcutsVisible;
   }

   void onItemActionRequestChanged(MenuItemImpl var1) {
      this.mIsActionItemsStale = true;
      this.onItemsChanged(true);
   }

   void onItemVisibleChanged(MenuItemImpl var1) {
      this.mIsVisibleItemsStale = true;
      this.onItemsChanged(true);
   }

   void onItemsChanged(boolean var1) {
      if (!this.mPreventDispatchingItemsChanged) {
         if (var1) {
            this.mIsVisibleItemsStale = true;
            this.mIsActionItemsStale = true;
         }

         this.dispatchPresenterUpdate(var1);
      } else {
         this.mItemsChangedWhileDispatchPrevented = true;
      }
   }

   public boolean performIdentifierAction(int var1, int var2) {
      return this.performItemAction(this.findItem(var1), var2);
   }

   public boolean performItemAction(MenuItem var1, int var2) {
      MenuItemImpl var6 = (MenuItemImpl)var1;
      boolean var4;
      if (var6 != null && var6.isEnabled()) {
         boolean var5 = var6.invoke();
         ActionProvider var7 = var6.getSupportActionProvider();
         boolean var3;
         if (var7 != null && var7.hasSubMenu()) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var6.hasCollapsibleActionView()) {
            var5 |= var6.expandActionView();
            var4 = var5;
            if (var5) {
               this.close(true);
               return var5;
            }
         } else if (!var6.hasSubMenu() && !var3) {
            var4 = var5;
            if ((var2 & 1) == 0) {
               this.close(true);
               return var5;
            }
         } else {
            this.close(false);
            if (!var6.hasSubMenu()) {
               var6.setSubMenu(new SubMenuBuilder(this.getContext(), this, var6));
            }

            SubMenuBuilder var8 = (SubMenuBuilder)var6.getSubMenu();
            if (var3) {
               var7.onPrepareSubMenu(var8);
            }

            var5 |= this.dispatchSubMenuSelected(var8);
            var4 = var5;
            if (!var5) {
               this.close(true);
               return var5;
            }
         }
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean performShortcut(int var1, KeyEvent var2, int var3) {
      MenuItemImpl var5 = this.findItemWithShortcutForKey(var1, var2);
      boolean var4 = false;
      if (var5 != null) {
         var4 = this.performItemAction(var5, var3);
      }

      if ((var3 & 2) != 0) {
         this.close(true);
      }

      return var4;
   }

   public void removeGroup(int var1) {
      int var3 = this.findGroupIndex(var1);
      if (var3 >= 0) {
         int var4 = this.mItems.size();

         for(int var2 = 0; var2 < var4 - var3 && ((MenuItemImpl)this.mItems.get(var3)).getGroupId() == var1; ++var2) {
            this.removeItemAtInt(var3, false);
         }

         this.onItemsChanged(true);
      }

   }

   public void removeItem(int var1) {
      this.removeItemAtInt(this.findItemIndex(var1), true);
   }

   public void removeItemAt(int var1) {
      this.removeItemAtInt(var1, true);
   }

   public void removeMenuPresenter(MenuPresenter var1) {
      Iterator var2 = this.mPresenters.iterator();

      while(true) {
         WeakReference var3;
         MenuPresenter var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (WeakReference)var2.next();
            var4 = (MenuPresenter)var3.get();
         } while(var4 != null && var4 != var1);

         this.mPresenters.remove(var3);
      }
   }

   public void restoreActionViewStates(Bundle var1) {
      if (var1 != null) {
         SparseArray var4 = var1.getSparseParcelableArray(this.getActionViewStatesKey());
         int var3 = this.size();

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            MenuItem var5 = this.getItem(var2);
            View var6 = MenuItemCompat.getActionView(var5);
            if (var6 != null && var6.getId() != -1) {
               var6.restoreHierarchyState(var4);
            }

            if (var5.hasSubMenu()) {
               ((SubMenuBuilder)var5.getSubMenu()).restoreActionViewStates(var1);
            }
         }

         var2 = var1.getInt("android:menu:expandedactionview");
         if (var2 > 0) {
            MenuItem var7 = this.findItem(var2);
            if (var7 != null) {
               MenuItemCompat.expandActionView(var7);
               return;
            }
         }
      }

   }

   public void restorePresenterStates(Bundle var1) {
      this.dispatchRestoreInstanceState(var1);
   }

   public void saveActionViewStates(Bundle var1) {
      SparseArray var4 = null;
      int var3 = this.size();

      SparseArray var6;
      for(int var2 = 0; var2 < var3; var4 = var6) {
         MenuItem var7 = this.getItem(var2);
         View var8 = MenuItemCompat.getActionView(var7);
         var6 = var4;
         if (var8 != null) {
            var6 = var4;
            if (var8.getId() != -1) {
               SparseArray var5 = var4;
               if (var4 == null) {
                  var5 = new SparseArray();
               }

               var8.saveHierarchyState(var5);
               var6 = var5;
               if (MenuItemCompat.isActionViewExpanded(var7)) {
                  var1.putInt("android:menu:expandedactionview", var7.getItemId());
                  var6 = var5;
               }
            }
         }

         if (var7.hasSubMenu()) {
            ((SubMenuBuilder)var7.getSubMenu()).saveActionViewStates(var1);
         }

         ++var2;
      }

      if (var4 != null) {
         var1.putSparseParcelableArray(this.getActionViewStatesKey(), var4);
      }

   }

   public void savePresenterStates(Bundle var1) {
      this.dispatchSaveInstanceState(var1);
   }

   public void setCallback(MenuBuilder.Callback var1) {
      this.mCallback = var1;
   }

   public void setCurrentMenuInfo(ContextMenuInfo var1) {
      this.mCurrentMenuInfo = var1;
   }

   public MenuBuilder setDefaultShowAsAction(int var1) {
      this.mDefaultShowAsAction = var1;
      return this;
   }

   void setExclusiveItemChecked(MenuItem var1) {
      int var3 = var1.getGroupId();
      int var4 = this.mItems.size();

      for(int var2 = 0; var2 < var4; ++var2) {
         MenuItemImpl var6 = (MenuItemImpl)this.mItems.get(var2);
         if (var6.getGroupId() == var3 && var6.isExclusiveCheckable() && var6.isCheckable()) {
            boolean var5;
            if (var6 == var1) {
               var5 = true;
            } else {
               var5 = false;
            }

            var6.setCheckedInt(var5);
         }
      }

   }

   public void setGroupCheckable(int var1, boolean var2, boolean var3) {
      int var5 = this.mItems.size();

      for(int var4 = 0; var4 < var5; ++var4) {
         MenuItemImpl var6 = (MenuItemImpl)this.mItems.get(var4);
         if (var6.getGroupId() == var1) {
            var6.setExclusiveCheckable(var3);
            var6.setCheckable(var2);
         }
      }

   }

   public void setGroupEnabled(int var1, boolean var2) {
      int var4 = this.mItems.size();

      for(int var3 = 0; var3 < var4; ++var3) {
         MenuItemImpl var5 = (MenuItemImpl)this.mItems.get(var3);
         if (var5.getGroupId() == var1) {
            var5.setEnabled(var2);
         }
      }

   }

   public void setGroupVisible(int var1, boolean var2) {
      int var6 = this.mItems.size();
      boolean var4 = false;

      boolean var5;
      for(int var3 = 0; var3 < var6; var4 = var5) {
         MenuItemImpl var7 = (MenuItemImpl)this.mItems.get(var3);
         var5 = var4;
         if (var7.getGroupId() == var1) {
            var5 = var4;
            if (var7.setVisibleInt(var2)) {
               var5 = true;
            }
         }

         ++var3;
      }

      if (var4) {
         this.onItemsChanged(true);
      }

   }

   protected MenuBuilder setHeaderIconInt(int var1) {
      this.setHeaderInternal(0, (CharSequence)null, var1, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderIconInt(Drawable var1) {
      this.setHeaderInternal(0, (CharSequence)null, 0, var1, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderTitleInt(int var1) {
      this.setHeaderInternal(var1, (CharSequence)null, 0, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderTitleInt(CharSequence var1) {
      this.setHeaderInternal(0, var1, 0, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderViewInt(View var1) {
      this.setHeaderInternal(0, (CharSequence)null, 0, (Drawable)null, var1);
      return this;
   }

   void setOptionalIconsVisible(boolean var1) {
      this.mOptionalIconsVisible = var1;
   }

   public void setQwertyMode(boolean var1) {
      this.mQwertyMode = var1;
      this.onItemsChanged(false);
   }

   public void setShortcutsVisible(boolean var1) {
      if (this.mShortcutsVisible != var1) {
         this.setShortcutsVisibleInner(var1);
         this.onItemsChanged(false);
      }
   }

   public int size() {
      return this.mItems.size();
   }

   public void startDispatchingItemsChanged() {
      this.mPreventDispatchingItemsChanged = false;
      if (this.mItemsChangedWhileDispatchPrevented) {
         this.mItemsChangedWhileDispatchPrevented = false;
         this.onItemsChanged(true);
      }

   }

   public void stopDispatchingItemsChanged() {
      if (!this.mPreventDispatchingItemsChanged) {
         this.mPreventDispatchingItemsChanged = true;
         this.mItemsChangedWhileDispatchPrevented = false;
      }

   }

   public interface Callback {
      boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2);

      void onMenuModeChange(MenuBuilder var1);
   }

   public interface ItemInvoker {
      boolean invokeItem(MenuItemImpl var1);
   }
}
