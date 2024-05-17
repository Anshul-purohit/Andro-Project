package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$integer;
import android.support.v7.appcompat.R$layout;
import android.support.v7.internal.view.ActionBarPolicy;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import java.util.ArrayList;

public class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
   private static final String TAG = "ActionMenuPresenter";
   private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
   private ActionMenuPresenter.ActionButtonSubmenu mActionButtonPopup;
   private int mActionItemWidthLimit;
   private boolean mExpandedActionViewsExclusive;
   private int mMaxItems;
   private boolean mMaxItemsSet;
   private int mMinCellSize;
   int mOpenSubMenuId;
   private View mOverflowButton;
   private ActionMenuPresenter.OverflowPopup mOverflowPopup;
   final ActionMenuPresenter.PopupPresenterCallback mPopupPresenterCallback = new ActionMenuPresenter.PopupPresenterCallback();
   private ActionMenuPresenter.OpenOverflowRunnable mPostedOpenRunnable;
   private boolean mReserveOverflow;
   private boolean mReserveOverflowSet;
   private View mScrapActionButtonView;
   private boolean mStrictWidthLimit;
   private int mWidthLimit;
   private boolean mWidthLimitSet;

   public ActionMenuPresenter(Context var1) {
      super(var1, R$layout.abc_action_menu_layout, R$layout.abc_action_menu_item_layout);
   }

   private View findViewForItem(MenuItem var1) {
      ViewGroup var6 = (ViewGroup)this.mMenuView;
      View var4;
      if (var6 == null) {
         var4 = null;
      } else {
         int var3 = var6.getChildCount();
         int var2 = 0;

         while(true) {
            if (var2 >= var3) {
               return null;
            }

            View var5 = var6.getChildAt(var2);
            if (var5 instanceof MenuView.ItemView) {
               var4 = var5;
               if (((MenuView.ItemView)var5).getItemData() == var1) {
                  break;
               }
            }

            ++var2;
         }
      }

      return var4;
   }

   public void bindItemView(MenuItemImpl var1, MenuView.ItemView var2) {
      var2.initialize(var1, 0);
      ActionMenuView var3 = (ActionMenuView)this.mMenuView;
      ((ActionMenuItemView)var2).setItemInvoker(var3);
   }

   public boolean dismissPopupMenus() {
      return this.hideOverflowMenu() | this.hideSubMenus();
   }

   public boolean filterLeftoverView(ViewGroup var1, int var2) {
      return var1.getChildAt(var2) == this.mOverflowButton ? false : super.filterLeftoverView(var1, var2);
   }

   public boolean flagActionItems() {
      ArrayList var16 = this.mMenu.getVisibleItems();
      int var11 = var16.size();
      int var1 = this.mMaxItems;
      int var9 = this.mActionItemWidthLimit;
      int var12 = MeasureSpec.makeMeasureSpec(0, 0);
      ViewGroup var17 = (ViewGroup)this.mMenuView;
      int var2 = 0;
      int var4 = 0;
      byte var7 = 0;
      boolean var5 = false;

      int var3;
      int var6;
      for(var3 = 0; var3 < var11; var1 = var6) {
         MenuItemImpl var18 = (MenuItemImpl)var16.get(var3);
         if (var18.requiresActionButton()) {
            ++var2;
         } else if (var18.requestsActionButton()) {
            ++var4;
         } else {
            var5 = true;
         }

         var6 = var1;
         if (this.mExpandedActionViewsExclusive) {
            var6 = var1;
            if (var18.isActionViewExpanded()) {
               var6 = 0;
            }
         }

         ++var3;
      }

      var3 = var1;
      if (this.mReserveOverflow) {
         label149: {
            if (!var5) {
               var3 = var1;
               if (var2 + var4 <= var1) {
                  break label149;
               }
            }

            var3 = var1 - 1;
         }
      }

      var3 -= var2;
      SparseBooleanArray var25 = this.mActionButtonGroups;
      var25.clear();
      int var8 = 0;
      var2 = 0;
      if (this.mStrictWidthLimit) {
         var2 = var9 / this.mMinCellSize;
         var1 = this.mMinCellSize;
         var8 = this.mMinCellSize + var9 % var1 / var2;
      }

      byte var21 = 0;
      var4 = var9;
      var9 = var21;

      int var23;
      for(var1 = var7; var9 < var11; var4 = var23) {
         MenuItemImpl var19 = (MenuItemImpl)var16.get(var9);
         int var10;
         View var20;
         int var24;
         if (var19.requiresActionButton()) {
            var20 = this.getItemView(var19, this.mScrapActionButtonView, var17);
            if (this.mScrapActionButtonView == null) {
               this.mScrapActionButtonView = var20;
            }

            if (this.mStrictWidthLimit) {
               var2 -= ActionMenuView.measureChildForCells(var20, var8, var2, var12, 0);
            } else {
               var20.measure(var12, var12);
            }

            var24 = var20.getMeasuredWidth();
            var23 = var4 - var24;
            var6 = var1;
            if (var1 == 0) {
               var6 = var24;
            }

            var1 = var19.getGroupId();
            if (var1 != 0) {
               var25.put(var1, true);
            }

            var19.setIsActionButton(true);
            var10 = var3;
            var24 = var2;
         } else {
            var24 = var2;
            var6 = var1;
            var10 = var3;
            var23 = var4;
            if (var19.requestsActionButton()) {
               var10 = var19.getGroupId();
               boolean var15 = var25.get(var10);
               boolean var13;
               if (var3 <= 0 && !var15 || var4 <= 0 || this.mStrictWidthLimit && var2 <= 0) {
                  var13 = false;
               } else {
                  var13 = true;
               }

               var24 = var2;
               var6 = var1;
               boolean var14 = var13;
               var23 = var4;
               if (var13) {
                  var20 = this.getItemView(var19, this.mScrapActionButtonView, var17);
                  if (this.mScrapActionButtonView == null) {
                     this.mScrapActionButtonView = var20;
                  }

                  if (this.mStrictWidthLimit) {
                     var6 = ActionMenuView.measureChildForCells(var20, var8, var2, var12, 0);
                     var23 = var2 - var6;
                     var2 = var23;
                     if (var6 == 0) {
                        var13 = false;
                        var2 = var23;
                     }
                  } else {
                     var20.measure(var12, var12);
                  }

                  var24 = var20.getMeasuredWidth();
                  var23 = var4 - var24;
                  var6 = var1;
                  if (var1 == 0) {
                     var6 = var24;
                  }

                  boolean var22;
                  if (this.mStrictWidthLimit) {
                     if (var23 >= 0) {
                        var22 = true;
                     } else {
                        var22 = false;
                     }

                     var14 = var13 & var22;
                     var24 = var2;
                  } else {
                     if (var23 + var6 > 0) {
                        var22 = true;
                     } else {
                        var22 = false;
                     }

                     var14 = var13 & var22;
                     var24 = var2;
                  }
               }

               if (var14 && var10 != 0) {
                  var25.put(var10, true);
                  var1 = var3;
               } else {
                  var1 = var3;
                  if (var15) {
                     var25.put(var10, false);
                     var2 = 0;

                     while(true) {
                        var1 = var3;
                        if (var2 >= var9) {
                           break;
                        }

                        MenuItemImpl var26 = (MenuItemImpl)var16.get(var2);
                        var1 = var3;
                        if (var26.getGroupId() == var10) {
                           var1 = var3;
                           if (var26.isActionButton()) {
                              var1 = var3 + 1;
                           }

                           var26.setIsActionButton(false);
                        }

                        ++var2;
                        var3 = var1;
                     }
                  }
               }

               var2 = var1;
               if (var14) {
                  var2 = var1 - 1;
               }

               var19.setIsActionButton(var14);
               var10 = var2;
            }
         }

         ++var9;
         var2 = var24;
         var1 = var6;
         var3 = var10;
      }

      return true;
   }

   public View getItemView(MenuItemImpl var1, View var2, ViewGroup var3) {
      View var5 = var1.getActionView();
      if (var5 == null || var1.hasCollapsibleActionView()) {
         var5 = var2;
         if (!(var2 instanceof ActionMenuItemView)) {
            var5 = null;
         }

         var5 = super.getItemView(var1, var5, var3);
      }

      byte var4;
      if (var1.isActionViewExpanded()) {
         var4 = 8;
      } else {
         var4 = 0;
      }

      var5.setVisibility(var4);
      ActionMenuView var6 = (ActionMenuView)var3;
      LayoutParams var7 = var5.getLayoutParams();
      if (!var6.checkLayoutParams(var7)) {
         var5.setLayoutParams(var6.generateLayoutParams(var7));
      }

      return var5;
   }

   public MenuView getMenuView(ViewGroup var1) {
      MenuView var2 = super.getMenuView(var1);
      ((ActionMenuView)var2).setPresenter(this);
      return var2;
   }

   public boolean hideOverflowMenu() {
      if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
         ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
         this.mPostedOpenRunnable = null;
         return true;
      } else {
         ActionMenuPresenter.OverflowPopup var1 = this.mOverflowPopup;
         if (var1 != null) {
            var1.dismiss();
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hideSubMenus() {
      if (this.mActionButtonPopup != null) {
         this.mActionButtonPopup.dismiss();
         return true;
      } else {
         return false;
      }
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
      super.initForMenu(var1, var2);
      Resources var6 = var1.getResources();
      ActionBarPolicy var5 = ActionBarPolicy.get(var1);
      if (!this.mReserveOverflowSet) {
         this.mReserveOverflow = var5.showsOverflowMenuButton();
      }

      if (!this.mWidthLimitSet) {
         this.mWidthLimit = var5.getEmbeddedMenuWidthLimit();
      }

      if (!this.mMaxItemsSet) {
         this.mMaxItems = var5.getMaxActionButtons();
      }

      int var3 = this.mWidthLimit;
      if (this.mReserveOverflow) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
            int var4 = MeasureSpec.makeMeasureSpec(0, 0);
            this.mOverflowButton.measure(var4, var4);
         }

         var3 -= this.mOverflowButton.getMeasuredWidth();
      } else {
         this.mOverflowButton = null;
      }

      this.mActionItemWidthLimit = var3;
      this.mMinCellSize = (int)(56.0F * var6.getDisplayMetrics().density);
      this.mScrapActionButtonView = null;
   }

   public boolean isOverflowMenuShowing() {
      return this.mOverflowPopup != null && this.mOverflowPopup.isShowing();
   }

   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      this.dismissPopupMenus();
      super.onCloseMenu(var1, var2);
   }

   public void onConfigurationChanged(Configuration var1) {
      if (!this.mMaxItemsSet) {
         this.mMaxItems = this.mContext.getResources().getInteger(R$integer.abc_max_action_buttons);
      }

      if (this.mMenu != null) {
         this.mMenu.onItemsChanged(true);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      ActionMenuPresenter.SavedState var2 = (ActionMenuPresenter.SavedState)var1;
      if (var2.openSubMenuId > 0) {
         MenuItem var3 = this.mMenu.findItem(var2.openSubMenuId);
         if (var3 != null) {
            this.onSubMenuSelected((SubMenuBuilder)var3.getSubMenu());
         }
      }

   }

   public Parcelable onSaveInstanceState() {
      ActionMenuPresenter.SavedState var1 = new ActionMenuPresenter.SavedState();
      var1.openSubMenuId = this.mOpenSubMenuId;
      return var1;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      if (var1.hasVisibleItems()) {
         SubMenuBuilder var2;
         for(var2 = var1; var2.getParentMenu() != this.mMenu; var2 = (SubMenuBuilder)var2.getParentMenu()) {
         }

         if (this.findViewForItem(var2.getItem()) == null) {
            if (this.mOverflowButton == null) {
               return false;
            }

            View var3 = this.mOverflowButton;
         }

         this.mOpenSubMenuId = var1.getItem().getItemId();
         this.mActionButtonPopup = new ActionMenuPresenter.ActionButtonSubmenu(var1);
         this.mActionButtonPopup.show((IBinder)null);
         super.onSubMenuSelected(var1);
         return true;
      } else {
         return false;
      }
   }

   public void onSubUiVisibilityChanged(boolean var1) {
      if (var1) {
         super.onSubMenuSelected((SubMenuBuilder)null);
      } else {
         this.mMenu.close(false);
      }
   }

   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mExpandedActionViewsExclusive = var1;
   }

   public void setItemLimit(int var1) {
      this.mMaxItems = var1;
      this.mMaxItemsSet = true;
   }

   public void setReserveOverflow(boolean var1) {
      this.mReserveOverflow = var1;
      this.mReserveOverflowSet = true;
   }

   public void setWidthLimit(int var1, boolean var2) {
      this.mWidthLimit = var1;
      this.mStrictWidthLimit = var2;
      this.mWidthLimitSet = true;
   }

   public boolean shouldIncludeItem(int var1, MenuItemImpl var2) {
      return var2.isActionButton();
   }

   public boolean showOverflowMenu() {
      if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null) {
         this.mPostedOpenRunnable = new ActionMenuPresenter.OpenOverflowRunnable(new ActionMenuPresenter.OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true));
         ((View)this.mMenuView).post(this.mPostedOpenRunnable);
         super.onSubMenuSelected((SubMenuBuilder)null);
         return true;
      } else {
         return false;
      }
   }

   public void updateMenuView(boolean var1) {
      super.updateMenuView(var1);
      if (this.mMenuView != null) {
         int var2;
         ArrayList var4;
         if (this.mMenu != null) {
            var4 = this.mMenu.getActionItems();
            int var3 = var4.size();

            for(var2 = 0; var2 < var3; ++var2) {
               ActionProvider var5 = ((MenuItemImpl)var4.get(var2)).getSupportActionProvider();
               if (var5 != null) {
                  var5.setSubUiVisibilityListener(this);
               }
            }
         }

         if (this.mMenu != null) {
            var4 = this.mMenu.getNonActionItems();
         } else {
            var4 = null;
         }

         boolean var7 = false;
         boolean var6 = var7;
         if (this.mReserveOverflow) {
            var6 = var7;
            if (var4 != null) {
               var2 = var4.size();
               if (var2 == 1) {
                  if (!((MenuItemImpl)var4.get(0)).isActionViewExpanded()) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }
               } else if (var2 > 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }
            }
         }

         if (var6) {
            if (this.mOverflowButton == null) {
               this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
            }

            ViewGroup var8 = (ViewGroup)this.mOverflowButton.getParent();
            if (var8 != this.mMenuView) {
               if (var8 != null) {
                  var8.removeView(this.mOverflowButton);
               }

               ActionMenuView var9 = (ActionMenuView)this.mMenuView;
               var9.addView(this.mOverflowButton, var9.generateOverflowButtonLayoutParams());
            }
         } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            ((ViewGroup)this.mMenuView).removeView(this.mOverflowButton);
         }

         ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
      }
   }

   private class ActionButtonSubmenu extends MenuDialogHelper {
      public ActionButtonSubmenu(SubMenuBuilder var2) {
         super(var2);
         ActionMenuPresenter.this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      public void onDismiss(DialogInterface var1) {
         super.onDismiss(var1);
         ActionMenuPresenter.this.mActionButtonPopup = null;
         ActionMenuPresenter.this.mOpenSubMenuId = 0;
      }
   }

   private class OpenOverflowRunnable implements Runnable {
      private ActionMenuPresenter.OverflowPopup mPopup;

      public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup var2) {
         this.mPopup = var2;
      }

      public void run() {
         ActionMenuPresenter.this.mMenu.changeMenuMode();
         View var1 = (View)ActionMenuPresenter.this.mMenuView;
         if (var1 != null && var1.getWindowToken() != null && this.mPopup.tryShow()) {
            ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
         }

         ActionMenuPresenter.this.mPostedOpenRunnable = null;
      }
   }

   private class OverflowMenuButton extends ImageButton implements ActionMenuView.ActionMenuChildView {
      public OverflowMenuButton(Context var2) {
         super(var2, (AttributeSet)null, R$attr.actionOverflowButtonStyle);
         this.setClickable(true);
         this.setFocusable(true);
         this.setVisibility(0);
         this.setEnabled(true);
      }

      public boolean needsDividerAfter() {
         return false;
      }

      public boolean needsDividerBefore() {
         return false;
      }

      public boolean performClick() {
         if (super.performClick()) {
            return true;
         } else {
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
         }
      }
   }

   private class OverflowPopup extends MenuPopupHelper {
      public OverflowPopup(Context var2, MenuBuilder var3, View var4, boolean var5) {
         super(var2, var3, var4, var5);
         this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      public void onDismiss() {
         super.onDismiss();
         ActionMenuPresenter.this.mMenu.close();
         ActionMenuPresenter.this.mOverflowPopup = null;
      }
   }

   private class PopupPresenterCallback implements MenuPresenter.Callback {
      private PopupPresenterCallback() {
      }

      // $FF: synthetic method
      PopupPresenterCallback(Object var2) {
         this();
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         if (var1 instanceof SubMenuBuilder) {
            ((SubMenuBuilder)var1).getRootMenu().close(false);
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if (var1 == null) {
            return false;
         } else {
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)var1).getItem().getItemId();
            return false;
         }
      }
   }

   private static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public ActionMenuPresenter.SavedState createFromParcel(Parcel var1) {
            return new ActionMenuPresenter.SavedState(var1);
         }

         public ActionMenuPresenter.SavedState[] newArray(int var1) {
            return new ActionMenuPresenter.SavedState[var1];
         }
      };
      public int openSubMenuId;

      SavedState() {
      }

      SavedState(Parcel var1) {
         this.openSubMenuId = var1.readInt();
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.openSubMenuId);
      }
   }
}
