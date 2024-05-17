package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutICS implements MenuBuilder.ItemInvoker, MenuView {
   static final int GENERATED_ITEM_PADDING = 4;
   static final int MIN_CELL_SIZE = 56;
   private static final String TAG = "ActionMenuView";
   private boolean mFormatItems;
   private int mFormatItemsWidth;
   private int mGeneratedItemPadding;
   private int mMaxItemHeight;
   private int mMeasuredExtraWidth;
   private MenuBuilder mMenu;
   private int mMinCellSize;
   private ActionMenuPresenter mPresenter;
   private boolean mReserveOverflow;

   public ActionMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setBaselineAligned(false);
      float var3 = var1.getResources().getDisplayMetrics().density;
      this.mMinCellSize = (int)(56.0F * var3);
      this.mGeneratedItemPadding = (int)(4.0F * var3);
      TypedArray var4 = var1.obtainStyledAttributes(var2, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      this.mMaxItemHeight = var4.getDimensionPixelSize(1, 0);
      var4.recycle();
   }

   static int measureChildForCells(View var0, int var1, int var2, int var3, int var4) {
      ActionMenuView.LayoutParams var9 = (ActionMenuView.LayoutParams)var0.getLayoutParams();
      int var6 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var3) - var4, MeasureSpec.getMode(var3));
      ActionMenuItemView var8;
      if (var0 instanceof ActionMenuItemView) {
         var8 = (ActionMenuItemView)var0;
      } else {
         var8 = null;
      }

      boolean var10;
      if (var8 != null && var8.hasText()) {
         var10 = true;
      } else {
         var10 = false;
      }

      byte var5 = 0;
      var3 = var5;
      if (var2 > 0) {
         label47: {
            if (var10) {
               var3 = var5;
               if (var2 < 2) {
                  break label47;
               }
            }

            var0.measure(MeasureSpec.makeMeasureSpec(var1 * var2, Integer.MIN_VALUE), var6);
            int var11 = var0.getMeasuredWidth();
            var3 = var11 / var1;
            var2 = var3;
            if (var11 % var1 != 0) {
               var2 = var3 + 1;
            }

            var3 = var2;
            if (var10) {
               var3 = var2;
               if (var2 < 2) {
                  var3 = 2;
               }
            }
         }
      }

      boolean var7;
      if (!var9.isOverflowButton && var10) {
         var7 = true;
      } else {
         var7 = false;
      }

      var9.expandable = var7;
      var9.cellsUsed = var3;
      var0.measure(MeasureSpec.makeMeasureSpec(var3 * var1, 1073741824), var6);
      return var3;
   }

   private void onMeasureExactFormat(int var1, int var2) {
      int var19 = MeasureSpec.getMode(var2);
      var1 = MeasureSpec.getSize(var1);
      int var18 = MeasureSpec.getSize(var2);
      var2 = this.getPaddingLeft();
      int var6 = this.getPaddingRight();
      int var23 = this.getPaddingTop() + this.getPaddingBottom();
      int var10;
      if (var19 == 1073741824) {
         var10 = MeasureSpec.makeMeasureSpec(var18 - var23, 1073741824);
      } else {
         var10 = MeasureSpec.makeMeasureSpec(Math.min(this.mMaxItemHeight, var18 - var23), Integer.MIN_VALUE);
      }

      int var20 = var1 - (var2 + var6);
      var2 = var20 / this.mMinCellSize;
      var1 = this.mMinCellSize;
      if (var2 == 0) {
         this.setMeasuredDimension(var20, 0);
      } else {
         int var21 = this.mMinCellSize + var20 % var1 / var2;
         var6 = 0;
         int var11 = 0;
         int var8 = 0;
         int var12 = 0;
         boolean var7 = false;
         long var25 = 0L;
         int var22 = this.getChildCount();

         int var9;
         boolean var13;
         int var14;
         int var15;
         int var16;
         int var17;
         long var27;
         View var32;
         ActionMenuView.LayoutParams var33;
         for(var9 = 0; var9 < var22; var25 = var27) {
            var32 = this.getChildAt(var9);
            if (var32.getVisibility() == 8) {
               var27 = var25;
               var13 = var7;
            } else {
               boolean var31 = var32 instanceof ActionMenuItemView;
               var14 = var12 + 1;
               if (var31) {
                  var32.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
               }

               var33 = (ActionMenuView.LayoutParams)var32.getLayoutParams();
               var33.expanded = false;
               var33.extraPixels = 0;
               var33.cellsUsed = 0;
               var33.expandable = false;
               var33.leftMargin = 0;
               var33.rightMargin = 0;
               if (var31 && ((ActionMenuItemView)var32).hasText()) {
                  var31 = true;
               } else {
                  var31 = false;
               }

               var33.preventEdgeOffset = var31;
               if (var33.isOverflowButton) {
                  var1 = 1;
               } else {
                  var1 = var2;
               }

               int var24 = measureChildForCells(var32, var21, var1, var10, var23);
               var15 = Math.max(var11, var24);
               var1 = var8;
               if (var33.expandable) {
                  var1 = var8 + 1;
               }

               if (var33.isOverflowButton) {
                  var7 = true;
               }

               var16 = var2 - var24;
               var17 = Math.max(var6, var32.getMeasuredHeight());
               var2 = var16;
               var8 = var1;
               var13 = var7;
               var11 = var15;
               var6 = var17;
               var27 = var25;
               var12 = var14;
               if (var24 == 1) {
                  var27 = var25 | (long)(1 << var9);
                  var2 = var16;
                  var8 = var1;
                  var13 = var7;
                  var11 = var15;
                  var6 = var17;
                  var12 = var14;
               }
            }

            ++var9;
            var7 = var13;
         }

         if (var7 && var12 == 2) {
            var13 = true;
         } else {
            var13 = false;
         }

         boolean var34 = false;

         while(true) {
            var27 = var25;
            if (var8 <= 0) {
               break;
            }

            var27 = var25;
            if (var2 <= 0) {
               break;
            }

            var14 = Integer.MAX_VALUE;
            long var29 = 0L;
            var17 = 0;

            for(var15 = 0; var15 < var22; var17 = var9) {
               ActionMenuView.LayoutParams var39 = (ActionMenuView.LayoutParams)this.getChildAt(var15).getLayoutParams();
               if (!var39.expandable) {
                  var9 = var17;
                  var27 = var29;
                  var16 = var14;
               } else if (var39.cellsUsed < var14) {
                  var16 = var39.cellsUsed;
                  var27 = (long)(1 << var15);
                  var9 = 1;
               } else {
                  var16 = var14;
                  var27 = var29;
                  var9 = var17;
                  if (var39.cellsUsed == var14) {
                     var27 = var29 | (long)(1 << var15);
                     var9 = var17 + 1;
                     var16 = var14;
                  }
               }

               ++var15;
               var14 = var16;
               var29 = var27;
            }

            var25 |= var29;
            if (var17 > var2) {
               var27 = var25;
               break;
            }

            for(var1 = 0; var1 < var22; var25 = var27) {
               var32 = this.getChildAt(var1);
               var33 = (ActionMenuView.LayoutParams)var32.getLayoutParams();
               if (((long)(1 << var1) & var29) == 0L) {
                  var9 = var2;
                  var27 = var25;
                  if (var33.cellsUsed == var14 + 1) {
                     var27 = var25 | (long)(1 << var1);
                     var9 = var2;
                  }
               } else {
                  if (var13 && var33.preventEdgeOffset && var2 == 1) {
                     var32.setPadding(this.mGeneratedItemPadding + var21, 0, this.mGeneratedItemPadding, 0);
                  }

                  ++var33.cellsUsed;
                  var33.expanded = true;
                  var9 = var2 - 1;
                  var27 = var25;
               }

               ++var1;
               var2 = var9;
            }

            var34 = true;
         }

         boolean var38;
         if (!var7 && var12 == 1) {
            var38 = true;
         } else {
            var38 = false;
         }

         int var36 = var2;
         boolean var37 = var34;
         if (var2 > 0) {
            var36 = var2;
            var37 = var34;
            if (var27 != 0L) {
               label223: {
                  if (var2 >= var12 - 1 && !var38) {
                     var36 = var2;
                     var37 = var34;
                     if (var11 <= 1) {
                        break label223;
                     }
                  }

                  float var5 = (float)Long.bitCount(var27);
                  float var4 = var5;
                  if (!var38) {
                     float var3 = var5;
                     if ((1L & var27) != 0L) {
                        var3 = var5;
                        if (!((ActionMenuView.LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                           var3 = var5 - 0.5F;
                        }
                     }

                     var4 = var3;
                     if (((long)(1 << var22 - 1) & var27) != 0L) {
                        var4 = var3;
                        if (!((ActionMenuView.LayoutParams)this.getChildAt(var22 - 1).getLayoutParams()).preventEdgeOffset) {
                           var4 = var3 - 0.5F;
                        }
                     }
                  }

                  if (var4 > 0.0F) {
                     var36 = (int)((float)(var2 * var21) / var4);
                  } else {
                     var36 = 0;
                  }

                  boolean var35;
                  for(var8 = 0; var8 < var22; var34 = var35) {
                     if (((long)(1 << var8) & var27) == 0L) {
                        var35 = var34;
                     } else {
                        var32 = this.getChildAt(var8);
                        var33 = (ActionMenuView.LayoutParams)var32.getLayoutParams();
                        if (var32 instanceof ActionMenuItemView) {
                           var33.extraPixels = var36;
                           var33.expanded = true;
                           if (var8 == 0 && !var33.preventEdgeOffset) {
                              var33.leftMargin = -var36 / 2;
                           }

                           var35 = true;
                        } else if (var33.isOverflowButton) {
                           var33.extraPixels = var36;
                           var33.expanded = true;
                           var33.rightMargin = -var36 / 2;
                           var35 = true;
                        } else {
                           if (var8 != 0) {
                              var33.leftMargin = var36 / 2;
                           }

                           var35 = var34;
                           if (var8 != var22 - 1) {
                              var33.rightMargin = var36 / 2;
                              var35 = var34;
                           }
                        }
                     }

                     ++var8;
                  }

                  var36 = 0;
                  var37 = var34;
               }
            }
         }

         if (var37) {
            for(var1 = 0; var1 < var22; ++var1) {
               var32 = this.getChildAt(var1);
               var33 = (ActionMenuView.LayoutParams)var32.getLayoutParams();
               if (var33.expanded) {
                  var32.measure(MeasureSpec.makeMeasureSpec(var33.cellsUsed * var21 + var33.extraPixels, 1073741824), var10);
               }
            }
         }

         var1 = var18;
         if (var19 != 1073741824) {
            var1 = var6;
         }

         this.setMeasuredDimension(var20, var1);
         this.mMeasuredExtraWidth = var36 * var21;
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 != null && var1 instanceof ActionMenuView.LayoutParams;
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      return false;
   }

   protected ActionMenuView.LayoutParams generateDefaultLayoutParams() {
      ActionMenuView.LayoutParams var1 = new ActionMenuView.LayoutParams(-2, -2);
      var1.gravity = 16;
      return var1;
   }

   public ActionMenuView.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ActionMenuView.LayoutParams(this.getContext(), var1);
   }

   protected ActionMenuView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 instanceof ActionMenuView.LayoutParams) {
         ActionMenuView.LayoutParams var2 = new ActionMenuView.LayoutParams((ActionMenuView.LayoutParams)var1);
         if (var2.gravity <= 0) {
            var2.gravity = 16;
         }

         return var2;
      } else {
         return this.generateDefaultLayoutParams();
      }
   }

   public ActionMenuView.LayoutParams generateOverflowButtonLayoutParams() {
      ActionMenuView.LayoutParams var1 = this.generateDefaultLayoutParams();
      var1.isOverflowButton = true;
      return var1;
   }

   public int getWindowAnimations() {
      return 0;
   }

   protected boolean hasSupportDividerBeforeChildAt(int var1) {
      View var4 = this.getChildAt(var1 - 1);
      View var5 = this.getChildAt(var1);
      boolean var3 = false;
      boolean var2 = var3;
      if (var1 < this.getChildCount()) {
         var2 = var3;
         if (var4 instanceof ActionMenuView.ActionMenuChildView) {
            var2 = false | ((ActionMenuView.ActionMenuChildView)var4).needsDividerAfter();
         }
      }

      var3 = var2;
      if (var1 > 0) {
         var3 = var2;
         if (var5 instanceof ActionMenuView.ActionMenuChildView) {
            var3 = var2 | ((ActionMenuView.ActionMenuChildView)var5).needsDividerBefore();
         }
      }

      return var3;
   }

   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   public boolean isExpandedFormat() {
      return this.mFormatItems;
   }

   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onConfigurationChanged(Configuration var1) {
      if (VERSION.SDK_INT >= 8) {
         super.onConfigurationChanged(var1);
      }

      this.mPresenter.updateMenuView(false);
      if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowing()) {
         this.mPresenter.hideOverflowMenu();
         this.mPresenter.showOverflowMenu();
      }

   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mPresenter.dismissPopupMenus();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (!this.mFormatItems) {
         super.onLayout(var1, var2, var3, var4, var5);
      } else {
         int var11 = this.getChildCount();
         int var10 = (var3 + var5) / 2;
         int var12 = this.getSupportDividerWidth();
         int var7 = 0;
         var5 = 0;
         var3 = var4 - var2 - this.getPaddingRight() - this.getPaddingLeft();
         boolean var8 = false;

         int var6;
         View var15;
         ActionMenuView.LayoutParams var16;
         for(var6 = 0; var6 < var11; ++var6) {
            var15 = this.getChildAt(var6);
            if (var15.getVisibility() != 8) {
               var16 = (ActionMenuView.LayoutParams)var15.getLayoutParams();
               int var9;
               if (var16.isOverflowButton) {
                  var9 = var15.getMeasuredWidth();
                  int var18 = var9;
                  if (this.hasSupportDividerBeforeChildAt(var6)) {
                     var18 = var9 + var12;
                  }

                  var9 = var15.getMeasuredHeight();
                  int var13 = this.getWidth() - this.getPaddingRight() - var16.rightMargin;
                  int var14 = var10 - var9 / 2;
                  var15.layout(var13 - var18, var14, var13, var14 + var9);
                  var3 -= var18;
                  var8 = true;
               } else {
                  var9 = var15.getMeasuredWidth() + var16.leftMargin + var16.rightMargin;
                  var7 += var9;
                  var9 = var3 - var9;
                  var3 = var7;
                  if (this.hasSupportDividerBeforeChildAt(var6)) {
                     var3 = var7 + var12;
                  }

                  ++var5;
                  var7 = var3;
                  var3 = var9;
               }
            }
         }

         if (var11 == 1 && !var8) {
            var15 = this.getChildAt(0);
            var3 = var15.getMeasuredWidth();
            var5 = var15.getMeasuredHeight();
            var2 = (var4 - var2) / 2 - var3 / 2;
            var4 = var10 - var5 / 2;
            var15.layout(var2, var4, var2 + var3, var4 + var5);
            return;
         }

         byte var17;
         if (var8) {
            var17 = 0;
         } else {
            var17 = 1;
         }

         var2 = var5 - var17;
         if (var2 > 0) {
            var2 = var3 / var2;
         } else {
            var2 = 0;
         }

         var5 = Math.max(0, var2);
         var3 = this.getPaddingLeft();

         for(var2 = 0; var2 < var11; var3 = var4) {
            var15 = this.getChildAt(var2);
            var16 = (ActionMenuView.LayoutParams)var15.getLayoutParams();
            var4 = var3;
            if (var15.getVisibility() != 8) {
               if (var16.isOverflowButton) {
                  var4 = var3;
               } else {
                  var3 += var16.leftMargin;
                  var4 = var15.getMeasuredWidth();
                  var6 = var15.getMeasuredHeight();
                  var7 = var10 - var6 / 2;
                  var15.layout(var3, var7, var3 + var4, var7 + var6);
                  var4 = var3 + var16.rightMargin + var4 + var5;
               }
            }

            ++var2;
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      boolean var6 = this.mFormatItems;
      boolean var5;
      if (MeasureSpec.getMode(var1) == 1073741824) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mFormatItems = var5;
      if (var6 != this.mFormatItems) {
         this.mFormatItemsWidth = 0;
      }

      int var3 = MeasureSpec.getMode(var1);
      if (this.mFormatItems && this.mMenu != null && var3 != this.mFormatItemsWidth) {
         this.mFormatItemsWidth = var3;
         this.mMenu.onItemsChanged(true);
      }

      if (this.mFormatItems) {
         this.onMeasureExactFormat(var1, var2);
      } else {
         int var4 = this.getChildCount();

         for(var3 = 0; var3 < var4; ++var3) {
            ActionMenuView.LayoutParams var7 = (ActionMenuView.LayoutParams)this.getChildAt(var3).getLayoutParams();
            var7.rightMargin = 0;
            var7.leftMargin = 0;
         }

         super.onMeasure(var1, var2);
      }
   }

   public void setOverflowReserved(boolean var1) {
      this.mReserveOverflow = var1;
   }

   public void setPresenter(ActionMenuPresenter var1) {
      this.mPresenter = var1;
   }

   public interface ActionMenuChildView {
      boolean needsDividerAfter();

      boolean needsDividerBefore();
   }

   public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
      @ExportedProperty
      public int cellsUsed;
      @ExportedProperty
      public boolean expandable;
      public boolean expanded;
      @ExportedProperty
      public int extraPixels;
      @ExportedProperty
      public boolean isOverflowButton;
      @ExportedProperty
      public boolean preventEdgeOffset;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.isOverflowButton = false;
      }

      public LayoutParams(int var1, int var2, boolean var3) {
         super(var1, var2);
         this.isOverflowButton = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(ActionMenuView.LayoutParams var1) {
         super(var1);
         this.isOverflowButton = var1.isOverflowButton;
      }
   }
}
